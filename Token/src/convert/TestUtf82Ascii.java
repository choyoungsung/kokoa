package convert;

import java.text.Normalizer;

public class TestUtf82Ascii {
	
	public static void main(String[] args) throws Exception{
		c();
	}
	
	public static void c()throws Exception{
		final String input = "é";
		
		String a = Normalizer.normalize(input, Normalizer.Form.NFD);
		String b = a.replaceAll("[^\\p{ASCII}]", "");
		String c = a.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		System.out.println(input);
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		char[] arr = a.toCharArray();
		for(char ch : arr){
			System.err.print(ch + " ");
		}
		
	}
	public static void b()throws Exception{
		final String input = "Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ";
		
		String a = Normalizer.normalize(input, Normalizer.Form.NFD);
		String b = a.replaceAll("[^\\p{ASCII}]", "");
		String c = a.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		System.out.println(input);
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		char[] arr = a.toCharArray();
		for(char ch : arr){
			System.err.print(ch + " ");
		}
	}
	
	public static void a()throws Exception{
		String a = "Â";
		
		byte[] bytes = a.getBytes("ASCII");
		String b = new String(bytes, "ASCII");
		System.err.println(a);
		System.err.println(b);
	}
	
}
