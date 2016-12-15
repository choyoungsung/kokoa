package tokenizer;

public class TestToken {
	
	static String[] samples ={
			" 1 2",
			" 12",
			"가 나",
			"소격동sohot 아이유좋은날" + "Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ소격동 ###23432한글abc###", 
			"아버지가 방에 들어가신다",
			 
			 " 가나234#@#   23423", 
			"# @", 
			"1 가 ",
			"가 1", 
			"a b", 
			"#가 나 #"
		};
	
	public static void main(String[] args) throws Exception{
		b();
	}
	
	public static void b() throws Exception{
		boolean bHangulAndEtc = false;
		boolean bDoCharNormalize = true;
		boolean bNumPuncAndEtc = false;
		int N = 2;
		
		for(String sample : samples){
			String str = LangTokenizer.Servicer.parse(sample, bHangulAndEtc, bDoCharNormalize, bNumPuncAndEtc, N);
			System.err.println(str);
		}
	}
	
	public static void a() throws Exception{
		boolean num = true;
		boolean punc = true;
		boolean hangul = true;
		
		for(String sample : samples){
//			String str = LangTokenizer.SplitCharByTargetType.splitByType(sample, num, punc, hangul);
			String str = Ngram.splitNonSpacingStr2NgramStr(sample, 3);
			System.err.println(str);
		}
	}
}
