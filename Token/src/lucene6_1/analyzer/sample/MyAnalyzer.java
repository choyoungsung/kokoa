package lucene6_1.analyzer.sample;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class MyAnalyzer extends Analyzer {
	 
	   public MyAnalyzer() {
	   }
	 
	   @Override
	   protected TokenStreamComponents createComponents(String fieldName) {
		   return newInstance1(fieldName);
		   }
	   
	   private static TokenStreamComponents newInstance3(String fieldName){
		   final Tokenizer source = new WhitespaceTokenizer();
//		   final Tokenizer source = new KeywordTokenizer();
		   
		     TokenStream result = new LengthFilter(source, 3, Integer.MAX_VALUE);
		     result = new MyPartOfSpeechTaggingFilter(result);
		     return new TokenStreamComponents(source, result);
	   }

	   private static TokenStreamComponents newInstance2(String fieldName){
		   final Tokenizer source = new WhitespaceTokenizer();
		     TokenStream result = new MyLengthFilter(source, 3, Integer.MAX_VALUE);
		     return new TokenStreamComponents(source, result);
	   }

	   private static TokenStreamComponents newInstance1(String fieldName){
		   return new TokenStreamComponents(new WhitespaceTokenizer());
	   }
	   
	   public static void main(String[] args) throws IOException {
		   test2();
	   }
	   
	   
	   public static void test2() throws IOException {
		     // text to tokenize
		     final String text = "This is a demo of the TokenStream API";
		     
		     MyAnalyzer analyzer = new MyAnalyzer();
		     TokenStream stream = analyzer.tokenStream("field", new StringReader(text));
		     
		     // get the CharTermAttribute from the TokenStream
		     CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
		     
		     // get the PartOfSpeechAttribute from the TokenStream
		     MyPartOfSpeechAttribute posAtt = stream.addAttribute(MyPartOfSpeechAttribute.class);
		 
		     try {
		       stream.reset();
		 
		       // print all tokens until stream is exhausted
		       while (stream.incrementToken()) {
		         System.out.println(termAtt.toString() + ": " + posAtt.getPartOfSpeech());
		       }
		     
		       stream.end();
		     } finally {
		       stream.close();
		     }
		   }
	   
	   public static void test1() throws IOException {
	     // text to tokenize
	     final String text = "This is a demo of the TokenStream API";
	     
	     MyAnalyzer analyzer = new MyAnalyzer();
	     TokenStream stream = analyzer.tokenStream("field", new StringReader(text));
	     
	     // get the CharTermAttribute from the TokenStream
	     CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
	 
	     try {
	       stream.reset();
	     
	       // print all tokens until stream is exhausted
	       while (stream.incrementToken()) {
	         System.out.println(termAtt.toString());
	       }
	     
	       stream.end();
	     } finally {
	       stream.close();
	       analyzer.close();
	     }
	   }
	 }