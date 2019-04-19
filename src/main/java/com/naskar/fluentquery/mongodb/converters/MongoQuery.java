package com.naskar.fluentquery.mongodb.converters;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import com.mongodb.BasicDBObject;
import com.naskar.fluentquery.conventions.SimpleConvention;
import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.QueryConverter;
import com.naskar.fluentquery.impl.QueryImpl;
import com.naskar.fluentquery.impl.TypeUtils;

public class MongoQuery implements QueryConverter<MongoResult> {
	
	private Convention convention;
	
	private MongoWhereImpl mongoWhereImpl;
	
	public MongoQuery(Convention convention) {
		this.convention = convention;
		this.mongoWhereImpl = new MongoWhereImpl();
		this.mongoWhereImpl.setConvention(convention);
	}
	
	public MongoQuery() {
		this(new SimpleConvention());
	}
	
	public MongoQuery setConvention(Convention convention) {
		this.convention = convention;
		this.mongoWhereImpl.setConvention(convention);
		return this;
	}
	
	@Override
	public <T> MongoResult convert(QueryImpl<T> queryImpl) {
		
		MongoResult result = new MongoResult();
		MongoParts parts = new MongoParts();
		
		convert(queryImpl, parts, result);
		
		// TODO: parts
		if(parts.hasSelect()) {
			result.setFields(parts.getSelect());
		}
		if(parts.hasWhere()) {
			result.setObject(parts.getWhere());
		}
		
		return result;
	}
	
	private <T> void convert(QueryImpl<T> queryImpl, MongoParts parts, MongoResult result) {
		MethodRecordProxy<T> proxy = TypeUtils.createProxy(queryImpl.getClazz());
		
		convertSelect(parts.getSelect(), proxy, queryImpl.getSelects());
		
		convertFrom(result, queryImpl.getClazz());
		
		mongoWhereImpl.convertWhere(parts.getWhere(), proxy, queryImpl.getPredicates());
		
		//convertGroupBy(parts.getGroupBy(), proxy, queryImpl.getGroups());
		//convertOrderBy(parts.getOrderBy(), proxy, queryImpl.getOrders());
		//convertFroms(parts, level, result, proxy, queryImpl.getFroms());
	}

//	private <T> void convertFroms(
//			MongoParts parts, 
//			String alias, 
//			final HolderInt level,
//			MongoResult result, 
//			MethodRecordProxy<T> proxy, 
//			List<Tuple<QueryImpl<?>, Consumer<T>>> froms) {
//		
//		froms.forEach(i -> {
//			
//			proxy.clear();
//			i.getT2().accept(proxy.getProxy());
//			
//			List<String> parentsTmp = new ArrayList<String>();
//			Iterator<Method> it = proxy.getMethods().iterator(); 
//			while(it.hasNext()) {
//				
//				Method m = null;
//				List<Method> path = new ArrayList<Method>();
//				do {
//					m = it.next();
//					path.add(m);
//				} while(!TypeUtils.isValueType(m.getReturnType()) && it.hasNext());
//				
//				parentsTmp.add(alias + convention.getNameFromMethod(path));
//			}
//			
//			level.value++;
//			convert(i.getT1(), parts, level, result, parentsTmp);
//			
//		});
//	}
	
	private <T> void convertSelect(
		BasicDBObject object,
		MethodRecordProxy<T> proxy, 
		List<Function<T, ?>> selects) {
		
		selects.stream().forEach(i -> {
			
			proxy.clear();
			i.apply(proxy.getProxy());
			
			Method m = proxy.getCalledMethod();
			
			String name = convention.getNameFromMethod(m);
			object.put(name, 1);
			
		});
	}

//	private <T> void convertGroupBy(
//			StringBuilder sb, String alias,
//			MethodRecordProxy<T> proxy, 
//			List<GroupByImpl> groups) {
//		
//		if(!groups.isEmpty()) {
//			String s = groups.stream().map(i -> {
//				
//				if(i instanceof AliasGroupByImpl) {
//					
//					AliasGroupByImpl group = (AliasGroupByImpl)i;
//					
//					return group.getAlias();
//						
//				} else if(i instanceof AttributeGroupByImpl) {
//					
//					@SuppressWarnings("unchecked")
//					AttributeGroupByImpl<T, ?> group = (AttributeGroupByImpl<T, ?>)i;
//					
//					proxy.clear();
//					group.getProperty().apply(proxy.getProxy());
//					
//					return 
//						alias 
//						+ convention.getNameFromMethod(proxy.getCalledMethod());
//				} else {
//					return "";
//					
//				}
//				
//			}).collect(Collectors.joining(", "));
//					
//			sb.append(s);
//		}
//	}
//	
//	private <T> void convertOrderBy(
//		StringBuilder sb, String alias,
//		MethodRecordProxy<T> proxy, 
//		List<OrderByImpl<?>> orders) {
//		
//		if(!orders.isEmpty()) {
//			String s = orders.stream().map(i -> {
//				
//				if(i instanceof AliasOrderByImpl) {
//					
//					AliasOrderByImpl<?> order = (AliasOrderByImpl<?>)i;
//					
//					return 
//						order.getAlias()
//						+ (OrderByImpl.OrderByType.DESC.equals(i.getType()) ? " desc" : "");
//						
//				} else if(i instanceof AttributeOrderByImpl) {
//					
//					@SuppressWarnings("unchecked")
//					AttributeOrderByImpl<?, T, ?> order = (AttributeOrderByImpl<?, T, ?>)i;
//					
//					proxy.clear();
//					order.getProperty().apply(proxy.getProxy());
//					
//					return 
//						alias 
//						+ convention.getNameFromMethod(proxy.getCalledMethod()) 
//						+ (OrderByImpl.OrderByType.DESC.equals(i.getType()) ? " desc" : "");
//				} else {
//					return "";
//					
//				}
//				
//			}).collect(Collectors.joining(", "));
//					
//			sb.append(s);
//		}
//	}
	
	private <T> void convertFrom(MongoResult result, Class<T> clazz) {
		result.setCollection(convention.getNameFromClass(clazz));
	}
	
}

