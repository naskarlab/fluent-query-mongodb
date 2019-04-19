package com.naskar.fluentquery.mongodb;

import com.mongodb.client.MongoDatabase;

public interface DatabaseProvider {
	
	MongoDatabase getDatabase();

}
