package com.naskar.fluentquery.mongodb.converters;

import org.bson.Document;

public class MongoDeleteResult {

	private String collection;
	private Document filter = new Document();

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public Document getFilter() {
		return filter;
	}

	public void setFilter(Document filter) {
		this.filter = filter;
	}

}
