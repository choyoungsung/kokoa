package tokenizer;

import java.util.ArrayList;
import java.util.List;

import job.util.MyUtil;

public class Ngram {
	
	// 공백 구분자에 의해 구분된 문자열이 입력됨을 가정
	// 구분자에 의해 구분된 단위는 같은 type임을 가정한다.
	// 지정한 type에 대해서만 ngram분할을 추가로 수행한다. 
	
	public static String split2NgramStrOnlyTargetType(String line, int N) throws Exception{
		StringBuilder sb = new StringBuilder();
		
		String[] arr = line.split("\\s+");
		
		for(int i=0;i<arr.length;i++){
			if(sb.length()>0){
				sb.append(" ");
			}
			String splited = splitNonSpacingStr2NgramStr(arr[i], N);
			sb.append(splited);

		}
		return sb.toString();
	}
	
	public static String split2NgramStrOnlyTargetType(String line, int N, TargetCharType ctype) throws Exception{
		StringBuilder sb = new StringBuilder();
		
		String[] arr = line.split("\\s+");
		
		for(int i=0;i<arr.length;i++){
			if(sb.length()>0){
				sb.append(" ");
			}
			if(arr[i].length()>0 && ctype.isTargetType(arr[i].charAt(0))){
				String splited = splitNonSpacingStr2NgramStr(arr[i], N);
				sb.append(splited);
			}else{
				sb.append(arr[i]);
			}
		}
		return sb.toString();
	}
	
	public static String splitNonSpacingStr2NgramStr(String line, int N) throws Exception{
		char[] arr = line.toCharArray();
		return splitNonSpacingStr2NgramStr(arr, N);
	}
	
	
	public static String splitNonSpacingStr2NgramStr(char[] arr, int N) throws Exception{
		if(arr==null){
			return null;
		}
		
		int realN = Math.min(arr.length, N);
		StringBuilder sb = new StringBuilder();
		int len = arr.length-realN + 1;
		for(int i=0;i<len;i++){
			String a = new String(arr, i, realN);
			if(sb.length()>0){
				sb.append(" ");
			}
			sb.append(a);
			
//			res.add(a);
		}
		return sb.toString();
	}
	
	public static List<String> splitNonSpacingStr2Ngram(String line, int N) throws Exception{
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
}
