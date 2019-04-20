package com.naskar.fluentquery.mongodb;

import java.util.function.Function;

import com.naskar.fluentquery.binder.Binder;
import com.naskar.fluentquery.mongodb.converters.MongoInsertResult;

public interface InsertBinder<T> extends Binder<T, MongoInsertResult> {
	
	void configure(MongoInsertResult result);

	<R> R get(Function<T, R> getter);
	
	MongoInsertResult bind(T t);

}