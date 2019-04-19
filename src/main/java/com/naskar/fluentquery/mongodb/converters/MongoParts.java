package com.naskar.fluentquery.mongodb.converters;

import com.mongodb.BasicDBObject;

public class MongoParts {

	private BasicDBObject select;
	private BasicDBObject from;
	private BasicDBObject where;
	private BasicDBObject groupBy;
	private BasicDBObject orderBy;
	
	public MongoParts() {
		this.select = new BasicDBObject();
		this.from = new BasicDBObject();
		this.where = new BasicDBObject();
		this.groupBy = new BasicDBObject();
		this.orderBy = new BasicDBObject();
	}

	public BasicDBObject getSelect() {
		return select;
	}

	public BasicDBObject getFrom() {
		return from;
	}

	public BasicDBObject getWhere() {
		return where;
	}
	
	public BasicDBObject getGroupBy() {
		return groupBy;
	}
	
	public BasicDBObject getOrderBy() {
		return orderBy;
	}
	
	public boolean hasSelect() {
		return !select.keySet().isEmpty();
	}
	
	public boolean hasWhere() {
		return !where.keySet().isEmpty();
	}
	
	public boolean hasGroupBy() {
		return !groupBy.keySet().isEmpty();
	}
	
	public boolean hasOrderBy() {
		return !orderBy.keySet().isEmpty();
	}

}

