package com.naskar.fluentquery.mongodb.binders;

import com.naskar.fluentquery.mongodb.InsertBinder;

public class BinderInsertBuilder {

	public <T> InsertBinder<T> from(Class<T> clazz) {
		return new InsertBinderImpl<T>(clazz);
	}

}
