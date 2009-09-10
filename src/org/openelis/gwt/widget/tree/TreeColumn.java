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
package org.openelis.gwt.widget.tree;

import java.util.ArrayList;
import java.util.HashSet;

import org.openelis.gwt.screen.UIUtil;
import org.openelis.gwt.widget.deprecated.IconContainer;
import org.openelis.gwt.widget.AutoComplete;
import org.openelis.gwt.widget.CalendarLookUp;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.DropdownWidget;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.table.Filter;
import org.openelis.gwt.widget.table.TableDataCell;
import org.openelis.gwt.widget.table.TableDataRow;

import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

public class TreeColumn {

    public String header;
    public boolean sortable;
    public boolean filterable;
    public int preferredWidth;
    public int currentWidth;
    public int minWidth;
    public boolean fixedWidth;
    public HasHorizontalAlignment.HorizontalAlignmentConstant alignment = HasHorizontalAlignment.ALIGN_LEFT;
    public TreeWidget controller;
    public int columnIndex;
    public String key;
    public Widget colWidget;
    public String query;
    protected PopupPanel pop;
    public boolean filtered;
    protected HashSet<Object> filtersInForce;
    protected ArrayList<Filter> filterList;
    public String leafType;
    public ArrayList<String> sortLeaves;
    
    
    public Widget getDisplayWidget(TableDataCell cell) {
    	Widget wid = null;
    	if(colWidget instanceof CheckBox){
    		wid = new IconContainer();
    		if(CheckBox.CHECKED.equals(cell.getValue()))
    			wid.setStyleName(CheckBox.CHECKED_STYLE);
    		else
    			wid.setStyleName(CheckBox.UNCHECKED_STYLE);
    		setAlign(HasHorizontalAlignment.ALIGN_CENTER);
    		wid.setWidth("15px");
       	}else{
    		if(colWidget instanceof AutoComplete) {
    			TableDataRow row = (TableDataRow)cell.getValue();
    			if(row  != null)
    				((AutoComplete)colWidget).setSelection(row);
    			else
    				((AutoComplete)colWidget).setSelection(null,"");
    		}else if(colWidget instanceof Dropdown){
    			((Dropdown)colWidget).setSelection(cell.getValue());
    		}else{
    			((HasField)colWidget).setFieldValue(cell.getValue());
    		}
    		Object val = ((HasField)colWidget).getFieldValue();
    		Label label = new Label("");
    		if(val != null) {
    			if(colWidget instanceof CalendarLookUp) {
    				label.setText((((CalendarLookUp) colWidget).getField().format()));
    			}else if(colWidget instanceof DropdownWidget) {
    				label.setText(((DropdownWidget)colWidget).getTextBoxDisplay());
    			}else if(colWidget instanceof TextBoxBase) {
    				label.setText(((TextBoxBase)colWidget).getText());
    			}else if(colWidget instanceof Label)
    				label.setText(((Label)colWidget).getText());
    		}
    		label.setWordWrap(false);
    		wid = label;
    		wid.setWidth((currentWidth)+ "px");
    	}
        
        wid.setHeight((controller.cellHeight+"px"));
        if(cell.errors != null) {
        	final VerticalPanel errorPanel = new VerticalPanel();
            for (String error : cell.errors) {
            	HorizontalPanel hp = new HorizontalPanel();
            	hp.add(new Label(error));
            	hp.add(new Image("Iamges/bullet_red.png"));
                hp.setStyleName("errorPopupLabel");
                errorPanel.add(hp);
            }
        	wid.addStyleName("InputError");
        	((HasMouseOverHandlers)wid).addMouseOverHandler(new MouseOverHandler() {
        		
				public void onMouseOver(MouseOverEvent event) {
			        if(((Widget)event.getSource()).getStyleName().indexOf("InputError") > -1){
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
			            pop.setPopupPosition(((Widget)event.getSource()).getAbsoluteLeft()+((Widget)event.getSource()).getOffsetWidth(), ((Widget)event.getSource()).getAbsoluteTop());
			            pop.show();
			        }
				}
        		
        	});
        	((HasMouseOutHandlers)wid).addMouseOutHandler(new MouseOutHandler() {

				public void onMouseOut(MouseOutEvent event) {
			        if(((Widget)event.getSource()).getStyleName().indexOf("InputError") > -1){
			            if(pop != null){
			                pop.hide();
			            }
			        }
				}
        		
        	});
        	
        }
        return wid;
    }
    
    public void loadWidget(Widget widget, TableDataCell cell) {
    	if(colWidget instanceof CheckBox){
    		if(CheckBox.CHECKED.equals(cell.getValue()))
    			widget.setStyleName(CheckBox.CHECKED_STYLE);
    	    else
    	    	widget.setStyleName(CheckBox.UNCHECKED_STYLE);
    	}else if(widget instanceof Label) {
    		if(colWidget instanceof AutoComplete) {
    			TableDataRow row = (TableDataRow)cell.getValue();
    			if(row != null)
    				((AutoComplete)colWidget).setSelection(row);
    			else
    				((AutoComplete)colWidget).setSelection(null,"");
    			((Label)widget).setText(((AutoComplete)colWidget).getTextBoxDisplay());
    		}else{
    			((HasField)colWidget).setFieldValue(cell.getValue());
    			if(colWidget instanceof CalendarLookUp) {
    				((Label)widget).setText(((CalendarLookUp)colWidget).getField().format());
    			}else if(colWidget instanceof DropdownWidget) {
    				((Label)widget).setText(((DropdownWidget)colWidget).getTextBoxDisplay());
    			}else if(colWidget instanceof TextBoxBase) {
    				((Label)widget).setText(((TextBoxBase)colWidget).getText());
    			}else{
    				if(((HasField)colWidget).getFieldValue() != null)
    					((Label)widget).setText(((HasField)colWidget).getField().format());
    				else
    					((Label)widget).setText("");
    			}
    		}
    	}
        if(cell.errors != null) {
        	final VerticalPanel errorPanel = new VerticalPanel();
            for (String error : cell.errors) {
            	HorizontalPanel hp = new HorizontalPanel();
            	hp.add(new Label(error));
            	hp.add(new Image("Iamges/bullet_red.png"));
                hp.setStyleName("errorPopupLabel");
                errorPanel.add(hp);
            }
        	widget.addStyleName("InputError");
        	((HasMouseOverHandlers)widget).addMouseOverHandler(new MouseOverHandler() {
        		
				public void onMouseOver(MouseOverEvent event) {
			        if(((Widget)event.getSource()).getStyleName().indexOf("InputError") > -1){
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
			            pop.setPopupPosition(((Widget)event.getSource()).getAbsoluteLeft()+((Widget)event.getSource()).getOffsetWidth(), ((Widget)event.getSource()).getAbsoluteTop());
			            pop.show();
			        }
				}
        		
        	});
        	((HasMouseOutHandlers)widget).addMouseOutHandler(new MouseOutHandler() {

				public void onMouseOut(MouseOutEvent event) {
			        if(((Widget)event.getSource()).getStyleName().indexOf("InputError") > -1){
			            if(pop != null){
			                pop.hide();
			            }
			        }
				}
        		
        	});
        	
        }
    }
    
    public Widget getWidgetEditor(TableDataCell cell) {
    	Widget editor = colWidget;
    	if(colWidget instanceof CheckBox){
    		((CheckBox)editor).setValue((String)cell.getValue());
    		//editor = controller.view.table.getWidget(controller.activeRow,controller.activeCell);
    		editor.setWidth("15px");
    		return editor;
    	}
    	editor = colWidget;
    	editor.setWidth((currentWidth)+ "px");
    	if(colWidget instanceof AutoComplete){
    		TableDataRow row =  (TableDataRow)cell.getValue();
    		if(row != null)
    			((AutoComplete)colWidget).setSelection(row);
    		else
    			((AutoComplete)colWidget).setSelection(null,"");
		}else if(colWidget instanceof Dropdown){
			((Dropdown)colWidget).setSelection(cell.getValue());
    	}else
    		((HasField)editor).setFieldValue(cell.getValue());
       
        editor.setHeight((controller.cellHeight+"px"));
        if(cell.errors != null) {
        	final VerticalPanel errorPanel = new VerticalPanel();
            for (String error : cell.errors) {
            	HorizontalPanel hp = new HorizontalPanel();
            	hp.add(new Label(error));
            	hp.add(new Image("Iamges/bullet_red.png"));
                hp.setStyleName("errorPopupLabel");
                errorPanel.add(hp);
            }
        	editor.addStyleName("InputError");
        	((HasMouseOverHandlers)colWidget).addMouseOverHandler(new MouseOverHandler() {
        		
				public void onMouseOver(MouseOverEvent event) {
			        if(((Widget)event.getSource()).getStyleName().indexOf("InputError") > -1){
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
			            pop.setPopupPosition(((Widget)event.getSource()).getAbsoluteLeft()+((Widget)event.getSource()).getOffsetWidth(), ((Widget)event.getSource()).getAbsoluteTop());
			            pop.show();
			        }
				}
        		
        	});
        	((HasMouseOutHandlers)editor).addMouseOutHandler(new MouseOutHandler() {

				public void onMouseOut(MouseOutEvent event) {
			        if(((Widget)event.getSource()).getStyleName().indexOf("InputError") > -1){
			            if(pop != null){
			                pop.hide();
			            }
			        }
				}
        		
        	});
        	
        }
        return editor;
    }

    public void enable(boolean enable) {
    	if(colWidget instanceof HasField)
    		((HasField)colWidget).enable(enable);
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
    
    public void setTreeWidget(TreeWidget controller){
        this.controller = controller;
    }
    
    public TreeWidget getTreeWidget() {
        return controller;
    }
    
    public ArrayList<Filter> getFilters() {
    	Filter all = new Filter(null,new Label("All"),true);
    	if(filtered)
    		all.filtered = false;
    	filterList = new ArrayList<Filter>();
    	filterList.add(all);
    	int col = controller.columns.get(leafType).indexOf(this);
    	ArrayList<Object> checkList = new ArrayList<Object>();
    	checkList.add(null);
    	for(TableDataRow row : controller.getData()){
    		if(checkList.contains(row.cells.get(col).getValue()))
    			continue;
    		Filter filter = new Filter(row.cells.get(col).getValue(),getDisplayWidget(row.cells.get(col)),false);
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
    	int col = controller.columns.get(leafType).indexOf(this);
    	for(TableDataRow row : controller.getData()) {
    		if(!row.shown)
    			continue;
    		if(!filtersInForce.contains(row.cells.get(col).getValue()))
    			row.shown = false;
    	}
    }

    public void applyQueryFilter() {
       
    }
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        
    }

    
}
