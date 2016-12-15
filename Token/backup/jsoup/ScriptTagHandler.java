package job.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScriptTagHandler {
	public static void main(String[] args) throws Exception{
		a();
	}
	
	
	public static void a() throws Exception{
		String url = "https://www.nespresso.com/kr/ko/Order-Capsules?icid=KRko_order.capsules_coffee_NA_NA"; // 원하는 데이터가 <script>  내의 javascript var로 존재하는 경우엔?
		String textQuery = "인텐소";
		String textQuery2 = null;
		String encoding = "UTF-8"; 
		boolean bNew =false;
		String html = Getter4MD.getHtmlContent(url, encoding, bNew);
//		System.err.println(html);
		
		Document doc = Jsoup.parse(html);
		Elements scripts = getAllScript(doc);
		
		Elements filtered = getTargetScriptData(scripts, textQuery, textQuery2);
		for(Element script : filtered){
//		for(Element script : scripts){
//			System.err.println(script);
			System.err.println(script.data());
			System.in.read(new byte[1]);
		}
	}
	
	public static Elements getAllScript(Document doc){
		Elements scripts = doc.select("script");
		return scripts;
	}
	
	public static Elements getTargetScriptData(Elements scripts, String textQuery, String textQuery2){
		Elements res = new Elements();
		for(Element script : scripts){
			String data = script.data();
			if(chkQueryIn(data, textQuery, textQuery2)){
				res.add(script);
			}
		}
		return res;
	}
	
	public static boolean chkQueryIn(String src, String textQuery, String textQuery2){
		if(src==null){
			return false;
		}
		
		if(textQuery==null){
			return true;
		}
		
		int idx1 = src.indexOf(textQuery);
		if(idx1<0){
			return false;
		}
		if(textQuery2==null){
			return true;
		}
		
		int idx2 = src.indexOf(textQuery2, idx1);
		if(idx2<0){
			return false;
		}
		return true;
	}
	
	// script내의 데이터는 각 script단위로 별도 처리
	// 순수 text로 간주하고, 원하는 데이터가 존재하는 텍스트의 범위를 찾는다. 
}
