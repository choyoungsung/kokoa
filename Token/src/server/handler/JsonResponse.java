package server.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;


public final class JsonResponse {
	static Logger log = LogManager.getLogger("JsonResponse");
	public static enum ResultCode  {SUCCESS, FAIL, ERROR}; 
	
	
	
	public static String toJSONStr(String result, String errMsg, Map<String, Object> data){
		try{
			Map<String, Object> table = new HashMap<String, Object>();
			table.put("RESULT", result);
			table.put("MSG", errMsg);
			if(data!=null)
				table.putAll(data);
			Gson gson = new Gson();
			return gson.toJson(table);
		}catch(Exception e){
			log.error(e, e);
			result =  ResultCode.ERROR.toString();
			errMsg = e.toString();
			try{
				Map<String, Object> table = toMap(result, errMsg);
				Gson gson = new Gson();
				return gson.toJson(table);
			}catch(Exception ee){
				return null;
			}
		}
	}
	
	private static Map<String, Object> toMap(String result, String errMsg) throws Exception{
		Map<String, Object> table = new HashMap<String, Object>();
		table.put("RESULT", result);
		table.put("MSG", errMsg);
		
		return table;
	}
}
