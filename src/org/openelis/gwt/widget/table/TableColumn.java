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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Warning;
import org.openelis.gwt.widget.AutoComplete;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.HasExceptions;
import org.openelis.gwt.widget.HasHelper;
import org.openelis.gwt.widget.IconContainer;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.QueryFieldUtil;
import org.openelis.gwt.widget.ScreenWidgetInt;
import org.openelis.gwt.widget.calendar.Calendar;

import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

public class TableColumn {

    public String header;
    public boolean sortable;
    public boolean filterable;
    public boolean queryable;
    public Widget colWidget;
    public int preferredWidth;
    public int currentWidth;
    public int minWidth = 15;
    public boolean fixedWidth;
    public HasHorizontalAlignment.HorizontalAlignmentConstant alignment = HasHorizontalAlignment.ALIGN_LEFT;
    public TableWidget controller;
    public int columnIndex;
    public String key;
    public boolean filterDisplayed = false;
    public QueryFieldUtil query;
    protected PopupPanel pop;
    public boolean filtered;
    protected HashSet<Object> filtersInForce;
    protected ArrayList<Filter> filterList;
    protected boolean enabled;
    protected VerticalPanel errorPanel;
    protected HandlerRegistration mouseOver;
    protected HandlerRegistration mouseOut;
    
    public class CheckBoxContainer extends AbsolutePanel implements HasMouseOutHandlers, HasMouseOverHandlers {

		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			return addDomHandler(handler, MouseOutEvent.getType());
		}

		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return addDomHandler(handler, MouseOverEvent.getType());
		}
    	
    }
    
    
    public Widget getDisplayWidget(TableDataRow row) {
    	TableDataCell cell = new TableDataCell(null);
    	if( columnIndex < row.cells.size())
    		cell = row.cells.get(columnIndex);
    	Widget wid = null;
    	Object val = null;
    	if(colWidget instanceof CheckBox){
    		wid = new CheckBoxContainer();
    		IconContainer icon = new IconContainer();
    		if(CheckBox.Value.CHECKED.getValue().equals(cell.getValue()))
    			icon.setStyleName(CheckBox.Value.CHECKED.getStyle());
    		else if(controller.queryMode && cell.getValue() == null)
    			icon.setStyleName(CheckBox.Value.UNKNOWN.getStyle());
    		else
    			icon.setStyleName(CheckBox.Value.UNCHECKED.getStyle());
    		setAlign(HasHorizontalAlignment.ALIGN_CENTER);
    		((AbsolutePanel)wid).add(icon);
    		DOM.setStyleAttribute(wid.getElement(), "align", "center");
    		wid.setWidth((currentWidth)+ "px");
    	}else{
    		if(colWidget instanceof AutoComplete) {
    			if(controller.queryMode){
    				val = cell.getValue();
    				if(val == null)
    					val = "";
    			}else{
    			    Item vrow = (Item)cell.getValue();
    				if(vrow  != null)
    					((AutoComplete)colWidget).setValue(vrow.getKey(),vrow.display);
    				else
    					((AutoComplete)colWidget).setValue(null,"");
    				val = ((AutoComplete)colWidget).getDisplay();
    			}
    		}else if(colWidget instanceof Dropdown){
    			if(cell.getValue() instanceof ArrayList)
    				((Dropdown)colWidget).setValues((ArrayList<Object>)cell.getValue());
    			else
    				((Dropdown)colWidget).setValue(cell.getValue());
    			val = ((Dropdown)colWidget).getDisplay();
    		}else{
    			((HasValue)colWidget).setValue(cell.getValue());
    		}
    		if(val == null){
    		    /*
    			if(((ScreenWidgetInt)colWidget).queryMode
    				val = ((HasField)colWidget).getField().queryString;
    			else
    			*/
    			val = ((HasValue)colWidget).getValue();
    		}
    		Label label = new Label("");
    		label.setStyleName("ScreenLabel");
    		if(val != null) {
    			if(colWidget instanceof Calendar) {
    			    /*
    				if(((HasField)colWidget).getField().queryMode)
    					label.setText(((HasField)colWidget).getField().queryString);
    				else
    				*/
    				label.setText((((Calendar)colWidget).getHelper().format((Datetime)val)));
    			}else if(colWidget instanceof AutoComplete && controller.queryMode) {
    				label.setText((String)val);
    			}else if(colWidget instanceof Dropdown) {
    				label.setText(((Dropdown)colWidget).getDisplay());
    			}else if(colWidget instanceof TextBoxBase) {
    			    /*
    				if(((HasField)colWidget).getField().queryMode)
    					label.setText(((HasField)colWidget).getField().queryString);
    				else
    				*/
    			    label.setText(((TextBoxBase)colWidget).getText());
    			}else if(colWidget instanceof Label)
    				label.setText(((Label)colWidget).getText());
    		}
    		label.setWordWrap(false);
    		wid = label;
    		wid.setWidth((currentWidth)+ "px");
    	}
        setExceptions(wid,cell.exceptions);
        wid.addStyleName("TableWidget");
        return wid;
    }
    
    public void loadWidget(Widget widget, TableDataRow row, int modelIndex) {
    	TableDataCell cell = new TableDataCell(null);
    	if( columnIndex < row.cells.size())
    		cell = row.cells.get(columnIndex);
    	if(colWidget instanceof CheckBox){
    		if(CheckBox.Value.CHECKED.getValue().equals(cell.getValue()))
    			((AbsolutePanel)widget).getWidget(0).setStyleName(CheckBox.Value.CHECKED.getStyle());
    	    else if(controller.queryMode && cell.getValue() == null)
    	    	((AbsolutePanel)widget).getWidget(0).setStyleName(CheckBox.Value.UNKNOWN.getStyle());
    	    else
    	    	((AbsolutePanel)widget).getWidget(0).setStyleName(CheckBox.Value.UNCHECKED.getStyle());
    	}else if(widget instanceof Label) {
    		if(colWidget instanceof AutoComplete) {
    			if(controller.queryMode){
    				((Label)widget).setText((String)cell.getValue());
    			}else{
    				Item vrow = (Item)cell.getValue();
    				if(vrow != null)
    					((AutoComplete)colWidget).setValue(vrow.getKey(), vrow.display);
    				else
    					((AutoComplete)colWidget).setValue(null,"");
    			
    				((Label)widget).setText(((AutoComplete)colWidget).getDisplay());
    			}
        	}else if(colWidget instanceof Dropdown){
        		if(cell.getValue() instanceof ArrayList)
    				((Dropdown)colWidget).setValues((ArrayList<Object>)cell.getValue());
    			else
    				((Dropdown)colWidget).setValue(cell.getValue());
        		((Label)widget).setText(((Dropdown)colWidget).getDisplay());
    		}else{
    			((HasValue)colWidget).setValue(cell.getValue());
    			if(colWidget instanceof Calendar) {
    			    /*
    				if(((HasField)colWidget).getField().queryMode)
    					((Label)widget).setText(((HasField)colWidget).getField().queryString);
    				else
    				*/
    			    ((Label)widget).setText(((Calendar)colWidget).getText());
    			}else if(colWidget instanceof Dropdown) {
    				((Label)widget).setText(((Dropdown)colWidget).getDisplay());
    			}else if(colWidget instanceof TextBoxBase) {
    			    /*
    				if(((HasField)colWidget).getField().queryMode)
    					((Label)widget).setText(((HasField)colWidget).getField().queryString);
    				else
    				*/
    				((Label)widget).setText(((TextBoxBase)colWidget).getText());
    			}else{
    				if(((HasValue)colWidget).getValue() != null)
    					((Label)widget).setText(((HasHelper)colWidget).getHelper().format(cell.getValue()));
    				else
    					((Label)widget).setText("");
    			}
    		}
    	}
    	setExceptions(widget,cell.exceptions);
    }
    
    public Widget getWidgetEditor(TableDataRow row) {
    	TableDataCell cell = new TableDataCell(null);
    	if( columnIndex < row.cells.size())
    		cell = row.cells.get(columnIndex);
    	Widget editor = colWidget;
    	if(colWidget instanceof CheckBox){
    		CheckBoxContainer ap = new CheckBoxContainer();
    		((CheckBox)editor).setValue((String)cell.getValue());
    		ap.setWidth((currentWidth)+ "px");
    		ap.add(editor);
    		return ap;
    	}
    	editor = colWidget;
    	editor.setWidth((currentWidth)+ "px");
    	if(colWidget instanceof AutoComplete){
    		if(controller.queryMode){
    			//((AutoComplete)colWidget).textbox.setText((String)cell.getValue());
    		}else{
    			Item vrow =  (Item)cell.getValue();
    			if(vrow != null)
    				((AutoComplete)colWidget).setValue(vrow.getKey(),vrow.display);
    			else
    				((AutoComplete)colWidget).setValue(null,"");
    		}
    	}else if(colWidget instanceof Dropdown){
    		if(cell.getValue() instanceof ArrayList)
				((Dropdown)colWidget).setValues((ArrayList<Object>)cell.getValue());
			else
				((Dropdown)colWidget).setValue(cell.getValue());
    	}else
    		((HasValue)editor).setValue(cell.getValue());
    	
    	((HasExceptions)editor).clearExceptions();
   		setExceptions(editor, cell.exceptions);
    	editor.addStyleName("TableWidget");
    	return editor;
    }

    public void enable(boolean enable) {
    	if(colWidget instanceof ScreenWidgetInt){
    		((ScreenWidgetInt)colWidget).setEnabled(enable);
    	}
    	this.enabled = enable;
    }
    
    public boolean isEnabled() {
    	return enabled;
    }
    
    public void setExceptions(final Widget wid, ArrayList<LocalizedException> exceptions) {
		//Clean up previous MouseHandlers
    	if(mouseOver != null){
    		try {
    			mouseOver.removeHandler();
    		}catch(AssertionError e) {
    			mouseOver = null;
    		}
    	}
    	if(mouseOut != null){
    		try {
    			mouseOut.removeHandler();
    		}catch(AssertionError e){
    			mouseOut = null;
    		}
    	}
    	if(exceptions == null || exceptions.size() == 0){
        	wid.removeStyleName("InputError");
        	wid.removeStyleName("InputWarning");
    		return;
    	}
    	errorPanel = new VerticalPanel();
		String style = "InputWarning";
		if(wid instanceof HasExceptions){
			((HasExceptions)wid).clearExceptions();
			//((HasField)wid).getField().drawErrors = false;
		}
		for (LocalizedException error : exceptions) {
			HorizontalPanel hp = new HorizontalPanel();
			if(error instanceof Warning){
				AbsolutePanel ap = new AbsolutePanel();
				ap.setStyleName("warnIcon");
				hp.add(ap);
				hp.setStyleName("warnPopupLabel");
			}else{
				AbsolutePanel ap = new AbsolutePanel();
				ap.setStyleName("errorIcon");
				hp.add(ap);
				hp.setStyleName("errorPopupLabel");
				style = "InputError";
			}
			hp.add(new Label(error.getMessage()));
			errorPanel.add(hp);
			if(wid instanceof HasExceptions)
				((HasExceptions)wid).addException(error);
		}
		if(wid instanceof HasExceptions)
			((HasExceptions)wid).addExceptionStyle(style);
		else
			wid.addStyleName(style);
    	
		mouseOver = ((HasMouseOverHandlers)wid).addMouseOverHandler(new MouseOverHandler() {

			public void onMouseOver(MouseOverEvent event) {

				String styleName = ((Widget)event.getSource()).getStyleName();
				
				//if(styleName.indexOf("InputError") > -1 || styleName.indexOf("InputWarning") > -1){
					if(pop == null){
						pop = new PopupPanel(true);
						//pop.setStyleName("ErrorPopup");
					}
					DecoratorPanel dp = new DecoratorPanel();

					//ScreenWindow win = new ScreenWindow(pop,"","","",false);
					dp.setStyleName("ErrorWindow");
					dp.add(errorPanel);
					dp.setVisible(true);
					pop.setWidget(dp);
					int left = ((Widget)event.getSource()).getAbsoluteLeft()+((Widget)event.getSource()).getOffsetWidth();
					if(left > controller.view.cellView.getAbsoluteLeft()+controller.view.cellView.getOffsetWidth())
						left = controller.view.cellView.getAbsoluteLeft()+controller.view.cellView.getOffsetWidth();
					pop.setPopupPosition(left, ((Widget)event.getSource()).getAbsoluteTop());
					pop.show();
					Timer timer = new Timer() {
						public void run() {
							pop.hide();
						}
					};
					timer.schedule(5000);
				}
		//	}

		});
		
		mouseOut = ((HasMouseOutHandlers)wid).addMouseOutHandler(new MouseOutHandler() {

			public void onMouseOut(MouseOutEvent event) {
				String styleName = ((Widget)event.getSource()).getStyleName();
				//if(styleName.indexOf("InputError") > -1 || styleName.indexOf("InputWarning") > -1){
					if(pop != null){
						pop.hide();
					}
				//}
			}

		});

    }
    
    public HorizontalAlignmentConstant getAlign() {
        return alignment;
    }

    public Widget getColumnWidget() {
        return colWidget;
    }

    public int getCurrentWidth() {
        return currentWidth;
    }

    public boolean getFilterable() {
        return filterable;
    }

    public boolean getFixedWidth() {
        return fixedWidth;
    }

    public String getHeader() {
        return header;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }

    public boolean getSortable() {
        return sortable;
    }

    public void setAlign(HorizontalAlignmentConstant align) {
        alignment = align;
    }

    public void setColumnWidget(Widget widget) {
        colWidget = widget;
    }

    public void setCurrentWidth(int width) {
        currentWidth = width;
    }

    public void setFilterable(boolean filterable) {
        this.filterable = filterable;
    }

    public void setFixedWidth(boolean fixed) {
        fixedWidth = fixed;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setMinWidth(int width) {
        minWidth = width;
    }

    public void setPreferredWidth(int width) {
        preferredWidth = width;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }
    
    public void setTableWidget(TableWidget controller){
        this.controller = controller;
    }
    
    public TableWidget getTableWidget() {
        return controller;
    }
    
    public ArrayList<Filter> getFilters() {
    	Filter all = new Filter(null,new Label("All"),true);
    	if(filtered)
    		all.filtered = false;
    	filterList = new ArrayList<Filter>();
    	filterList.add(all);
    	int col = controller.columns.indexOf(this);
    	ArrayList<Object> checkList = new ArrayList<Object>();
    	checkList.add(null);
    	for(TableDataRow row : controller.getData()){
    		if(checkList.contains(row.cells.get(col).getValue()))
    			continue;
    		Filter filter = new Filter(row.cells.get(col).getValue(),getDisplayWidget(row),false);
    		if(filtered){
    			if(filtersInForce.contains(row.cells.get(col).getValue())){
    				filter.filtered = true;
    			}
    		}
    		filterList.add(filter);
    		checkList.add(row.cells.get(col).getValue());
    	}
    	return filterList;
    }
    
    public ArrayList<Filter> getFilterList() {
    	return filterList; 
    }
    
    public void setFilter(ArrayList<Filter> filters) {
    	filtersInForce = new HashSet<Object>();
    	if(filters.get(0).filtered){
    		filtered = false;
    		return;
    	}
    	filtered = false;
    	for(Filter filt : filters) {
    		if(filt.filtered){
    			filtered = true;
    			filtersInForce.add(filt.obj);
    		}
    	}
    }
    
    public void applyFilter() {
    	if(!filtered)
    		return;
    	int col = controller.columns.indexOf(this);
    	for(TableDataRow row : controller.getData()) {
    		if(!row.shown)
    			continue;
    		if(!filtersInForce.contains(row.cells.get(col).getValue()))
    			row.shown = false;
    	}
    }
    
    public void applyQueryFilter() {
    	int col = controller.columns.indexOf(this);
        for(TableDataRow row : controller.getData()) {
        	if(!row.shown)
        		continue;
        	Object cellValue = row.cells.get(col).getValue();
        	if(cellValue == null) {
        		row.shown = false;
        		continue;
        	}
        	for(int i = 0; i < query.parameter.size(); i++) {
        		String params[] = query.parameter.get(i).split("\\.\\.");
        		Object[] parameter = new Object[params.length];
        		Object comparator = query.getComparator().get(i);
        		if(cellValue instanceof Double) {
        			parameter[0] = new Double((String)params[0]);
        			if(parameter.length > 1)
        				parameter[1] = new Double((String)params[1]);
        		}else if(cellValue instanceof Integer) {
        			parameter[0] = new Integer((String)params[0]);
        			if(parameter.length > 1)
        				parameter[1] = new Integer((String)params[1]);
        		}else if(cellValue instanceof Long){
        			parameter[0] = new Long((String)params[0]);
        			if(parameter.length > 1)
        				parameter[1] = new Long((String)params[1]);
        		}else if(cellValue instanceof Datetime) {
        			parameter[0] = Datetime.getInstance(((Datetime)cellValue).startCode,((Datetime)cellValue).endCode,new Date(((String)params[0]).replaceAll("-","/")));
        			if(parameter.length > 1)
        				parameter[1] = Datetime.getInstance(((Datetime)cellValue).startCode,((Datetime)cellValue).endCode,new Date(((String)params[1]).replaceAll("-","/")));
        		}else{
        			parameter[0] = params[0];
        			if(parameter.length > 1)
        				parameter[1] = params[1];
        		}
        		if(comparator.equals("=")) {
        			if(!cellValue.equals(parameter[0])){
        				row.shown = false;
        			}else {
        				row.shown = true;
        				break;
        			}
        		}else if(comparator.equals(">")) {
        			if(((Comparable)cellValue).compareTo(parameter[0]) <= 0){
        				row.shown = false;
        			}else {
        				row.shown = true;
        				break;
        			}
        		}else if(comparator.equals("<")) {
        			if(((Comparable)cellValue).compareTo(parameter[0]) >= 0) {
        				row.shown = false;
        			}else {
        				row.shown = true;
        				break;
        			}
        		}else if(comparator.equals("between ")){
        			if(((Comparable)cellValue).compareTo(parameter[0]) < 0) 
        				row.shown = false;
        			else if(((Comparable)cellValue).compareTo(parameter[1]) > 0)
        				row.shown = false;
        			else{
        				row.shown = true;
        				break;
        			}
        		}else if(comparator.equals("like ")){
        			if(((String)parameter[0]).startsWith("%")){
        				String p = ((String)parameter[0]).substring(1);
        				if(p.endsWith("%")) {
        					p = p.substring(0, p.length()-1);
        					if( ((String)cellValue).toUpperCase().indexOf(p.toUpperCase()) < 0)
        						row.shown = false;
        					else{
        						row.shown = true;
        						break;
        					}
        				}else {
            				if(!((String)cellValue).toUpperCase().endsWith(p.toUpperCase())) {
            					row.shown = false;
            				}else{
            					row.shown = true;
            					break;
            				}
        				}
        			}else{
        				String p = ((String)parameter[0]).substring(0, ((String)parameter[0]).length()-1);
        				if(!((String)cellValue).toUpperCase().startsWith(p.toUpperCase())) {
        					row.shown = false;
        				}else{
        					row.shown = true;
        					break;
        				}
        			}
        		}
        	}
        }
    }

    public boolean queryable() {
        return queryable;
    }

    public void setQuerayable(boolean queryable) {
        this.queryable = queryable;
    }
    
    public void setKey(String key) {
    	this.key = key;
    }
    
    public String getKey() {
    	return key;
    }
    
    protected Widget setCellDisplay(int modelIndex) {
    	int tableIndex = controller.tableIndex(modelIndex);
    	controller.renderer.setCellDisplay(modelIndex, columnIndex);
    	return controller.view.table.getWidget(tableIndex, columnIndex);
    }
    
    protected void resetAlign(int modelIndex) {
    	controller.view.table.getFlexCellFormatter().setHorizontalAlignment(controller.tableIndex(modelIndex), columnIndex, getAlign());
    }
}
