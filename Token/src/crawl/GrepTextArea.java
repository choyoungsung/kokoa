package crawl;

import java.util.*;

public class GrepTextArea {
	String startClueText;
	List<String> endClueTexts;
	boolean bRefineHtmlTag;
	int lastIdx; // wholeText의 index여야만 의미있을 것
	
	public String refineHtmlTag(String wholeText){
		wholeText = wholeText.replaceAll("<[^>]+{2,50}>", "");
		wholeText = wholeText.replaceAll("&lt;", "<");
		wholeText = wholeText.replaceAll("&gt;", ">");
		return wholeText;
	}
	
//	public List<String> grepAsList(String wholeText, int idxOfSearch){
//		if(endClueTexts==null){
//			return null;
//		}
//		
//		List<String> res = new ArrayList<String>();
////		int idxOfSearch = 0;
//		int idxOfStartClue = wholeText.indexOf(startClueText, idxOfSearch);
//		while(idxOfSearch < wholeText.length()){
//			idxOfStartClue = wholeText.indexOf(startClueText, idxOfSearch);
//			if(idxOfStartClue<0){
//				break;
//			}
//			
//			int startOfBlock = idxOfStartClue + startClueText.length();
//			
//			int endOfBlock = wholeText.indexOf(endClueText, startOfBlock);
//			idxOfSearch = endOfBlock + endClueText.length();
//			
//			String block = wholeText.substring(startOfBlock, endOfBlock);
//			this.lastIdx =  endOfBlock;
//			if(bRefineHtmlTag){
//				block = refineHtmlTag(block);
//			}
//			res.add(block);
//		}
//		return res;
//	}
	
	
	public List<String> grepAsList(String wholeText, int idxOfSearch){
		if(endClueTexts==null){
			return null;
		}
		
		List<String> res = new ArrayList<String>();
//		int idxOfSearch = 0;
		int idxOfStartClue = wholeText.indexOf(startClueText, idxOfSearch);
		while(idxOfSearch < wholeText.length()){
			idxOfStartClue = wholeText.indexOf(startClueText, idxOfSearch);
			if(idxOfStartClue<0){
				break;
			}
			
			int startOfBlock = idxOfStartClue + startClueText.length();
			int endOfBlock = -1;
			
			for(String endClueText : endClueTexts){
				endOfBlock = wholeText.indexOf(endClueText, startOfBlock);
				if(endOfBlock>=0){
					idxOfSearch = endOfBlock + endClueText.length();
					break;
				}
			}
			
			if( (startOfBlock<0) || (endOfBlock<0) || (startOfBlock>=endOfBlock) || startOfBlock>=wholeText.length() || endOfBlock>=wholeText.length() ){
				return res;
			}
			
			String block = wholeText.substring(startOfBlock, endOfBlock);
			this.lastIdx =  endOfBlock;
			if(bRefineHtmlTag){
				block = refineHtmlTag(block);
			}
			res.add(block);
		}
		return res;
	}
	
	
	public String grep(String wholeText, int idxOfSearch){
		int idxOfAreaStart = -1;
		if(startClueText == null){
			idxOfAreaStart = 0;
		}else{
			int idxOfStartClue = wholeText.indexOf(startClueText, idxOfSearch);
			if(idxOfStartClue<0){
				return null;
			}else{
				int idxOfBlock = idxOfStartClue + startClueText.length();
				if(idxOfBlock>=wholeText.length()){
					return "";
				}else{
					idxOfAreaStart = idxOfBlock;
				}
			}
		}
		
		int idxOfEndClue = -1;
		for(String endClueText : endClueTexts){
			idxOfEndClue = wholeText.indexOf(endClueText, idxOfAreaStart);
			if(idxOfEndClue>=0){
				break;
			}
		}
//		int idxOfEndClue = wholeText.indexOf(endClueText, idxOfAreaStart);
		if(idxOfEndClue<0){
			return null;
		}else{
			int idxOfBlock = idxOfEndClue;
			this.lastIdx =  idxOfBlock;
			return wholeText.substring(idxOfAreaStart, idxOfBlock);
		}
	}
	
//	public String grep(String wholeText, int idxOfSearch){
//		int idxOfAreaStart = -1;
//		if(startClueText == null){
//			idxOfAreaStart = 0;
//		}else{
//			int idxOfStartClue = wholeText.indexOf(startClueText, idxOfSearch);
//			if(idxOfStartClue<0){
//				return null;
//			}else{
//				int idxOfBlock = idxOfStartClue + startClueText.length();
//				if(idxOfBlock>=wholeText.length()){
//					return "";
//				}else{
//					idxOfAreaStart = idxOfBlock;
//				}
//			}
//		}
//		
//		int idxOfEndClue = wholeText.indexOf(endClueText, idxOfAreaStart);
//		if(idxOfEndClue<0){
//			return null;
//		}else{
//			int idxOfBlock = idxOfEndClue;
//			this.lastIdx =  idxOfBlock;
//			return wholeText.substring(idxOfAreaStart, idxOfBlock);
//		}
//	}

	
	public static class Builder{
		String startClueText;
		List<String> endClueTexts;
		boolean bRefineHtmlTag;
		
		public GrepTextArea build(){
			GrepTextArea t = new GrepTextArea();
			t.startClueText = startClueText;
			t.endClueTexts = endClueTexts;
			t.bRefineHtmlTag = bRefineHtmlTag;
			return t;
		}
		
		public Builder setBRefineHtml(boolean bRefineHtmlTag){
			this.bRefineHtmlTag = bRefineHtmlTag;
			return this;
		}
		
		public Builder setStartClueText(String startClueText){
			this.startClueText = startClueText;
			return this;
		}
		
		public Builder addEndClueText(String endClueText){
			if(this.endClueTexts==null){
				this.endClueTexts = new ArrayList<String>();
			}
			this.endClueTexts.add(endClueText);
			return this;
		}
	}
	
	
}
