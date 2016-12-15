package tokenizer;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import job.util.MyUtil;
//import job.util.LangUtil.CharNormalizer;
//import job.util.LangUtil.GrepHangul;
//import job.util.LangUtil.NGram;


// fiter 스타일의 tokenizer, lucene style로 구현해보자. 
public class LangTokenizer {
	public static class Servicer{
		public static String parse(String text, boolean bHangulAndEtc, boolean bDoCharNormalize,  boolean bNumPuncAndEtc, int N) throws Exception{
			boolean num = false;
			boolean punc = false;
			boolean hangul = false;
			
			if(bNumPuncAndEtc){
				num = true;
				punc = true;
			}
			
			if(bHangulAndEtc){
				hangul = true;
				text = SplitCharByTargetType.splitByType(text, num, punc, hangul);
				text = Ngram.split2NgramStrOnlyTargetType(text, N, SplitCharByTargetType.hangulType);
			}else{
				hangul = false;
				text = SplitCharByTargetType.splitByType(text, num, punc, hangul);
				text = Ngram.split2NgramStrOnlyTargetType(text, N);
			}
			
			if(bDoCharNormalize){
				text = CharNormalizer.toAsciiStr(text);
			}
			return text;
		}
		
	}
//	public static class ServiceSet{
//		public static String parse(String text, boolean bHangulAndEtc, boolean bDoCharNormalize, int N) throws Exception{
//			List<String> list2;
//			if(bHangulAndEtc){
//				 list2 = GrepHangul.ngramOfHangulAndWord(text, N);
//			}else{
//				list2 = NGram.split2Ngram(text, N);
//			}
//			
//			if(bDoCharNormalize){
//				list2  = CharNormalizer.toAsciiStr(list2);
//			}
//			
//			return MyUtil.arrayJoin(" ", list2);
//		}
//	}
	
	public static class SplitCharByTargetType{
		static NumberType numType = new NumberType();
		static PunctuationType puncType = new PunctuationType();
		static HangulType hangulType = new HangulType();
		static int NSizeTooBig = 1000;
		
		public static String splitByType(String org, boolean num, boolean punc, boolean hangul){
//			char[] chars = org.toCharArray();
			if(num)
				org = SplitCharByType.splitByType(org, numType);
			if(punc)
				org = SplitCharByType.splitByType(org, puncType);
			if(hangul)
				org = SplitCharByType.splitByType(org, hangulType);
			return org;
		}
		
		public static String splitByNum(String org){
			char[] chars = org.toCharArray();
			
			String list1 = SplitCharByType.splitByType(chars, numType);
			return list1;
		}
		
		public static String splitByPunc(String org){
			char[] chars = org.toCharArray();
			
			String list1 = SplitCharByType.splitByType(chars, puncType);
			return list1;
		}
		
		// token내 숫자, 구두점을 추가 분리한다. 공백은 제거됨을 가정한다. 숫자끼리 구두점끼리 모아야 한다. 
		static class NumberType implements TargetCharType{
			public boolean isTargetType(char ch) {
				return isNumber(ch);
			}
		}
		
		static class PunctuationType implements TargetCharType{
			public boolean isTargetType(char ch) {
				return isPunctuation(ch);
			}
		}
		
		static class HangulType implements TargetCharType{
			public boolean isTargetType(char ch) {
				return isHangulChar(ch);
			}
		}
		
		private static boolean isHangulChar(char ch){
			if (ch >= 0xAC00 && ch <= 0xD7A3) {
				return true;
			}else{
				return false;
			}
		}
		
		public static boolean isNumber(char ch){
			int type = Character.getType(ch);
			if( (Character.DECIMAL_DIGIT_NUMBER <= type) && (type<=Character.OTHER_NUMBER)){
				return true;
			}else{
				return false;
			}
		}
		
		public static boolean isPunctuation(char ch){
			int type = Character.getType(ch);
			if( (Character.DASH_PUNCTUATION <= type) && (type<=Character.FINAL_QUOTE_PUNCTUATION)){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public static class CharNormalizer{
		public static List<String> toAsciiStr(List<String> inputArr){
			List<String> res = new ArrayList<String>();
			for(String input : inputArr){
				res.add(toAsciiStr(input));
			}
			return res;
		}
		
		public static String toAsciiStr(String input){
			String a = Normalizer.normalize(input, Normalizer.Form.NFD);
			String c = a.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			return c;
		}
	}
}
