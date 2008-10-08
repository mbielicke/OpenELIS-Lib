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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.MenuLabel;

import java.util.ArrayList;

public class ScreenInputWidget extends ScreenWidget implements FocusListener, MouseListener {
    
    protected ScreenInputWidget queryWidget;
    protected AbstractField field;
    protected Widget displayWidget;
    protected boolean queryMode;
    protected HorizontalPanel hp;
    protected FocusPanel errorImg = new FocusPanel();
    protected VerticalPanel errorPanel = new VerticalPanel();
    protected PopupPanel pop;
    protected boolean showError = true;
    
    public ScreenInputWidget() {

    }
    
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
        if(node.getAttributes().getNamedItem("showError") != null){
            if(node.getAttributes().getNamedItem("showError").getNodeValue().equals("false"))
                showError = false;
        }
            
    }
    
    public void load(AbstractField field){
        this.field = field;
    }
    
    public void setQueryWidget(ScreenInputWidget qWid){
        queryWidget = qWid;
        queryWidget.key = key;
    }
    
    public void setForm(boolean mode){
        this.queryMode = mode;
        if(queryMode){
            if(queryWidget != null){
                initWidget(queryWidget.displayWidget);
            }else{
                queryMode = false;
            }
        }else{
           initWidget(displayWidget);
        }
    }
    
    public void initWidget(Widget widget){
        if(hp == null){
            hp = new HorizontalPanel();
            if(showError){
                errorImg.setStyleName("ErrorPanelHidden");
                errorImg.addMouseListener(this);
                hp.add(errorImg);
            }
        }
        if(hp.getWidgetCount() > 1 || (hp.getWidgetCount() == 1 && !showError)){
            hp.remove(0);
        }
        hp.insert(widget,0);
        setWidget(hp);
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
        /*
        String tempKey = key;
        submit(screen.rpc.getField(tempKey));
        screen.rpc.getField(tempKey).clearErrors();
        screen.rpc.getField(tempKey).validate();
        if(!screen.rpc.getField(tempKey).isValid())
            drawError();
        else{
            errorImg.setStyleName("ErrorPanelHidden");
            if(pop != null){
                pop.hide();
            }
        }
        */
        submit(field);
        field.clearErrors();
        field.validate();
        if(!field.isValid())
            drawError();
        else{
            errorImg.setStyleName("ErrorPanelHidden");
            if(pop != null){
                pop.hide();
            }
        }
        if(sender instanceof TextBoxBase){
          //  ((TextBoxBase)sender).setSelectionRange(0, 0);
        }
    }
    
    public void clearError() {
        if(!showError)
            return;
        if(pop != null){
            pop.hide();
        }
        errorImg.setStyleName("ErrorPanelHidden");
        errorPanel.clear();
    }
    
    public void drawError() {
        if(!showError)
            return;
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
            errorImg.setStyleName("ErrorPanelHidden");
        }else{
            errorImg.setStyleName("ErrorPanel");
        }
    }
    
    public void drawBusyIcon(){
        errorImg.setStyleName("BusyPanel");
    }
    
    public void clearBusyIcon(){
        errorImg.setStyleName("ErrorPanelHidden");
    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        // TODO Auto-generated method stub
        if(errorImg.getStyleName().equals("ErrorPanel")){
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
            pop.setPopupPosition(sender.getAbsoluteLeft()+16, sender.getAbsoluteTop());
            pop.show();
        }
        
    }

    public void onMouseLeave(Widget sender) {
        // TODO Auto-generated method stub
        if(errorImg.getStyleName().equals("ErrorPanel")){
            if(pop != null){
                pop.hide();
            }
        }
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }
    
    public Widget getWidget() {
        return hp.getWidget(0);
    }
    
    public Widget getQueryWidget() {
        return queryWidget;
    }

}
