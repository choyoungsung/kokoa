package job.util;

import org.apache.log4j.Logger;


import java.io.*;
import java.util.*;

final public class Data2File {
	static Logger log = Logger.getLogger("Data2File");
	/*public static void main(String[] args) throws Exception{
		Matrix mat = new SparseMatrix(10, 10);
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				mat.setQuick(i, j, i*j);
			}
		}
		
		saveMatrixByDense(mat, "mat.txt", true);
		
		Matrix mat2 = loadMatrixByDense("mat.txt");
		System.err.println(mat2.toString());
		
		saveMatrixByDense(mat2, "mat2.txt", true);
	}*/
	
	
	public static void main(String[] args) throws Exception{
//		Matrix matQ = loadMatrixByDense("D:\\workspaceLab\\Personal\\DATAsmall\\QSInverseMat_org.txt");
//		saveMatrixByDense(matQ, "D:\\workspaceLab\\Personal\\DATAsmall\\QSInverseMat.txt", true);
//		
//		Matrix matUsersQ = loadMatrixByDense("D:\\workspaceLab\\Personal\\DATAsmall\\UsersQSInverseMat_org.txt");
//		saveMatrixByDense(matUsersQ, "D:\\workspaceLab\\Personal\\DATAsmall\\UsersQSInverseMat.txt", true);
		Map<String, Integer> dic = new HashMap<String, Integer>();
		dic = Data2File.loadMap("D:\\workspaceLab\\Recom\\test\\YZoneDic.txt");
		System.err.println(dic.size());
	}
	
	
	
	
	public static void saveIntMap(Map<Integer, Integer> map, String saveFile) throws Exception{
		File f = new File(saveFile);
		PrintWriter pw = new PrintWriter(new FileWriter(f));
		Set<Integer> keyset = map.keySet();
		Iterator<Integer> iter = keyset.iterator();
		while(iter.hasNext()){
			Integer key = iter.next();
			pw.println(key + "\t" + map.get(key));
		}
		pw.close();
	}
	
	public static Map<Integer, Integer> loadIntMap(String saveFile) throws Exception{
		File f = new File(saveFile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		while((line=br.readLine())!=null){
			String[] arr = line.split("\t");
			Integer key = Integer.parseInt(arr[0]);
			Integer val = Integer.parseInt(arr[1]);
			map.put(key, val);
		}
		br.close();
		return map;
	}
	
	
	
	public static void saveMap(Map<String, Integer> map, String saveFile) throws Exception{
		File f = new File(saveFile);
		PrintWriter pw = new PrintWriter(new FileWriter(f));
		Set<String> keyset = map.keySet();
		Iterator<String> iter = keyset.iterator();
		while(iter.hasNext()){
			String key = iter.next();
			pw.println(key + "\t" + map.get(key));
		}
		pw.close();
	}
	
	public static Map<String, Integer> loadMap(String saveFile) throws Exception{
		File f = new File(saveFile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		while((line=br.readLine())!=null){
			String[] arr = line.split("\t");
			String key = arr[0];
			Integer val = Integer.parseInt(arr[1]);
			Integer savedVal = map.get(key);
			if(savedVal!=null){
				System.err.println("dup " + key + " " + savedVal);
			}
			map.put(key, val);
		}
		br.close();
		return map;
	}
	
	
	public static void saveList(List<String> list, String saveFile) throws Exception{
		File f = new File(saveFile);
		PrintWriter pw = new PrintWriter(new FileWriter(f));
		for(String item : list){
			pw.println(item);
		}
		pw.close();
	}
	
	public static List<String> loadList(String saveFile) throws Exception{
		File f = new File(saveFile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		List<String> list = new ArrayList<String>();
		while((line=br.readLine())!=null){
			list.add(line);
		}
		return list;
	}
	

	
	private static String getTail(String src, String seps) throws Exception{
		String[] arr = src.split(seps);
		return arr[arr.length-1];
	}
}
