package job.jsoup;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import job.util.HttpReq;
import job.util.MyUtil;

public class Getter4MD {
	static Map<String, String> requestHeader;
	static{
		requestHeader = new HashMap<String, String>();
		requestHeader.put("User-Agent", "Mozilla/5.0 (Windos NT 6.1; rv:46.0) Gecko/20100101 Firefox/46.0");
		
//		requestHeader.put("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
//		requestHeader.put("Accept-Encoding", "gzip, deflate, sdch");
//		requestHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	}
	
	static String htmldir = "./htmldir";
	
	public static void main(String[] args) throws Exception{
		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("accept", "application/json");
//		headers.put("accept-encoding", "gzip, deflate, br");
//		headers.put("accept-language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
//		headers.put("content-length", "4");
		headers.put("content-type", "application/json");
//		headers.put("cookie", "s_channeltrafic_p=Direct%20Load; c_m=undefinedDirect%20LoadDirect%20Load; s_vnum=1470208064261%26vn%3D2; JSESSIONID=DDE2C62873E4AA54B25B77D06E756EB9; wcs_bt=unknown:1467620062; STICKED-TO=R3863310585; CKI_MARKET=kr; CKI_MARKET=kr; CKI_LANGUAGE=ko; CKI_LANGUAGE=ko; s_evar2=KRko_order.capsules_coffee_NA_NA; nestmsStr=1467616064015; s_cc=true; s_fid=6A4B065CE2388FB0-3B71914E523BDFED; s_dl=1; s_evar21=Direct%20Load_B2B; s_nr=1467620241786-Repeat; s_invisit=true; s_visit=1; s_sq=%5B%5BB%5D%5D; _sdsat_AppVersion=NC2-classic; _sdsat_AppPlatform=desktop-site; s_vi=[CS]v1|2BBD059E05488C1E-40000102E003891F[CE]");
//		headers.put("origin", "https://www.nespresso.com");
//		headers.put("referer", "https://www.nespresso.com/kr/ko/Order-Capsules");
//		headers.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
//		headers.put("x-requested-with", "XMLHttpRequest");
		
		String res = MyUtil.requestPost("https://www.nespresso.com/kr/ko/getQuickCapsules/original", headers, "null");
		System.err.println(res);
	}
	
	public static String getHtmlContent(String url, String encoding, boolean bNew) throws Exception{
		String html = null;
		if(bNew){
			html = reqGetWithMozillaHeader(url, encoding);
		}
		String fullPath = getFullFileName(url);
		File f = new File(fullPath);
		if(!f.exists()){
			html = reqGetWithMozillaHeader(url, encoding);
			MyUtil.saveText2File(html, fullPath, encoding);
		}else{
			html = MyUtil.loadFileAsStr(fullPath);
		}
		
		return html;
	}
	
	private static String getFullFileName(String url){
		String fileName = url2FileName(url);
		MyUtil.mkDirIfNotExist(htmldir);
		return MyUtil.concatDirName(htmldir, fileName, File.separator);
	}
	
	private static String url2FileName(String urlstr ){
		String name = urlstr.replaceAll("[/:?\\.]", "_");
		return name + ".html";
	}
	
	public static String reqGetWithMozillaHeader(String url, String encoding) throws Exception{
		
//		String html = MyUtil.requestGet(url, requestHeader, encoding);
		HttpReq hr = new HttpReq();
		String html = hr.requestHttpGetWithRedirection(url, requestHeader, encoding);
		return html;
	}
}
