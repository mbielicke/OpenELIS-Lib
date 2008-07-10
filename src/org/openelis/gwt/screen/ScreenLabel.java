/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
/**
 * ScreenLabel wraps a GWT Label for display on a Screen.
 * @author tschmidt
 *
 */
public class ScreenLabel extends ScreenWidget implements SourcesClickEvents{

    private DelegatingClickListenerCollection clickListeners;
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "label";
	/**
	 * Widget wrapped by this class
	 */
    public Label label;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenLabel() {
    }
    
    public ScreenLabel(ScreenBase screen, String key){
    	label = new Label();
    	initWidget(label);
    	screen.widgets.put(key, this);
    }

    public ScreenLabel(String text, Object value) {
        label = new Label(text);
        setUserObject(value);
        initWidget(label);
        label.setStyleName("ScreenLabel");
        
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;label key="string" wordwrap="boolean" text="string" onclick="string"/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenLabel(Node node, ScreenBase screen) {
        super(node);
        label = new Label();
        if (node.getAttributes().getNamedItem("text") != null){
            label.setText(node.getAttributes().getNamedItem("text").getNodeValue());
        } 
        if (node.getAttributes().getNamedItem("wordwrap") != null)
            label.setWordWrap(Boolean.valueOf(node.getAttributes()
                                                  .getNamedItem("wordwrap")
                                                  .getNodeValue())
                                     .booleanValue());
        else
            label.setWordWrap(false);
        if(node.getAttributes().getNamedItem("onClick") != null){
            String[] listeners = node.getAttributes().getNamedItem("onClick").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this"))
                    addClickListener((ClickListener)screen);
                else
                    addClickListener((ClickListener)ClassFactory.forName(listeners[i]));
            }
        }
        if (node.hasChildNodes())
            label.setText((node.getFirstChild().getNodeValue()));
        initWidget(label);
        label.setStyleName("ScreenLabel");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenLabel(node, screen);
    }

    public void load(AbstractField field) {
        label.setText(field.toString());
    }
    
    
    public ScreenLabel getDropInstance(){
        ScreenLabel slabel = new ScreenLabel();
        slabel.setDropTargets(getDropTargets());
        slabel.label = new Label(label.getText());
        slabel.label.setWordWrap(label.getWordWrap());
        slabel.initWidget(slabel.label);
        slabel.setUserObject(getUserObject());
        return slabel;
    }

    public void addClickListener(ClickListener arg0) {
       if(clickListeners == null)
           clickListeners = new DelegatingClickListenerCollection(this,label);
       if(clickListeners.contains(arg0))
           return;
       clickListeners.add(arg0);
        
    }

    public void removeClickListener(ClickListener arg0) {
        if(clickListeners != null)
            clickListeners.remove(arg0);
    }
    
    public void destroy(){
        label = null;
        clickListeners = null;
        super.destroy();
    }
  
}
