package org.lemsml.gwtui.client.event;
 

import com.google.gwt.core.client.GWT;
 
public class Events {
 
	ListenerList<LoadListener> loadListeners;
 	
 
	boolean reportedPL = false;
	
	
	public Events() {
		resetListeners();
	}
	


	public void clear() {
		resetListeners();
	}
	
	private void resetListeners() {
	 	 
		 loadListeners = new ListenerList<LoadListener>();
	 
	}
	
	
	
	
 
	
	public void addLoadListener(LoadListener pl) {
		loadListeners.add(pl);
 	}
	
	 
 
	 

	public void allReady() {
		GWT.log("Events propagating project ready to " + loadListeners.size());
		loadListeners.reset();
		while (loadListeners.hasNext()) {
			loadListeners.next().allReady();
		}	
		reportedPL = true;
	}

	 

 
 


}
