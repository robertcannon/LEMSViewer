package org.lemsml.gwtui.client;

public class State {
	final static int WEBWORKER = 1;
	final static int SYNCHRONOUS = 2;
	
	int executionMode = WEBWORKER;

	
	
	public void setRunWebworker() {
		executionMode = WEBWORKER;
	}

	public void setRunSync() {
		executionMode = SYNCHRONOUS;
	}

	public boolean runAsWebworker() {
		boolean ret = false;
		if (executionMode == WEBWORKER) {
			ret = true;
		}
		return ret;
	}

	public boolean runSync() {
		boolean ret = false;
		if (executionMode == SYNCHRONOUS) {
			ret = true;
		}
		return ret;
	}
}
