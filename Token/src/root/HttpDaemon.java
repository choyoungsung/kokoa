package root;


import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import job.util.MyUtil;
import job.util.ServerConf;
import job.util.UrlCommand;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

import server.handler.*;

import com.sun.net.httpserver.*;

public class HttpDaemon {
	static Logger log = LogManager.getLogger("HttpDaemon");
	
	public static void main(String[] args) throws IOException {
		String serverConfFile = "release/token.conf"; 
		try{
			if(args.length>=1){
				serverConfFile = args[0];
			}
			if(!ServerConf.load(serverConfFile)){
				System.err.println("server.conf is invalid");
				System.exit(-1);
			}
			
			HttpServer server = run();
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static HttpServer run()  throws Exception{
		int port = Integer.parseInt(ServerConf.getProperty("server.port"));
		System.setProperty("file.encoding", "UTF-8"); 
		String currEncodingType = System.getProperty("file.encoding");
		log.debug("currEncodingType " + currEncodingType);
		
		InetSocketAddress addr = new InetSocketAddress(port);
		
		String handlerName = ServerConf.getProperty("handler");
		MyHttpHandler serviceHandler =selectHandler(handlerName);
		
		serviceHandler.initobExecutor(100, 1000);
		HttpServer server = HttpServer.create(addr, 0);
		server.createContext("/", serviceHandler);
		//ExecutorService dispatcherExecutor = Executors.newCachedThreadPool();
		ExecutorService dispatcherExecutor = Executors.newFixedThreadPool(1000);
		server.setExecutor(dispatcherExecutor);
		server.start();
		log.info("Server is listening on port " + port );
		
		return server;
	}
	
	// run에 의 해 생성된 HttpServer의 httpContext를 중지하고, 새로운 handler를 설정
	/*public static MyHttpHandler installHandler(HttpServer server) throws Exception{
		String handlerName = ServerConf.getProperty("handler");
		MyHttpHandler dispatcher =selectHandler(handlerName);
		
		dispatcher.initobExecutor(100, 1000);
		server.createContext("/", dispatcher);
		return dispatcher;
	}*/
	
	
	private static MyHttpHandler selectHandler(String handlerName) throws Exception{
		log.trace("handler = <<" + handlerName +">>");
		if(handlerName==null){
			throw new Exception("handler IS NOT DEFINED");
		}
		
		if(handlerName.equals("HandlerMnetSearchClient")){
			return new HandlerToken();
		}else{
			throw new Exception("handler IS NOT DEFINED");
		}
	}
}



/*
<?xml version="1.0" encoding="UTF-8"?>
<aodtrans>
	<result><![CDATA[FAIL]]></result>
	<msg><![CDATA[java.lang.NullPointerException]]></msg>
</aodtrans>
 */


// // $jobID.$command;recvID=$recvID?$parameter



