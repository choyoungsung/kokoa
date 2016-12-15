package server.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

import servicer.APIServicer;
import job.util.MyUtil;
import job.util.ServerConf;
import job.util.ThreadPoolExecutorFn;
import job.util.UrlCommand;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class HandlerToken implements MyHttpHandler{
	Logger log = LogManager.getLogger("");
	
	ThreadPoolExecutorFn jobExecutor = null;
	APIServicer service = null;
	
	public void initobExecutor(int poolSize, int queueSize) throws Exception{
		this.jobExecutor = new ThreadPoolExecutorFn();
		jobExecutor.init(poolSize, queueSize);
		service = new APIServicer();
	}

	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		//String taskID = UUID.randomUUID().toString();
		try{
			URI uri = exchange.getRequestURI();
			
			UrlCommand urlCmd = null;
			try{
				urlCmd  = UrlCommand.newInstance(exchange, null);
			}catch(Exception e){
				String responseStr = "url command parsing error " + uri.toString();
				log.debug(responseStr);
				sendResponse(exchange, 200, responseStr + " desired url Format is http://${host}:${port}/${jobName}.${operation}[;${options}]=${parameter}");
				return;
			}
			
			log.info(exchange.getRemoteAddress() + " => " + exchange.getRequestURI().toASCIIString());

			if(urlCmd.jobID.equals("token")){
				//urlCmd.parameter = URLDecoder.decode(urlCmd.parameter, "UTF-8");
				OneTask task = new OneTask(urlCmd, exchange, service);
				jobExecutor.submitJob(task);
			}else if(urlCmd.jobID.equals("test")){
				sendResponse(exchange, 444, "");
			}else{
				sendResponse(exchange, 444, "");
			}
			
		}catch(Exception e){
			log.error(e, e); 
			String msg = JsonResponse.toJSONStr(JsonResponse.ResultCode.ERROR.toString(),  e.toString() + " CAUSEDBY "  + e.getCause(), null);
			sendResponse(exchange, 200, msg);
		}
	}


	@Override
	public String reloadService() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String rebuildService(Map<String, String> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void sendResponse(HttpExchange exchange, int responseCode, String msg){
		OutputStream responseBody = null;
		try{
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/plain; charset=UTF-8");
			exchange.sendResponseHeaders(responseCode, 0);
			responseBody = exchange.getResponseBody();
			String response = msg;
			log.trace("[RESPONSE] " + response);
			responseBody.write(response.getBytes("UTF-8"));
			responseBody.close();
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	private void sendResponseAsHtml(HttpExchange exchange, int responseCode, String msg){
		try{
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/html; charset=UTF-8");
			exchange.sendResponseHeaders(responseCode, 0);
			OutputStream responseBody = exchange.getResponseBody();
			String response = msg;
			//String response = XmlStyleHttpResponse.getXmlString(result, msg);
			log.trace("[RESPONSE] " + response);
			responseBody.write(response.getBytes("UTF-8"));
			responseBody.close();
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	
	

}
