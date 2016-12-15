package job.jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

public class Selector4MD {
	public static class Traverse{
		public static void traverseChildren(Element parent, int currDepth){
			traverseChildren(parent, currDepth, 0, Integer.MAX_VALUE);
		}
		
		public static void traverse(Node root, NodeVisitor visitor){
		     Node node = root;
		     int depth = 0;
		     
		     while (node != null) {
		       visitor.head(node, depth);
		       if (node.childNodeSize() > 0) {
		         node = node.childNode(0);
		         depth++;
		       } else {
		         while ((node.nextSibling() == null) && (depth > 0)) {
		           visitor.tail(node, depth);
		           node = node.parentNode();
		           depth--;
		         }
		         visitor.tail(node, depth);
		         if (node == root)
		           break;
		         node = node.nextSibling();
		       }
		     }
		   }
		
		public static void traverseChildren(Element parent, int currDepth, int startDepth, int endDepth){
			Elements children = parent.children();
			traverseElements(children, currDepth, startDepth, endDepth);
		}
		
		public static void traverseElements(Elements children, int currDepth){
			traverseElements(children, currDepth, 0, Integer.MAX_VALUE);
		}
				
		public static void printElements(Elements children, int currDepth){
			if(children==null){
				return;
			}
			
			for(Element child : children){
				System.err.println(manyTab(currDepth) + child.nodeName() + " id : " +  child.id()  + " >>  "  + child.ownText());
			}
			System.err.println("##endofelements##\n");
		}
		
		public static void traverseElements(Elements children, int currDepth, int startDepth, int endDepth){
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
		
		public static void printChildren(Element parent){
			Elements children = parent.children();
			for(Element child : children){
				System.err.println(child.nodeName() + " >>  " + child.text());
			}
		}
	}
}
