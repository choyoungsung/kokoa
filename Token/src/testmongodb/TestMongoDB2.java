package testmongodb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import job.util.MyUtil;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

//http://mongodb.github.io/mongo-java-driver/3.0/driver/reference/connecting/authenticating/
//https://docs.mongodb.com/getting-started/java/ 의 코드를 정리
public class TestMongoDB2 {
	static String IP = "52.78.40.88";
	static int port = 27017;
	static String dbName = "sizfire";
	static String tableName = "table1";
	
	public static void main(String[] args) throws Exception{
		test9();
	}
	
	
	public static void test9() throws Exception{
		Bson findQuery = Filters.eq("grades", "B");
		Gson gson = new Gson();
		
		System.err.println(gson.toJson(findQuery));
	}
	
	//https://docs.mongodb.com/manual/meta/aggregation-quick-reference/
	public static void find8() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		MongoDatabase db = client.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		
		List<Document> querys = new ArrayList<Document>();
		querys.add(new Document("$group", new Document("_id", "$borough").append("count", new Document("$sum", 1))));
		AggregateIterable<Document> iter = table.aggregate(querys);
		
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.err.println(document);
		    }
		});
		
		client.close();
	}
	
	public static void find7() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		MongoDatabase db = client.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		
		Document findQuery = new Document("grades.score", new Document("$gt", 30)); 
		FindIterable<Document> iter = table.find(findQuery).sort(new Document("borough", 1).append("address.zipcode", 1));
		
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.err.println(document);
		    }
		});
		
		client.close();
	}
	
	// $eq, $gt, $gte, $lt, $lte, $ne, $in, $nin
	// $or, $and, $not, $nor
	// $exists, $type
	// $mod, $regex, $text, $where
	// $geoWithin, $geoIntersects, $near, $nearSphere
	// $all, $elemMatch, $size
	// $bitsAllSet, $bitsAnySet, $bitsAllclear, $bitsAnyClear
	// $comment
	// $, $elemMatch, $meta, $slice
	public static void find6() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		MongoDatabase db = client.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		
		Document findQuery = new Document("grades.score", new Document("$gt", 30)); 
		FindIterable<Document> iter = table.find(findQuery);
		
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.err.println(document);
		    }
		});
		
		client.close();
	}
	
	public static void find5() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		MongoDatabase db = client.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		
		Bson findQuery = Filters.eq("grades.grade", "B");
		FindIterable<Document> iter = table.find(findQuery);
		
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.err.println(document);
		    }
		});
		
		client.close();
	}
	
	public static void find4() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		MongoDatabase db = client.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		
		Bson findQuery = Filters.eq("address.zipcode", "10462");
		FindIterable<Document> iter = table.find(findQuery);
		
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.err.println(document);
		    }
		});
		
		client.close();
	}
	
	public static void find3() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		MongoDatabase db = client.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		
		Bson findQuery = Filters.eq("borough", "Manhattan");
		FindIterable<Document> iter = table.find(findQuery);
		
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.err.println(document);
		    }
		});
		
		client.close();
	}
	
	public static void find2() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		MongoDatabase db = client.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		
		Document findQuery = new Document("borough", "Manhattan");
		FindIterable<Document> iter = table.find(findQuery);
		
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.err.println(document);
		    }
		});
		
		client.close();
	}
	
	public static void find() throws Exception{
		MongoClient client = new MongoClient(IP, port);
		MongoDatabase db = client.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		
		FindIterable<Document> iter = table.find();
		
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.err.println(document);
		    }
		});
		
		client.close();
	}
	
	public static void insert() throws Exception{
		MongoClient mongoClient = new MongoClient(IP, port);
		MongoDatabase db = mongoClient.getDatabase(dbName);
		
		String jsonstr = MyUtil.loadFileAsStr("C:\\sizfire\\workspace\\Token\\src\\mongodb\\restaurant.json");
		Document doc = Document.parse(jsonstr);
		
		MongoCollection<Document> table = db.getCollection(tableName);
		table.insertOne(doc);
		System.err.println(doc);
		
		mongoClient.close();
	}
	
	public static void connection() throws Exception{
		MongoClient mongoClient = new MongoClient(IP, port);
		MongoDatabase db = mongoClient.getDatabase(dbName);
		
		ListCollectionsIterable<Document> iter = db.listCollections();
		Document doc = iter.first();
		System.err.println(doc);
		mongoClient.close();
	}
}
