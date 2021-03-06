package com.naskar.fluentquery.mongodb.converters;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import org.bson.Document;

import com.naskar.fluentquery.Into;
import com.naskar.fluentquery.Value;
import com.naskar.fluentquery.conventions.SimpleConvention;
import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.impl.IntoConverter;
import com.naskar.fluentquery.impl.IntoImpl;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.Tuple;
import com.naskar.fluentquery.impl.TypeUtils;
import com.naskar.fluentquery.impl.ValueImpl;

public class MongoInsertInto implements IntoConverter<MongoInsertResult> {
	
	private Convention convention;
	
	public MongoInsertInto(Convention convention) {
		this.convention = convention;
	}
	
	public MongoInsertInto() {
		this(new SimpleConvention());
	}
	
	public MongoInsertInto setConvention(Convention convention) {
		this.convention = convention;
		return this;
	}
	
	public Convention getConvention() {
		return convention;
	}
	
	@Override
	public <T> MongoInsertResult convert(IntoImpl<T> intoImpl) {
		MongoInsertResult result = new MongoInsertResult();
		
		convert(intoImpl, result);
		
		return result;
	}
	
	private <T> void convert(IntoImpl<T> intoImpl, MongoInsertResult result) {
		MethodRecordProxy<T> proxy = TypeUtils.createProxy(intoImpl.getClazz());
		convertInto(result, intoImpl.getClazz());
		convertNameValue(result.getValue(), proxy, intoImpl.getValues());		
	}

	private <T> void convertInto(MongoInsertResult result, Class<T> clazz) {
		result.setCollection(convention.getNameFromClass(clazz));
	}
	
	@SuppressWarnings("unchecked")
	private <T> void convertNameValue(
		Document valueDoc, 
		MethodRecordProxy<T> proxy,
		List<Tuple<Function<T, ?>, Value<Into<T>, ?>>> values
	) {
		values.stream().forEach(i -> {
			
			proxy.clear();
			i.getT1().apply(proxy.getProxy());
			
			Method m = proxy.getCalledMethod();
			
			ValueImpl<Into<?>, ?> valueImpl = ((ValueImpl<Into<?>, ?>)(Object)i.getT2());
			Object value = valueImpl.get();
			
			String name = convention.getNameFromMethod(m);
			
			valueDoc.append(name, value);
			
		});
	}
	
}
