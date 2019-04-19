package com.naskar.fluentquery.mongodb.converters;

import com.mongodb.BasicDBObject;

public class MongoResult {

	private String collection;
	private BasicDBObject object = new BasicDBObject();
	private BasicDBObject fields = new BasicDBObject();

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}
	
	public BasicDBObject getObject() {
		return object;
	}

	public BasicDBObject getFields() {
		return fields;
	}

	public void setFields(BasicDBObject fields) {
		this.fields = fields;
	}

	public void setObject(BasicDBObject object) {
		this.object = object;
	}
		
}
