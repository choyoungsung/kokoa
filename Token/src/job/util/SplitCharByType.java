package job.util;

import java.util.ArrayList;
import java.util.List;

public class SplitCharByType {
//	public abstract boolean isTargetType(char ch);
	public static interface TargetType{
		public boolean isTargetType(char ch);
	}
	
	
	public static String splitByType(String org, TargetType targetType, int NSizeTooBig){
		char[] chars = org.toCharArray();
		return splitByType(chars, targetType, NSizeTooBig);
	}
	
	public static String splitByType(char[] chars, TargetType targetType, int NSizeTooBig){
		StringBuffer sb = new StringBuffer();
		
		int cntOfType = 0;
		for (int i = 0; i < chars.length; i++) {
			
			if( (i<chars.length-1) && (Character.isWhitespace(chars[i])) && (Character.isWhitespace(chars[i+1]))){
				cntOfType = 0;
				continue;
			}
			
			char ch = chars[i];
			if(!Character.isWhitespace(ch)){
				sb.append(ch);
			}
			
			if (targetType.isTargetType(ch)) {
				cntOfType++;
			}else{
				cntOfType = 0;
			}
			
			if(cntOfType>= NSizeTooBig){
				sb.append(" ");
				cntOfType = 0;
			}else if(Character.isWhitespace(ch) && !Character.isWhitespace(sb.charAt(sb.length()-1))){//sb.length()>0){
				sb.append(" ");
				cntOfType = 0;
//			}else if(i==chars.length-1 && sb.length()>0){
//				sb.append(" ");
//				cntOfType = 0;
			}else if(i<chars.length-1 && targetType.isTargetType(chars[i]) && !targetType.isTargetType(chars[i+1])  ){
				sb.append(" ");
				cntOfType = 0;
			}else if(i<chars.length-1 && !targetType.isTargetType(chars[i]) && targetType.isTargetType(chars[i+1])){
				sb.append(" ");
				cntOfType = 0;
			}
		}
		
		return sb.toString();
	}
//	public static List<String> splitByType(char[] chars, TargetType targetType, int NSizeTooBig){
//		List<String> res = new ArrayList<String>();
//		StringBuffer sb = new StringBuffer();
//		
//		int cntOfHangul = 0;
//		for (int i = 0; i < chars.length; i++) {
//			
//			if( (i<chars.length-1) && (Character.isWhitespace(chars[i])) && (Character.isWhitespace(chars[i+1]))){
//				cntOfHangul = 0;
//				continue;
//			}
//			
//			char ch = chars[i];
//			if(!Character.isWhitespace(ch)){
//				sb.append(ch);
//			}
//			
//			if (targetType.isTargetType(ch)) {
//				cntOfHangul++;
//			}else{
//				cntOfHangul = 0;
//			}
//			
//			if(cntOfHangul>= NSizeTooBig){
//				res.add(sb.toString());
//				sb.setLength(0);
//				cntOfHangul = 0;
//			}else if(Character.isWhitespace(ch) && sb.length()>0){
//				res.add(sb.toString());
//				sb.setLength(0);
//				cntOfHangul = 0;
//			}else if(i==chars.length-1 && sb.length()>0){
//				res.add(sb.toString());
//				sb.setLength(0);
//				cntOfHangul = 0;
//			}else if(i<chars.length-1 && targetType.isTargetType(chars[i]) && !targetType.isTargetType(chars[i+1])  ){
//				res.add(sb.toString());
//				sb.setLength(0);
//				cntOfHangul = 0;
//			}else if(i<chars.length-1 && !targetType.isTargetType(chars[i]) && targetType.isTargetType(chars[i+1])){
//				res.add(sb.toString());
//				sb.setLength(0);
//				cntOfHangul = 0;
//			}
//		}
//		
//		return res;
//	}
	
//	public static List<String> splitByType(char[] chars, TargetType targetType, int NSizeTooBig){
//		if(chars==null)
//			return null;
//		
//		List<String> res = new ArrayList<String>();
//		StringBuffer sb = new StringBuffer();
//		
//		int cntOfClue = 0;
//		for (int i = 0; i < chars.length; i++) {
//			char ch = chars[i];
//			sb.append(ch);
//			
//			if (targetType.isTargetType(ch)) {// 숫자면
//				cntOfClue++;
//			}else{
//				cntOfClue = 0;
//			}
//			
//			if(cntOfClue>= NSizeTooBig){
//				res.add(sb.toString());
//				sb.setLength(0);
//				cntOfClue = 0;
//			}else if(i==chars.length-1 && sb.length()>0){
//				res.add(sb.toString());
//				sb.setLength(0);
//				cntOfClue = 0;
//			}else if(i<chars.length-1 && targetType.isTargetType(chars[i]) && !targetType.isTargetType(chars[i+1])  ){// 한글, 비한글인 경우엔 구분
//				res.add(sb.toString());
//				sb.setLength(0);
//				cntOfClue = 0;
//			}else if(i<chars.length-1 && !targetType.isTargetType(chars[i]) && targetType.isTargetType(chars[i+1])){
//				res.add(sb.toString());
//				sb.setLength(0);
//				cntOfClue = 0;
//			}
//		}
//		
//		return res;
//	}
}
