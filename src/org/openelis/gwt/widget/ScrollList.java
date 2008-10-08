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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.KeyboardListener;
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
import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.widget.table.TableColumn;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableKeyboardHandlerInt;
import org.openelis.gwt.widget.table.TableModel;
import org.openelis.gwt.widget.table.TableMouseHandler;
import org.openelis.gwt.widget.table.TableRenderer;
import org.openelis.gwt.widget.table.TableView;
import org.openelis.gwt.widget.table.TableWidget;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;
import org.openelis.gwt.widget.table.event.SourcesTableWidgetEvents;
import org.openelis.gwt.widget.table.event.TableWidgetListener;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DragList is a widget that displays widgets in a vertical list
 * that can be dragged to other widgets on a screen.
 * 
 * @author tschmidt
 *
 */
public class ScrollList extends TableWidget implements SourcesChangeEvents, TableKeyboardHandlerInt, TableWidgetListener {
    
    private int top = 0;
    private int cellspacing = 1;
    public boolean drag;
    public boolean drop;
    public boolean maxHeight;
    
    public ScrollList(ArrayList<TableColumnInt> columns, int maxRows, String width, String title, boolean showHeader, VerticalScroll showScroll){
        super();
        for(TableColumnInt column : columns) {
            column.setTableWidget(this);
        }
        this.columns = columns;
        this.maxRows = maxRows;
        this.title = title;
        this.showHeader = showHeader;
        renderer = new TableRenderer(this);
        model = new TableModel(this);
        view = new TableView(this,showScroll);
        view.setWidth("auto");
        view.setHeight((maxRows*cellHeight+(maxRows*cellSpacing)+(maxRows*2)+cellSpacing));
        keyboardHandler = this;
        mouseHandler = new TableMouseHandler(this);
        addTableWidgetListener((TableWidgetListener)renderer);
        setWidget(view);
    }
    
    public ScrollList(int[] colWidths, int maxRows, String width, String title, boolean showHeader, VerticalScroll showScroll){
        super();
        columns = new ArrayList<TableColumnInt>();
        for(int wid : colWidths){
            TableColumn col = new TableColumn();
            col.setCurrentWidth(wid);
            columns.add(col);
            col.setTableWidget(this);
        }
        this.maxRows = maxRows;
        this.title = title;
        this.showHeader = showHeader;
        renderer = new TableRenderer(this);
        model = new TableModel(this);
        view = new TableView(this,showScroll);
        view.setWidth("auto");
        view.setHeight((maxRows*cellHeight+(maxRows*cellSpacing)+(maxRows*2)+cellSpacing));
        keyboardHandler = this;
        mouseHandler = new TableMouseHandler(this);
        addTableWidgetListener((TableWidgetListener)renderer);
        setWidget(view);
    }
    
    
        
    public void setSelected(ArrayList selections){
        model.clearSelections();
        for(int i = 0; i < selections.size(); i++){
            if(selections.get(i) instanceof DataSet){
                model.selectRow(model.getData().indexOf(model.getData().getByKey(((DataSet)selections.get(i)).getKey())));                    
            }else{
                model.selectRow(model.getData().indexOf(model.getData().getByKey(((DataObject)selections.get(i)))));   
            }
        }
    }
    
    public void setSelected(int index){
        model.selectRow(index);
    }
    
    public DataModel getDataModel() {
       return model.getData(); 
    }
    
  /*  
    public void scrollLoad(int scrollPos){
        int rowsPer = maxRows;
        if(maxRows > model.shownRows()){
            rowsPer = model.shownRows();
        }
        int loadStart = new Double(Math.ceil(((double)scrollPos)/(cellHeight))).intValue();
        if(model.numRows() != model.shownRows()){
            int start = 0;
            int i = 0;
            while(start < loadStart && i < model.numRows() -1){
                if(model.getRow(i).shown)
                    start++;
                i++;
            }
            loadStart = i;   
        }
        if(loadStart+rowsPer > model.numRows()){
            loadStart = loadStart - ((loadStart+rowsPer) - model.numRows());
        }
        for(int i = 0; i < rowsPer; i++){
            while(loadStart+i < model.numRows() && !model.getRow(loadStart+i).shown)
                loadStart++;
            loadRow(i,loadStart+i);
        }

    }
    
    
    public void loadRow(int index, int modelIndex){
        modelIndexList[index] = modelIndex;
        for(int i = 0; i < columns.size(); i++){
            ScreenLabel label = (ScreenLabel)view.table.getWidget(index,i);
            label.label.setText(model.getRow(modelIndex).get(i).getValue().toString());
            label.setUserObject(model.getRow(modelIndex).getKey().getValue());
        }
        if(model.isSelected(modelIndex)){
            view.table.getRowFormatter().addStyleName(index, view.selectedStyle);
        }else
            view.table.getRowFormatter().removeStyleName(index,view.selectedStyle);
    }
    
    public void createRow(int index){
        int i = 0;
        for(TableColumnInt column : columns) {
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
            label.setWidth(column.getCurrentWidth()+"px");
            view.table.getFlexCellFormatter().setWidth(index, i, column.getCurrentWidth() + "px");
            view.table.getFlexCellFormatter().setHeight(index, i, cellHeight+"px");
            view.table.getFlexCellFormatter().addStyleName(index,
                                                           i,
                                                           TableView.cellStyle);
            i++;
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
        ds.add(so);
        ds.setKey(value);
        model.addRow(ds);
        if(view.table.getRowCount() < maxRows){
            //createRow(model.numRows() -1);
            //loadRow(model.numRows()-1,model.numRows() - 1);
        }
        view.setScrollHeight((model.numRows()*cellHeight)+(model.numRows()*cellspacing)+(model.numRows()*2)+cellspacing);
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
        
    public void clear(){
        model.clear();
    }
    
    public Iterator getIterator(){
        return view.table.iterator();
    }
    
    public void removeItem(ScreenWidget wid){
        for(int i = 0; i < view.table.getRowCount(); i++){
            if(DOM.isOrHasChild(view.table.getRowFormatter().getElement(i),wid.getElement())){
                model.deleteRow(modelIndexList[i]);
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
                if(focused && !DOM.isOrHasChild(view.table.getElement(), ((AppScreen)sender).clickTarget)){
                    if(changeListeners != null){
                        setActive(-1);
                        if(focused)
                            changeListeners.fireChange(this);
                        focused = false;
                    }
                    return;
                }
            }
            
            if(model.getData().multiSelect && !ctrlKey){
                unselectAll();
                //scrollLoad(view.scrollBar.getScrollPosition());
            }
        }
    }
    
    public void unselectAll() {
        model.clearSelections();
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
        return activeRow;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setActive(int active) {
        activeRow = active;
        if(active > -1)
            model.selectRow(modelIndexList[active]);
            
    }
    
    public int getMaxRows() {
        return maxRows;
    }

    public ArrayList<DataSet> getSelected() {
        return model.getSelections();
    }

    public void setMulti(boolean multi) {
        model.enableMultiSelect(multi);
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
        focused = true;
            if(activeRow > -1){
                if(!ctrlKey){
                    if(!model.getData().multiSelect){
                        model.unselectRow(modelIndexList[row]);
                    }
                }   
            }
            if(model.getData().multiSelect && model.isSelected(modelIndexList[row])){
                model.unselectRow(modelIndexList[row]);
            }else{
                if(!model.getData().multiSelect)
                    model.clearSelections();
                activeRow = clicked;
                model.selectRow(modelIndexList[row]);
            }
            changeListeners.fireChange(this);
    }

    public void onKeyDown(Widget sender, char code, int modifiers) {
        if(!focused)
            return;
        boolean shift = modifiers == KeyboardListener.MODIFIER_SHIFT;
        ctrlKey = modifiers == KeyboardListener.MODIFIER_CTRL;
        if (KeyboardListener.KEY_DOWN == code) {
            if(activeRow < 0){
                activeRow = 0;
                model.selectRow(activeRow);
            }else{
                if(activeRow == view.table.getRowCount() -1){
                    if(modelIndexList[activeRow]+1 < model.numRows()){
                        model.unselectRow(-1);
                        view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
                        DeferredCommand.addCommand(new Command() {
                            public void execute() {
                                activeRow = view.table.getRowCount() -1;
                                model.selectRow(modelIndexList[view.table.getRowCount() -1]);
                            }
                        });
                    }
                }else{
                    int row = activeRow + 1;
                    model.unselectRow(-1);
                    activeRow = row;
                    model.selectRow(modelIndexList[activeRow]);
                }
            }
        }
        if (KeyboardListener.KEY_UP == code) {
            if(activeRow == 0){
                if(modelIndexList[activeRow] - 1 > -1){
                    model.unselectRow(-1);
                    view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()-cellHeight);
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            activeRow = 0;
                            model.selectRow(modelIndexList[0]);
                        }
                    });
                }
            }else if (activeRow > 0){
                int row = activeRow -1;
                model.unselectRow(-1);
                activeRow = row;
                model.selectRow(modelIndexList[activeRow]);
            }
        }
        if (KeyboardListener.KEY_ENTER == code || KeyboardListener.KEY_TAB == code) {
            if(activeRow > -1){
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

    public void load(int pos) {
        // TODO Auto-generated method stub
        
    }

    public void setCellDisplay(int row, int col) {
        // TODO Auto-generated method stub
        
    }

    public void setCellEditor(int row, int col) {
        // TODO Auto-generated method stub
        
    }

    public void finishedEditing(SourcesTableWidgetEvents sender, int row, int col) {
        // TODO Auto-generated method stub
        
    }

    public void startedEditing(SourcesTableWidgetEvents sender, int row, int col) {
        // TODO Auto-generated method stub
        
    }
    
    
}
