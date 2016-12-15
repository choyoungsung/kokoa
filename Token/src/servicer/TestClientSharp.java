package servicer;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.google.gson.Gson;

import job.jsoup.Getter4MD;
import job.util.MyUtil;

public class TestClientSharp {
	public static void main(String[] args) throws Exception{
		f();
	}
	
	public static void f() throws Exception{
//		String res = MyUtil.requestGet("http://www.adidas.co.kr", null, "UTF-8");
//		String res = Getter4MD.reqGetWithMozillaHeader("http://www.adidas.co.kr", "UTF-8");
		String res = Getter4MD.reqGetWithMozillaHeader("https://www.diapers.com", "UTF-8");
		
		System.err.println(res);
	}
	
	//{"url":"http://www.adidas.co.kr", "outputMode":"1"}
	public static void e() throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("url", "http://www.adidas.co.kr");
		params.put("outputMode", "1");
		Gson gson = new Gson();
		
		String res = MyUtil.requestPost("http://127.0.0.1:8090/token.jsoup", null, gson.toJson(params));
		System.err.println(res);
	}
	
	public static void d() throws Exception{
		Gson gson = new Gson();
		
		
//		Map<String, Object> dic = new HashMap<String, Object>();
//		dic.put("category2", "점퍼/패딩");
//		List<Object> arr = new ArrayList<Object>();
//		arr.add(dic);
		
		List<Document> paramList = new ArrayList<Document>();
		Document query = new Document("$group", new Document("_id", "$category2").append("count", new Document("$sum", 1) )) ;
		
		paramList.add(query);
		
		String postData = gson.toJson(paramList);
		System.err.println(postData);
		
		String res = MyUtil.requestPost("http://127.0.0.1:8090/token.mongodb?operation=aggregate&collection=gmarket", null, postData);
		System.err.println(res);
	}
	
	public static void c() throws Exception{
		Gson gson = new Gson();
		
		Map<String, Object> dic = new HashMap<String, Object>();
		dic.put("category2", "점퍼/패딩");
		String postData = gson.toJson(dic);
		
		System.err.println(postData);
		
		String res = MyUtil.requestPost("http://127.0.0.1:8090/token.mongodb?collection=gmarket&operation=find", null, postData);
		System.err.println(res);
	}
	
	public static void b() throws Exception{
		Gson gson = new Gson();
		Map<String, Object> dic = new HashMap<String, Object>();
//		dic.put("text", "소격동 sohot ");
		dic.put("text", "소격동so hot " + "Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ 소격동");
		dic.put("N", 2);
		dic.put("bHangulAndEtc", Boolean.TRUE);
		dic.put("bDoCharNormalize", Boolean.TRUE);
		String postData = gson.toJson(dic);
		String res = MyUtil.requestPost("http://127.0.0.1:8090/token.parse", null, postData);
		System.err.println(res);
	}
	
	public static void a() throws Exception{
		String res = MyUtil.requestGet("http://127.0.0.1:8090/token.parse?text=#abc", "UTF-8");
		System.err.println(res);
	}
	
}
