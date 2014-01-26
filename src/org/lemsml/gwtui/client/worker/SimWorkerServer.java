package org.lemsml.gwtui.client.worker;

import org.lemsml.jlems.core.display.DataViewerFactory;
import org.lemsml.jlems.core.sim.Sim;

import com.google.gwt.core.client.JavaScriptObject;
 
public class SimWorkerServer {
 
	Sim sim;
	
	boolean groupPoints = true;
	
	JavaScriptObject pointArray = null;
	JavaScriptObject pointGroupSize = null;
	JavaScriptObject lastSend = null;
	
	public SimWorkerServer() {
		WorkerDataViewerFactory wdf = new WorkerDataViewerFactory(this);
		DataViewerFactory.getFactory().setDelegate(wdf);
	
		createJsHandler(this);
		
		sendMessageToClient("LOADED");
	}
	
	 
	public native void createJsHandler(SimWorkerServer sws) /*-{
		
		pointGroupSize = 10;
		lastSend = 0;
		
		
  		self.onmessage = function(e) {
	 		var str = "" + e.data;
  	  		sws.@org.lemsml.gwtui.client.worker.SimWorkerServer::messageFromClient(Ljava/lang/String;)(str);
  		}		
	}-*/;
	
	
	
	public void messageFromClient(String s) {
 		int sl = s.length();
		if (sl > 10) {
			sl = 10;
		}
		String start = s.substring(0, sl);
		if (start.indexOf("STOP") >= 0) {
			if (sim != null) {
			//	sim.setStop();
			}
			
		} else if (start.indexOf("RUNTEST") >= 0) {
			sendMessageToClient("PING 1");
			
			
		} else if (start.indexOf("PING") >= 0) {
			String[] sa = start.split(" ");
			if (sa.length > 1) {
				int ino = Integer.parseInt(sa[1]);
				if (ino < 10) {
					sendMessageToClient("PING " + (ino + 1));
				} else {
					sendMessageToClient("LOG test complete");
				}
			}
			
		} else if (start.indexOf("RUNMODEL") >= 0) {
			String rest = s.substring(9, s.length());
			runSim(rest);
		
		} else {
			logMsg("Unrecognized msg received in worker server " + start);
		}
		
	}
	
	
	
	public void runSim(String stxt) {
		try {
			sim = new Sim(stxt);
	
			sim.readModel();

			logMsg("Read the model");
			sim.setMaxExecutionTime(0);
			
			sim.build();
			logMsg("Built...");
			sim.run();
			logMsg("Completed run");
			
			flushPoints();
			
		} catch (Exception ex) {
			logMsg("error " + ex);
		}
	}
	
	
	
	
	private void logMsg(String s) {
		sendMessageToClient("LOG " + s);
	}

	
	public native void sendMessageToClient(String s) /*-{
	 	postMessage(s);
	 }-*/;
    
	
	public native void initQueue() /*-{
		pointArray = [];
	}-*/;
	
	public native void queuePoint(String grid, String line, double x, double y, String scol) /*-{
		var o = {};
		o.graph = grid;
		o.line = line;
		o.x = x;
		o.y = y;
		o.color = scol;
		pointArray.push(o);
		
		if (pointArray.length >= pointGroupSize) {
			var msg = {};
			msg.id = "POINTS";
			msg.data = pointArray;
			postMessage(msg);
    		pointArray = [];
    		
    		var tnow = (new Date()).getTime();
    		if (lastSend > 0) {
    			var dt = tnow - lastSend;
    			if (dt < 1) {
    				dt = 1;
    			} 
    			var pgs = (250 / dt) * pointGroupSize;
    			pointGroupSize = Math.round(0.7 * pointGroupSize + 0.3 * pgs);
    		}
    		lastSend = tnow;
		}
	}-*/;
	
	
	public native void flushPoints() /*-{
	    if (pointArray.length > 0) {
		    var msg = {};
		    msg.id = "POINTS";
		    msg.data = pointArray;
		    postMessage(msg);
		    pointArray = [];
	    }
    }-*/;
	
	
	public void startNewGraph(String myId, String title) {
		initQueue();
		String msg = "NEWGRAPH " + myId + " " + title;
		sendMessageToClient(msg);
	}


	public void addPoint(String grId, String line, double x, double y, String scol) {
		// TODO Auto-generated method stub
		
		if (groupPoints) {
			queuePoint(grId, line, x, y, scol);
			
		} else {
			String msg = "ADDPOINT " + grId + " " + line + " " + x + " " + y + " " + scol;
			sendMessageToClient(msg);			
		}
	}


	public void setRegion(String myId, double[] xxyy) {
		String msg = "SETREGION " + myId + " " + xxyy[0] + " " + xxyy[1] + " " + xxyy[2] + " " + xxyy[3];
		sendMessageToClient(msg);
	}
	 
}
