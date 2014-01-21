package org.lemsml.gwtui.client.customimages;

import org.lemsml.gwtui.client.guilib.Action;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
 



public class CustomButtonMap extends ButtonMap {
	
	CustomImages ci;
	
	public CustomButtonMap() {
		ci =  GWT.create(CustomImages.class);
	}

	@Override
	public ImageResource getImage(Action a) {
		ImageResource ret = null;
		if (a.equals(Action.ADD_COMMENT)) {
			ret = ci.comment();
		
		} else if (a.equals(Action.ADD_INFO)) {
			ret = ci.add();
		
		} else if (a.equals (Action.APPROVE)) {
			ret = ci.ok();
		
		} else if (a.equals(Action.BACK)) {
			ret = ci.back();
			
		} else if (a.equals(Action.BROWSE)) {
			ret = ci.pdfIcon();
		
		} else if (a.equals(Action.CANCEL)) {
			ret=  ci.cancel();
		
		} else if (a.equals(Action.DOWNLOAD)) {
			ret = ci.download();
		
		} else if (a.equals(Action.CLOSE)) {
			//ret= ci.close();
		
		} else if (a.equals(Action.EDIT)) {
			ret = ci.edit();
		
		} else if (a.equals(Action.EXPORT)) {
			// ret = ci.export();
			
		} else if (a.equals(Action.EXPORT_EXCEL)) {
			ret = ci.excelExport();
			
			
		} else if (a.equals(Action.FILES)) {
			//ret=  ci.files();
		
		} else if (a.equals(Action.PLAYVIDEO)) {
			ret = ci.film();
		
		} else if (a.equals(Action.FILTER)) {
			ret=  ci.filter();
			
		} else if (a.equals(Action.GO)) {
			ret = ci.go();
		
		} else if (a.equals(Action.HELP)) {
			ret = ci.help();
		
		} else if (a.equals(Action.HIDE_DETAILS)) {
			//ret= ci.hideDetails();
		
		} else if (a.equals(Action.LOGIN)) {
			ret= ci.login();
		
		} else if (a.equals(Action.LOGOUT)) {
			ret = ci.logout();
		
		} else if (a.equals(Action.NEW_PROJECT)) {
			ret = ci.add();
		
		} else if (a.equals(Action.NEXT)) {
			ret = ci.next();
		
		} else if (a.equals(Action.OK)) {
			ret= ci.ok();
		
		} else if (a.equals(Action.PREVIOUS)) {
			ret = ci.previous();
			
		} else if (a.equals(Action.SAVE)) {
			ret= ci.save();
		
		} else if (a.equals(Action.SHOW_DETAILS)) {
			//ret= ci.showdetails();
		
		} else if (a.equals(Action.SUBMIT)) {
			ret = ci.submit();
		
		} else if (a.equals(Action.UPLOAD)) {
			ret= ci.upload();
		
		} else if (a.equals(Action.VIEW)) {
			ret = ci.view();
 		
		} else if (a.equals(Action.PREVIEW)) {
 			ret = ci.view();
		
		} else if (a.equals(Action.REVIEW)) {
 			ret = ci.view();
 			
 		} else if (a.equals(Action.APPLY)) {
 			ret = ci.submit();
 		
 		} else if (a.equals(Action.SELECT)) {
 			ret = ci.define();
 			
 		} else if (a.equals(Action.DEFINE)) {
 			ret = ci.define();
 			
 		}
		
		
		return ret;
	}
	
}
