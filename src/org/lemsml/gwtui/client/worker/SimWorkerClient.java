package org.lemsml.gwtui.client.worker;
 

import org.lemsml.gwtui.client.BrowserMessageHandler;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Timer;
 

public class SimWorkerClient {
 
	BrowserMessageHandler logger;
	
	JavaScriptObject worker = null;
	
	public SimWorkerClient(BrowserMessageHandler bmh) {
		logger = bmh;
	}
	 
	public void initWorker() {
		createWorker("", this);
	 
	}
	
	
	public void runSim(String stxt) {
		if (worker == null) {
			logger.msg("SWC initializing worker");
			 initWorker();
		}
		logger.msg("SWC sending RUNMODEL cmd");
		sendToWorker("RUNMODEL " + stxt);
	}
	
	
	public void stopSim() {
		sendToWorker("STOP");
	}

	
	public native void createWorker(String url, SimWorkerClient wh) /*-{
	 
      function jsReceive(e) {
 		 console.log("worker said " + e.data);
		 var str = "" + e.data;
	  	  wh.@org.lemsml.gwtui.client.worker.SimWorkerClient::messageFromWorker(Ljava/lang/String;)(str);
      }		
		
	  // worker = new Worker("viewer.nocache.js");  
	  // worker = new Worker("worker.nocache.js");
	
	 console.log("loading js...");
	  worker = new Worker("../task.js");

 	console.log("made worker");
  	  worker.addEventListener('message', jsReceive, false);
	 
	 console.log("added EL");
	  worker.postMessage("PING");
	}-*/;
	
	
	
	public native void sendToWorker(String s) /*-{
		console.log("Sending to worker " + s);
		worker.postMessage(s);	
		console.log("done");
	}-*/;
	
	
	
	public void messageFromWorker(String s) {
		logger.msg(s);
		int sl = s.length();
		String start = s.substring(0, (sl > 10 ? 10 : sl));
		if (start.indexOf("ADDPOINT") >= 0) {
		
		} else if (start.indexOf("NEWGRAPH") >= 0) {
			
		} else if (start.indexOf("SETREGION") >= 0) {
			
		} else if (start.indexOf("BASELOADED") >= 0) {
			logger.msg("Got base load msg");
			 
		} else if (start.indexOf("LOG") >= 0) {
			logger.msg(s.substring(4, s.length()));
			
		} else if (start.indexOf("LOADED") >= 0) {
			sendToWorker("PING");
				
		} else if (start.indexOf("PONG") >= 0) {
	 		 Timer t = new Timer() {
				 @Override
				 public void run() {
					 sendToWorker("PING");
				 }
			 };
			 t.schedule(2500);
			 
			
			
		} else {
			logger.msg("Unrecognized action " + start);
		}
	}
	
	 
	
	
}
