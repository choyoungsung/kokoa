package servicer;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.BsonArray;
import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;

import crawl.CrawlDaumNews;
import crawl.CrawlGmarket;
import job.jsoup.Servicer4MD;
import job.util.LangUtil;
import job.util.MongoDBHelper;
import job.util.MyUtil;
import tokenizer.LangTokenizer;

public class APIServicer {
	
	public String jsoup(Map<String, String> params, String postData) throws Exception{
		Gson gson = new Gson();
		Map<String, Object> dic = (Map<String, Object>)gson.fromJson(postData, Map.class);
		
		String url = MyUtil.Str2Type.getSmartStrVal(dic.get("url"), null);
		if(url==null){
			throw new Exception("url param is missing");
		}
		
		String encoding = MyUtil.Str2Type.getSmartStrVal(dic.get("encoding"), "UTF-8");
		boolean bNew = MyUtil.Str2Type.getSmartBooleanVal(dic.get("bNew"), Boolean.TRUE);
		int outputMode = MyUtil.Str2Type.getSmartIntVal(dic.get("outputMode"), 1);
		
		url = addProtocolIfNot(url);
		String res = Servicer4MD.forcursedHtml(url, encoding, bNew, outputMode);
		return res;
	}
	
	private static String addProtocolIfNot(String url){
		if(url==null)
			return null;
		
		int idx = url.indexOf("http://");
		if(idx>=0){
			return url;
		}
		
		idx = url.indexOf("https://");
		if(idx>=0){
			return url;
		}
		
		return "http://" + url;
	}
	
	public String parse(Map<String, String> params, String postData) throws Exception{
		System.err.println(postData);
		Gson gson = new Gson();
		Map<String, Object> dic = (Map<String, Object>)gson.fromJson(postData, Map.class);
		String text = (String)dic.get("text");
		System.err.println(text);
		text = URLDecoder.decode(text, "UTF-8");
		System.err.println(text);
		Double N = MyUtil.Str2Type.getSmartDoubleVal(dic.get("N"), 2D);
		boolean bHangulAndEtc = MyUtil.Str2Type.getSmartBooleanVal(dic.get("bHangulAndEtc"), true);
		boolean bDoCharNormalize = MyUtil.Str2Type.getSmartBooleanVal(dic.get("bDoCharNormalize"), true);
		boolean bNumPuncAndEtc = MyUtil.Str2Type.getSmartBooleanVal(dic.get("bNumPuncAndEtc"), true);
		
//		String res = LangUtil.ServiceSet.parse(text, bHangulAndEtc, bDoCharNormalize, N.intValue());
		String res = LangTokenizer.Servicer.parse(text, bHangulAndEtc, bDoCharNormalize, bNumPuncAndEtc, N.intValue());

		return res;
	}
	
	static String dbName = "sizfire";
	
	public String mongodb(Map<String, String> params, String postData) throws Exception{
		String collection = MyUtil.Str2Type.getSmartStrVal(params.get("collection"), null);
		String operation = MyUtil.Str2Type.getSmartStrVal(params.get("operation"), null);
		int listSize = MyUtil.Str2Type.getSmartIntVal(params.get("listSize"), 100);
		
		postData = URLDecoder.decode(postData, "UTF-8");
		MongoClient client = MongoDBHelper.getClientMockupInstance();
		
		if(operation.equals("find")){
			Document query = Document.parse(postData);
			List<Document> list = MongoDBHelper.find(client, dbName, collection, query, listSize);
			Map<String, Long> countInfo = MongoDBHelper.countInfo(client, dbName, collection, query);
			Gson gson = new Gson();
			return countInfo.toString() + " ##### " + gson.toJson(list);
		
		}else if(operation.equals("aggregate")){
			List<Document> query = MyUtil.listOfDocument(postData);
			List<Document> list = MongoDBHelper.aggregate(client, dbName, collection, query);
			Gson gson = new Gson();
			return gson.toJson(list);
		
		}else{
			Document query = Document.parse(postData);
			List<Document> list = MongoDBHelper.find(client, dbName, collection, query, listSize);
			Map<String, Long> countInfo = MongoDBHelper.countInfo(client, dbName, collection, query);
			Gson gson = new Gson();
			return countInfo.toString() + " ##### " + gson.toJson(list);
		}
	}
	
	
	
	public String parse(Map<String, String> params) throws Exception{
		String text = MyUtil.Str2Type.getSmartStrVal(params.get("text"), null);
		if(text==null){
			throw new Exception("text param is missing");
		}
		int N = MyUtil.Str2Type.getSmartIntVal(params.get("N"), 2);
		text = URLDecoder.decode(text, "UTF-8");
		String res = LangUtil.NGram.convert2NgramStr(text, N);
		return res;
	}
	
	
	
	public String crawl(Map<String, String> params) throws Exception{
		String query = MyUtil.Str2Type.getSmartStrVal(params.get("query"), null);
		if(query==null){
			throw new Exception("text param is missing");
		}
		int start = MyUtil.Str2Type.getSmartIntVal(params.get("start"), 1);
		int end = MyUtil.Str2Type.getSmartIntVal(params.get("end"), 10);
		
		query = URLDecoder.decode(query, "UTF-8");
		List<String> list = CrawlDaumNews.crawlDaumNews(query, start, end);
		
		List<Map<String, String>> all = convert2ListOfMap( list, "crawled");
		
		Gson gson = new Gson();
		String res = gson.toJson(all);
		return res;
		
	}
	
	private List<Map<String, String>> convert2ListOfMap(List<String> list, String title) throws Exception{
		 List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		 for(String item : list){
			 Map<String, String> row = new HashMap<String, String>();
			 row.put(title, item);
			 res.add(row);
		 }
		return res;
	}
	
}
