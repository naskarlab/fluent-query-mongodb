package com.naskar.fluentquery.mongodb.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.naskar.fluentquery.converters.PredicateProvider;
import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.PredicateImpl;
import com.naskar.fluentquery.impl.PredicateImpl.Type;

public class MongoWhereImpl {
	
	private Convention convention;
	
	public void setConvention(Convention convention) {
		this.convention = convention;
	}
	
	public <T, I, B> void convertWhere(
			BasicDBObject object,
			MethodRecordProxy<T> proxy,
			List<PredicateImpl<T, Object, I, B>> predicates) {
		
		List<List<BasicDBObject>> conditions = new ArrayList<List<BasicDBObject>>();
		List<Type> conditionTypes = new ArrayList<Type>();
		
		predicates.stream().forEach(p -> {
			
			if(p.getType() == Type.SPEC_AND || p.getType() == Type.SPEC_OR) {
				
				BasicDBObject filter = new BasicDBObject();
				
				@SuppressWarnings("unchecked")
				PredicateProvider<T, B> q = ((PredicateProvider<T, B>)p.getProperty().apply(null));
				
				convertWhere(filter, proxy, q.getPredicates());
				
				conditions.add(Arrays.asList(filter));
				conditionTypes.add(p.getType());
				
			} else {
			
				proxy.clear();
				p.getProperty().apply(proxy.getProxy());
				
				String name = convention.getNameFromMethod(proxy.getMethods());
				
				p.getActions().forEach(action -> {
					
					BasicDBObject filter = new BasicDBObject();
					
					MongoPredicate<T, Object, I> predicate = new MongoPredicate<T, Object, I>(name, filter);
					action.accept(predicate);
					
					conditions.add(Arrays.asList(filter));
					conditionTypes.add(p.getType());
					
				});
				
			}
			
		});
		
		for(int i = 0; i < conditions.size(); i++) {
			appendType(object, conditionTypes.get(i), conditions.get(i));
		}
	}

	private void appendType(BasicDBObject object, Type t, List<BasicDBObject> filters) {
		if(Type.AND == t || Type.SPEC_AND == t) {
			object.append("$and", filters);
			
		} else if(Type.OR == t || Type.SPEC_OR == t) {
			object.append("$or", filters);
			
		}
	}

}

