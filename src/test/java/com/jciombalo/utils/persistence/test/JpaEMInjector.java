package com.jciombalo.utils.persistence.test;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class JpaEMInjector {
	protected static EntityManagerFactory emf;
	protected static EntityManager em;

	@BeforeClass
	public static void init() throws FileNotFoundException, SQLException {
		emf = Persistence.createEntityManagerFactory("persistence-test");
		em = emf.createEntityManager();
	}
	
	@AfterClass
	public static void tearDown() {
		em.clear();
		em.close();
		emf.close();
	}
}
