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
package org.openelis.gwt.widget.richtext;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.screen.ScreenBase;

public class RichTextWidget extends Composite implements FocusListener{
    
    private FlexTable vp = new FlexTable();
    public RichTextArea area;
    public RichTextToolbar toolbar;
    private boolean tools;
    private boolean enabled;
    
    public RichTextWidget(ScreenBase screen) {
        area = new RichTextArea();
        toolbar = new RichTextToolbar(area,screen);
    }
    
    public RichTextWidget(boolean tools) {
        init(tools);
    }
    
    public void init(boolean tools){
        this.tools = tools;
        initWidget(vp);
        vp.setCellPadding(0);
        vp.setCellSpacing(0);
        
        if(tools){
        	vp.setWidget(0,0,toolbar);
           // vp.getFlexCellFormatter().setHeight(0, 0,"75px");
            vp.getFlexCellFormatter().setVerticalAlignment(0, 0, HasAlignment.ALIGN_TOP);
            vp.setWidget(1,0,area);
            vp.getFlexCellFormatter().addStyleName(1, 0, "WhiteContentPanel");
        }else{
            vp.setWidget(0,0,area);
            vp.getFlexCellFormatter().addStyleName(0, 0, "WhiteContentPanel");
        }
        area.setSize("100%","100%");
        area.addFocusListener(this);

    }
    
    public void setText(String text){
        area.setHTML(text);
    }
    
    public String getText(){
        return area.getHTML();
    }
    
    public String getClearText() {
    	return area.getText();
    }
    
    public void addFocusListener(FocusListener listener){
        area.addFocusListener(listener);
    }
    
    public void removeFocusListener(FocusListener listener){
        area.removeFocusListener(listener);
    }
    
    public void setFocus(boolean focused) {
        if(enabled)
            area.setFocus(focused);
        else
            area.setFocus(false);
    }
    
    public boolean isEnabled(){
        return area.isEnabled();
    }
    
    public void setWidth(String width){
        vp.setWidth(width);
    }
    
    public void setHeight(String height){
        vp.setHeight(height);
    }
    
    public void enable(boolean enabled) {
        this.enabled = enabled;
        if(tools) {
            toolbar.enable(enabled);
        }
        area.setEnabled(enabled);
    }

    public void onFocus(Widget sender) {
        if(!enabled)
            area.setFocus(false);
    }

    public void onLostFocus(Widget sender) {
        // TODO Auto-generated method stub
        
    }

}
