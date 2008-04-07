package org.openelis.gwt.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.MouseWheelListenerCollection;
import com.google.gwt.user.client.ui.MouseWheelVelocity;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesMouseWheelEvents;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;
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
import org.openelis.gwt.screen.ClassFactory;
import org.openelis.gwt.screen.ScreenBase;
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
    private ArrayList selected = new ArrayList();
    private int maxRows;
    private int start = 0;
    private int top = 0;
    private int cellHeight = 18;
    private int[] cellWidths;
    private int active = -1;
    private int cellspacing = 1;
    public boolean drag;
    public boolean drop;
    private boolean ctrl;
    public boolean multi;
    
    public ScrollList() {
        view = new TableView();
        view.setTableListener(this);
        initWidget(view);
    }
    
    public void setDataModel(DataModel dm) {
        this.dm = dm;
        start = 0;
    }
        
    public void setSelected(ArrayList selections){
        selected = new ArrayList();
        for(int i = 0; i < selections.size(); i++){
            if(selections.get(i) instanceof DataSet){
                if(dm.indexOf(((DataSet)selections.get(i)).getKey()) > -1)
                    selected.add(dm.get((DataObject)((DataSet)selections.get(i)).getKey()));
                else{
                    dm.add((DataSet)selections.get(i));
                    selected.add((DataSet)selections.get(i));
                }
                    
            }else{
                selected.add(dm.get((DataObject)selections.get(i)));
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
        try{
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
                view.setHeight((rowsPer*cellHeight+(rowsPer*cellspacing)+cellspacing));
                view.setScrollHeight((dm.size()*cellHeight)+(dm.size()*cellspacing)+cellspacing);
            }else if(view.table.getRowCount() > rowsPer){
                for(int i = view.table.getRowCount() -1; i >= rowsPer; i--)
                    view.table.removeRow(i);
                view.setHeight((rowsPer*cellHeight+(rowsPer*cellspacing)+cellspacing));
                view.setScrollHeight((dm.size()*cellHeight)+(dm.size()*cellspacing)+cellspacing);
            }
            for(int i = 0; i < rowsPer; i++){
                loadRow(i);
            }
        }catch(Exception e){
            Window.alert("scrollLoad "+e.getMessage());
        }
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
            active = index;
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
        view.setScrollHeight((dm.size()*cellHeight)+(dm.size()*cellspacing)+cellspacing);
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
                StringObject so = new StringObject();
                so.setValue(value);
                addDropItem(text,so);
            }
        }
    }
    
    public void setMaxRows(int rows){
        this.maxRows = rows;
        
        //view.cellView.setHeight((rows*cellHeight+(rows*cellspacing)+cellspacing+cellHeight+1)+"px");
        //view.scrollBar.setHeight((rows*cellHeight+1)+(rows*cellspacing)+cellHeight+1+"px");
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
    
    public boolean onKeyPress(Event event){
        int code = DOM.eventGetKeyCode(event);
        boolean shift = DOM.eventGetShiftKey(event);
        boolean ctrl = DOM.eventGetCtrlKey(event);
        if (KeyboardListener.KEY_DOWN == code) {
            if(active < 0){
                active = 0;
                view.table.getRowFormatter().addStyleName(active, TableView.selectedStyle);
                selected.add(dm.get(start+active));
            }else{
                if(active == maxRows -1){
                    if(start+active+1 < dm.size()){
                        selected.remove(dm.get(start+active));
                        selected.add(dm.get(start+active+1));
                    }
                    view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
                    view.table.getRowFormatter().addStyleName(active, TableView.selectedStyle);
                }else{
                    view.table.getRowFormatter().removeStyleName(active, TableView.selectedStyle);
                    selected.remove(dm.get(start+active));
                    active++;
                    view.table.getRowFormatter().addStyleName(active, TableView.selectedStyle);
                    selected.add(dm.get(start+active));
                }
            }
            DOM.eventCancelBubble(event, true);
            DOM.eventPreventDefault(event);
            return false;
        }
        if (KeyboardListener.KEY_UP == code) {
            if(active == 0){
                if(start+active-1 > -1){
                    selected.remove(dm.get(start+active));
                    selected.add(dm.get(start+active-1));
                }
                view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()-cellHeight);
                view.table.getRowFormatter().addStyleName(active, TableView.selectedStyle);
            }else if (active > 0){
                view.table.getRowFormatter().removeStyleName(active, TableView.selectedStyle);
                selected.remove(dm.get(start+active));
                active--;
                view.table.getRowFormatter().addStyleName(active, TableView.selectedStyle);
                selected.add(dm.get(start+active));
            }
            DOM.eventCancelBubble(event, true);
            DOM.eventPreventDefault(event);
            return false;
        }
        if (KeyboardListener.KEY_ENTER == code) {
            if(active > -1){
                changeListeners.fireChange(this);
            }
        }       
        return true;
    }

    /**
     * EventPreview for catching Keyboard events for the table.
     */
    public boolean onEventPreview(Event event) {
        // TODO Auto-generated method stub
        if (view.isAttached()) {
            if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                return onKeyPress(event);
            }
            if (DOM.eventGetType(event) == Event.ONCLICK){
                if(!DOM.isOrHasChild(view.table.getElement(), DOM.eventGetTarget(event))){
                    DOM.removeEventPreview(this);
                    if(changeListeners != null){
                        setActive(-1);
                        changeListeners.fireChange(this);
                    }
                    
                    return true;
                }
                if(multi && ctrl && !DOM.eventGetCtrlKey(event)){
                    unselectAll();
                    scrollLoad(view.scrollBar.getScrollPosition());
                }
                if(multi)
                    ctrl = DOM.eventGetCtrlKey(event);
            }
        }
        return true;
    }
    
    public void unselectAll() {
        selected = new ArrayList();
        for(int i=0; i < view.table.getRowCount(); i++){
            view.table.getRowFormatter().removeStyleName(i,TableView.selectedStyle);
        }        
        active = -1;
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
        return active;
    }

    public int getStart() {
        return start;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setActive(int active) {
        this.active = active;
    }
    
    public void setStart(int start) {
        this.start = start;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public ArrayList getSelected() {
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

    public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
        int clicked = row;
            if(active > -1){
                if(!ctrl){
                    if(!multi){
                        view.table.getRowFormatter().removeStyleName(active,TableView.selectedStyle);
                        selected.remove(dm.get(start+active));
                    }
                }   
            }
            if(multi && selectedRow == clicked){
                view.table.getRowFormatter().removeStyleName(clicked, TableView.selectedStyle);
                selected.remove(dm.get(start+clicked));
            }else{
                if(!multi)
                    selected.clear();
                active = clicked;
                view.table.getRowFormatter().addStyleName(active,TableView.selectedStyle);
                selected.add(dm.get(start+clicked));
            }
            changeListeners.fireChange(this);
    }
    
    
}
