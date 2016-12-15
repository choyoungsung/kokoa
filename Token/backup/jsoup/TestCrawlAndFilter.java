package job.jsoup;

import job.util.MyUtil;

public class TestCrawlAndFilter {
	public static void main(String[] args) throws Exception{
		a();
	}
	
	static String url = "http://www.nau.com/mens/jackets";
	public static void a() throws Exception{
		String whole = MyUtil.requestGet(url, "UTF-8");
		System.err.println(whole);
	}
	
}

