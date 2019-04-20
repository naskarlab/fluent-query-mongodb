package com.naskar.fluentquery.mongodb.converters;

import org.bson.Document;

public class MongoQueryResult {

	private String collection;
	private Document filter = new Document();
	private Document fields = new Document();

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}
	
	public Document getFilter() {
		return filter;
	}

	public Document getFields() {
		return fields;
	}

	public void setFields(Document fields) {
		this.fields = fields;
	}

	public void setFilter(Document filter) {
		this.filter = filter;
	}
		
}
