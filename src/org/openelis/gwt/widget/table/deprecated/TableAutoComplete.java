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
package org.openelis.gwt.widget.table.deprecated;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.deprecated.AbstractField;
import org.openelis.gwt.common.data.deprecated.DropDownField;
import org.openelis.gwt.common.data.deprecated.Field;
import org.openelis.gwt.common.data.deprecated.QueryStringField;
import org.openelis.gwt.common.data.deprecated.StringField;
import org.openelis.gwt.common.data.deprecated.TableDataModel;
import org.openelis.gwt.common.data.deprecated.TableDataRow;
import org.openelis.gwt.screen.deprecated.ScreenAutoCompleteWidget;
import org.openelis.gwt.screen.deprecated.ScreenBase;
import org.openelis.gwt.screen.deprecated.AppScreenForm.State;
import org.openelis.gwt.widget.deprecated.AutoComplete;
@Deprecated
public class TableAutoComplete extends TableCellInputWidget implements ChangeListener {

	public AutoComplete editor;
	private Label display;
	
	//this should be the id of the selected element
	private StringField textValue = new StringField();
    private ChangeListener listener;
	
    public boolean loadFromModel;
    public String loadFromHidden = null;
    private boolean multi;
    private int visible = 1;
    private String type = "";
    private int width;
    public static final String TAG_NAME = "table-autoComplete";
    
    
    public TableAutoComplete(){
    	sinkEvents(Event.KEYEVENTS);
    }
    
    public TableCellWidget getNewInstance() {
    	TableAutoComplete ta = new TableAutoComplete();
        ta.multi = multi;
        ta.visible = visible;
        ta.loadFromHidden = loadFromHidden;
        ta.loadFromModel = loadFromModel;
        ta.editor = editor;
        ta.listener = listener;
        ta.type = type;
        ta.screen = screen;
        return ta;
    }
    
    public TableAutoComplete getNewTableAuto() {
    	TableAutoComplete ta = new TableAutoComplete();
        ta.multi = multi;
        ta.visible = visible;
        ta.loadFromHidden = loadFromHidden;
        ta.loadFromModel = loadFromModel;
        ta.editor = editor;
        ta.listener = listener;
        ta.type = type;
        ta.screen = screen;
        return ta;
    }
    
	public TableAutoComplete(Node node, ScreenBase screen) {
        ScreenAutoCompleteWidget sAuto = new ScreenAutoCompleteWidget(node,screen);
        this.screen = screen;
        editor = (AutoComplete)sAuto.getWidget();
        editor.setStyleName("TableDropdown");
        textValue.setValue("");
	}

	public void saveValue() {
	    if(editor.queryMode) {
	        field.setValue(editor.lookUp.getText());
	    }else{
	        if(editor.model.getData().size() == 0)
	            return;
	       
	        field.setValue(editor.getSelections());
	        ((DropDownField)field).setModel(editor.model.getData());
	        editor.hideTable();
	    }
        super.saveValue();
	}

	//the editor is shared so we need to set the textvalue
	public void setDisplay() {
		if(display == null){
			display = new Label();
			display.setWordWrap(false);
		}
		if(editor.queryMode){
		    display.setText((String)field.getValue());
		}else{
		    if(((DropDownField)field).getModel() != null){
		    //    editor.model.load(((DropDownField)field).getModel());
		    //    editor.setSelections(((DropDownField<Object>)field).getKeyValues());
		        TableDataModel model = ((DropDownField)field).getModel();
		        for(int i = 0; i < model.list.size(); i++) {
		            if(((TableDataRow)model.list.get(i)).key.equals(((DropDownField<Object>)field).getSelectedKey())) {
		                display.setText(((TableDataRow)model.list.get(i)).cells[0].getValue().toString());
		                break;
		            }
		        }
		    }else
		        //editor.model.load(new TableDataModel());
		        display.setText("");
		}
		setWidget(display);
        super.setDisplay();
	}

	public void setEditor() {
	    if(editor.queryMode) {
	        editor.lookUp.setText((String)field.getValue());
	    }else{
	        if(((DropDownField)field).getModel() != null){
	            editor.model.load(((DropDownField)field).getModel());
	            editor.setSelections(((DropDownField<Object>)field).getKeyValues());
	        }else{
	            editor.model.load((new TableDataModel()));
	            editor.setSelections(((DropDownField<Object>)field).getKeyValues());
	        }
	        editor.activeCell = -1;
	        editor.activeRow = -1;
	    }
        editor.setWidth(width+"px");
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

    public void onChange(Widget sender) {
        
    }
    
    public void setFocus(boolean focused) {
        editor.setFocus(focused);
    }
    
    public void setForm(State state) {
        if(state == State.QUERY){
            editor.queryMode = true;
        }else
            editor.queryMode = false;
        super.setForm(state);
    }
}
