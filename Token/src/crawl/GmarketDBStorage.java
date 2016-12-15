package crawl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import job.util.MongoDBHelper;
import job.util.MyUtil;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class GmarketDBStorage {
	static Logger log = Logger.getLogger("");
	
	static String dbName = "sizfire";
	static String tableName = "gmarket";
	
	static String mainListTableName = "gmarket.mainList";
	static String midListTableName ="gmarket.midList";
	static String goodsListTableName ="gmarket.goodsList";
	static String goodscodeTableName = "gmarket.goodscode";
//	static String goodsIDetailnfoTableName ="gmarket.goodsDetailInfo";
	
	static String[] fieldNames = new String[]{"thumb", "price", "text", "title", "catogory1", "catogory2"};
	static Map<String, String> fieldNameReplace = null;
	static{
		fieldNameReplace =  new HashMap<String, String>();
		fieldNameReplace.put("catogory1", "category1");
		fieldNameReplace.put("catogory2", "category2");
	}
	
	public static void main(String[] args) throws Exception{
//		printByGroup();
//		printTable2();
//		insert("C:\\sizfire\\workspace\\Token\\simple.log.2016-06-15");
//		String mapString ="{thumb=http://gdimg.gmarket.co.kr/goods_image2/gallery_jpgimg/729/935/729935549.jpg, price=9,010원9,900원 8%할인, text=[파파야] 민소매 기본 나시(CNFRTP039B) (영등포점A), title=[파파야] 민소매 기본 나시(CNFRTP039B) (영등포점A), catogory1=브랜드 여성의류, catogory2=티셔츠}"; 
//		Map<String, String> map  =  parse2Map(mapString, new String[]{"thumb", "price", "text", "title", "catogory1", "catogory2"});
		
//		System.err.println(map);
		
		
//		List<Map<String, Object>> list = selectNotRunnedJobFromTable(midListTableName, Boolean.TRUE, 10);
		
//		changeFieldVal(midListTableName, "seen", Boolean.TRUE, null, 6000);
//		updateOne2Seen(midListTableName, "_id", "http://category.gmarket.co.kr/listview/List.aspx?gdsc_cd=300026660&ecp_gdlc=100000103&ecp_gdmc=200002669", "seen", Boolean.TRUE);
//		updateOne2Seen(midListTableName, "_id", "http://category.gmarket.co.kr/listview/List.aspx?gdsc_cd=300026661&ecp_gdlc=100000103&ecp_gdmc=200002669", "seen", Boolean.TRUE);
		 
		
//		System.err.println(list);
		
		
//		printTable(midListTableName);
//		printTable(midListTableName);
		printTable(goodsListTableName);
		printTableStat(midListTableName, "seen");
		printTableStat(goodsListTableName, null);
//		makeIndex();
	}
	
	public static void makeIndex() throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
//		MongoDBHelper.makeIndex(client, dbName, midListTableName, "seen");
//		GmarketDBStorage.goodsListTableName, goodsList, "thumb"
//		MongoDBHelper.makeIndex(client, dbName, goodsListTableName, "thumb");
		MongoDBHelper.printIndex(client, dbName, midListTableName);
		
		client.close();
	}
	
	public static List<Map<String, Object>> iterByPaging(String tableName, int pageNum, int pageSize) throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		List<Document> list = MongoDBHelper.find(client, dbName, tableName, null, pageNum, pageSize);
		client.close();
		
		return MongoDBHelper.docList2ListOfMapObj(list);
	}
	
	public static Map<String, Long> getCountInfo(String tableName, String groupByCountFieldName) throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		
		
		Map<String, Long> countInfo = MongoDBHelper.countInfo(client, dbName, tableName, null);
		
		if(groupByCountFieldName!=null){
			List<Document> paramList = MongoDBHelper.makeGroupByAndCount(null, null, groupByCountFieldName);
		
			List<Document> list = MongoDBHelper.aggregate(client, dbName, tableName, paramList);
		}
		client.close();
		return countInfo;
	}
	
	public static void printTableStat(String tableName, String groupByCountFieldName) throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		
		
		Map<String, Long> countInfo = MongoDBHelper.countInfo(client, dbName, tableName, null);
		System.err.println(countInfo);
		
		if(groupByCountFieldName!=null){
			List<Document> paramList = MongoDBHelper.makeGroupByAndCount(null, null, groupByCountFieldName);
		
			List<Document> list = MongoDBHelper.aggregate(client, dbName, tableName, paramList);
			System.err.println(list);
		}
		client.close();
	}
	
	public static void changeFieldVal(String tableName, String targetFieldName, Boolean targetFieldValue, Boolean newFieldValue, int listSize) throws Exception{
		List<Map<String, Object>> list = selectNotRunnedJobFromTable(tableName, targetFieldValue, listSize);
		for(Map<String, Object> row : list){
			String id = (String)row.get("_id");
			updateOne2Seen(midListTableName, "_id", id,targetFieldName, newFieldValue);
		}
	}
	
	public static List<Map<String, Object>> selectNotRunnedJobFromTable(String tableName, Boolean seenVal, int listSize) throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		Document query = new Document("seen", seenVal);
		List<Document> docList = MongoDBHelper.find(client, dbName, tableName, query, listSize);
		List<Map<String, Object>> res = MongoDBHelper.docList2ListOfMapObj(docList);
		client.close();
		return res;
	}
	
	public static void updateOne2Seen(String tableName, String idFieldName, String idVal, String targetFieldName, Boolean isSeen) throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		Document query = new Document(idFieldName, idVal); 
		Document doc = new Document("$set", new Document(targetFieldName, isSeen)); 
		UpdateOptions option = (new UpdateOptions()).upsert(true);
		
		MongoDBHelper.updateOne(client,  dbName, tableName, query, doc, option);
		client.close();
	}
	
	public static void updateTable() throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		
		Document query = new Document("link", "http://category.gmarket.co.kr/listview/L100000076.aspx_TEST");
		Document doc = new Document("$set", new Document("category", "test"));
		UpdateOptions option = new UpdateOptions().upsert(true);
		
		MongoDBHelper.updateOne(client, dbName, mainListTableName, query, doc, option);
		client.close();
	}
	
	
	
	public static void dropTable(String tableName) throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		MongoDBHelper.dropTable(client, dbName, tableName);
		client.close();
	}
	
	public static List<Document> findDBTable(String tableName, Document query, int listSize) throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		List<Document> list = MongoDBHelper.find(client, dbName, tableName, query, listSize);
		
		client.close();
		return list;
	}
	
	
	public static void insertListOfMap2DB(String tableName, List<Map<String, String>> list, String idFieldName) throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		MongoDBHelper.insertListOfMap(client, dbName, tableName, list, idFieldName);
		client.close();
	}
	
	public static void saveList2DB(String tableName, List<Map<String, Object>> list, String idFieldName, boolean upsert) throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
//		MongoDBHelper.saveListWithUniqID2DB(client, dbName, tableName, list, idFieldName);
		MongoDBHelper.saveListWithUniqID2DB(client, dbName, tableName, list, idFieldName, upsert);
		
		client.close();
	}
	
	public static void printByGroup() throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		MongoDBHelper.printTableByGroupFieldAndCount(client, dbName, tableName);
		client.close();
	}
	
	public static void printTable2() throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		MongoDBHelper.printTable(client, dbName, tableName, "category2", "점퍼/패딩") ;
		client.close();
	}
	
	public static void printTable(String tableName) throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		MongoDBHelper.printTable(client, dbName, tableName) ;
//		MongoDBHelper.printTable(client, dbName, tableName, "category2", "티셔츠") ;
		client.close();
	}
	
	public static void insert(String simpleLogFileName) throws Exception{
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		
		MongoDBHelper.dropTable(client, dbName, tableName);
		
		List<String> lines = MyUtil.loadFileAsArrayOfLine(simpleLogFileName);
		for(String line : lines){
			try{
				if(!line.startsWith("{thumb=http://gdimg")){
					continue;
				}
				
				Document doc = parse2Map(line, fieldNames, fieldNameReplace);
				
				System.err.println(doc);
	//			System.in.read(new byte[1]);
				MongoDBHelper.insertOne(client, dbName, tableName, doc);
			}catch(Exception e){
				log.error(e, e);
			}
		}
		
		client.close();
	}
	
	//{thumb=http://gdimg.gmarket.co.kr/goods_image2/gallery_jpgimg/729/935/729935549.jpg, price=9,010원9,900원 8%할인, text=[파파야] 민소매 기본 나시(CNFRTP039B) (영등포점A), title=[파파야] 민소매 기본 나시(CNFRTP039B) (영등포점A), catogory1=브랜드 여성의류, catogory2=티셔츠}
	// 1개의 라인에 map.toString() 출력을 다시 읽는다. 
	private static Document parse2Map(String mapString, String[] fieldNames, Map<String, String> fieldNameReplace) throws Exception{
		if(mapString==null || fieldNames==null || fieldNames.length==0)
			return null;
			
		char cs = mapString.charAt(0);
		char ce = mapString.charAt(mapString.length()-1);
		if(!(cs=='{' && ce=='}')){
			return null;
		}
		
		
		List<Integer> arrOfStart = new ArrayList<Integer>();
		
		int fromIndex = 1;
		for(String fieldName : fieldNames){
			int startOfName = mapString.indexOf(fieldName, fromIndex);
			if(startOfName<0){
				throw new Exception("NOT FOUND " + fieldName + " from " + mapString);
			}
			arrOfStart.add(startOfName);
		}
		arrOfStart.add(mapString.length()-1+2);
		
		Document res = new Document();
		for(int i=0;i<arrOfStart.size()-1;i++){
			int startOf1st = arrOfStart.get(i);
			int startOf2nd = arrOfStart.get(i+1);
			String fieldName = fieldNames[i];
			int startOfData = startOf1st + fieldName.length() + 1;
			int endOfData = startOf2nd - 2;
			String data = mapString.substring(startOfData, endOfData);
			
			String newFieldName = fieldName;
			if(fieldNameReplace!=null){
				String tmpFieldName = fieldNameReplace.get(fieldName);
				if(tmpFieldName!=null){
					newFieldName = tmpFieldName;
				}
			}
			res.put(newFieldName, data);
		}
		return res;
	}
	
}
