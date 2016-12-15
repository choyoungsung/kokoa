package crawl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import job.util.MyUtil;

import org.apache.log4j.Logger;

public class CrawlDaumNews {
	static Logger log = Logger.getLogger("simple");
	public static void main(String[]args) throws Exception{
		crawlFromDaumSearch();
	} 
	
	static String url = "http://search.daum.net/search?w=news&req=tab&q=%s&cluster=y&viewio=i&repno=0&n=10&DA=PGD&p=%d";
	static String[] querys = {"현대카드", "오픈마켓", "삼성카드", "쿠팡",
		"쿠차", "비즈니스", "aws", "강산", "kbs", "키보드", "우산", "네이버", "영어", "인사", "OTP", "USB", "캐피탈", "현대캐피탈",  
		"md cerchandiser"};
	public static void crawlFromDaumSearch() throws Exception{
		for(String query : querys){
			log.info("### " + query);
			List<String> all = crawlDaumNews(query, 1, 2);
			for(String line : all){
				log.info(line);
			}
		}
	}
	
	public static List<String> crawlDaumNews(String query, int start, int end) throws Exception{
		List<String> res = new ArrayList<String>();
		for(int pageNum = start; pageNum<end;pageNum++){
			List<String> list = crawlDaumNews(query, pageNum);
			if(list!=null){
				res.addAll(list);
			}
		}
		return res;
	}
	
	public static String downloadOrNull(String query, int pageNum){
		try{
		String urlstr = String.format(url, URLEncoder.encode(query, "UTF-8"), pageNum);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", "Mozilla/5.0 (Windos NT 6.1; rv:46.0) Gecko/20100101 Firefox/46.0");
		String whole = MyUtil.requestGet(urlstr, headers,  "UTF-8");
		return whole;
		}catch(Exception e){
			return null;
		}
	}
	
	public static List<String> crawlDaumNews(String query, int pageNum) throws Exception{
		String whole = downloadOrNull(query, pageNum);
		if(whole==null){
			return null;
		}
		GrepTextArea grepTable = (new GrepTextArea.Builder()).setStartClueText("<div id=\"newsColl\" class=\"type_fulltext wid_f\">").addEndClueText("<div class=\"paging_comm\"></div>").build();
		String bigTable = grepTable.grep(whole, 0);
		
		GrepTextArea grepRow = (new GrepTextArea.Builder()).setStartClueText("<div class=\"cont_inner\">").addEndClueText("<span id").build();
		
		List<String> rows = grepRow.grepAsList(bigTable, 0);
		
		List<String> all = new ArrayList<String>();
		for(String row : rows){
			System.err.println(row);
			GrepTextArea grepTitle = (new GrepTextArea.Builder()).setStartClueText("target=\"_blank\"").addEndClueText("</a>").setBRefineHtml(true).build();
			List<String> titles = grepTitle.grepAsList(row, 0);
			
			GrepTextArea grepText = (new GrepTextArea.Builder()).setStartClueText("<p class=\"f_eb desc\">").addEndClueText("</p>").setBRefineHtml(true).build();
			List<String> texts = grepText.grepAsList(row, 0);
			all.add(titles.get(0));
			all.add(texts.get(0));
		}
		return all;
		
	}
	
}
