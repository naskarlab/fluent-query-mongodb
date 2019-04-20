# Fluent Query MongoDB

Create Queries using only POJO classes and MongoDB Driver.  

See more example in: [Fluent Query](https://github.com/naskarlab/fluent-query)

## Features

* Configuration over code: independence business code of the infrastructure code;
* Intrusive-less: zero or less changes for your code;
* Glue code: it’s only a small and simple classes set;
* Fluent Builder: code complete is your friend!


## Examples

```

MongoDatabase database = client.getDatabase("testdb");
		
DatabaseProvider databaseProvider = new DatabaseProvider() {
	@Override
	public MongoDatabase getDatabase() {
		return database;
	}
}; 

DAO dao = new DAOImpl(databaseProvider);

dao.execute(dao.insert(Customer.class)
	.value(x -> x.getId()).set(UUID.randomUUID().toString())
	.value(i -> i.getName()).set("test1"));
				
List<Customer> actual = dao.list(dao.query(Customer.class)
		.where(i -> i.getName()).like("t%"));
			
```

## Usage with Maven

```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
    <groupId>com.github.naskarlab</groupId>
    <artifactId>fluent-query-mongodb</artifactId>
    <version>0.0.1</version>
</dependency>
```

