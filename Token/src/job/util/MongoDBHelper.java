package job.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class MongoDBHelper {
	static Logger log = Logger.getLogger("");
	
//	static String IPMockup = "52.78.40.88";
//	static String IPMockup = "52.78.72.89";
	static String IPMockup = "172.19.32.229";
	
//	static String IPMockup = "127.0.0.1";
	static int port = 27017;
	static String dbName = "sizfire";
	static String tableName = "gmarket";
	
	public static void main(String[] args) throws Exception{
		
	}
	
	public static MongoClient getClientMockupInstance(){
		MongoClient mongoClient = new MongoClient(IPMockup, port);
		return mongoClient;
	}
	
	
	public static void printIndex(MongoClient mongoClient, String dbName, String tableName){
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		MongoIterable<Document> iter = table.listIndexes();
		printIter(iter);
	}
	
	public static void makeIndex(MongoClient mongoClient, String dbName, String tableName, String fieldName){
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		Document index = new Document(fieldName, 1);
		table.createIndex(index);
		MongoIterable<Document> iter = table.listIndexes();
	}
	
	public static void printTableByGroupFieldAndCount(MongoClient mongoClient, String dbName, String tableName){
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		
		List<Document> paramList = new ArrayList<Document>();
		Document query = new Document("$group", new Document("_id", "$category2").append("count", new Document("$sum", 1) )) ;
		paramList.add(query);
		AggregateIterable<Document> iter = table.aggregate(paramList);
		
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.err.println(document);
		    }
		});
	}
	
	public static void printTable(MongoClient mongoClient, String dbName, String tableName, String fieldName, String fieldValue) throws Exception{
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		Document query = new Document(fieldName, fieldValue);
		
		System.err.println("table.all.count() = " +  table.count() + " table.query.count() = " + table.count(query));
		
		FindIterable<Document> iter = table.find(query);
		
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.err.println(document);
		    }
		});
	}
	
	public static List<Document> iter2List(FindIterable<Document> iter, int maxSize){
		List<Document> res = new ArrayList<Document>();
		
		iter.limit(maxSize);
		
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	res.add(document);
		    }
		});
		
		return res;
	}
	
	public static List<Document> iter2ListByPaging(FindIterable<Document> iter, int pageNum, int pageSize){
		List<Document> res = new ArrayList<Document>();
		
		iter = iter.skip(pageNum*pageSize).limit(pageSize);

		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	res.add(document);
		    }
		});
		
		return res;
	}
	
	public static List<Document> iter2List(MongoIterable<Document> iter){
		List<Document> res = new ArrayList<Document>();
		
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	
		    	res.add(document);
		    }
		});
		
		return res;
	}
	
	public static void printIter(MongoIterable<Document> iter){
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	System.err.println(document);
		    }
		});
	}
	
	public static List<Map<String, String>> docList2ListOfMapStr(List<Document> list){
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		Gson gson = new Gson();
		for(Document doc : list){
			String jsonstr = doc.toJson();
			Map<String, String> map = gson.fromJson(jsonstr, Map.class);
			res.add(map);
		}
		return res;
	}
	
	public static List<Map<String, Object>> docList2ListOfMapObj(List<Document> list){
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Gson gson = new Gson();
		for(Document doc : list){
			String jsonstr = doc.toJson();
			Map<String, Object> map = gson.fromJson(jsonstr, Map.class);
			res.add(map);
		}
		return res;
	}
	
	public static Map<String, Long> countInfo(MongoClient mongoClient, String dbName, String tableName, Document query) throws Exception{
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		
		Map<String, Long> info = new HashMap<String, Long>();
		info.put("table.count()", table.count());
		if(query!=null){
			info.put("table.count(query)", table.count(query));
		}
		return info;
	}
	
	
	//List<Document> paramList = MongoDBHelper.makeAggregateOp(null, null, "seen", "$sum",  1);
	public static List<Document> makeGroupByAndCount(String matchFieldName, Object matchFieldValue, String idFieldName){
		return makeAggregateOp(matchFieldName, matchFieldValue, idFieldName, "$sum", 1);
	}
	
	public static List<Document> makeAggregateOp(String matchFieldName, Object matchFieldValue, String idFieldName, String op, Object countFieldName){
		List<Document> paramList = new ArrayList<Document>();
		
		if(matchFieldName!=null){
			Document match = new Document("$match", new Document(matchFieldName, matchFieldValue));
			paramList.add(match);
		}
		
		Document group = new Document("$group", new Document("_id", "$"+idFieldName).append("total", new Document(op, countFieldName)));//.append(op, "$" + countFieldName));
		paramList.add(group);
		return paramList;
	}
	
	// {$match : {status : "A}}
	// {$group : {_id : "$cust_id, cust_result_field_name : {$sum, "$amount"}}}
	public static List<Document> aggregate(MongoClient mongoClient, String dbName, String tableName, List<Document> paramList){
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);

		AggregateIterable<Document> iter = table.aggregate(paramList);
		List<Document> list = iter2List(iter);
		return list;
	}
	
	//List<Document> iter2ListByPaging(FindIterable<Document> iter, int pageNumber, int pageSize)
	public static List<Document> find(MongoClient mongoClient, String dbName, String tableName, Document query, int pageNum, int pageSize) throws Exception{
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		FindIterable<Document> iter = null;
		
		if(query!=null){
			System.err.println("table.all.count() = " +  table.count() + " table.query.count() = " + table.count(query));
			iter = table.find(query);
		}else{
			System.err.println("table.all.count() = " +  table.count());
			iter = table.find();
		}
		
		List<Document> list = iter2ListByPaging(iter, pageNum, pageSize);
		return list;
	}
	
	public static List<Document> find(MongoClient mongoClient, String dbName, String tableName, Document query, int listSize) throws Exception{
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		FindIterable<Document> iter = null;
		
		if(query!=null){
			System.err.println("table.all.count() = " +  table.count() + " table.query.count() = " + table.count(query));
			iter = table.find(query);
		}else{
			System.err.println("table.all.count() = " +  table.count());
			iter = table.find();
		}
		
		List<Document> list = iter2List(iter, listSize);
		return list;
	}
	
	public static void printTable(MongoClient mongoClient, String dbName, String tableName) throws Exception{
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		FindIterable<Document> iter = table.find();
		iter = iter.limit(100);
		System.err.println("table.count() = " +  table.count());
		iter.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.err.println(document);
		    }
		});
	}
	
	public static void dropTable(MongoClient mongoClient, String dbName, String tableName) throws Exception{
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		table.drop();
	}
	
	
	public static <T> Document makeDoc(Map<String, T > map, String idFieldName){
		Document doc = new Document();
		doc.putAll(map);
		T keyVal = map.get(idFieldName);
		doc.put("_id", keyVal);
		return doc;
	}
	
	public static void insertOne(MongoClient mongoClient, String dbName, String tableName, Document doc) throws Exception{
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
//		String jsonstr = MyUtil.loadFileAsStr("C:\\sizfire\\workspace\\Token\\src\\mongodb\\restaurant.json");
//		Document doc = Document.parse(jsonstr);
		table.insertOne(doc);
	}
	
	public static void insertListOfMap(MongoClient mongoClient, String dbName, String tableName, List<Map<String, String>> list, String idFieldName) throws Exception{
		for(Map<String, String> row : list){
			try{
				Document doc = makeDoc(row, idFieldName);
				insertOne(mongoClient, dbName, tableName, doc);
			}catch(Exception e){
				log.error(e, e);
			}
		}
	}
	
	public static void insertList(MongoClient mongoClient, String dbName, String tableName, List<Document> list) throws Exception{
		for(Document doc : list){
			insertOne(mongoClient, dbName, tableName, doc);
		}
	}
	
	public static UpdateResult updateOne(MongoClient mongoClient, String dbName, String tableName, Document query, Document doc, UpdateOptions option) throws Exception{
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> table = db.getCollection(tableName);
		return table.updateOne(query, doc, option);
	}
	
	public static void saveListWithUniqID2DB(MongoClient mongoClient, String dbName, String tableName, List<Map<String, Object>> list, String idFieldName, boolean upsert) throws Exception{
		UpdateOptions option = new UpdateOptions();
		option = option.upsert(upsert);
		for(Map<String, Object> map : list){
			try{
				System.err.println(map);
				Object idVal = map.get(idFieldName);
				
				Document queryDoc =new Document(idFieldName, idVal);//new Document(idFieldName, new Document("$eq", idVal));
				
				Document doc = makeDoc(map, idFieldName);
				
				Document setdoc = new Document();
				setdoc.put("$set", doc);
				
				updateOne(mongoClient, dbName, tableName, queryDoc, setdoc, option);
				
			}catch(Exception e){
				log.error(e, e);
			}
		}
	}
	
}
