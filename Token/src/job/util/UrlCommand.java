package job.util;
import java.net.*;
import java.util.*;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;


public final class UrlCommand {
	static Logger log = LogManager.getLogger("UrlCommand");
	
	public String jobID = null;
	public String command = null;
	public String option = null;
	public String parameter = null;
	public String post = null;
	
	public static void main(String[] args) throws Exception{
		UrlCommand t = UrlCommand.newInstance(new URI("/job.start;opOpt?a=bdd"), null);
		log.trace(t.jobID);
		log.trace(t.command);
		log.trace(t.option);
		log.trace(t.parameter);
	}
	
	public static UrlCommand newInstance(HttpExchange exchange, String charSet) throws Exception{
		URI uri = exchange.getRequestURI();
		HashMap<String, String> table = parseCommand(uri, charSet);
		UrlCommand t = new UrlCommand();
		t.jobID = table.get("jobID");
		t.command = table.get("command");
		t.option = table.get("option");
		t.parameter = table.get("parameter");
		
		if(exchange.getRequestMethod().equalsIgnoreCase("POST")){
			t.post = MyUtil.is2String(exchange.getRequestBody(), "UTF-8");
		}
		
		return t;
	}
	
	public static UrlCommand newInstance(URI uri, String charSet) throws Exception{
		HashMap<String, String> table = parseCommand(uri, charSet);
		UrlCommand t = new UrlCommand();
		t.jobID = table.get("jobID");
		t.command = table.get("command");
		t.option = table.get("option");
		t.parameter = table.get("parameter");
		return t;
	}
	
	//   /audiotrans.start;runOnlyOneID=albumID_1234?aodfile=../data/2426172.mp3&saveDir=../data/result&outputFormat=transNtag&bitrate
	// ${jobName}.${operation}[;${options}]=${parameter}
	public static HashMap<String, String> parseCommand(URI uri, String charSet) throws Exception{
		HashMap<String, String> commandTable = null;
		try{
			commandTable = new HashMap<String, String>(); 
			String rawURIStr = uri.toASCIIString();
				
			String decodedURIStr =null;
			if(charSet!=null)
				decodedURIStr = URLDecoder.decode(rawURIStr, charSet);
			else
				decodedURIStr = rawURIStr;
				
			String[] arr = parsePathParameter(decodedURIStr);
			
			if(arr!=null && arr.length>=2 && arr[1]!=null){
				String parameter = arr[1];
				commandTable.put("parameter", parameter);
			}
			
			String path = arr[0];
				
			String[] jobNCommand = parsePath(path);
			String jobID = jobNCommand[0]; // $jobID
			commandTable.put("jobID", jobID);
			String commandWithOption = jobNCommand[1]; // $command;recvID=$recvID
			parseCommand(commandWithOption,commandTable); 
		}catch(Exception e){
			throw e;
		}
			
		if(chkCommandTable(commandTable))
			return commandTable;
		else
			return null;
	}
	
	public static String toStringCommandTable(HashMap<String, String> commandTable){
		StringBuffer sb = new StringBuffer();
		Set<String> keys = commandTable.keySet();
		Iterator<String> iter= keys.iterator();
		while(iter.hasNext()){
			String key = iter.next();
			sb.append(key + ":" + commandTable.get(key) +", ");
		}
		return sb.toString();
	}
	
	// /audiotrans.start;runOnlyOneID=albumID_1234?aodfile=../data/2426172.mp3&saveDir=../data/result&outputFormat=transNtag&bitrate
	private static String[] parsePathParameter(String decodedURIStr){
		String[] arr = new String[2];
		int idx = decodedURIStr.indexOf('?');
		if(idx==-1){
			arr[0] =  decodedURIStr;
			arr[1] = null;
			return arr;
		}else{
			arr[0] = decodedURIStr.substring(0, idx);
			arr[1] = decodedURIStr.substring(idx+1);
			return arr;
		}
	}

	private static boolean chkCommandTable(HashMap<String, String> commandTable){
		if(commandTable==null)
			return false;
		if(commandTable.get("jobID")==null)
			return false;
		if(commandTable.get("command")==null)
			return false;
		
		return true;
	}
		
	// $command;recvID=$recvID
	private static void parseCommand(String commandWithOption, HashMap<String, String> table){
		if(commandWithOption==null)
			return;
			
		int idx = commandWithOption.indexOf(';');
		if(idx==-1){
			table.put("command", commandWithOption);
			return;
		}
			
		String command = commandWithOption.substring(0, idx);
		table.put("command", command);
			
		String optionString = commandWithOption.substring(idx+1); //recvID=$recvID
		//MyUtil.parseQueryString(optionString, table);
		table.put("option", optionString);
			
	}
		
	private static String[] parsePath(String urlPath){
	//	log.trace("urlPath " + urlPath); 
				
		String input = null;
				
		if(urlPath.startsWith("/")){
			input = urlPath.substring(1);
		}else{
			input = urlPath;
		}
					
		//int idx = input.lastIndexOf('.');
		int idx = input.indexOf('.');
		if(idx==-1){
			log.trace("invalid urlPath " + urlPath);
			return null;
		}
			
		String[] arr = new String[2];
				
		arr[0] = input.substring(0, idx);
		arr[1] = input.substring(idx+1);
				
		log.trace("[0] " + arr[0] + ", [1] " + arr[1]);
		return arr;
	}
}
