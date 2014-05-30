package org.lemsml.gwtui.client.worker;
 

import java.util.HashMap;

import org.lemsml.gwtui.client.BrowserMessageHandler;
import org.lemsml.gwtui.client.JsGraphDataViewerFactory;
import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.display.DataViewerFactory;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
 

public class SimWorkerClient {
 
	BrowserMessageHandler logger;
	
	JavaScriptObject worker = null;
	JsGraphDataViewerFactory jsgdf;
	
	
	boolean doneInit = false;
	 	
	HashMap<String, DataViewer> dvHM = new HashMap<String, DataViewer>();
	
	
	String onInitCmd = null;
	
	
	public SimWorkerClient(BrowserMessageHandler bmh, FlowPanel graphPanel) {
		logger = bmh;
		
		jsgdf = new JsGraphDataViewerFactory(graphPanel);
		DataViewerFactory.getFactory().setDelegate(jsgdf);
	
		dvHM.clear();
		
	}
	 
	public void initWorker() {
		logger.msg("Creating a new worker");
		createWorker("", this);
		doneInit = true;
	}
	
	
	public void runTest() {
		onInitCmd = "RUNTEST";
		if (!doneInit) {
			logger.msg("Initializing worker");
			 initWorker();
		} else {
			sendToWorker(onInitCmd);
		}
	}
	
	
	public void runSim(String stxt) {
		onInitCmd = "RUNMODEL " + stxt;
		
		if (!doneInit) {
			logger.msg("Initializing worker");
			 initWorker();
		} else {
			logger.msg("SWC sending RUNMODEL cmd");
			sendToWorker(onInitCmd);
			onInitCmd = null;
		}
	}
	
	
	public void stopSim() {
		sendToWorker("STOP");
	}

	
	public native void createWorker(String url, SimWorkerClient wh) /*-{
	 
      function jsReceive(e) {
 		  if (typeof(e.data) == "object") {
 		  	var pts = e.data.data;
 		  	
  		  	for (var i = 0; i < pts.length; i++) {
 		  		var pt = pts[i];
 		   	    wh.@org.lemsml.gwtui.client.worker.SimWorkerClient::receivePoint(Ljava/lang/String;Ljava/lang/String;DDLjava/lang/String;)(pt.graph, pt.line, pt.x, pt.y, pt.color);		
 		  	}
 		  	
 		  } else {
 		     var str = "" + e.data;
	  	     wh.@org.lemsml.gwtui.client.worker.SimWorkerClient::messageFromWorker(Ljava/lang/String;)(str);
 		  }
      }		
		
	  // worker = new Worker("viewer.nocache.js");  
	  // worker = new Worker("worker.nocache.js");
	
	 worker = new Worker("../task.js");
  	  worker.addEventListener('message', jsReceive, false);
	  worker.postMessage("PING");
	}-*/;
	
	
	
	public native void sendToWorker(String s) /*-{
		if (typeof(worker) == "undefined") {
			console.log("undefined worker");
		} else {
			worker.postMessage(s);	
		} 
	}-*/;
	
	
	public void receivePoint(String grid, String line, double x, double y, String col) {
		dvHM.get(grid).addPoint(line, x, y, col);
	}
	
	
	public void messageFromWorker(String s) {
		int sl = s.length();
		String start = s.substring(0, (sl > 10 ? 10 : sl));
		final String ss = start;
		
		if (start.indexOf("ADDPOINT") >= 0) {
			String[] bits = s.split(" ");
			String id = bits[1];
			String lineid = bits[2];
			
			if (bits.length >= 5) {
				 double x = Double.parseDouble(bits[3]);
				 double y = Double.parseDouble(bits[4]);
				 if (bits.length >= 6) {
					 String col = bits[5];
					 dvHM.get(id).addPoint(lineid, x, y, col);
				 } else {
					 dvHM.get(id).addPoint(lineid, x, y);
				 }
			} else {
				logger.msg("Invalid: " + s);
			} 
			
		} else if (start.indexOf("NEWGRAPH") >= 0) {
			String[] bits = s.split(" ");
			String id = bits[1];
			String snm = (bits.length > 2 ? bits[2] : "");
			DataViewer dv = jsgdf.newDataViewer(snm);
			dvHM.put(id, dv);
			
			
		} else if (start.indexOf("SETREGION") >= 0) {
			String[] bits = s.split(" ");
			String id = bits[1];
			if (bits.length >= 6) {
				double[] xxyy = new double[4];
				for (int i = 0; i < 4; i++) {
					xxyy[i] = Double.parseDouble(bits[2 + i]);
				}
				dvHM.get(id).setRegion(xxyy);
			} else {
				logger.msg("Invalid " + s);
			}
			
		} else if (start.indexOf("BASELOADED") >= 0) {
			// logger.msg("Got base load msg");
			 
		} else if (start.indexOf("LOG") >= 0) {
			logger.msg(s.substring(4, s.length()));
			
			
		} else if (start.indexOf("LOADED") >= 0) {
			logger.msg("Worker reports that it is ready");

			if (onInitCmd != null) {
				String sinit = onInitCmd;
				onInitCmd = null;
				logger.msg("Sending to worker: " + sinit.substring(0, 6));
				sendToWorker(sinit);
			}
			
			
		} else if (start.indexOf("PING") >= 0) {
	 		logger.msg(s);
			Timer t = new Timer() {
				 @Override
				 public void run() {
					 sendToWorker(ss);
				 }
			 };
			 t.schedule(1000);
			 
			
			
		} else {
			logger.msg("Unrecognized action " + start);
		}
	}
	
	 
	
	
}
