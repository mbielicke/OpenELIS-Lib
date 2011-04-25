package org.openelis.gwt.server;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.openelis.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class UIGenerator extends Generator {
	
	private String className;
	private String packageName;
	private Document doc;
	private SourceWriter sw;
	private int count;
	private ClassSourceFileComposerFactory composer;
	private String lang;
	
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,	String typeName) throws UnableToCompleteException {
		try {
			lang = context.getPropertyOracle().getSelectionProperty(logger,"locale").getCurrentValue();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		TypeOracle typeOracle = context.getTypeOracle();
		
		try {
			JClassType classType = typeOracle.getType(typeName);
			packageName = classType.getPackage().getName();
			className = classType.getSimpleSourceName();
			 generateClass(logger,context);
		}catch(Exception e) {
			logger.log(TreeLogger.ERROR,"Screen ERROR",e);
		}
		
		return packageName+"."+className+"_"+lang;
	}
	
	private void generateClass(TreeLogger logger, GeneratorContext context) throws Exception {
		PrintWriter printWriter = null;
		
		printWriter = context.tryCreate(logger, packageName, className+"_"+lang);
		
		if(printWriter == null)
			return;
		
		System.out.println("Generating "+className+".xsl");
		
		InputStream  xsl = context.getResourcesOracle().getResourceMap().get(packageName.replaceAll("\\.","/")+"/"+className+".xsl").openContents();
		String props = context.getPropertyOracle().getSelectionProperty(logger,"props").getCurrentValue().replaceAll("_","\\.");
    	doc = XMLUtil.parse(ServiceUtils.getGeneratorXML(xsl,props,lang));
		
    	composer = new ClassSourceFileComposerFactory(packageName,className+"_"+lang);
        composer.addImport("org.openelis.gwt.screen.ScreenPanel");
        composer.addImport("java.util.HashMap");
        composer.addImport("org.openelis.gwt.screen.ScreenDefInt");
        composer.addImport("com.google.gwt.user.client.ui.Widget");
        composer.addImport("com.google.gwt.user.client.ui.VerticalPanel");
        composer.addImport("java.util.ArrayList");
        composer.addImport("java.util.Arrays");
        composer.addImport("java.util.Date");
        composer.addImport("java.util.HashMap");
        composer.addImport("java.util.List");
        composer.addImport("org.openelis.gwt.common.Datetime");
        composer.addImport("org.openelis.gwt.widget.StringHelper");
        composer.addImport("org.openelis.gwt.widget.DateHelper");
        composer.addImport("org.openelis.gwt.widget.IntegerHelper");
        composer.addImport("org.openelis.gwt.widget.LongHelper");
        composer.addImport("org.openelis.gwt.widget.DoubleHelper");
        composer.addImport("org.openelis.gwt.common.Util");

        findImports(doc.getElementsByTagName("screen").item(0));

        composer.addImport("org.openelis.gwt.screen.Screen");
        composer.addImport("org.openelis.gwt.screen.Tab");
        composer.addImport("org.openelis.gwt.screen.Shortcut");
        composer.addImport("com.google.gwt.event.dom.client.HasFocusHandlers");
        composer.addImplementedInterface("ScreenDefInt");
        
        sw = composer.createSourceWriter(context,printWriter);
	  
		sw.println("protected ScreenPanel panel;");
		sw.println("protected HashMap<String,Widget> widgets;");
		sw.println("protected HashMap<Widget,Tab> tabs;");
		sw.println("protected ArrayList<Shortcut> shortcuts;");
		sw.println("public String name;");
		
		sw.println("");
		sw.println("public "+className+"_"+lang+"() {");
		sw.println("widgets = new HashMap<String,Widget>();");
		sw.println("tabs = new HashMap<Widget,Tab>();");
		sw.println("shortcuts = new ArrayList<Shortcut>();");
		sw.println("panel = new ScreenPanel();");
		sw.println("createPanel();");
		sw.println("}");
		
		sw.println("public void setWidgets(HashMap<String,Widget> widgets) {");
		sw.println("this.widgets = widgets;");
		sw.println("}");
		sw.println("public HashMap<String,Widget> getWidgets() {");
		sw.println("return widgets;");
		sw.println("}");
		sw.println("public Widget getWidget(String key) {");
		sw.println("return widgets.get(key);");
		sw.println("}");
		sw.println("public void setWidget(Widget widget, String key) {");
		sw.println("widgets.put(key,widget);");
		sw.println("}");
		sw.println("public ScreenPanel getPanel() {");
		sw.println("return panel;");
		sw.println("}");
		sw.println("public String getName() {");
		sw.println("return name;");
		sw.println("}");
		sw.println("public void setName(String name) {");
		sw.println("this.name = name;");
		sw.println("}");
		sw.println("public ArrayList<Shortcut> getShortcuts() {");
		sw.println("return shortcuts;");
		sw.println("}");
		sw.println("public HashMap<Widget,Tab> getTabs() {");
		sw.println("return tabs;");
		sw.println("}");
		sw.println("private void createPanel() {");
	    
		try {
	    	count = 0;
	    	createWidgets();
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	    
	    sw.println("}");
	    sw.println("}");
	    
		context.commit(logger, printWriter);
		
	}

     
    public void createWidgets() throws Exception{
    	String name,key;
    	Node attrib,screen,widget;
    	NodeList widgets;
    	
    	screen = doc.getElementsByTagName("screen").item(0);
    	name = (attrib = screen.getAttributes().getNamedItem("name")) != null ? attrib.getNodeValue() : null;
    	
    	if(name != null) 
    		sw.println("name = \""+name+"\";");
    	
        widgets = screen.getChildNodes();
        for (int i = 0; i < widgets.getLength(); i++) {
        	widget = widgets.item(i);
            if (widget.getNodeType() == Node.ELEMENT_NODE) {
                createWidget(widget,0);
                key = (attrib = widget.getAttributes().getNamedItem("key")) != null ? attrib.getNodeValue() : null;
                
                if(key != null)
                	sw.println("setWidget(wid0, \""+key+"\");");
                
                sw.println("panel.add(wid0);");
                break;
            }
        }
    }
    
    public void findImports(Node node) {
        NodeList widgets;
        Node widget;
        
        widgets = node.getChildNodes();
        for (int i = 0; i < widgets.getLength(); i++) {
        	widget = widgets.item(i);
            if (widget.getNodeType() == Node.ELEMENT_NODE) {
            	String widName = widget.getNodeName();
            	if(factoryMap.containsKey(widName)){
            		factoryMap.get(widName).addImport();
            	}
            	if(widgets.item(i).hasChildNodes())
            		findImports(widgets.item(i));
            }
        }
        
    }
    
    
    public boolean createWidget(Node node, int id) {
    	String key,widName;
    	Node attrib;
    	
    	if(node.getNodeName().equals("code")){
    		sw.println(node.getTextContent());
    		return false;
    	}
    	
        widName = node.getNodeName();
        factoryMap.get(widName).getNewInstance(node,id);
        
        key = (attrib = node.getAttributes().getNamedItem("key")) != null ? attrib.getNodeValue() : null; 
        if(key != null) 
        	sw.println("widgets.put(\""+key+"\", wid"+id+");");
        
        return true;
    }
    
    private boolean loadWidget(Node node, int id){
    	NodeList inputs;
        Node input = null;
        int i = 0;
        
        if (node.getNodeName().equals("widget")) {
            inputs = node.getChildNodes();
            while(inputs.item(i).getNodeType() != Node.ELEMENT_NODE) 
            	i++;
            input = inputs.item(i);
        } else
            input = node;

        return createWidget(input,id);
        
    }
    
    private void setDefaults(Node node, String wid) {
    	String[] style;
    	String width,height,tip,visible;
    	Node attrib;
    	
    	style = (attrib = node.getAttributes().getNamedItem("style")) != null ? attrib.getNodeValue().split(",") : null;
    	width = (attrib = node.getAttributes().getNamedItem("width")) != null ? attrib.getNodeValue() : null;
    	height = (attrib = node.getAttributes().getNamedItem("height")) != null ? attrib.getNodeValue() : null;
    	tip = (attrib = node.getAttributes().getNamedItem("tip")) != null ? attrib.getNodeValue() : null;
    	visible = (attrib = node.getAttributes().getNamedItem("visible")) != null ? attrib.getNodeValue() : "true";
    	
        if (style != null){
            sw.println(wid+".setStyleName(\""+style[0]+"\");");
            for(int i = 1; i < style.length; i++){
                sw.println(wid+".addStyleName(\""+style[i]+"\");");
            }
        }
        
        if (width != null)
            sw.println(wid+".setWidth(\""+width+"\");");
        
        if (height != null)
            sw.println(wid+".setHeight(\""+height+"\");");
        
        if (tip != null)
            sw.println(wid+".setTitle(\""+tip+"\");");
        
        sw.println(wid+".setVisible("+visible+");");
        
    }
    
    private interface Factory {
    	public void getNewInstance(Node node,int id);
    	public void addImport();
    }
    
    public void addShortcutHandler(Node node,String wid) {
    	String ctrl = "false",shift = "false", alt = "false", shortcut;
    	char key;
    	
    	shortcut = node.getAttributes().getNamedItem("shortcut").getNodeValue();
    	List<String> keys = Arrays.asList(shortcut.split("\\+"));
    	
    	if(keys.contains("ctrl"))
    		ctrl = "true";
    	if(keys.contains("shift"))
    		shift = "true";
    	if(keys.contains("alt"))
    		alt = "true";
    	
    	key = shortcut.charAt(shortcut.length()-1);
    	
    	sw.println("shortcuts.add(new Shortcut("+ctrl+","+shift+","+alt+",'"+key+"',"+wid+"));");
    }	
    
    public void addTabHandler(Node node,String wid) {
    	String[] tab;
    	String key;
    	Node attrib;
    	
    	tab = (attrib = node.getAttributes().getNamedItem("tab")) != null ? attrib.getNodeValue().split(",") : null;
    	key = (attrib = node.getAttributes().getNamedItem("key")) != null ? attrib.getNodeValue() : null;
    	
    	sw.println("tabs.put("+wid+",new Tab(\""+key+"\",\""+tab[0]+"\",\""+tab[1]+"\"));");
    	 
    }
    
    private static HashMap<String,Factory> factoryMap = new HashMap<String,Factory>();
    
    {

    	factoryMap.put("AbsolutePanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String overflow,x,y,align;
    			NodeList widgets;
    			Node attrib,widget;
    			
    			overflow = (attrib = node.getAttributes().getNamedItem("overflow")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("AbsolutePanel wid"+id+" = new AbsolutePanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenAbsolute\");");
    	        
    	        if(overflow != null)
    	            sw.println("DOM.setStyleAttribute(wid"+id+".getElement(),\"overflow\","+overflow+");");
    	        
    	        widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	        	widget = widgets.item(k);
    	            if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	                if(!loadWidget(widget,child)){
    	                	count--;
    	                	continue;
    	                }
    	                x = (attrib = widget.getAttributes().getNamedItem("x")) != null ? attrib.getNodeValue() : "-1";
    	                y = (attrib = widget.getAttributes().getNamedItem("y")) != null ? attrib.getNodeValue() : "-1";
    	                align = (attrib = widget.getAttributes().getNamedItem("align")) != null ? attrib.getNodeValue() : null;
    	                
    	                if(align != null)
    	                    sw.println("DOM.setElementProperty(wid"+id+".getElement(),\"align\",\""+align+"\");");
    	                
    	                sw.println("wid"+id+".add(wid"+child+","+x+","+y+");");
    	            }
    	        }
    	        setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.AbsolutePanel");
    			composer.addImport("com.google.gwt.user.client.DOM");
    		}
    	});
    	
        factoryMap.put("autoComplete", new Factory() {
            public void getNewInstance(Node node, int id) {
            	String fcase,visibleItems,enabled,delay,required,tableWidth;
                Element table,label,col; 
                NodeList cols,list;
                Node attrib;
                
    		    visibleItems = (attrib = node.getAttributes().getNamedItem("visibleItems")) != null ? attrib.getNodeValue() : "10";
    		    fcase = (attrib = node.getAttributes().getNamedItem("case")) != null ? attrib.getNodeValue() : "MIXED";
    		    table = (list = ((Element)node).getElementsByTagName("table")).getLength() > 0 ? (Element)list.item(0) : null;
    		    enabled = (attrib = node.getAttributes().getNamedItem("enabled")) != null ? attrib.getNodeValue() : null;
    		    delay = (attrib = node.getAttributes().getNamedItem("delay")) != null ? attrib.getNodeValue() : null;
    		    required = (attrib = node.getAttributes().getNamedItem("required")) != null ? attrib.getNodeValue() : null;
    		    tableWidth = (attrib = node.getAttributes().getNamedItem("tableWidth")) != null ? attrib.getNodeValue() : null;
    		    
                sw.println("AutoComplete wid"+id+" = new AutoComplete();");
                sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
                sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
                sw.println("wid"+id+".addFocusHandler(panel);");
                
                if(node.getAttributes().getNamedItem("tab") != null) 
                	addTabHandler(node,"wid"+id);
                
                sw.println("wid"+id+".setCase(Case.valueOf(\""+fcase+"\"));");
                sw.println("wid"+id+".setVisibleItemCount("+visibleItems+");");
 
                if(table == null) {
                    table = doc.createElement("table");
                    table.setAttribute("rows", visibleItems);
                    if(tableWidth != null)
                    	table.setAttribute("width", tableWidth);
                    cols = ((Element)node).getElementsByTagName("col");
				    int length = cols.getLength();
				    if(length > 0){
				        for(int i = 0; i < length; i++){
				        	col = (Element)cols.item(0);
				        	if(col.getNodeType() != Node.ELEMENT_NODE || !col.getNodeName().equals("col"))
                                continue;
				        	/*
				            if(!col.hasChildNodes()) {
				                label = doc.createElement("label");
				                label.setAttribute("field", "String");
				                cols.item(0).appendChild(label);
				            }
				            */
				            table.appendChild(col);  
				        }
                    }else{
                        col = doc.createElement("col");
                        col.setAttribute("width", node.getAttributes().getNamedItem("width").getNodeValue());
                        label = doc.createElement("label");
                        label.setAttribute("field", "String");
                        col.appendChild(label);
                        table.appendChild(col);
                    }
                }
                factoryMap.get("table").getNewInstance(table,1000+id);
                sw.println("wid"+id+".setPopupContext(wid"+(1000+id)+");");
                
                if(enabled != null)
                	sw.println("wid"+id+".setEnabled("+enabled+");");
                
                if(delay != null)
                	sw.println("wid"+id+".setDelay("+delay+");");
                
                if(required != null)
                	sw.println("wid"+id+".setRequired("+required+");");
                
                setDefaults(node,"wid"+id);
               
            }
            public void addImport() {
                composer.addImport("org.openelis.gwt.widget.AutoComplete");
                composer.addImport("org.openelis.gwt.widget.table.Table");
                composer.addImport("org.openelis.gwt.widget.TextBox.Case");
                composer.addImport("org.openelis.gwt.widget.table.Column");
                composer.addImport("org.openelis.gwt.widget.table.LabelCell");
                composer.addImport("org.openelis.gwt.widget.table.TextBoxCell");
                composer.addImport("org.openelis.gwt.widget.table.AutoCompleteCell");
                composer.addImport("org.openelis.gwt.widget.table.DropdownCell");
                composer.addImport("org.openelis.gwt.widget.table.CheckBoxCell");
                composer.addImport("org.openelis.gwt.widget.table.CalendarCell");
            }
        });
        
    	factoryMap.put("browser", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			String sizeToWindow,limit;
    			Node attrib;
    			
    			sizeToWindow = (attrib = node.getAttributes().getNamedItem("sizeToWindow")) != null ? attrib.getNodeValue() : "false";
    			limit = (attrib = node.getAttributes().getNamedItem("winLimit")) != null ? attrib.getNodeValue() : "10";
    			
    			sw.println("Browser wid"+id+" = new Browser("+sizeToWindow+","+limit+");");
    	        sw.println("wid"+id+".setStyleName(\"ScreenWindowBrowser\");");
    	        setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.Browser");
    		}
    	});
    	
    	factoryMap.put("button", new Factory() {
    		public void getNewInstance(Node node, int id) {    			
    			String toggle,action,enabled,wrap,icon,text;
    			NodeList widgets;
    			Node attrib,widget;
    			
    			toggle = (attrib = node.getAttributes().getNamedItem("toggle")) != null ? attrib.getNodeValue() : "false";
    			action = (attrib = node.getAttributes().getNamedItem("action")) != null ? attrib.getNodeValue() : null;
    			wrap = (attrib = node.getAttributes().getNamedItem("wrap")) != null ? attrib.getNodeValue() : "true";
    			enabled = (attrib = node.getAttributes().getNamedItem("enabled")) != null ? attrib.getNodeValue() : null;
    			icon = (attrib = node.getAttributes().getNamedItem("icon")) != null ? attrib.getNodeValue() : "";
    			text = (attrib = node.getAttributes().getNamedItem("text")) != null ? attrib.getNodeValue() : "";
    			
    			if(text.equals("") && icon.equals(""))
    				sw.println("Button wid"+id+" = new Button();");
    			else
    				sw.println("Button wid"+id+" = new Button(\""+icon+"\",\""+text+"\");");

    			
				if(node.getAttributes().getNamedItem("tab") != null) 
					addTabHandler(node,"wid"+id);
				
				if(node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);			    
    			
                sw.println("wid"+id+".setToggles("+toggle+");");
    	        
    	        if(action != null)
    	            sw.println("wid"+id+".setAction(\""+action+"\");");
    	        
    	        widgets = node.getChildNodes();
    	        for (int l = 0; l < widgets.getLength(); l++) {
    	        	widget = widgets.item(l);
    	            if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	            	if(!loadWidget(widget,child)){
    	            		count--;
     	            	    continue;
     	                }
   	            		sw.println("wid"+id+".setDisplay(wid"+child+","+wrap+");");
    	            }
    	        }

    	        if(enabled != null)
    	        	sw.println("wid"+id+".setEnabled("+enabled+");");
    	        
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.Button");
    		}
    	});      	
        
    	factoryMap.put("buttonGroup", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String enabled;
    			NodeList widgets;
    			Node attrib,widget;
    			
    			enabled = (attrib = node.getAttributes().getNamedItem("enabled")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("ButtonGroup wid"+id+" = new ButtonGroup();");
    	        
    			widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	        	widget = widgets.item(k);
    	            if(widget.getNodeType() == Node.ELEMENT_NODE){
    	               int child = ++count;
    	               if(!loadWidget(widget,child)){
    	            	   count--;
    	            	   continue;
    	               }
    	               sw.println("wid"+id+".setButtons(wid"+child+");");
    	               break;
    	            }
    	        }
    	        setDefaults(node,"wid"+id);
    	        
    	        if (enabled != null)
    	        	sw.println("wid"+id+".setEnabled("+enabled+");");
    	        
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.ButtonGroup");
    		}
    	});        

        factoryMap.put("calendar", new Factory() {
            public void getNewInstance(Node node, int id) {
            	String enabled;
            	Node attrib;
            	
            	enabled = (attrib = node.getAttributes().getNamedItem("enabled")) != null ? attrib.getNodeValue() : null;
            	
                sw.println("Calendar wid"+id+" = new Calendar();");
                
                if (node.getAttributes().getNamedItem("tab") != null) 
                	addTabHandler(node,"wid"+id);

                if (node.getAttributes().getNamedItem("shortcut") != null)
                    addShortcutHandler(node,"wid"+id);

                setDefaults(node,"wid"+id);

                factoryMap.get("Date").getNewInstance(node, id);
                sw.println("wid"+id+".setHelper(field"+id+");");
                
                sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
                sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
                sw.println("wid"+id+".addFocusHandler(panel);");
                
                if(enabled != null)
                	sw.println("wid"+id+".setEnabled("+enabled+");");

            }
            public void addImport() {
                composer.addImport("org.openelis.gwt.widget.calendar.Calendar");
            }
        });
        
    	factoryMap.put("check", new Factory() {
    		public void getNewInstance(Node node, int id) {
            	String threeState;
            	Node attrib;
            	
            	threeState = (attrib = node.getAttributes().getNamedItem("threeState")) != null ? attrib.getNodeValue() : null;
            	
    			sw.println("CheckBox wid"+id+" = new CheckBox();");
    			
				if(node.getAttributes().getNamedItem("tab") != null)
					addTabHandler(node,"wid"+id);

				if(node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
					
    	        if(threeState != null)
    	            sw.println("wid"+id+".setType(CheckBox.CheckType.THREE_STATE);");
    	        
    	        setDefaults(node, "wid"+id);
    	            	        
    	        sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
    	        sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
    	        
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.CheckBox");
    		}
    	});
    	
    	factoryMap.put("CollapsePanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String open;
    			NodeList widgets;
    			Node attrib,widget;
    			
    			open = (attrib = node.getAttributes().getNamedItem("open")) != null ? attrib.getNodeValue() : "false";
    			
  				sw.println("CollapsePanel wid"+id+" = new CollapsePanel("+open+");");

  				if (node.getChildNodes().getLength() > 0){
    		       widgets = node.getChildNodes();
    		       for (int k = 0; k < widgets.getLength(); k++) {
    		    	   widget = widgets.item(k);
    		           if (widget.getNodeType() == Node.ELEMENT_NODE) {
    		        	   int child = ++count;
    		        	   if(!loadWidget(widget,child)){
        	            	   count--;
        	            	   continue;
        	               }
    		               //sw.println("wid"+child+".setHeight(\"100%\");");
    		               //sw.println("wid"+child+".setWidth(\"auto\");");
    		               sw.println("wid"+id+".setContent(wid"+child+");");
    		           }
    		       }
    		    }
  				
    		    setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.CollapsePanel");
    		}
    	});
    	
        factoryMap.put("Date", new Factory() {
            public void getNewInstance(Node node, int id){
            	String begin,end,pattern;
            	Node attrib;
            	
            	begin = (attrib = node.getAttributes().getNamedItem("begin")) != null ? attrib.getNodeValue() : "0";
            	end = (attrib = node.getAttributes().getNamedItem("end")) != null ? attrib.getNodeValue() : "2";
            	pattern = (attrib = node.getAttributes().getNamedItem("pattern")) != null ? attrib.getNodeValue() : "yyyy-MM-dd";
            	
                sw.println("DateHelper field"+id+" = new DateHelper();");
                sw.println("field"+id+".setBegin((byte)"+begin+");");
                sw.println("field"+id+".setEnd((byte)"+end+");");
                sw.println("field"+id+".setPattern(\""+pattern+"\");");
            }
            public void addImport() {
                composer.addImport("org.openelis.gwt.widget.DateHelper");
            }
        });    	
    	
    	factoryMap.put("DeckPanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String tab;
    			NodeList decks,widgets;
    			Node attrib,deck,widget;
    			
    			sw.println("DeckPanel wid"+id+" = new DeckPanel();");
    	        sw.println("wid"+id+".setStyleName(\"gwt-TabPanelBottom\");");

    	        decks = ((Element)node).getElementsByTagName("deck");
    	        for (int k = 0; k < decks.getLength(); k++) {
    	        	deck = decks.item(k);
    	        	tab = (attrib = deck.getAttributes().getNamedItem("tab")) != null ? attrib.getNodeValue() : "null";
    	            widgets = decks.item(k).getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	            	widget = widgets.item(l);
    	                if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	                	int child = ++count;
    	                	if(!loadWidget(widget,child)){
    	                		count--;
    	    	            	continue;
    	    	            }
   	                		sw.println("wid"+id+".add(wid"+child+",\""+tab+"\");");
    	                }
    	            }
    	        }
    	        sw.println("wid"+id+".showWidget(0);");
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.DeckPanel");
    			composer.addImport("com.google.gwt.user.client.ui.ScrollPanel");
    		}
    	});
    	
    	factoryMap.put("DecoratorPanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			NodeList widgets;
    			Node widget;
    			
    			sw.println("DecoratorPanel wid"+id+" = new DecoratorPanel();");
    	        sw.println("wid"+id+".setStyleName(\"ConfirmWindow\");");

    	        widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	        	widget = widgets.item(k);
    	            if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	                if(!loadWidget(widget,count)){
    	                	count--;
    	                	continue;
    	                }
    	                sw.println("wid"+id+".add(wid"+child+");");
    	                break;
    	            }
    	        }
    	        setDefaults(node,"wid"+id);    	       
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.DecoratorPanel");
    			composer.addImport("com.google.gwt.user.client.DOM");
    		}
    	});    	    	    	
    	
    	factoryMap.put("diagram", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("Diagram wid"+id+" = new Diagram();");
    	        setDefaults(node, "wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.Diagram");
    		}
    	});

    	factoryMap.put("DockPanel", new Factory() {
    		public void getNewInstance(Node node, int id){
    			String spacing,dir,halign,valign;
    			NodeList widgets;
    			Node attrib,widget;
    			
    			spacing = (attrib = node.getAttributes().getNamedItem("spacing")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("DockPanel wid"+id+" = new DockPanel();");
    	        sw.println("wid"+id+".addStyleName(\"ScreenDock\");");
    	        
    	        if (spacing != null)
    	            sw.println("wid"+id+".setSpacing("+spacing+");");
    	        
    	        widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	        	widget = widgets.item(k);
    	            if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	            	if(!loadWidget(widget,child)){
    	            		count--;
     	            	    continue;
     	                }
    	                dir = (attrib = widget.getAttributes().getNamedItem("dir")) != null ? attrib.getNodeValue() : "CENTER";
    	                halign = (attrib = widget.getAttributes().getNamedItem("align")) != null ? attrib.getNodeValue() : "LEFT";
    	                valign = (attrib = widget.getAttributes().getNamedItem("valign")) != null ? attrib.getNodeValue() : "TOP";

   	                    sw.println("wid"+id+".add(wid"+child+", DockPanel."+dir.toUpperCase()+");");
                        sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+",HasAlignment.ALIGN_"+halign+");");
                        sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+",HasAlignment.ALIGN_"+valign+");");    	               
    	            }
    	        }
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.DockPanel");
    			composer.addImport("com.google.gwt.user.client.ui.HasAlignment");
    		}
    	});
    	
        factoryMap.put("Double", new Factory() {
            public void getNewInstance(Node node, int id) {
            	String pattern;
            	Node attrib;
            	 
            	pattern = (attrib = node.getAttributes().getNamedItem("pattern")) != null ? attrib.getNodeValue() : null;

                sw.println("DoubleHelper field"+id+" = new DoubleHelper();");
                
                if (pattern != null) 
                    sw.println("field"+id+".setPattern(\""+pattern+"\");");
                
            }
            public void addImport() {
                composer.addImport("org.openelis.gwt.widget.DoubleHelper");
            }
        });
    	
    	factoryMap.put("dropdown", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String fcase,visibleItems,field,width,enabled,required;
    			Node attrib;
    			NodeList list,cols;
    		    Element table,label,col;
    		    
    		    field = (attrib = node.getAttributes().getNamedItem("field")) != null ? attrib.getNodeValue() : "Integer";
    		    visibleItems = (attrib = node.getAttributes().getNamedItem("visibleItems")) != null ? attrib.getNodeValue() : "10";
    		    fcase = (attrib = node.getAttributes().getNamedItem("case")) != null ? attrib.getNodeValue() : "MIXED";
    		    table = (list = ((Element)node).getElementsByTagName("table")).getLength() > 0 ? (Element)list.item(0) : null;
    		    width = (attrib = node.getAttributes().getNamedItem("width")) != null ? attrib.getNodeValue() : "-1";
    		    enabled = (attrib = node.getAttributes().getNamedItem("enabled")) != null ? attrib.getNodeValue() : null;
    		    required = (attrib = node.getAttributes().getNamedItem("required")) != null ? attrib.getNodeValue() : null;
    		    
    		    if(field.equals("Integer")) 
    		        sw.println("Dropdown<Integer> wid"+id+" = new Dropdown<Integer>();");
    		    else
    		        sw.println("Dropdown<String> wid"+id+" = new Dropdown<String>();");
    		    
				sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
				
				if(node.getAttributes().getNamedItem("tab") != null) 
					addTabHandler(node,"wid"+id);
				
				if(node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node, "wid"+id);
				
				sw.println("wid"+id+".setCase(Case.valueOf(\""+fcase+"\"));");                
                sw.println("wid"+id+".setVisibleItemCount("+visibleItems+");");

				if(table == null) {
				    table = doc.createElement("table");
				    table.setAttribute("rows", visibleItems);
				    table.setAttribute("width",width);
				    cols = ((Element)node).getElementsByTagName("col");
				    int length = cols.getLength();
				    if(length > 0){
				        for(int i = 0; i < length; i++){
				        	col = (Element)cols.item(0);
				        	if(col.getNodeType() != Node.ELEMENT_NODE || !col.getNodeName().equals("col"))
                                continue;
				        	/*
				            if(!col.hasChildNodes()) {
				                label = doc.createElement("label");
				                label.setAttribute("field", "String");
				                cols.item(0).appendChild(label);
				            }
				            */
				            table.appendChild(col);  
				        }
				    }else{
				        col = doc.createElement("col");
				        col.setAttribute("width", node.getAttributes().getNamedItem("width").getNodeValue());
				        label = doc.createElement("label");
				        label.setAttribute("field", "String");
				        col.appendChild(label);
				        table.appendChild(col);
				    }
				}
				factoryMap.get("table").getNewInstance(table,1000+id);
				sw.println("wid"+id+".setPopupContext(wid"+(1000+id)+");");
				
				if(enabled != null)
					sw.println("wid"+id+".setEnabled("+enabled+");");
				
				if(required != null)
					sw.println("wid"+id+".setRequired("+required+");");
				
				setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.Dropdown");
                composer.addImport("org.openelis.gwt.widget.table.Table");
                composer.addImport("org.openelis.gwt.widget.table.Column");
                composer.addImport("org.openelis.gwt.widget.table.LabelCell");
                composer.addImport("org.openelis.gwt.widget.TextBox.Case");
    		}
    	});
    	
    	factoryMap.put("editbox",new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String enabled;
    			Node attrib;
    			
    			enabled = (attrib = node.getAttributes().getNamedItem("enabled")) != null ? attrib.getNodeValue() : "true";
    			
    			sw.println("EditBox wid"+id+" = new EditBox();");
    			factoryMap.get("String").getNewInstance(node, id);
    			sw.println("wid"+id+".setHelper(field"+id+");");
    			
				if(node.getAttributes().getNamedItem("tab") != null)
					addTabHandler(node,"wid"+id);

				if(node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
					
    			setDefaults(node,"wid"+id);
    	        
   	        	sw.println("wid"+id+".setEnabled("+enabled+");");

    			sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
    			sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
    			
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.EditBox");
    		}
    	});            	
    	
    	factoryMap.put("fileUpload", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String service;
    			Node attrib;
    			
    			service = (attrib = node.getAttributes().getNamedItem("service")) != null ? attrib.getNodeValue() : "";
    			sw.println("FileUploadWidget wid"+id+" = new FileUploadWidget(\""+service+"\");");
    			setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.fileupload.FileUploadWidget");
    		}
    	});   
    	
        factoryMap.put("FocusPanel", new Factory() {
            public void getNewInstance(Node node, int id) {
            	NodeList widgets;
            	Node widget;
            	
                sw.println("FocusPanel wid"+id+" = new FocusPanel();");
                sw.println("wid"+id+".setStyleName(\"ScreenAbsolute\");");
                
                widgets = node.getChildNodes();
                for (int k = 0; k < widgets.getLength(); k++) {
                	widget = widgets.item(k);
                    if (widget.getNodeType() == Node.ELEMENT_NODE) {
                        int child = ++count;
                        if(!loadWidget(widget,count)){
                            count--;
                            continue;
                        }
                        sw.println("wid"+id+".setWidget(wid"+child+");");
                        break;
                    }
                }
                setDefaults(node,"wid"+id);
            }
            public void addImport() {
                composer.addImport("com.google.gwt.user.client.ui.FocusPanel");
            }
        });  
    	
    	factoryMap.put("Grid", new Factory() {			
			public void getNewInstance(Node node, int id) {
				String numRows,numCols,style,align,valign,text,padding,spacing;
				Node attrib,widget;
				NodeList rows,widgets;
				String[] styles;
				
				numRows = (attrib = node.getAttributes().getNamedItem("rows")) != null ? attrib.getNodeValue() : "1";
				numCols = (attrib = node.getAttributes().getNamedItem("cols")) != null ? attrib.getNodeValue() : "1";
				padding = (attrib = node.getAttributes().getNamedItem("padding")) != null ? attrib.getNodeValue() : "0";
				spacing = (attrib = node.getAttributes().getNamedItem("spacing")) != null ? attrib.getNodeValue() : "0";
				
				sw.println("Grid wid"+id+" = new Grid("+numRows+","+numCols+");");
				sw.println("wid"+id+".setCellPadding("+padding+");");
				sw.println("wid"+id+".setCellSpacing("+spacing+");");
				
    	        rows = ((Element)node).getElementsByTagName("row");
    	        for (int k = 0; k < rows.getLength(); k++) {
    	            widgets = rows.item(k).getChildNodes();
    	            int w = -1;
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	            	widget = widgets.item(l);
    	                if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	                    w++;
    	                    if(widget.getNodeName().equals("cell")) {
    	                    	styles = (attrib = widget.getAttributes().getNamedItem("style")) != null ? attrib.getNodeValue().split(",") : null;
    	                    	text = (attrib = widget.getAttributes().getNamedItem("text")) != null ? attrib.getNodeValue().trim() : null;
    	                    	align   = (attrib = widget.getAttributes().getNamedItem("halign"))   != null ? attrib.getNodeValue() : null;
        	                    valign  = (attrib = widget.getAttributes().getNamedItem("valign"))  != null ? attrib.getNodeValue() : null;
    	                    	
    	                    	 if (styles != null){
    	                             sw.println("wid"+id+".getCellFormatter().setStyleName("+k+","+w+","+"\""+styles[0]+"\");");
    	                             for(int i = 1; i < styles.length; i++){
    	                            	 sw.println("wid"+id+".getCellFormatter().addStyleName("+k+","+w+","+"\""+styles[i]+"\");");
    	                             }
    	                         }
    	                    	
    	                    	if(text != null && !text.equals(""))
    	                    		sw.println("wid"+id+".setText("+k+","+w+",\""+text+"\");");
    	                    	
    	                      	if (align != null) 
  	                    			sw.println("wid"+id+".getCellFormatter().setHorizontalAlignment("+k+","+w+",HasAlignment.ALIGN_"+align.toUpperCase()+");");

    	                    	if (valign != null) 
  	                    			sw.println("wid"+id+".getCellFormatter().setVerticalAlignment("+k+","+w+",HasAlignment.ALIGN_"+valign.toUpperCase()+");");
    	                    	
    	                    	continue;
    	                    }
    	                    
    	                    int child = ++count;
    	                    if(!loadWidget(widget,child)){
    	                    	count--;
    	                    	continue;
    	                    }
    	                    
    	                    style   = (attrib = widget.getAttributes().getNamedItem("style"))   != null ? attrib.getNodeValue() : null;
    	                    align   = (attrib = widget.getAttributes().getNamedItem("halign"))   != null ? attrib.getNodeValue() : null;
    	                    valign  = (attrib = widget.getAttributes().getNamedItem("valign"))  != null ? attrib.getNodeValue() : null;
    	                    
  	                    	sw.println("wid"+id+".setWidget("+k+","+w+",wid"+child+");");
    	                    
    	                    if(widget.getNodeName().equals("widget")) {    	                    	
    	                    	if (style != null)
    	                    		sw.println("wid"+id+".getCellFormatter().addStyleName("+k+","+w+","+"\""+style+"\");");
    	                    	
    	                    	if (align != null) 
  	                    			sw.println("wid"+id+".getCellFormatter().setHorizontalAlignment("+k+","+w+",HasAlignment.ALIGN_"+align.toUpperCase()+");");

    	                    	if (valign != null) 
  	                    			sw.println("wid"+id+".getCellFormatter().setVerticalAlignment("+k+","+w+",HasAlignment.ALIGN_"+valign.toUpperCase()+");");
    	                    }
    	                }
    	            }
    	            
    	            style = (attrib = rows.item(k).getAttributes().getNamedItem("style")) != null ? attrib.getNodeValue() : null;
    	            if (style != null) 
    	            	sw.println("wid"+id+".getRowFormatter().addStyleName("+k+",\""+style+"\");");
    	  
    	        }
    	        setDefaults(node,"wid"+id);
				
			}
			
			public void addImport() {
				composer.addImport("com.google.gwt.user.client.ui.Grid");
			}
		});
    	
    	factoryMap.put("HorizontalPanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String spacing,halign,valign;
    			NodeList widgets;
    			Node attrib,widget;
    			
    			spacing = (attrib = node.getAttributes().getNamedItem("spacing")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("HorizontalPanel wid"+id+" = new HorizontalPanel();");
    			
    			if (spacing != null)
    				sw.println("wid"+id+".setSpacing("+spacing+");");
    			
    			widgets = node.getChildNodes();
    			for (int k = 0; k < widgets.getLength(); k++) {
    				widget = widgets.item(k);
    				if (widget.getNodeType() == Node.ELEMENT_NODE) {
    					int child = ++count;
    					if(!loadWidget(widget,child)){
     	            	   count--;
     	            	   continue;
     	                }
    				    halign = (attrib = widget.getAttributes().getNamedItem("halign")) != null ? attrib.getNodeValue().toUpperCase() : "LEFT";
    				    valign = (attrib = widget.getAttributes().getNamedItem("valign")) != null ? attrib.getNodeValue().toUpperCase() : "TOP";
    				    
    					sw.println("wid"+id+".add(wid"+child+");");
						sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+", HasAlignment.ALIGN_"+halign+");");
						sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+", HasAlignment.ALIGN_"+valign+");");
    				}
    			}
    			sw.println("wid"+id+".setStyleName(\"ScreenPanel\");");
    			setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.HorizontalPanel");
    			composer.addImport("com.google.gwt.user.client.ui.HasAlignment");
    		}
    	});
    	
    	factoryMap.put("HorizontalSplitPanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String splitPos;
    			NodeList sections,widgets;
    			Node attrib,section,widget;
    			
    			splitPos = (attrib = node.getAttributes().getNamedItem("splitpos")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("final HorizontalSplitPanel wid"+id+" = new HorizontalSplitPanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenSplit\");");
    	        sections = ((Element)node).getElementsByTagName("section");
    	        for (int k = 0; k < sections.getLength(); k++) {
    	        	section = sections.item(k);
    	            widgets = section.getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	            	widget = widgets.item(l);
    	                if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	                	int child = ++count;
    	                	if(!loadWidget(widget,child)){
    	                		count--;
    	    	            	continue;
    	    	            }
    	                    if (k == 0) 
    	                    	sw.println("wid"+id+".setLeftWidget(wid"+child+");");        		
    	                    else
   	                            sw.println("wid"+id+".setRightWidget(wid"+child+");");
    	                }
    	            }
    	        }
    	        
    	        if (splitPos != null) 
   	                sw.println("wid"+id+".setSplitPosition("+splitPos+");");
    	        
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.HorizontalSplitPanel");
    		}
    	});
    	
    	factoryMap.put("html", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("HTML wid"+id+" = new HTML();");
    	        if(node.getFirstChild() != null)
    	            sw.println("wid"+id+".setHTML(\""+node.getFirstChild().getNodeValue()+"\");");
    	        sw.println("wid"+id+".setStyleName(\"ScreenHTML\");");
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.HTML");
    		}
    	});
    	
    	factoryMap.put("image", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			//do nothing for now 
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.table.ImageCell");
    		}
    	});
    	
        factoryMap.put("Integer", new Factory(){
            public void getNewInstance(Node node, int id) {
            	String pattern;
            	Node attrib;
            	
            	pattern = (attrib = node.getAttributes().getNamedItem("pattern")) != null ? attrib.getNodeValue() : null;
            	
                sw.println("IntegerHelper field"+id+" = new IntegerHelper();");
                
                if (pattern != null) 
                    sw.println("field"+id+".setPattern(\""+pattern+"\");");
                
            }
            public void addImport() {
                composer.addImport("org.openelis.gwt.widget.IntegerHelper");
            }
        });    	
    	
    	factoryMap.put("label", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String field,text,wordwrap;
    			Node attrib;
    			
    			field = (attrib = node.getAttributes().getNamedItem("field")) != null ? attrib.getNodeValue() : "String";
    			text = (attrib = node.getAttributes().getNamedItem("text")) != null ? attrib.getNodeValue() : null;
    			wordwrap = (attrib = node.getAttributes().getNamedItem("wordwrap")) != null ? attrib.getNodeValue() : "false";
    			
    			factoryMap.get(field).getNewInstance(node, id);
    			
    			if(field.equals("Date"))
    				field = "Datetime";
    			
    			sw.println("org.openelis.gwt.widget.Label<"+field+"> wid"+id+" = new org.openelis.gwt.widget.Label<"+field+">();");
   				sw.println("wid"+id+".setHelper(field"+id+");");

    	        if (text != null)
    	            sw.println("wid"+id+".setText(\""+text+"\");");
    	        
   	            sw.println("wid"+id+".setWordWrap("+wordwrap+");");

    	        sw.println("wid"+id+".setStyleName(\"ScreenLabel\");");
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    		}
    	});
    	
        factoryMap.put("Long", new Factory(){
            public void getNewInstance(Node node,int id) {
            	String pattern;
            	Node attrib;
            	
            	pattern = (attrib = node.getAttributes().getNamedItem("pattern")) != null ? attrib.getNodeValue() : null;
            	
                sw.println("LongHelper field"+id+" = new LongHelper();");
                
                if (pattern != null) 
                    sw.println("field"+id+".setPattern(\""+pattern+"\");");
                
            }
            public void addImport() {
                composer.addImport("org.openelis.gwt.widget.LongHelper");
            }
        });
    	
    	factoryMap.put("menu",new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String icon,display,description,showBelow;
    			boolean selfShow;
    			Node attrib;
    			NodeList items,wid;
    			int child = 0;
    			
    			wid = ((Element)node).getElementsByTagName("menuDisplay");
    			if(wid.getLength() > 0) {
    				items = wid.item(0).getChildNodes();
    				for(int i = 0; i < items.getLength(); i++) {
    					if(items.item(i).getNodeType() == Node.ELEMENT_NODE) {
    						child = ++count;
    						loadWidget(items.item(i),child);
    						break;
    					}
    				}
				   sw.println("Menu wid"+id+"= new Menu(wid"+child+");");
    			}else {
    				icon = (attrib = node.getAttributes().getNamedItem("icon")) != null ? attrib.getNodeValue() : "";
    				display = (attrib = node.getAttributes().getNamedItem("display")) != null ? attrib.getNodeValue() : "";
    				description = (attrib = node.getAttributes().getNamedItem("description")) != null ? attrib.getNodeValue().replaceAll("\"","'") : "";
    			
    				sw.println("Menu wid"+id+"= new Menu(\""+icon+"\",\""+display+"\",\""+description+"\");");
    			}
    			
    			
    			selfShow = (attrib = node.getAttributes().getNamedItem("selfShow")) != null ? Boolean.parseBoolean(attrib.getNodeValue()) : false; 
    			showBelow = (attrib = node.getAttributes().getNamedItem("showBelow")) != null ? attrib.getNodeValue() : "false";
    			
    			if(selfShow) 
    				sw.println("wid"+id+".setSelfShow();");
    			
    			sw.println("wid"+id+".showBelow("+showBelow+");");
    			
    			items = node.getChildNodes();
    			for(int i = 0; i < items.getLength(); i++) {
    				if(items.item(i).getNodeType() == Node.ELEMENT_NODE) {
    					if(items.item(i).getNodeName().equals("separator"))
    						sw.println("wid"+id+".addMenuSeparator();");
    					else if(items.item(i).getNodeName().equals("menuDisplay")){
    						continue;
    					}else{
    						child = ++count;
        					if(!loadWidget(items.item(i),child)){
         	            	   count--;
         	            	   continue;
         	               }
        				   sw.println("wid"+id+".addItem(wid"+child+");");
    					}
    				}
    			}
    	        
    	        setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.Menu");
    		}
    	});
    	
    	factoryMap.put("menuItem", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String icon,display,description,enabled,shortcut,autoClose;
    			Node attrib;
    			
    			icon = (attrib = node.getAttributes().getNamedItem("icon")) != null ? attrib.getNodeValue() : "";
    			display = (attrib = node.getAttributes().getNamedItem("display")) != null ? attrib.getNodeValue() : "";
    			description = (attrib = node.getAttributes().getNamedItem("description")) != null ? attrib.getNodeValue().replaceAll("\"","'") : "";
    			enabled = (attrib = node.getAttributes().getNamedItem("enabled")) != null ? attrib.getNodeValue() : null;
    			shortcut = (attrib = node.getAttributes().getNamedItem("shortcut")) != null ? attrib.getNodeValue() : null;
    			autoClose = (attrib = node.getAttributes().getNamedItem("autoClose")) != null ? attrib.getNodeValue() : "true";
    			
    			sw.println("MenuItem wid"+id+"= new MenuItem(\""+icon+"\",\""+display+"\",\""+description+"\","+autoClose+");");
    			
    			if(enabled != null) 
    				sw.println("wid"+id+".setEnabled("+enabled+");");
    			
    			if(shortcut != null)
    				addShortcutHandler(node,"wid"+id);
    	        
    	        setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.MenuItem");
    			composer.addImport("com.google.gwt.user.client.ui.Widget");
    		}
    	});    	
    	
    	factoryMap.put("menuBar", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			NodeList menus;
    			
    			sw.println("MenuBar wid"+id+"= new MenuBar();");
    			
    			menus = node.getChildNodes();
    			for(int i = 0; i < menus.getLength(); i++) {
    				if(menus.item(i).getNodeType() == Node.ELEMENT_NODE) {
    					int child = ++count;
    					if(!loadWidget(menus.item(i),child)){
    						count--;
    						continue;
    					}
    					sw.println("wid"+id+".addMenu(wid"+child+");");
    				}
    			}

    	        setDefaults(node,"wid"+id);
    		}

    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.MenuBar");
    		}
    	});
    	
    	factoryMap.put("notes", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("NotesPanel wid"+id+" = new NotesPanel();");
    			sw.println("wid"+id+".setStyleName(\"ScreenTable\");");
    			setDefaults(node,"wid"+id);
    		}
    		public void addImport(){
    			composer.addImport("org.openelis.gwt.widget.NotesPanel");
    		}
    	});    	
    	
    	factoryMap.put("password", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String enabled,required;
    			Node attrib;
    			
    			enabled = (attrib = node.getAttributes().getNamedItem("enabled")) != null ? attrib.getNodeValue() : null;
    			required = (attrib = node.getAttributes().getNamedItem("required")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("PassWordTextBox wid"+id+" = new PassWordTextBox();");
    			
				if(node.getAttributes().getNamedItem("tab") != null) 
					addTabHandler(node,"wid"+id);
				
				if(node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
			    
    	        sw.println("wid"+id+".setStyleName(\"ScreenPassword\");");
    	        setDefaults(node, "wid"+id);
    	        
    	        if(enabled != null)
    	        	sw.println("wid"+id+".setEnabled("+enabled+");");
    	        
    	        if(required != null)
    	        	sw.println("wid"+id+".setRequired("+required+");");
    	        
    	        factoryMap.get("String").getNewInstance(node, id);
    	        sw.println("wid"+id+".setHelper(field"+id+");");
    	        
    	        sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
    	        sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
    	        
    		}
    		public void addImport(){
    			composer.addImport("org.openelis.gwt.widget.PassWordTextBox");
    		}
    	});    	
    	
    	factoryMap.put("richtext", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String tools,width,height,enabled;
    			Node attrib;
    			
    			enabled = (attrib = node.getAttributes().getNamedItem("enabled")) != null ? attrib.getNodeValue() : null;
    			width = (attrib = node.getAttributes().getNamedItem("width")) != null ? attrib.getNodeValue() : "100%";
    			height = (attrib = node.getAttributes().getNamedItem("width")) != null ? attrib.getNodeValue() : "300px";
    			tools = (attrib = node.getAttributes().getNamedItem("tools")) != null ? attrib.getNodeValue() : "true";
    			
    			sw.println("RichTextWidget wid"+id+" = new RichTextWidget();"); 
    	        
				if(node.getAttributes().getNamedItem("tab") != null) 
					addTabHandler(node,"wid"+id);

				if(node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
				
    	        sw.println("wid"+id+".init("+tools+");");
    	        sw.println("wid"+id+".area.setSize(\""+width+"\",\""+height+"\");");
    	        
    	        sw.println("wid"+id+".area.setStyleName(\"ScreenTextArea\");");
    	        setDefaults(node, "wid"+id);
    	        if(enabled != null){
    	        	sw.println("wid"+id+".setEnabled("+enabled+");");
    	        }
    	        
    	        sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
    	        sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
    	        
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.richtext.RichTextWidget");
    		}
    	});
    	
    	factoryMap.put("ScrollPanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			NodeList widgets;
    			Node widget;
    			
    			sw.println("ScrollPanel wid"+id+" = new ScrollPanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenAbsolute\");");

    	        widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	        	widget = widgets.item(k);
    	            if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	                if(!loadWidget(widget,count)){
    	                	count--;
    	                	continue;
    	                }
    	                sw.println("wid"+id+".setWidget(wid"+child+");");
    	                break;
    	            }
    	        }
    	        setDefaults(node,"wid"+id);    	       
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.ScrollPanel");
    			composer.addImport("com.google.gwt.user.client.DOM");
    		}
    	});    	
    	
    	factoryMap.put("ScrollTabBar", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			sw.println("ScrollableTabBar wid"+id+" = new ScrollableTabBar();");
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.ScrollableTabBar");
    		}
    	});    	
    	
    	factoryMap.put("StackPanel", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			String text;
    			Node attrib,stack,widget;
    			NodeList stacks,widgets;
    			
    			sw.println("StackPanel wid"+id+" = new StackPanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenStack\");");
    	        stacks = ((Element)node).getElementsByTagName("stack");
    	        for (int k = 0; k < stacks.getLength(); k++) {
    	        	stack = stacks.item(k);
    	            widgets = stack.getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	            	widget = widgets.item(l);
    	                if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	                	int child = ++count;
    	                	if(!loadWidget(widget,child)){
    	                		count--;
    	    	            	continue;
    	    	            }
    	                	text = (attrib = stack.getAttributes().getNamedItem("text")) != null ? attrib.getNodeValue() : "";
    	                    sw.println("wid"+id+".add(wid"+child+", \""+text+"\");");
    	                }
    	            }
    	        }
    	        sw.println("wid"+id+".showStack(0);");
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport(){
    			composer.addImport("com.google.gwt.user.client.ui.StackPanel");
    		}
    	});
    	
        factoryMap.put("String", new Factory() {
            public void getNewInstance(Node node, int id) {
                sw.println("StringHelper field"+id+" = new StringHelper();");
            }
            public void addImport() {
                composer.addImport("org.openelis.gwt.widget.StringHelper");
            }
        });    	
    	
    	factoryMap.put("TabBar", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String text,visible;
    			Node attrib,tab;
    			NodeList tabs;
    			
    			sw.println("TabBar wid"+id+" = new TabBar();");
    			tabs = ((Element)node).getElementsByTagName("tab");
      	        
    			for (int k = 0; k < tabs.getLength(); k++) {
    				tab = tabs.item(k);
    				text = (attrib = tab.getAttributes().getNamedItem("text")) != null ? attrib.getNodeValue() : "";
    				visible = (attrib = tab.getAttributes().getNamedItem("visible")) != null ? attrib.getNodeValue() : "true";
    				
      	        	sw.println("wid"+id+".addTab(\""+text+"\");");
   	        		sw.println("wid"+id+".setTabVisible(wid"+id+".getTabCount() -1, "+visible+");");
      	        }
      	        
    			if(tabs.getLength() > 0)
      	        	sw.println("wid"+id+".selectTab(0);");
    			
    			setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.TabBar");
    		}
    	});
    	
    	factoryMap.put("table", new Factory() {
     	   public void getNewInstance(Node node, int id) {
     		   String key,rows,width,hscroll,vscroll,header,minWidth,name,field,rowHeight,filter,sort,align,multiSelect,fixScroll;
     		   Node attrib,col;
     		   NodeList cols,editor;
     		   
     		   rows = (attrib = node.getAttributes().getNamedItem("rows")) != null ? attrib.getNodeValue() : "10";
     		   width = (attrib = node.getAttributes().getNamedItem("width")) != null ? attrib.getNodeValue() : null;
     		   vscroll = (attrib = node.getAttributes().getNamedItem("vscroll")) != null ? attrib.getNodeValue() : "AS_NEEDED";
     		   hscroll = (attrib = node.getAttributes().getNamedItem("hscroll")) != null ? attrib.getNodeValue() : "AS_NEEDED";
     		   rowHeight = (attrib = node.getAttributes().getNamedItem("rowHeight")) != null ? attrib.getNodeValue() : null;
     		   multiSelect = (attrib = node.getAttributes().getNamedItem("multiSelect")) != null ? attrib.getNodeValue() : null;
     		   fixScroll = (attrib = node.getAttributes().getNamedItem("fixScroll")) != null ? attrib.getNodeValue() : "true";
     		   
     	       sw.println("Table wid"+id+" = new Table();");
     		   
                if(node.getAttributes().getNamedItem("tab") != null) 
                	addTabHandler(node,"wid"+id);
                
                sw.println("wid"+id+".setVisibleRows("+rows+");");
                
                if(width != null)
             	   sw.println("wid"+id+".setWidth("+width+");");
                
                sw.println("wid"+id+".setVerticalScroll(Table.Scrolling.valueOf(\""+vscroll+"\"));");
                sw.println("wid"+id+".setHorizontalScroll(Table.Scrolling.valueOf(\""+hscroll+"\"));");
                
                if(rowHeight != null) 
             	   sw.println("wid"+id+".setRowHeight("+rowHeight+");");
                
                if(multiSelect != null) 
             	   sw.println("wid"+id+".setAllowMultipleSelection("+multiSelect+");");
                
                sw.println("wid"+id+".setFixScrollbar("+fixScroll+");");
     	       
                cols = node.getChildNodes();
                for(int i = 0; i < cols.getLength(); i++) {
                    col = cols.item(i);
                    if(col.getNodeType() != Node.ELEMENT_NODE || !col.getNodeName().equals("col"))
                        continue;
                    key = (attrib = col.getAttributes().getNamedItem("key")) != null ? attrib.getNodeValue() : null;
                    header = (attrib = col.getAttributes().getNamedItem("header")) != null ? attrib.getNodeValue() : null;
                    width = (attrib = col.getAttributes().getNamedItem("width")) != null ? attrib.getNodeValue() : null;
                    minWidth = (attrib = col.getAttributes().getNamedItem("minWidth")) != null ? attrib.getNodeValue() : null;
                    filter = (attrib = col.getAttributes().getNamedItem("filter")) != null ? attrib.getNodeValue() : null;
                    sort = (attrib = col.getAttributes().getNamedItem("sort")) != null ? attrib.getNodeValue() : null;
                    align = (attrib = col.getAttributes().getNamedItem("align")) != null ? attrib.getNodeValue() : null;
                    
                    sw.println("Column column"+id+"_"+i+" = wid"+id+".addColumn();");
                    
                    if(key != null)
                        sw.println("column"+id+"_"+i+".setName(\""+key+"\");");
                    
                    if(header != null){
                        sw.println("column"+id+"_"+i+".setLabel(\""+header+"\");");
                        sw.println("wid"+id+".setHeader(true);");
                    }
                    
                    if(width != null)
                        sw.println("column"+id+"_"+i+".setWidth("+width+");");
                    
                    if(minWidth != null)
                        sw.println("column"+id+"_"+i+".setMinWidth("+minWidth+");");
                    
                    if(sort != null)
                        sw.println("column"+id+"_"+i+".setSortable("+sort+");");
                    
                    if(filter != null)
                 	   sw.println("column"+id+"_"+i+".setFilterable("+filter+");");
                    
                    
                    //if(align != null)
                 	  // sw.println("column"+id+"_"+i+".setAlignment(\")
                    
                    editor = col.getChildNodes();
                    for(int j = 0; j < editor.getLength(); j++){
                        if(editor.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            int child = ++count;
                            if(!createWidget(editor.item(j),child)){
                                count--;
                                continue;
                            }
                            
                            name = editor.item(j).getNodeName();
                            field = (attrib = editor.item(j).getAttributes().getNamedItem("field")) != null ? attrib.getNodeValue() : name.equals("dropdown") ? "Integer" : "String";
                            
                            if(field.equals("Date"))
                                field = "Datetime";
                           
                            if(name.equals("dropdown"))    
                                sw.println("DropdownCell<"+field+"> cell"+child+" = new DropdownCell<"+field+">(wid"+child+");");
                            else if(name.equals("textbox")) 
                                sw.println("TextBoxCell<"+field+"> cell"+child+" = new TextBoxCell<"+field+">(wid"+child+");");
                            else if(name.equals("autoComplete"))
                                sw.println("AutoCompleteCell cell"+child+" = new AutoCompleteCell(wid"+child+");");
                            else if(name.equals("check"))
                                sw.println("CheckBoxCell cell"+child+" = new CheckBoxCell(wid"+child+");");
                            else if(name.equals("calendar"))
                                sw.println("CalendarCell cell"+child+" = new CalendarCell(wid"+child+");");
                            else if(name.equals("image"))
                            	sw.println("ImageCell cell"+child+" = new ImageCell();");
                            else
                                sw.println("LabelCell<"+field+"> cell"+child+"= new LabelCell<"+field+">(wid"+child+");");
                            
                            sw.println("column"+id+"_"+i+".setCellRenderer(cell"+child+");");
                            break;
                        }
                    }
                }
                sw.println("panel.addFocusHandler(wid"+id+");");
				sw.println("wid"+id+".addFocusHandler(panel);");
                setDefaults(node,"wid"+id);
     	        
     	    }
     	   public void addImport() {
     	       composer.addImport("org.openelis.gwt.widget.table.Table");
     	       composer.addImport("org.openelis.gwt.widget.table.Column");
                composer.addImport("org.openelis.gwt.widget.table.LabelCell");
                composer.addImport("org.openelis.gwt.widget.table.TextBoxCell");
                composer.addImport("org.openelis.gwt.widget.table.AutoCompleteCell");
                composer.addImport("org.openelis.gwt.widget.table.DropdownCell");
                composer.addImport("org.openelis.gwt.widget.table.CheckBoxCell");
                composer.addImport("org.openelis.gwt.widget.table.CalendarCell");  
                composer.addImport("org.openelis.gwt.widget.table.ImageCell");
      	   }
     	});    	
    	
    	factoryMap.put("TabPanel", new Factory() {
    		public void getNewInstance(Node node, int id){
    			String width,height,text,tabKey,visible;
    			NodeList tabs,widgets;
    			Node attrib,tab,widget;
    			
    			width = (attrib = node.getAttributes().getNamedItem("width")) != null ? attrib.getNodeValue() : null;
    			height = (attrib = node.getAttributes().getNamedItem("height")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("TabPanel wid"+id+" = new TabPanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenTab\");");
    	        if(width != null)
    	        	sw.println("wid"+id+".setWidth(\""+width+"\");");
    	        if(height != null)
    	        	sw.println("wid"+id+".setHeight(\""+height+"\");");

    	        tabs = ((Element)node).getElementsByTagName("tab");
    	        for (int k = 0; k < tabs.getLength(); k++) {
    	        	tab = tabs.item(k);
    	            widgets = tabs.item(k).getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	            	widget = widgets.item(l);
    	                if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	                	int child = ++count;
    	                	
    	                    if(!loadWidget(widget,child)){
    	    	               count--;
    	    	               continue;    	    	            
    	    	            }
    	                    
    	                    tabKey = (attrib = tab.getAttributes().getNamedItem("tab")) != null ? attrib.getNodeValue() : null;
    	                    text = (attrib = tab.getAttributes().getNamedItem("text")) != null ? attrib.getNodeValue() : "";
    	                    visible = (attrib = tab.getAttributes().getNamedItem("visible")) != null ? attrib.getNodeValue() : "true";
    	                    
    	                    if(tabKey != null)
    	                    	sw.println("wid"+id+".add(wid"+child+", \""+text+"\",\""+tabKey+"\");");
    	                    else
    	                    	sw.println("wid"+id+".add(wid"+child+", \""+text+"\");");

                    		sw.println("wid"+id+".setTabVisible(wid"+id+".getTabBar().getTabCount() -1,"+visible+");");
    	                }
    	            }
    	            sw.println("wid"+id+".selectTab(0);");
    	    		sw.println("wid"+id+".addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {");
    	    			sw.println("public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {");
    	    				sw.println("panel.setFocusWidget(null);");
    	    			sw.println("}");	
    	    		sw.println("});");
    	        }
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.TabPanel");
    			composer.addImport("com.google.gwt.user.client.ui.ScrollPanel");
    			composer.addImport("com.google.gwt.event.logical.shared.BeforeSelectionEvent");
    			composer.addImport("com.google.gwt.event.logical.shared.BeforeSelectionHandler");
    		}
    	});
    	
    	factoryMap.put("TablePanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String spacing,padding,colSpan,rowSpan,align,valign,style,text,height,width;
    			NodeList rows,widgets;
    			Node attrib,widget;
    			String[] styles;
    			
    			spacing = (attrib = node.getAttributes().getNamedItem("spacing")) != null ? attrib.getNodeValue() : null;
    			padding = (attrib = node.getAttributes().getNamedItem("padding")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("FlexTable wid"+id+" = new FlexTable();");

    	        if (spacing != null)
    	            sw.println("wid"+id+".setCellSpacing("+spacing+");");
    	        
    	        if (padding != null)
    	            sw.println("wid"+id+".setCellPadding("+padding+");");
    	        
    	        rows = ((Element)node).getElementsByTagName("row");
    	        for (int k = 0; k < rows.getLength(); k++) {
    	            widgets = rows.item(k).getChildNodes();
    	            int w = -1;
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	            	widget = widgets.item(l);
    	                if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	                    w++;
    	                    if(widget.getNodeName().equals("text")) {
    	                    	styles = (attrib = widget.getAttributes().getNamedItem("style")) != null ? attrib.getNodeValue().split(",") : null;
    	                    	text = (attrib = widget.getFirstChild()) != null ? attrib.getNodeValue().trim() : null;
    	                    	
    	                    	 if (styles != null){
    	                             sw.println("wid"+id+".getCellFormatter().setStyleName("+k+","+w+","+"\""+styles[0]+"\");");
    	                             for(int i = 1; i < styles.length; i++){
    	                            	 sw.println("wid"+id+".getCellFormatter().addStyleName("+k+","+w+","+"\""+styles[i]+"\");");
    	                             }
    	                         }
    	                    	
    	                    	if(text != null && !text.equals(""))
    	                    		sw.println("wid"+id+".setText("+k+","+w+",\""+text+"\");");
    	                    	
    	                    	continue;	
    	                    }
    	                    int child = ++count;
    	                    if(!loadWidget(widgets.item(l),child)){
    	                    	count--;
    	                    	continue;
    	                    }
    	                    colSpan = (attrib = widget.getAttributes().getNamedItem("colspan")) != null ? attrib.getNodeValue() : null;
    	                    rowSpan = (attrib = widget.getAttributes().getNamedItem("rowspan")) != null ? attrib.getNodeValue() : null;
    	                    style   = (attrib = widget.getAttributes().getNamedItem("style"))   != null ? attrib.getNodeValue() : null;
    	                    align   = (attrib = widget.getAttributes().getNamedItem("halign"))  != null ? attrib.getNodeValue() : null;
    	                    valign  = (attrib = widget.getAttributes().getNamedItem("valign"))  != null ? attrib.getNodeValue() : null;
    	                    width   = (attrib = widget.getAttributes().getNamedItem("width"))   != null ? attrib.getNodeValue() : null;
    	                    height  = (attrib = widget.getAttributes().getNamedItem("height"))  != null ? attrib.getNodeValue() : null;
    	                    
    	                    sw.println("wid"+id+".setWidget("+k+","+w+",wid"+child+");");
    	                    
    	                    if(widget.getNodeName().equals("widget")) {
    	                    	if (colSpan != null)
    	                    		sw.println("wid"+id+".getFlexCellFormatter().setColSpan("+k+","+w+","+colSpan+");");
    	                    	
    	                    	if (rowSpan != null)
    	                    		sw.println("wid"+id+".getFlexCellFormatter().setRowSpan("+k+","+w+","+rowSpan+");");
    	                    	
    	                    	if (style != null)
    	                    		sw.println("wid"+id+".getFlexCellFormatter().addStyleName("+k+","+w+","+"\""+style+"\");");
    	                    	
    	                    	if (align != null) 
  	                    			sw.println("wid"+id+".getFlexCellFormatter().setHorizontalAlignment("+k+","+w+",HasAlignment.ALIGN_"+align.toUpperCase()+");");

    	                    	if (valign != null) 
  	                    			sw.println("wid"+id+".getFlexCellFormatter().setVerticalAlignment("+k+","+w+",HasAlignment.ALIGN_"+valign.toUpperCase()+");");
    	                    	
    	                    	if (width != null)
    	                    		sw.println("wid"+id+".getFlexCellFormatter().setWidth("+k+","+w+",\""+width+"\");");
    	                    	
    	                    	if (height != null)
    	                    		sw.println("wid"+id+".getFlexCellFormatter().setHeight("+k+","+w+",\""+height+"\");");
    	                    }
    	                }
    	            }
    	            
    	            style = (attrib = rows.item(k).getAttributes().getNamedItem("style")) != null ? attrib.getNodeValue() : null;
    	            if (style != null) 
    	            	sw.println("wid"+id+".getRowFormatter().addStyleName("+k+",\""+style+"\");");
    	  
    	        }
    	        setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.FlexTable");
    			composer.addImport("com.google.gwt.user.client.ui.HasAlignment");
    		}
    	});    	
    	
    	factoryMap.put("text",new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String wordwrap,text;
    			Node attrib;
    			
    			wordwrap = (attrib = node.getAttributes().getNamedItem("wordwrap")) != null ? attrib.getNodeValue() : "false";
    			text = node.hasChildNodes() ? node.getFirstChild().getNodeValue().trim() : "";
    			
    			sw.println("Label wid"+id+" = new Label();");
  	            sw.println("wid"+id+".setWordWrap("+wordwrap+");");
  	            sw.println("wid"+id+".setText(\""+text+"\");");
    	        sw.println("wid"+id+".setStyleName(\"ScreenLabel\");");

    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.Label");
    		}
    	});    		
    	
    	factoryMap.put("textarea", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			String enabled,required;
    			Node attrib;
    			
    			enabled = (attrib = node.getAttributes().getNamedItem("enabled")) != null ? attrib.getNodeValue() : null;
    			required = (attrib = node.getAttributes().getNamedItem("required")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("TextArea wid"+id+" = new TextArea();");
    			
				if(node.getAttributes().getNamedItem("tab") != null) 
					addTabHandler(node,"wid"+id);

				if(node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
			    
    	        sw.println("wid"+id+".setStyleName(\"ScreenTextArea\");");
    	        
    	        setDefaults(node, "wid"+id);
    	        
    	        if(enabled != null)
    	        	sw.println("wid"+id+".setEnabled("+enabled+");");
    	        
    	        if(required != null)
    	        	sw.println("wid"+id+".setRequired("+required+");");
    	        
    	        factoryMap.get("String").getNewInstance(node, id);
    	        sw.println("wid"+id+".setHelper(field"+id+");");
    	        
    	        sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
    	        sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
    	        
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.TextArea");
    		}
    	});    	

    	factoryMap.put("textbox",new Factory() {
			public void getNewInstance(Node node, int id) {
				String field,fcase,max,textAlign,mask,required,enabled,cField;
				Node attrib;
				
				cField = (field = (attrib = node.getAttributes().getNamedItem("field")) != null ? attrib.getNodeValue() : "String").equals("Date") ? "Datetime" : field;
				fcase = (attrib = node.getAttributes().getNamedItem("case")) != null ? attrib.getNodeValue() : "MIXED";
				max = (attrib = node.getAttributes().getNamedItem("max")) != null ? attrib.getNodeValue() : null;
				textAlign = (attrib = node.getAttributes().getNamedItem("textAlign")) != null ? attrib.getNodeValue() : "LEFT";
				mask = (attrib = node.getAttributes().getNamedItem("mask")) != null ? attrib.getNodeValue() : null;
				required = (attrib = node.getAttributes().getNamedItem("required")) != null ? attrib.getNodeValue() : null;
				enabled = (attrib = node.getAttributes().getNamedItem("enabled")) != null ? attrib.getNodeValue() : null;
				
				sw.println("TextBox<"+cField+"> wid"+id+" = new TextBox<"+cField+">();");
				
				if(node.getAttributes().getNamedItem("tab") != null) 
					addTabHandler(node,"wid"+id);

				if(node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
				
				sw.println("wid"+id+".setStyleName(\"ScreenTextBox\");");
				
				sw.println("wid"+id+".setCase(Case.valueOf(\""+fcase+"\"));");

				if (max != null) 
					sw.println("wid"+id+".setMaxLength("+max+");");

				sw.println("wid"+id+".setTextAlignment(ValueBoxBase.TextAlignment."+textAlign.toUpperCase()+");");
	        
				/*
				if (node.getAttributes().getNamedItem("autoNext") != null){
					if(node.getAttributes().getNamedItem("autoNext").getNodeValue().equals("true")){
						sw.println("wid"+id+".autoNext = true;");
					}
				}
				*/
				
				if (mask != null) 
					sw.println("wid"+id+".setMask(\""+mask+"\");");
				
				if(required != null)
					sw.println("wid"+id+".setRequired("+required+");");
				
				setDefaults(node,"wid"+id);

				if(enabled != null)
					sw.println("wid"+id+".setEnabled("+enabled+");");

				factoryMap.get(field).getNewInstance(node, id);
				sw.println("wid"+id+".setHelper(field"+id+");");
				
				sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
			}
			public void addImport() {
				composer.addImport("org.openelis.gwt.widget.TextBox");
				composer.addImport("org.openelis.gwt.widget.TextBox.Case");
				composer.addImport("com.google.gwt.user.client.ui.ValueBoxBase");
                composer.addImport("org.openelis.gwt.widget.IntegerHelper");
                composer.addImport("org.openelis.gwt.widget.DoubleHelper");
                composer.addImport("org.openelis.gwt.widget.LongHelper");
                composer.addImport("org.openelis.gwt.widget.DateHelper");
                composer.addImport("org.openelis.gwt.widget.StringHelper");
			}
    	});
    	
    	factoryMap.put("TitledPanel", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			Element legend;
    			NodeList widgets;
    			Node widget;
    			
    			sw.println("TitledPanel wid"+id+" = new TitledPanel();");
    	        legend = (Element) ((Element)node).getElementsByTagName("legend").item(0);
    	        widgets = legend.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	        	widget = widgets.item(k);
    	            if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	            	if(!loadWidget(widget,child)){
     	            	   count--;
     	            	   continue;
     	               }
    	                sw.println("wid"+id+".setTitle(wid"+child+");");
    	            }
    	        }
    	        
    	        legend = (Element) ((Element)node).getElementsByTagName("content").item(0);
    	        widgets = legend.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	        	widget = widgets.item(k);
    	            if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	            	if(!loadWidget(widget,child)){
     	            	   count--;
     	            	   continue;
     	               }
    	                sw.println("wid"+id+".setWidget(wid"+child+");");
    	            }
    	        }
    	        setDefaults(node, "wid"+id);
    		}
    		
    		public void addImport() {
    			composer.addImport("org.openelis.widget.TitledPanel");
    		}	
    	});
    	
    	factoryMap.put("tree", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String width,rows,vscroll,hscroll,multiSelect,key,header,minWidth,rowHeight,enabled;
    			Node attrib,columns,col;
    			NodeList colList,editor,leafList;
    			
    			key = (attrib = node.getAttributes().getNamedItem("key")) != null ? attrib.getNodeValue() : "";
    			width = (attrib = node.getAttributes().getNamedItem("width")) != null ? attrib.getNodeValue() : "-1";
    			rows = (attrib = node.getAttributes().getNamedItem("rows")) != null ? attrib.getNodeValue() : "10";
    			vscroll = (attrib = node.getAttributes().getNamedItem("vscroll")) != null ? attrib.getNodeValue() : "AS_NEEDED";
    			hscroll = (attrib = node.getAttributes().getNamedItem("hscroll")) != null ? attrib.getNodeValue() : "AS_NEEDED";
    			multiSelect = (attrib = node.getAttributes().getNamedItem("multiSelect")) != null ? attrib.getNodeValue() : null;
    			rowHeight = (attrib = node.getAttributes().getNamedItem("rowHeight")) != null ? attrib.getNodeValue() : null;
    			enabled = (attrib = node.getAttributes().getNamedItem("enabled")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("Tree wid"+id+" = new Tree();");
    			
				if(node.getAttributes().getNamedItem("tab") != null) 
					addTabHandler(node,"wid"+id);
				
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
                
                sw.println("wid"+id+".setWidth("+width+");");
                sw.println("wid"+id+".setVisibleRows("+rows+");");
                sw.println("wid"+id+".setVerticalScroll(Tree.Scrolling.valueOf(\""+vscroll+"\"));");
                sw.println("wid"+id+".setHorizontalScroll(Tree.Scrolling.valueOf(\""+hscroll+"\"));");
                
                if(rowHeight != null) 
                	sw.println("wid"+id+".setRowHeight("+rowHeight+");");
                
                if(multiSelect != null)
                	sw.println("wid"+id+".setAllowMultipleSelection("+multiSelect+");");
                
                if(enabled != null)
                	sw.println("wid"+id+".setEnabled("+enabled+");");
                
                columns  = ((Element)node).getElementsByTagName("columns").item(0);
                if(columns != null){
                	colList = columns.getChildNodes();
                    for(int i = 0; i < colList.getLength(); i++) {
                        col = colList.item(i);
                        if(col.getNodeType() != Node.ELEMENT_NODE || !col.getNodeName().equals("col"))
                            continue;
                        key = (attrib = col.getAttributes().getNamedItem("key")) != null ? attrib.getNodeValue() : null;
                        header = (attrib = col.getAttributes().getNamedItem("header")) != null ? attrib.getNodeValue() : null;
                        width = (attrib = col.getAttributes().getNamedItem("width")) != null ? attrib.getNodeValue() : null;
                        minWidth = (attrib = col.getAttributes().getNamedItem("minWidth")) != null ? attrib.getNodeValue() : null;
                        
                        sw.println("org.openelis.gwt.widget.tree.Column column"+id+"_"+i+" = wid"+id+".addColumn();");
                        
                        if(key != null)
                            sw.println("column"+id+"_"+i+".setName(\""+key+"\");");
                        
                        if(header != null){
                            sw.println("column"+id+"_"+i+".setLabel(\""+header+"\");");
                            sw.println("wid"+id+".setHeader(true);");
                        }
                        
                        if(width != null)
                            sw.println("column"+id+"_"+i+".setWidth("+width+");");
                        
                        if(minWidth != null)
                            sw.println("column"+id+"_"+i+".setMinWidth("+minWidth+");");
                        
                        editor = col.getChildNodes();
                        for(int j = 0; j < editor.getLength(); j++){
                            if(editor.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                int child = ++count;
                                if(!createWidget(editor.item(j),child)){
                                    count--;
                                    continue;
                                }
                                
                                String name = editor.item(j).getNodeName();
                                String field = (attrib = editor.item(j).getAttributes().getNamedItem("field")) != null ? attrib.getNodeValue() : name.equals("Dropdown") ? "Integer" : "String";
                                
                                if(field.equals("Date"))
                                	field = "Datetime";
                                                               
                                if(editor.item(j).getNodeName().equals("dropdown")) 
                                    sw.println("DropdownCell<"+field+"> cell"+child+" = new DropdownCell<"+field+">(wid"+child+");");
                                else if(editor.item(j).getNodeName().equals("textbox")) 
                                    sw.println("TextBoxCell<"+field+"> cell"+child+" = new TextBoxCell<"+field+">(wid"+child+");");
                                else if(editor.item(j).getNodeName().equals("autoComplete"))
                                    sw.println("AutoCompleteCell cell"+child+" = new AutoCompleteCell(wid"+child+");");
                                else if(editor.item(j).getNodeName().equals("check"))
                                    sw.println("CheckBoxCell cell"+child+" = new CheckBoxCell(wid"+child+");");
                                else if(editor.item(j).getNodeName().equals("calendar"))
                                    sw.println("CalendarCell cell"+child+" = new CalendarCell(wid"+child+");");
                                else if(editor.item(j).getNodeName().equals("image"))
                                	sw.println("ImageCell cell"+child+" = new ImageCell();");
                                else
                                	sw.println("LabelCell<"+field+"> cell"+child+" = new LabelCell<"+field+">(wid"+child+");");
                                
                                sw.println("column"+id+"_"+i+".setCellRenderer(cell"+child+");");
                                break;
                            }
                        }
                    }
                }
                
                leafList = ((Element)node).getElementsByTagName("node");
                for(int i = 0; i < leafList.getLength(); i++) {
                	sw.println("ArrayList<org.openelis.gwt.widget.tree.Column> leafDef"+id+"_"+i+" = new ArrayList<org.openelis.gwt.widget.tree.Column>();");
                	colList = leafList.item(i).getChildNodes();
                	for(int h = 0; h < colList.getLength(); h++) {
                		col = colList.item(h);
                		if(col.getNodeType() != Node.ELEMENT_NODE || !col.getNodeName().equals("col"))
                			continue;
                		sw.println("org.openelis.gwt.widget.tree.Column leafCol"+id+"_"+i+"_"+h+" = new org.openelis.gwt.widget.tree.Column();");
                		sw.println("leafDef"+id+"_"+i+".add(leafCol"+id+"_"+i+"_"+h+");");
                		
                		key = (attrib = col.getAttributes().getNamedItem("key")) != null ? attrib.getNodeValue() : null;
                		
                		if(key != null)
                			sw.println("leafCol"+id+"_"+i+"_"+h+".setName(\""+key+"\");");
                		
                		editor = col.getChildNodes();
                		for(int j = 0; j < editor.getLength(); j++){
                			if(editor.item(j).getNodeType() == Node.ELEMENT_NODE) {
                				int child = ++count;
                				if(!createWidget(editor.item(j),child)){
                					count--;
                					continue;
                				}
                				String name = editor.item(j).getNodeName();
                				String field = (attrib = editor.item(j).getAttributes().getNamedItem("field")) != null ? attrib.getNodeValue() : name.equals("Dropdown") ? "Integer" : "String";
                				
                				if(field.equals("Date"))
                					field = "Datetime";
                				
                				if(name.equals("dropdown")) 
                					sw.println("DropdownCell<"+field+"> leafCell"+child+" = new DropdownCell<"+field+">(wid"+child+");");
                				else if(name.equals("textbox")) 
                					sw.println("TextBoxCell<"+field+"> leafCell"+child+" = new TextBoxCell<"+field+">(wid"+child+");");
                				else if(name.equals("autoComplete"))
                					sw.println("AutoCompleteCell leafCell"+child+" = new AutoCompleteCell(wid"+child+");");
                				else if(name.equals("check"))
                					sw.println("CheckBoxCell leafCell"+child+" = new CheckBoxCell(wid"+child+");");
                				else if(name.equals("calendar"))
                					sw.println("CalendarCell leafCell"+child+" = new CalendarCell(wid"+child+");");
                				else
                                	sw.println("LabelCell<"+field+"> leafCell"+child+" = new LabelCell<"+field+">(wid"+child+");");

                				sw.println("leafCol"+id+"_"+i+"_"+h+".setCellRenderer(leafCell"+child+");");
                				//sw.println("column"+id+"_"+i+".setCellEditor(cell"+child+");");
                				break;
                			}
                		}
                	}
                	key = leafList.item(i).getAttributes().getNamedItem("key").getNodeValue();
                	sw.println("wid"+id+".addNodeDefinition(\""+key+"\",leafDef"+id+"_"+i+");");
                }
                setDefaults(node,"wid"+id);
                sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
                sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
                sw.println("panel.addFocusHandler(wid"+id+");");
                sw.println("wid"+id+".addFocusHandler(panel);");
    		}
    	
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.tree.Tree");
    			composer.addImport("com.google.gwt.user.client.ui.HasAlignment");
    			composer.addImport("org.openelis.gwt.widget.tree.View");
    			composer.addImport("org.openelis.gwt.widget.table.LabelCell");
                composer.addImport("org.openelis.gwt.widget.table.TextBoxCell");
                composer.addImport("org.openelis.gwt.widget.table.AutoCompleteCell");
                composer.addImport("org.openelis.gwt.widget.table.DropdownCell");
                composer.addImport("org.openelis.gwt.widget.table.CheckBoxCell");
                composer.addImport("org.openelis.gwt.widget.table.CalendarCell");  
                composer.addImport("org.openelis.gwt.widget.table.ImageCell");
    		}
    	});    	
    	
    	factoryMap.put("VerticalPanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			String spacing,halign,valign,height,width;
    			NodeList widgets;
    			Node attrib,widget;
    			
    			spacing = (attrib = node.getAttributes().getNamedItem("spacing")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("VerticalPanel wid"+id+" = new VerticalPanel();");
    	        
    			if (spacing != null)
    	            sw.println("wid"+id+".setSpacing("+spacing+");");
  
    	        widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	        	widget = widgets.item(k);
    	            if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	                if(!loadWidget(widgets.item(k),child)){
    	                	count--;
    	                	continue;
    	                }
    	                halign = (attrib = widget.getAttributes().getNamedItem("halign")) != null ? attrib.getNodeValue() : "LEFT";
    	                valign = (attrib = widget.getAttributes().getNamedItem("valign")) != null ? attrib.getNodeValue() : "TOP";
    	                height = (attrib = widget.getAttributes().getNamedItem("height")) != null ? attrib.getNodeValue() : null;
    	                width = (attrib = widget.getAttributes().getNamedItem("width")) != null ? attrib.getNodeValue() : null;
    	                
     	                sw.println("wid"+id+".add(wid"+child+");");
    	                
                        sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+", HasAlignment.ALIGN_"+halign.toUpperCase()+");");
                        sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+", HasAlignment.ALIGN_"+valign.toUpperCase()+");");

    	                if(height != null) 
    	                	sw.println("wid"+id+".setCellHeight(wid"+child+",\""+height+"\");");
    	                
    	                if(width != null && !width.equals("auto"))
   	                		sw.println("wid"+id+".setCellWidth(wid"+child+",\""+width+"\");");
    	            }
    	        }
    	        sw.println("wid"+id+".setStyleName(\"ScreenPanel\");");
    	        setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.VerticalPanel");
    			composer.addImport("com.google.gwt.user.client.ui.HasAlignment");
    		}
    	});

    	factoryMap.put("VerticalSplitPanel", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			String splitpos;
    			NodeList sections,widgets;
    			Node attrib, section, widget;
    			
    			sw.println("VerticalSplitPanel wid"+id+" = new VerticalSplitPanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenSplit\");");
    	        sections = ((Element)node).getElementsByTagName("section");
    	        for (int k = 0; k < sections.getLength(); k++) {
    	        	section = sections.item(k);
    	            widgets = section.getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	            	widget = widgets.item(l);
    	                if (widget.getNodeType() == Node.ELEMENT_NODE) {
    	                	int child = ++count;
    	                	if(!loadWidget(widget,child)){
    	                		count--;
    	    	                continue;
    	    	            }
    	                    if (k == 0) 
    	                             sw.println("wid"+id+".setTopWidget(wid"+child+");");
    	                    
    	                    if (k == 1) 
    	                             sw.println("wid"+id+".setBottomWidget(wid"+child+");");
    	                    
    	                }
    	            }
    	        }
    	        splitpos = (attrib = node.getAttributes().getNamedItem("splitpos")) != null ? attrib.getNodeValue() : null;
    	        if(splitpos != null) 
   	                 sw.println("wid"+id+".setSplitPosition("+splitpos+");");
    	        
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.VerticalSplitPanel");
    		}
    	});

    	factoryMap.put("webButton", new Factory() {    		
    		public void getNewInstance(Node node, int id) {
    			String icon,label;
    			Node attrib;
    			
    			icon = (attrib = node.getAttributes().getNamedItem("icon")) != null ? attrib.getNodeValue() : "";
    			label = (attrib = node.getAttributes().getNamedItem("label")) != null ? attrib.getNodeValue() : "";
    			
    			sw.println("org.openelis.gwt.widget.web.MenuButton wid"+id+" = new org.openelis.gwt.widget.web.MenuButton(\""+icon+"\",\""+label+"\");");
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.web.MenuButton");
    		}
    	});
    }

    public static void addFactory(String tag, Factory factory) {
        factoryMap.put(tag,factory);
    }
}
