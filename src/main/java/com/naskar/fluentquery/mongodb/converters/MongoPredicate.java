package com.naskar.fluentquery.mongodb.converters;

import org.bson.Document;

import com.naskar.fluentquery.Predicate;

class MongoPredicate<T, R, I> implements Predicate<T, R, I> {

	private String name;
	private Document object;
	
	public MongoPredicate(String name, Document object) {
		this.name = name;
		this.object = object;
	}
	
	@Override
	public I eq(R value) {
		object.append(name, new Document("$eq", value));
		return null;
	}
	
	@Override
	public I ne(R value) {
		object.append(name, new Document("$ne", value));
		return null;
	}

	@Override
	public I gt(R value) {
		object.append(name, new Document("$gt", value));
		return null;
	}
	
	@Override
	public I ge(R value) {
		object.append(name, new Document("$gte", value));
		return null;
	}
	
	@Override
	public I lt(R value) {
		object.append(name, new Document("$lt", value));
		return null;
	}
	
	@Override
	public I le(R value) {
		object.append(name, new Document("$lte", value));
		return null;
	}
	
	@Override
	public I like(R value) {
		String str = value != null ? value.toString() : "";
		str = str.replace("%", ".*");
		object.append(name, new Document("$regex", str));
		return null;
	}
	
	@Override
	public I isNull() {
		object.append(name, null);
		return null;
	}
	
	@Override
	public I isNotNull() {
		object.append(name, new Document("$ne", null));
		return null;
	}

}
