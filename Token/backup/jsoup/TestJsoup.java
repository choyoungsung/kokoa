package job.jsoup;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import job.util.MyUtil;

public class TestJsoup {
	public static void main(String[] args) throws Exception{
		e();
	}
	
//	static String url = "http://www.nau.com/mens/jackets";
//	static String url = "http://www.hats.com/";
	static String url  = "https://www.heels.com/brands";
	
	public static void e() throws Exception{
//		Map<String, String> requestHeader = new HashMap<String, String>();
//		requestHeader.put("User-Agent", "Mozilla/5.0 (Windos NT 6.1; rv:46.0) Gecko/20100101 Firefox/46.0");
//		String html = MyUtil.requestGet(url, requestHeader, "UTF-8");
		String html = testHtml();
		Document doc = Jsoup.parse(html);
		Elements nodes = doc.getAllElements();
//		traverseElements(nodes, 0, 0, 0);
		printElements(nodes, 0);
		System.err.println();
		System.err.println();
		System.err.println();
//		traverseChildren(doc, 0);
//		System.err.println();
//		System.err.println();
//		System.err.println();
		
		Elements nodes0 = doc.getElementsByIndexEquals(0);
		printElements(nodes0, 0);
		
		Elements nodes1 = doc.getElementsByIndexEquals(1);
		printElements(nodes1, 1);
		
		Elements nodes2 = doc.getElementsByIndexEquals(2);
		printElements(nodes2, 2);
		
	}
	
	private static String testHtml(){
		return "<html>"
				+ "<head>"
				+ "</head>"
					+ "<body>"
						+ "<div id=\"div1\" tabindex=\"0\">mymyText</div>"
						+ "<div id=\"div2\" tabindex=\"0\">mymyText2</div>"
					+ "</body>"
				+ "</html>";
	}
	
	public static void d() throws Exception{
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put("User-Agent", "Mozilla/5.0 (Windos NT 6.1; rv:46.0) Gecko/20100101 Firefox/46.0");
		String html = MyUtil.requestGet(url, requestHeader, "UTF-8");
		Document doc = Jsoup.parse(html);
		Elements nodes = doc.getElementsMatchingText("FREE.*");
		traverseElements(nodes, 0, 1, 3);
	}
	
	public static void c() throws Exception{
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put("User-Agent", "Mozilla/5.0 (Windos NT 6.1; rv:46.0) Gecko/20100101 Firefox/46.0");
		String html = MyUtil.requestGet(url, requestHeader, "UTF-8");
		Document doc = Jsoup.parse(html);
		HtmlToPlainText formatter = new HtmlToPlainText();
		String text = formatter.getPlainText(doc);
		System.err.println(text);
	}
	
	public static void b() throws Exception{
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put("User-Agent", "Mozilla/5.0 (Windos NT 6.1; rv:46.0) Gecko/20100101 Firefox/46.0");
		String html = MyUtil.requestGet(url, requestHeader, "UTF-8");
		Document doc = Jsoup.parse(html);
		
		traverseChildren(doc, 0);
		
	}
	
	private static void traverseChildren(Element parent, int currDepth){
		traverseChildren(parent, currDepth, 0, Integer.MAX_VALUE);
	}
	
	private static void traverseChildren(Element parent, int currDepth, int startDepth, int endDepth){
		Elements children = parent.children();
		traverseElements(children, currDepth, startDepth, endDepth);
	}
	
	private static void traverseElements(Elements children, int currDepth){
		traverseElements(children, currDepth, 0, Integer.MAX_VALUE);
	}
			
	private static void printElements(Elements children, int currDepth){
		for(Element child : children){
			System.err.println(manyTab(currDepth) + child.nodeName() + " id : " +  child.id()  + " >>  "  + child.ownText());
		}
		System.err.println("##endofelemtns##\n");
	}
	
	private static void traverseElements(Elements children, int currDepth, int startDepth, int endDepth){
//		if(!((startDepth<= currDepth) && (currDepth<=endDepth))){
//			return;
//		}
		if(currDepth>endDepth){
			return;
		}
		
		for(Element child : children){
			if((startDepth<= currDepth) && (currDepth<=endDepth)){
				System.err.println(manyTab(currDepth) + child.nodeName() + " id : " +  child.id()  + " >>  "  + child.ownText());
			}
			if(child.children().size()>0 && currDepth<=endDepth){
				traverseChildren(child, currDepth+1);
			}
		}
	}
	
	private static String manyTab(int depth){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<depth;i++){
			sb.append("\t");
		}
		return sb.toString();
	}
	
	private static void printChildren(Element parent){
		Elements children = parent.children();
		for(Element child : children){
			System.err.println(child.nodeName() + " >>  " + child.text());
		}
	}
	
	public static void a() throws Exception{
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put("User-Agent", "Mozilla/5.0 (Windos NT 6.1; rv:46.0) Gecko/20100101 Firefox/46.0");
		String html = MyUtil.requestGet(url, requestHeader, "UTF-8");
//		System.err.println(html);
		Document doc = Jsoup.parse(html);
//		
		Elements divs = doc.select("div");
		for(Element src : divs){
			System.err.println(src.text());
		}
//		String text = doc.body().text();
		
		
		
	}
	
}


//https://jsoup.org/cookbook/extracting-data/selector-syntax
/*

Description
jsoup elements support a CSS (or jquery) like selector syntax to find matching elements, that allows very powerful and robust queries.

The select method is available in a Document, Element, or in Elements. It is contextual, so you can filter by selecting from a specific element, or by chaining select calls.

Select returns a list of Elements (as Elements), which provides a range of methods to extract and manipulate the results.

Selector overview
	tagname: find elements by tag, e.g. a
	ns|tag: find elements by tag in a namespace, e.g. fb|name finds <fb:name> elements
	#id: find elements by ID, e.g. #logo
	.class: find elements by class name, e.g. .masthead
	[attribute]: elements with attribute, e.g. [href]
	[^attr]: elements with an attribute name prefix, e.g. [^data-] finds elements with HTML5 dataset attributes
	[attr=value]: elements with attribute value, e.g. [width=500] (also quotable, like [data-name='launch sequence'])
	[attr^=value], [attr$=value], [attr*=value]: elements with attributes that start with, end with, or contain the value, e.g. [href*=/path/]
	[attr~=regex]: elements with attribute values that match the regular expression; e.g. img[src~=(?i)\.(png|jpe?g)]
	*: all elements, e.g. *

Selector combinations
	el#id: elements with ID, e.g. div#logo
	el.class: elements with class, e.g. div.masthead
	el[attr]: elements with attribute, e.g. a[href]
	Any combination, e.g. a[href].highlight
	ancestor child: child elements that descend from ancestor, e.g. .body p finds p elements anywhere under a block with class "body"
	parent > child: child elements that descend directly from parent, e.g. div.content > p finds p elements; and body > * finds the direct children of the body tag
	siblingA + siblingB: finds sibling B element immediately preceded by sibling A, e.g. div.head + div
	siblingA ~ siblingX: finds sibling X element preceded by sibling A, e.g. h1 ~ p
	el, el, el: group multiple selectors, find unique elements that match any of the selectors; e.g. div.masthead, div.logo

Pseudo selectors
	:lt(n): find elements whose sibling index (i.e. its position in the DOM tree relative to its parent) is less than n; e.g. td:lt(3)
	:gt(n): find elements whose sibling index is greater than n; e.g. div p:gt(2)
	:eq(n): find elements whose sibling index is equal to n; e.g. form input:eq(1)
	:has(seletor): find elements that contain elements matching the selector; e.g. div:has(p)
	:not(selector): find elements that do not match the selector; e.g. div:not(.logo)
	:contains(text): find elements that contain the given text. The search is case-insensitive; e.g. p:contains(jsoup)
	:containsOwn(text): find elements that directly contain the given text
	:matches(regex): find elements whose text matches the specified regular expression; e.g. div:matches((?i)login)
	:matchesOwn(regex): find elements whose own text matches the specified regular expression
	
Note that the above indexed pseudo-selectors are 0-based, that is, the first element is at index 0, the second at 1, etc
See the Selector API reference for the full supported list and details.

*/
	