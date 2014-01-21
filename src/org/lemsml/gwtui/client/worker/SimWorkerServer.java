package org.lemsml.gwtui.client.worker;

import org.lemsml.jlems.core.display.DataViewerFactory;
import org.lemsml.jlems.core.sim.Sim;

import com.google.gwt.user.client.Timer;

public class SimWorkerServer {
 
	Sim sim;
	
 
	
	public SimWorkerServer() {
		WorkerDataViewerFactory wdf = new WorkerDataViewerFactory(this);
		DataViewerFactory.getFactory().setDelegate(wdf);
	
		createJsHandler(this);
		
		sendMessageToClient("LOADED");
	}
	
	 
	public native void createJsHandler(SimWorkerServer sws) /*-{
  		self.onmessage = function(e) {
	 		var str = "" + e.data;
  	  		sws.@org.lemsml.gwtui.client.worker.SimWorkerServer::messageFromClient(Ljava/lang/String;)(str);
  		}		
  		
  	 
	}-*/;
	
	
	
	
	
	
	public void messageFromClient(String s) {
		String start = s.substring(0, 10);
		if (start.indexOf("STOP") >= 0) {
			if (sim != null) {
			//	sim.setStop();
			}
			
		} else if (start.indexOf("PING") >= 0) {
			sendMessageToClient("PONG");
			
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
    
	

	public void startNewGraph(String myId, String title) {
		String msg = "NEWGRAPH " + myId + " " + title;
		sendMessageToClient(msg);
	}


	public void addPoint(String myId, String line, double x, double y, String scol) {
		// TODO Auto-generated method stub
		String msg = "ADDPOINT " + myId + " " + line + " " + x + " " + y + " " + scol;
		sendMessageToClient(msg);
	}


	public void setRegion(String myId, double[] xxyy) {
		String msg = "SETREGION " + myId + " " + xxyy[0] + " " + xxyy[1] + " " + xxyy[2] + " " + xxyy[3];
		sendMessageToClient(msg);
	}
	 
}