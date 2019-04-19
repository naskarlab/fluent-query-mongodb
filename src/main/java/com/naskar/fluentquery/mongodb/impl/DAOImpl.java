package com.naskar.fluentquery.mongodb.impl;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.naskar.fluentquery.InsertBuilder;
import com.naskar.fluentquery.Into;
import com.naskar.fluentquery.Query;
import com.naskar.fluentquery.QueryBuilder;
import com.naskar.fluentquery.mongodb.DAO;
import com.naskar.fluentquery.mongodb.DatabaseProvider;
import com.naskar.fluentquery.mongodb.DocumentHandler;
import com.naskar.fluentquery.mongodb.converters.MongoInsertInto;
import com.naskar.fluentquery.mongodb.converters.MongoQuery;
import com.naskar.fluentquery.mongodb.converters.MongoResult;
import com.naskar.fluentquery.mongodb.impl.ClassListHandler.Converter;

public class DAOImpl implements DAO {
	
//	private static final Logger logger = Logger.getLogger(DAOImpl.class.getName());
	
	private DatabaseProvider databaseProvider;
	
	private MongoQuery mongoQuery;
	private QueryBuilder queryBuilder;
//	
	private MongoInsertInto insert;
	private InsertBuilder insertBuilder;
//	
//	private NativeSQLUpdate updateSQL;
//	private UpdateBuilder updateBuilder;
//	
//	private NativeSQLDelete deleteSQL;
//	private DeleteBuilder deleteBuilder;
//	
//	private BinderSQLBuilder binderBuilder;
	
	public DAOImpl(DatabaseProvider databaseProvider) {
		this.databaseProvider = databaseProvider;
		
		this.mongoQuery = new MongoQuery();
		this.queryBuilder = new QueryBuilder();
		
		this.insert = new MongoInsertInto();
		this.insertBuilder = new InsertBuilder();
		
//		this.updateSQL = new NativeSQLUpdate();
//		this.updateSQL.setConvention(mappings);
//		this.updateBuilder = new UpdateBuilder();
//		
//		this.deleteSQL = new NativeSQLDelete();
//		this.deleteSQL.setConvention(mappings);
//		this.deleteBuilder = new DeleteBuilder();
//		
//		this.binderBuilder = new BinderSQLBuilder();
	}
	
	@Override
	public <T> Query<T> query(Class<T> clazz) {
		return queryBuilder.from(clazz);
	}
//	
//	@Override
//	public <T> T single(Query<T> query) {
//		T o = null;
//		
//		List<T> l = list(query);
//		if(l != null && !l.isEmpty()) {
//			o = l.get(0);
//		}
//		
//		return o;
//	}
//	
	@Override
	public <T> List<T> list(Query<T> query) {
		// TODO: performance cache
		ClassListHandler<T> handler = new ClassListHandler<T>(query.getClazz());
		handler.addAlias("_id", "id");
		handler.addConverter("id", new Converter() {
			
			@Override
			public Object to(Object value) {
				Object r = value;
				if(r instanceof ObjectId) {
					r = ((ObjectId)value).toString();
				}
				return r;
			}
		});
		
		list(query, handler);
		return handler.getList();
	}
		
	private <T> void list(Query<T> query, DocumentHandler handler) {
		MongoResult result = query.to(mongoQuery);
		
		MongoCollection<Document> collection = this.databaseProvider.getDatabase()
			.getCollection(result.getCollection());
		
		try (MongoCursor<Document> cursor = collection.find(result.getObject()).iterator()) {
			forEachHandler(handler, cursor);
		}
	}
	
	private void forEachHandler(DocumentHandler handler, MongoCursor<Document> cursor) {
		while(cursor.hasNext()) {
			if(!handler.next(cursor.next())) {
				break;
			}
		}
	}
	
	@Override
	public <T> Into<T> insert(Class<T> clazz) {
		return insertBuilder.into(clazz);
	}
//	
//	@Override
//	public <T> Update<T> update(Class<T> clazz) {
//		return updateBuilder.entity(clazz);
//	}
//	
//	@Override
//	public <T> Delete<T> delete(Class<T> clazz) {
//		return deleteBuilder.entity(clazz);
//	}
//	
//	@Override
//	public <R> BinderSQL<R> binder(Class<R> clazz) {	
//		return binderBuilder.from(clazz);
//	}
//	
//	@Override
//	public <R, T> void configure(BinderSQL<R> binder, Into<T> into) {
//		binder.configure(into.to(insertSQL));
//	}
//	
//	@Override
//	public <T, R> List<R> list(Query<T> query, Class<R> clazz) {
//		ClassListHandler<R> handler = new ClassListHandler<R>(clazz);
//		list(query, handler);
//		return handler.getList();
//	}
//	
//	@Override
//	public <T> void list(Query<T> query, ResultSetHandler handler) {
//		NativeSQLResult result = query.to(nativeSQL);
//		list(result.sqlValues(), result.values(), handler);
//	}
//	
//	@Override
//	public void list(String sql, List<Object> params, ResultSetHandler handler) {
//		/*
//		PreparedStatement st = null;
//		ResultSet rs = null;
//		try {
//			st = connectionProvider.getConnection()
//					.prepareStatement(sql);
//			
//			addParams(st, params);
//			
//			logger.info("SQL:" + sql + "\nParams:" + params);
//			
//			rs = st.executeQuery();
//			
//			forEachHandler(handler, rs);
//			
//		} catch(Exception e) {
//			logger.log(Level.SEVERE, "SQL:" + sql + "\nParams:" + params, e);
//			throw new RuntimeException(e);
//			
//		} finally {
//			
//			if(rs != null) {
//				try {
//					rs.close();
//				} catch(Exception e) {
//					logger.log(Level.SEVERE, "Error on close ResultSet.", e);
//				}
//			}
//			if(st != null) {
//				try {
//					st.close();
//				} catch(Exception e) {
//					logger.log(Level.SEVERE, "Error on close Statement.", e);
//				}
//			}
//		}
//		*/
//	}
//
//	private void addParams(PreparedStatement st, List<Object> params) throws SQLException {
//		if(params != null) {
//			for(int i = 0; i < params.size(); i++) {
//				Object o = params.get(i);
//				if(o instanceof Date) {
//					st.setTimestamp(i + 1, new java.sql.Timestamp(((java.util.Date)o).getTime()));
//				} else if(o instanceof File) {
//					try {
//						st.setBinaryStream(i + 1, new FileInputStream((File)o));
//					} catch(Exception e) {
//						throw new RuntimeException(e);
//					}
//				} else if(o instanceof InputStream) {
//					try {
//						st.setBinaryStream(i + 1, (InputStream)o);
//					} catch(Exception e) {
//						throw new RuntimeException(e);
//					}				
//				} else {
//					st.setObject(i + 1, o);
//				}
//			}
//		}
//	}
//	
	@Override
	public <T> void execute(Into<T> into) {
		MongoResult result = into.to(insert);
		result.getObject().remove("id");
		this.databaseProvider.getDatabase()
			.getCollection(result.getCollection())
			.insertOne(new Document(result.getObject()));
	}
//	
//	@Override
//	public <T> void execute(Into<T> into, ResultSetHandler handlerKeys) {
//		NativeSQLResult result = into.to(insertSQL);
//		execute(result.sqlValues(), result.values(), handlerKeys);
//	}
//	
//	@Override
//	public <T> void execute(Update<T> update) {
//		NativeSQLResult result = update.to(updateSQL);
//		execute(result.sqlValues(), result.values());
//	}
//	
//	@Override
//	public <T> void execute(Delete<T> delete) {
//		NativeSQLResult result = delete.to(deleteSQL);
//		execute(result.sqlValues(), result.values());
//	}
//	
//	@Override
//	public <R> void execute(BinderSQL<R> binder, R r) {
//		NativeSQLResult result = binder.bind(r);
//		execute(result.sqlValues(), result.values());
//	}
//	
//	@Override
//	public void execute(String sql) {
//		this.execute(sql, null, null);
//	}
//	
//	@Override
//	public void execute(String sql, List<Object> params) {
//		this.execute(sql, params, null);
//	}
//	
//	@Override
//	public void execute(String sql, List<Object> params, ResultSetHandler handlerKeys) {
//		/*
//		PreparedStatement st = null;
//		ResultSet rs = null;
//		try {
//			st = connectionProvider.getConnection()
//					.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//			
//			addParams(st, params);
//			
//			logger.info("SQL:" + sql + "\nParams:" + params);
//			
//			int count = st.executeUpdate();
//			
//			logger.info("SQL: Count: " + count);
//
//			if(handlerKeys != null) {
//				rs = st.getGeneratedKeys();
//				if(rs != null) {
//					forEachHandler(handlerKeys, rs);
//				}
//			}
//			
//		} catch(Exception e) {
//			logger.log(Level.SEVERE, "SQL:" + sql + " params: " + params, e);
//			throw new RuntimeException(e);
//			
//		} finally {
//			
//			if(rs != null) {
//				try {
//					rs.close();
//				} catch(Exception e) {
//					logger.log(Level.SEVERE, "Error on close ResultSet.", e);
//				}
//			}
//			
//			if(st != null) {
//				try {
//					st.close();
//				} catch(Exception e) {
//					logger.log(Level.SEVERE, "Error on close Statement.", e);
//				}
//			}
//		}
//		*/
//	}
//	
//	private void forEachHandler(ResultSetHandler handler, ResultSet rs) throws SQLException {
//		while(rs.next()) {
//			if(!handler.next(rs)) {
//				break;
//			}
//		}
//	}
	
}
