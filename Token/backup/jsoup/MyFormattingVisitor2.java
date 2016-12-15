package job.jsoup;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

public class MyFormattingVisitor2 implements NodeVisitor
{
  private static final int maxWidth = 80;
  private int width = 0;
  private StringBuilder accum = new StringBuilder();
  private boolean bPrintNodeName = false;
  
  public MyFormattingVisitor2() {
  }
  
  public MyFormattingVisitor2(boolean bPrintNodeName) {
	  this.bPrintNodeName = bPrintNodeName;
  }
  
	public void head(Node node, int depth) { 
		String name = node.nodeName();
		if ((node instanceof TextNode)) {
		  append(((TextNode)node).text());
		} else if (name.equals("li")) {
		  append("\n");
		  append(headnodename(node, depth));
		} else if (name.equals("dt")) {
		  append("  ");
		} else if (StringUtil.in(name, new String[] { "p", "h1", "h2", "h3", "h4", "h5", "tr" })) {
		  append("\n");
		  append(headnodename(node, depth));
		}
	}
	
	private String headnodename(Node node, int depth){
		if(bPrintNodeName){
			return manyTab(depth) + "<"+node.nodeName() + ":" + depth + ">";
		}else{
			return manyTab(depth);
		}
	}
	
	private static String manyTab(int depth){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<depth;i++){
			sb.append("\t");
		}
		return sb.toString();
	}
  
	public void tail(Node node, int depth) {
		String name = node.nodeName();
		if (StringUtil.in(name, new String[] { "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5" })) {
			append("\n");
		} else if (name.equals("a")) {
//			append(String.format(" <%s>", new Object[] { node.absUrl("href") }));
		}
	}
  
  private void append(String text) {
    if (text.startsWith("\n"))
      width = 0;
    if (text.equals(" ")) {
      if (accum.length() != 0) { if (!StringUtil.in(accum.substring(accum.length() - 1), new String[] { " ", "\n" })) {}
      } else return;
    }
    if (text.length() + width > 80) {
      String[] words = text.split("\\s+");
      for (int i = 0; i < words.length; i++) {
        String word = words[i];
        boolean last = i == words.length - 1;
        if (!last)
          word = word + " ";
        if (word.length() + width > 80) {
          accum.append("\n").append(word);
          width = word.length();
        } else {
          accum.append(word);
          width += word.length();
        }
      }
    } else {
      accum.append(text);
      width += text.length();
    }
  }
  
  public String toString()
  {
    return accum.toString();
  }

}