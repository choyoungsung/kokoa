package job.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

// doc tree로부터 table을 구성한다. 
// 부모, 자식, 동종 tag의 자식으로 부터 table을 build한다. 
public class SimpliedTree {
	static String[] allowedName = {"ul", "li", "table", "tr", "td", "div"};
	
	public static void simplified(Element node){
		Elements children = node.children();
		simplied(node);
		for(Element child : children){
			if(child.hasText() || StringUtil.in(child.nodeName(), allowedName)){
				simplified(child);
			}else{
				child.remove();
			}
		}
	}
	
	private static void simplied(Element node){
		Attributes attrs = node.attributes();
		for(Attribute attr : attrs){
			node.removeAttr(attr.getKey());
		}
//		
		for(String classname : node.classNames()){
			node.removeClass(classname);
		}
	}
	
//		for(Element child : children){
//			if((startDepth<= currDepth) && (currDepth<=endDepth)){
//				System.err.println(manyTab(currDepth) + child.nodeName() + " id : " +  child.id()  + " >>  "  + child.ownText());
//			}
//			if(child.children().size()>0 && currDepth<=endDepth){
//				traverseChildren(child, currDepth+1);
//			}
//		}

}



/*

<table style="width:100%">
  <tr>
    <td>Jill</td>
    <td>Smith</td> 
    <td>50</td>
  </tr>
  <tr>
    <td>Eve</td>
    <td>Jackson</td> 
    <td>94</td>
  </tr>
</table>

*/


/*

<div id="container">
	<div id="header"></div>
	<div id="content"></div>
	<div id="sideinfo"></div>
	<div id="footer"></div>
</div>


#container  { background-color:#F0F0F0; width:960px; margin:0 auto; padding:10px; }
#header   { background-color:#908886; height:100px; }
#content  { background-color:#F5F5F5; float:left; width:660px; height:400px; }
#sideinfo  { background-color:#DCDAD9; float:right; width:300px; height:400px; }
#footer   { background-color:#555555; clear:both; height:100px; }

*/