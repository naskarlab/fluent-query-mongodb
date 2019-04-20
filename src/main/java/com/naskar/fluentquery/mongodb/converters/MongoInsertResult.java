package com.naskar.fluentquery.mongodb.converters;

import org.bson.Document;

public class MongoInsertResult {

	private String collection;
	private Document value = new Document();

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}
	
	public Document getValue() {
		return value;
	}

	public void setValue(Document value) {
		this.value = value;
	}
		
}
