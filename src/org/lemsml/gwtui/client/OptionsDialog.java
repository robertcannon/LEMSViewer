package org.lemsml.gwtui.client;

import org.lemsml.gwtui.client.event.Bundle;
import org.lemsml.gwtui.client.guilib.Action;
import org.lemsml.gwtui.client.guilib.BaseDialog;
import org.lemsml.gwtui.client.guilib.CommandHandler;
import org.lemsml.gwtui.client.guilib.RadioActionButton;
import org.lemsml.gwtui.client.guilib.UserAction;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OptionsDialog extends BaseDialog implements CommandHandler {

	State state;
	
	UserAction uaWeb = new UserAction("web", Action.RUN);
	UserAction uaSync = new UserAction("sync", Action.RUN);
	
	public OptionsDialog(State s ) {
		super("Options", "Close");
		
		state = s;
		
		FlexTable ft = new FlexTable();
	 	ft.setCellPadding(4);
	 	
		ft.setText(0, 0, "Model Execution");
		RadioActionButton rab1 = new RadioActionButton("exe", uaWeb, this, "Web Worker", state.runAsWebworker());
		RadioActionButton rab2 = new RadioActionButton("exe", uaSync, this, "Synchronous", state.runSync());
		ft.setWidget(0, 1, rab1.getWidget());
		ft.setWidget(1, 1, rab2.getWidget());
		 
		
		bodyAdd(ft);
	}

	
	
	@Override
	public void doApply(Widget w) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void doCommand(UserAction ma, Widget w, Bundle payload) {
		if (ma == uaWeb) {
			state.setRunWebworker();
			
		} else if (ma == uaSync) {
			state.setRunSync();
 		}
	}
	
	
}
