package org.lemsml.gwtui.client;

import org.lemsml.gwtui.client.event.Bundle;
import org.lemsml.gwtui.client.guilib.Action;
import org.lemsml.gwtui.client.guilib.ActionButton;
import org.lemsml.gwtui.client.guilib.CommandHandler;
import org.lemsml.gwtui.client.guilib.UserAction;
import org.lemsml.jlems.core.check.ParserCheck;
import org.lemsml.jlems.core.logging.E;
 
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;



public class CheckRunner implements CommandHandler {

	
	FlowPanel resultPanel;
	
	UserAction uaRunParser = new UserAction("Run parse test", Action.RUN);
	
	UserAction uaRunExample = new UserAction("Run example test", Action.RUN);
	
	boolean doneLemsInit = false;
	
	
	BrowserMessageHandler bmh;
	
	LEMSViewer lemsServer;
	
	public CheckRunner(LEMSViewer lgwt) {
		lemsServer = lgwt;
	}
	
	
	
	public void displayIn(RootPanel rp) {
		FlowPanel fp = new FlowPanel();
		rp.add(fp);
		
		FlowPanel fpb = new FlowPanel();
		fp.add(fpb);
		
		ActionButton runB = new ActionButton(uaRunParser, this);
		fpb.add(runB.getWidget());
		
		ActionButton runexampleB = new ActionButton(uaRunExample, this);
		fpb.add(runexampleB.getWidget());
		
		resultPanel = new FlowPanel();
		fp.add(resultPanel);
		 
	
	}


	private void lemsInit() {
		bmh = new BrowserMessageHandler(resultPanel);
		
		E.setMessageHandler(bmh);
		
		doneLemsInit = true;
	}
	

	@Override
	public void doCommand(UserAction ma, Widget w, Bundle payload) {
		if (!doneLemsInit) {
			lemsInit();
		}
		bmh.clear();
		
		if (ma.equals(uaRunParser)) {
			bmh.msg("Running parser check");
			
			ParserCheck pc = new ParserCheck();
			pc.checkExamples();

		} else if (ma.equals(uaRunExample)) {
			bmh.msg("Loading example data");
			
		 
			
		}
		
	}
	
	
}
