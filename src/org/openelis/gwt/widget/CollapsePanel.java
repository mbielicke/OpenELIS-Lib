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
package org.openelis.gwt.widget;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CollapsePanel extends Composite implements ClickHandler, HasResizeHandlers {
    
    private Grid panel = new Grid(1,2);
    private HorizontalPanel content = new HorizontalPanel();
    private FocusPanel arrow = new FocusPanel();
    public boolean isOpen;
    
    public CollapsePanel(boolean open){
        
        initWidget(panel);
        panel.setCellPadding(0);
        panel.setCellSpacing(0);
      
        panel.setWidth("100%");
        panel.getCellFormatter().setStyleName(0,1,"LeftMenuPanePanelClosed");
        arrow.setStyleName("LeftMenuPanePanelDiv");
        arrow.addClickHandler(this);
        arrow.addMouseOverHandler(new MouseOverHandler() {
        	public void onMouseOver(MouseOverEvent event) {
        		arrow.addStyleName("Hover");
                panel.getCellFormatter().addStyleName(0,1,"Hover");
        	}
        });
        arrow.addMouseOutHandler(new MouseOutHandler() {
        	public void onMouseOut(MouseOutEvent event) {
                arrow.removeStyleName("Hover");
                panel.getCellFormatter().removeStyleName(0,1,"Hover");
        	}
        });
        panel.setWidget(0, 0, content);
        panel.setWidget(0,1,arrow);
        panel.getCellFormatter().setVerticalAlignment(0,0,HasAlignment.ALIGN_TOP);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				panel.setHeight(panel.getParent().getParent().getParent().getOffsetHeight()+"px");
			}
		});
        
        if(open)
        	open();
    }
    
    public void setContent(Widget wid){
        if(content.getWidgetCount() > 0)
            content.remove(0);
        content.add(wid);
        content.setCellVerticalAlignment(wid,HasAlignment.ALIGN_TOP);
        content.setHeight("100%");
    }
    
    public void open() {
        if(!isOpen){
            content.setVisible(true);
            panel.getCellFormatter().setStyleName(0,1,"LeftMenuPanePanelOpen");
            arrow.setFocus(false);
            isOpen = true;
            ResizeEvent.fire(this, content.getOffsetWidth(), content.getOffsetHeight());
        }
    }
    
    public void close(){
        if(isOpen){
            content.setVisible(false);
            panel.getCellFormatter().setStyleName(0,1,"LeftMenuPanePanelClosed");
            arrow.setFocus(false);
            isOpen = false;       
            ResizeEvent.fire(this, content.getOffsetWidth(), content.getOffsetHeight());
        }
    }

    public void onClick(ClickEvent event) {
        if(content.isVisible()){
            close();
        }else{
            open();
        }       
    }
    
    @Override
    protected void onAttach() {
        boolean firstAttach = !isOrWasAttached();
        super.onAttach();
        
        if(firstAttach) 
            content.setVisible(false);   
        
     
    }
    
	public HandlerRegistration addResizeHandler(ResizeHandler handler) {
		return addHandler(handler,ResizeEvent.getType());
	}
    

}
