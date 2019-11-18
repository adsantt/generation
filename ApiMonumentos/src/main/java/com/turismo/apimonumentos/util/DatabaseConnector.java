package com.turismo.apimonumentos.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DatabaseConnector {

	//Conexion
	private static Session session;
	
	public static Session getConnection() {
		session= null;
		StandardServiceRegistry registry= 
				new StandardServiceRegistryBuilder()
				.configure()
				.build();
		
		SessionFactory sessionF= new MetadataSources(registry)
				.buildMetadata().buildSessionFactory();
		
		session=sessionF.openSession();
		
		System.out.println("Se inicio la sesion Hibernate");
		return session;
	}
	
	public static void closeConnection() throws HibernateException {
		if (session!=null) {
			session.close();
			System.out.println("Se cerro la sesion Hibernate");
		}
	}
	
}
