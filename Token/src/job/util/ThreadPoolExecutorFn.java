package job.util;


import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// http://download.oracle.com/javase/6/docs/api/java/util/concurrent/ThreadPoolExecutor.html
// �۾� �Ϸ�� ���Ŵ� ��� �ؾ� �ϴµ�
// executor�� Queue�� ��ü �Һ��ϸ鼭 �� �ǰ� �ִ�. 
// ProcessCallable ť�� �۾� �Ϸ��� ��� ���ؾ� �ϳ�? ;
public class ThreadPoolExecutorFn {
	static Logger log = LogManager.getLogger("ThreadPoolExecutorFn");
	private ThreadPoolExecutor pool = null;
	//private ConcurrentMap<String, Future<String>> poolOfFuture = null; //   submit
	
	public void init(int poolSize, int queueSize){
		int maxPoolSize = poolSize*2;
		int keepAliveTime = 10;
		BlockingQueue<Runnable> queue =new ArrayBlockingQueue<Runnable>(queueSize);
		pool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
		//poolOfFuture = new ConcurrentHashMap<String, Future<String>>();
	}
	
	/*public boolean isWorking(String runOnlyOneID){
		if(poolOfFuture==null || runOnlyOneID==null){
			return false;
		}
		
		Future<String> prev = poolOfFuture.get(runOnlyOneID);
		if(prev!=null){
			if(prev.isCancelled()||prev.isDone()){
				poolOfFuture.remove(runOnlyOneID);
				return false;
			}else{
				return true;
				//throw new Exception("[REJECT IN submitJob] runOnlyOneID is working");
			}
		}else{
			return false;
		}
	}*/
	
	public boolean submitJob(Callable task) throws Exception{
		if(task==null){
			throw new Exception("[ERROR IN submitJob ] TASK IS NULL");
		}
		
		try{
			Future<String> myFuture = pool.submit(task);
			return true;
		}catch(IllegalStateException ise){
			log.trace("submit Failed by Queue Full");
			throw new Exception("[ERROR IN submitJob ] submit Failed by Queue Full");
		}catch(Exception e){
			throw e;
		}
	}
}




