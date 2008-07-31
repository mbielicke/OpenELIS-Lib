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
package org.openelis.gwt.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.screen.ClassFactory;
import org.openelis.gwt.screen.ScreenLabel;
import org.openelis.gwt.screen.ScreenScrollList;
import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.widget.table.TableController;
import org.openelis.gwt.widget.table.TableView;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DragList is a widget that displays widgets in a vertical list
 * that can be dragged to other widgets on a screen.
 * 
 * @author tschmidt
 *
 */
public class ScrollList extends TableController implements SourcesChangeEvents {
    
    private ChangeListenerCollection changeListeners;
    private DataModel dm = new DataModel();
    private ArrayList<DataSet> selected = new ArrayList<DataSet>();
    private int maxRows;
    private int start = 0;
    private int top = 0;
    private int cellHeight = 18;
    private int[] cellWidths;
    public int activeIndex = -1;
    private int cellspacing = 1;
    public boolean drag;
    public boolean drop;
    private boolean ctrl;
    public boolean multi;
    public boolean maxHeight;
    
    public ScrollList() {
        view = new TableView();
        view.setTableListener(this);
        initWidget(view);
    }
    
    public void setDataModel(DataModel dm) {
        if(dm != null)
            this.dm = dm;
        else
            this.dm = new DataModel();
        start = 0;
    }
        
    public void setSelected(ArrayList selections){
        selected = new ArrayList<DataSet>();
        for(int i = 0; i < selections.size(); i++){
            if(selections.get(i) instanceof DataSet){
                if(dm.indexOf(((DataSet)selections.get(i)).getKey()) > -1)
                	if(multi)
                		selected.add(dm.get((DataObject)((DataSet)selections.get(i)).getKey()));
                	else{
                		selected.clear();
                		selected.add(dm.get((DataObject)((DataSet)selections.get(i)).getKey()));
                	}
                else{
                    dm.add((DataSet)selections.get(i));
                    if(multi)
                    	selected.add((DataSet)selections.get(i));
                    else{
                    	selected.clear();
                    	selected.add((DataSet)selections.get(i));
                    }
                }
                    
            }else{
            	if(multi)
            		selected.add(dm.get((DataObject)selections.get(i)));
            	else{
            		selected.clear();
            		selected.add(dm.get((DataObject)selections.get(i)));
            	}
            }
        }
    }
    
    public void setSelected(int index){
        if(selected.contains(dm.get(index))){
            selected.remove(dm.get(index));
        }else{
            selected.add(dm.get(index));
        }
    }
    
    public DataModel getDataModel() {
       return dm; 
    }
    
    public void scrollLoad(int scrollPos){
        
            int rowsPer = maxRows;
            if(maxRows > dm.size())
                rowsPer = dm.size();
            start = (scrollPos)/(cellHeight);
            if(start+rowsPer > dm.size())
                start = start - ((start+rowsPer) - dm.size());
            if(view.table.getRowCount() < rowsPer){
                for(int i = view.table.getRowCount(); i < rowsPer; i++){
                    createRow(i);
                }
 //               view.setHeight((rowsPer*cellHeight+(rowsPer*cellspacing)+cellspacing));
 //               view.setScrollHeight((dm.size()*cellHeight)+(dm.size()*cellspacing)+cellspacing);
            }else if(view.table.getRowCount() > rowsPer){
                for(int i = view.table.getRowCount() -1; i >= rowsPer; i--)
                    view.table.removeRow(i);
            }
            if(!maxHeight){
                view.setHeight((rowsPer*cellHeight+(rowsPer*cellspacing)+cellspacing));
            }else
                view.setHeight((maxRows*cellHeight+(maxRows*cellspacing)+cellspacing));
            view.setScrollHeight((dm.size()*cellHeight)+(dm.size()*cellspacing)+cellspacing);
            for(int i = 0; i < rowsPer; i++){
                loadRow(i);
            }
            super.active = true;
        
    }
    
    private void loadRow(int index){
        for(int i = 0; i < cellWidths.length; i++){
            ScreenLabel label = (ScreenLabel)view.table.getWidget(index,i);
            label.label.setText(dm.get(start+index).getObject(i).getValue().toString());
            label.setUserObject(dm.get(start+index).getKey().getValue());
        }
        view.table.getRowFormatter().removeStyleName(index, TableView.selectedStyle);
        if(selected.contains(dm.get(start+index))){
            view.table.getRowFormatter().addStyleName(index, TableView.selectedStyle);
            activeIndex = index;
        }
    }
    
    private void createRow(int index){
        for(int i = 0; i < cellWidths.length; i++){
            ScreenLabel label = new ScreenLabel("   ",null);
            view.table.setWidget(index, i, label);
            label.label.setWordWrap(false);
            DOM.setStyleAttribute(label.getElement(), "overflowX", "hidden");
            label.addMouseListener((MouseListener)ClassFactory.forName("HoverListener"));
            if(drag){
                label.addMouseListener((MouseListener)ClassFactory.forName("ProxyListener"));
                label.sinkEvents(Event.MOUSEEVENTS);
            }
            if(drop){
                label.setDropTargets(((ScreenScrollList)getParent()).getDropTargets());
                label.setScreen(((ScreenScrollList)getParent()).getScreen());
            }
            label.setWidth(cellWidths[i]+"px");
            view.table.getFlexCellFormatter().setWidth(index, i, curColWidth[i] + "px");
            view.table.getFlexCellFormatter().setHeight(index, i, cellHeight+"px");
            view.table.getFlexCellFormatter().addStyleName(index,
                                                           i,
                                                           TableView.cellStyle);
        }
        view.table.getRowFormatter().addStyleName(index, TableView.rowStyle);
        if(index % 2 == 1){
            DOM.setStyleAttribute(view.table.getRowFormatter().getElement(index), "background", "#f8f8f9");
        }
        if(showRows){
            Label rowNum = new Label(String.valueOf(index+1));
            view.rows.setWidget(index,0,rowNum);
            view.rows.getFlexCellFormatter().setStyleName(index, 0, "RowNum");
            view.rows.getFlexCellFormatter().setHeight(index,0,cellHeight+"px");
        }
        
        //vp.setCellWidth(hp,cellView.getOffsetWidth()+"px");
    }
   
    /**
     * Method used to add a widget to the list
     * @param wid
     */
    public void addItem(Widget wid){
        view.table.add(wid);

    }
    
    /**
     * Handles the dropping of widget on to this widget
     * @param text
     * @param value
     */
    public void addDropItem(String text, DataObject value){
        DataSet ds = new DataSet();
        StringObject so = new StringObject();
        so.setValue(text);
        ds.addObject(so);
        ds.setKey(value);
        dm.add(ds);
        if(view.table.getRowCount() < maxRows){
            createRow(dm.size() -1);
            loadRow(dm.size()-1);
        }
        view.setScrollHeight((dm.size()*cellHeight)+(dm.size()*cellspacing)+(dm.size()*2)+cellspacing);
    }
    
    /**
     * This method takes an xml string that set the list of widgets
     * If using as part of ScreenDragList use its load(String xml) instead.
     * @param xml
     */
    public void setList(String xml){
        Document doc = XMLParser.parse(xml);
        Element root = doc.getDocumentElement();
        NodeList items = root.getElementsByTagName("item");
        for(int i = 0; i < items.getLength(); i++){
            if(items.item(i).getNodeType() == Node.ELEMENT_NODE){
                String text = items.item(i).getAttributes().getNamedItem("text").getNodeValue();
                String value = "";
                if(items.item(i).getAttributes().getNamedItem("value") != null)
                    value = items.item(i).getAttributes().getNamedItem("value").getNodeValue();
                StringObject so = new StringObject(value);
                addDropItem(text,so);
            }
        }
    }
    
    public void setMaxRows(int rows){
        this.maxRows = rows;
    }
    
    
    public void setCellHeight(int height){
        this.cellHeight = height;
    }
    
    public void setWidth(String width){
        view.setWidth(width);
    }
    
    public void setCellWidths(int[] widths){
        cellWidths = widths;
        setColWidths(widths);
        view.initTable(this);
    }
    
    public void setHeaders(String[] headers){
        view.setHeaders(headers);
    }
    
    public void clear(){
        view.table.clear();
        dm = new DataModel();
    }
    
    public Iterator getIterator(){
        return view.table.iterator();
    }
    
    public void removeItem(ScreenWidget wid){
        for(int i = 0; i < view.table.getRowCount(); i++){
            if(DOM.isOrHasChild(view.table.getRowFormatter().getElement(i),wid.getElement())){
                dm.delete(start+i);
                view.table.removeRow(i);
                break;
            }
        }

        
    }
    
    /**
     * Sets the list to be draggable or not
     * @param enabled
     */
    public void enable(boolean enabled){
        if(!drag)
            return;
        Iterator it = view.table.iterator();
        while(it.hasNext()){
            ScreenWidget wid = (ScreenWidget)it.next();
            wid.removeMouseListener((MouseListener)ClassFactory.forName("ProxyListener"));
            if(enabled){
                wid.addMouseListener((MouseListener)ClassFactory.forName("ProxyListener"));
            }
        }
    }
    
    
    public void onClick(Widget sender){
        if(view.table.isAttached()){
            if(sender instanceof AppScreen){
                if(super.active && !DOM.isOrHasChild(view.table.getElement(), ((AppScreen)sender).clickTarget)){
                    if(changeListeners != null){
                        setActive(-1);
                        if(super.active)
                            changeListeners.fireChange(this);
                        super.active = false;
                    }
                    return;
                }
            }
            
            if(multi && !ctrl){
                unselectAll();
                scrollLoad(view.scrollBar.getScrollPosition());
            }
        }
    }
    
    public void unselectAll() {
        selected = new ArrayList();
        for(int i=0; i < view.table.getRowCount(); i++){
            view.table.getRowFormatter().removeStyleName(i,TableView.selectedStyle);
        }        
        activeIndex = -1;
    }

    public void addChangeListener(ChangeListener listener) {
        if(changeListeners == null){
            changeListeners = new ChangeListenerCollection();
        }
        changeListeners.add(listener);
        
    }

    public void removeChangeListener(ChangeListener listener) {
        if(changeListeners != null){
            changeListeners.remove(listener);
        }
    }

    public int getActive() {
        return activeIndex;
    }

    public int getStart() {
        return start;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setActive(int active) {
        this.activeIndex = active;
        if(active > -1 && view.table.getRowCount() > 0){
            view.table.getRowFormatter().addStyleName(active, TableView.selectedStyle);
            //this will make sure it isnt already in the array
            if(multi){
            	selected.remove(dm.get(start+active));
                selected.add(dm.get(start+active));	
            }else{
            	selected.clear();
            	selected.add(dm.get(start+active));
            }
            
        }
        //super.active=true;
        
    }
    
    public void setStart(int start) {
        this.start = start;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public ArrayList<DataSet> getSelected() {
        return selected;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseLeave(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }
    
    public void fireChange() {
        changeListeners.fireChange(this);
    }

    public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
        int clicked = row;
        super.active = true;
            if(activeIndex > -1){
                if(!ctrl){
                    if(!multi){
                        view.table.getRowFormatter().removeStyleName(activeIndex,TableView.selectedStyle);
                        selected.remove(dm.get(start+activeIndex));
                    }
                }   
            }
            if(multi && selected.contains(dm.get(start+clicked))){
                view.table.getRowFormatter().removeStyleName(clicked, TableView.selectedStyle);
                selected.remove(dm.get(start+clicked));
            }else{
                if(!multi)
                    selected.clear();
                activeIndex = clicked;
                view.table.getRowFormatter().addStyleName(activeIndex,TableView.selectedStyle);
                selected.add(dm.get(start+clicked));
            }
            changeListeners.fireChange(this);
    }

    public void onKeyDown(Widget sender, char code, int modifiers) {
        if(!super.active)
            return;
        boolean shift = modifiers == KeyboardListener.MODIFIER_SHIFT;
        ctrl = modifiers == KeyboardListener.MODIFIER_CTRL;
        if (KeyboardListener.KEY_DOWN == code) {
            if(activeIndex < 0){
                activeIndex = 0;
                view.table.getRowFormatter().addStyleName(activeIndex, TableView.selectedStyle);
                selected.add(dm.get(start+activeIndex));
            }else{
                if(activeIndex == view.table.getRowCount() -1){
                    if(start+activeIndex+1 < dm.size()){
                        selected.remove(dm.get(start+activeIndex));
                        selected.add(dm.get(start+activeIndex+1));
                    }
                    view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
                    view.table.getRowFormatter().addStyleName(activeIndex, TableView.selectedStyle);
                }else{
                    view.table.getRowFormatter().removeStyleName(activeIndex, TableView.selectedStyle);
                    selected.remove(dm.get(start+activeIndex));
                    activeIndex++;
                    view.table.getRowFormatter().addStyleName(activeIndex, TableView.selectedStyle);
                    selected.add(dm.get(start+activeIndex));
                }
            }
        }
        if (KeyboardListener.KEY_UP == code) {
            if(activeIndex == 0){
                if(start+activeIndex-1 > -1){
                    selected.remove(dm.get(start+activeIndex));
                    selected.add(dm.get(start+activeIndex-1));
                }
                view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()-cellHeight);
                view.table.getRowFormatter().addStyleName(activeIndex, TableView.selectedStyle);
            }else if (activeIndex > 0){
                view.table.getRowFormatter().removeStyleName(activeIndex, TableView.selectedStyle);
                selected.remove(dm.get(start+activeIndex));
                activeIndex--;
                view.table.getRowFormatter().addStyleName(activeIndex, TableView.selectedStyle);
                selected.add(dm.get(start+activeIndex));
            }
        }
        if (KeyboardListener.KEY_ENTER == code || KeyboardListener.KEY_TAB == code) {
            if(activeIndex > -1){
                changeListeners.fireChange(this);
            }
        }       
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }
    
    
}
