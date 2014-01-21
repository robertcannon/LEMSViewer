package org.lemsml.gwtui.client;

import org.lemsml.gwtui.client.event.Bundle;
import org.lemsml.gwtui.client.guilib.Action;
import org.lemsml.gwtui.client.guilib.ActionButton;
import org.lemsml.gwtui.client.guilib.CommandHandler;
import org.lemsml.gwtui.client.guilib.UserAction;
import org.lemsml.gwtui.client.worker.SimWorkerClient;
import org.lemsml.jlems.core.check.ParserCheck;
import org.lemsml.jlems.core.display.DataViewerFactory;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.Sim;
 
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;



public class JLEMSRunner implements CommandHandler, ModelFileUser, ChangeHandler, ResizeHandler,
	SplitHandler {

	
	ListBox exampleLB;
	
	XMLEditor editor;
 
	FlowPanel resultPanel;
	
	ScrollPanel graphScroll;
	FlowPanel graphPanel;
	
	UserAction uaRun = new UserAction("Run", Action.RUN);
	
	UserAction uaWkr = new UserAction("WorkerTest", Action.RUN);
	
	// UserAction uaRunExample = new UserAction("Run ModelFileUserexample test", Action.RUN);
	
	boolean doneLemsInit = false;
	
	
	BrowserMessageHandler logger;
	
	LEMSViewer lemsServer;
	
	SimWorkerClient simWorkerClient = null;
	
	
	public JLEMSRunner(LEMSViewer lgwt) {
		lemsServer = lgwt;
	}
	
	
	
	public void buildPage() {
		ReportingSplitLayoutPanel mainLayout = new ReportingSplitLayoutPanel(this);
		RootLayoutPanel.get().add(mainLayout);
		
		FlowPanel headerPanel = new FlowPanel();
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(4);
		
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		headerPanel.add(hp);
		
		Label ttl = new Label("jLEMS via GWT");
		ttl.addStyleName("sidetitle");
		hp.add(ttl);
		
		
		Label lbl = new Label("Select model: ");
		hp.add(lbl);
		
		exampleLB = new ListBox(false);
		exampleLB.addChangeHandler(this);
		hp.add(exampleLB);
		
		ActionButton runB = new ActionButton(uaRun, this);
		hp.add(runB.getWidget());
		
		
		ActionButton wkrB = new ActionButton(uaWkr, this);
		hp.add(wkrB.getWidget());
	 
		
		mainLayout.addNorth(headerPanel, 32);
	
		
		editor = new XMLEditor();
		editor.setHeight(600);
	
		mainLayout.addWest(editor.getPanel(), 300);
	
	
		ScrollPanel logScroll = new ScrollPanel();
		mainLayout.addNorth(logScroll, 200);
		resultPanel = new FlowPanel();
		logScroll.add(resultPanel);
		logger = new BrowserMessageHandler(resultPanel);
		E.setMessageHandler(logger);
		
		
		graphScroll = new ScrollPanel();
		graphScroll.addStyleName("graphscroll");
		
		graphPanel = new FlowPanel();
		
		graphPanel.addStyleName("graphpanel");
		
		graphScroll.add(graphPanel);
		mainLayout.add(graphScroll);
		graphScroll.setHeight("600px");	
	
	
		
		DataViewerFactory dvf = new JsGraphDataViewerFactory(graphPanel);
		DataViewerFactory.getFactory().setDelegate(dvf);
		
		
		lemsServer.getExampleFiles(logger, this);
		
		Window.addResizeHandler(this);
	
		resizeComponents();
	 
	}
		
	public void splitMoved() {
		resizeComponents();
	}
	 
	

	@Override
	public void doCommand(UserAction ma, Widget w, Bundle payload) {
		logger.clear();
		
		if (ma.equals(uaRun)) {
			String stxt = editor.getText(); 
			runSim(stxt);
		
		} else if (ma.equals(uaWkr)) {
			String stxt = editor.getText(); 
			runWorkerSim(stxt);
		}
		
		
	}
	 

	@Override
	public void gotFile(String stxt) {
		logger.msg("Got the file...");
		editor.setText(stxt);
	}
	
	
	public void gotFileList(String[] fl) {
		exampleLB.clear();
		for (int i = 0; i < fl.length; i++) {
			exampleLB.addItem(fl[i]);
		}
	}
	
	
	
	private void runSim(String stxt) {
		Sim sim = new Sim(stxt);
		
		logger.clear();
		graphPanel.clear();
		
		try {
		sim.readModel();
		
		logger.msg("Read the model");
		
		sim.setMaxExecutionTime(0);
    
		sim.build();
		logger.msg("Built...");
		
		sim.run();
    
		E.info("OK - completed run");
		} catch (Exception ex) {
			E.info("Exception thrown: " + ex + " " + pst(ex.getStackTrace()));
		}
	}
 
	private void runWorkerSim(String stxt) {
		logger.clear();
		graphPanel.clear();
		
		if (simWorkerClient == null) {
			simWorkerClient = new SimWorkerClient(logger);
			
		} else {
			simWorkerClient.stopSim();
		}
		
		simWorkerClient.runSim(stxt);
	}
	
	
	
	

	private String pst(StackTraceElement[] stackTrace) {
		String ret = "";
		for (StackTraceElement ste : stackTrace) {
			ret += ste + "\n";
		}
		return ret;
	}



	@Override
	public void onChange(ChangeEvent event) {
		int idx = exampleLB.getSelectedIndex();
		String fnm = exampleLB.getItemText(idx);
		editor.setText("");
		logger.clear();
		logger.msg("Loading " + fnm);
		lemsServer.fetchModel(fnm, logger, this);
	}



	@Override
	public void onResize(ResizeEvent event) {
		resizeComponents();
	}
	
	
	private void resizeComponents() {
		int wh = Window.getClientHeight();
			
		int xptop = editor.getPanel().getAbsoluteTop();
		editor.setHeight(wh - xptop);
		
		int gstop = graphScroll.getAbsoluteTop();
		graphScroll.setHeight((wh - gstop) + "px");
	}
	
	
}
