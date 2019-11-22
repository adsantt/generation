package com.turismo.apimonumentos.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class DatabaseConnectorTest {
	
	private static Session session;

	@Test
	public void getSession() {
		session=null;
		StandardServiceRegistry registry=
				new StandardServiceRegistryBuilder()
				.configure()
				.build();
		
		SessionFactory sessionF= new MetadataSources(registry)
				.buildMetadata().buildSessionFactory();
		
		session=sessionF.openSession();
		assertTrue(session.isConnected());
	}
	
	@Test
	public void closeSession() {
		session.close();
		assertFalse(session.isConnected());
	}
	
}
