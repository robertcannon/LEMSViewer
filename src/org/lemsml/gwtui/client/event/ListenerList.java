package org.lemsml.gwtui.client.event;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

public class ListenerList<T extends Object> {

	ArrayList<T> items = new ArrayList<T>();
	
	int counter;
	
	
	public void reset() {
		counter = 0;
	}
	
	
	public void remove(T t) {
		if (items.contains(t)) {
			items.remove(t);
		}
	}
	
	public void add(T t) {
		if (items.contains(t)) {
			GWT.log("Warning - tried to add same listener twice " + t);
		} else {
			items.add(t);
		}
	}
	
	
	public boolean hasNext() {
		boolean ret = false;
		if (counter < items.size()) {
			ret = true;
		}
		return ret;
	}
	
	
	public T next() {
		T ret = null;
		if (counter < items.size()) {
			ret = items.get(counter);
		}
		counter += 1;
		return ret;
	}


	public int size() {
		return items.size();
	}
	
	
}
