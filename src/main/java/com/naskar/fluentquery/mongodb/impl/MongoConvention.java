package com.naskar.fluentquery.mongodb.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.naskar.fluentquery.impl.Convention;

public class MongoConvention implements Convention {
	
	private Convention target;
	private Map<String, String> alias;
	
	public MongoConvention(Convention convention) {
		this.target = convention;
		this.alias = new HashMap<String, String>();
	}
	
	public MongoConvention alias(String src, String dst) {
		this.alias.put(src, dst);
		return this;
	}
	
	private String translate(String name) {
		String alias = this.alias.get(name);
		if(alias != null) {
			name = alias;
		}
		return name;
	}
	
	public String getNameFromMethod(List<Method> methods) {
		return translate(target.getNameFromMethod(methods));
	}

	public String getNameFromMethod(Method m) {
		return translate(target.getNameFromMethod(m));
	}

	public <T> String getNameFromClass(Class<T> clazz) {
		return translate(target.getNameFromClass(clazz));
	}

}
