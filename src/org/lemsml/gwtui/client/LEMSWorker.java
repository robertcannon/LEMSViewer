package org.lemsml.gwtui.client;

import org.lemsml.gwtui.client.worker.SimWorkerServer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
 
public class LEMSWorker implements EntryPoint {
	 
	 
	public void onModuleLoad() {
		
		conslog("worker module load...");
		SimWorkerServer sws = new SimWorkerServer();
	}	

	 
	 
	private native void conslog(String str) /*-{
		console.log(str);
	 }-*/;
	
	
	 
}
