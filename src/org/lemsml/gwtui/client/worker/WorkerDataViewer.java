package org.lemsml.gwtui.client.worker;

import org.lemsml.jlems.core.display.DataViewer;

import com.google.gwt.user.client.ui.FlowPanel;

public class WorkerDataViewer implements DataViewer {
 
	SimWorkerServer simWorkerServer;
	String myId;
	
	public WorkerDataViewer(SimWorkerServer sws, String title) {
		simWorkerServer = sws;
		myId = "" + Math.round(1.e8 * Math.random());	  
	
		simWorkerServer.startNewGraph(myId, title);
	}
	  
	 
	@Override
	public void addPoint(String line, double x, double y) {
		addPoint(line, x, y, "#000000");
		
	}

	@Override
	public void addPoint(String line, double x, double y, String scol) {
		// TODO Auto-generated method stub
		// addPointToGraph(myId, line, x, y, scol);
		simWorkerServer.addPoint(myId, line, x, y, scol);
	}

	
	@Override
	public void showFinal() {
		// TODO Auto-generated method stub	
	}

	
	@Override
	public void setRegion(double[] xxyy) {
	  //	setGraphRange(myId, xxyy[0], xxyy[1], xxyy[2], xxyy[3]);
		simWorkerServer.setRegion(myId, xxyy);
	}


	 
	
}
