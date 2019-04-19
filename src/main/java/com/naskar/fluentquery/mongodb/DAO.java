package com.naskar.fluentquery.mongodb;

import com.naskar.fluentquery.Into;

public interface DAO {
	
//	<T> Query<T> query(Class<T> clazz);
//	
//	<T> T single(Query<T> query);
//	
//	<R> BinderSQL<R> binder(Class<R> clazz);
//	
//	<R, T> void configure(BinderSQL<R> binder, Into<T> into);
//	
	<T> Into<T> insert(Class<T> clazz);
//	
//	<T> Update<T> update(Class<T> clazz);
//	
//	<T> Delete<T> delete(Class<T> clazz);
//	
//	<T> List<T> list(Query<T> query);
//	
//	<T, R> List<R> list(Query<T> query, Class<R> clazz);
//	
//	<T> void list(Query<T> query, ResultSetHandler handler);
//	
//	void list(String sql, List<Object> params, ResultSetHandler handler);
//	
	<T> void execute(Into<T> into);
//	
//	<T> void execute(Into<T> into, ResultSetHandler handlerKeys);
//	
//	<T> void execute(Update<T> update);
//	
//	<T> void execute(Delete<T> delete);
//	
//	void execute(String sql);
//	
//	void execute(String sql, List<Object> params);
//	
//	void execute(String sql, List<Object> params, ResultSetHandler handlerKeys);
//
//	<R> void execute(BinderSQL<R> binder, R r);

}
