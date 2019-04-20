package com.naskar.fluentquery.mongodb.converters;

import com.naskar.fluentquery.conventions.SimpleConvention;
import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.impl.DeleteConverter;
import com.naskar.fluentquery.impl.DeleteImpl;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.TypeUtils;

public class MongoDelete implements DeleteConverter<MongoDeleteResult> {
	
	private Convention convention;
	
	private MongoWhereImpl mongoWhereImpl;
	
	public MongoDelete(Convention convention) {
		this.convention = convention;
		this.mongoWhereImpl = new MongoWhereImpl();
		this.mongoWhereImpl.setConvention(convention);
	}
	
	public MongoDelete() {
		this(new SimpleConvention());
	}
	
	public MongoDelete setConvention(Convention convention) {
		this.convention = convention;
		this.mongoWhereImpl.setConvention(convention);
		return this;
	}
	
	public Convention getConvention() {
		return convention;
	}
		
	@Override
	public <T> MongoDeleteResult convert(DeleteImpl<T> deleteImpl) {
		MongoDeleteResult result = new MongoDeleteResult();
		
		convert(deleteImpl, result);
				
		return result;
	}
	
	private <T> void convert(DeleteImpl<T> deleteImpl, MongoDeleteResult result) {
		MethodRecordProxy<T> proxy = TypeUtils.createProxy(deleteImpl.getClazz());
		convertFrom(result, deleteImpl.getClazz());
		mongoWhereImpl.convertWhere(result.getFilter(), proxy, deleteImpl.getPredicates());
	}

	private <T> void convertFrom(MongoDeleteResult result, Class<T> clazz) {
		result.setCollection(convention.getNameFromClass(clazz));
	}
	
}
