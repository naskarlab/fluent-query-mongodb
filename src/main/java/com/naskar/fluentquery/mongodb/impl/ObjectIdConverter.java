package com.naskar.fluentquery.mongodb.impl;

import org.bson.types.ObjectId;

import com.naskar.fluentquery.mongodb.impl.ClassListHandler.Converter;

public class ObjectIdConverter implements Converter {
	
	@Override
	public Object to(Object value) {
		Object r = value;
		if(r instanceof ObjectId) {
			r = ((ObjectId)value).toString();
		}
		return r;
	}

}
