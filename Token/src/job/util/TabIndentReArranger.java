package job.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TabIndentReArranger{
	// 다중 문장 list가 앞에 tab문자로 indentation되어 있는 경우, 이전 line과의 tab 갯수를 최대 1로 재조정한다. 
	// 최소 tab -> zero tab , 최소 다음의 tab one tab으로 변경 ...
	// 		srclist -> tab갯수 - text로 분해 ;
	//		tab갯수로 정렬 -> 정렬 순서대로 탭 갯수를 할당한다. 단, 2개 이상 동일 탭 갯수는 동일한 점수를 부여해야 하고, 다음 것에는 +1 점수를 할당한다. 
	
	public static void main(String[] args) throws Exception{
		String[] arr = {
				"x",
				"\ta",
				"\t\t\t\t\t xyz",
				"\t\t\t\t\t yyy",
				"\t\t\tc",
		};
		
		TabIndentReArranger t = new TabIndentReArranger();
		String[] res = t.reduceTooManyTab(Arrays.asList(arr));
		for(String line : res){
			System.err.println(line);
		}
	}
	
	public static String reducedtxt(String tabbedtext){
		TabIndentReArranger tr = new TabIndentReArranger();
		String[] arr = tr.reduceTooManyTab(tabbedtext);
		String tabreducedtext = tr.join(arr, "\n");
		return tabreducedtext;
	}
	
	public String join(String[] arr, String seps){
		StringBuilder sb = new StringBuilder();
		for(String line : arr){
			if(sb.length()>0){
				sb.append(seps);
			}
			sb.append(line);
		}
		return sb.toString();
	}
	
	public String[] reduceTooManyTab(String multiline){
		String[] arr = multiline.split("\n");
		List<String> srclist = Arrays.asList(arr);
		int before = srclist.size();
		srclist = removeWhiteLine(srclist);
		int after = srclist.size();
		return reduceTooManyTab(srclist);
	}
	
	private List<String> removeWhiteLine(List<String> srclist){
		List<String> res = new ArrayList<String>();
		for(String line : srclist){
			if(!line.matches("^\\s+$")){
				res.add(line);
			}
		}
		return res;
	}
	
	public String[] reduceTooManyTab(List<String> srclist){
		if(srclist==null){
			return null;
		}
		
		List<TabbedLine> list = splitTabNumAndTextList(srclist);
		Collections.sort(list, new TabbedLine());
		reduceTabNum(list);
		String[] res = reOrderedList(list);
		return res;
	}
	
	private String[] reOrderedList(List<TabbedLine> list){
		String[] arr = new String[list.size()];
		for(TabbedLine tl : list){
			arr[tl.lineIdx] = getTabbedText(tl.tabNum, tl.text);
		}
		return arr;
	}
	
	private String getTabbedText(int tabNum, String text){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<tabNum;i++){
			sb.append(tab);
		}
		sb.append(text);
		return sb.toString();
	}
	
	private void reduceTabNum(List<TabbedLine> list){
		int prevTabNum = -1;
		int newTabNum = -1;
		for(TabbedLine tl : list){
			if(tl.tabNum==prevTabNum){
				
			}else if(tl.tabNum>prevTabNum){
				newTabNum++;
			}else{
				newTabNum--;
			}
			prevTabNum = tl.tabNum;
			tl.tabNum = newTabNum;
		}
		
	}
	
	static class TabbedLine implements Comparator<TabbedLine>{
		Integer tabNum;
		String text;
		Integer lineIdx;
		
		public TabbedLine(){}
		
		public TabbedLine(Integer tabNum, String text, Integer lineIdx){
			this.tabNum = tabNum;
			this.text = text;
			this.lineIdx = lineIdx;
		}
		
		public String toString(){
			return "[" + lineIdx + "][tabnum]:" + tabNum + " " + text;
		}

		@Override
		public int compare(TabbedLine o1, TabbedLine o2) {
			if(o1.tabNum < o2.tabNum)
				return -1;
			else if(o1.tabNum == o2.tabNum){
				return 0;
			}else{
				return 1;
			}
		}
	}
	
	static char tab = '\t';
	private List<TabbedLine> splitTabNumAndTextList(List<String> srclist){
		List<TabbedLine> list = new ArrayList<TabbedLine>();
		
//		for(String src : srclist){
		for(int i=0;i<srclist.size();i++){
			String src = srclist.get(i);
			int idxOfNonTab = idxOfNonTab(src);
			TabbedLine tl = new TabbedLine(idxOfNonTab, src.substring(idxOfNonTab), i);
			list.add(tl);
		}
		return list;
	}
	
	private int idxOfNonTab(String line){
		if(line==null)
			return -1;
		int cnt = 0;
		for(int i=0;i<line.length();i++){
			if(line.charAt(i)==tab){
				cnt++;
			}else{
				return cnt;
			}
		}
		return cnt;
	}
	
}
