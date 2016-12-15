package job.jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import htmlfragment.ClassifyHtmlFragment;
import job.util.MyUtil;
import job.util.TabIndentReArranger;

public class Servicer4MD {
	public static void main(String[] args) throws Exception{
		b();
	}
	
	// html filter for md finder
//	static String url  = "https://www.heels.com/brands";
	public static void a() throws Exception{
//		String url = "https://www.heels.com/brands";
//		String url = "http://www.ikea.com/kr/ko/catalog/allproducts/";
//		String url = "http://www.lego.com/ko-kr/friends/products";
//		String url = "https://www.nespresso.com/kr/ko/Order-Capsules?icid=KRko_order.capsules_coffee_NA_NA"; // 원하는 데이터가 <script>  내의 javascript var로 존재하는 경우엔?
//		String url = "http://www.ems.com";
//		String url = "http://www.apple.com/mac/";
//		String url = "http://www.asos.com/";
//		String url = "http://www.gap.com/browse/subDivision.do?cid=5646&mlink=5058,10323290,visnav_W&clink=10323290";
//		String url = "http://www.lush.co.kr/site/main.jsp";
		String url = "https://www.diapers.com";
		String encoding = "UTF-8"; 
		boolean bNew =false;
		String html = Getter4MD.getHtmlContent(url, encoding, bNew);
//		System.err.println(html);
		MyUtil.saveText2File(html, "org.html");
		
		Document doc = Jsoup.parse(html);
		
//		String res = forcursedHtml(doc, SimpliedTree.allowedName);
		String filteredHtml = forcursedHtml(doc, "html");
//		
//		String res = findTable(doc, ".*Casual.*", ".*Down.*");
		System.err.println(filteredHtml);
		MyUtil.saveText2File(filteredHtml, "res.txt");
		Document reshtml = Jsoup.parseBodyFragment(filteredHtml);
		MyUtil.saveText2File(reshtml.html(), "res.html");
		
		MyFormattingVisitor2 formatter = new MyFormattingVisitor2(false);
		Selector4MD.Traverse.traverse(doc, formatter);
		MyUtil.saveText2File(formatter.toString(), "res2.txt");
		
	}
	
	public static void b() throws Exception{
		String url = "https://www.diapers.com";
		String encoding = "UTF-8"; 
		boolean bNew =true;
		int outputMode = 4;
		String res = forcursedHtml(url, encoding, bNew, outputMode);
		System.err.println(res);
	}
	
	public static String forcursedHtml(String url, String encoding, boolean bNew, int outputMode) throws Exception{
		String html = Getter4MD.getHtmlContent(url, encoding, bNew);
//		System.err.println(html);
		Document doc = Jsoup.parse(html);
		if(outputMode==0){
			return outputMode0(doc);
		}
		else if(outputMode==1){
			return outputMode1(doc);
		}else if(outputMode==2){
			return outputMode2(doc);
		}else if(outputMode==3){
			return outputMode3(doc);
		}else if(outputMode==4){
			return outputMode4(doc);
		}else{
			return outputMode3(doc);
		}
	}
	
	private static String outputMode4(Document doc) throws Exception{
		ClassifyHtmlFragment chf = new ClassifyHtmlFragment();
		chf.init();
		return chf.eval(doc);
	}
	
	private static String outputMode3(Document doc) throws Exception{
		Elements chosonOnes = chosonOnes(doc, SimpliedTree.allowedName);
		StringBuilder sb = new StringBuilder();
		for(Element node : chosonOnes){
			SimpliedTree.simplified(node);
			Document innerdoc = Jsoup.parseBodyFragment(node.outerHtml());
			String fragment = outputMode2(innerdoc);
			if(fragment!=null && !fragment.matches("^\\s+$")){
				sb.append(fragment).append("\n");
			}
		}
		return sb.toString();
	}
	
	private static String outputMode2(Document doc) throws Exception{
		MyFormattingVisitor2 formatter = new MyFormattingVisitor2(false);
		Selector4MD.Traverse.traverse(doc, formatter);
		String tabbedtext = formatter.toString();
		String tabreducedtext = TabIndentReArranger.reducedtxt(tabbedtext);
		return tabreducedtext;
	}
	
	private static String outputMode1(Document doc) throws Exception{
		String filteredHtml = forcursedHtml(doc, SimpliedTree.allowedName);
		if(filteredHtml==null || filteredHtml.isEmpty()){
			return "";
		}
		Document reshtml = Jsoup.parseBodyFragment(filteredHtml);
		return reshtml.html();
	}
	
	private static String outputMode0(Document doc) throws Exception{
		return doc.text();
	}
	
	public static String forcursedHtml(Document doc, String[] selectQuerys) throws Exception{
		Elements chosonOnes = chosonOnes(doc, selectQuerys);
		
		StringBuilder sb = new StringBuilder();
		for(Element node : chosonOnes){
			SimpliedTree.simplified(node);
			sb.append(node.outerHtml()).append("\n\n");
		}
		return sb.toString();
	}
	
	private static Elements chosonOnes(Document doc, String[] selectQuerys) throws Exception{
		Elements childrenall = new Elements();
		for(String selectQuery : selectQuerys){
			Elements children = doc.select(selectQuery);
			childrenall.addAll(children);
		}
		
		Elements chosonOnes = uniq(childrenall);
		return chosonOnes;
	}
	public static String forcursedHtml(Document doc, String selectQuery) throws Exception{
		StringBuilder sb = new StringBuilder();
		Elements children = doc.select(selectQuery);
		for(Element node : children){
			SimpliedTree.simplified(node);
			sb.append(node.outerHtml()).append("\n\n");
		}
		return sb.toString();
	}
	
	
	
	private static Elements uniq(Elements list){
		Set<Element> map = new HashSet<Element>();
		Elements res = new Elements();
		for(Element item : list){
			if(!map.contains(item)){
				res.add(item);
				map.add(item);
			}
		}
		return res;
	}
	
	
	
	
	
	
	
	
	
	public static String findTable(Document doc, String textQuery, String textQuery2){
		Elements children = doc.getElementsMatchingText(textQuery);
		int backtraceNum4Parent = 4;
		
		Elements parents = getParent(children, backtraceNum4Parent);
		parents = uniq(parents);
		
		Elements childrenAandBTotal = new Elements();
		for(Element parent : parents){
			Elements childrenAandB = parent.getElementsMatchingText(textQuery2);
			childrenAandBTotal.addAll(childrenAandB);
//			printFormattedText(childrenAandB);
		}
		
		childrenAandBTotal = uniq(childrenAandBTotal);
		return toFormattedText(childrenAandBTotal);
	}
	
	public static String traverseTable(Document doc, String selectQuery){
		if(selectQuery!=null){
			Elements children = doc.select(selectQuery);
//			Selector4MD.Traverse.printElements(children, 0);
			return toFormattedText(children);
		}else{
			MyFormattingVisitor2 formatter = new MyFormattingVisitor2();
			Selector4MD.Traverse.traverse(doc, formatter);
			return formatter.toString();
		}
	}
	
	private static String toFormattedText(Elements children){
		StringBuilder sb = new StringBuilder();
		for(Element doc : children){
			sb.append(doc.nodeName());
			MyFormattingVisitor2 formatter = new MyFormattingVisitor2();
			Selector4MD.Traverse.traverse(doc, formatter);
			sb.append(formatter);
		}
		sb.append("\n");
		return sb.toString();
	}
	
	private static void printFormattedText(Elements children){
		for(Element doc : children){
			System.err.println(doc.nodeName());
			MyFormattingVisitor2 formatter = new MyFormattingVisitor2();
			Selector4MD.Traverse.traverse(doc, formatter);
			System.err.println(formatter);
		}
		System.err.println();
	}
	
	private static Elements getParent(Elements children, int backtraceNum4Parent){
		Elements parents = new Elements();
		for(Element doc : children){
			Element parent = getParent(doc, backtraceNum4Parent);
			parents.add(parent);
		}
		return parents;
	}
	
	private static void printFormattedTextOfParent(Elements children, int backtraceNum4Parent){
		for(Element doc : children){
			Element parent = getParent(doc, backtraceNum4Parent);
			MyFormattingVisitor2 formatter = new MyFormattingVisitor2();
			Selector4MD.Traverse.traverse(doc.parent(), formatter);
			System.err.println("\n######start of " + parent.nodeName() + " " + parent.ownText());
			System.err.println(formatter);
			System.err.println("######end of " + parent.nodeName() + " " + parent.ownText() + "\n");
		}
	}
	
	private static Element getParent(Element node, int backtraceNum4Parent){
		Element target = node;
		if(target==null){
			return null;
		}
		for(int i=0;i<backtraceNum4Parent;i++){
			Element parent = target.parent();
//			System.err.println(parent.nodeName() + " <=  " + target.nodeName());
			if(parent==null){
				return target;
			}else{
				target = parent;
			}
		}
		return target;
	}
	
}

