package org.openelis.gwt.client.screen;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.dnd.DragListener;
import com.google.gwt.user.client.dnd.DropListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.client.services.ScreenServiceInt;
import org.openelis.gwt.client.services.ScreenServiceIntAsync;
import org.openelis.gwt.client.widget.WidgetMap;
import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.FormRPC;

import java.util.HashMap;
import java.util.Iterator;
/**
 * The Screen class is the base class for displaying a screen 
 * drawn on the client machine.  It can also validate input and
 * and return data to the servlet.  This class is compiled into 
 * javascript and run on the client.
 * 
 * @author tschmidt
 *
 */
public class Screen extends Composite implements
                                     FocusListener,
                                     ChangeListener,
                                     ClickListener,
                                     TabListener,
                                     DragListener,
                                     DropListener,
                                     TreeListener,
                                     MouseListener,
                                     PopupListener{
	/**
	 * All drawn widgets will be held in this panel.
	 */
    protected VerticalPanel panel = new VerticalPanel();
    public FormRPC rpc;
    /** 
     * All widgets drawn on screen are referenced in this
     * HashMap
     */
    public HashMap widgets = new HashMap();
    public String xmlUrl;
    protected Document xml;
    public HashMap errors = new HashMap();
    protected HashMap tabOrder = new HashMap();
    protected HashMap tabBack = new HashMap();
    public ConstantsWithLookup constants;
    private ScreenServiceIntAsync screenService = (ScreenServiceIntAsync)GWT.create(ScreenServiceInt.class);
    private ServiceDefTarget target = (ServiceDefTarget)screenService;
    public boolean keep;
    /**
     * This field contains all widgets available to this application
     */
    private static WidgetMap WIDGET_MAP;

    /**
     * No arg constructor will initiate a blank panel and new FormRPC 
     */
    public Screen() {
        initWidget(panel);
        rpc = new FormRPC();
    }

    /**
     * This constructor takes a file name as a string which points to 
     * the xml definition of this screen on the server.
     * @param xmlUrl
     */
    public Screen(String xmlUrl) {
        this();
        this.xmlUrl = xmlUrl;
    }

    /**
     * This method will set the static WidgetMap used to access the 
     * available widgets for this class that will be defined in the 
     * xml
     * @param wids
     */
    public static void setWidgetMap(WidgetMap wids) {
        WIDGET_MAP = wids;
    }

    /**
     * Returns the static Map of widgets that are used by the application
     * @return
     */
    public static WidgetMap getWidgetMap() {
        return WIDGET_MAP;
    }

    /**
     * This method will return the widget being wrapped by the ScreenWidget
     * wth given name parameter
     * @param name
     * @return
     */
    public Widget getWidget(String name) {
        return ((ScreenWidget)widgets.get(name)).getWidget();
    }

    /**
     * This method is used to init the serviceUrl to be used by the form.
     * 
     * @param url
     */
    public void initService(String url) {
        String base = GWT.getModuleBaseURL();
        base += url;
        target.setServiceEntryPoint(base);
    }

    /**
     * This method will call the defined ScreenServiceURL and on 
     * return load the screen from the RPC returned.  It will then
     * call the afterSubmit method or the afterQuery method.  
     * 
     * @param method
     */
    protected void callService(final String method) {
        if(method.equals("query")){
            screenService.query(rpc, new AsyncCallback() {
                public void onSuccess(Object result) {
                    afterQuery(result,true);
                }
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                    afterQuery(null,false);
                }
            });
        }else{
            screenService.action(rpc, new AsyncCallback() {
                public void onSuccess(Object result) {
                    rpc = (FormRPC)result;
                    load();
                    afterSubmit(method, true);
                }
                
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                    afterSubmit(method, false);
                }
            });
        }
    }

    /**
     * This method is called after a call to the servlet.  It should be 
     * overridden by the extending class to provide functionality for a 
     * screen.
     * 
     * @param method
     * @param success
     */
    public void afterSubmit(String method, boolean success) {

    }

    /**
     * This method is called after a call to the servlet for a form query.
     * It should be overridden by the extending class to provide functionality
     * for a screen.
     * 
     * @param result
     * @param success
     */
    public void afterQuery(Object result, boolean success){
        
    }
    
    /**
     * This method can be overridden for proccesing onChange events triggered
     * from the screen.
     * 
     * @param sender
     */
    public void onChange(Widget sender) {
        // TODO Auto-generated method stub

    }

    /**
     * This method will put together the screen from the xml definition.
     * It will call the afterSubmit method when done with a method of "draw"
     * 
     */
    protected void draw() {
        try {
            Node display = xml.getElementsByTagName("display").item(0);
            if (display.getAttributes().getNamedItem("constants") != null) {
                constants = (ConstantsWithLookup)WIDGET_MAP.get(display.getAttributes().getNamedItem("constants").getNodeValue());
            }
            NodeList widgets = display.getChildNodes();
            for (int i = 0; i < widgets.getLength(); i++) {
                if (widgets.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Widget wid = WIDGET_MAP.getWidget(widgets.item(i), this);
                    panel.add(wid);
                }
            }
            panel.setStyleName("Screen"); 
        } catch (Exception e) {
            Window.alert("draw " + e.getMessage());
        }
    }

    /**
     * This method will load the form with data provided by the callService
     * method and extract it from the FormRPC.
     * 
     */
    protected void load() {
        try {
            Iterator inputKeys = widgets.keySet().iterator();
            while (inputKeys.hasNext()) {
                String key = (String)inputKeys.next();
                if (!rpc.getFieldMap().containsKey(key))
                    continue;
                AbstractField rpcField = rpc.getField(key);
                ScreenWidget inputField = (ScreenWidget)widgets.get(key);
                inputField.load(rpcField);
            }
        } catch (Exception e) {
            Window.alert("Load " + e.getMessage());
        }
    }

    /**
     * This method will draw any validation errors the form may have
     * 
     */
    protected void drawErrors() {
        clearErrors();
        Iterator rpcKeys = rpc.getFieldMap().keySet().iterator();
        while (rpcKeys.hasNext()) {
            String key = (String)rpcKeys.next();
            AbstractField field = rpc.getField(key);
            setError(new Object[] {field.getErrors()}, key);
        }
    }

    /**
     * This method puts validation errors into a vertical panel to be displayed on the
     * screen for widgets of type error.
     * 
     * @param fields
     * @param key
     */
    protected void setError(Object[] fields, String key) {
        VerticalPanel errPanel = new VerticalPanel();
        errPanel.setStyleName("ScreenError");
        for (int i = 0; i < fields.length; i++) {
            String[] errors = (String[])fields[i];
            for (int j = 0; j < errors.length; j++) {
                String error = (String)errors[j];
                errPanel.add(new HTML(error));
            }
        }
        if (errPanel.getWidgetCount() > 0) {
            Object errLoc = errors.get(key);
            if (errLoc instanceof Panel) {
                VerticalPanel vp = (VerticalPanel)errLoc;
                vp.add(errPanel);
                vp.setVisible(true);
            } else {
                com.google.gwt.user.client.Element el = (com.google.gwt.user.client.Element)errLoc;
                String tableId = DOM.getAttribute(el, "table");
                FlexTable table = (FlexTable)getWidget(tableId);
                int row = DOM.getIntAttribute(el, "row");
                table.setWidget(row, 1, errPanel);
                table.getRowFormatter().setVisible(row, true);
            }
        }
    }

    /**
     * This method can be overridden by an extending class to provide more
     * specific validation logic if necessary.  
     * 
     * @return
     */
    protected boolean validate() {
        return true;
    }

    /**
     * This method will pull information inputed by the user and prepare it to
     * be sent back to the server for processing
     * 
     */
    protected void doSubmit() {
        String key = "";
        try {
            rpc.reset();
            Iterator inputKeys = widgets.keySet().iterator();
            while (inputKeys.hasNext()) {
                key = (String)inputKeys.next();
                if (!rpc.getFieldMap().containsKey(key)) {
                    continue;
                }
                AbstractField rpcField = (AbstractField)rpc.getField(key);
                ScreenWidget inputField = (ScreenWidget)widgets.get(key);
                inputField.submit(rpcField);
            }
        } catch (Exception e) {
            Window.alert(key + e.getMessage());
        }
    }

    /**
     * This method will enable or disable all input widgets on the screen depending on
     * the value of the parameter enabled
     * 
     * @param enabled
     */
    protected void enable(boolean enabled) {
        Iterator wids = widgets.values().iterator();
        while (wids.hasNext()) {
            Widget wid = (Widget)wids.next();
            if(wid instanceof ScreenWidget)
                ((ScreenWidget)wid).enable(enabled);
        }
    }

    /**
     * Implementation of the onFocus method from FocusListener. Any widget that adds
     * the Screen as a FocusListener will call this method when focused. May be overridden
     * by the extending class to change the default behavior.
     */
    public void onFocus(Widget sender) {
        sender.addStyleName("focused");
    }

    /**
     * Implementation of the onFocus method from FocusListener. Any widget that adds
     * the Screen as a FocusListener will call this method when focus is lost. May be overridden
     * by the extending class to change the default behavior.
     */
    public void onLostFocus(Widget sender) {
        sender.removeStyleName("focused");
    }

    /** 
     * this method is called when the screen is to be drawn using the xmlUrl defined by the 
     * constructor call
     */
    public void getXML(){
        getXML(xmlUrl);
    }
    
    /**
     * This method will retrieve the XML definition of this form from the
     * server. All forms are expected to be in a Forms directory under the
     * public path.  It will then call the draw() method after parsing the 
     * xml to display the screen.  It will create the FormRPC fields form
     * the parsed XML's RPC section. It also loads any default values for fields 
     * defined in the xml.  afterSubmit("draw",true) is called at the end of this
     * method so any custom initialization can be done by an extending class.
     * 
     * @param name
     */
    public void getXML(String name) {
        String url = "";
        if(name.indexOf(".xml") > -1)
            url = "Forms/"+name;
        else
            url = "Forms?name="+name;
        HTTPRequest.asyncGet(url, new ResponseTextHandler() {
            public void onCompletion(String response) {
                xml = XMLParser.parse(response);
                draw();
                try {
                    NodeList rpcList = xml.getDocumentElement()
                                          .getElementsByTagName("rpc");
                    Element rpcEl = (Element)rpcList.item(0);
                    NodeList fieldList = rpcEl.getChildNodes();
                    HashMap map = new HashMap();
                    for (int i = 0; i < fieldList.getLength(); i++) {
                        if (fieldList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                            AbstractField field = Screen.getWidgetMap()
                                                        .getField(fieldList.item(i));
                            map.put((String)field.getKey(), field);
                        }
                    }
                    rpc.setFieldMap(map);
                } catch (Exception e) {
                    Window.alert("FormUtil: " + e.getMessage());
                }
                load();
                if (xml.getDocumentElement().getAttribute("serviceUrl") != null) {
                    String url = xml.getDocumentElement()
                                    .getAttribute("serviceUrl");
                    initService(url);
                }
                afterSubmit("draw", true);
            }
        });
    }

    /**
     * This method will reset all input fields to default null value.
     * 
     */
    protected void doReset() {
        Iterator rpcKeys = rpc.getFieldMap().keySet().iterator();
        while (rpcKeys.hasNext()) {
            String key = (String)rpcKeys.next();
            rpc.setFieldValue(key, null);
        }
        load();
    }

    /**
     * This method will clear all displayed errors in the form.
     * 
     */
    protected void clearErrors() {
        Iterator errorIt = errors.keySet().iterator();
        while (errorIt.hasNext()) {
            Object errLoc = errors.get((String)errorIt.next());
            if (errLoc instanceof Panel) {
                ((Panel)errLoc).clear();
                ((Panel)errLoc).setVisible(false);
            } else {
                com.google.gwt.user.client.Element el = (com.google.gwt.user.client.Element)errLoc;
                String tableId = DOM.getAttribute(el, "table");
                FlexTable table = (FlexTable)getWidget(tableId);
                int row = DOM.getIntAttribute(el, "row");
                if (table.isCellPresent(row, 1))
                    table.clearCell(row, 1);
                table.getRowFormatter().setVisible(row, false);
            }
        }
    }

    /**
     * This method is used to control the Tab order of input fields defined in
     * the form XML.  It is called when tab is pressed on widgets that have been 
     * defined to do so by overriding it's onBrowser(Event event) method during
     * declaration.  
     * 
     * @param event
     * @param wid
     */
    public void doTab(Event event, Widget wid) {
        Object obj = null;
        if (event != null && DOM.eventGetShiftKey(event))
            obj = widgets.get((String)tabBack.get(wid));
        else
            obj = widgets.get((String)tabOrder.get(wid));
        if (obj != null) {
            boolean tabbed = false;
            while (!tabbed) {
                if (((Widget)obj).isVisible()) {
                    tabbed = true;
                    ((ScreenWidget)obj).setFocus(true);
                } else {
                    if (event != null && DOM.eventGetShiftKey(event))
                        obj = widgets.get((String)tabBack.get(obj));
                    else
                        obj = widgets.get((String)tabOrder.get(obj));
                }
            }
            if(event != null){
                DOM.eventCancelBubble(event, true);
                DOM.eventPreventDefault(event);
            }
        }
    }

    /**
     * This method adds the input field to the form tab order.
     * 
     * @param on
     * @param to
     */
    public void addTab(Widget on, String[] to) {
        tabOrder.put(on, to[0]);
        if (to.length > 1)
            tabBack.put(on, to[1]);
    }

    /**
     * This stub can be overridden to handle click events generated by widgets on
     * the screen.
     */
    public void onClick(Widget sender) {
    }

    /**
     * This stub can be overridden to handle onTabSelected events from a Tab
     * Panel
     */
    public void onTabSelected(SourcesTabEvents sources, int index) {
    }

    /**
     * This stub can be overridden to handle onBeforeTabSelected events from a
     * Tab Panel
     */
    public boolean onBeforeTabSelected(SourcesTabEvents sources, int index) {
        return true;
    }

    /**
     * This stub can be overridden to handle onTreeItemSelected events generated from 
     * a Tree on the Screen.
     */
    public void onTreeItemSelected(TreeItem item) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onTreeItemStateChanged events generated from
     * a Tree on the Screen. 
     */
    public void onTreeItemStateChanged(TreeItem item) {

    }

    /**
     * This stub can be overridden to handle onDragDropEnd events generated form
     * widgets on the Screen. 
     */
    public void onDragDropEnd(Widget sender, Widget target) {

    }

    /**
     * This stub can be overridden to handle onDragEnd events generated from  
     * widgets on the Screen.
     */
    public void onDragEnd(Widget sender, int x, int y) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onDragEnter events generated from 
     * widgets on the Screen. 
     *
     */
    public void onDragEnter(Widget sender, Widget target) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onDragExit events generated from 
     * widgets on the Screen.
     */
    public void onDragExit(Widget sender, Widget target) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onDragMouseMoved events generated from
     * widget on the Screen.
     */
    public void onDragMouseMoved(Widget sender, int x, int y) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onDragOver events generated from
     * widgets on the Screen.
     */
    public void onDragOver(Widget sender, Widget target) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onDragStart events generated from 
     * widgets on the Screen.
     */
    public void onDragStart(Widget sender, int x, int y) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onDrop events generated from
     * widgets on the Screen.
     */
    public void onDrop(Widget sender, Widget source) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onDropEnter events generated from
     * widgets on the Screen.
     */
    public void onDropEnter(Widget sender, Widget source) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onDropExit events generated from
     * widgets on the screen 
     */
    public void onDropExit(Widget sender, Widget source) {
        // TODO Auto-generated method stub

    }
    
    /**
     * This stub can be overridden to handle onDropOver events generated from
     * widgets on the screen 
     */
    public void onDropOver(Widget sender, Widget source) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onMouseDown events generated from
     * widgets on the screen 
     */
    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onMouseEnter events generated from
     * widgets on the screen 
     */
    public void onMouseEnter(Widget sender) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onMouseLeave events generated from
     * widgets on the screen 
     */
    public void onMouseLeave(Widget sender) {
        // TODO Auto-generated method stub

    }
    
    /**
     * This stub can be overridden to handle onMouseMove events generated from
     * widgets on the screen 
     */
    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub

    }

    /**
     * This stub can be overridden to handle onMouseUp events generated from
     * widgets on the screen 
     */
    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub

    }
    
    protected void onDetach() {
        // TODO Auto-generated method stub
        if(!keep){
            Iterator wids = widgets.values().iterator();
            while (wids.hasNext()) {
                Widget wid = (Widget)wids.next();
                if(wid instanceof ScreenWidget)
                    ((ScreenWidget)wid).destroy();
            }
            widgets.clear();
            widgets = null;
            xmlUrl = null;
            xml = null;
            errors.clear();
            errors = null;
            tabOrder.clear();
            tabOrder = null;
            tabBack.clear();
            tabBack = null;
            constants = null;
            screenService = null;
            target = null;
            rpc = null;
        }
        super.onDetach();
    }

	public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
		// TODO Auto-generated method stub
		
	}
}
