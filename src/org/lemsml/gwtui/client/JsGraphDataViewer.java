package org.lemsml.gwtui.client;

import org.lemsml.jlems.core.display.DataViewer;

import com.google.gwt.user.client.ui.FlowPanel;

public class JsGraphDataViewer implements DataViewer {

	FlowPanel container;
	
	String myId;
	
	public JsGraphDataViewer(FlowPanel ctr, String title) {
		container = ctr;
	
		myId = "" + Math.round(1.e8 * Math.random());
		container.getElement().setId(myId);
		createGraph(myId, title);
	}
	

	public native void createGraph(String id, String title) /*-{
	  if (!$wnd.graphMap) {
	  	$wnd.graphMap = {};
	  }
	  var par = $doc.getElementById(id);
	  var g = $wnd.newGraph(par, title);
	  $wnd.graphMap[id] = g;  
	}-*/;
	
	
	public native void addPointToGraph(String id, String line, double x, double y, String col) /*-{
		 var g = $wnd.graphMap[id];
		 g.addPoint(line, x, y, col);
	}-*/;
	
	public native void setGraphRange(String id, double xa, double xb, double ya, double yb) /*-{
	 var g = $wnd.graphMap[id];
	 g.setRanges(xa, xb, ya, yb);
	}-*/;
	 
	@Override
	public void addPoint(String line, double x, double y) {
		addPoint(line, x, y, "#000000");
		
	}

	@Override
	public void addPoint(String line, double x, double y, String scol) {
		// TODO Auto-generated method stub
		addPointToGraph(myId, line, x, y, scol);
	}

	
	@Override
	public void showFinal() {
		// TODO Auto-generated method stub	
	}

	
	@Override
	public void setRegion(double[] xxyy) {
		setGraphRange(myId, xxyy[0], xxyy[1], xxyy[2], xxyy[3]);
	}


	 
	
}
