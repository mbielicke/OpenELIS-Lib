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
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RichTextWidget extends Composite {
    
    private VerticalPanel vp = new VerticalPanel();
    private RichTextArea area = new RichTextArea();
    private RichTextToolbar toolbar = new RichTextToolbar(area);
    
    public RichTextWidget(boolean tools) {
        initWidget(vp);
        if(tools)
        	vp.add(toolbar);
        vp.add(area);
        area.setSize("100%","100%");

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
        area.setFocus(focused);
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

}
