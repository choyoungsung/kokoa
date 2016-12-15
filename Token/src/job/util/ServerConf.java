package job.util;
import java.io.*;
import java.util.*;

public final class ServerConf {
	static Properties table = null;
	static Set<String> categorys = null;
	
	public static void main(String[] args){
		ServerConf.load("server.conf");
	}
	
	public static String getMustProperty(String key) throws Exception{
		if(table==null || key==null)
			throw new Exception("NotFound Property " + key);
		String res = table.getProperty(key);
		if(res==null || res.isEmpty())
			throw new Exception("NotFound Property " + key);
		return res;
	}
	
	public static String getProperty(String key) throws Exception{
		if(table==null || key==null)
			throw new Exception("NotFound Property " + key);
		return table.getProperty(key);
	}
	
	public static boolean load(String fileName){
		try{
			InputStream is = new FileInputStream(fileName);
			table = new Properties();
			table.load(is);
			categorys = parseCategory();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static void print(){
		Set<String> keys = table.stringPropertyNames();
		for(String key : keys){
			System.err.println(key + " => " + table.getProperty(key));
		}
	}
	
	private static Set<String> parseCategory(){
		Enumeration e = table.propertyNames();
		Set<String> list = new HashSet<String>();
		while(e.hasMoreElements()){
			String fullKey = (String)e.nextElement();
			String[] arr = fullKey.split("\\.");	//test.job1.splitNsql.col = 2  => test
			//System.err.println(arr[0]);
			list.add(arr[0]);
		}
		return list;
	}
	
	
	public static String[] getCategoryArr(){
		String[] res = categorys.toArray(new String[0]);
		return res;
	}
	
	public static HashMap<String, String> getSqlScriptArr(String category){
		HashMap<String, String> map = new HashMap<String, String>();
		Enumeration e = table.propertyNames();
		while(e.hasMoreElements()){
			String fullKey = (String)e.nextElement();
			String[] arr = fullKey.split("\\.");
			if(arr[0].equals(category) && arr[1].equals("sql")){
				map.put(arr[2], table.getProperty(fullKey));
			}
		}
		return map;
	}
	
	// srcipt==arr[2] �� �ΰ��ɼ� arr[3]�� �����ٸ� �̸� �����Ѵ�. ��ٸ� null�� �����Ѵ�. 
	public static String getScriptOption(String category, String scriptName, String optionName) throws Exception{
		Enumeration e = table.propertyNames();
		while(e.hasMoreElements()){
			String fullKey = (String)e.nextElement();
			String[] arr = fullKey.split("\\.");
			if(arr!=null && arr.length>=4 && arr[0].equals(category) && arr[1].equals("sql") && arr[2].equals(scriptName) && arr[3].equals(optionName)){
				return table.getProperty(fullKey);
			}
		}
		throw new Exception("NOTFOUND_IN_PROPERTY " + category + "." + scriptName + "." + optionName);
	}
	
	public static int[] convertIntArr(String line, String seps){
		try{
			String[] arr = line.split(seps);
			int[] intArr = new int[arr.length];
			
			for(int i=0;i<arr.length;i++){
				intArr[i] = Integer.parseInt(arr[i]);
			}
			return intArr;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	// arr[0] = category, arr[1]=(db|sql) , arr[2]=scriptName
	
	/*private static HashMap<String, String> getPropByRegEx(String keyPattern){
		Enumeration e = table.propertyNames();
		HashMap<String, String> map = new HashMap<String, String>(); 
		while(e.hasMoreElements()){
			String key = (String)e.nextElement();
			if(key.matches(keyPattern)){
				map.put(key, table.getProperty(key));
			}
		}
		return map;
	}
	*/
	
	/*
test.db.password = )!23dlszhem
test.sql.write1 = insert into [encodingDB].[dbo].[tvm_test] (filename) values (?)
test.sql.write1.split = 2	,

$Category		(db|sql)	$jobName		split
	 * */
}
