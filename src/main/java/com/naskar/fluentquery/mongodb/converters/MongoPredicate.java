package com.naskar.fluentquery.mongodb.converters;

import com.mongodb.BasicDBObject;
import com.naskar.fluentquery.Predicate;

class MongoPredicate<T, R, I> implements Predicate<T, R, I> {

	private String name;
	private BasicDBObject object;
	
	public MongoPredicate(String name, BasicDBObject object) {
		this.name = name;
		this.object = object;
	}
	
	@Override
	public I eq(R value) {
		object.append(name, new BasicDBObject("$eq", value));
		return null;
	}
	
	@Override
	public I ne(R value) {
		object.append(name, new BasicDBObject("$ne", value));
		return null;
	}

	@Override
	public I gt(R value) {
		object.append(name, new BasicDBObject("$gt", value));
		return null;
	}
	
	@Override
	public I ge(R value) {
		object.append(name, new BasicDBObject("$gte", value));
		return null;
	}
	
	@Override
	public I lt(R value) {
		object.append(name, new BasicDBObject("$lt", value));
		return null;
	}
	
	@Override
	public I le(R value) {
		object.append(name, new BasicDBObject("$lte", value));
		return null;
	}
	
	@Override
	public I like(R value) {
		String str = value != null ? value.toString() : "";
		str = str.replace("%", ".*");
		object.append(name, new BasicDBObject("$regex", str));
		return null;
	}
	
	@Override
	public I isNull() {
		object.append(name, null);
		return null;
	}
	
	@Override
	public I isNotNull() {
		object.append(name, new BasicDBObject("$ne", null));
		return null;
	}

}
