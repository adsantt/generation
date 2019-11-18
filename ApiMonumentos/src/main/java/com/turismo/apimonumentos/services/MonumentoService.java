package com.turismo.apimonumentos.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.HibernateException;
import org.hibernate.QueryException;

import com.turismo.apimonumentos.models.Monumento;
import com.turismo.apimonumentos.util.MonumentoDAO;

@Path("monumentos")
public class MonumentoService {

	private MonumentoDAO monumentoDAO = new MonumentoDAO();
	private Map<String, Object> response;
	private String mensaje;
	private Status status;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMonumentos() {

		List<Monumento> monumentos = null;
		mensaje = null;
		status = Status.INTERNAL_SERVER_ERROR;
		response = null;

		try {
			monumentoDAO.getConnection();
			monumentos = monumentoDAO.getAll();
			monumentoDAO.closeConnection();
			if (monumentos != null) {
				mensaje = "Se muestran todos los monumentos";
				status = Status.OK;
			}
		} catch (QueryException e) {
			e.printStackTrace();
			mensaje = "Error al crear la peticion a la base de datos";
		} catch (HibernateException e) {
			e.printStackTrace();
			mensaje = "Error en la conexion con la base de datos";
		} catch (Exception e) {
			e.printStackTrace();
			mensaje = "Error";
		}

		response = new HashMap<>();
		response.put("data", monumentos);
		response.put("mensaje", mensaje);

		return Response.status(status).entity(response).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response guardarMonumento(Monumento m) {

		response = null;
		mensaje = null;
		Status status = Status.INTERNAL_SERVER_ERROR;

		mensaje = monumentoDAO.validateBean(m);

		if (mensaje == null) {
			try {
				monumentoDAO.getConnectionWTransaction();
				boolean isSuccessful = monumentoDAO.create(m);
				monumentoDAO.closeConnectionWTransaction();
				if (isSuccessful) {
					status = Status.OK;
					mensaje = "Monumento guardado correctamente";
				} else if (!isSuccessful) {
					status = Status.CONFLICT;
					mensaje = "El monumento ya existe, ingresa uno nuevo";
				}
			} catch (RollbackException e) {
				e.printStackTrace();
				try {
					monumentoDAO.rollback();
				} catch (PersistenceException e2) {
					e2.printStackTrace();
					mensaje = "Error al realizar el rollback";
				}
				mensaje = "Error en la transaccion";
			} catch (HibernateException e) {
				e.printStackTrace();
				mensaje = "Error en la conexion con la base de datos";
			} catch (Exception e) {
				e.printStackTrace();
				mensaje = "Error";
			}
		}

		response = new HashMap<>();
		response.put("data", m);
		response.put("mensaje", mensaje);

		return Response.status(status).entity(response).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response actualizarMonumento(Monumento m) {

		response = null;
		mensaje = null;
		status = Status.INTERNAL_SERVER_ERROR;

		try {
			monumentoDAO.getConnectionWTransaction();
			monumentoDAO.update(m);
			monumentoDAO.closeConnectionWTransaction();
			status = Status.OK;
			mensaje = "El monumento se actualizo correctamente";
		} catch (RollbackException e) {
			e.printStackTrace();
			try {
				monumentoDAO.rollback();
			} catch (PersistenceException e2) {
				e.printStackTrace();
				mensaje = "Error al realizar el rollback";
			}
			mensaje = "Error al realizar la transaccion";
		} catch (HibernateException e) {
			e.printStackTrace();
			mensaje = "Error en la conexion con la base de datos";
		} catch (Exception e) {
			e.printStackTrace();
			mensaje = "Error";
		}

		response = new HashMap<>();
		response.put("data", m);
		response.put("mensaje", mensaje);

		return Response.status(status).entity(response).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response eliminarMonumento(Monumento m) {
		int id = m.getId();
		response = null;
		mensaje = null;
		status = Status.INTERNAL_SERVER_ERROR;

		try {
			monumentoDAO.getConnectionWTransaction();
			monumentoDAO.delete(id);
			monumentoDAO.closeConnectionWTransaction();
			status = Status.OK;
			mensaje = "El monumento " + m.getNombre_construccion() + " fue correctamente" + " eliminado.";
		} catch (NotFoundException e) {
			e.printStackTrace();
			mensaje = "No se encontro el monumento a eliminar";
		} catch (RollbackException e) {
			e.printStackTrace();
			try {
				monumentoDAO.rollback();
			} catch (PersistenceException e2) {
				e2.printStackTrace();
				mensaje = "Error al hacer rollback";
			}
			mensaje = "Error al realizar la transaccion";
		} catch (HibernateException e) {
			e.printStackTrace();
			mensaje = "Error en la conexion con la base de datos";
		} catch (Exception e) {
			e.printStackTrace();
			mensaje = "Error";
		}

		response = new HashMap<>();
		response.put("mensaje", mensaje);

		return Response.status(status).entity(response).build();
	}

	@GET
	@Path("buscar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNombres(@QueryParam("nombre") String nombre) {
		status = Status.INTERNAL_SERVER_ERROR;
		mensaje = null;
		response = null;

		List<String> listaNombres = null;

		try {
			monumentoDAO.getConnection();
			listaNombres = monumentoDAO.getNames(nombre);
			monumentoDAO.closeConnection();
			if (listaNombres.isEmpty()) {
				mensaje = "No se encontraron monumentos con ese nombre";
				status = Status.NOT_FOUND;
			}
			mensaje = "Estos son los monumentos encontrados";
			status = Status.OK;
		} catch (HibernateException e) {
			e.printStackTrace();
			mensaje = "Error en la conexion con la base de datos";
		} catch (Exception e) {
			e.printStackTrace();
			mensaje = "Error";
		}

		response = new HashMap<>();
		response.put("data", listaNombres);
		response.put("mensaje", mensaje);

		return Response.status(status).entity(response).build();
	}

	@GET
	@Path("{nombre}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMonumentoByName(@PathParam("nombre") String nombre) {

		response = null;
		mensaje = null;
		status = Status.INTERNAL_SERVER_ERROR;
		Monumento monumento = null;

		try {
			monumentoDAO.getConnectionWTransaction();
			monumento = monumentoDAO.getByName(nombre);
			monumentoDAO.closeConnectionWTransaction();
			if (monumento == null) {
				status = Status.NOT_FOUND;
				mensaje = "No se encontro el monumento";
			} else {
				mensaje = "Se muestra monumento" + nombre;
				status = Status.OK;
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			mensaje = "Error en la conexion con la base de datos";
		} catch (Exception e) {
			e.printStackTrace();
			mensaje = "Error";
		}

		response = new HashMap<>();
		response.put("data", monumento);
		response.put("mensaje", mensaje);

		return Response.status(status).entity(response).build();
	}

	@GET
	@Path("populares/{numero}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMonumentosByPopularity(@PathParam("numero") int n) {

		response = null;
		status = Status.INTERNAL_SERVER_ERROR;
		mensaje = null;

		List<Monumento> listaMonumentos = null;

		try {
			monumentoDAO.getConnection();
			listaMonumentos = monumentoDAO.getByPopularity(n);
			monumentoDAO.closeConnection();
			if (listaMonumentos != null) {
				status = Status.OK;
				mensaje = "Se muestran los monumentos mas populares";
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			mensaje = "Error en la conexion con la base de datos";
		} catch (Exception e) {
			e.printStackTrace();
			mensaje = "Error";
		}

		response = new HashMap<>();
		response.put("data", listaMonumentos);
		response.put("mensaje", mensaje);

		return Response.status(status).entity(response).build();
	}

	@POST
	@Path("foto")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMonumentoPorFoto(String jsonImagen) {
		mensaje = null;
		response = null;
		status = Status.INTERNAL_SERVER_ERROR;

		Monumento monumento = null;

		try {
			monumentoDAO.getConnectionWTransaction();
			monumento = monumentoDAO.getByPhoto(jsonImagen);
			monumentoDAO.closeConnectionWTransaction();
			if (monumento != null) {
				mensaje = "Se muestra el objeto detectado";
				status = Status.OK;
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			mensaje = "Error en la conexion con la base de datos";
		} catch (Exception e) {
			e.printStackTrace();
			mensaje = "Error";
		}
		response = new HashMap<>();
		response.put("data", monumento);
		response.put("mensaje", mensaje);

		return Response.status(status).entity(response).build();
	}

}
