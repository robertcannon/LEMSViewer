package org.lemsml.gwtui.client.worker;

import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.display.DataViewerFactory;
 
public class WorkerDataViewerFactory extends DataViewerFactory {
 
	SimWorkerServer simWorkerServer;
	
	public WorkerDataViewerFactory(SimWorkerServer sws) {
		simWorkerServer = sws;
	}
	
	
	@Override
	public DataViewer newDataViewer(String s) {
		WorkerDataViewer ret = new WorkerDataViewer(simWorkerServer, s);
		return ret;
	}
	
}
