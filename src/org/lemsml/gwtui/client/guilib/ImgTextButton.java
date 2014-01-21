package org.lemsml.gwtui.client.guilib;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;


public class ImgTextButton extends Button {
 
    private String text;

    Image img;
    Element textSpan;
    
    public ImgTextButton() {
        super();
    }

    public ImgTextButton(ImageResource imageResource, String txt) { 
    	super();
    	text = txt;
    	img = new Image(imageResource);     
        img.addStyleName("buttonImage");
        DOM.insertBefore(getElement(), img.getElement(), DOM.getFirstChild(getElement()));
 
        textSpan = DOM.createElement("span");
        textSpan.setInnerText(text);
        textSpan.addClassName("buttonText");
        DOM.insertChild(getElement(), textSpan, 1);
    }

    @Override
    public void setText(String text) {
        this.text = text;
        
        textSpan.setInnerText(text);
      
    
    }

    @Override
    public String getText() {
        return this.text;
    }
}
