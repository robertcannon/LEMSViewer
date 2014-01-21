package org.lemsml.gwtui.client.guilib;

public class UserAction {

	String label;
	
	Action action;
	
	
	public UserAction(String lbl, Action a) {
		label = lbl;
		action = a;
	}
	
	public String getLabel() {
		return label;
	}

	public Action getAction() {
		return action;
	}
	 
}
