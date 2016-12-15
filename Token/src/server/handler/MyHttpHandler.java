package server.handler;
import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public interface MyHttpHandler extends HttpHandler{
	public void initobExecutor(int poolSize, int queueSize) throws Exception;
	public String reloadService() throws Exception;
	public String rebuildService(Map<String, String> params) throws Exception;
}
