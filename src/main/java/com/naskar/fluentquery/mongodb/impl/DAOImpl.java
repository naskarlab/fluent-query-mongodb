package com.naskar.fluentquery.mongodb.impl;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.naskar.fluentquery.Delete;
import com.naskar.fluentquery.DeleteBuilder;
import com.naskar.fluentquery.InsertBuilder;
import com.naskar.fluentquery.Into;
import com.naskar.fluentquery.Query;
import com.naskar.fluentquery.QueryBuilder;
import com.naskar.fluentquery.Update;
import com.naskar.fluentquery.UpdateBuilder;
import com.naskar.fluentquery.mongodb.DAO;
import com.naskar.fluentquery.mongodb.DatabaseProvider;
import com.naskar.fluentquery.mongodb.DocumentHandler;
import com.naskar.fluentquery.mongodb.InsertBinder;
import com.naskar.fluentquery.mongodb.binders.BinderInsertBuilder;
import com.naskar.fluentquery.mongodb.converters.MongoDelete;
import com.naskar.fluentquery.mongodb.converters.MongoDeleteResult;
import com.naskar.fluentquery.mongodb.converters.MongoInsertInto;
import com.naskar.fluentquery.mongodb.converters.MongoInsertResult;
import com.naskar.fluentquery.mongodb.converters.MongoQuery;
import com.naskar.fluentquery.mongodb.converters.MongoQueryResult;
import com.naskar.fluentquery.mongodb.converters.MongoUpdate;
import com.naskar.fluentquery.mongodb.converters.MongoUpdateResult;

public class DAOImpl implements DAO {
	
	private static final Logger logger = Logger.getLogger(DAOImpl.class.getName());
	
	private DatabaseProvider databaseProvider;
	private ObjectIdConverter objectIdConverter;
	
	private MongoQuery mongoQuery;
	private QueryBuilder queryBuilder;

	private MongoInsertInto mongoInsert;
	private InsertBuilder insertBuilder;
	
	private MongoUpdate mongoUpdate;
	private UpdateBuilder updateBuilder;
	
	private MongoDelete mongoDelete;
	private DeleteBuilder deleteBuilder;
	
	private BinderInsertBuilder binderInsertBuilder;
	
	public DAOImpl(DatabaseProvider databaseProvider) {
		this.databaseProvider = databaseProvider;
		this.objectIdConverter = new ObjectIdConverter();
		
		this.mongoQuery = new MongoQuery();
		this.mongoQuery.setConvention(new MongoConvention(this.mongoQuery.getConvention())
				.alias("id", "_id"));
		this.queryBuilder = new QueryBuilder();
		
		this.mongoInsert = new MongoInsertInto();
		this.mongoInsert.setConvention(new MongoConvention(this.mongoInsert.getConvention())
				.alias("id", "_id"));
		this.insertBuilder = new InsertBuilder();
		
		this.mongoUpdate = new MongoUpdate();
		this.mongoUpdate.setConvention(new MongoConvention(this.mongoUpdate.getConvention())
				.alias("id", "_id"));
		this.updateBuilder = new UpdateBuilder();
		
		this.mongoDelete = new MongoDelete();
		this.mongoDelete.setConvention(new MongoConvention(this.mongoDelete.getConvention())
				.alias("id", "_id"));
		this.deleteBuilder = new DeleteBuilder();
		
		this.binderInsertBuilder = new BinderInsertBuilder();
	}
	
	@Override
	public <T> Query<T> query(Class<T> clazz) {
		return queryBuilder.from(clazz);
	}
	
	@Override
	public <T> T single(Query<T> query) {
		T o = null;
		
		ClassListHandler<T> handler = new ClassListHandler<T>(query.getClazz());
		handler.addAlias("_id", "id");
		handler.addConverter("id", objectIdConverter);
		list(query, handler, (find) -> {
			find.limit(1);
		});
		List<T> l = handler.getList();
		 
		if(l != null && !l.isEmpty()) {
			o = l.get(0);
		}
		
		logger.info(" single " + o);
		
		return o;
	}
	
	@Override
	public <T> List<T> list(Query<T> query) {
		ClassListHandler<T> handler = new ClassListHandler<T>(query.getClazz());
		handler.addAlias("_id", "id");
		handler.addConverter("id", objectIdConverter);
		list(query, handler, null);
		logger.info(" list " + handler.getList().size());
		return handler.getList();
	}
		
	private <T> void list(Query<T> query, DocumentHandler handler, 
			Consumer<FindIterable<Document>> action) {
		MongoQueryResult result = query.to(mongoQuery);
		
		String collection = result.getCollection();
		Document filter = result.getFilter();
		logger.info(collection + " find " + filter.toJson());
		
		MongoCollection<Document> mongoCollection = 
			this.databaseProvider.getDatabase()
			.getCollection(collection);
		
		FindIterable<Document> find = mongoCollection.find(filter);
		if(action != null) {
			action.accept(find);
		}
		
		try (MongoCursor<Document> cursor = find.iterator()) {			
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
	
	@Override
	public <T> Update<T> update(Class<T> clazz) {
		return updateBuilder.entity(clazz);
	}
	
	@Override
	public <T> Delete<T> delete(Class<T> clazz) {
		return deleteBuilder.entity(clazz);
	}
	
	@Override
	public <R> InsertBinder<R> binderInsert(Class<R> clazz) {
		return binderInsertBuilder.from(clazz);
	}
	
	@Override
	public <R, T> void configure(InsertBinder<R> binder, Into<T> into) {
		binder.configure(into.to(mongoInsert));
	}

	@Override
	public <T> String execute(Into<T> into) {
		MongoInsertResult result = into.to(mongoInsert);
		return insert(result);
	}

	private String insert(MongoInsertResult result) {
		String collection = result.getCollection();
		Document doc = result.getValue();
		logger.info(collection + " insert " + doc.toJson());
		
		this.databaseProvider.getDatabase()
			.getCollection(collection)
			.insertOne(doc);
		
		String id = doc.get("_id").toString();
		logger.info(collection + " insert " + id);
		return id;
	}
	
	@Override
	public <T> void execute(Update<T> update) {
		MongoUpdateResult result = update.to(mongoUpdate);
		
		String collection = result.getCollection();
		Document filterDoc = result.getFilter();
		Document updateDoc = result.getUpdate();
		logger.info(collection + " update " + filterDoc.toJson() + " " + updateDoc.toJson());
		
		UpdateResult r = this.databaseProvider.getDatabase()
			.getCollection(collection)
			.updateMany(filterDoc, updateDoc);
		
		logger.info(collection + " update " + r.getModifiedCount());
	}
	
	@Override
	public <T> void execute(Delete<T> delete) {
		MongoDeleteResult result = delete.to(mongoDelete);
		
		String collection = result.getCollection();
		Document filterDoc = result.getFilter();
		logger.info(collection + " delete " + filterDoc.toJson());
		
		DeleteResult r = this.databaseProvider.getDatabase()
			.getCollection(collection)
			.deleteMany(filterDoc);
		
		logger.info(collection + " delete " + r.getDeletedCount());
	}
	
	@Override
	public <R> String execute(InsertBinder<R> binder, R r) {
		MongoInsertResult result = binder.bind(r);
		return insert(result);
	}
	
}
