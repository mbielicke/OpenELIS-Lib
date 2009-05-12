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
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DropDownField;
import org.openelis.gwt.common.data.StringField;
import org.openelis.gwt.common.data.TableDataModel;
import org.openelis.gwt.common.data.TableDataRow;
import org.openelis.gwt.screen.ClassFactory;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.ScreenDropDownWidget;
import org.openelis.gwt.screen.AppScreenForm.State;
import org.openelis.gwt.widget.Dropdown;

import java.util.ArrayList;

public class TableDropdown extends TableCellInputWidget implements ChangeListener, SourcesChangeEvents {

	public Dropdown editor;
	private Label display;
	
	//this should be the id of the selected element
	private StringField textValue = new StringField();
    private ChangeListenerCollection listeners;
	
    public boolean loadFromModel;
    public String loadFromHidden = null;
    private boolean multi;
    private int visible = 1;
    private String type = "";
    private int width;
    public static final String TAG_NAME = "table-dropdown";
    
    
    public TableDropdown(){
    	sinkEvents(Event.KEYEVENTS);
    }
    
    public TableCellWidget getNewInstance() {
    	TableDropdown ta = new TableDropdown();
        ta.multi = multi;
        ta.visible = visible;
        ta.loadFromHidden = loadFromHidden;
        ta.loadFromModel = loadFromModel;
        ta.editor = editor;
        ta.listeners = listeners;
        ta.type = type;
        ta.screen = screen;
        return ta;
    }
    
    public TableDropdown getNewTableAuto() {
    	TableDropdown ta = new TableDropdown();
        ta.multi = multi;
        ta.visible = visible;
        ta.loadFromHidden = loadFromHidden;
        ta.loadFromModel = loadFromModel;
        ta.editor = editor;
        ta.listeners = listeners;
        ta.type = type;
        ta.screen = screen;
        return ta;
    }
    
	public TableDropdown(Node node, ScreenBase screen) {
        ScreenDropDownWidget sDrop = new ScreenDropDownWidget(node,screen);
        this.screen = screen;
        editor = (Dropdown)sDrop.getWidget();
        if (node.getAttributes().getNamedItem("onchange") != null){
            String[] listeners = node.getAttributes().getNamedItem("onchange").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this")){
                    addChangeListener((ChangeListener)screen);
                }else{
                    addChangeListener((ChangeListener)ClassFactory.forName(listeners[i]));
                }
            }
            editor.changeListeners = null;
            editor.addChangeListener(this);
        }
        //editor.lookUp.setIconHeight("18px");
        editor.setStyleName("TableDropdown");
        textValue.setValue("");
	}

	public void saveValue() {
		//editor.complete();
        if(field instanceof DropDownField)
            field.setValue(editor.getSelections());
        else{
            field.setValue(editor.getSelections().get(0).key);
        }
		editor.hideTable();
        super.saveValue();
	}

	//the editor is shared so we need to set the textvalue
	public void setDisplay() {
		if(display == null){
			display = new Label();
			display.setWordWrap(false);
		}
        if(field instanceof DropDownField){
            if(((DropDownField)field).getModel() != null && ((DropDownField)field).getModel().size() > 0){
                editor.setModel(((DropDownField)field).getModel());
            }
            editor.setSelections(((DropDownField<Object>)field).getKeyValues());
            
        }else{
            ArrayList selected = new ArrayList();
            selected.add(field);
            editor.setSelections(selected);
        }
        display.setText(editor.lookUp.getText());
		setWidget(display);		
        super.setDisplay();
	}

	public void setEditor() {
        if(field instanceof DropDownField){
            if(((DropDownField)field).getModel() != null && ((DropDownField)field).getModel().size() > 0){
                editor.setModel(((DropDownField)field).getModel());
            }
            editor.activeCell = -1;
            editor.activeRow = -1;
            editor.setSelections(((DropDownField<Object>)field).getKeyValues());
        }else{
            ArrayList selected = new ArrayList();
            selected.add(field);
            editor.setSelections(selected);
        }
        editor.setWidth((width-18)+"px");
		setWidget(editor);			
	}

	public void setField(AbstractField field) {
		this.field = field;
    }
	
	public void enable(boolean enabled) {
	    editor.enabled(enabled);
    }
	
    public void setCellWidth(int width){
        this.width = width;
        if(editor != null){
            editor.setWidth(width+"px");
        }
        if(display != null)
            display.setWidth(width+"px");
    }
    
    public <T extends TableDataRow> void setModel(TableDataModel<T> model){
        editor.setModel(model);
    }
    
    public void setFocus(boolean focused) {
        editor.setFocus(focused);
    }

    public void addChangeListener(ChangeListener listener) {
        if(listeners == null)
            listeners = new ChangeListenerCollection();
        listeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        if(listeners != null)
            listeners.remove(listener);
        
    }

    public void onChange(Widget sender) {
        listeners.fireChange(this);
        
    }
    
    public void setForm(State state) {
        if(state == State.QUERY)
            editor.setMultiSelect(true);
        else
            editor.setMultiSelect(multi);
    }
    
    
}

