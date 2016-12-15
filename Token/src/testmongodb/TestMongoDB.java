package testmongodb;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.client.MongoDatabase;

public class TestMongoDB {
	static String IP = "52.78.40.88";
	static int port = 27017;
	static String dbName = "sizfire";
	
	public static void main(String[] args) throws Exception{
		delete();
	}
	
	public static void connection() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		DB db = client.getDB(dbName);
		
		System.err.println(db.getStats().toJson());
		
		client.close();
	}
	
	public static void collection() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		DB db = client.getDB(dbName);
		Set<String> tableNames = db.getCollectionNames();
		System.err.println(tableNames);
		
		client.close();
	}
	
	public static void save() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		DB db = client.getDB(dbName);
		DBCollection table = db.getCollection("table1");
		BasicDBObject document = new BasicDBObject();
		document.put("name", "mkyong");
		document.put("age", 30);
		document.put("createdDate", new Date());
		table.insert(document);
		
		client.close();
	}
	
	public static void update() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		DB db = client.getDB(dbName);
		
		DBCollection table = db.getCollection("table1");
		
		BasicDBObject query = new BasicDBObject();
		query.put("name", "mkyong");
		
		BasicDBObject newDoc = new BasicDBObject();
		newDoc.put("name", "mkyong-New");
		
		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDoc);
		
		table.update(query, updateObj);
		
		client.close();
	}
	
	public static void find() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		DB db = client.getDB(dbName);
		
		DBCollection table = db.getCollection("table1");
		
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("name", "mkyong-New");
		
		
		DBCursor cursor = table.find(searchQuery);
		
		while (cursor.hasNext()) {
			System.err.println(cursor.next());
		}
		
		client.close();
	}
	
	public static void delete() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		DB db = client.getDB(dbName);
		
		DBCollection table = db.getCollection("table1");
		
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("name", "mkyong-New");
		
		 WriteResult res = table.remove(searchQuery);
		 
		 System.err.println(res);
		
		client.close();
	}
	
}
