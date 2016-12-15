package crawl;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.google.gson.Gson;

import job.util.MyUtil;

public class BasicCrawlTool {
	static Logger log = Logger.getLogger("simple");
	static private Map<String, String> headersMozilla = new HashMap<String, String>();
	static{
		headersMozilla.put("User-Agent", "Mozilla/5.0 (Windos NT 6.1; rv:46.0) Gecko/20100101 Firefox/46.0");
	}
	
	//"http://category.gmarket.co.kr/listview/List.aspx?gdsc_cd=300021072&ecp_gdmc=200001966&ecp_gdlc=100000002"
	public static String getIDFromUrl(String url){
		int idx = url.indexOf("?");
		if(idx<0){
			return getNameFromUrl(url, "http", "");
		}else{
			String params = url.substring(idx+1, url.length());
			return params.replaceAll("&", "_");
		}
	}
	
	//http://gdimg.gmarket.co.kr/goods_image2/middle_jpgimg3/727/371/727371383.jpg
		public static String getNameFromUrl(String url, String urlStartSubstr, String urlEndSubstr){
			if(url==null){
				return null;
			}
//			if(url.startsWith("http://gdimg") && thumbUrl.endsWith(".jpg")){
			if(url.startsWith(urlStartSubstr) && url.endsWith(urlEndSubstr)){
				int start = url.lastIndexOf("/");
				int end = url.indexOf(".", start);
				return url.substring(start+1, end);
			}else{
				return null;
			}
		}
	
	public static void findColumnAndAdd(String columnName, String row, String startClue, String endClue, Map<String, Object > line) throws Exception{
		findColumnAndAdd(columnName, row, startClue, endClue, line, null); 
	}
	
	// true면 탐색 성공
	public static boolean findColumnAndAdd(String title, String whole, String startOuter, String endOuter, String startInner, String endInner, Map<String, Object> res) throws Exception{
		GrepTextArea grepTable2nd = (new GrepTextArea.Builder()).setStartClueText(startOuter).addEndClueText(endOuter).build(); 
		String bigTable2nd = grepTable2nd.grep(whole, 0);
		if(bigTable2nd==null){
			return false;
		}
//		System.err.println(bigTable2nd);
		GrepTextArea grepRow = (new GrepTextArea.Builder()).setStartClueText(startInner).addEndClueText(endInner).setBRefineHtml(true).build();
		List<String> rows = grepRow.grepAsList(bigTable2nd, 0);
//		System.err.println(rows);
		if(rows!=null && !rows.isEmpty()){
			res.put(title, rows.get(0).trim());
			return true;
		}else{
			return false;
		}
	}
	
	public static void findColumnAndAdd(String columnName, String row, String startClue, String endClue, Map<String, Object > line, String foundFormat) throws Exception{
		try{
			GrepTextArea grepTitle = (new GrepTextArea.Builder()).setStartClueText(startClue).addEndClueText(endClue).setBRefineHtml(true).build();
			List<String> found = grepTitle.grepAsList(row, 0);
			if(found!=null &&!found.isEmpty()){
				String target = found.get(0).trim();
				if(foundFormat!=null){
					target = String.format(foundFormat, target);
				}
				line.put(columnName, target);
			}
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static void printDocTable(List<Document> table, String title){
		log.info(title);
		Gson gson = new Gson();
		for(int i=0;i<table.size();i++){
			log.info(gson.toJson(table.get(i)));
//			log.info(table.get(i));
		}
	}
	
	public static void printTable(List<Map<String, Object>> table, String title){
		log.info(title);
		Gson gson = new Gson();
		for(int i=0;i<table.size();i++){
			log.info(gson.toJson(table.get(i)));
//			log.info(table.get(i));
		}
	}
	
	public static String getWithMozillaHeader(String urlstr, String encoding, String dir, String saveFileName, String fileExt) throws Exception{
		String whole = getWithMozillaHeader(urlstr,  encoding);
		
		MyUtil.mkDirIfNotExist(dir);
		
		String fullPath = MyUtil.concatDirName(dir, saveFileName + fileExt, File.separator);
		
		MyUtil.saveText2File(whole, fullPath);
		return whole;
	}
	
	private static String getWithMozillaHeader(String urlstr, String encoding){
			try{
				String whole = MyUtil.requestGet(urlstr, headersMozilla,  encoding);
				return whole;
			}catch(Exception e){
				return null;
			}
	}
}
