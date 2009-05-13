/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.screen;

import java.util.ArrayList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.AppScreenForm.State;
import org.openelis.gwt.widget.MenuLabel;

import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class ScreenInputWidget extends ScreenWidget implements FocusListener, MouseOutHandler, MouseOverHandler {
    
    protected ScreenInputWidget queryWidget;
    protected AbstractField queryField;
    protected AbstractField field;
    protected Widget displayWidget;
    protected boolean queryMode;
    protected AbsolutePanel outer = new AbsolutePanel();
    protected FocusPanel errorImg = new FocusPanel();
    protected VerticalPanel errorPanel = new VerticalPanel();
    protected PopupPanel pop;
    protected boolean showError = true;
    
    public ScreenInputWidget() {

    }
    
    public class InnerPanel extends HorizontalPanel implements HasMouseOverHandlers,HasMouseOutHandlers {

		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			// TODO Auto-generated method stub
			return addDomHandler(handler, MouseOverEvent.getType());
		}

		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			// TODO Auto-generated method stub
			return addDomHandler(handler,MouseOutEvent.getType());
		}
    	
    }
    
    public InnerPanel inner = new InnerPanel();
    
    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
            if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                screen.doTab(event, this);
                return;
            }
        }
        super.onBrowserEvent(event);
    }
    
    public ScreenInputWidget(Node node){
        super(node);
        /*
        if(node.getAttributes().getNamedItem("showError") != null){
            if(node.getAttributes().getNamedItem("showError").getNodeValue().equals("false"))
                showError = false;
        }
        */
            
    }
    
    public void load(AbstractField field){
        this.field = field;
    }
    
    public void setQueryWidget(ScreenInputWidget qWid){
        queryWidget = qWid;
        queryWidget.key = key;
    }
    
    public void setForm(State state){
        if(state == State.QUERY)
            this.queryMode = true;
        else
            this.queryMode = false;
        if(queryMode){
            if(queryWidget != null){
                initWidget(queryWidget.displayWidget);
            }
            if(queryField != null){
                field = queryField;
            }
        }else{
           if(queryWidget != null)
               initWidget(displayWidget);
        }
    }
    
    public void initWidget(final Widget widget){
        //if(showError) {
    	/*
        if(hp == null){
            hp = new HorizontalPanel();
            if(showError){
                errorImg.setStyleName("ErrorPanelHidden");
                errorImg.addMouseListener(this);
                hp.add(errorImg);
            }
        }
        */
        
        //if(hp.getWidgetCount() > 1 || (hp.getWidgetCount() == 1 && !showError)){
            //hp.remove(0);
        //}
    	outer.clear();
    	inner.clear();
        inner.add(widget);
        inner.setWidth("100%");
        outer.add(inner);
        inner.addMouseOutHandler(this);
        inner.addMouseOverHandler(this);
        //DeferredCommand.addCommand(new Command() {
        //	public void execute() {
        //		inner.setWidth(widget.getOffsetWidth()+"px");
        //	}
        //});
        //hp.setCellWidth(widget, "100%");
        setWidget(outer);
    }

    @Override
    public void setDefaults(Node node, ScreenBase screen) {
        if (node.getAttributes().getNamedItem("width") != null)
            inner.setWidth(node.getAttributes()
                                     .getNamedItem("width")
                                     .getNodeValue());
        super.setDefaults(node, screen);
    }
    
    
    public void onFocus(final Widget sender) {
        if(sender instanceof TextBoxBase){
            //((TextBoxBase)sender).setSelectionRange(0, 0);
            //DeferredCommand.addCommand(new Command(){
              //  public void execute() {
                   //((TextBoxBase)sender).setSelectionRange(0,0);
                //   ((TextBoxBase)sender).setCursorPos(0);
                   ((TextBoxBase)sender).selectAll();
                //}
            //});
        }
    }

    public void onLostFocus(Widget sender) {
        if(!showError)
            return;
        submit(field);
        field.clearErrors();
        field.validate();
        screen.validate(field);
        if(!field.isValid())
            drawError();
        else{
            errorImg.setStyleName("ErrorPanelHidden");
            if(pop != null){
                pop.hide();
            }
        }
        if(sender instanceof TextBoxBase){
          ((TextBoxBase)sender).setText(field.format());
        }
    }
    
    public void clearError() {
        if(pop != null){
            pop.hide();
        }
        inner.removeStyleName("InputError");
        errorPanel.clear();
    }
    
    public void drawError() {
        if(this instanceof ScreenTableWidget){
            //((EditTable)displayWidget).load(0);
            return;
        }
        ArrayList<String> errors;
        errors = field.getErrors();
        
        errorPanel.clear();
        for (String error : errors) {
            MenuLabel errorLabel = new MenuLabel(error,"Images/bullet_red.png");
            errorLabel.setStyleName("errorPopupLabel");
            //errorPanel.add(new MenuLabel(error,"Images/bullet_red.png"));
            errorPanel.add(errorLabel);
        }
        if(errors.size() == 0){
            inner.removeStyleName("InputError");
        }else{
            inner.setStyleName("InputError");
        }
    }
    
    public void drawBusyIcon(){
       // errorImg.setStyleName("BusyPanel");
    }
    
    public void clearBusyIcon(){
        //errorImg.setStyleName("ErrorPanelHidden");
    }


    
    public Widget getWidget() {
        //if(showError)
            return inner.getWidget(0);
        //else
           // return super.getWidget();
    }
    
    public Widget getQueryWidget() {
        return queryWidget;
    }
    
    public void submitQuery(ArrayList<AbstractField> qList) {
        if(queryField != null && queryField.getValue() != null && !"".equals(queryField.getValue())){
            qList.add(queryField);
        }
    }

	public void onMouseOut(MouseOutEvent event) {
        if(inner.getStyleName().indexOf("InputError") > -1){
            if(pop != null){
                pop.hide();
            }
        }
	}

	public void onMouseOver(MouseOverEvent event) {
        if(inner.getStyleName().indexOf("InputError") > -1){
            if(pop == null){
                pop = new PopupPanel();
                //pop.setStyleName("ErrorPopup");
            }
            DecoratorPanel dp = new DecoratorPanel();
            
            //ScreenWindow win = new ScreenWindow(pop,"","","",false);
            dp.setStyleName("ErrorWindow");
            dp.add(errorPanel);
            dp.setVisible(true);
            pop.setWidget(dp);
            pop.setPopupPosition(((Widget)event.getSource()).getAbsoluteLeft()+16, ((Widget)event.getSource()).getAbsoluteTop());
            pop.show();
        }
		
	}

}
