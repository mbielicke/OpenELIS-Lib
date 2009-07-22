package org.openelis.gwt.screen.rewrite;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;

import org.openelis.gwt.common.DatetimeRPC;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.screen.ClassFactory;
import org.openelis.gwt.screen.ScreenMenuItem;
import org.openelis.gwt.screen.rewrite.Screen.State;
import org.openelis.gwt.services.ScreenService;
import org.openelis.gwt.widget.CalendarLookUp;
import org.openelis.gwt.widget.CollapsePanel;
import org.openelis.gwt.widget.EditBox;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.IconContainer;
import org.openelis.gwt.widget.MenuItem;
import org.openelis.gwt.widget.MenuPanel;
import org.openelis.gwt.widget.ScrollableTabBar;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.TitledPanel;
import org.openelis.gwt.widget.WindowBrowser;
import org.openelis.gwt.widget.diagram.Diagram;
import org.openelis.gwt.widget.rewrite.AppButton;
import org.openelis.gwt.widget.rewrite.AutoComplete;
import org.openelis.gwt.widget.rewrite.ButtonGroup;
import org.openelis.gwt.widget.rewrite.CheckBox;
import org.openelis.gwt.widget.rewrite.CheckField;
import org.openelis.gwt.widget.rewrite.CommandButton;
import org.openelis.gwt.widget.rewrite.DateField;
import org.openelis.gwt.widget.rewrite.DoubleField;
import org.openelis.gwt.widget.rewrite.Dropdown;
import org.openelis.gwt.widget.rewrite.Field;
import org.openelis.gwt.widget.rewrite.IntegerField;
import org.openelis.gwt.widget.rewrite.PassWordTextBox;
import org.openelis.gwt.widget.rewrite.RadioButton;
import org.openelis.gwt.widget.rewrite.ResultsTable;
import org.openelis.gwt.widget.rewrite.StringField;
import org.openelis.gwt.widget.rewrite.TextArea;
import org.openelis.gwt.widget.richtext.RichTextWidget;
import org.openelis.gwt.widget.table.rewrite.TableColumn;
import org.openelis.gwt.widget.table.rewrite.TableWidget;
import org.openelis.gwt.widget.table.rewrite.TableView.VerticalScroll;
import org.openelis.gwt.widget.tree.rewrite.TreeColumn;
import org.openelis.gwt.widget.tree.rewrite.TreeWidget;
import org.openelis.gwt.widget.tree.rewrite.TreeView;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class UIUtil {
	 
	 public static ScreenDef createWidgets(String xml) throws Exception{
		 ScreenDef def = new ScreenDef();
		 def.xmlDef = xml;
		 return UIUtil.createWidgets(def, false);
	 }
	 
	 public static ScreenDef createWidgets(ScreenDef def) throws Exception{
		 return createWidgets(def,true);
	 }
     
    public static ScreenDef createWidgets(ScreenDef def, boolean call) throws Exception{
    	String xmlDef = def.xmlDef;
    	if(call)
    		xmlDef = getScreen(def.loadURL);
    	Document doc  = XMLParser.parse(xmlDef);
    	Node screen = doc.getElementsByTagName("screen").item(0);
    	if(screen.getAttributes().getNamedItem("name") != null) {
    		def.name = screen.getAttributes().getNamedItem("name").getNodeValue();
    	}
        Node display = doc.getElementsByTagName("display").item(0);
        NodeList widgets = display.getChildNodes();
        for (int i = 0; i < widgets.getLength(); i++) {
            if (widgets.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = createWidget(widgets.item(i),def);
                if(widgets.item(i).getAttributes().getNamedItem("key") != null)
                	def.setWidget(wid, widgets.item(i).getAttributes().getNamedItem("key").getNodeValue());
                def.panel = wid;
                break;
            }
        }
        return def;
    }
    
    static String xmlRet = null;
    
    public static String getScreen(String loadURL) throws Exception{
        ScreenService service = new ScreenService(loadURL);
        return service.callString("getScreen");
    }
    
    public static Widget createWidget(Node node) {
    	return createWidget(node,null);
    }
    
    public static Widget createWidget(Node node, ScreenDef def) {
        String widName = node.getNodeName();
        Widget wid = (Widget)factoryMap.get(widName).getNewInstance(node, def);
        if(def != null && node.getAttributes().getNamedItem("key") != null) 
        	def.widgets.put(node.getAttributes().getNamedItem("key").getNodeValue(), wid);
        return wid;
    }
    
    private static Widget loadWidget(Node node, ScreenDef def){
        Node input = null;
        if (node.getNodeName().equals("widget")) {
            NodeList inputList = node.getChildNodes();
            for (int m = 0; m < inputList.getLength(); m++) {
                if (inputList.item(m).getNodeType() == Node.ELEMENT_NODE) {
                    input = inputList.item(m);
                    m = 100;
                }
            }
        } else
            input = node;
        return createWidget(input,def);
    }
    
    private static void setDefaults(Node node, Widget wid) {
        if (node.getAttributes().getNamedItem("style") != null){
            String[] styles = node.getAttributes().getNamedItem("style").getNodeValue().split(",");
            wid.setStyleName(styles[0]);
            for(int i = 1; i < styles.length; i++){
                wid.addStyleName(styles[i]);
            }
        }
        if (node.getAttributes().getNamedItem("width") != null)
            wid.setWidth(node.getAttributes()
                                     .getNamedItem("width")
                                     .getNodeValue());
        if (node.getAttributes().getNamedItem("height") != null)
            wid.setHeight(node.getAttributes()
                                      .getNamedItem("height")
                                      .getNodeValue());
        if (node.getAttributes().getNamedItem("tip") != null){
            wid.setTitle(node.getAttributes().getNamedItem("tip").getNodeValue());
        }
        if (node.getAttributes().getNamedItem("visible") != null){
            if(node.getAttributes().getNamedItem("visible").getNodeValue().equals("false"))
                wid.setVisible(false);
        }
    }
    
    private interface Factory<T> {
    	public T getNewInstance(Node node, ScreenDef def);
    }
    
    public static class TabHandler implements KeyPressHandler {
    	String next;
    	String prev;
    	ScreenDef def;
    	
    	public TabHandler(Node node, ScreenDef def) {
    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
			String[] tabs = tab.split(",");
    		next = tabs[0];
    		prev = tabs[1];
    		this.def = def;
    	}
    	
		public void onKeyPress(KeyPressEvent event) {
			if(event.getNativeEvent().getKeyCode() == KeyboardHandler.KEY_TAB){
				if(event.isShiftKeyDown()){
					if(((HasField)def.getWidget(prev)).isEnabled()) 
						((Focusable)def.getWidget(prev)).setFocus(true);
					else
						KeyPressEvent.fireNativeEvent(event.getNativeEvent(), (HasHandlers)def.getWidget(prev));
				}else{
					if(((HasField)def.getWidget(next)).isEnabled()) 
						((Focusable)def.getWidget(next)).setFocus(true);
					else
						KeyPressEvent.fireNativeEvent(event.getNativeEvent(), (HasHandlers)def.getWidget(next));
				}
				event.preventDefault();
				event.stopPropagation();
			}
		}
    }
    
    private static class UIFocusHandler implements FocusHandler,BlurHandler {

		public void onFocus(FocusEvent event) {
			((Widget)event.getSource()).addStyleName("Focus");
		}

		public void onBlur(BlurEvent event) {
			((Widget)event.getSource()).removeStyleName("Focus");
		}
    	
    }
    
    public static UIFocusHandler focusHandler = new UIFocusHandler();
    
    private static HashMap<String,Factory> factoryMap = new HashMap<String,Factory>();
    static {
    	factoryMap.put("textbox",new Factory<TextBox>() {
			public TextBox getNewInstance(Node node, ScreenDef def) {
				TextBox textbox = new TextBox();
				if(node.getAttributes().getNamedItem("tab") != null) 
					textbox.addTabHandler(new TabHandler(node,def));
				if (node.getAttributes().getNamedItem("shortcut") != null)
					textbox.setAccessKey(node.getAttributes()
							.getNamedItem("shortcut")
							.getNodeValue()
							.charAt(0));
				textbox.setStyleName("ScreenTextBox");
				if (node.getAttributes().getNamedItem("case") != null){
					String fieldCase = node.getAttributes().getNamedItem("case")
					.getNodeValue().toUpperCase();
					textbox.setCase(TextBox.Case.valueOf(fieldCase));
	        	}

				if (node.getAttributes().getNamedItem("max") != null) {
					int length = Integer.parseInt(node.getAttributes().getNamedItem("max").getNodeValue());
					textbox.setLength(length);
				}
	        
				if (node.getAttributes().getNamedItem("textAlign") != null) {
					String align = node.getAttributes().getNamedItem("textAlign").getNodeValue();
					if(align.equalsIgnoreCase("center"))
						textbox.alignment = TextBox.ALIGN_CENTER;
					if(align.equalsIgnoreCase("right"))
						textbox.alignment = TextBox.ALIGN_RIGHT;
					if(align.equalsIgnoreCase("left"))   
						textbox.alignment = TextBox.ALIGN_LEFT;
					textbox.setTextAlignment(textbox.alignment);
				}
	        
				if (node.getAttributes().getNamedItem("autoNext") != null){
					if(node.getAttributes().getNamedItem("autoNext").getNodeValue().equals("true")){
						textbox.autoNext = true;
					}
				}
				
				if (node.getAttributes().getNamedItem("mask") != null) {
					String mask = node.getAttributes().getNamedItem("mask").getNodeValue();
					textbox.setMask(mask);
				}
				setDefaults(node,textbox);
				if (node.getAttributes().getNamedItem("field") != null) {
					textbox.setField((Field)factoryMap.get(node.getAttributes().getNamedItem("field").getNodeValue()).getNewInstance(node, null));
				}else
					textbox.setField((StringField)factoryMap.get("String").getNewInstance(node, null));
				textbox.addBlurHandler(focusHandler);
				textbox.addFocusHandler(focusHandler);
				return textbox;
			}
    	});
    	factoryMap.put("VerticalPanel", new Factory<VerticalPanel>() {
    		public VerticalPanel getNewInstance(Node node, ScreenDef def) {
    			VerticalPanel panel = new VerticalPanel();
    	        if (node.getAttributes().getNamedItem("spacing") != null)
    	            panel.setSpacing(Integer.parseInt(node.getAttributes()
    	                                                  .getNamedItem("spacing")
    	                                                  .getNodeValue()));
  
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	                Widget wid = loadWidget(widgets.item(k),def);
    	                panel.add(wid);
    	                if (widgets.item(k).getAttributes().getNamedItem("halign") != null) {
    	                    String align = widgets.item(k).getAttributes()
    	                                         .getNamedItem("halign")
    	                                         .getNodeValue();
    	                    if (align.equals("right"))
    	                        panel.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_RIGHT);
    	                    if (align.equals("left"))
    	                        panel.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_LEFT);
    	                    if (align.equals("center"))
    	                        panel.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_CENTER);
    	                }
    	                if (widgets.item(k).getAttributes().getNamedItem("valign") != null) {
    	                    String align = widgets.item(k).getAttributes()
    	                                         .getNamedItem("valign")
    	                                         .getNodeValue();
    	                    if (align.equals("top"))
    	                        panel.setCellVerticalAlignment(wid, HasAlignment.ALIGN_TOP);
    	                    if (align.equals("middle"))
    	                        panel.setCellVerticalAlignment(wid, HasAlignment.ALIGN_MIDDLE);
    	                    if (align.equals("bottom"))
    	                        panel.setCellVerticalAlignment(wid, HasAlignment.ALIGN_BOTTOM);
    	                }
    	            }
    	        }
    	        panel.setStyleName("ScreenPanel");
    	        setDefaults(node,panel);
    			return panel;
    		}
    	});
    	factoryMap.put("TablePanel", new Factory<FlexTable>() {
    		public FlexTable getNewInstance(Node node, ScreenDef def) {
    			FlexTable panel = new FlexTable();
    	        if (node.getAttributes().getNamedItem("visible") != null && node.getAttributes().getNamedItem("visible").getNodeValue().equals("false")){
    	            panel.setVisible(false);
    	        }
    	        if (node.getAttributes().getNamedItem("spacing") != null)
    	            panel.setCellSpacing(Integer.parseInt(node.getAttributes()
    	                                                      .getNamedItem("spacing")
    	                                                      .getNodeValue()));
    	        if (node.getAttributes().getNamedItem("padding") != null)
    	            panel.setCellPadding(Integer.parseInt(node.getAttributes()
    	                                                      .getNamedItem("padding")
    	                                                      .getNodeValue()));
    	        if (node.getAttributes().getNamedItem("style") != null){
    	            panel.addStyleName(node.getAttributes().getNamedItem("style").getNodeValue());
    	        }
    	        
    	        NodeList rows = ((Element)node).getElementsByTagName("row");
    	        for (int k = 0; k < rows.getLength(); k++) {
    	        	
    	            NodeList widgets = rows.item(k).getChildNodes();
    	            int w = -1;
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	                    w++;
    	                    Widget wid = loadWidget(widgets.item(l),def);
    	                    panel.setWidget(k, w, wid);
    	                    if (widgets.item(l).getAttributes().getNamedItem("colspan") != null)
    	                        panel.getFlexCellFormatter()
    	                             .setColSpan(k,
    	                                         w,
    	                                         Integer.parseInt(widgets.item(l)
    	                                                                 .getAttributes()
    	                                                                 .getNamedItem("colspan")
    	                                                                 .getNodeValue()));
    	                    if (widgets.item(l).getAttributes().getNamedItem("rowspan") != null)
    	                        panel.getFlexCellFormatter()
    	                             .setRowSpan(k,
    	                                         w,
    	                                         Integer.parseInt(widgets.item(l)
    	                                                                 .getAttributes()
    	                                                                 .getNamedItem("rowspan")
    	                                                                 .getNodeValue()));
    	                    if (widgets.item(l).getAttributes().getNamedItem("style") != null)
    	                        panel.getFlexCellFormatter()
    	                             .addStyleName(k,
    	                                           w,
    	                                           widgets.item(l)
    	                                                  .getAttributes()
    	                                                  .getNamedItem("style")
    	                                                  .getNodeValue());
    	                    if (widgets.item(l).getAttributes().getNamedItem("align") != null) {
    	                        String align = widgets.item(l)
    	                                              .getAttributes()
    	                                              .getNamedItem("align")
    	                                              .getNodeValue();
    	                        if (align.equals("right"))
    	                            panel.getFlexCellFormatter()
    	                                 .setHorizontalAlignment(k,
    	                                                         w,
    	                                                         HasAlignment.ALIGN_RIGHT);
    	                        if (align.equals("left"))
    	                            panel.getFlexCellFormatter()
    	                                 .setHorizontalAlignment(k,
    	                                                         w,
    	                                                         HasAlignment.ALIGN_LEFT);
    	                        if (align.equals("center"))
    	                            panel.getFlexCellFormatter()
    	                                 .setHorizontalAlignment(k,
    	                                                         w,
    	                                                         HasAlignment.ALIGN_CENTER);
    	                    }
    	                    if (widgets.item(l).getAttributes().getNamedItem("valign") != null) {
    	                        String align = widgets.item(l)
    	                                              .getAttributes()
    	                                              .getNamedItem("valign")
    	                                              .getNodeValue();
    	                        if (align.equals("top"))
    	                            panel.getFlexCellFormatter()
    	                                 .setVerticalAlignment(k,
    	                                                         w,
    	                                                         HasAlignment.ALIGN_TOP);
    	                        if (align.equals("bottom"))
    	                            panel.getFlexCellFormatter()
    	                                 .setVerticalAlignment(k,
    	                                                         w,
    	                                                         HasAlignment.ALIGN_BOTTOM);
    	                        if (align.equals("middle"))
    	                            panel.getFlexCellFormatter()
    	                                 .setVerticalAlignment(k,
    	                                                         w,
    	                                                         HasAlignment.ALIGN_MIDDLE);
    	                    }
    	                }
    	            }
    	            
    	            if (rows.item(k).getAttributes().getNamedItem("style") != null) {
    	            	panel.getRowFormatter().addStyleName(k, rows.item(k).getAttributes().getNamedItem("style").getNodeValue());
    	            }
    	        }
    	        setDefaults(node,panel);
    			return panel;
    		}
    	});
    	factoryMap.put("AbsolutePanel", new Factory<AbsolutePanel>() {
    		public AbsolutePanel getNewInstance(Node node, ScreenDef def) {
    			AbsolutePanel panel = new AbsolutePanel();
    	        panel.setStyleName("ScreenAbsolute");
    	        if(node.getAttributes().getNamedItem("overflow") != null)
    	            DOM.setStyleAttribute(panel.getElement(),"overflow",node.getAttributes().getNamedItem("overflow").getNodeValue());
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	                Widget wid = loadWidget(widgets.item(k),def);
    	                int x = -1;
    	                if (widgets.item(k).getAttributes().getNamedItem("x") != null)
    	                    x = Integer.parseInt(widgets.item(k)
    	                                                .getAttributes()
    	                                                .getNamedItem("x")
    	                                                .getNodeValue());
    	                int y = -1;
    	                if (widgets.item(k).getAttributes().getNamedItem("y") != null)
    	                    y = Integer.parseInt(widgets.item(k)
    	                                                .getAttributes()
    	                                                .getNamedItem("y")
    	                                                .getNodeValue());
    	                if(node.getAttributes().getNamedItem("align") != null)
    	                    DOM.setElementProperty(panel.getElement(),"align",node.getAttributes().getNamedItem("align").getNodeValue());
    	                panel.add(wid, x, y);
    	            }
    	        }
    	        setDefaults(node,panel);
    	        return panel;
    		}
    	});
    	factoryMap.put("ScrollPanel", new Factory<ScrollPanel>() {
    		public ScrollPanel getNewInstance(Node node, ScreenDef def) {
    			ScrollPanel panel = new ScrollPanel();
    	        panel.setStyleName("ScreenAbsolute");
    	        if(node.getAttributes().getNamedItem("overflow") != null)
    	            DOM.setStyleAttribute(panel.getElement(),"overflow",node.getAttributes().getNamedItem("overflow").getNodeValue());
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	                Widget wid = loadWidget(widgets.item(k),def);
    	                panel.setWidget(wid);
    	                break;
    	            }
    	        }
    	        setDefaults(node,panel);
    	        return panel;
    		}
    	});
    	factoryMap.put("buttonGroup", new Factory<ButtonGroup>() {
    		public ButtonGroup getNewInstance(Node node, ScreenDef def) {
    			ButtonGroup bgroup = new ButtonGroup();
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if(widgets.item(k).getNodeType() == Node.ELEMENT_NODE){
    	               Panel panel = (Panel)loadWidget(widgets.item(k),def);
    	               bgroup.setButtons(panel);
    	               break;
    	            }
    	        }
    	        setDefaults(node,bgroup);
    	        return bgroup;
    		}
    	});
    	factoryMap.put("calendar", new Factory<CalendarLookUp>() {
    		public CalendarLookUp getNewInstance(Node node, ScreenDef def) {
    			CalendarLookUp cal = new CalendarLookUp();
    			if(node.getAttributes().getNamedItem("tab") != null) 
    				cal.addTabHandler(new TabHandler(node,def));
    			byte begin = Byte.parseByte(node.getAttributes()
    					.getNamedItem("begin")
    					.getNodeValue());
    			byte end = Byte.parseByte(node.getAttributes()
    					.getNamedItem("end")
    					.getNodeValue());
    			if (node.getAttributes().getNamedItem("week") != null)
    				cal.init(begin,
    						end,
    						Boolean.valueOf(node.getAttributes()
    								.getNamedItem("week")
    								.getNodeValue())
    								.booleanValue());
    			else
    				cal.init(begin, end, false);
    			cal.setStyleName("ScreenCalendar");
    			setDefaults(node,cal);
    			cal.setField((DateField)factoryMap.get("Date").getNewInstance(node, null));
    			cal.addBlurHandler(focusHandler);
    			cal.addFocusHandler(focusHandler);
    			return cal;
    		}
    	});
    	factoryMap.put("check", new Factory<CheckBox>() {
    		public CheckBox getNewInstance(Node node, ScreenDef def) {
    			CheckBox check = new CheckBox();
    			if(node.getAttributes().getNamedItem("tab") != null) 
    				check.addTabHandler(new TabHandler(node,def));
    	        if(node.getAttributes().getNamedItem("threeState") != null){
    	            check.setType(CheckBox.CheckType.THREE_STATE);
    	            //defaultType = CheckBox.CheckType.THREE_STATE;
    	        }
    	        if (node.getFirstChild() != null){
    	             check.setText(node.getFirstChild().getNodeValue());
    	        }
    	        if (node.getChildNodes().getLength() > 0){
    	            NodeList widgets = node.getChildNodes();
    	            for (int k = 0; k < widgets.getLength(); k++) {
    	                if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	                    Widget wid = loadWidget(widgets.item(k),def);
    	                    check.setWidget(wid);
    	                }
    	            }
    	        }
    	        //check.setStyleName("ScreenCheck");
    	        setDefaults(node, check);
    	        check.setField((CheckField)factoryMap.get("Check").getNewInstance(node, null));
    	        check.addBlurHandler(focusHandler);
    	        check.addFocusHandler(focusHandler);
    	        return check;
    		}
    	});
    	factoryMap.put("CollapsePanel", new Factory<CollapsePanel>() {
    		public CollapsePanel getNewInstance(Node node, ScreenDef def) {
    			CollapsePanel panel = new CollapsePanel();
    		    if (node.getChildNodes().getLength() > 0){
    		       NodeList widgets = node.getChildNodes();
    		       for (int k = 0; k < widgets.getLength(); k++) {
    		           if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    		               Widget wid = loadWidget(widgets.item(k),def);
    		               wid.setHeight("100%");
    		               wid.setWidth("auto");
    		               panel.setContent(wid);
    		           }
    		       }
    		    }
    		    if(node.getAttributes().getNamedItem("height") != null)
    		       panel.setHeight(node.getAttributes().getNamedItem("height").getNodeValue());
    		       
    		    setDefaults(node, panel);
    		    return panel;
    		}
    	});
    	factoryMap.put("Deck", new Factory<DeckPanel>() {
    		public DeckPanel getNewInstance(Node node, ScreenDef def) {
    			DeckPanel panel = new DeckPanel();
    	        panel.setStyleName("ScreenDeck");
    	        NodeList decks = ((Element)node).getElementsByTagName("deck");
    	        for (int k = 0; k < decks.getLength(); k++) {
    	            NodeList widgets = decks.item(k).getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	                    Widget wid = loadWidget(widgets.item(l),def);
    	                    panel.add(wid);
    	                }
    	            }
    	        }
    	        panel.showWidget(0);
    	        setDefaults(node, panel);
    	        return panel;
    		}
    	});
    	factoryMap.put("diagram", new Factory<Diagram>() {
    		public Diagram getNewInstance(Node node, ScreenDef def) {
    			Diagram diagram = new Diagram();
    	        setDefaults(node, diagram);
    	        return diagram;
    		}
    	});
    	factoryMap.put("disclosure", new Factory<DisclosurePanel>() {
    		public DisclosurePanel getNewInstance(Node node, ScreenDef def){
    			DisclosurePanel panel = new DisclosurePanel();
    	        panel.setStyleName("ScreenDisclosure");
    	        Element header = (Element) ((Element)node).getElementsByTagName("header").item(0);
    	        NodeList widgets = header.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	                Widget wid = loadWidget(widgets.item(k),def);
    	                panel.setHeader(wid);
    	            }
    	        }
    	        Element content = (Element) ((Element)node).getElementsByTagName("content").item(0);
    	        widgets = content.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	                Node input = null;
    	                if (widgets.item(k).getNodeName().equals("widget")) {
    	                    NodeList inputList = widgets.item(k).getChildNodes();
    	                    for (int m = 0; m < inputList.getLength(); m++) {
    	                        if (inputList.item(m).getNodeType() == Node.ELEMENT_NODE) {
    	                            input = inputList.item(m);
    	                            m = 100;
    	                        }
    	                    }
    	                } else
    	                    input = widgets.item(k);
    	                Widget wid = createWidget(input,def);
    	                panel.setContent(wid);
    	            }
    	        }
    	        setDefaults(node, panel);
    	        return panel;
    		}
    	});
    	factoryMap.put("DockPanel", new Factory<DockPanel>() {
    		public DockPanel getNewInstance(Node node, ScreenDef def){
    			DockPanel panel = new DockPanel();
    	        panel.addStyleName("ScreenDock");
    	        if (node.getAttributes().getNamedItem("spacing") != null)
    	            panel.setSpacing(Integer.parseInt(node.getAttributes()
    	                                                  .getNamedItem("spacing")
    	                                                  .getNodeValue()));
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	                Widget wid = loadWidget(widgets.item(k),def);
    	                String dir = widgets.item(k)
    	                                    .getAttributes()
    	                                    .getNamedItem("dir")
    	                                    .getNodeValue();
    	                if (dir.equals("north"))
    	                    panel.add(wid, DockPanel.NORTH);
    	                else if (dir.equals("south"))
    	                    panel.add(wid, DockPanel.SOUTH);
    	                else if (dir.equals("east"))
    	                    panel.add(wid, DockPanel.EAST);
    	                else if (dir.equals("west"))
    	                    panel.add(wid, DockPanel.WEST);
    	                else if (dir.equals("center"))
    	                    panel.add(wid, DockPanel.CENTER);
    	                if (widgets.item(k).getAttributes().getNamedItem("halign") != null) {
    	                    String align = widgets.item(k)
    	                                          .getAttributes()
    	                                          .getNamedItem("align")
    	                                          .getNodeValue();
    	                    if (align.equals("right"))
    	                        panel.setCellHorizontalAlignment(wid,
    	                                                         HasAlignment.ALIGN_RIGHT);
    	                    if (align.equals("left"))
    	                        panel.setCellHorizontalAlignment(wid,
    	                                                         HasAlignment.ALIGN_LEFT);
    	                    if (align.equals("center"))
    	                        panel.setCellHorizontalAlignment(wid,
    	                                                         HasAlignment.ALIGN_CENTER);
    	                }
    	                if (widgets.item(k).getAttributes().getNamedItem("valign") != null) {
    	                    String align = widgets.item(k)
    	                                          .getAttributes()
    	                                          .getNamedItem("valign")
    	                                          .getNodeValue();
    	                    if (align.equals("top"))
    	                        panel.setCellVerticalAlignment(wid,
    	                                                       HasAlignment.ALIGN_TOP);
    	                    if (align.equals("middle"))
    	                        panel.setCellVerticalAlignment(wid,
    	                                                       HasAlignment.ALIGN_MIDDLE);
    	                    if (align.equals("bottom"))
    	                        panel.setCellVerticalAlignment(wid,
    	                                                       HasAlignment.ALIGN_BOTTOM);
    	                }
    	               
    	            }
    	        }
    	        setDefaults(node, panel);
    	        return panel;
    		}
    	});
    	factoryMap.put("editbox",new Factory<EditBox>() {
    		public EditBox getNewInstance(Node node, ScreenDef def) {
    			EditBox box = new EditBox();
    			Field field = (StringField)factoryMap.get("String").getNewInstance(node, def);
    			
    			setDefaults(node,box);
    			return box;
    		}
    	});
    	factoryMap.put("HorizontalPanel", new Factory<HorizontalPanel>() {
    		public HorizontalPanel getNewInstance(Node node, ScreenDef def) {
    			HorizontalPanel panel = new HorizontalPanel();
    			if (node.getAttributes().getNamedItem("spacing") != null)
    				panel.setSpacing(Integer.parseInt(node.getAttributes()
    						.getNamedItem("spacing")
    						.getNodeValue()));
    			NodeList widgets = node.getChildNodes();
    			for (int k = 0; k < widgets.getLength(); k++) {
    				if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    					Widget wid = loadWidget(widgets.item(k),def);
    					panel.add(wid);
    					if (widgets.item(k).getAttributes().getNamedItem("halign") != null) {
    						String align = widgets.item(k).getAttributes()
    						.getNamedItem("halign")
    						.getNodeValue();
    						if (align.equals("right"))
    							panel.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_RIGHT);
    						if (align.equals("left"))
    							panel.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_LEFT);
    						if (align.equals("center"))
    							panel.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_CENTER);
    					}
    					if (widgets.item(k).getAttributes().getNamedItem("valign") != null) {
    						String align = widgets.item(k).getAttributes()
    						.getNamedItem("valign")
    						.getNodeValue();
    						if (align.equals("top"))
    							panel.setCellVerticalAlignment(wid, HasAlignment.ALIGN_TOP);
    						if (align.equals("middle"))
    							panel.setCellVerticalAlignment(wid, HasAlignment.ALIGN_MIDDLE);
    						if (align.equals("bottom"))
    							panel.setCellVerticalAlignment(wid, HasAlignment.ALIGN_BOTTOM);
    					}
    				}
    			}
    			panel.setStyleName("ScreenPanel");
    			setDefaults(node, panel);
    			return panel;
    		}
    	});
    	factoryMap.put("HorizontalSplitPanel", new Factory<HorizontalSplitPanel>() {
    		public HorizontalSplitPanel getNewInstance(Node node, ScreenDef def) {
    			final HorizontalSplitPanel hp = new HorizontalSplitPanel();
    	        hp.setStyleName("ScreenSplit");
    	        NodeList sections = ((Element)node).getElementsByTagName("section");
    	        for (int k = 0; k < sections.getLength(); k++) {
    	            NodeList widgets = sections.item(k).getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	                    Widget wid = loadWidget(widgets.item(l),def);
    	                    if (k == 0) {
    	                            hp.setLeftWidget(wid);
    	                    }
    	                    if (k == 1) {
    	                            hp.setRightWidget(wid);
    	                    }
    	                }
    	            }
    	        }
    	        if (node.getAttributes().getNamedItem("splitpos") != null) {
    	            String splitpos = node.getAttributes()
    	                                  .getNamedItem("splitpos")
    	                                  .getNodeValue();
    	                hp.setSplitPosition(splitpos);
    	        }
    	                
    	        setDefaults(node, hp);
    	        return hp;
    		}
    	});
    	factoryMap.put("html", new Factory<HTML>() {
    		public HTML getNewInstance(Node node, ScreenDef def) {
    			HTML html = new HTML();
    	        if(node.getFirstChild() != null)
    	            html.setHTML(node.getFirstChild().getNodeValue());
    	        html.setStyleName("ScreenHTML");
    	        setDefaults(node, html);
    	        return html;
    		}
    	});
    	factoryMap.put("label", new Factory<org.openelis.gwt.widget.Label>() {
    		public org.openelis.gwt.widget.Label getNewInstance(Node node, ScreenDef def) {
    			org.openelis.gwt.widget.Label label = new org.openelis.gwt.widget.Label();
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
    	        if (node.hasChildNodes())
    	            label.setText((node.getFirstChild().getNodeValue()));
    	        label.setStyleName("ScreenLabel");
    	        setDefaults(node, label);
    	        label.addValueChangeHandler((StringField)factoryMap.get("String").getNewInstance(node, null));
    	        return label;
    		}
    	});
    	factoryMap.put("menuItem", new Factory<MenuItem>() {
    		public MenuItem getNewInstance(Node node, ScreenDef def) {
    			MenuItem item = new MenuItem();
    			Widget wid = null;
    			String label;
    	        if(((Element)node).getElementsByTagName("menuDisplay").getLength() > 0 &&  ((Element)node).getElementsByTagName("menuDisplay").item(0).getParentNode().equals(node)){
    	            NodeList displayList = ((Element)node).getElementsByTagName("menuDisplay").item(0).getChildNodes();
    	            int i = 0; 
    	            while(displayList.item(i).getNodeType() != Node.ELEMENT_NODE)
    	                i++;
    	            wid = loadWidget(displayList.item(i),def);
    	            item.init(wid);
    	        }else if(node.getAttributes().getNamedItem("header") != null){
    	            wid = MenuItem.createTableHeader("", new Label(node.getAttributes().getNamedItem("label").getNodeValue()));
    	            item.init(wid);
    	        }else{
    	            item.init(node.getAttributes().getNamedItem("icon").getNodeValue(), 
    	                                         node.getAttributes().getNamedItem("label").getNodeValue(), 
    	                                         node.getAttributes().getNamedItem("description").getNodeValue());
    	            label = node.getAttributes().getNamedItem("label").getNodeValue();
    	        }

    	        if (node.getAttributes().getNamedItem("class") != null){
    	            item.objClass = node.getAttributes().getNamedItem("class").getNodeValue();
    	        }
    	        if (node.getAttributes().getNamedItem("args") != null && !"".equals(node.getAttributes().getNamedItem("args").getNodeValue())){
    	            String[] argStrings = node.getAttributes().getNamedItem("args").getNodeValue().split(",");
    	            item.args = new Object[argStrings.length];
    	            for(int i = 0; i < argStrings.length; i++){
    	                item.args[i] = new StringObject(argStrings[i]);
    	            }
    	        }
    	        
    	        
    	        item.popupNode = ((Element)node).getElementsByTagName("menuPanel").item(0);
    	        if(node.getAttributes().getNamedItem("enabled") != null){
    	            if(node.getAttributes().getNamedItem("enabled").getNodeValue().equals("true"))
    	                item.enable(true);
    	            else
    	                item.enable(false);
    	        }else
    	            item.enable(true);
    	        
    	        if(item.popupNode != null){
    	            item.menuItemsPanel = (MenuPanel)loadWidget(item.popupNode, def);
    	            if(item.popupNode.getAttributes().getNamedItem("position") != null)
    	                item.popPosition = MenuItem.PopPosition.valueOf(item.popupNode.getAttributes().getNamedItem("position").getNodeValue().toUpperCase());
    	        }

    	        if(node.getAttributes().getNamedItem("key") != null){
    	            item.key = node.getAttributes().getNamedItem("key").getNodeValue();
    	        }
    	        
    	        setDefaults(node,item);
    	        return item;
    		}
    	});
    	factoryMap.put("menuPanel",new Factory<MenuPanel>() {
    		public MenuPanel getNewInstance(Node node, ScreenDef def) {
    			MenuPanel panel = new MenuPanel();
    		       String layout = node.getAttributes().getNamedItem("layout").getNodeValue();
    		        panel.init(layout);
    		        NodeList items = node.getChildNodes();
    		        for (int i = 0; i < items.getLength(); i++) {
    		            if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
    		                Widget wid = loadWidget(items.item(i),def);
    		                if(wid instanceof ScreenMenuItem)
    		                    panel.add(((ScreenMenuItem)wid).getWidget());
    		                else
    		                    panel.add(wid);
    		            }
    		        }
    		        setDefaults(node, panel);
    		        return panel;
    		}
    	});
    	factoryMap.put("password", new Factory<PassWordTextBox>() {
    		public PassWordTextBox getNewInstance(Node node, ScreenDef def) {
    			PassWordTextBox textbox = new PassWordTextBox();
    			if (node.getAttributes().getNamedItem("tab") != null) 
    				textbox.addTabHandler(new TabHandler(node,def));
    	        if (node.getAttributes().getNamedItem("shortcut") != null)
    	            textbox.setAccessKey(node.getAttributes()
    	                                     .getNamedItem("shortcut")
    	                                     .getNodeValue()
    	                                     .charAt(0));
    	        textbox.setStyleName("ScreenPassword");
    	        setDefaults(node, textbox);
    	        textbox.setField((StringField)factoryMap.get("String").getNewInstance(node, null));
    	        textbox.addBlurHandler(focusHandler);
    	        textbox.addFocusHandler(focusHandler);
    	        return textbox;
    		}
    	});
    	factoryMap.put("radio", new Factory<RadioButton>() {
    		public RadioButton getNewInstance(Node node, ScreenDef def) {
                RadioButton radio = new RadioButton(node.getAttributes().getNamedItem("group").getNodeValue());
                if(node.getAttributes().getNamedItem("tab") != null)
                	radio.addTabHandler(new TabHandler(node,def));
                if (node.getFirstChild() != null)
                	radio.setText(node.getFirstChild().getNodeValue());
                if (node.getAttributes().getNamedItem("shortcut") != null)
                	radio.setAccessKey(node.getAttributes()
                			.getNamedItem("shortcut")
                			.getNodeValue()
                			.charAt(0));
                
                radio.setStyleName("ScreenRadio");
                setDefaults(node, radio);
                radio.setField((CheckField)factoryMap.get("Check").getNewInstance(node, def));
                radio.addBlurHandler(focusHandler);
                radio.addFocusHandler(focusHandler);
                return radio;
    		}
    	});
    	factoryMap.put("resultsTable", new Factory<ResultsTable>() {
    		public ResultsTable getNewInstance(Node node, ScreenDef def) {
    			ResultsTable results = new ResultsTable();
    	        NodeList children = node.getChildNodes();
    	        Node buttons = null;
    	        Node table = null;
    	        Node query = null;
    	        for(int i = 0; i < children.getLength(); i++){
    	            if(children.item(i).getNodeType() == Node.ELEMENT_NODE) {
    	                if(children.item(i).getNodeName().equals("buttonGroup"))
    	                    buttons = children.item(i);
    	                else if(children.item(i).getNodeName().equals("table"))
    	                    table = children.item(i);
    	                else if(children.item(i).getNodeName().equals("query"))
    	                    query = children.item(i);
    	            }
    	        }
    	        if(table != null){
    	            results.setTable((TableWidget)factoryMap.get("table").getNewInstance(table, null));
    	        }
    	        if(node.getAttributes().getNamedItem("showNavPanel") != null) {
    	            if(node.getAttributes().getNamedItem("showNavPanel").getNodeValue().equals("false")){
    	                results.showNavPanel = false;
    	            }
    	        }

    	        if(buttons != null)
    	            results.setButtonGroup((ButtonGroup)loadWidget(buttons,def));
    	        
    	        setDefaults(node, results);
    	        return results;
    		}
    	});
    	factoryMap.put("richtext", new Factory<RichTextWidget>() {
    		public RichTextWidget getNewInstance(Node node, ScreenDef def) {
    			boolean tools = true;
    	        if(node.getAttributes().getNamedItem("tools") != null){
    	            if(node.getAttributes().getNamedItem("tools").getNodeValue().equals("false")){
    	                tools = false;
    	            }
    	        }
    	        RichTextWidget textarea = new RichTextWidget(tools); 
    	        if(node.getAttributes().getNamedItem("tab") != null) 
    	        	textarea.addTabHandler(new TabHandler(node,def));
    	        String width = "100%";
    	        String height = "300px";
    	        if(node.getAttributes().getNamedItem("width") != null){
    	            width = node.getAttributes().getNamedItem("width").getNodeValue();
    	        }
    	        if(node.getAttributes().getNamedItem("height") != null) {
    	            height = node.getAttributes().getNamedItem("height").getNodeValue();
    	        }
    	        textarea.init(tools);
    	        textarea.area.setSize(width, height);
    	        
    	        textarea.area.setStyleName("ScreenTextArea");
    	        setDefaults(node, textarea);
    	        textarea.setField((StringField)factoryMap.get("String").getNewInstance(node, null));
    	        textarea.addFocusHandler(focusHandler);
    	        textarea.addBlurHandler(focusHandler);
    	
    	        return textarea;
    		}
    	});
    	factoryMap.put("ScrollTabBar", new Factory<ScrollableTabBar>(){
    		public ScrollableTabBar getNewInstance(Node node, ScreenDef def) {
    			ScrollableTabBar scrollableTabBar = new ScrollableTabBar();
    	        scrollableTabBar.setStyleName("ScreenTab");        
    	        String width = "";
    	        if (node.getAttributes().getNamedItem("width") != null){
    	            width = node.getAttributes()
    	                                  .getNamedItem("width")
    	                                  .getNodeValue();
    	            scrollableTabBar.setWidth(width);
    	        }
    	        NodeList tabs = ((Element)node).getElementsByTagName("tab");           
    	        if(tabs.getLength()>0){
    	         for (int k = 0; k < tabs.getLength(); k++) {                       
    	            scrollableTabBar.addTab(tabs.item(k).getAttributes()
    	                                    .getNamedItem("text")
    	                                    .getNodeValue());             
    	         }
    	         scrollableTabBar.selectTab(0);
    	        }        
    	        
    	        setDefaults(node, scrollableTabBar);
    	        return scrollableTabBar;
    		}
    	});
    	factoryMap.put("StackPanel", new Factory<StackPanel>(){
    		public StackPanel getNewInstance(Node node, ScreenDef def) {
    			StackPanel stack = new StackPanel();
    	        stack.setStyleName("ScreenStack");
    	        NodeList stacks = ((Element)node).getElementsByTagName("stack");
    	        for (int k = 0; k < stacks.getLength(); k++) {
    	            NodeList widgets = stacks.item(k).getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	                    Widget wid = loadWidget(widgets.item(l),def);
    	                    stack.add(wid, stacks.item(k)
    	                                         .getAttributes()
    	                                         .getNamedItem("text")
    	                                         .getNodeValue());
    	                }
    	            }
    	        }
    	        stack.showStack(0);
    	        setDefaults(node, stack);
    	        return stack;	
    		}
    	});
    	factoryMap.put("TabPanel", new Factory<TabPanel>() {
    		public TabPanel getNewInstance(Node node, ScreenDef def){
   	            TabPanel panel = new TabPanel();
    	        panel.setStyleName("ScreenTab");
    	        NodeList tabs = ((Element)node).getElementsByTagName("tab");
    	        for (int k = 0; k < tabs.getLength(); k++) {
    	            NodeList widgets = tabs.item(k).getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	                    Widget wid = loadWidget(widgets.item(l),def);
    	                    ScrollPanel scroll = new ScrollPanel();
    	                    scroll.add(wid);
    	                    scroll.setWidth("100%");
    	                    //tabs can not have a constant or hard coded text
    	                    panel.add(scroll, tabs.item(k).getAttributes()
    	                                             .getNamedItem("text")
    	                                             .getNodeValue());
    	                }
    	            }
    	        }
    	        panel.selectTab(0);
    	        setDefaults(node, panel);
    	        return panel;
    		}
    	});
    	factoryMap.put("text",new Factory<Label>() {
    		public Label getNewInstance(Node node, ScreenDef def) {
    			Label text = new Label();
    	        if (node.getAttributes().getNamedItem("wordwrap") != null)
    	            text.setWordWrap(Boolean.valueOf(node.getAttributes()
    	                                                 .getNamedItem("wordwrap")
    	                                                 .getNodeValue())
    	                                    .booleanValue());
    	        else
    	            text.setWordWrap(false);
    	        if (node.getAttributes().getNamedItem("mouse") != null) {
    	            String mouse = node.getAttributes()
    	                               .getNamedItem("mouse")
    	                               .getNodeValue();
    	            text.addMouseListener((MouseListener)ClassFactory.forName(mouse));
    	        }
    	        if (node.hasChildNodes())
    	            text.setText(node.getFirstChild().getNodeValue());
    	        else
    	            text.setText("");
    	        text.setStyleName("ScreenLabel");
    	        setDefaults(node, text);
    	        return text;
    		}
    	});
    	factoryMap.put("textarea", new Factory<TextArea>(){
    		public TextArea getNewInstance(Node node, ScreenDef def) {
    			TextArea textarea = new TextArea();
    			if(node.getAttributes().getNamedItem("tab") != null) 
    				textarea.addTabHandler(new TabHandler(node,def));
    	        if (node.getAttributes().getNamedItem("shortcut") != null)
    	            textarea.setAccessKey(node.getAttributes()
    	                                      .getNamedItem("shortcut")
    	                                      .getNodeValue()
    	                                      .charAt(0));
    	        
    	        textarea.setStyleName("ScreenTextArea");
    	        setDefaults(node, textarea);
    	        textarea.setField((StringField)factoryMap.get("String").getNewInstance(node, null));
    	        textarea.addBlurHandler(focusHandler);
    	        textarea.addFocusHandler(focusHandler);
    	        return textarea;
    		}
    	});
    	factoryMap.put("TitledPanel", new Factory<TitledPanel>(){
    		public TitledPanel getNewInstance(Node node, ScreenDef def) {
    			TitledPanel panel = new TitledPanel();
    	        Element legend = (Element) ((Element)node).getElementsByTagName("legend").item(0);
    	        NodeList widgets = legend.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	                Widget wid = loadWidget(widgets.item(k),def);
    	                panel.setTitle(wid);
    	            }
    	        }
    	        
    	        legend = (Element) ((Element)node).getElementsByTagName("content").item(0);
    	        widgets = legend.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	                Widget wid = loadWidget(widgets.item(k),def);
    	                panel.setWidget(wid);
    	            }
    	        }
    	       
    	        setDefaults(node, panel);
    	        return panel;
    		}
    	});
    	factoryMap.put("VerticalSplitPanel", new Factory<VerticalSplitPanel>(){
    		public VerticalSplitPanel getNewInstance(Node node, ScreenDef def) {
    			VerticalSplitPanel vp = new VerticalSplitPanel();
    	        vp.setStyleName("ScreenSplit");
    	        NodeList sections = ((Element)node).getElementsByTagName("section");
    	        for (int k = 0; k < sections.getLength(); k++) {
    	            NodeList widgets = sections.item(k).getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	                    Widget wid = loadWidget(widgets.item(l),def);
    	                    if (k == 0) {
    	                             vp.setTopWidget(wid);
    	                    }
    	                    if (k == 1) {
    	                             vp.setBottomWidget(wid);
    	                    }
    	                }
    	            }
    	        }
    	        if (node.getAttributes().getNamedItem("splitpos") != null) {
    	            String splitpos = node.getAttributes()
    	                                  .getNamedItem("splitpos")
    	                                  .getNodeValue();
    	                 vp.setSplitPosition(splitpos);
    	        }
    	        setDefaults(node, vp);
    	        return vp;
    		}
    	});
    	factoryMap.put("winbrowser", new Factory<WindowBrowser>(){
    		public WindowBrowser getNewInstance(Node node, ScreenDef def) {
    			WindowBrowser browser = new WindowBrowser();
    	        int limit = 10;
    	        if(node.getAttributes().getNamedItem("winLimit") != null){
    	            limit = Integer.parseInt(node.getAttributes().getNamedItem("winLimit").getNodeValue());
    	        }
    	        if(node.getAttributes().getNamedItem("sizeToWindow") != null)
    	            browser.init(true,limit);
    	        else
    	            browser.init(false,limit);
    	        browser.setStyleName("ScreenWindowBrowser");
    	        setDefaults(node,browser);
    	        return browser;
    		}
    	});
    	factoryMap.put("table", new Factory<TableWidget>() {
    		public TableWidget getNewInstance(Node node, ScreenDef def) {
    			TableWidget table = new TableWidget();
    			if(node.getAttributes().getNamedItem("tab") != null) 
    				table.addTabHandler(new TabHandler(node,def));
                if(node.getAttributes().getNamedItem("cellHeight") != null){
                    table.cellHeight = (Integer.parseInt(node.getAttributes().getNamedItem("cellHeight").getNodeValue()));
                }
                
                table.setTableWidth(node.getAttributes().getNamedItem("width").getNodeValue());
                table.maxRows = Integer.parseInt(node.getAttributes().getNamedItem("maxRows").getNodeValue());

                if(node.getAttributes().getNamedItem("title") != null){
                        table.title =node.getAttributes().getNamedItem("title").getNodeValue();
                }
                if(node.getAttributes().getNamedItem("showRows") != null){
                    if(node.getAttributes().getNamedItem("showRows").getNodeValue().equals("true"))
                        table.showRows = true;
                }
                if(node.getAttributes().getNamedItem("showScroll") != null){
                	String showScroll = node.getAttributes().getNamedItem("showScroll").getNodeValue();
                    table.showScroll = VerticalScroll.valueOf(showScroll);
                }
                if(node.getAttributes().getNamedItem("multiSelect") != null){
                    if(node.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true"))
                        table.enableMultiSelect(true);
                }
                NodeList colList = ((Element)node).getElementsByTagName("col");
                ArrayList<TableColumn> columns = new ArrayList<TableColumn>(colList.getLength());
                for(int i = 0; i < colList.getLength(); i++) {
                	Node col = colList.item(i);
                	TableColumn column = new TableColumn();
                	if(col.getAttributes().getNamedItem("key") != null)
                		column.setKey(col.getAttributes().getNamedItem("key").getNodeValue());
                	if(col.getAttributes().getNamedItem("header") != null){
                		column.setHeader(col.getAttributes().getNamedItem("header").getNodeValue());
                		table.showHeader = true;
                	}
                	if(col.getAttributes().getNamedItem("width") != null)
                		column.setCurrentWidth(Integer.parseInt(col.getAttributes().getNamedItem("width").getNodeValue()));
                	if(col.getAttributes().getNamedItem("sort") != null)
                		column.setSortable(Boolean.parseBoolean(col.getAttributes().getNamedItem("sort").getNodeValue()));
                	if(col.getAttributes().getNamedItem("filter") != null)
                		column.setFilterable(Boolean.parseBoolean(col.getAttributes().getNamedItem("filter").getNodeValue()));
                	if(col.getAttributes().getNamedItem("align") != null){
                		String align = col.getAttributes().getNamedItem("align").getNodeValue();
                        if (align.equals("left"))
                            column.setAlign(HasAlignment.ALIGN_LEFT);
                        if (align.equals("center"))
                            column.setAlign(HasAlignment.ALIGN_CENTER);
                        if (align.equals("right"))
                            column.setAlign(HasAlignment.ALIGN_RIGHT);
                	}
                	NodeList editor = col.getChildNodes();
                	for(int j = 0; j < editor.getLength(); j++){
                		if(editor.item(j).getNodeType() == Node.ELEMENT_NODE) {
                			Widget wid = createWidget(editor.item(j),def);
                			if(wid instanceof HasBlurHandlers)
                				((HasBlurHandlers)wid).addBlurHandler(table);
                			column.setColumnWidget(wid);
                			break;
                		}
                	}
                	column.controller = table;
                	columns.add(column);
                }
                table.columns = columns;
                table.init();
                setDefaults(node,table);
                table.addBlurHandler(focusHandler);
                table.addFocusHandler(focusHandler);
    			return table;
    		}
    	});
    	factoryMap.put("dropdown", new Factory<Dropdown>(){
    		public Dropdown getNewInstance(Node node, ScreenDef def){
    		    Dropdown<?> drop = null;
    		    Field field = (IntegerField)factoryMap.get("Integer").getNewInstance(node, null);
    		    if(node.getAttributes().getNamedItem("field") != null){
    		        if(node.getAttributes().getNamedItem("field").getNodeValue().equals("String")){
    		            drop = new Dropdown<String>();
    		            field = (StringField)factoryMap.get("String").getNewInstance(node,null);
    		        }else if(node.getAttributes().getNamedItem("field").getNodeValue().equals("Integer")) {
    		            drop = new Dropdown<Integer>();
    		        }else
    		            drop = new Dropdown();
    		    }else
    		        drop = new Dropdown();
    	        if(node.getAttributes().getNamedItem("tab") != null) {
    	        	drop.addTabHandler(new TabHandler(node,def));
    	        }
    		    drop.setField(field);
    		    drop.multiSelect = false;
    	                
    	        if (node.getAttributes().getNamedItem("multiSelect") != null && node.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true"))
    	        	drop.multiSelect = true;
    	        
    	        if (node.getAttributes().getNamedItem("text") != null)
    	        	drop.textBoxDefault = node.getAttributes()
    	                                  .getNamedItem("text")
    	                                  .getNodeValue();
    	        
    	        if (node.getAttributes().getNamedItem("width") != null)
    	        	drop.width = node.getAttributes()
    	                                  .getNamedItem("width")
    	                                  .getNodeValue();
    	        
    	        if (node.getAttributes().getNamedItem("popWidth") != null)
    	            drop.setTableWidth(node.getAttributes()
    	                                  .getNamedItem("popWidth")
    	                                  .getNodeValue());
    	        else
    	        	drop.setTableWidth("auto");
                NodeList colList = ((Element)node).getElementsByTagName("col");
                ArrayList<TableColumn> columns = new ArrayList<TableColumn>();
                if(colList.getLength() > 0){
                	for(int i = 0; i < colList.getLength(); i++) {
                		Node col = colList.item(i);
                		TableColumn column = new TableColumn();
                		if(col.getAttributes().getNamedItem("name") != null)
                			column.setKey(col.getAttributes().getNamedItem("name").getNodeValue());
                		if(col.getAttributes().getNamedItem("header") != null){
                			column.setHeader(col.getAttributes().getNamedItem("header").getNodeValue());
                			drop.showHeader = true;
                		}
                		if(col.getAttributes().getNamedItem("width") != null)
                			column.setCurrentWidth(Integer.parseInt(col.getAttributes().getNamedItem("width").getNodeValue()));
                		if(col.getAttributes().getNamedItem("sort") != null)
                			column.setSortable(Boolean.parseBoolean(col.getAttributes().getNamedItem("sort").getNodeValue()));
                		if(col.getAttributes().getNamedItem("filter") != null)
                			column.setFilterable(Boolean.parseBoolean(col.getAttributes().getNamedItem("filter").getNodeValue()));
                		if(col.getAttributes().getNamedItem("align") != null){
                			String align = col.getAttributes().getNamedItem("align").getNodeValue();
                			if (align.equals("left"))
                				column.setAlign(HasAlignment.ALIGN_LEFT);
                			if (align.equals("center"))
                				column.setAlign(HasAlignment.ALIGN_CENTER);
                			if (align.equals("right"))
                				column.setAlign(HasAlignment.ALIGN_RIGHT);
                		}
                		column.setColumnWidget(new org.openelis.gwt.widget.Label());
                		columns.add(column);
                	}
                }else{
                	TableColumn column = new TableColumn();
                	column.controller = drop;
                	column.setCurrentWidth(100);
                	column.setColumnWidget(new org.openelis.gwt.widget.Label());
                	columns.add(column);
                }
                drop.columns = columns;
                drop.setup();
                setDefaults(node,drop);
                drop.textbox.addBlurHandler(focusHandler);
                drop.textbox.addFocusHandler(focusHandler);
    			return drop;
    		}
    	});
    	factoryMap.put("autoComplete", new Factory<AutoComplete>() {
    		public AutoComplete getNewInstance(Node node, ScreenDef def) {
    			AutoComplete<?> auto = null;
    			Field field = (IntegerField)factoryMap.get("Integer").getNewInstance(node, null);
                if(node.getAttributes().getNamedItem("field") != null){
                    if(node.getAttributes().getNamedItem("field").getNodeValue().equals("String")){
                        auto = new AutoComplete<String>();
                        field = (StringField)factoryMap.get("String").getNewInstance(node, null);
                    }else if(node.getAttributes().getNamedItem("field").getNodeValue().equals("Integer")) {
                        auto = new AutoComplete<Integer>();
                    }else
                        auto = new AutoComplete();
                }else
                    auto = new AutoComplete();
                auto.setField(field);
                
    	        if(node.getAttributes().getNamedItem("tab") != null) {
    	        	auto.addTabHandler(new TabHandler(node,def));
    	        }
    	        if (node.getAttributes().getNamedItem("multiSelect") != null && node.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true"))
    	        	auto.multiSelect = true;
    	        
    	        if (node.getAttributes().getNamedItem("text") != null)
    	        	auto.textBoxDefault = node.getAttributes()
    	                                  .getNamedItem("text")
    	                                  .getNodeValue();
    	        else
    	            auto.textBoxDefault = "";
    	        
    	        if (node.getAttributes().getNamedItem("width") != null)
    	        	auto.setWidth(node.getAttributes()
    	                                  .getNamedItem("width")
    	                                  .getNodeValue());
    	        
    	        if (node.getAttributes().getNamedItem("popWidth") != null)
    	            auto.setTableWidth(node.getAttributes()
    	                                  .getNamedItem("popWidth")
    	                                  .getNodeValue());
    	        else
    	        	auto.setTableWidth("auto");
    	        if(node.getAttributes().getNamedItem("case") != null){
    	            String textCase = node.getAttributes().getNamedItem("case").getNodeValue().toUpperCase();
    	            auto.textbox.setCase(TextBox.Case.valueOf(textCase));
    	        }
                NodeList colList = ((Element)node).getElementsByTagName("col");
                ArrayList<TableColumn> columns = new ArrayList<TableColumn>();
                if(colList.getLength() > 0){
                	for(int i = 0; i < colList.getLength(); i++) {
                		Node col = colList.item(i);
                		TableColumn column = new TableColumn();
                		if(col.getAttributes().getNamedItem("name") != null)
                			column.setKey(col.getAttributes().getNamedItem("name").getNodeValue());
                		if(col.getAttributes().getNamedItem("header") != null) {
                			column.setHeader(col.getAttributes().getNamedItem("header").getNodeValue());
                			auto.showHeader = true;
                		}
                		if(col.getAttributes().getNamedItem("width") != null)
                			column.setCurrentWidth(Integer.parseInt(col.getAttributes().getNamedItem("width").getNodeValue()));
                		if(col.getAttributes().getNamedItem("sort") != null)
                			column.setSortable(Boolean.parseBoolean(col.getAttributes().getNamedItem("sort").getNodeValue()));
                		if(col.getAttributes().getNamedItem("filter") != null)
                			column.setFilterable(Boolean.parseBoolean(col.getAttributes().getNamedItem("filter").getNodeValue()));
                		if(col.getAttributes().getNamedItem("align") != null){
                			String align = col.getAttributes().getNamedItem("align").getNodeValue();
                			if (align.equals("left"))
                				column.setAlign(HasAlignment.ALIGN_LEFT);
                			if (align.equals("center"))
                				column.setAlign(HasAlignment.ALIGN_CENTER);
                			if (align.equals("right"))
                				column.setAlign(HasAlignment.ALIGN_RIGHT);
                		}
                		column.setColumnWidget(new org.openelis.gwt.widget.Label());
                		column.controller = auto;
                		columns.add(column);
                		
                	}
                }else{
                	TableColumn column = new TableColumn();
                	column.setCurrentWidth(100);
                	column.setColumnWidget(new org.openelis.gwt.widget.Label());
                	column.controller = auto;
                	columns.add(column);
                }
                auto.columns = columns;
                auto.setup();
                auto.textbox.addBlurHandler(focusHandler);
                auto.textbox.addFocusHandler(focusHandler);
    			return auto;
    		}
    	});
    	factoryMap.put("tree", new Factory<TreeWidget>() {
    		public TreeWidget getNewInstance(Node node, ScreenDef def) {
    			TreeWidget tree = new TreeWidget();
    			if(node.getAttributes().getNamedItem("tab") != null) 
    				tree.addTabHandler(new TabHandler(node,def));
                if(node.getAttributes().getNamedItem("cellHeight") != null){
                    tree.cellHeight = (Integer.parseInt(node.getAttributes().getNamedItem("cellHeight").getNodeValue()));
                }
                
                tree.setTreeWidth(node.getAttributes().getNamedItem("width").getNodeValue());
                tree.maxRows = Integer.parseInt(node.getAttributes().getNamedItem("maxRows").getNodeValue());

                if(node.getAttributes().getNamedItem("title") != null){
                        tree.title =node.getAttributes().getNamedItem("title").getNodeValue();
                }
                if(node.getAttributes().getNamedItem("showRows") != null){
                    if(node.getAttributes().getNamedItem("showRows").getNodeValue().equals("true"))
                        tree.showRows = true;
                }
                if(node.getAttributes().getNamedItem("showScroll") != null){
                	String showScroll = node.getAttributes().getNamedItem("showScroll").getNodeValue();
                    tree.showScroll = TreeView.VerticalScroll.valueOf(showScroll);
                }
                if(node.getAttributes().getNamedItem("multiSelect") != null){
                    if(node.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true"))
                        tree.enableMultiSelect(true);
                }
                NodeList leafList = ((Element)node).getElementsByTagName("leaf");
                HashMap<String, ArrayList<TreeColumn>> columns = new HashMap<String,ArrayList<TreeColumn>>(leafList.getLength());
                for(int h = 0; h < leafList.getLength(); h++) {
                	NodeList colList = ((Element)leafList.item(h)).getElementsByTagName("col");
                	ArrayList<TreeColumn> cols = new ArrayList<TreeColumn>(colList.getLength());
                	for(int i = 0; i < colList.getLength(); i++) {
                		Node col = colList.item(i);
                		TreeColumn column = new TreeColumn();
                		if(col.getAttributes().getNamedItem("key") != null)
                			column.setKey(col.getAttributes().getNamedItem("key").getNodeValue());
                		if(col.getAttributes().getNamedItem("header") != null){
                			column.setHeader(col.getAttributes().getNamedItem("header").getNodeValue());
                			tree.showHeader = true;
                		}
                		if(col.getAttributes().getNamedItem("width") != null)
                			column.setCurrentWidth(Integer.parseInt(col.getAttributes().getNamedItem("width").getNodeValue()));
                		if(col.getAttributes().getNamedItem("sort") != null)
                			column.setSortable(Boolean.parseBoolean(col.getAttributes().getNamedItem("sort").getNodeValue()));
                		if(col.getAttributes().getNamedItem("filter") != null)
                			column.setFilterable(Boolean.parseBoolean(col.getAttributes().getNamedItem("filter").getNodeValue()));
                		if(col.getAttributes().getNamedItem("align") != null){
                			String align = col.getAttributes().getNamedItem("align").getNodeValue();
                			if (align.equals("left"))
                				column.setAlign(HasAlignment.ALIGN_LEFT);
                			if (align.equals("center"))
                				column.setAlign(HasAlignment.ALIGN_CENTER);
                			if (align.equals("right"))
                				column.setAlign(HasAlignment.ALIGN_RIGHT);
                		}
                		NodeList editor = col.getChildNodes();
                		for(int j = 0; j < editor.getLength(); j++){
                			if(editor.item(j).getNodeType() == Node.ELEMENT_NODE) {
                				Widget wid = createWidget(editor.item(j),def);
                				if(wid instanceof HasBlurHandlers)
                					((HasBlurHandlers)wid).addBlurHandler(tree);
                				column.setColumnWidget(wid);
                				break;
                			}
                		}
                		column.controller = tree;
                		cols.add(column);
                	}
                	columns.put(leafList.item(h).getAttributes().getNamedItem("key").getNodeValue(), cols);
                }
                tree.columns = columns;
                tree.init();
                setDefaults(node,tree);
                tree.addBlurHandler(focusHandler);
                tree.addFocusHandler(focusHandler);
    			return tree;
    		}
    	});
    	factoryMap.put("appButton", new Factory<AppButton>() {
    		public AppButton getNewInstance(Node node, ScreenDef def) {
    			AppButton button = new AppButton();
    			if(node.getAttributes().getNamedItem("tab") != null) {
    				button.addTabHandler(new TabHandler(node,def));
    			}
    	        button.action = node.getAttributes().getNamedItem("action").getNodeValue();
    	        /*
    	        if(node.getAttributes().getNamedItem("toggle") != null){
    	            if(node.getAttributes().getNamedItem("toggle").getNodeValue().equals("true"))
    	                button.toggle = true;
    	        }
    	        */
    	        NodeList widgets = node.getChildNodes();
    	        for (int l = 0; l < widgets.getLength(); l++) {
    	            if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {                       
    	                Widget wid = loadWidget(widgets.item(l),def);
    	                button.setWidget(wid);
    	            }
    	        }
    	        /*
    	        if(node.getAttributes().getNamedItem("enabledStates") != null && !"".equals(node.getAttributes().getNamedItem("enabledStates").getNodeValue())){
    	            EnumSet<State> enabledStateSet = EnumSet.noneOf(State.class);
    	        	 String[] enabledStates = node.getAttributes().getNamedItem("enabledStates").getNodeValue().split(",");
    	        	 for(int i = 0; i < enabledStates.length; i++)
    	        		 enabledStateSet.add(State.valueOf(enabledStates[i].toUpperCase()));
    	        	 button.setEnabledStates(enabledStateSet);
    	        }else{
    	            button.setEnabledStates(EnumSet.noneOf(State.class));
    	        }
    	        
    	        if(node.getAttributes().getNamedItem("lockedStates") != null && !"".equals(node.getAttributes().getNamedItem("lockedStates").getNodeValue())){
    	        	 EnumSet<State> lockedStateSet = EnumSet.noneOf(State.class);
    	        	 String[] lockedStates = node.getAttributes().getNamedItem("lockedStates").getNodeValue().split(",");
    	        	 for(int i = 0; i < lockedStates.length; i++)
    	        		 lockedStateSet.add(State.valueOf(lockedStates[i].toUpperCase()));
    	        	 button.setLockedStates(lockedStateSet);
    	        }else{
    	            button.setLockedStates(EnumSet.noneOf(State.class));
    	        }
    	        */    
    	        setDefaults(node, button);
    	        
    	        if (node.getAttributes().getNamedItem("style") != null)
    	        	button.setStyleName(node.getAttributes().getNamedItem("style").getNodeValue());
    	        return button;
    		}
    	});
    	factoryMap.put("comButton", new Factory<CommandButton>() {
    		public CommandButton getNewInstance(Node node, ScreenDef def){
    			CommandButton button = new CommandButton();
    	        if(node.getAttributes().getNamedItem("tab") != null) {
    	        	button.addTabHandler(new TabHandler(node,def));
    	        }
    	        button.command = ClassFactory.getEnum(node.getAttributes().getNamedItem("command").getNodeValue());
    	        if(node.getAttributes().getNamedItem("value") != null){
    	            button.value = node.getAttributes().getNamedItem("value").getNodeValue();
    	        }
    	        if(node.getAttributes().getNamedItem("toggle") != null){
    	            if(node.getAttributes().getNamedItem("toggle").getNodeValue().equals("true"))
    	                button.toggle = true;
    	        }
    	        NodeList widgets = node.getChildNodes();
    	        for (int l = 0; l < widgets.getLength(); l++) {
    	            if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {                       
    	                Widget wid = loadWidget(widgets.item(l),def);
    	                button.setWidget(wid);
    	            }
    	        }
    	    
    	        if(node.getAttributes().getNamedItem("enabledStates") != null && !"".equals(node.getAttributes().getNamedItem("enabledStates").getNodeValue())){
    	            EnumSet<State> enabledStateSet = EnumSet.noneOf(State.class);
    	        	 String[] enabledStates = node.getAttributes().getNamedItem("enabledStates").getNodeValue().split(",");
    	        	 for(int i = 0; i < enabledStates.length; i++)
    	        		 enabledStateSet.add(State.valueOf(enabledStates[i].toUpperCase()));
    	        	 button.setEnabledStates(enabledStateSet);
    	        }else{
    	            button.setEnabledStates(EnumSet.noneOf(State.class));
    	        }
    	        
    	        if(node.getAttributes().getNamedItem("lockedStates") != null && !"".equals(node.getAttributes().getNamedItem("lockedStates").getNodeValue())){
    	        	 EnumSet<State> lockedStateSet = EnumSet.noneOf(State.class);
    	        	 String[] lockedStates = node.getAttributes().getNamedItem("lockedStates").getNodeValue().split(",");
    	        	 for(int i = 0; i < lockedStates.length; i++)
    	        		 lockedStateSet.add(State.valueOf(lockedStates[i].toUpperCase()));
    	        	 button.setLockedStates(lockedStateSet);
    	        }else{
    	            button.setLockedStates(EnumSet.noneOf(State.class));
    	        }
    	            
    	        setDefaults(node, button);
    	        
    	        if (node.getAttributes().getNamedItem("style") != null)
    	        	button.setStyleName(node.getAttributes().getNamedItem("style").getNodeValue());
    	        return button;
    		}
    	});
    	factoryMap.put("icon", new Factory<IconContainer>() {
    		public IconContainer getNewInstance(Node node, ScreenDef def) {
    			IconContainer icon = new IconContainer();
    			if(node.getAttributes().getNamedItem("tab") != null) {
    				icon.addTabHandler(new TabHandler(node,def));
    			}
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	                Widget wid = loadWidget(widgets.item(k),def);
    	                int x = -1;
    	                if (widgets.item(k).getAttributes().getNamedItem("x") != null)
    	                    x = Integer.parseInt(widgets.item(k)
    	                                                .getAttributes()
    	                                                .getNamedItem("x")
    	                                                .getNodeValue());
    	                int y = -1;
    	                if (widgets.item(k).getAttributes().getNamedItem("y") != null)
    	                    y = Integer.parseInt(widgets.item(k)
    	                                                .getAttributes()
    	                                                .getNamedItem("y")
    	                                                .getNodeValue());
    	                if(node.getAttributes().getNamedItem("align") != null)
    	                    DOM.setElementProperty(icon.getElement(),"align",node.getAttributes().getNamedItem("align").getNodeValue());
    	                icon.add(wid, x, y);
    	            }
    	        }
    	        setDefaults(node,icon);
    	        return icon;
    		}
    	});
    	factoryMap.put("String", new Factory<StringField>() {
    		public StringField getNewInstance(Node node, ScreenDef def) {
    			StringField field = new StringField();
    			if(node.getAttributes().getNamedItem("max") != null) 
    				field.setMax(Integer.parseInt(node.getAttributes().getNamedItem("max").getNodeValue()));
    			if(node.getAttributes().getNamedItem("min") != null)
    				field.setMax(Integer.parseInt(node.getAttributes().getNamedItem("min").getNodeValue()));
    			if(node.getAttributes().getNamedItem("required") != null)
    				field.required = Boolean.parseBoolean(node.getAttributes().getNamedItem("required").getNodeValue());
    			return field;
    		}
    	});
    	factoryMap.put("Date", new Factory<DateField>() {
    		public DateField getNewInstance(Node node, ScreenDef def){
    			DateField field = new DateField();
    	        if (node.getAttributes().getNamedItem("required") != null)
    	            field.required = Boolean.parseBoolean(node.getAttributes()
    	                                              .getNamedItem("required")
    	                                              .getNodeValue());
    	        if (node.getAttributes().getNamedItem("begin") != null)
    	            field.setBegin(Byte.parseByte(node.getAttributes()
    	                                              .getNamedItem("begin")
    	                                              .getNodeValue()));
    	        if (node.getAttributes().getNamedItem("end") != null)
    	            field.setEnd(Byte.parseByte(node.getAttributes()
    	                                            .getNamedItem("end")
    	                                            .getNodeValue()));
    	        if (node.getAttributes().getNamedItem("max") != null)
    	            field.setMax(new Integer(node.getAttributes()
    	                                         .getNamedItem("max")
    	                                         .getNodeValue()));
    	        if (node.getAttributes().getNamedItem("min") != null)
    	            field.setMin(new Integer(node.getAttributes()
    	                                         .getNamedItem("min")
    	                                         .getNodeValue()));
    	        if (node.hasChildNodes()) {
    	            String deflt = node.getFirstChild().getNodeValue();
    	            Date dat = null;
    	            if (def.equals("current"))
    	                dat = new Date();
    	            else
    	                dat = new Date(deflt);
    	            field.setValue(DatetimeRPC.getInstance(field.getBegin(),field.getEnd(),dat).getDate());
    	        }
    	        if(node.getAttributes().getNamedItem("pattern") != null){
    	            field.setFormat(node.getAttributes().getNamedItem("pattern").getNodeValue());
    	        }   
    			return field;
    		}
    	});
    	factoryMap.put("Check", new Factory<CheckField>() {
    		public CheckField getNewInstance(Node node, ScreenDef def) {
    			CheckField field = new CheckField();
    	        if (node.getAttributes().getNamedItem("required") != null)
    	            field.required = Boolean.parseBoolean(node.getAttributes()
    	                                              .getNamedItem("required")
    	                                              .getNodeValue());
    	        if (node.hasChildNodes()) {
    	            field.setValue(node.getFirstChild().getNodeValue());
    	        }
    			return field;
    		}
    	});
    	factoryMap.put("Integer", new Factory<IntegerField>(){
    		public IntegerField getNewInstance(Node node, ScreenDef def) {
    			IntegerField field = new IntegerField();
    			if(node.getAttributes().getNamedItem("required") != null)
    				field.required = Boolean.parseBoolean(node.getAttributes().getNamedItem("required").getNodeValue());
    			if(node.getAttributes().getNamedItem("max") != null)
    				field.setMax(Integer.parseInt(node.getAttributes().getNamedItem("max").getNodeValue()));
    			if(node.getAttributes().getNamedItem("min") != null)
    				field.setMin(Integer.parseInt(node.getAttributes().getNamedItem("min").getNodeValue()));
    	        if (node.getAttributes().getNamedItem("pattern") != null) {
    	            field.setFormat(node.getAttributes()
    	                          .getNamedItem("pattern")
    	                          .getNodeValue());
    	        }
    			return field;
    		}
    	});
    	factoryMap.put("Double", new Factory<DoubleField>() {
    		public DoubleField getNewInstance(Node node, ScreenDef def) {
    			DoubleField field = new DoubleField();
    	        if (node.getAttributes().getNamedItem("required") != null)
    	            field.required = Boolean.parseBoolean(node.getAttributes()
    	                                        .getNamedItem("required")
    	                                        .getNodeValue());
    	        if (node.getAttributes().getNamedItem("max") != null)
    	            field.setMax(new Double(node.getAttributes()
    	                                  .getNamedItem("max")
    	                                  .getNodeValue()));
    	        if (node.getAttributes().getNamedItem("min") != null)
    	            field.setMin(new Double(node.getAttributes()
    	                                  .getNamedItem("min")
    	                                  .getNodeValue()));
    	        if (node.getAttributes().getNamedItem("pattern") != null) {
    	            field.setFormat(node.getAttributes()
    	                          .getNamedItem("pattern")
    	                          .getNodeValue());
    	        }
    	        return field;
    		}
    	});
    }
    
    
}
