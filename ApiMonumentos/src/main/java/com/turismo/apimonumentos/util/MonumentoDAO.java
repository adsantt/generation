package com.turismo.apimonumentos.util;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.HibernateException;
import org.hibernate.QueryException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.turismo.apimonumentos.models.Monumento;

public class MonumentoDAO implements DAO<Monumento> {

	private Session session = null;
	private Transaction tx = null;

	public Session getConnection() throws HibernateException {
		session = DatabaseConnector.getConnection();
		return session;
	}

	public void closeConnection() throws HibernateException {
		DatabaseConnector.closeConnection();
	}

	public Session getConnectionWTransaction() throws HibernateException {
		session = DatabaseConnector.getConnection();
		tx = session.beginTransaction();
		return session;
	}

	public void closeConnectionWTransaction() throws RollbackException, HibernateException {
		tx.commit();
		DatabaseConnector.closeConnection();
	}

	public void rollback() throws PersistenceException{
		tx.rollback();
	}
	
	public String validateBean(Monumento m) {
		Validator validator=null;
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator=factory.getValidator();
		String message=null;
		Set<ConstraintViolation<Monumento>> violations=validator.validate(m);
		if (!violations.isEmpty()) {
			message="";
		}
		for (ConstraintViolation<Monumento> violation:violations) {
			message+=violation.getMessage();
		}
		return message;
	}
	
	@Override
	public boolean create(Monumento m) {
		m.setFecha_registro(new Date());
		
		Monumento monumentoDB=null;
		
		monumentoDB=(Monumento) session.createQuery("FROM Monumento m "
				+ "WHERE m.nombre_construccion LIKE :nombre")
				.setParameter("nombre", '%'+ m.getNombre_construccion()+'%')
				.getSingleResult();
		if (monumentoDB==null) {
			session.save(m);
		}else if (monumentoDB!=null && monumentoDB.isEliminado()) {
			m.setId(monumentoDB.getId());
			monumentoDB.actualizar(m);
			session.update(monumentoDB);
		} else if (monumentoDB!=null && !monumentoDB.isEliminado()) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Monumento> getAll() throws QueryException {
		List<Monumento> listaMonumentos = null;
		listaMonumentos = session.createQuery("FROM Monumento m "
				+ "WHERE m.eliminado=false")
				.list();
		return listaMonumentos;
	}

	@Override
	public Monumento getByName(String nombre) {
		Monumento monumentoDB = null;
		monumentoDB = (Monumento) session
				.createQuery("FROM Monumento m " 
		+ "WHERE m.eliminado=false AND m.nombre_construccion LIKE :nombre")
				.setParameter("nombre", '%' + nombre + '%')
				.getSingleResult();
		monumentoDB.setContador_usuarios(1+monumentoDB.getContador_usuarios());
		session.update(monumentoDB);
		return monumentoDB;
	}

	@Override
	public void update(Monumento m) {

		Monumento monumentoDB = null;
		monumentoDB = session.find(Monumento.class, m.getId());
		if (monumentoDB != null) {
			monumentoDB.actualizar(m);
			session.update(monumentoDB);
		} else {
			throw new NotFoundException();
		}
	}

	@Override
	public void delete(int id) throws NotFoundException {

		Monumento monumentoDB = null;
		monumentoDB = session.find(Monumento.class, id);
		if (monumentoDB != null) {
			monumentoDB.setEliminado(true);
			session.update(monumentoDB);
		} else {
			throw new NotFoundException();
		}

	}
	
	@SuppressWarnings("unchecked")
	public List<String> getNames(String name){
		List<String> nameList=null;
		
		nameList=session.createQuery("SELECT m.nombre_construccion "
				+ "FROM Monumento m "
				+ "WHERE m.eliminado=false AND m.nombre_construccion "
				+ "LIKE :name")
				.setParameter("name", '%'+name+'%')
				.list();
		
		return nameList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Monumento> getByPopularity(int n){
		List<Monumento> monumentos=null;
		monumentos=session.createQuery("FROM Monumento m "
				+ "WHERE m.eliminado=false "
				+ "ORDER BY m.contador_usuarios DESC")
				.setMaxResults(n)
				.list();
		return monumentos;
	}
	
	public Monumento getByPhoto(String json) {
		
		Client cliente=ClientBuilder.newClient();
		WebTarget target=cliente.target("http://localhost:4000/api/Monumento");
		
		Invocation.Builder request=target.request(MediaType.APPLICATION_JSON);
		
		Response rs=request.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(json, MediaType.APPLICATION_JSON));
		@SuppressWarnings("unchecked")
		Map<String, Object> jsonResponse=rs.readEntity(Map.class);
		String nombre=(String) jsonResponse.get("name");
		Monumento monumentoDB=getByName(nombre);
		
		return monumentoDB;
	}

}
