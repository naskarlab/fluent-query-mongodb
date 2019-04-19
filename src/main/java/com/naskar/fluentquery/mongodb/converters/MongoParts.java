package com.naskar.fluentquery.mongodb.converters;

import org.bson.Document;

public class MongoParts {

	private Document select;
	private Document from;
	private Document where;
	private Document groupBy;
	private Document orderBy;
	
	public MongoParts() {
		this.select = new Document();
		this.from = new Document();
		this.where = new Document();
		this.groupBy = new Document();
		this.orderBy = new Document();
	}

	public Document getSelect() {
		return select;
	}

	public Document getFrom() {
		return from;
	}

	public Document getWhere() {
		return where;
	}
	
	public Document getGroupBy() {
		return groupBy;
	}
	
	public Document getOrderBy() {
		return orderBy;
	}
	
	public boolean hasWhere() {
		return !where.isEmpty();
	}
	
	public boolean hasGroupBy() {
		return !groupBy.isEmpty();
	}
	
	public boolean hasOrderBy() {
		return !orderBy.isEmpty();
	}

}

