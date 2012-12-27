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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SlideOutPanel extends Composite implements ClickHandler, MouseOverHandler, MouseOutHandler{
    
    private Widget content = new Widget();
    private VerticalPanel middle = new VerticalPanel();
    private FocusPanel arrow = new FocusPanel();
    public boolean isOpen;
    private DecoratorPanel slide;
    
    public SlideOutPanel(){
        initWidget(middle);
        middle.add(arrow);
        middle.setWidth("6px");
        middle.setCellVerticalAlignment(arrow, HasAlignment.ALIGN_MIDDLE);
        //content.setVisible(false);
        arrow.setStyleName("SlideOpen");
        arrow.addClickHandler(this);
        arrow.addMouseOutHandler(this);
        arrow.addMouseOverHandler(this);
        DeferredCommand.addCommand(new Command(){
            public void execute(){
            	int hght = middle.getParent().getParent().getOffsetHeight();
                middle.setHeight(hght+"px");
            }
         });
        
    }
    
    public void setContent(Widget wid){
        this.content = wid;
    }
    
    public void open() {
        if(!isOpen){
            //content.setVisible(true);
            arrow.setFocus(false);
            arrow.clear();
            arrow.setStyleName("SlideClose");
            isOpen = true;
            if(slide == null){
            	slide = new DecoratorPanel();
            	slide.setStyleName("SlideOut");
            	slide.add(content);
            }
            slide.setHeight(middle.getOffsetHeight()+"px");
            content.setHeight(middle.getOffsetHeight()+"px");
            //slide.setPopupPosition(getAbsoluteLeft()+middle.getOffsetWidth(), getAbsoluteTop());
            //slide.show();
            RootPanel.get().add(slide, getAbsoluteLeft()+middle.getOffsetWidth()-2, getAbsoluteTop());
           
        }
    }
    
    public void close(){
        if(isOpen){
            //content.setVisible(false);
            arrow.setFocus(false);
            arrow.clear();
            arrow.setStyleName("SlideOpen");
            isOpen = false;
            RootPanel.get().remove(slide);
            //slide.hide();
        }
    }

    public void onClick(ClickEvent sender) {
        if(isOpen){
            close();
        }else{
            open();
        }   
        
    }

    public void onMouseOver(MouseOverEvent event) {
        arrow.addStyleName("Hover");
        
    }

    public void onMouseOut(MouseOutEvent event) {
        arrow.removeStyleName("Hover");
    }

}
