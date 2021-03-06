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
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;



public class JLEMSRunner implements CommandHandler, ModelFileUser, ResizeHandler,
	SplitHandler, KeyUpHandler {

	
	TextBox urlTB;
	
 	XMLEditor editor;
 
	FlowPanel resultPanel;
	
	ScrollPanel graphScroll;
	FlowPanel graphPanel;
	
	ScrollPanel logScroll;
	
	UserAction uaRun = new UserAction("Run", Action.RUN);
	
	UserAction uaLoad = new UserAction("Open", Action.RUN);
	
	UserAction uaOptions= new UserAction("Options", Action.RUN);
	
	// UserAction uaRunExample = new UserAction("Run ModelFileUserexample test", Action.RUN);
	
	boolean doneLemsInit = false;
	
	
	BrowserMessageHandler logger;
	
	LEMSViewer lemsServer;
	
	SimWorkerClient simWorkerClient = null;
	
	State state = new State();
	
	ActionButton optionsButton = null;
	OptionsDialog optionsDialog = null;
	
	
	public JLEMSRunner(LEMSViewer lgwt) {
		lemsServer = lgwt;
	}
	
	
	
	public void buildPage() {
		ReportingSplitLayoutPanel mainLayout = new ReportingSplitLayoutPanel(this);
		RootLayoutPanel.get().add(mainLayout);
		
		FlowPanel headerPanel = new FlowPanel();
		
		headerPanel.addStyleName("headrow");
		 
		
		Label ttl = new Label("LEMS Live");
		ttl.addStyleName("sidetitle");
		headerPanel.add(ttl);
		
		urlTB = new TextBox();
		urlTB.addStyleName("urlinput");
		headerPanel.add(urlTB);
		urlTB.addKeyUpHandler(this);
		
		ActionButton loadB = new ActionButton(uaLoad, this);
		headerPanel.add(loadB.getWidget());
		
		ActionButton runB = new ActionButton(uaRun, this);
		headerPanel.add(runB.getWidget());
	 	 
		
		optionsButton = new ActionButton(uaOptions, this);
		headerPanel.add(optionsButton.getWidget());
	 
		 	
		
		mainLayout.addNorth(headerPanel, 32);
	
		
		FlowPanel bottomRow = new FlowPanel();
		mainLayout.addSouth(bottomRow, 0);
		
		
		graphScroll = new ScrollPanel();
		graphScroll.addStyleName("graphscroll");
		graphPanel = new FlowPanel();
		graphPanel.addStyleName("graphpanel");
		graphScroll.add(graphPanel);
		mainLayout.addEast(graphScroll, 600);
		graphScroll.setHeight("600px");	
		

		logScroll = new ScrollPanel();
		mainLayout.addSouth(logScroll, 200);
		resultPanel = new FlowPanel();
		logScroll.add(resultPanel);
		logger = new BrowserMessageHandler(resultPanel);
		E.setMessageHandler(logger);
		
		lemsServer.setLogger(logger);
		
		editor = new XMLEditor();
		editor.setHeight(600);
		mainLayout.add(editor.getPanel());
	
	
		
		DataViewerFactory dvf = new JsGraphDataViewerFactory(graphPanel);
		DataViewerFactory.getFactory().setDelegate(dvf);
		
		Window.addResizeHandler(this);
	
		resizeComponents();
	 
	}
		
	public void splitMoved() {
		resizeComponents();
	}
	
	private void doLoad() {
		String stxt = urlTB.getText(); 
		loadURL(stxt);
	}
	
	private void doRun() {
		String stxt = editor.getText(); 
		if (state.runAsWebworker()) {
			runWorkerSim(stxt);
		} else {
			runSim(stxt);
		}
	}	

	@Override
	public void doCommand(UserAction ma, Widget w, Bundle payload) {
		logger.clear();
		
		if (ma.equals(uaLoad)) {
			doLoad();
			
		} else if (ma.equals(uaRun)) {
			doRun();
		
		} else if (ma.equals(uaOptions)) {
			if (optionsDialog == null) {
				optionsDialog = new OptionsDialog(state);
			}
			optionsDialog.showDialog(optionsButton.getWidget());
			 
		
		} 
		 
	}
	 
	
	public void loadURL(String stxt) {
		ResourceInclusionLoader ril = new ResourceInclusionLoader(lemsServer, logger, stxt, this);
		ril.startLoad();
 	}
	

	@Override
	public void gotFile(String stxt) {
		logger.msg("Runner got the file, length=" + stxt.length());
		editor.setText(stxt);
		resizeComponents();
		Timer t = new Timer() {
			public void run() {
				doRun();
			}
		};
		// doing the run in a timeout so the editor gets a chance to repaint itself
		// doRun();
		t.schedule(50);
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
			simWorkerClient = new SimWorkerClient(logger, graphPanel);
			
		} else {
			simWorkerClient.stopSim();
		}
		
		simWorkerClient.runSim(stxt);
	}
	
	
	private void testWorker() {
		logger.clear();
		graphPanel.clear();
		
		if (simWorkerClient == null) {
			simWorkerClient = new SimWorkerClient(logger, graphPanel);
			
		} else {
			simWorkerClient.stopSim();
		}
		
		simWorkerClient.runTest();
	}
	
	
	
	
	
	
	
	
	

	private String pst(StackTraceElement[] stackTrace) {
		String ret = "";
		for (StackTraceElement ste : stackTrace) {
			ret += ste + "\n";
		}
		return ret;
	}


 


	@Override
	public void onResize(ResizeEvent event) {
		resizeComponents();
	}
	
	
	private void resizeComponents() {
		int wh = Window.getClientHeight();
			
		int xptop = editor.getPanel().getAbsoluteTop();
		int lstop = logScroll.getAbsoluteTop();
		logScroll.setHeight((wh - lstop) + "px");
		
		editor.setHeight(lstop - xptop);
				
		int gstop = graphScroll.getAbsoluteTop();
		graphScroll.setHeight((wh - gstop) + "px");
	}



	@Override
	public void onKeyUp(KeyUpEvent event) {
		int kc = event.getNativeKeyCode();
		if (kc == 13) {
			doLoad();
		}
	}



	public void displayLoadAndRun(String sm) {
		urlTB.setValue(sm);
		loadURL(sm);
	}
	
	
}
