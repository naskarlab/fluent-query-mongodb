package com.naskar.fluentquery.mongodb.converters;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import org.bson.Document;

import com.naskar.fluentquery.Into;
import com.naskar.fluentquery.Value;
import com.naskar.fluentquery.conventions.SimpleConvention;
import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.Tuple;
import com.naskar.fluentquery.impl.TypeUtils;
import com.naskar.fluentquery.impl.UpdateConverter;
import com.naskar.fluentquery.impl.UpdateImpl;
import com.naskar.fluentquery.impl.ValueImpl;

public class MongoUpdate implements UpdateConverter<MongoUpdateResult> {
	
	private Convention convention;
	
	private MongoWhereImpl mongoWhereImpl;
	
	public MongoUpdate(Convention convention) {
		this.convention = convention;
		this.mongoWhereImpl = new MongoWhereImpl();
		this.mongoWhereImpl.setConvention(convention);
	}
	
	public MongoUpdate() {
		this(new SimpleConvention());
	}
	
	public MongoUpdate setConvention(Convention convention) {
		this.convention = convention;
		this.mongoWhereImpl.setConvention(convention);
		return this;
	}
	
	public Convention getConvention() {
		return convention;
	}
			
	@Override
	public <T> MongoUpdateResult convert(UpdateImpl<T> updateImpl) {
		MongoUpdateResult result = new MongoUpdateResult();
		
		convert(updateImpl, result);
				
		return result;
	}
	
	private <T> void convert(UpdateImpl<T> updateImpl, MongoUpdateResult result) {
		MethodRecordProxy<T> proxy = TypeUtils.createProxy(updateImpl.getClazz());
		convertFrom(result, updateImpl.getClazz());
		convertNameValue(result.getValues(), proxy, updateImpl.getValues());
		mongoWhereImpl.convertWhere(result.getFilter(), proxy, updateImpl.getPredicates());
	}

	private <T> void convertFrom(MongoUpdateResult result, Class<T> clazz) {
		result.setCollection(convention.getNameFromClass(clazz));
	}
	
	@SuppressWarnings("unchecked")
	private <T> void convertNameValue(
		Document update, 
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
			
			update.append(name, value);
			
		});
	}
	
}
