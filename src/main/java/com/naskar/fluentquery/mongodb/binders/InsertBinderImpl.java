package com.naskar.fluentquery.mongodb.binders;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.bson.Document;

import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.TypeUtils;
import com.naskar.fluentquery.mongodb.InsertBinder;
import com.naskar.fluentquery.mongodb.converters.MongoInsertResult;

public class InsertBinderImpl<T> implements InsertBinder<T> {
	
	private MongoInsertResult result;
	
	private Map<Object, Function<T, ?>> maps;
	private MethodRecordProxy<T> proxy;

	public InsertBinderImpl(Class<T> clazz) {
		this.maps = new IdentityHashMap<Object, Function<T, ?>>();
		this.proxy = TypeUtils.createProxy(clazz);
		this.proxy.setExecute(false);
	}

	@Override
	public void configure(MongoInsertResult result) {
		this.result = result;
	}

	@Override
	public <R> R get(Function<T, R> getter) {
		proxy.clear();
		getter.apply(proxy.getProxy());
		
		R r = createInstance(proxy.getCalledMethod().getReturnType());
		
		maps.put(r, getter);
		
		return r;
	}

	@SuppressWarnings("unchecked")
	private <R, E> R createInstance(Class<E> returnType) {
		if(Long.class.equals(returnType)) {
			return (R) new Long(0L);
			
		} else if(Double.class.equals(returnType)) {
				return (R) new Double(0L);
				
		} else {
			return (R)TypeUtils.createInstance(returnType);
		}
	}

	@Override
	public MongoInsertResult bind(T t) {
		
		MongoInsertResult newResult = new MongoInsertResult();
		newResult.setCollection(result.getCollection());
		
		Document value = newResult.getValue();
		
		for(Entry<String, Object> e : result.getValue().entrySet()) {
			
			Object v = e.getValue();
			
			Function<T, ?> f = maps.get(e.getValue());
			if(f != null) {
				v = f.apply(t);
			}
			
			value.append(e.getKey(), v);
		}
		
		return newResult;
	}

}

