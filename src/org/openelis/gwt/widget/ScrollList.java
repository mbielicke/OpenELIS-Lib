package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.Iterator;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.ScreenLabel;
import org.openelis.gwt.screen.ScreenScrollList;
import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.StringObject;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.MouseWheelListenerCollection;
import com.google.gwt.user.client.ui.MouseWheelVelocity;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesMouseWheelEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;


/**
 * DragList is a widget that displays widgets in a vertical list
 * that can be dragged to other widgets on a screen.
 * 
 * @author tschmidt
 *
 */
public class ScrollList extends Composite implements ScrollListener, MouseWheelListener, ClickListener, EventPreview, SourcesChangeEvents{
    
    public class CellView extends ScrollPanel implements SourcesMouseWheelEvents {

        public CellView() {
            sinkEvents(Event.ONMOUSEWHEEL);
        }
        
        public void onBrowserEvent(Event event) {
            // TODO Auto-generated method stub
            if(DOM.eventGetType(event) == event.ONMOUSEWHEEL){
                listeners.fireMouseWheelEvent(this, event);
            }
            super.onBrowserEvent(event);
        }
        
        private MouseWheelListenerCollection listeners;
        
        public void addMouseWheelListener(MouseWheelListener listener) {
            if(listeners == null){
                listeners = new MouseWheelListenerCollection();
            }
            listeners.add(listener);
        }

        public void removeMouseWheelListener(MouseWheelListener listener) {
            if(listeners != null){
                listeners.remove(listener);
            }
            
        }
        
    }
    
    private ChangeListenerCollection changeListeners;
    public CellView cellView = new CellView();
    private VerticalPanel vp = new VerticalPanel();
    private HorizontalPanel hp = new HorizontalPanel();
    public ScrollPanel scrollBar = new ScrollPanel();
    private DataModel dm = new DataModel();
    private ArrayList selected = new ArrayList();
    private int maxRows;
    private int start = 0;
    private int top = 0;
    private int cellHeight = 15;
    private int active = -1;
    private int cellspacing = 1;
    public boolean drag;
    public boolean drop;
    private boolean ctrl;
    public boolean multi;
    
    public ScrollList() {
        initWidget(hp);
        vp.setSpacing(1);
        cellView.setWidget(vp);
        hp.add(cellView);
        hp.add(scrollBar);
        
        cellView.addScrollListener(this);
        //vp.setStyleName("DragContainer");
        cellView.addMouseWheelListener(this);
        scrollBar.setWidth("18px");
        scrollBar.addScrollListener(this);
        AbsolutePanel ap = new AbsolutePanel();
        DOM.setStyleAttribute(scrollBar.getElement(), "overflowX", "hidden");
        DOM.setStyleAttribute(scrollBar.getElement(), "display", "none");
        DOM.setStyleAttribute(cellView.getElement(),"overflowY","hidden");        
        DOM.setStyleAttribute(cellView.getElement(),"overflowX","hidden");
        scrollBar.setWidget(ap);
        //DOM.addEventPreview(this);
    }
    
    public void setDataModel(DataModel dm) {
        this.dm = dm;
        if(dm != null && dm.size() > 0){
            setScrollHeight((dm.size()*cellHeight)+(dm.size()*cellspacing)+cellspacing);
            vp.clear();
            int num = maxRows;
            if(dm.size() < maxRows)
                num = dm.size();
            for(int i = 0; i < num; i++){
                createRow();
            }
            scrollLoad(0);
        }
    }
        
    public void setSelected(ArrayList selections){
        selected.clear();
        for(int i = 0; i < selections.size(); i++){
            if(selections.get(i) instanceof DataSet){
                selected.add(selections.get(i));
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
            for(int i = 0; i < rowsPer; i++){
                loadRow(i);
            }
        }catch(Exception e){
            Window.alert("scrollLoad "+e.getMessage());
        }
    }
    
    private void loadRow(int index){
        ScreenLabel label = (ScreenLabel)vp.getWidget(index);
        label.label.setText(dm.get(start+index).getObject(0).getValue().toString());
        label.setUserObject(dm.get(start+index).getKey().getValue());
        label.removeStyleName("Highlighted");
        if(selected.contains(dm.get(start+index))){
            label.addStyleName("Highlighted");
        }
    }
    
    private void createRow(){
        ScreenLabel label = new ScreenLabel("   ",null);
        vp.add(label);
        label.label.setWordWrap(false);
        label.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
        if(vp.getWidgetCount() % 2 == 1){
            label.addStyleName("AltTableRow");
        }else{
            label.addStyleName("TableRow");
        }
        if(drag){
            label.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("ProxyListener"));
            label.sinkEvents(Event.MOUSEEVENTS);
        }
        if(drop){
            label.setDropTargets(((ScreenScrollList)getParent()).getDropTargets());
            label.setScreen(((ScreenScrollList)getParent()).getScreen());
        }
        vp.setCellWidth(label,cellView.getOffsetWidth()+"px");
        label.setHeight(cellHeight+"px");
        label.addClickListener(this);
    }
   
    /**
     * Method used to add a widget to the list
     * @param wid
     */
    public void addItem(Widget wid){
        vp.add(wid);

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
        if(vp.getWidgetCount() < maxRows){
            createRow();
            loadRow(dm.size()-1);
        }
        setScrollHeight((dm.size()*cellHeight)+(dm.size()*cellspacing)+cellspacing);
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
        cellView.setHeight((rows*cellHeight+(rows*cellspacing)+cellspacing)+"px");
        scrollBar.setHeight((rows*cellHeight+1)+(rows*cellspacing)+"px");
        
    }
    
    public void setCellHeight(int height){
        this.cellHeight = height;
    }
    
    public void setScrollHeight(int height) {
        try {
            scrollBar.getWidget().setHeight(height+"px");
            if(height > cellView.getOffsetHeight())
                DOM.setStyleAttribute(scrollBar.getElement(), "display", "block");
            else 
                DOM.setStyleAttribute(scrollBar.getElement(),"display","none");
        }catch(Exception e){
            Window.alert("set scroll height"+e.getMessage());
        }
    }
    
    public void setWidth(String width){
        cellView.setWidth(width); 
    }
    
    public void clear(){
        vp.clear();
        dm = new DataModel();
    }
    
    public Iterator getIterator(){
        return vp.iterator();
    }
    
    public void removeItem(ScreenWidget wid){
        dm.delete(vp.getWidgetIndex(wid));
        vp.remove(wid);
        
    }
    
    /**
     * Sets the list to be draggable or not
     * @param enabled
     */
    public void enable(boolean enabled){
        if(!drag)
            return;
        Iterator it = vp.iterator();
        while(it.hasNext()){
            ScreenWidget wid = (ScreenWidget)it.next();
            wid.removeMouseListener((MouseListener)ScreenBase.getWidgetMap().get("ProxyListener"));
            if(enabled){
                wid.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("ProxyListener"));
            }
        }
    }
    
    public void onMouseWheel(Widget sender, MouseWheelVelocity velocity) {
        int pos = scrollBar.getScrollPosition();
        int delta = velocity.getDeltaY();
        if(delta < 0 && delta > -cellHeight)
            delta = -cellHeight;
        if(delta > 0 && delta < cellHeight)
            delta = cellHeight;
        scrollBar.setScrollPosition(pos + delta);
    }
    
    public void onScroll(Widget sender, int scrollLeft, int scrollTop) {
        if(sender == scrollBar ) {
            if(top != scrollTop){
                scrollLoad(scrollTop);
                top = scrollTop;
            }
        }
    }
    
    private boolean onKeyPress(Event event){
        int code = DOM.eventGetKeyCode(event);
        boolean shift = DOM.eventGetShiftKey(event);
        boolean ctrl = DOM.eventGetCtrlKey(event);
        if (KeyboardListener.KEY_DOWN == code) {
            if(active < 0){
                active = 0;
                ((ScreenWidget)vp.getWidget(0)).getWidget().addStyleName("Highlighted");
                selected.add(dm.get(start+active));
            }else{
                if(active == maxRows -1){
                    if(start+active+1 < dm.size()){
                        selected.remove(dm.get(start+active));
                        selected.add(dm.get(start+active+1));
                    }
                    scrollBar.setScrollPosition(scrollBar.getScrollPosition()+cellHeight);
                }else{
                    ((ScreenWidget)vp.getWidget(active)).removeStyleName("Highlighted");
                    selected.remove(dm.get(start+active));
                    active++;
                    ((ScreenWidget)vp.getWidget(active)).addStyleName("Highlighted");
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
                scrollBar.setScrollPosition(scrollBar.getScrollPosition()-cellHeight);
            }else if (active > 0){
                ((ScreenWidget)vp.getWidget(active)).removeStyleName("Highlighted");
                selected.remove(dm.get(start+active));
                active--;
                ((ScreenWidget)vp.getWidget(active)).addStyleName("Highlighted");
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
        if (vp.isAttached()) {
            if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                return onKeyPress(event);
            }
            if (DOM.eventGetType(event) == Event.ONCLICK){
                if(!DOM.isOrHasChild(vp.getElement(), DOM.eventGetTarget(event))){
                    DOM.removeEventPreview(this);
                    
                    if(changeListeners != null){
                    	setActive(-1);
                    	changeListeners.fireChange(this);
                    }
                    
                    return true;
                }
                if(multi && ctrl && !DOM.eventGetCtrlKey(event)){
                    unselectAll();
                    scrollLoad(scrollBar.getScrollPosition());
                }
                if(multi)
                    ctrl = DOM.eventGetCtrlKey(event);
            }
        }
        return true;
    }

    public void onClick(Widget sender) {
        int clicked = vp.getWidgetIndex(sender);
        //if(clicked != active){
            if(active > -1){
                if(!ctrl){
                	if(!multi){
                		((ScreenWidget)vp.getWidget(active)).removeStyleName("Highlighted");
                    	selected.remove(dm.get(start+active));
                	}
                }   
            }
            if(multi && ((ScreenWidget)vp.getWidget(clicked)).getStyleName().indexOf("Highlighted") != -1){
            	((ScreenWidget)vp.getWidget(clicked)).removeStyleName("Highlighted");
            	selected.remove(dm.get(start+clicked));
            }else{
            	active = clicked;
            	((ScreenWidget)vp.getWidget(active)).addStyleName("Highlighted");
            	selected.add(dm.get(start+clicked));
            }
            changeListeners.fireChange(this);
      //  }
    }
    
    public void unselectAll() {
        selected.clear();
        for(int i=0; i<vp.getWidgetCount(); i++){
        	((ScreenWidget)vp.getWidget(0)).removeStyleName("Highlighted");
        }        
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
    
    
}
