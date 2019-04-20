package com.naskar.fluentquery.mongodb;

import java.util.List;

import com.naskar.fluentquery.Delete;
import com.naskar.fluentquery.Into;
import com.naskar.fluentquery.Query;
import com.naskar.fluentquery.Update;

public interface DAO {
	
	<T> Query<T> query(Class<T> clazz);
	
	<T> T single(Query<T> query);
	
	<R> InsertBinder<R> binderInsert(Class<R> clazz);
	
	<R, T> void configure(InsertBinder<R> binder, Into<T> into);
	
	<T> Into<T> insert(Class<T> clazz);
	
	<T> Update<T> update(Class<T> clazz);
	
	<T> Delete<T> delete(Class<T> clazz);
	
	<T> List<T> list(Query<T> query);
	
	<T> String execute(Into<T> into);
	
	<T> void execute(Update<T> update);
	
	<T> void execute(Delete<T> delete);
	
	<R> String execute(InsertBinder<R> binder, R r);

}
