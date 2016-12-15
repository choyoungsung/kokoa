package tokenizer;

import java.util.ArrayList;
import java.util.List;

public class SplitCharByType {
	
	public static String splitByWhiteChar(String src){
		char[] arr = src.toCharArray();
		return splitByWhiteChar(arr);
	}
	
	public static String splitByWhiteChar(char[] chars){
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < chars.length; i++) {
			
			if( (i<chars.length-1) && (Character.isWhitespace(chars[i])) && (Character.isWhitespace(chars[i+1]))){
				continue;
			}
			
			char ch = chars[i];
			if(!Character.isWhitespace(ch)){
				sb.append(ch);
			}else if(Character.isWhitespace(ch) && sb.length()>0 && !Character.isWhitespace(sb.charAt(sb.length()-1))){//sb.length()>0){
				sb.append(" ");
			}
		}
		
		
		return sb.toString();
	}
	
	public static String splitByType(String org, TargetCharType targetType){
		char[] chars = org.toCharArray();
		return splitByType(chars, targetType);
	}
	
	public static String splitByType(char[] chars, TargetCharType targetType){
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < chars.length; i++) {
			
			if( (i<chars.length-1) && (Character.isWhitespace(chars[i])) && (Character.isWhitespace(chars[i+1]))){
				continue;
			}
			
			char ch = chars[i];
			if(!Character.isWhitespace(ch)){
				sb.append(ch);
			}
			
			if(sb.length()>0 && Character.isWhitespace(ch) && sb.length()>0 && !Character.isWhitespace(sb.charAt(sb.length()-1))){
				sb.append(" ");
			}else if(i<chars.length-1 && targetType.isTargetType(chars[i]) && !targetType.isTargetType(chars[i+1]) && !Character.isWhitespace(chars[i+1])  ){
				sb.append(" ");
			}else if(i<chars.length-1 && !targetType.isTargetType(chars[i]) && targetType.isTargetType(chars[i+1])  && !Character.isWhitespace(chars[i+1]) ){
				sb.append(" ");
			}
		}
		
		
		return sb.toString();
	}

}
