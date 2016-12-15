http://www.mkyong.com/tutorials/java-mongodb-tutorials/
내용을 요약


3. Mongo Connection
	// Since 2.10.0, uses MongoClient
	MongoClient mongo = new MongoClient( "localhost" , 27017 );

3.1  connection with secure mode	
	MongoClient mongoClient = new MongoClient();
	DB db = mongoClient.getDB("database name");
	boolean auth = db.authenticate("username", "password".toCharArray());
	
4 getdb
	DB db = mongo.getDB("database name");

5. collection
	DBCollection table = db.getCollection("user");

6. save
	DBCollection table = db.getCollection("user");
	BasicDBObject document = new BasicDBObject();
	document.put("name", "mkyong");
	document.put("age", 30);
	document.put("createdDate", new Date());
	table.insert(document);