package server.handler;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import servicer.APIServicer;
import job.util.MyUtil;
import job.util.ServerConf;
import job.util.UrlCommand;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class OneTask implements Callable<String>{
	Logger log = LogManager.getLogger("");
	
	UrlCommand urlCmd;
	HttpExchange exchange;
	APIServicer service;
	
	public OneTask(UrlCommand urlCmd, HttpExchange exchange, APIServicer service){
		this.urlCmd = urlCmd;
		this.exchange = exchange;
		this.service = service;
	}
	@Override
	
	public String call() throws Exception {
		try{
			Map<String, String> params = MyUtil.parseQueryString(urlCmd.parameter);
			//params.put("q", URLDecoder.decode(params.get("q"), "UTF-8"));
			String methodName = urlCmd.command;
			log.info(methodName);
			if(urlCmd.post!=null){
				Method method = service.getClass().getDeclaredMethod(methodName, Map.class, String.class);
				String msg = (String)method.invoke(service, params, urlCmd.post);
				sendResponse(exchange, 200, msg);
				return null;
			}else{
				Method method = service.getClass().getDeclaredMethod(methodName, Map.class);
				String msg = (String)method.invoke(service, params);
				sendResponse(exchange, 200, msg);
			return null;
			}
			
		}catch(Exception e){
			log.error(e, e);
			sendResponse(exchange, 512, e.getCause().toString());
			throw e;
		}
	}
	
	private void sendResponse(HttpExchange exchange, int responseCode, String msg){
		try{
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/plain; charset=UTF-8");
			exchange.sendResponseHeaders(responseCode, 0);
			OutputStream responseBody = exchange.getResponseBody();
			String response = msg;
			log.trace("[RESPONSE] " + response);
			responseBody.write(response.getBytes("UTF-8"));
			responseBody.close();
		}catch(Exception e){
			log.error(e, e);
		}
	}
}
