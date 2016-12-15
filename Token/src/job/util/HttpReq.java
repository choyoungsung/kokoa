package job.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class HttpReq {
	static Logger log = Logger.getLogger("");
	public static void main(String[] args) throws Exception{
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put("User-Agent", "Mozilla/5.0 (Windos NT 6.1; rv:46.0) Gecko/20100101 Firefox/46.0");
		requestHeader.put("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
//		requestHeader.put("Accept-Encoding", "gzip, deflate, sdch");
		requestHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//		requestHeader.put("Host", "www.diapers.com");
//		requestHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		requestHeader.put("Connection", "keep-alive");
//		requestHeader.put("Cookie", "session-id=187-8758167-7279422; ubid-main=185-5833638-4306909; VISITOR_ID=0a74d119-609b-4dc4-ad30-48b1ae311e59; _ga=GA1.2.1270384307.1467782632; __utma=1.1270384307.1467782632.1467782632.1467787838.2; __utmz=1.1467782632.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); session-id-time=2082787201l; session-token=H+OxeLsypHcgmapx00QjqI10ewr0tvbe1S80rie0fBknF0K0VgLyZ3mzJFxfs9HA948Aa8I/NjQZw3c1GZiD6iWj2WEC4Vqjn7kMcEFAIHs1K4VqoQIsxU3m9vhigVRWepF/+y32GZnWgFFJzcHzCoirUC2taNaY1l33Lmv4KUmh53GakLyrLFEHTCv0AcsK; cvo_sid1=DQA5NGNYK55Z; _caid=fd719d1b-9768-4d6f-ad3a-8bc827f4bebc; cvo_tid1=BB7fNqbXsHc|1467782626|1467787931|8; IRF_1198=%7Bvisits%3A2%2Cuser%3A%7Btime%3A1467782633962%2Cref%3A%22direct%22%2Cpv%3A3%2Ccap%3A%7B%7D%2Cv%3A%7B%7D%7D%2Cvisit%3A%7Btime%3A1467787839254%2Cref%3A%22direct%22%2Cpv%3A2%2Ccap%3A%7B%7D%2Cv%3A%7B%7D%7D%2Clp%3A%22https%3A//www.diapers.com/%22%2Cdebug%3A0%2Ca%3A1467787939058%7D; __utmb=1.2.10.1467787838; __utmt=1; _gat_UA-555486-8=1; _cavisit=155bef903b2|");
		
		HttpReq hr = new HttpReq();
		String res = hr.requestHttpGetWithRedirection("https://www.diapers.com", requestHeader, "UTF-8");
		System.out.println(res);
	}

	HttpURLConnection urlconn = null;
	public String requestHttpGetWithRedirection(String urlstr, Map<String, String> requestHeaders, String encodingType) throws Exception{
		try{
			log.trace("\n\nREQ " + urlstr);
			log.trace("REQ " + requestHeaders);
			String response = requestHttpGet(urlstr, requestHeaders, encodingType);
			System.out.println(response);
			if((urlconn!=null) && (300<= urlconn.getResponseCode()) && (urlconn.getResponseCode()<400)){
				Map<String, List<String>> hdr = urlconn.getHeaderFields();
				List<String> setCookies =  hdr.get("Set-Cookie");//urlconn.getHeaderField("Set-Cookie");
				
				if(setCookies!=null){
					requestHeaders.put("Cookie", MyUtil.arrayJoin(";", setCookies));
				}
//				System.err.println(Location);
				String Location = urlconn.getHeaderField("Location");
				if(Location!=null){
					response = requestHttpGetWithRedirection(Location, requestHeaders, encodingType);
				}
			}
			return response;
		}catch(Exception e){
			log.error(e,e);
//			e.printStackTrace();
//			String a = e.getMessage();
//			System.err.println(a);
			return null;
		}
	}
	
	public String requestHttpGet(String urlstr, Map<String, String> requestHeaders, String encodingType) throws Exception{
		URL url = new URL(urlstr);
		HttpURLConnection.setFollowRedirects(false);
		this.urlconn =(HttpURLConnection)url.openConnection();
		HttpURLConnection.setFollowRedirects(true);
		if(requestHeaders!=null){
			Set<String> keys = requestHeaders.keySet();
			for(String key : keys){
				urlconn.setRequestProperty(key, requestHeaders.get(key));
			}
		}
		
		log.trace("\nRES " + urlconn.getResponseCode());
		
		Map<String, List<String>> resHeaders = urlconn.getHeaderFields();
		log.trace("RES " + resHeaders);
		
		InputStream is = urlconn.getInputStream();
		if(encodingType==null)
			encodingType = System.getProperty("file.encoding");
		String responseStr = MyUtil.is2String(is, encodingType);
		return responseStr;
	}
}
