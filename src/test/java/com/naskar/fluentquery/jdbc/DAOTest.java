package com.naskar.fluentquery.jdbc;

//import static com.mongodb.client.model.Filters.and;
//import static com.mongodb.client.model.Filters.gt;
//import static com.mongodb.client.model.Filters.lt;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.naskar.fluentquery.domain.Customer;
import com.naskar.fluentquery.mongodb.DatabaseProvider;
import com.naskar.fluentquery.mongodb.impl.DAOImpl;

public class DAOTest {
	
	private MongoClient client;
	
	private DAOImpl dao;
	
	@Before
	public void setup() throws Exception {
		
		client = new MongoClient(
		  Arrays.asList(new ServerAddress("localhost", 27017)));

		MongoDatabase database = client.getDatabase("testdb");
		
		dao = new DAOImpl(new DatabaseProvider() {
			
			@Override
			public MongoDatabase getDatabase() {
				return database;
			}
			
		});
	}
	
	@After
	public void cleanup() throws Exception {
		client.close();
	}
	
	@Test
	public void testSuccessInsertQuery() {
		dao.execute(dao.insert(Customer.class)
				.value(i -> i.getId()).set(1L)
				.value(i -> i.getName()).set("teste1"));
		
//		List<Customer> actual = dao.list(dao.query(Customer.class)
//			.where(i -> i.getName()).like("t%"));
//		
//		Assert.assertEquals(actual.size(), 2);
//		
//		Assert.assertEquals((long)actual.get(0).getId(), 1L);
//		Assert.assertEquals((long)actual.get(1).getId(), 2L);
//		
//		Assert.assertEquals(actual.get(0).getName(), "teste1");
//		Assert.assertEquals(actual.get(1).getName(), "teste2");
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
