package com.naskar.fluentquery.mongodb;

import org.bson.Document;

@FunctionalInterface
public interface DocumentHandler {
	boolean next(Document doc);
}
