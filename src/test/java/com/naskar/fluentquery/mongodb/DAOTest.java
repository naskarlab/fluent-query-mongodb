package com.naskar.fluentquery.mongodb;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.naskar.fluentquery.domain.Customer;
import com.naskar.fluentquery.mongodb.DatabaseProvider;
import com.naskar.fluentquery.mongodb.InsertBinder;
import com.naskar.fluentquery.mongodb.impl.DAOImpl;

public class DAOTest {
	
	private MongoClient client;
	
	private DAOImpl dao;
	
	@Before
	public void setup() throws Exception {
				
		client = new MongoClient(
			Arrays.asList(new ServerAddress("localhost", 27017)));

		MongoDatabase database = client.getDatabase("testdb");
		
		// clean
		database.getCollection(Customer.class.getSimpleName())
			.deleteMany(new Document());
		
		DatabaseProvider databaseProvider = new DatabaseProvider() {
			@Override
			public MongoDatabase getDatabase() {
				return database;
			}
		}; 
		
		dao = new DAOImpl(databaseProvider);
	}
	
	@After
	public void cleanup() throws Exception {
		client.close();
	}
	
	@Test
	public void testSuccessInsert() {
		// Arrange
		dao.execute(dao.insert(Customer.class)
				.value(x -> x.getId()).set(UUID.randomUUID().toString())
				.value(i -> i.getName()).set("teste1"));
		
		dao.execute(dao.insert(Customer.class)
				.value(x -> x.getId()).set(UUID.randomUUID().toString())
				.value(i -> i.getName()).set("teste2"));
		
		// Act
		List<Customer> actual = dao.list(dao.query(Customer.class)
			.where(i -> i.getName()).like("t%"));
		
		// Assert
		Assert.assertEquals(2, actual.size());
		
		Assert.assertNotNull(actual.get(0).getId());
		Assert.assertNotNull(actual.get(1).getId());
		
		Assert.assertEquals("teste1", actual.get(0).getName());
		Assert.assertEquals("teste2", actual.get(1).getName());
	}
	
	@Test
	public void testSuccessUpdate() {
		// Arrange
		String id = dao.execute(dao.insert(Customer.class)
			.value(x -> x.getId()).set(UUID.randomUUID().toString())
			.value(i -> i.getName()).set("teste1")
		);
		
		// Act
		dao.execute(
			dao.update(Customer.class)
				.value(i -> i.getName()).set("teste1-updated")
				.where(i -> i.getId()).eq(id)
		);
		
		Customer actual = dao.single(dao.query(Customer.class)
			.where(i -> i.getId()).eq(id)
		);
		
		// Assert
		Assert.assertNotNull(actual);
		Assert.assertEquals("teste1-updated", actual.getName());
	}
	
	@Test
	public void testSuccessDelete() {
		// Arrange
		String id = dao.execute(dao.insert(Customer.class)
			.value(x -> x.getId()).set(UUID.randomUUID().toString())
			.value(i -> i.getName()).set("teste1")
		);
		
		List<Customer> l = dao.list(dao.query(Customer.class)
			.where(i -> i.getId()).eq(id)
		);
		
		// Act
		dao.execute(dao.delete(Customer.class)
			.where(i -> i.getId()).eq(id)
		);
		
		// Assert
		List<Customer> actual = dao.list(dao.query(Customer.class)
			.where(i -> i.getId()).eq(id)
		);
		
		Assert.assertEquals(1, l.size());
		Assert.assertEquals(0, actual.size());
	}

	@Test
	public void testSuccessBinderInsert() {
		InsertBinder<Customer> binder = dao.binderInsert(Customer.class);
		
		dao.configure(binder, dao.insert(Customer.class)
			.value(i -> i.getId()).set(binder.get(i -> i.getId()))
			.value(i -> i.getRegionCode()).set(binder.get(i -> i.getRegionCode()))
			.value(i -> i.getBalance()).set(binder.get(i -> i.getBalance())));
		
		dao.execute(binder, new Customer() {{ setId(UUID.randomUUID().toString()); setBalance(100.0); }});
		dao.execute(binder, new Customer() {{ setId(UUID.randomUUID().toString()); setBalance(200.0); }});
		dao.execute(binder, new Customer() {{ setId(UUID.randomUUID().toString()); setBalance(300.0); }});
		
		List<Customer> actual = dao.list(dao.query(Customer.class));
		
		Assert.assertEquals(3, actual.size());
		
		Assert.assertEquals(100, actual.get(0).getBalance().intValue());
		Assert.assertEquals(200, actual.get(1).getBalance().intValue());
		Assert.assertEquals(300, actual.get(2).getBalance().intValue());
	}
	
//	@Test
//	public void testSuccessClassResult() {
//		BinderSQL<Customer> binder = dao.binder(Customer.class);
//		
//		dao.configure(binder, dao.insert(Customer.class)
//				.value(i -> i.getId()).set(binder.get(i -> i.getId()))
//				.value(i -> i.getRegionCode()).set(binder.get(i -> i.getRegionCode()))
//				.value(i -> i.getBalance()).set(binder.get(i -> i.getBalance())));
//		
//		dao.execute(binder, new Customer() {{ setId(1L); setRegionCode(1L); setBalance(100.0); }});
//		dao.execute(binder, new Customer() {{ setId(2L); setRegionCode(1L); setBalance(100.0); }});
//		dao.execute(binder, new Customer() {{ setId(3L); setRegionCode(2L); setBalance(300.0); }});
//		
//		List<RegionSummary> actual = dao.list(dao.query(Customer.class)
//			.select(i -> i.getRegionCode(), s -> s.func(c -> c, "region"))
//			.select(x -> x.getBalance(), s -> s.func(c -> "sum(" + c + ")", "balance"))
//			.groupBy(i -> i.getRegionCode()), 
//			RegionSummary.class);
//		
//		Assert.assertEquals(actual.size(), 2);
//		
//		Assert.assertEquals((long)actual.get(0).getRegion(), 1L);
//		Assert.assertEquals((long)actual.get(1).getRegion(), 2L);
//		
//		Assert.assertEquals(actual.get(0).getBalance().intValue(), 200);
//		Assert.assertEquals(actual.get(1).getBalance().intValue(), 300);
//	}

}
