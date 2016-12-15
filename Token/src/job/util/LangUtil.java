package job.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LangUtil {
//	public static void main(String[] args) throws Exception{
////		String d = SearchableTerms.toJasoNgramTerms("잉위");
////		String d = SearchableTerms.toNgramTerms("소격동 abba");
////		System.err.println(d);
////		String a = SearchableTerms.toJasoNgramTerms("소격동 abba");
//		
////		System.err.println(a);
//		
////		List<String> f = SearchableTerms.splitBracedStr("산다는 건 (Cheer Up)");
////		List<String> f = SearchableTerms.splitBracedStr("뱅뱅뱅 ( bang)");
//		
////		List<String> line = new ArrayList<String>();
////		line.add("소"); line.add("격"); line.add("동"); line.add("a"); line.add("b"); line.add("b");
////		List<List<String>> res = NGram.split2Ngram(line, 3);
////		System.err.println(res);
//		
////		List<String> arr = LangUtil.NGram.split2Ngram("소격동 abba");
////		System.err.println(arr);
////		System.err.println(SearchableTerms.toNgramTerms("소격동 abba"));
////		System.err.println(CharNormalizer.toAsciiStr("Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ소격동"));
////		List<String> list = GrepHangul.listOfHangulEumjeolAndWord("소격동sohot " + "Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ소격동");
////		List<List<String>> list2 = NGram.split2Ngram(list, 2);
////		System.err.println(list);
////		System.err.println(list2);
//		
////		String res = ServiceSet.parse("소격동sohot 아이유좋은날" + "Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ소격동", true, true, 2);
//		String res = ServiceSet.parse("아버지가 방에 들어가신다", true, true, 3);
//		
////		String res = ServiceSet.parse("소", false, true, 1);
////		String res = ServiceSet.parse("가나다라마바사", false, true, 2);
//		System.err.println(res);
//	}
	
//	public static void main(String[] args) throws Exception{
//		System.err.println(Character.isDigit('1'));
//		System.err.println(Character.getType('한'));
		
//		System.err.println(Character.getType('#'));
//		System.err.println(Character.getType('?'));
//		System.err.println(Character.getType('}'));
		
//		String str = "!@#$%^&*()_+`~-=[]\\{}|,./<>?;\':\"2334234rksfk나다라라 ㄱ";
//		for(char ch : str.toCharArray()){
////			System.err.println(Character.getType(ch) + " '" + ch + "' " + CharType.isNumberOrPunctuation(ch));
//			System.err.println(ch + "' " + NumberOrPunctuation.isNumber(ch) + " " + NumberOrPunctuation.isPunctuation(ch));
//		}
//		for(char ch : str.toCharArray()){
//			System.err.println(Character.getType(ch) + " " + ch);
//		}
		
//		System.err.println('\uDBFF');
		//
//	}
	// 한글만 음절단위 nGram을 사용, 그 외엔 word단위만 출력하자. 
	
	// 기호, 숫자 분리를 추가한다. 
	public static class ServiceSet{
		public static String parse(String text, boolean bHangulAndEtc, boolean bDoCharNormalize, int N) throws Exception{
			List<String> list2;
			if(bHangulAndEtc){
				 list2 = GrepHangul.ngramOfHangulAndWord(text, N);
			}else{
				list2 = NGram.split2Ngram(text, N);
			}
			
			if(bDoCharNormalize){
				list2  = CharNormalizer.toAsciiStr(list2);
			}
			
			return MyUtil.arrayJoin(" ", list2);
		}
		
	}
	
	/*public static void main(String[] args) throws Exception{
		String[] keywords = {"꽃갈피",
        "꽃갈피아이유",
        "아이유꽃갈피",
        "아이유너의의미",
        "아이유삐에로는우릴보고웃지",
        "IU아이유리메이크"};
		
		String res = SearchableTerms.toJasoNgramTerms(Arrays.asList(keywords));
		System.err.println(res);
        
	}*/
	/*public static void main(String[] args) throws Exception{
		int N = 3;
		List<String> arra = LangUtil.NGram.split2Ngram(LangUtil.Jaso.hangulToJaso("남자다그래"), N);
		List<String> arrb = LangUtil.NGram.split2Ngram(LangUtil.Jaso.hangulToJaso("남잔다그래"), N);
		List<String> arrc = LangUtil.NGram.split2Ngram(LangUtil.Jaso.hangulToJaso("남잔 다 그래"), N);
		List<String> arrd = LangUtil.NGram.split2Ngram(LangUtil.Jaso.hangulToJaso("내가그리웠니"), N);
		
		calJaccard(arra, arrb);
		calJaccard(arra, arrc);
		calJaccard(arra, arrd);
	}*/
	
//	public static void main(String[] args) {
//	String a = hangulToJaso("우리나라 .';? 좋은나라 뷃 ");
//	System.err.println(a);
//}
	
//	public static void main(String[] args) throws Exception{
//	List<String> arr = split2Ngram("가나다라마바", 3);
//	System.err.println(arr);
//}
	
	/*public static void main(String[] args) throws Exception{
		//String a = Jaso.hangulToChosungs("한글s 운영화");
//		String a = Jaso.hangulToChosungs("ㅎㄱ");
		
//		String a=  SearchableTerms.toPuncted("ㄴㄲㅇ");
//		List<String> a = SearchableTerms.toTailedKeywords(new String"아이유 너의  의미 ");
//		System.err.println(a);
	}*/
	
	/*public static void main(String[] args) throws Exception{
		String str = "( 잡지 촬영 스케치) - Fly To The Sky";
//		String str = "S.O.S. (Story Of Sadness)";
		String a = GrepHangul.grepHangul(str);
		System.err.println(a);
		String res = SearchableTerms.toPuncted(a, false);
		System.err.println(res);
	}*/
	public static void main(String[] args) throws Exception{
		String src = "#@@    12312     @#@#rksekk1231 가나다라마바  ";
		src = NumberOrPunctuation.splitByNum(src);
		System.err.println(src);
		src = NumberOrPunctuation.splitByPunc(src);
		System.err.println(src);
	}
	
	public static class NumberOrPunctuation{
		static NumberType numType = new NumberType();
		static PunctuationType puncType = new PunctuationType();
		static int NSizeTooBig = 1000;
		
		public static String splitByNum(String org){
			char[] chars = org.toCharArray();
			
			String list1 = SplitCharByType.splitByType(chars, numType, NSizeTooBig);
			return list1;
		}
		
		public static String splitByPunc(String org){
			char[] chars = org.toCharArray();
			
			String list1 = SplitCharByType.splitByType(chars, puncType, NSizeTooBig);
			return list1;
		}
		
		// token내 숫자, 구두점을 추가 분리한다. 공백은 제거됨을 가정한다. 숫자끼리 구두점끼리 모아야 한다. 
		static class NumberType implements SplitCharByType.TargetType{
			public boolean isTargetType(char ch) {
				return isNumber(ch);
			}
		}
		
		static class PunctuationType implements SplitCharByType.TargetType{
			public boolean isTargetType(char ch) {
				return isPunctuation(ch);
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
	
	
	
	
	
	
	
	
	public static class SearchableTerms{
		public static List<String> splitBracedStr(String str){
			if(str==null)
				return null;
			
			List<String> res = new ArrayList<String>();
			int startIdx = str.indexOf("(");
			if(startIdx<0){
				res.add(str);
				return res;
			}
				
			String headPart = str.substring(0, startIdx);
			int endIdx = str.indexOf(")");
			if(endIdx<0){
				res.add(str);
				return res;
			}
			String tailPart = str.substring(startIdx+1, Math.min(endIdx, str.length()));
			res.add(headPart);
			res.add(tailPart);
			return res;
			
		}
		
		public static String grepHangulNgram(String src) throws Exception{
			String grepHangul = LangUtil.SearchableTerms.grepHangul(src);
			String NGRAM_KEYWORD = SearchableTerms.toNgramTerms(grepHangul);
			return NGRAM_KEYWORD;
		}
		
		public static String grepHangulNgram(List<String> list) throws Exception{
			List<String> grepHangul = LangUtil.SearchableTerms.grepHangul(list);
			String NGRAM_KEYWORD = SearchableTerms.toNgramTerms(grepHangul);
			return NGRAM_KEYWORD;
		}
		
		public static List<String> grepHangul(List<String> list){
			if(list==null)
				return new ArrayList<String>();
			
			List<String> res = new ArrayList<String>();
			for(String str : list){
				String grep =  grepHangul(str);
				res.add(grep);
			}
			return res;
		}
		
		public static String grepHangul(String str){
			String a = GrepHangul.grepHangul(str);
			String res = SearchableTerms.toPuncted(a, false);
			return res;
		}
		
		public static List<String> toTailedKeywords(List<String> arrOfText){
			if(arrOfText==null || arrOfText.isEmpty())
				return null;
			
			List<String> res = new ArrayList<String>();
			for(String text : arrOfText){
				List<String> arr =  toTailedKeywords(text);
				if(arr!=null){
					res.addAll(arr);
				}
			}
			return MyUtil.uniq(res);
		}
		
		public static List<String> toTailedKeywords(String text){
			String refined = Punctuation.refine(text, true);
			if(refined==null || refined.length()<=0){
				return null;
			}
			List<String> res = new ArrayList<String>();
			for(int last=1;last<=refined.length();last++){
				String chunk = refined.substring(0, last);
				res.add(chunk);
			}
			
			return res;
		}
		
		//LangUtil.NGram.split2Ngram(srcFieldValue);
		public static List<String> split2Ngram(String userInputQuery) throws Exception{
			String refined = Punctuation.refine(userInputQuery, true);
			return MyUtil.uniq(LangUtil.NGram.split2Ngram(refined));
		}
		
		
		
		public static String toPuncted(String userInputQuery){
			String puncted = LangUtil.Punctuation.refine(userInputQuery, false);
			return puncted;
		}
		
		public static String toPuncted(String userInputQuery, boolean bRmAllSpacing){
			String puncted = LangUtil.Punctuation.refine(userInputQuery, bRmAllSpacing);
			return puncted;
		}
		
		public static List<String> toArrOfNgramTerms(List<String> userInputQueryArr) throws Exception{
			List<String> res = new ArrayList<String>();
			for(String userInputQuery : userInputQueryArr){
				res.add(toNgramTerms(userInputQuery));
			}
			return MyUtil.uniq(res);
		}
		
		public static String toNgramTerms(List<String> userInputQueryArr) throws Exception{
			if(userInputQueryArr==null)
				return null;
			List<String> res = new ArrayList<String>();
			for(String userInputQuery : userInputQueryArr){
				String refined = Punctuation.refine(userInputQuery, true);
				List<String> arr = LangUtil.NGram.split2Ngram(refined);
				if(arr!=null && !arr.isEmpty()){
					res.addAll(arr);
				}
			}
			return MyUtil.arrayJoin(" ", MyUtil.uniq(res));
		}
		
		
		public static String toNgramTerms(String userInputQuery) throws Exception{
			String refined = Punctuation.refine(userInputQuery, true);
			List<String> arr = LangUtil.NGram.split2Ngram(refined);
			arr = MyUtil.uniq(arr);
			String ngramQuery = MyUtil.arrayJoin(" ", arr);
			//String ngramQuery = LangUtil.NGram.convert2NgramStr(refined);
			return ngramQuery;
		}
		
		public static List<String> toArrOfJasoNgramTerms(List<String> userInputQueryArr) throws Exception{
			List<String> res = new ArrayList<String>();
			for(String userInputQuery : userInputQueryArr){
				res.add(toJasoNgramTerms(userInputQuery));
			}
			return MyUtil.uniq(res);
		}
		
		public static String toJasoNgramTerms(List<String> userInputQueryArr) throws Exception{
			if(userInputQueryArr==null)
				return null;
			List<String> res = new ArrayList<String>();
			for(String userInputQuery : userInputQueryArr){
				String refined = Punctuation.refine(userInputQuery, true);
				List<String> arr = LangUtil.Jaso.hangulToListOfNgramOfJaso(refined);
				if(arr!=null && !arr.isEmpty()){
					res.addAll(arr);
				}
			}
			return MyUtil.arrayJoin(" ", MyUtil.uniq(res));
		}
		
		public static String toJasoNgramTerms(String userInputQuery) throws Exception{
			String refined = Punctuation.refine(userInputQuery, true);
			
			List<String> arr = LangUtil.Jaso.hangulToListOfNgramOfJaso(refined);
			arr = MyUtil.uniq(arr);
			String jasoQuery = MyUtil.arrayJoin(" ", arr);
			return jasoQuery;
		}
		
		public static String toChosungsTerms(List<String> userInputQueryArr) throws Exception{
			if(userInputQueryArr==null)
				return null;
			List<String> res = new ArrayList<String>();
			for(String userInputQuery : userInputQueryArr){
				String refined = Punctuation.refine(userInputQuery, false);
				String chosungs = LangUtil.Jaso.hangulToChosungs(refined);
				if(chosungs!=null && !chosungs.isEmpty()){
					res.add(chosungs);
				}
				
			}
			return MyUtil.arrayJoin(" ", MyUtil.uniq(res));
		}
	}
	
	public static class GrepHangul{
		public static String grepHangul(String org){
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < org.length(); i++) {
				char ch = org.charAt(i);
				
				if (ch >= 0xAC00 && ch <= 0xD7A3) { // "AC00:가" ~ "D7A3:힣" 에 속한 글자면 분해
					sb.append(ch);
				}else{
					if(i>0 && org.charAt(i-1)==' '){
						continue;
					}else{
						sb.append(" ");
					}
				}
			}
			
			return sb.toString();
		}
		
		public static String strOfHangulEumjeolAndWord(String org){
			List<String> arr = listOfHangulEumjeolAndWord(org);
			return MyUtil.arrayJoin(" ", arr);
		}
		
		
		public static List<String> ngramOfHangulAndWord(String text, int N) throws Exception{
			List<String> listT = GrepHangul.listOfHangulEojeolAndWord(text);
			System.err.println(listT);
			List<String> list2 = new ArrayList<String>();
			for(String word : listT){
				if(word==null || word.isEmpty()){
					continue;
				}
				if(GrepHangul.isHangulChar(word.charAt(0))){
					list2.addAll(NGram.split2Ngram(word, N));
				}else{
					list2.add(word);
				}
			}
			return list2;
		}
		
		public static List<String> listOfHangulEojeolAndWord(String org){
			return listOfHangulEumjeolAndWord(org, Integer.MAX_VALUE);
		}
		
		public static List<String> listOfHangulEumjeolAndWord(String org){
			return listOfHangulEumjeolAndWord(org, 1); 
		}
		// 한글음절은 N음절까지, 공백 구분은 무조건 인정, 비한글은 공백 단위로, 한글과 비한글간 경계도 구분
		//String res = ServiceSet.parse("소격동sohot 아이유좋은날" + "Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ소격동", false, true, 2);
		// 스페이스 이외의 공백문자에 대한 처리가 없다. 
		public static List<String> listOfHangulEumjeolAndWord(String org, int NSizeHangulEumJeol){
			List<String> res = new ArrayList<String>();
			char[] chars = org.toCharArray();
			StringBuffer sb = new StringBuffer();
			
			int cntOfHangul = 0;
			for (int i = 0; i < chars.length; i++) {
				if( (i<chars.length-1) && (chars[i]==' ') && (chars[i+1]==' ')){
					cntOfHangul = 0;
					continue;
				}
				
				char ch = chars[i];
				if(ch!=' '){
					sb.append(ch);
				}
				
				if (isHangulChar(ch)) {// 한글 1음절 // "AC00:가" ~ "D7A3:힣" 에 속한 글자면 분해
					cntOfHangul++;
				}else{
					cntOfHangul = 0;
				}
				
				if(cntOfHangul>= NSizeHangulEumJeol){
					res.add(sb.toString());
					sb.setLength(0);
					cntOfHangul = 0;
				}else if(ch==' ' && sb.length()>0){
					res.add(sb.toString());
					sb.setLength(0);
					cntOfHangul = 0;
				}else if(i==chars.length-1 && sb.length()>0){
					res.add(sb.toString());
					sb.setLength(0);
					cntOfHangul = 0;
				}else if(i<chars.length-1 && isHangulChar(chars[i]) && !isHangulChar(chars[i+1])  ){// 한글, 비한글인 경우엔 구분
					res.add(sb.toString());
					sb.setLength(0);
					cntOfHangul = 0;
				}else if(i<chars.length-1 && !isHangulChar(chars[i]) && isHangulChar(chars[i+1])){
					res.add(sb.toString());
					sb.setLength(0);
					cntOfHangul = 0;
				}
			}
			
			return res;
		}
		
		private static boolean isHangulChar(char ch){
			if (ch >= 0xAC00 && ch <= 0xD7A3) {
				return true;
			}else{
				return false;
			}
		}
		
		// 문장 org내 한글과 비한글이 섞인 경우, 한글은 음절단위로 나머지는 공백단위로 단위를 구분한다. 
		
		
	}
	

	
	private static class Punctuation{
		// ^0-9a-zA-Z가힣  
		static List<Pattern> patterns;
		static List<String> replacements;
		
		static Pattern patternSpacing;
		
		static{
			init();
		}
		
		public static void init(){
			patterns = new ArrayList<Pattern>();
			replacements = new ArrayList<String>();
			
			add("[^0-9a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ]", " "); //add("[^0-9a-zA-Z가-힣]", " ");
			add("(^\\s+|\\s+$)", "");
			patternSpacing = Pattern.compile("\\s+");
		}
		
		private static void add(String pattern, String replacement){
			patterns.add(Pattern.compile(pattern));
			replacements.add(replacement);
		}
		
		public static List<String> refine(List<String> arr, boolean bRmAllSpacing){
			if(arr==null)
				return null;
			List<String> res = new ArrayList<String>();
			for(String line : arr){
				res.add(refine(line, bRmAllSpacing));
			}
			return res;
		}
		
//		public static List<String> refine(List<String> arr){
//			return refine(arr, false);
//		}
//		
//		public static String refine(String line){
//			return refine(line, false);
//		}
		
		public static String refine(String line, boolean bRmAllSpacing){
			if(line==null)
				return null;
			for(int i=0;i<patterns.size();i++){
				Pattern pattern = patterns.get(i);
				Matcher m = pattern.matcher(line);
				line = m.replaceAll(replacements.get(i));
			}
			Matcher ms = patternSpacing.matcher(line);
			if(bRmAllSpacing){
				line = ms.replaceAll("");
			}else{
				line = ms.replaceAll(" ");
			}
			//line = line.toLowerCase();
			return line;
		}
	}
	
	/*private static class Jaso {
	        // ㄱ      ㄲ      ㄴ      ㄷ      ㄸ      ㄹ      ㅁ      ㅂ      ㅃ      ㅅ      ㅆ      ㅇ      ㅈ      ㅉ      ㅊ      ㅋ      ㅌ      ㅍ      ㅎ
		final static char[] ChoSung   = { 0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };
		        // ㅏ      ㅐ      ㅑ      ㅒ      ㅓ      ㅔ      ㅕ      ㅖ      ㅗ      ㅘ      ㅙ      ㅚ      ㅛ      ㅜ      ㅝ      ㅞ      ㅟ      ㅠ      ㅡ      ㅢ      ㅣ
		final static char[] JwungSung = { 0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163 };
		        //         ㄱ      ㄲ      ㄳ      ㄴ      ㄵ      ㄶ      ㄷ      ㄹ      ㄺ      ㄻ      ㄼ      ㄽ      ㄾ      ㄿ      ㅀ      ㅁ      ㅂ      ㅄ      ㅅ      ㅆ      ㅇ      ㅈ      ㅊ      ㅋ      ㅌ      ㅍ      ㅎ
		final static char[] JongSung  = { 0,      0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };
		
		static int N = 3;
		
		public static List<String> hangulToListOfNgramOfJaso(String s) throws Exception{
			String b = Jaso.hangulToJaso(s);
			return NGram.split2Ngram(b, N);
		}
		
		public static String hangulToNgramOfJaso(String s) throws Exception{
			String b = Jaso.hangulToJaso(s);
			return NGram.convert2NgramStr(b, N);
		}
		
		public static List<String> hangulToNgramOfJaso(List<String> arr) throws Exception{
			if(arr==null)
				return null;
			List<String> res = new ArrayList<String>();
			for(String hangul : arr){
				res.add(hangulToNgramOfJaso(hangul));
			}
			return res;
		}
		
		static List<String> hangulToJaso(List<String> arr){
			if(arr==null)
				return null;
			List<String> res = new ArrayList<String>();
			for(String hangul : arr){
				res.add(hangulToJaso(hangul));
			}
			return res;
		}
		
		static String hangulToJaso(String s) { // 유니코드 한글 문자열을 입력 받음
			int a, b, c; // 자소 버퍼: 초성/중성/종성 순
			String result = "";
			if(s==null)
				return null;
			
			for (int i = 0; i < s.length(); i++) {
				char ch = s.charAt(i);
				
				if (ch >= 0xAC00 && ch <= 0xD7A3) { // "AC00:가" ~ "D7A3:힣" 에 속한 글자면 분해
					c = ch - 0xAC00;
					a = c / (21 * 28);
					c = c % (21 * 28);
					b = c / 28;
					c = c % 28;
					
					result = result + ChoSung[a] + JwungSung[b];
					if (c != 0){
						result = result + JongSung[c] ; // c가 0이 아니면, 즉 받침이 있으면
					}
				} else {
					result = result + ch;
				}
			}
			return result;
		}
	}*/
	
	
	private static class Jaso {
        // ㄱ      ㄲ      ㄴ      ㄷ      ㄸ      ㄹ      ㅁ      ㅂ      ㅃ      ㅅ      ㅆ      ㅇ      ㅈ      ㅉ      ㅊ      ㅋ      ㅌ      ㅍ      ㅎ
		final static char[] ChoSung   = { 0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };
		        // ㅏ      ㅐ      ㅑ      ㅒ      ㅓ      ㅔ      ㅕ      ㅖ      ㅗ      ㅘ      ㅙ      ㅚ      ㅛ      ㅜ      ㅝ      ㅞ      ㅟ      ㅠ      ㅡ      ㅢ      ㅣ
		final static char[] JwungSung = { 0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163 };
		        //         ㄱ      ㄲ      ㄳ      ㄴ      ㄵ      ㄶ      ㄷ      ㄹ      ㄺ      ㄻ      ㄼ      ㄽ      ㄾ      ㄿ      ㅀ      ㅁ      ㅂ      ㅄ      ㅅ      ㅆ      ㅇ      ㅈ      ㅊ      ㅋ      ㅌ      ㅍ      ㅎ
		final static char[] JongSung  = { 0,      0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };
		
		static int N = 3;
		
		public static List<String> hangulToListOfNgramOfJaso(String s) throws Exception{
			String b = Jaso.hangulToJaso(s);
			return NGram.split2Ngram(b, N);
		}
		
		public static String hangulToNgramOfJaso(String s) throws Exception{
			String b = Jaso.hangulToJaso(s);
			return NGram.convert2NgramStr(b, N);
		}
		
		public static List<String> hangulToNgramOfJaso(List<String> arr) throws Exception{
			if(arr==null)
				return null;
			List<String> res = new ArrayList<String>();
			for(String hangul : arr){
				res.add(hangulToNgramOfJaso(hangul));
			}
			return res;
		}
		
		static List<String> hangulToJaso(List<String> arr){
			if(arr==null)
				return null;
			List<String> res = new ArrayList<String>();
			for(String hangul : arr){
				res.add(hangulToJaso(hangul));
			}
			return res;
		}
		
		static String hangulToJaso(String s) { // 유니코드 한글 문자열을 입력 받음
			int a, b, c; // 자소 버퍼: 초성/중성/종성 순
			String result = "";
			if(s==null)
				return null;
			
			for (int i = 0; i < s.length(); i++) {
				char ch = s.charAt(i);
				
				if (ch >= 0xAC00 && ch <= 0xD7A3) { // "AC00:가" ~ "D7A3:힣" 에 속한 글자면 분해
					c = ch - 0xAC00;
					a = c / (21 * 28);
					c = c % (21 * 28);
					b = c / 28;
					c = c % 28;
					
					result = result + ChoSung[a] + JwungSung[b];
					if (c != 0){
						result = result + JongSung[c] ; // c가 0이 아니면, 즉 받침이 있으면
					}
				} else {
					result = result + ch;
				}
			}
			return result;
		}
		
		static String hangulToChosungs(String s) { // 유니코드 한글 문자열을 입력 받음
			int a, b, c; // 자소 버퍼: 초성/중성/종성 순
			String result = "";
			if(s==null)
				return null;
			
			for (int i = 0; i < s.length(); i++) {
				char ch = s.charAt(i);
				
				if (ch >= 0xAC00 && ch <= 0xD7A3) { // "AC00:가" ~ "D7A3:힣" 에 속한 글자면 분해
					c = ch - 0xAC00;
					a = c / (21 * 28);
					c = c % (21 * 28);
					b = c / 28;
					c = c % 28;
					
					result = result + ChoSung[a];
				} else {
					result = result + ch;
				}
			}
			return result;
		}
		
		static boolean isChosungs(String s) { // 유니코드 한글 문자열을 입력 받음
			if(s==null)
				return false;
			
			int found = 0;
			for (int i = 0; i < s.length(); i++) {
				char ch = s.charAt(i);
				
				if (ch >= 0xAC00 && ch <= 0xD7A3) { // "AC00:가" ~ "D7A3:힣" 에 속한 글자면 분해
					return false;
				}
				
				if( 0x3131 <= ch && ch <=0x314e){
					found++;
				}
				
			}
			
			if(found>0)
				return true;
			else
				return false;
		}
	}
	
	
	
	
	public static class NGram {
		static int N = 2;
		
		public static String convert2NgramStr(String line) throws Exception{
			return convert2NgramStr(line, N);
		}
		
		public static String convert2NgramStr(String line, int N) throws Exception{
			List<String> arr = split2Ngram(line, N);
			return MyUtil.arrayJoin(" ", arr);
		}
		
		public static List<String> split2Ngram(String line) throws Exception{
			return split2Ngram(line, N);
		}
		
		public static List<String> split2Ngram(String line, int N) throws Exception{
			if(line==null || line.isEmpty())
				return null;
			
			char[] arr = line.toCharArray();
			int realN = Math.min(arr.length, N);
			
			List<String> res = new ArrayList<String>();
			int len = arr.length-realN + 1;
			for(int i=0;i<len;i++){
				String a = new String(arr, i, realN);
				res.add(a);
			}
			return res;
		}
		
		public static <T> List<List<T>> split2Ngram(List<T> line, int N) throws Exception{
			if(line==null || line.isEmpty())
				return null;
			
			int realN = Math.min(line.size(), N);
			
			List<List<T>> res = new ArrayList<List<T>>();
			int len = line.size()-realN + 1;
			for(int i=0;i<len;i++){
				List<T> inner = new ArrayList<T>();
				for(int tmp = i; tmp<i+realN;tmp++){
					inner.add(line.get(tmp));
				}
				res.add(inner);
			}
			return res;
		}
		
//		public static <T> List<List<T>> split2Ngram(List<T> line, int N) throws Exception{
//			if(line==null || line.isEmpty())
//				return null;
//			
//			int realN = Math.min(line.size(), N);
//			
//			List<List<T>> res = new ArrayList<List<T>>();
//			int len = line.size()-realN + 1;
//			for(int i=0;i<len;i++){
//				List<T> inner = new ArrayList<T>();
//				for(int tmp = i; tmp<i+realN;tmp++){
//					inner.add(line.get(tmp));
//				}
//				res.add(inner);
//			}
//			return res;
//		}
		
	}
	
	
	private static float calJaccard(List<String> arra, List<String> arrb){
		Set<String> setb = new HashSet<String>();
		setb.addAll(arrb);
		
		int allcnt = arra.size() + arrb.size();
		int matchcnt = 0;
		for(String item : arra){
			if(setb.contains(item)){
				System.err.print(item + " ");
				matchcnt++;
			}
		}
		System.err.println(matchcnt + "/" + allcnt);
		return (float)((float)matchcnt / (float)allcnt);
	}
}
