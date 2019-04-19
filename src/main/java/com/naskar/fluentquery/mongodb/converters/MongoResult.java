package com.naskar.fluentquery.mongodb.converters;

import com.mongodb.BasicDBObject;

public class MongoResult {

	private String collection;
	private BasicDBObject object = new BasicDBObject();

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}
	
	public BasicDBObject getObject() {
		return object;
	}
	
}
