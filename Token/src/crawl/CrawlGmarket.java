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
import com.mongodb.client.model.UpdateOptions;

//http://category.gmarket.co.kr/listview/List.aspx?gdmc_cd=200002786&ecp_gdlc=&ecp_gdmc=
//http://category.gmarket.co.kr//listview/List.aspx?gdsc_cd=300021072&ecp_gdmc=200001966&ecp_gdlc=100000002
// http://item2.gmarket.co.kr/item/detailview/Item.aspx?goodscode=799069571 << 이 주소에서 text 추출이 실패됨

public class CrawlGmarket {
	static Logger log = Logger.getLogger("simple");
	static String dbName = "sizfire";
	
	static String mainUrl = "http://gmarket.co.kr/";
//	static String url1st = "http://category.gmarket.co.kr/listview/List.aspx?gdmc_cd=200002786&ecp_gdlc=&ecp_gdmc=";
	static String url1st = "http://category.gmarket.co.kr/listview/List.aspx?gdsc_cd=300021072&ecp_gdmc=200001966&ecp_gdlc=100000002";
	static String urlformat = "http://category.gmarket.co.kr/listview/List.aspx?gdsc_cd=%s&ecp_gdmc=%s&ecp_gdlc=%s";
	
	public static void main(String[] args) throws Exception{
//		doAll();
		e();
//		GmarketDBStorage.updateTable();
//		System.err.println(ItemDetail.crawlItemDetailUrl("797057313"));
	}
	
	
	
	public static void doAll() throws Exception{
		log.info("start from mainUrl " + mainUrl);
		List<Map<String, Object>> mainList = ListOuter.crawlLinkUrl(mainUrl);
		BasicCrawlTool.printTable(mainList, "mainList");

		for(Map<String, Object> line : mainList){
			try{
				String midlink = (String)line.get("link");
				List<Map<String, Object>> midList = ListMid.crawlLinkUrl(midlink);
				BasicCrawlTool.printTable(midList, "midList");
				for(Map<String, Object> midLine : midList){
					try{
						String detailLink = (String)midLine.get("link");
						List<Map<String, Object>> goodsList = ListDetail.crawlOnePage(detailLink);
						ItemDetail.fillItemDetailInfo(goodsList, 3000);
						BasicCrawlTool.printTable(goodsList, "goodsList");
					}catch(Exception e){
						log.error(e, e);
					}
					Thread.sleep(3000);
				}
			}catch(Exception ee){
				log.error(ee, ee);
			}
		}
	}
	
	public static void a() throws Exception{
		List<Map<String, Object>> table = ListDetail.crawlOnePage(url1st);
		BasicCrawlTool.printTable(table, "");
	}

	public static void b() throws Exception{
		ListOuter.crawlLinkUrl(mainUrl);
	}
	
	public static void c() throws Exception{
		List<Map<String, Object>> res = ListMid.crawlLinkUrl("http://category.gmarket.co.kr/listview/List.aspx?gdsc_cd=300025057&ecp_gdlc=100000043&ecp_gdmc=200002452");
		System.err.println(res);
	}
	
	public static void d() throws Exception{
		ItemDetail.crawlLinkUrl("http://category.gmarket.co.kr/listview/List.aspx?gdsc_cd=300025057", "300025057");
	}

	public static void e() throws Exception{
//		String paramOfGmarket = "gdsc_cd=300026735&ecp_gdlc=100000104&ecp_gdmc=200002686";
//		Search2nd.traverseAndCrawl(paramOfGmarket, 100);
		String param = Search2nd.getParam("http://category.gmarket.co.kr/listview/List.aspx?gdsc_cd=300026735&ecp_gdlc=100000104&ecp_gdmc=200002686");
		System.err.println(param);
	}
	
	
	public static class Search2nd{
		static String savedir = "search2nd";
//		http://category.gmarket.co.kr/listview/List.aspx?gdsc_cd=300026735&ecp_gdlc=100000104&ecp_gdmc=200002686
// >>			http://category.gmarket.co.kr/subpage/SearchItemListView.aspx?page=1&page2=1&page_size=1000&list_type=IMG&gdsc_cd=300026735&ecp_gdlc=100000104&ecp_gdmc=200002686
		static String search2ndFormat = "http://category.gmarket.co.kr/subpage/SearchItemListView.aspx?page=%d&page2=1&page_size=80&list_type=IMG&%s";
		
		public static String getParam(String url){
			if(url==null){
				return null;
			}
			int idx = url.indexOf("?");
			if(idx<0){
				return null;
			}
			
			return url.substring(idx+1);
		}
		
		public static List<Map<String, Object>> traverseAndCrawl(String paramOfGmarket, long sleepTime) throws Exception{
			if(!isProperParamOfGmarket(paramOfGmarket)){
				log.info("REJECT.traverseAndCrawl BY INVALID PARAM PATTERN " + paramOfGmarket);
				return null;
			}
			
			List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
			for(int pageNum = 1; pageNum<100;pageNum++){
				try{
					String whole = crawlOne(pageNum, paramOfGmarket);
					if(whole==null || whole.isEmpty()){
						break;
					}
					
					List<Map<String, Object>> parsed = set1(whole);
					if(parsed!=null){
						res.addAll(parsed);
					}
					Thread.sleep(sleepTime);
					
					if(maybeEnd(whole, parsed)){
						break;
					}
				}catch(Exception e){
					log.error(e, e);
				}
			}
			return res;
		}
		
		private static boolean maybeEnd(String whole, List<Map<String, Object>> parsed){
			if(whole==null || whole.isEmpty() || parsed==null || parsed.isEmpty())
				return true;
			
//			if(whole.length()<100*1024){
//				return true;
//			}
			
			return false;
		}
		
		private static List<Map<String, Object>> set1(String whole) throws Exception{
			GrepTextArea grepTable = (new GrepTextArea.Builder()).setStartClueText("<div id=\"itemListPosition\"></div>").addEndClueText("<div class=\"paginate_more\">").addEndClueText("<div class=\"search-condition\">").build(); 
			String bigTable = grepTable.grep(whole, 0);
			if(bigTable==null || bigTable.isEmpty()){
				return null;
			}
//			MyUtil.saveText2File(bigTable, "a.html");
			
			GrepTextArea grepRow = (new GrepTextArea.Builder()).setStartClueText("<div class=\"item_info\">").addEndClueText("<span class=\"delivery\">").build();
			List<String> rows = grepRow.grepAsList(bigTable, 0);
//			
			List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
			for(String row : rows){
				try{
					Map<String, Object > line = new HashMap<String, Object>();
//	//				
					BasicCrawlTool.findColumnAndAdd( "thumb", row, "<img src=\"" ,  "\" ",line, null);
					BasicCrawlTool.findColumnAndAdd( "title", row, "<span class=\"title\">", "</span>",line);
					BasicCrawlTool.findColumnAndAdd( "price", row, "<span class=\"price\">", "</span>",line);
					
					String thumb = (String)line.get("thumb");
					String goodscode = BasicCrawlTool.getNameFromUrl(thumb, "http://gdimg", ".jpg");
					line.put("goodscode", goodscode);
					System.err.println(line);
					if(line!=null && !line.isEmpty()){
						res.add(line);
					}
				}catch(Exception e){
					log.error(e, e);
				}
			}
			return res;
		}
		
		
		private static String crawlOne(int pageNum, String paramOfGmarket) throws Exception{
			log.info("Search2nd.crawlOne " + pageNum + " " + paramOfGmarket);
			String url = String.format(search2ndFormat, pageNum, paramOfGmarket);
			String saveFileName = url.substring(url.indexOf("?")+1).replaceAll("&", "_");
			String whole = BasicCrawlTool.getWithMozillaHeader(url, "euc-kr", savedir, saveFileName, ".html");
			return whole;
		}
		
		private static boolean isProperParamOfGmarket(String paramOfGmarket){
//			gdsc_cd=300026738&ecp_gdlc=100000104&ecp_gdmc=200002686
			if(paramOfGmarket==null || paramOfGmarket.isEmpty()){
				return false;
			}
			if(paramOfGmarket.indexOf("gdsc_cd")<0){
				return false;
			}
			if(paramOfGmarket.indexOf("ecp_gdlc")<0){
				return false;
			}
			if(paramOfGmarket.indexOf("ecp_gdmc")<0){
				return false;
			}
			return true;
		}
		
	}
	
	public static class ItemDetail{
//		static String url1st = "http://item2.gmarket.co.kr/Item/detailview/Item.aspx?goodscode=807483555";
		static String url1st = "http://item2.gmarket.co.kr/Item/detailview/Item.aspx?goodscode=798484120";
		
		static String urlFormat = "http://item2.gmarket.co.kr/Item/detailview/Item.aspx?goodscode=%s";
		
		// goodsList.get(0)는 {catogory1=e쿠폰, catogory2=게임/영화 관람권} 정보
		//{thumb=http://gdimg.gmarket.co.kr/goods_image2/middle_jpgimg3/727/371/727371383.jpg, price=8,100원10,000원 19%할인, title=[메가박스]  (메가박스) 1인 관람권(주말)}
		// <thumb, price, title, text, category1, category2>를 구성한다. 
		public static void fillItemDetailInfo(List<Map<String, Object>> goodsList, long sleepTime) throws Exception{
			if(goodsList==null || goodsList.isEmpty()){
				return;
			}
			Map<String, Object> categoryInfo = goodsList.get(0);
			
			for(Map<String, Object> line : goodsList){
				if(categoryInfo!=null){
					line.putAll(categoryInfo);
				}
				
				String thumb = (String)line.get("thumb");
				if(thumb==null){
					continue;
				}
				
				String goodscode = BasicCrawlTool.getNameFromUrl(thumb, "http://gdimg", ".jpg");//getgoodsCodeFromThumbUrl(thumb);
				if(goodscode==null){
					continue;
				}
				
				Thread.sleep(sleepTime);
				String itemDetailUrl = String.format(urlFormat, goodscode);
				Map<String, Object> itemDetail = crawlLinkUrl(itemDetailUrl, goodscode);
				if(itemDetail!=null){
					line.putAll(itemDetail);
				}
			}
		}
		
		public static Map<String, Object> crawlItemDetailUrl(String goodscode) throws Exception{
			String itemDetailUrl = String.format(urlFormat, goodscode);
			return crawlLinkUrl(itemDetailUrl, goodscode);
		}
		
		
		public static Map<String, Object> crawlLinkUrl(String itemDetailUrl, String goodscode) throws Exception{
			log.info(itemDetailUrl);
			String whole = BasicCrawlTool.getWithMozillaHeader(itemDetailUrl, "euc-kr", "itemdetail", goodscode, ".html");
			
			Map<String, Object> line = new HashMap<String, Object>();
			line.put("goodscode", goodscode);
			
			boolean got = BasicCrawlTool.findColumnAndAdd("text", whole, "<div class=\"tit-goodsnum\">", "<div class=\"goodsnum\">", "<p class=\"tit\">", "</p>", line);
			if(!got){
				boolean got2 = BasicCrawlTool.findColumnAndAdd("text", whole, "<div class=\"top-cont\">", "<div class=\"goods-explain\">", "<h2>", "</h2>", line);
			}
			
			got = BasicCrawlTool.findColumnAndAdd("brand", whole, "<table summary=\"상품정보\" class=\"goods-info mgt30\">", "</tr>", "<th scope=\"row\">브랜드</th>", "</td>", line);
			
			got = BasicCrawlTool.findColumnAndAdd("seller", whole, "<p class=\"goods-info-txt5\">", "<ul class=\"last\">", "상호/대표자", "</li>", line);
			
			
//			got = BasicCrawlTool.findColumnAndAdd("category1", whole, "<div id=\"headerCate\" class=\"category\">", "<script", ".aspx\">", "</a>", line);
//			got = BasicCrawlTool.findColumnAndAdd("category1", whole, "<div id=\"headerCate\" class=\"category\">", "<script", ".aspx\">", "</a>", line);
			
			System.err.println(line);

			return line;
		}
		
//		public static Map<String, Object> crawlLinkUrl(String itemDetailUrl, String goodscode) throws Exception{
//			log.info(itemDetailUrl);
//			String whole = BasicCrawlTool.getWithMozillaHeader(itemDetailUrl, "euc-kr", "itemdetail", goodscode, ".html");
//			
//			Map<String, Object> line = new HashMap<String, Object>();
//			line.put("goodscode", goodscode);
//			
//			boolean got = BasicCrawlTool.findColumnAndAdd("text", whole, "<div class=\"tit-goodsnum\">", "<div class=\"goodsnum\">", "<p class=\"tit\">", "</p>", line);
//			if(!got){
//				boolean got2 = BasicCrawlTool.findColumnAndAdd("text", whole, "<div class=\"top-cont\">", "<div class=\"goods-explain\">", "<h2>", "</h2>", line);
//			}
//			System.err.println(line);
//
//			return line;
//		}
		
	}
	
	
	
//	String dir, String saveFileName, String fileExt
	public static class ListMid{
		static String mainUrl = "http://category.gmarket.co.kr/listview/L100000079.aspx";
//		static String listUrlFormat = "http://category.gmarket.co.kr/listview/L%s.aspx";
		
		public static List<Map<String, Object>> crawlLinkUrl(String mainUrl) throws Exception{
			log.info(mainUrl);
			String id = BasicCrawlTool.getIDFromUrl(mainUrl); //.getNameFromUrl(mainUrl, "http://category", ".aspx");
			String whole = BasicCrawlTool.getWithMozillaHeader(mainUrl, "euc-kr", "listmid", id, ".html"); 
			
			List<Map<String, Object>> res = set1(whole);
			if(res==null){
				res = set2(whole);
			}
			
			return res;

		}
		
		private static List<Map<String, Object>> set2(String whole){
			GrepTextArea grepTable = (new GrepTextArea.Builder()).setStartClueText("<li class=\"plusitem firstitem\">").addEndClueText("<div class=\"paginate_more\">").build(); 
			String bigTable = grepTable.grep(whole, 0);
			if(bigTable==null || bigTable.isEmpty()){
				return null;
			}
//			MyUtil.saveText2File(bigTable, "a.html");
			
			GrepTextArea grepRow = (new GrepTextArea.Builder()).setStartClueText("<span class=\"title\">").addEndClueText("</span>").build();
			List<String> rows = grepRow.grepAsList(bigTable, 0);
//			
			List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
			for(String row : rows){
				try{
					Map<String, Object > line = new HashMap<String, Object>();
	//				
					BasicCrawlTool.findColumnAndAdd( "link", row, "http:" ,  "',",line, "http:%s");
					BasicCrawlTool.findColumnAndAdd( "category", row, ";\">", "<",line);
					if(line!=null && !line.isEmpty()){
						res.add(line);
					}
				}catch(Exception e){
					log.error(e, e);
				}
			}
			return res;
		}
		
		private static List<Map<String, Object>> set1(String whole){
			GrepTextArea grepTable = (new GrepTextArea.Builder()).setStartClueText("<div class=\"mid-all-list\" id=\"divMiddleLayer\">").addEndClueText("<div class=\"brand-name-list\"").addEndClueText("<div class=\"search-condition\">").build(); 
			String bigTable = grepTable.grep(whole, 0);
			if(bigTable==null || bigTable.isEmpty()){
				return null;
			}
//			MyUtil.saveText2File(bigTable, "a.html");
			
			GrepTextArea grepRow = (new GrepTextArea.Builder()).setStartClueText("<li><a").addEndClueText("/a>").build();
			List<String> rows = grepRow.grepAsList(bigTable, 0);
//			
			List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
			for(String row : rows){
				try{
					Map<String, Object > line = new HashMap<String, Object>();
	//				
					BasicCrawlTool.findColumnAndAdd( "link", row, "http:" ,  "',",line, "http:%s");
					BasicCrawlTool.findColumnAndAdd( "category", row, ";\">", "<",line);
					if(line!=null && !line.isEmpty()){
						res.add(line);
					}
				}catch(Exception e){
					log.error(e, e);
				}
			}
			return res;
		}
		
	}
	
	public static class ListOuter{
		static String listUrlFormat = "http://category.gmarket.co.kr/listview/L%s.aspx";
		
		public static List<Map<String, Object>> crawlLinkUrl(String mainUrl) throws Exception{
			log.info(mainUrl);
			String id = BasicCrawlTool.getNameFromUrl(mainUrl, "http://category", ".aspx");
			String whole = BasicCrawlTool.getWithMozillaHeader(mainUrl, "euc-kr", "listouter", id, ".aspx");
			GrepTextArea grepTable = (new GrepTextArea.Builder()).setStartClueText("<div id=\"main_gnb\" class=\"gnb\">").addEndClueText("<div class=\"m_promotion\">").build();
			String bigTable = grepTable.grep(whole, 0);
			
			GrepTextArea grepRow = (new GrepTextArea.Builder()).setStartClueText("<a href=\"javascript:GoSNAChannel(&#39;CHM2A001&#39;,&#39;http://category.gmarket.co.kr/listview/LList.aspx?").addEndClueText("/a>").build();
			List<String> rows = grepRow.grepAsList(bigTable, 0);
			
			List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
			for(String row : rows){
//				System.err.println(row);
				Map<String, Object > line = new HashMap<String, Object>();
				
				BasicCrawlTool.findColumnAndAdd( "gdlc_cd", row, "gdlc_cd=", "&#39",line);
				BasicCrawlTool.findColumnAndAdd( "category", row, ";\">", "<",line);
				
				if(line!=null && !line.isEmpty()){
					String gdlc_cd = (String)line.get("gdlc_cd");
					String link = String.format(listUrlFormat, gdlc_cd);
					line.put("link", link);
//					System.err.println(line);
					res.add(line);
				}
			}
			return res;
		}
	}
	
	
	public static class ListDetail{
		public static List<Map<String, Object>> crawlOnePageByGDCD(String gdmc_cd, String ecp_gdmc, String ecp_gdlc) throws Exception{
			String url = String.format(urlformat, gdmc_cd, ecp_gdmc, ecp_gdlc);
			List<Map<String, Object>> res = crawlOnePage(url);
			return res;
		}
		
		//"http://category.gmarket.co.kr/listview/List.aspx?gdsc_cd=300021072&ecp_gdmc=200001966&ecp_gdlc=100000002"
		public static List<Map<String, Object>> crawlOnePage(String url) throws Exception{
			log.info(url);
			String id = BasicCrawlTool.getIDFromUrl(url);
			String whole = BasicCrawlTool.getWithMozillaHeader(url, "euc-kr", "listdetail", id, ".html");
	//		MyUtil.saveText2File(whole, "a.html");
			List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
			
			grepCategory(whole, res);
			
			GrepTextArea grepTable = (new GrepTextArea.Builder()).setStartClueText("<div class=\"item_info\">").addEndClueText("<div class=\"paginate_more\">").build();
			String bigTable = grepTable.grep(whole, 0);
			
//			int lastIdx = grepPowerSell(bigTable, res);
			grepPlusItem(bigTable, 0, res);
			return res;
		}
		
		private static void grepCategory(String whole,  List<Map<String, Object>> res) throws Exception{
			GrepTextArea grepCategory = (new GrepTextArea.Builder()).setStartClueText("<div class=\"select-cate-cont\">").addEndClueText("<div class=\"cate-detail-list\">").build();
			String headerTxt = grepCategory.grep(whole, 0);
			GrepTextArea grepRow = (new GrepTextArea.Builder()).setStartClueText("<span >").addEndClueText("</span>").build();
			List<String> rows = grepRow.grepAsList(headerTxt, 0);
			Map<String, Object> line = new HashMap<String, Object>();
			if(rows!=null && rows.size()>1){
				line.put("catogory1", rows.get(0));
				line.put("catogory2", rows.get(1));
				res.add(line);
			}
		}
		
		private static void grepPlusItem(String bigTable, int idxOfBigTable, List<Map<String, Object>> res) throws Exception{
			GrepTextArea grepRow = (new GrepTextArea.Builder()).setStartClueText("<li class=").addEndClueText("</li>").build();
			List<String> rows = grepRow.grepAsList(bigTable, idxOfBigTable);
			for(String row : rows){
				Map<String, Object > line = new HashMap<String, Object>();
				
				BasicCrawlTool.findColumnAndAdd("thumb", row, "<img src=\"", "\"", line);
				BasicCrawlTool.findColumnAndAdd("title", row, "<span class=\"title\">", "</span>", line);
				BasicCrawlTool.findColumnAndAdd("price", row, "<span class=\"price\">", "</span>", line);
				
				if(line!=null && !line.isEmpty()){
					res.add(line);
				}
	//			System.err.println(line);
			}
		}
		
		private static int grepPowerSell(String bigTable, List<Map<String, Object>> res) throws Exception{
			GrepTextArea grepRow = (new GrepTextArea.Builder()).setStartClueText("<li>").addEndClueText("</li>").build();
			List<String> rows = grepRow.grepAsList(bigTable, 0);
			
			for(String row : rows){
				Map<String, Object > line = new HashMap<String, Object>();
				
				BasicCrawlTool.findColumnAndAdd( "minishop", row, "class=\"minishop\">", "</a></span>",line);
				BasicCrawlTool.findColumnAndAdd("title", row, "<span class=\"title\">", "</span>", line);
				BasicCrawlTool.findColumnAndAdd("thumb", row, "<span class=\"thumb\"><img src=\"", "\" alt", line);
				BasicCrawlTool.findColumnAndAdd("price", row, "<span class=\"price\">", "</span>", line);
				BasicCrawlTool.findColumnAndAdd("origin_price", row, "<span class=\"origin_price\">", "</span>", line);
				
				if(line!=null && !line.isEmpty()){
					res.add(line);
				}
	//			System.err.println(line);
	
			}
			return grepRow.lastIdx;
		}
	}
	
}
