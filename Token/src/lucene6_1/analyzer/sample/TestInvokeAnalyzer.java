package lucene6_1.analyzer.sample;

import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;

import lucene6_1.analyzer.sample.MyAnalyzer;

public class TestInvokeAnalyzer {
	
	public static void main(String[] args) throws Exception{
		a();
	}
	
	public static void a() throws Exception{
//		testAnalyzer(new StandardAnalyzer(), "abc", "this is a text");
		testAnalyzer(new MyAnalyzer(), "abc", "this is a text");
	}
	
	public static void testAnalyzer(Analyzer analyzer, String fieldName, String text) throws Exception{
		TokenStream ts = analyzer.tokenStream(fieldName, new StringReader(text));
		OffsetAttribute offsetAtt = ts.addAttribute(OffsetAttribute.class);
		CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
		
		try{
		ts.reset();
		while (ts.incrementToken()) {
			System.out.println("token: " + ts.reflectAsString(true));
			System.out.println("termAtt: " + termAtt.toString());
			 
	         System.out.println("token start offset: " + offsetAtt.startOffset());
	         System.out.println("token end offset: " + offsetAtt.endOffset());
	         System.out.println();
		}
		
		ts.end();
		}catch(Exception e){
			throw e;
		}finally{
			ts.close();
		}
	}
}
