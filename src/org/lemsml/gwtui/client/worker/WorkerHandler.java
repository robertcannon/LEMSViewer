package org.lemsml.gwtui.client.worker;
 

import org.lemsml.gwtui.client.BrowserMessageHandler;

import com.google.gwt.core.client.JavaScriptObject;
 

public class WorkerHandler {
 
	BrowserMessageHandler logger;
	
	JavaScriptObject wkr;
	
	public WorkerHandler(BrowserMessageHandler bmh) {
		logger = bmh;
	}
	 
	public void startWorker() {
		createWorker("", this);
	 
	}
	

	public native void createWorker(String url, WorkerHandler wh) /*-{
		
	  function logMsg(s) {
	  	  wh.@org.lemsml.gwtui.client.worker.WorkerHandler::logMessage(Ljava/lang/String;)(s);
	  }	
		
	  wkr = $wnd.newLemsWebWorker(url, logMsg);
	  wkr.reportState();
	}-*/;
	
	
	 public void logMessage(String s) {
		 logger.msg(s);
	 }
	
}
