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

import org.openelis.gwt.resources.CollapseCSS;
import org.openelis.gwt.resources.OpenELISResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class CollapsePanel extends Composite implements ClickHandler, HasResizeHandlers {
	@UiTemplate("Collapse.ui.xml")
	interface CollapseUiBinder extends UiBinder<Widget, CollapsePanel>{};
	public static final CollapseUiBinder uiBinder = GWT.create(CollapseUiBinder.class);
    
	@UiField
    protected AbsolutePanel    				content,panel;
	@UiField
    protected FocusPanel 					arrow;
    protected boolean 						isOpen;
    protected CollapseCSS 					css;
    
    public CollapsePanel() {
    	this(false);
    }
    
    public CollapsePanel(boolean open){
    	initWidget(uiBinder.createAndBindUi(this));

    	content.setVisible(false);
    	
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				if(isAttached())
					panel.setHeight(getParent().getParent().getParent().getOffsetHeight()+"px");
			}
		});
        
        setCSS(OpenELISResources.INSTANCE.collapse());
        
        if(open)
        	open();
    }
    
    @UiChild(limit=1,tagname="content")
    public void setContent(Widget wid){
        if(content.getWidgetCount() > 0)
            content.remove(0);
        content.add(wid);
        content.setHeight("100%");
    }
    
    public void open() {
        if(!isOpen){
            content.setVisible(true);
            panel.setStyleName(css.LeftMenuPanePanelOpen());
            arrow.setStyleName(css.ArrowClose());
            arrow.setFocus(false);
            isOpen = true;
            ResizeEvent.fire(this, content.getOffsetWidth(), content.getOffsetHeight());
        }
    }
    
    public void close(){
        if(isOpen){
            content.setVisible(false);
            panel.setStyleName(css.LeftMenuPanePanelClosed());
            arrow.setStyleName(css.ArrowOpen());
            arrow.setFocus(false);
            isOpen = false;       
            ResizeEvent.fire(this, content.getOffsetWidth(), content.getOffsetHeight());
        }
    }

    public void setOpen(boolean open) {
    	if(open)
    		open();
    	else
    		close();
    }
    
    @UiHandler("arrow")
    public void onClick(ClickEvent event) {
        if(content.isVisible()){
            close();
        }else{
            open();
        }       
    }
    
    public void setCSS(CollapseCSS css) {
    	css.ensureInjected();
    	this.css = css;
    	
    	if(isOpen) {
    		panel.setStylePrimaryName(css.LeftMenuPanePanelOpen());
    		arrow.setStyleName(css.ArrowClose());
    	}else{
    		panel.setStylePrimaryName(css.LeftMenuPanePanelClosed());
    		arrow.setStyleName(css.ArrowOpen());
    	}
    	
    }
    
    @Override
    protected void onAttach() {
        boolean firstAttach = !isOrWasAttached();
        super.onAttach();
        
        panel.setHeight(getParent().getParent().getParent().getOffsetHeight()+"px");
        
        if(firstAttach) 
            content.setVisible(false);   
     
    }
    
    @Override
    public boolean isVisible() {
    	return content.isVisible();
    }
    
	public HandlerRegistration addResizeHandler(ResizeHandler handler) {
		return addHandler(handler,ResizeEvent.getType());
	}

}
