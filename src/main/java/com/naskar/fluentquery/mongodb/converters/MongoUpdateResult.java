package com.naskar.fluentquery.mongodb.converters;

import org.bson.Document;

public class MongoUpdateResult {

	private String collection;
	private Document filter = new Document();
	private Document values = new Document();
	private Document update = new Document("$set", values);

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

	public Document getValues() {
		return values;
	}

	public void setValues(Document values) {
		this.values = values;
	}

	public Document getUpdate() {
		return update;
	}

	public void setUpdate(Document update) {
		this.update = update;
	}

}
