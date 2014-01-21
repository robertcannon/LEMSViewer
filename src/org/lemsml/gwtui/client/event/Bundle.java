package org.lemsml.gwtui.client.event;

import java.util.HashMap;

public class Bundle {

	Object obj;
	Object o1;
	
	HashMap<String, Object> itemHM = new HashMap<String, Object>();
	
	public Bundle() {

	}
	

	public Bundle(Object o) {
		obj = o;
	}	
	
	public Bundle(Object o, Object ao) {
		obj = o;
		o1 = ao;
	}
	
	public void addItem(String s, Object o) {
		itemHM.put(s, o);
	}
	
	
	public Object get() {
		return obj;
	}
	
	public String getString() {
		return (String)obj;
	}
	
	
	public Object get(int i) {
		Object ret = null;
		if (i == 0) {
			ret = get();
		} else if (i == 1) {
			ret = get1();
		} else {
			// TODO - ever needed?  Deprecate get1
		}
		return ret;
	}
	
	public Object get1() {
		return o1;
	}

	public Object getItem(String s) {
		return itemHM.get(s);
	}
	
}
