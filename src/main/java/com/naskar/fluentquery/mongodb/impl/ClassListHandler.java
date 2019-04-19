package com.naskar.fluentquery.mongodb.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.naskar.fluentquery.mongodb.DocumentHandler;

public class ClassListHandler<T> implements DocumentHandler {
	
	public interface Converter {
		Object to(Object value);
	}
	
	private Class<T> clazz;
	private Map<String, Field> fields;
	private Map<String, String> alias;
	private Map<String, Converter> converters;
	private List<T> list;
	
	public ClassListHandler(Class<T> clazz) {
		this.clazz = clazz;
		this.fields = getFields(clazz);
		this.alias = new HashMap<String, String>();
		this.converters = new HashMap<String, Converter>();
		this.list = new ArrayList<T>();
	}
	
	public void addAlias(String src, String dst) {
		this.alias.put(src.toUpperCase(), dst.toUpperCase());
	}
	
	public void addConverter(String src, Converter converter) {
		this.converters.put(src.toUpperCase(), converter);
	}
	
	public List<T> getList() {
		return list;
	}
	
	@Override
	public boolean next(Document doc) {
		try {
			T r = clazz.newInstance();
			list.add(r);
			 
			for(Map.Entry<String, Object> e : doc.entrySet()) {
				
				String key = e.getKey().toUpperCase();
				String alias = this.alias.get(key);
				if(alias != null) {
					key = alias;
				}
				
				Object value = e.getValue();
				Converter converter = this.converters.get(key);
				if(converter != null) {
					value = converter.to(value);
				}
				
				Field f = fields.get(key);
				
				if(f != null) {
					f.setAccessible(true);
					f.set(r, value); 
					f.setAccessible(false);
				}
				
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return true;
	}
	
	private Map<String, Field> getFields(Class<?> clazz) {
		Map<String, Field> m = new HashMap<String, Field>();
		
		for(Field f : clazz.getDeclaredFields()) {
			m.put(f.getName().toUpperCase(), f);
		}
		
		return m;
	}
	
}
