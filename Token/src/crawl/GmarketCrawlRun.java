package crawl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import job.util.MongoDBHelper;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;

import crawl.CrawlGmarket.ItemDetail;
import crawl.CrawlGmarket.ListDetail;
import crawl.CrawlGmarket.ListMid;
import crawl.CrawlGmarket.ListOuter;
import crawl.CrawlGmarket.Search2nd;

public class GmarketCrawlRun {
	static Logger log = Logger.getLogger("simple");
	
	static String mainUrl = "http://gmarket.co.kr/";
	
	public static void main(String[] args) throws Exception{
//		dropTable();

//		runMainAndMid();
//		getGoodsListFromMidUrl();
		getGoodsListBySearch2ndFromMidUrl();
//		fillGoodsCodeFromGoodsListUrlOneTime();
//		crawlGoodsDetailInfo(); ///// 내일 합시다.....
//		printTable();
	}
	
	public static void printTable() throws Exception{
//		List<Document> list = GmarketDBStorage.findDBTable(GmarketDBStorage.mainListTableName, null, 100);
//		BasicCrawlTool.printDocTable(list, "mainList");
		
		List<Document> list2 = GmarketDBStorage.findDBTable(GmarketDBStorage.midListTableName, null, 10);
		BasicCrawlTool.printDocTable(list2, "midListTableName");
		
		List<Document> list3 = GmarketDBStorage.findDBTable(GmarketDBStorage.goodsListTableName, null, 10);
		BasicCrawlTool.printDocTable(list3, "goodsListTableName");
		
		List<Document> list4 = GmarketDBStorage.findDBTable(GmarketDBStorage.goodscodeTableName, null, 10);
		BasicCrawlTool.printDocTable(list4, "goodscodeTableName");
	}
	
	public static void crawlGoodsDetailInfo() throws Exception{
		List<Map<String, Object>> goodscodeList = GmarketDBStorage.selectNotRunnedJobFromTable(GmarketDBStorage.goodscodeTableName, null, 100);
		for(Map<String, Object> line : goodscodeList){
			String goodscode = (String)line.get("goodscode");
			Map<String, Object> itemDetailInfo = ItemDetail.crawlItemDetailUrl(goodscode);
			System.err.println(itemDetailInfo);
			if(itemDetailInfo==null || itemDetailInfo.isEmpty()){
				GmarketDBStorage.updateOne2Seen(GmarketDBStorage.goodscodeTableName, "goodscode", goodscode, "seen", Boolean.FALSE);
			}else{
				
			}
		}
		
	}
	
	public  static void fillGoodsCodeFromGoodsListUrlOneTime() throws Exception{
		int pageSize = 100;
		 
		Map<String, Long> countInfo = GmarketDBStorage.getCountInfo(GmarketDBStorage.goodsListTableName, null);
		
		Long cnt = countInfo.get("table.count()");
		
		int maxPageNum = cnt.intValue()/pageSize + 1;
		
		for(int pageNum=0;pageNum<maxPageNum;pageNum++){
			try{
				List<Map<String, Object>> goodsList = GmarketDBStorage.iterByPaging(GmarketDBStorage.goodsListTableName, pageNum, pageSize);
				BasicCrawlTool.printTable(goodsList, "goodslist");
				List<Map<String, Object>> goodscodeList = getGoodscode(goodsList);
				GmarketDBStorage.saveList2DB(GmarketDBStorage.goodscodeTableName, goodscodeList, "goodscode", upsert);
			}catch(Exception e){
				log.error(e, e);
			}
		}
	}
	
	private static List<Map<String, Object>> getGoodscode(List<Map<String, Object>> goodsList){
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> row : goodsList){
			String thumb = (String)row.get("thumb");
			if(thumb==null || thumb.isEmpty()){
				continue;
			}
			String goodscode = BasicCrawlTool.getNameFromUrl(thumb, "http://gdimg", ".jpg");
			if(goodscode==null || goodscode.isEmpty()){
				continue;
			}
			Map<String, Object> line = new HashMap<String, Object>();
			line.put("goodscode", goodscode);
			res.add(line);
		}
		return res;
	}
	
	
	public static void getGoodsListBySearch2ndFromMidUrl() throws Exception{
		final long sleepTime = 2000;
		List<Map<String, Object>> midList = GmarketDBStorage.selectNotRunnedJobFromTable(GmarketDBStorage.midListTableName, null, 10000);
		for(Map<String, Object> row : midList){
			try{
				log.info("getGoodsListBySearch2ndFromMidUrl " + row);
				Thread.sleep(sleepTime);
				String detailLink = (String)row.get("link");
				String param = Search2nd.getParam(detailLink);
				List<Map<String, Object>> goodsList = Search2nd.traverseAndCrawl(param, sleepTime);
				if(goodsList==null){
					continue;
				}
				BasicCrawlTool.printTable(goodsList, "goodsList");
				GmarketDBStorage.saveList2DB(GmarketDBStorage.goodsListTableName, goodsList, "thumb", upsert);
				GmarketDBStorage.updateOne2Seen(GmarketDBStorage.midListTableName, "link", detailLink, "seen", Boolean.TRUE);
			}catch(Exception e){
				log.error(e, e);
			}
		}
	}
	
	public static void getGoodsListFromMidUrl() throws Exception{
		List<Map<String, Object>> midList = GmarketDBStorage.selectNotRunnedJobFromTable(GmarketDBStorage.midListTableName, null, 10000);
		for(Map<String, Object> row : midList){
			log.info("getGoodsListFromMidUrl " + row);
			try{
				String detailLink = (String)row.get("link");
				List<Map<String, Object>> goodsList = ListDetail.crawlOnePage(detailLink);  // ListMid.crawlLinkUrl(midLink);
				if(goodsList==null || goodsList.isEmpty()){
					log.info("FAIL_DETAIL_LINK " + detailLink);
					continue;
				}
				BasicCrawlTool.printTable(goodsList, "goodsList");
				GmarketDBStorage.saveList2DB(GmarketDBStorage.goodsListTableName, goodsList, "thumb", upsert);
				GmarketDBStorage.updateOne2Seen(GmarketDBStorage.midListTableName, "link", detailLink, "seen", Boolean.TRUE);
				Thread.sleep(3000);
			}catch(Exception e){
				log.error(e, e);
			}
		}
	}
	
	static boolean upsert = true;
	public static void getMainAndMidList() throws Exception{
		GmarketCrawlRun t = new GmarketCrawlRun();
		List<Map<String, Object>> mainList = t.crawlMainList(mainUrl);
		
//		CrawlGmarket.insertListOfMap2DB("gmarket.mainList", mainList, "link");
		GmarketDBStorage.saveList2DB("gmarket.mainList", mainList, "link", upsert);
		
		for(Map<String, Object> line : mainList){
			try{
				String midlink = (String)line.get("link");
				List<Map<String, Object>> midList = ListMid.crawlLinkUrl(midlink);
				GmarketDBStorage.saveList2DB(GmarketDBStorage.midListTableName, midList, "link", upsert);
				BasicCrawlTool.printTable(midList, "midList");
			}catch(Exception e){
				log.error(e, e);
			}
		}
	}
	
	public List<Map<String, Object>> crawlMainList(String mainUrl) throws Exception{
		log.info("start from mainUrl " + mainUrl);
		List<Map<String, Object>> mainList = ListOuter.crawlLinkUrl(mainUrl);
		BasicCrawlTool.printTable(mainList, "mainList");
		return mainList;
	}

	
	public static void dropTable() throws Exception{
//		GmarketDBStorage.dropTable(GmarketDBStorage.mainListTableName);
//		GmarketDBStorage.dropTable(GmarketDBStorage.goodsListTableName);
//		GmarketDBStorage.dropTable(GmarketDBStorage.goodscodeTableName);
//		GmarketDBStorage.dropTable(midListTableName);
//		GmarketDBStorage.dropTable(GmarketDBStorage.goodsIDetailnfoTableName);
		
	}
	
}
