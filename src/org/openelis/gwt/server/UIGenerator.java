package org.openelis.gwt.server;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.openelis.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.sun.org.apache.xpath.internal.operations.Number;

public class UIGenerator extends Generator {
	
	private String className;
	private String packageName;
	private String typeName;
	private Document doc;
	private SourceWriter sw;
	private int count;
	private ClassSourceFileComposerFactory composer;
	private String lang;
	
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		try {
			lang = context.getPropertyOracle().getSelectionProperty(logger,"locale").getCurrentValue();
		}catch(Exception e){
			e.printStackTrace();
		}
		this.typeName = typeName;
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
        composer.addImport("org.openelis.ui.common.Datetime");
        composer.addImport("org.openelis.gwt.widget.StringField");
        composer.addImport("org.openelis.gwt.widget.DateField");
        composer.addImport("org.openelis.gwt.widget.IntegerField");
        composer.addImport("org.openelis.gwt.widget.LongField");
        composer.addImport("org.openelis.gwt.widget.DoubleField");
        composer.addImport("org.openelis.gwt.widget.CheckField");
        composer.addImport("org.openelis.gwt.common.Util");
        composer.addImport("org.openelis.cache.UserCache");

        findImports(doc.getElementsByTagName("screen").item(0));

        composer.addImport("org.openelis.gwt.screen.Screen");
        composer.addImport("org.openelis.gwt.screen.TabHandler");
        composer.addImport("org.openelis.gwt.screen.ShortcutHandler");
        composer.addImport("com.google.gwt.event.dom.client.HasFocusHandlers");
        composer.addImplementedInterface("ScreenDefInt");
        
        sw = composer.createSourceWriter(context,printWriter);
	  
		sw.println("protected ScreenPanel panel;");
		sw.println("protected HashMap<String,Widget> widgets;");
		sw.println("public String name;");
		
		sw.println("");
		sw.println("public "+className+"_"+lang+"() {");
		sw.println("widgets = new HashMap<String,Widget>();");
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
    	Node screen = doc.getElementsByTagName("screen").item(0);
    	if(screen.getAttributes().getNamedItem("name") != null) {
    		sw.println("name = \""+screen.getAttributes().getNamedItem("name").getNodeValue()+"\";");
    	}
        //Node display = doc.getElementsByTagName("display").item(0);
        NodeList widgets = screen.getChildNodes();
        for (int i = 0; i < widgets.getLength(); i++) {
            if (widgets.item(i).getNodeType() == Node.ELEMENT_NODE) {
                createWidget(widgets.item(i),0);
                if(widgets.item(i).getAttributes().getNamedItem("key") != null)
                	sw.println("setWidget(wid0, \""+widgets.item(i).getAttributes().getNamedItem("key").getNodeValue()+"\");");
                sw.println("panel.add(wid0);");
                break;
            }
        }
    }
    
    public void findImports(Node node) {
        NodeList widgets = node.getChildNodes();
        for (int i = 0; i < widgets.getLength(); i++) {
            if (widgets.item(i).getNodeType() == Node.ELEMENT_NODE) {
            	String widName = widgets.item(i).getNodeName();
            	if(factoryMap.containsKey(widName)){
            		factoryMap.get(widName).addImport();
            	}
            	if(widgets.item(i).hasChildNodes())
            		findImports(widgets.item(i));
            }
        }
        
    }
    
    
    public boolean createWidget(Node node, int id) {
    	if(node.getNodeName().equals("code")){
    		sw.println(node.getTextContent());
    		return false;
    	}
        String widName = node.getNodeName();
        factoryMap.get(widName).getNewInstance(node,id);
        if(node.getAttributes().getNamedItem("key") != null) 
        	sw.println("widgets.put(\""+node.getAttributes().getNamedItem("key").getNodeValue()+"\", wid"+id+");");
        return true;
    }
    
    private boolean loadWidget(Node node, int id){
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
        return createWidget(input,id);
        
    }
    
    private void setDefaults(Node node, String wid) {
    	StringBuffer sb = new StringBuffer();
        if (node.getAttributes().getNamedItem("style") != null){
            String[] styles = node.getAttributes().getNamedItem("style").getNodeValue().split(",");
            sw.println(wid+".setStyleName(\""+styles[0]+"\");");
            for(int i = 1; i < styles.length; i++){
                sw.println(wid+".addStyleName(\""+styles[i]+"\");");
            }
        }
        if (node.getAttributes().getNamedItem("width") != null)
            sw.println(wid+".setWidth(\""+withUnits(node.getAttributes().getNamedItem("width"))+"\");");
        if (node.getAttributes().getNamedItem("height") != null)
            sw.println(wid+".setHeight(\""+withUnits(node.getAttributes().getNamedItem("height"))+"\");");
        if (node.getAttributes().getNamedItem("tip") != null){
            sw.println(wid+".setTitle(\""+node.getAttributes().getNamedItem("tip").getNodeValue()+"\");");
        }
        if (node.getAttributes().getNamedItem("visible") != null){
            if(node.getAttributes().getNamedItem("visible").getNodeValue().equals("false"))
                sw.println(wid+".setVisible(false);");
        }
    }
    
    private interface Factory {
    	public void getNewInstance(Node node,int id);
    	public void addImport();
    }
    
    public void addShortcutHandler(Node node, String wid) {
    	String ctrl = "false";
    	String shift = "false";
    	String alt = "false";
    	char key;
    	String shortcut = node.getAttributes().getNamedItem("shortcut").getNodeValue();
    	List<String> keys = Arrays.asList(shortcut.split("\\+"));
    	if(keys.contains("ctrl"))
    		ctrl = "true";
    	if(keys.contains("shift"))
    		shift = "true";
    	if(keys.contains("alt"))
    		alt = "true";
    	key = shortcut.charAt(shortcut.length()-1);
    	sw.println("panel.addShortcutHandler(new ShortcutHandler("+ctrl+","+shift+","+alt+",'"+key+"',"+wid+"));");
    }
    
    private static HashMap<String,Factory> factoryMap = new HashMap<String,Factory>();
    
    {
    	factoryMap.put("textbox",new Factory() {
			public void getNewInstance(Node node, int id) {
				if(node.getAttributes().getNamedItem("field") != null){
					if(node.getAttributes().getNamedItem("field").getNodeValue().equals("Integer"))
						sw.println("TextBox<Integer> wid"+id+" = new TextBox<Integer>();");
					else if(node.getAttributes().getNamedItem("field").getNodeValue().equals("Double"))
						sw.println("TextBox<Double> wid"+id+" = new TextBox<Double>();");
					else if(node.getAttributes().getNamedItem("field").getNodeValue().equals("Long"))
						sw.println("TextBox<Long> wid"+id+" = new TextBox<Long>();");
					else if(node.getAttributes().getNamedItem("field").getNodeValue().equals("Date"))
						sw.println("TextBox<Datetime> wid"+id+" = new TextBox<Datetime>();");
					else
						sw.println("TextBox<String> wid"+id+" = new TextBox<String>();");
				}else
					sw.println("TextBox<String> wid"+id+" = new TextBox<String>();");
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
				sw.println("wid"+id+".setStyleName(\"ScreenTextBox\");");
				if (node.getAttributes().getNamedItem("case") != null){
					String fieldCase = node.getAttributes().getNamedItem("case")
					.getNodeValue().toUpperCase();
					sw.println("wid"+id+".setCase(TextBox.Case.valueOf(\""+fieldCase+"\"));");
	        	}

				if (node.getAttributes().getNamedItem("max") != null) {
					int length = Integer.parseInt(node.getAttributes().getNamedItem("max").getNodeValue());
					sw.println("wid"+id+".setLength("+length+");");
				}
	        
				if (node.getAttributes().getNamedItem("textAlign") != null) {
					String align = node.getAttributes().getNamedItem("textAlign").getNodeValue();
					if(align.equalsIgnoreCase("center"))
						sw.println("wid"+id+".alignment = TextBox.ALIGN_CENTER;");
					if(align.equalsIgnoreCase("right"))
						sw.println("wid"+id+".alignment = TextBox.ALIGN_RIGHT;");
					if(align.equalsIgnoreCase("left"))   
						sw.println("wid"+id+".alignment = TextBox.ALIGN_LEFT;");
					sw.println("wid"+id+".setTextAlignment(wid"+id+".alignment);");
				}
	        
				if (node.getAttributes().getNamedItem("autoNext") != null){
					if(node.getAttributes().getNamedItem("autoNext").getNodeValue().equals("true")){
						sw.println("wid"+id+".autoNext = true;");
					}
				}
				
				if (node.getAttributes().getNamedItem("mask") != null) {
					String mask = node.getAttributes().getNamedItem("mask").getNodeValue();
					sw.println("wid"+id+".setMask(\""+mask+"\");");
				}
				setDefaults(node,"wid"+id);
		        if (node.getAttributes().getNamedItem("enable") != null){
		        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
		        }
				if (node.getAttributes().getNamedItem("field") != null) {
					factoryMap.get(node.getAttributes().getNamedItem("field").getNodeValue()).getNewInstance(node, id);
					sw.println("wid"+id+".setField(field"+id+");");
				}else{
					factoryMap.get("String").getNewInstance(node, id);
					sw.println("wid"+id+".setField(field"+id+");");
				}
				sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
			}
			public void addImport() {
				composer.addImport("org.openelis.gwt.widget.TextBox");
			}
    	});
    	factoryMap.put("LayoutPanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("LayoutPanel wid"+id+" = new LayoutPanel();");
    			NodeList widgets = node.getChildNodes();
     	        for (int k = 0; k < widgets.getLength(); k++) {
     	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
     	            	int child = ++count;
     	                if(!loadWidget(widgets.item(k),child)){
     	                	count--;
     	                	continue;
     	                }
     	                sw.println("wid"+id+".add(wid"+child+");");
     	                String left = widgets.item(k).getAttributes().getNamedItem("left").getNodeValue();
     	                String width = widgets.item(k).getAttributes().getNamedItem("width").getNodeValue();
     	                
     	                sw.println("wid"+id+".setWidgetLeftWidth(wid"+child+","+Double.parseDouble(left)+", Unit.PCT,"+Double.parseDouble(width)+", Unit.PCT);");
     	                /*
     	                if (widgets.item(k).getAttributes().getNamedItem("halign") != null) {
     	                    String align = widgets.item(k).getAttributes()
     	                                         .getNamedItem("halign")
     	                                         .getNodeValue();
     	                    if (align.equals("right"))
     	                        sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+", HasAlignment.ALIGN_RIGHT);");
     	                    if (align.equals("left"))
     	                        sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+", HasAlignment.ALIGN_LEFT);");
     	                    if (align.equals("center"))
     	                        sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+", HasAlignment.ALIGN_CENTER);");
     	                }
     	                if (widgets.item(k).getAttributes().getNamedItem("valign") != null) {
     	                    String align = widgets.item(k).getAttributes()
     	                                         .getNamedItem("valign")
     	                                         .getNodeValue();
     	                    if (align.equals("top"))
     	                        sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+", HasAlignment.ALIGN_TOP);");
     	                    if (align.equals("middle"))
     	                        sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+", HasAlignment.ALIGN_MIDDLE);");
     	                    if (align.equals("bottom"))
     	                        sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+", HasAlignment.ALIGN_BOTTOM);");
     	                }
     	                if(widgets.item(k).getAttributes().getNamedItem("height") != null) {
     	                	sw.println("wid"+id+".setCellHeight(wid"+child+",\""+widgets.item(k).getAttributes().getNamedItem("height").getNodeValue()+"\");");
     	                }
     	                if(widgets.item(k).getAttributes().getNamedItem("width") != null) {
     	                	if(!widgets.item(k).getAttributes().getNamedItem("width").getNodeValue().equals("auto"))
     	                		sw.println("wid"+id+".setCellWidth(wid"+child+",\""+widgets.item(k).getAttributes().getNamedItem("width").getNodeValue()+"\");");
     	                }
     	                */
     	            }
     	        }
     	        sw.println("wid"+id+".setStyleName(\"ScreenPanel\");");
     	        setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.LayoutPanel");
    			composer.addImport("com.google.gwt.dom.client.Style.Unit");
    		}
    	});
    	factoryMap.put("VerticalPanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("VerticalPanel wid"+id+" = new VerticalPanel();");
    	        if (node.getAttributes().getNamedItem("spacing") != null)
    	            sw.println("wid"+id+".setSpacing("+Integer.parseInt(node.getAttributes()
    	                                                  .getNamedItem("spacing")
    	                                                  .getNodeValue())+");");
  
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	                if(!loadWidget(widgets.item(k),child)){
    	                	count--;
    	                	continue;
    	                }
    	                sw.println("wid"+id+".add(wid"+child+");");
    	                if (widgets.item(k).getAttributes().getNamedItem("halign") != null) {
    	                    String align = widgets.item(k).getAttributes()
    	                                         .getNamedItem("halign")
    	                                         .getNodeValue();
    	                    if (align.equals("right"))
    	                        sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+", HasAlignment.ALIGN_RIGHT);");
    	                    if (align.equals("left"))
    	                        sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+", HasAlignment.ALIGN_LEFT);");
    	                    if (align.equals("center"))
    	                        sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+", HasAlignment.ALIGN_CENTER);");
    	                }
    	                if (widgets.item(k).getAttributes().getNamedItem("valign") != null) {
    	                    String align = widgets.item(k).getAttributes()
    	                                         .getNamedItem("valign")
    	                                         .getNodeValue();
    	                    if (align.equals("top"))
    	                        sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+", HasAlignment.ALIGN_TOP);");
    	                    if (align.equals("middle"))
    	                        sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+", HasAlignment.ALIGN_MIDDLE);");
    	                    if (align.equals("bottom"))
    	                        sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+", HasAlignment.ALIGN_BOTTOM);");
    	                }
    	                if(widgets.item(k).getAttributes().getNamedItem("height") != null) {
    	                	sw.println("wid"+id+".setCellHeight(wid"+child+",\""+widgets.item(k).getAttributes().getNamedItem("height").getNodeValue()+"\");");
    	                }
    	                if(widgets.item(k).getAttributes().getNamedItem("width") != null) {
    	                	if(!widgets.item(k).getAttributes().getNamedItem("width").getNodeValue().equals("auto"))
    	                		sw.println("wid"+id+".setCellWidth(wid"+child+",\""+withUnits(widgets.item(k).getAttributes().getNamedItem("width"))+"\");");
    	                }
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
    	factoryMap.put("TablePanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			StringBuffer sb = new StringBuffer();
    			sw.println("FlexTable wid"+id+" = new FlexTable();");
    	        if (node.getAttributes().getNamedItem("visible") != null && node.getAttributes().getNamedItem("visible").getNodeValue().equals("false")){
    	            sw.println("wid"+id+".setVisible(false);");
    	        }
    	        if (node.getAttributes().getNamedItem("spacing") != null)
    	            sw.println("wid"+id+".setCellSpacing("+Integer.parseInt(node.getAttributes()
    	                                                      .getNamedItem("spacing")
    	                                                      .getNodeValue())+");");
    	        if (node.getAttributes().getNamedItem("padding") != null)
    	            sw.println("wid"+id+".setCellPadding("+Integer.parseInt(node.getAttributes()
    	                                                      .getNamedItem("padding")
    	                                                      .getNodeValue())+");");
    	        if (node.getAttributes().getNamedItem("style") != null){
    	            sw.println("wid"+id+".addStyleName(\""+node.getAttributes().getNamedItem("style").getNodeValue()+"\");");
    	        }
    	        
    	        NodeList rows = ((Element)node).getChildNodes();
    	        for (int k = 0; k < rows.getLength(); k++) {
    	        	if(!rows.item(k).getNodeName().equals("row"))
    	        		continue;
    	            NodeList widgets = rows.item(k).getChildNodes();
    	            int w = -1;
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	                    w++;
    	                    int child = ++count;
    	                    if(!loadWidget(widgets.item(l),child)){
    	                    	count--;
    	                    	continue;
    	                    }
    	                    sw.println("wid"+id+".setWidget("+k+","+w+",wid"+child+");");
    	                    if(widgets.item(l).getNodeName().equals("widget")) {
    	                    	if (widgets.item(l).getAttributes().getNamedItem("width") != null)
    	                    		sw.println("wid"+id+".getFlexCellFormatter().setWidth("+k+","+w+",\""+
    	                    						withUnits(widgets.item(l).getAttributes().getNamedItem("width"))+"\");");
    	                    	if (widgets.item(l).getAttributes().getNamedItem("height") != null)
    	                    		sw.println("wid"+id+".getFlexCellFormatter().setHeight("+k+","+w+",\""+
    	                    						withUnits(widgets.item(l).getAttributes().getNamedItem("height"))+"\");");
    	                    	if (widgets.item(l).getAttributes().getNamedItem("colspan") != null)
    	                    		sw.println("wid"+id+".getFlexCellFormatter().setColSpan("+k+","+w+","+
    	                    				Integer.parseInt(widgets.item(l)
    	                    						.getAttributes()
    	                    						.getNamedItem("colspan")
    	                    						.getNodeValue())+");");
    	                    	if (widgets.item(l).getAttributes().getNamedItem("rowspan") != null)
    	                    		sw.println("wid"+id+".getFlexCellFormatter().setRowSpan("+k+","+w+","+
    	                    				Integer.parseInt(widgets.item(l)
    	                    						.getAttributes()
    	                    						.getNamedItem("rowspan")
    	                    						.getNodeValue())+");");
    	                    	if (widgets.item(l).getAttributes().getNamedItem("style") != null)
    	                    		sw.println("wid"+id+".getFlexCellFormatter().addStyleName("+k+","+w+","+"\""+
    	                    				widgets.item(l)
    	                    				.getAttributes()
    	                    				.getNamedItem("style")
    	                    				.getNodeValue()+"\");");
    	                    	if (widgets.item(l).getAttributes().getNamedItem("align") != null) {
    	                    		String align = widgets.item(l)
    	                    			.getAttributes()
    	                    			.getNamedItem("align")
    	                    			.getNodeValue();
    	                    		if (align.equals("right"))
    	                    			sw.println("wid"+id+".getFlexCellFormatter().setHorizontalAlignment("+k+","+w+",HasAlignment.ALIGN_RIGHT);");
    	                    		if (align.equals("left"))
    	                    			sw.println("wid"+id+".getFlexCellFormatter().setHorizontalAlignment("+k+","+w+",HasAlignment.ALIGN_LEFT);");
    	                    		if (align.equals("center"))
    	                    			sw.println("wid"+id+".getFlexCellFormatter().setHorizontalAlignment("+k+","+w+",HasAlignment.ALIGN_CENTER);");
    	                    	}
    	                    	if (widgets.item(l).getAttributes().getNamedItem("valign") != null) {
    	                    		String align = widgets.item(l)
    	                    			.getAttributes()
    	                    			.getNamedItem("valign")
    	                    			.getNodeValue();
    	                    		if (align.equals("top"))
    	                    			sw.println("wid"+id+".getFlexCellFormatter().setVerticalAlignment("+k+","+w+",HasAlignment.ALIGN_TOP);");
    	                    		if (align.equals("bottom"))
    	                    			sw.println("wid"+id+".getFlexCellFormatter().setVerticalAlignment("+k+","+w+",HasAlignment.ALIGN_BOTTOM);");
    	                    		if (align.equals("middle"))
    	                    			sw.println("wid"+id+".getFlexCellFormatter().setVerticalAlignment("+k+","+w+",HasAlignment.ALIGN_MIDDLE);");
    	                    	}
    	                    }
    	                }
    	            }
    	            
    	            if (rows.item(k).getAttributes().getNamedItem("style") != null) {
    	            	sw.println("wid"+id+".getRowFormatter().addStyleName("+k+",\""+rows.item(k).getAttributes().getNamedItem("style").getNodeValue()+"\");");
    	            }
    	        }
    	        setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.FlexTable");
    			composer.addImport("com.google.gwt.user.client.ui.HasAlignment");
    		}
    	});
    	factoryMap.put("fileUpload", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("FileLoad wid"+id+" = new FileLoad();");
    			if(node.getAttributes().getNamedItem("service") != null)
    				sw.println("wid"+id+".setService(\""+node.getAttributes().getNamedItem("service").getNodeValue()+"\");");
    			if(node.getAttributes().getNamedItem("method") != null)
    				sw.println("wid"+id+".setMethod(\""+node.getAttributes().getNamedItem("method").getNodeValue()+"\");");
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	                if(!loadWidget(widgets.item(k),child)){
    	                	count--;
    	                	continue;
    	                }
    	                sw.println("wid"+id+".setWidget(wid"+child+");");
    	            }
    	        }
    			setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.FileLoad");
    		}
    	});
    	factoryMap.put("AbsolutePanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("AbsolutePanel wid"+id+" = new AbsolutePanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenAbsolute\");");
    	        if(node.getAttributes().getNamedItem("overflow") != null)
    	            sw.println("DOM.setStyleAttribute(wid"+id+".getElement(),\"overflow\","+node.getAttributes().getNamedItem("overflow").getNodeValue()+");");
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	                if(!loadWidget(widgets.item(k),child)){
    	                	count--;
    	                	continue;
    	                }
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
    	                    sw.println("DOM.setElementProperty(wid"+id+".getElement(),\"align\",\""+node.getAttributes().getNamedItem("align").getNodeValue()+"\");");
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
    	factoryMap.put("ScrollPanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("ScrollPanel wid"+id+" = new ScrollPanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenAbsolute\");");
    	        if(node.getAttributes().getNamedItem("overflow") != null)
    	            sw.println("DOM.setStyleAttribute(wid"+id+".getElement(),\"overflow\","+node.getAttributes().getNamedItem("overflow").getNodeValue()+");");
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	                if(!loadWidget(widgets.item(k),count)){
    	                	count--;
    	                	continue;
    	                }
    	                sw.println("wid"+id+".setWidget(wid"+child+");");
    	                break;
    	            }
    	        }
    	        setDefaults(node,"wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.ScrollPanel");
    			composer.addImport("com.google.gwt.user.client.DOM");
    		}
    	});
    	factoryMap.put("buttonGroup", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			StringBuffer sb = new StringBuffer();
    			sw.println("ButtonGroup wid"+id+" = new ButtonGroup();");
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if(widgets.item(k).getNodeType() == Node.ELEMENT_NODE){
    	               int child = ++count;
    	               if(!loadWidget(widgets.item(k),child)){
    	            	   count--;
    	            	   continue;
    	               }
    	               sw.println("wid"+id+".setButtons(wid"+child+");");
    	               break;
    	            }
    	        }
    	        setDefaults(node,"wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.ButtonGroup");
    		}
    	});
    	factoryMap.put("calendar", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("CalendarLookUp wid"+id+" = new CalendarLookUp();");
    			
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
					
    			sw.println("byte begin"+id+" = Byte.parseByte(\""+node.getAttributes().getNamedItem("begin").getNodeValue()+"\");");
    			sw.println("byte end"+id+" = Byte.parseByte(\""+node.getAttributes().getNamedItem("end").getNodeValue()+"\");");
    			if (node.getAttributes().getNamedItem("week") != null)
    				sw.println("wid"+id+".init(begin"+id+",end"+id+","+node.getAttributes().getNamedItem("week").getNodeValue()+");");
    			else
    				sw.println("wid"+id+".init(begin"+id+", end"+id+", false);");
    			sw.println("wid"+id+".setStyleName(\"ScreenCalendar\");");
    			setDefaults(node,"wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    			factoryMap.get("Date").getNewInstance(node, id);
    			sw.println("wid"+id+".setField(field"+id+");");
    			
    			if(node.getAttributes().getNamedItem("mask") != null) 
    				sw.println("wid"+id+".setMask(\""+node.getAttributes().getNamedItem("mask").getNodeValue()+"\");");
    			
    			
    			//sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
    			//sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");

    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.CalendarLookUp");
    		}
    	});
    	factoryMap.put("check", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("CheckBox wid"+id+" = new CheckBox();");
    			
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
					
    	        if(node.getAttributes().getNamedItem("threeState") != null){
    	            sw.println("wid"+id+".setType(CheckBox.CheckType.THREE_STATE);");
    	            //defaultType = CheckBox.CheckType.THREE_STATE;
    	        }
    	       
    	        if (node.getChildNodes().getLength() > 0){
    	            NodeList widgets = node.getChildNodes();
    	            for (int k = 0; k < widgets.getLength(); k++) {
    	                if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	                	int child = ++count;
    	                    loadWidget(widgets.item(k),child);
    	                    sw.println("wid"+id+".setWidget(wid"+child+");");
    	                }
    	            }
    	        }
    	        //check.setStyleName("ScreenCheck");
    	        setDefaults(node, "wid"+id);
    	        
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    	        
    	        factoryMap.get("Check").getNewInstance(node, id);
    	        sw.println("wid"+id+".setField(field"+id+");");
    	        
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
    			if(node.getAttributes().getNamedItem("open") != null) {
    				sw.println("CollapsePanel wid"+id+" = new CollapsePanel("+node.getAttributes().getNamedItem("open").getNodeValue()+");");
    			}else
    				sw.println("CollapsePanel wid"+id+" = new CollapsePanel(false);");
    		    if (node.getChildNodes().getLength() > 0){
    		       NodeList widgets = node.getChildNodes();
    		       for (int k = 0; k < widgets.getLength(); k++) {
    		           if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    		        	   int child = ++count;
    		        	   if(!loadWidget(widgets.item(k),child)){
        	            	   count--;
        	            	   continue;
        	               }
    		               //sw.println("wid"+child+".setHeight(\"100%\");");
    		               //sw.println("wid"+child+".setWidth(\"auto\");");
    		               sw.println("wid"+id+".setContent(wid"+child+");");
    		           }
    		       }
    		    }
    		    if(node.getAttributes().getNamedItem("height") != null)
    		       sw.println("wid"+id+".setHeight(\""+withUnits(node.getAttributes().getNamedItem("width"))+"\");");
    		       
    		    setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.CollapsePanel");
    		}
    	});
    	factoryMap.put("SlidePanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("SlideOutPanel wid"+id+" = new SlideOutPanel();");
    		    if (node.getChildNodes().getLength() > 0){
    		       NodeList widgets = node.getChildNodes();
    		       for (int k = 0; k < widgets.getLength(); k++) {
    		           if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    		        	   int child = ++count;
    		        	   if(!loadWidget(widgets.item(k),child)){
        	            	   count--;
        	            	   continue;
        	               }
    		               sw.println("wid"+child+".setHeight(\"100%\");");
    		               sw.println("wid"+child+".setWidth(\"auto\");");
    		               sw.println("wid"+id+".setContent(wid"+child+");");
    		           }
    		       }
    		    }
    		    if(node.getAttributes().getNamedItem("height") != null)
    		       sw.println("wid"+id+".setHeight(\""+withUnits(node.getAttributes().getNamedItem("height"))+"\");");
    		       
    		    setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.SlideOutPanel");
    		}
    	});
    	factoryMap.put("DeckPanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("DeckPanel wid"+id+" = new DeckPanel();");
    	        sw.println("wid"+id+".setStyleName(\"gwt-TabPanelBottom\");");
    	        if(node.getAttributes().getNamedItem("height") != null)
                	sw.println("wid"+id+".setHeight(\""+withUnits(node.getAttributes().getNamedItem("height"))+"\");");
                if(node.getAttributes().getNamedItem("width") != null)
                	sw.println("wid"+id+".setWidth(\""+withUnits(node.getAttributes().getNamedItem("width"))+"\");");
    	        NodeList decks = ((Element)node).getElementsByTagName("deck");
    	        for (int k = 0; k < decks.getLength(); k++) {
    	            NodeList widgets = decks.item(k).getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	                	int child = ++count;
    	                	if(!loadWidget(widgets.item(l),child)){
    	    	            	   count--;
    	    	            	   continue;
    	    	               }
    	                	if(decks.item(k).getAttributes().getNamedItem("tab") != null) 
    	                		sw.println("wid"+id+".add(wid"+child+",\""+decks.item(k).getAttributes().getNamedItem("tab").getNodeValue()+"\");");
    	                	else
    	                		sw.println("wid"+id+".add(wid"+child+",null);");
    	                }
    	            }
    	        }
    	        sw.println("wid"+id+".showWidget(0);");
    	        setDefaults(node, "wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable")+");");
    	        }
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.DeckPanel");
    			composer.addImport("com.google.gwt.user.client.ui.ScrollPanel");
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
    	factoryMap.put("disclosure", new Factory() {
    		public void getNewInstance(Node node, int id){
    			sw.println("DisclosurePanel wid"+id+" = new DisclosurePanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenDisclosure\");");
    	        Element header = (Element) ((Element)node).getElementsByTagName("header").item(0);
    	        NodeList widgets = header.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	            	if(!loadWidget(widgets.item(k),child)){
     	            	   count--;
     	            	   continue;
     	               }
    	                sw.println("wid"+id+".setHeader(wid"+child+");");
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
    	                int child = ++count;
    	                if(!createWidget(input,child)){
    	                	count--;
    	                	continue;
    	                }
    	                sw.println("wid"+id+".setContent(wid"+child+");");
    	            }
    	        }
    	        setDefaults(node, "wid"+id);

    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.DisclosurePanel");
    		}
    	});
    	factoryMap.put("DockPanel", new Factory() {
    		public void getNewInstance(Node node, int id){
    			sw.println("DockPanel wid"+id+" = new DockPanel();");
    	        sw.println("wid"+id+".addStyleName(\"ScreenDock\");");
    	        if (node.getAttributes().getNamedItem("spacing") != null)
    	            sw.println("wid"+id+".setSpacing(Integer.parseInt(\""+node.getAttributes().getNamedItem("spacing").getNodeValue()+"\"));");
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	            	if(!loadWidget(widgets.item(k),child)){
     	            	   count--;
     	            	   continue;
     	               }
    	                String dir = widgets.item(k)
    	                                    .getAttributes()
    	                                    .getNamedItem("dir")
    	                                    .getNodeValue();
    	                if (dir.equals("north"))
    	                    sw.println("wid"+id+".add(wid"+child+", DockPanel.NORTH);");
    	                else if (dir.equals("south"))
    	                    sw.println("wid"+id+".add(wid"+child+", DockPanel.SOUTH);");
    	                else if (dir.equals("east"))
    	                    sw.println("wid"+id+".add(wid"+child+", DockPanel.EAST);");
    	                else if (dir.equals("west"))
    	                    sw.println("wid"+id+".add(wid"+child+", DockPanel.WEST);");
    	                else if (dir.equals("center"))
    	                    sw.println("wid"+id+".add(wid"+child+", DockPanel.CENTER);");
    	                if (widgets.item(k).getAttributes().getNamedItem("halign") != null) {
    	                    String align = widgets.item(k)
    	                                          .getAttributes()
    	                                          .getNamedItem("align")
    	                                          .getNodeValue();
    	                    if (align.equals("right"))
    	                        sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+",HasAlignment.ALIGN_RIGHT);");
    	                    if (align.equals("left"))
    	                        sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+",HasAlignment.ALIGN_LEFT);");
    	                    if (align.equals("center"))
    	                        sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+",HasAlignment.ALIGN_CENTER);");
    	                }
    	                if (widgets.item(k).getAttributes().getNamedItem("valign") != null) {
    	                    String align = widgets.item(k)
    	                                          .getAttributes()
    	                                          .getNamedItem("valign")
    	                                          .getNodeValue();
    	                    if (align.equals("top"))
    	                        sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+",HasAlignment.ALIGN_TOP);");
    	                    if (align.equals("middle"))
    	                        sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+",HasAlignment.ALIGN_MIDDLE);");
    	                    if (align.equals("bottom"))
    	                        sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+",HasAlignment.ALIGN_BOTTOM);");
    	                }
    	               
    	            }
    	        }
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.DockPanel");
    			composer.addImport("com.google.gwt.user.client.ui.HasAlignment");
    		}
    	});
    	factoryMap.put("editbox",new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("EditBox wid"+id+" = new EditBox();");
    			factoryMap.get("String").getNewInstance(node, id);
    			sw.println("wid"+id+".setField(field"+id+");");
    			
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
					
    			setDefaults(node,"wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    			sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
    			sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
    			
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.EditBox");
    		}
    	});
    	factoryMap.put("HorizontalPanel", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("HorizontalPanel wid"+id+" = new HorizontalPanel();");
    			if (node.getAttributes().getNamedItem("spacing") != null)
    				sw.println("wid"+id+".setSpacing("+node.getAttributes().getNamedItem("spacing").getNodeValue()+");");
    			NodeList widgets = node.getChildNodes();
    			for (int k = 0; k < widgets.getLength(); k++) {
    				if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    					int child = ++count;
    					if(!loadWidget(widgets.item(k),child)){
     	            	   count--;
     	            	   continue;
     	               }
    					sw.println("wid"+id+".add(wid"+child+");");
    					if (widgets.item(k).getAttributes().getNamedItem("halign") != null) {
    						String align = widgets.item(k).getAttributes()
    						.getNamedItem("halign")
    						.getNodeValue();
    						if (align.equals("right"))
    							sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+", HasAlignment.ALIGN_RIGHT);");
    						if (align.equals("left"))
    							sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+", HasAlignment.ALIGN_LEFT);");
    						if (align.equals("center"))
    							sw.println("wid"+id+".setCellHorizontalAlignment(wid"+child+", HasAlignment.ALIGN_CENTER);");
    					}
    					if (widgets.item(k).getAttributes().getNamedItem("valign") != null) {
    						String align = widgets.item(k).getAttributes()
    						.getNamedItem("valign")
    						.getNodeValue();
    						if (align.equals("top"))
    							sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+", HasAlignment.ALIGN_TOP);");
    						if (align.equals("middle"))
    							sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+", HasAlignment.ALIGN_MIDDLE);");
    						if (align.equals("bottom"))
    							sw.println("wid"+id+".setCellVerticalAlignment(wid"+child+", HasAlignment.ALIGN_BOTTOM);");
    					}
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
    			sw.println("final HorizontalSplitPanel wid"+id+" = new HorizontalSplitPanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenSplit\");");
    	        NodeList sections = ((Element)node).getElementsByTagName("section");
    	        for (int k = 0; k < sections.getLength(); k++) {
    	            NodeList widgets = sections.item(k).getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	                	int child = ++count;
    	                	if(!loadWidget(widgets.item(l),child)){
    	    	            	   count--;
    	    	            	   continue;
    	    	               }
    	                    if (k == 0) {
    	                            sw.println("wid"+id+".setLeftWidget(wid"+child+");");        		
    	                    }
    	                    if (k == 1) {
    	                            sw.println("wid"+id+".setRightWidget(wid"+child+");");
    	                    }
    	                }
    	            }
    	        }
    	        if (node.getAttributes().getNamedItem("splitpos") != null) {
    	            String splitpos = node.getAttributes()
    	                                  .getNamedItem("splitpos")
    	                                  .getNodeValue();
    	                sw.println("wid"+id+".setSplitPosition(splitpos);");
    	        }
    	                
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.HorizontalSplitPanel");
    		}
    	});
    	factoryMap.put("html", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("HTML wid"+id+" = new HTML();");
    	        sw.println("wid"+id+".setHTML(\""+node.getTextContent().replaceAll("\\n","")+"\");");
    	        sw.println("wid"+id+".setStyleName(\"ScreenHTML\");");
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.HTML");
    		}
    	});
    	factoryMap.put("label", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			if(node.getAttributes().getNamedItem("field") != null){
    				factoryMap.get(node.getAttributes().getNamedItem("field").getNodeValue()).getNewInstance(node, id);
    				if(node.getAttributes().getNamedItem("field").getNodeValue().equals("Integer"))
    					sw.println("org.openelis.gwt.widget.Label<Integer> wid"+id+" = new org.openelis.gwt.widget.Label<Integer>();");
    				else if(node.getAttributes().getNamedItem("field").getNodeValue().equals("Double"))
    					sw.println("org.openelis.gwt.widget.Label<Double> wid"+id+" = new org.openelis.gwt.widget.Label<Double>();");
    				else if(node.getAttributes().getNamedItem("field").getNodeValue().equals("Long"))
    					sw.println("org.openelis.gwt.widget.Label<Long> wid"+id+" = new org.openelis.gwt.widget.Label<Long>();");
    				else if(node.getAttributes().getNamedItem("field").getNodeValue().equals("Date"))
    					sw.println("org.openelis.gwt.widget.Label<Datetime> wid"+id+" = new org.openelis.gwt.widget.Label<Datetime>();");
    				else if(node.getAttributes().getNamedItem("field").getNodeValue().equals("String"))
    					sw.println("org.openelis.gwt.widget.Label<String> wid"+id+" = new org.openelis.gwt.widget.Label<String>();");
    				sw.println("wid"+id+".setField(field"+id+");");
    			}else{
    				sw.println("org.openelis.gwt.widget.Label<String> wid"+id+" = new org.openelis.gwt.widget.Label<String>();");
    				factoryMap.get("String").getNewInstance(node, id);
    				sw.println("wid"+id+".setField(field"+id+");");
    			}
    				
    	        if (node.getAttributes().getNamedItem("text") != null){
    	            sw.println("wid"+id+".setText(\""+node.getAttributes().getNamedItem("text").getNodeValue()+"\");");
    	        } 
    	        if (node.getAttributes().getNamedItem("wordwrap") != null)
    	            sw.println("wid"+id+".setWordWrap("+node.getAttributes().getNamedItem("wordwrap").getNodeValue()+");");
    	        else
    	            sw.println("wid"+id+".setWordWrap(false);");
    	        if (node.hasChildNodes())
    	            sw.println("wid"+id+".setText(\""+node.getFirstChild().getNodeValue()+"\");");
    	        sw.println("wid"+id+".setStyleName(\"ScreenLabel\");");
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    		}
    	});
    	factoryMap.put("menuItem", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("MenuItem wid"+id+"= new MenuItem();");
    			Widget wid = null;
    			String label;
    	        if(((Element)node).getElementsByTagName("menuDisplay").getLength() > 0 &&  ((Element)node).getElementsByTagName("menuDisplay").item(0).getParentNode().equals(node)){
    	            NodeList displayList = ((Element)node).getElementsByTagName("menuDisplay").item(0).getChildNodes();
    	            int i = 0; 
    	            while(displayList.item(i).getNodeType() != Node.ELEMENT_NODE)
    	                i++;
    	            int child = ++count;
    	            loadWidget(displayList.item(i),child);
    	            sw.println("wid"+id+".init(wid"+child+");");
    	        }else if(node.getAttributes().getNamedItem("header") != null){
    	            sw.println("Widget header = MenuItem.createTableHeader(\"\", new Label(\""+node.getAttributes().getNamedItem("label").getNodeValue()+"\"));");
    	            sw.println("wid"+id+".init(header);");
    	        }else{
    	            sw.println("wid"+id+".init(\""+node.getAttributes().getNamedItem("icon").getNodeValue()+"\",\""+ 
    	                                         node.getAttributes().getNamedItem("label").getNodeValue()+"\",\""+ 
    	                                         node.getAttributes().getNamedItem("description").getNodeValue().replaceAll("\"","'")+"\");");
    	            //label = node.getAttributes().getNamedItem("label").getNodeValue();
    	        }

    	        Node popup = ((Element)node).getElementsByTagName("menuPanel").item(0);
    	        if(node.getAttributes().getNamedItem("enable") != null){
    	            if(node.getAttributes().getNamedItem("enable").getNodeValue().equals("true"))
    	                sw.println("wid"+id+".enable(true);");
    	            else
    	                sw.println("wid"+id+".enable(false);");
    	        }else
    	            sw.println("wid"+id+".enable(true);");
    	        
    	        if(popup != null){
    	        	int child = ++count;
    	        	loadWidget(popup, child);
    	            if(popup.getAttributes().getNamedItem("position") != null)
    	                sw.println("wid"+id+".setMenuPopup((MenuPanel)wid"+child+", MenuItem.PopPosition.valueOf(\""+popup.getAttributes().getNamedItem("position").getNodeValue().toUpperCase()+"\"));");
    	            else
    	            	sw.println("wid"+id+".setMenuPopup((MenuPanel)wid"+child+", MenuItem.PopPosition.BESIDE);");
    	        }

    	        if(node.getAttributes().getNamedItem("key") != null){
    	            sw.println("wid"+id+".key = \""+node.getAttributes().getNamedItem("key").getNodeValue()+"\";");
    	        }
    	        
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
			    
    	        setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.MenuItem");
    			composer.addImport("org.openelis.gwt.widget.MenuPanel");
    			composer.addImport("com.google.gwt.user.client.ui.Widget");
    		}
    	});
    	factoryMap.put("menuPanel",new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("MenuPanel wid"+id+" = new MenuPanel();");
    		       String layout = node.getAttributes().getNamedItem("layout").getNodeValue();
    		        sw.println("wid"+id+".init(\""+layout+"\");");
    		        NodeList items = node.getChildNodes();
    		        for (int i = 0; i < items.getLength(); i++) {
    		            if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
    		            	int child = ++count;
    		            	if(!loadWidget(items.item(i),child)){
    	    	            	   count--;
    	    	            	   continue;
    	    	            }
   		                    sw.println("wid"+id+".add(wid"+child+");");
    		            }
    		        }
    		        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.MenuPanel");
    		}
    	});
    	factoryMap.put("password", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			StringBuffer sb = new StringBuffer();
    			sw.println("PassWordTextBox wid"+id+" = new PassWordTextBox();");
    			
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
			    
    	        sw.println("wid"+id+".setStyleName(\"ScreenPassword\");");
    	        setDefaults(node, "wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    	        factoryMap.get("String").getNewInstance(node, id);
    	        sw.println("wid"+id+".setField(field"+id+");");
    	        
    	        sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
    	        sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
    	        
    		}
    		public void addImport(){
    			composer.addImport("org.openelis.gwt.widget.PassWordTextBox");
    		}
    	});
    	factoryMap.put("radio", new Factory() {
    		public void getNewInstance(Node node, int id) {
                sw.println("RadioButton wid"+id+" = new RadioButton(\""+node.getAttributes().getNamedItem("group").getNodeValue()+"\");");
                
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
			    if (node.getFirstChild() != null)
                	sw.println("wid"+id+".setText(\""+node.getFirstChild().getNodeValue()+"\");");
                sw.println("wid"+id+".setStyleName(\"ScreenRadio\");");
                setDefaults(node, "wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
                factoryMap.get("Check").getNewInstance(node, id);
                sw.println("wid"+id+".setField(field"+id+");");
                
                sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
                sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
                
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.RadioButton");
    		}
    	});
    	factoryMap.put("richtext", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("RichTextWidget wid"+id+" = new RichTextWidget();"); 
    			boolean tools = true;
    	        if(node.getAttributes().getNamedItem("tools") != null){
    	            if(node.getAttributes().getNamedItem("tools").getNodeValue().equals("false")){
    	                tools = false;
    	            }
    	        }
    	        
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
				
    	        String width = "100%";
    	        String height = "300px";
    	        if(node.getAttributes().getNamedItem("width") != null){
    	            width = withUnits(node.getAttributes().getNamedItem("width"));
    	        }
    	        if(node.getAttributes().getNamedItem("height") != null) {
    	            height = withUnits(node.getAttributes().getNamedItem("height"));
    	        }
    	        sw.println("wid"+id+".init("+tools+");");
    	        sw.println("wid"+id+".area.setSize(\""+width+"\",\""+height+"\");");
    	        
    	        sw.println("wid"+id+".area.setStyleName(\"ScreenTextArea\");");
    	        setDefaults(node, "wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    	        factoryMap.get("String").getNewInstance(node, id);
    	        sw.println("wid"+id+".setField(field"+id+");");
    	        
    	        sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
    	        sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
    	        
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.richtext.RichTextWidget");
    		}
    	});
    	factoryMap.put("ScrollTabBar", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			StringBuffer sb = new StringBuffer();
    			sw.println("ScrollableTabBar wid"+id+" = new ScrollableTabBar();");
    	        //scrollableTabBar.setStyleName("ScreenTab");        
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.ScrollableTabBar");
    		}
    	});
    	factoryMap.put("TabBar", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("TabBar wid"+id+" = new TabBar();");
    			NodeList tabs = ((Element)node).getElementsByTagName("tab");
      	        for (int k = 0; k < tabs.getLength(); k++) {
      	        	sw.println("wid"+id+".addTab(\""+tabs.item(k).getAttributes().getNamedItem("text").getNodeValue()+"\");");
      	        	if(tabs.item(k).getAttributes().getNamedItem("visible") != null) {
      	        		sw.println("wid"+id+".setTabVisible(wid"+id+".getTabCount() -1, "+tabs.item(k).getAttributes().getNamedItem("visible").getNodeValue()+");");
      	        	}
      	        }
      	        if(tabs.getLength() > 0)
      	        	sw.println("wid"+id+".selectTab(0);");
    			setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.TabBar");
    		}
    	});
    	factoryMap.put("StackPanel", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			sw.println("StackPanel wid"+id+" = new StackPanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenStack\");");
    	        NodeList stacks = ((Element)node).getElementsByTagName("stack");
    	        for (int k = 0; k < stacks.getLength(); k++) {
    	            NodeList widgets = stacks.item(k).getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	                	int child = ++count;
    	                	if(!loadWidget(widgets.item(l),child)){
    	    	            	   count--;
    	    	            	   continue;
    	    	               }
    	                    sw.println("wid"+id+".add(wid"+child+", \""+stacks.item(k).getAttributes().getNamedItem("text").getNodeValue()+"\");");
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
    	factoryMap.put("TabPanel", new Factory() {
    		public void getNewInstance(Node node, int id){
    			sw.println("TabPanel wid"+id+" = new TabPanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenTab\");");
    	        if(node.getAttributes().getNamedItem("width") != null)
    	        	sw.println("wid"+id+".setWidth(\""+withUnits(node.getAttributes().getNamedItem("width"))+"\");");
    	        else
    	        	sw.println("wid"+id+".setWidth(\"auto\");");
    	        if(node.getAttributes().getNamedItem("height") != null)
    	        	sw.println("wid"+id+".setHeight(\""+withUnits(node.getAttributes().getNamedItem("height"))+"\");");
    	        else
    	        	sw.println("wid"+id+".setHeight(\"auto\");");
    	        NodeList tabs = ((Element)node).getElementsByTagName("tab");
    	        boolean visible = false;
    	        for (int k = 0; k < tabs.getLength(); k++) {
    	            NodeList widgets = tabs.item(k).getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	                	int child = ++count;
    	                	if(!loadWidget(widgets.item(l),child)){
    	    	            	   count--;
    	    	            	   continue;
    	    	               }
    	                    if(tabs.item(k).getAttributes().getNamedItem("tab") != null)
    	                    	sw.println("wid"+id+".add(wid"+child+", \""+
    	                    			                  tabs.item(k).getAttributes().getNamedItem("text").getNodeValue()+"\",\""+
    	                    			                  tabs.item(k).getAttributes().getNamedItem("tab").getNodeValue()+"\");");
    	                    else
    	                    	sw.println("wid"+id+".add(wid"+child+", \""+tabs.item(k).getAttributes().getNamedItem("text").getNodeValue()+"\");");
    	                    if(tabs.item(k).getAttributes().getNamedItem("visible") != null) {
    	                    	if(tabs.item(k).getAttributes().getNamedItem("visible").getNodeValue().equals("true"))
    	                    			visible= true;
    	                    	else
    	                    		sw.println("wid"+id+".setTabVisible(wid"+id+".getTabBar().getTabCount() -1, false);");
    	      	        	}else
    	      	        		visible= true;
    	                }
    	            }
    	            if(!visible)
    	            	sw.println("wid"+id+".getTabBar().setStyleName(\"None\");");
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
    	factoryMap.put("text",new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("Label wid"+id+" = new Label();");
    	        if (node.getAttributes().getNamedItem("wordwrap") != null)
    	            sw.println("wid"+id+".setWordWrap("+node.getAttributes().getNamedItem("wordwrap").getNodeValue().trim()+");");
    	        else
    	            sw.println("wid"+id+".setWordWrap(false);");
    	        if (node.hasChildNodes())
    	            sw.println("wid"+id+".setText(\""+node.getFirstChild().getNodeValue().trim()+"\");");
    	        else
    	            sw.println("wid"+id+".setText(\"\");");
    	        sw.println("wid"+id+".setStyleName(\"ScreenLabel\");");
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.Label");
    		}
    	});
    	factoryMap.put("textarea", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			sw.println("TextArea wid"+id+" = new TextArea();");
    			
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
			    
    	        sw.println("wid"+id+".setStyleName(\"ScreenTextArea\");");
    	        setDefaults(node, "wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    	        factoryMap.get("String").getNewInstance(node, id);
    	        sw.println("wid"+id+".setField(field"+id+");");
    	        
    	        sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
    	        sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
				sw.println("wid"+id+".addFocusHandler(panel);");
    	        
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.TextArea");
    		}
    	});
    	factoryMap.put("TitledPanel", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			sw.println("TitledPanel wid"+id+" = new TitledPanel();");
    	        Element legend = (Element) ((Element)node).getElementsByTagName("legend").item(0);
    	        NodeList widgets = legend.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	            	if(!loadWidget(widgets.item(k),child)){
     	            	   count--;
     	            	   continue;
     	               }
    	                sw.println("wid"+id+".setTitle(wid"+child+");");
    	            }
    	        }
    	        
    	        legend = (Element) ((Element)node).getElementsByTagName("content").item(0);
    	        widgets = legend.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	            	if(!loadWidget(widgets.item(k),child)){
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
    	factoryMap.put("VerticalSplitPanel", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			sw.println("VerticalSplitPanel wid"+id+" = new VerticalSplitPanel();");
    	        sw.println("wid"+id+".setStyleName(\"ScreenSplit\");");
    	        NodeList sections = ((Element)node).getElementsByTagName("section");
    	        for (int k = 0; k < sections.getLength(); k++) {
    	            NodeList widgets = sections.item(k).getChildNodes();
    	            for (int l = 0; l < widgets.getLength(); l++) {
    	                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	                	int child = ++count;
    	                	if(!loadWidget(widgets.item(l),child)){
    	    	            	   count--;
    	    	            	   continue;
    	    	               }
    	                    if (k == 0) {
    	                             sw.println("wid"+id+".setTopWidget(wid"+child+");");
    	                    }
    	                    if (k == 1) {
    	                             sw.println("wid"+id+".setBottomWidget(wid"+child+");");
    	                    }
    	                }
    	            }
    	        }
    	        if (node.getAttributes().getNamedItem("splitpos") != null) {
    	            String splitpos = node.getAttributes()
    	                                  .getNamedItem("splitpos")
    	                                  .getNodeValue();
    	                 sw.println("wid"+id+".setSplitPosition(splitpos);");
    	        }
    	        setDefaults(node, "wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("com.google.gwt.user.client.ui.VerticalSplitPanel");
    		}
    	});
    	factoryMap.put("winbrowser", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			sw.println("WindowBrowser wid"+id+" = new WindowBrowser();");
    	        int limit = 10;
    	        if(node.getAttributes().getNamedItem("winLimit") != null){
    	            limit = Integer.parseInt(node.getAttributes().getNamedItem("winLimit").getNodeValue());
    	        }
    	        if(node.getAttributes().getNamedItem("sizeToWindow") != null)
    	            sw.println("wid"+id+".init(true,"+limit+");");
    	        else
    	            sw.println("wid"+id+".init(false,"+limit+");");
    	        sw.println("wid"+id+".setStyleName(\"ScreenWindowBrowser\");");
    	        setDefaults(node,"wid"+id);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.WindowBrowser");
    		}
    	});
    	factoryMap.put("table", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("TableWidget wid"+id+" = new TableWidget();");
    			
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
			                   
                sw.println("wid"+id+".setTableWidth(\""+withUnits(node.getAttributes().getNamedItem("width"))+"\");");
                sw.println("wid"+id+".setMaxRows(Integer.parseInt(\""+node.getAttributes().getNamedItem("maxRows").getNodeValue()+"\"));");

                if(node.getAttributes().getNamedItem("title") != null){
                        sw.println("wid"+id+".setTitle(\""+node.getAttributes().getNamedItem("title").getNodeValue() + "\");");
                }
                if(node.getAttributes().getNamedItem("showScroll") != null){
                	String showScroll = node.getAttributes().getNamedItem("showScroll").getNodeValue();
                    sw.println("wid"+id+".setShowScroll(VerticalScroll.valueOf(\""+showScroll+"\"));");
                }
                if(node.getAttributes().getNamedItem("multiSelect") != null){
                    if(node.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true"))
                        sw.println("wid"+id+".enableMultiSelect(true);");
                }
                NodeList colList = node.getChildNodes();
                sw.println("wid"+id+".setColumns(new ArrayList<TableColumn>());");
                for(int i = 0; i < colList.getLength(); i++) {
                	Node col = colList.item(i);
                	if(col.getNodeType() != Node.ELEMENT_NODE || !col.getNodeName().equals("col"))
                		continue;
                	if(col.getAttributes().getNamedItem("class") != null){
                		String colClass = col.getAttributes().getNamedItem("class").getNodeValue();
                		sw.println(colClass+" column"+id+"_"+i+" = new "+colClass+"();");
                	}else
                		sw.println("TableColumn column"+id+"_"+i+" = new TableColumn();");
                	sw.println("column"+id+"_"+i+".controller = wid"+id+";");
                	if(col.getAttributes().getNamedItem("key") != null)
                		sw.println("column"+id+"_"+i+".setKey(\""+col.getAttributes().getNamedItem("key").getNodeValue()+"\");");
                	if(col.getAttributes().getNamedItem("header") != null){
                		sw.println("column"+id+"_"+i+".setHeader(\""+col.getAttributes().getNamedItem("header").getNodeValue()+"\");");
                		sw.println("wid"+id+".showHeader(true);");
                	}
                	if(col.getAttributes().getNamedItem("width") != null)
                		sw.println("column"+id+"_"+i+".setCurrentWidth("+col.getAttributes().getNamedItem("width").getNodeValue()+");");
                	if(col.getAttributes().getNamedItem("minWidth") != null)
                		sw.println("column"+id+"_"+i+".setMinWidth("+col.getAttributes().getNamedItem("minWidth").getNodeValue()+");");
                	if(col.getAttributes().getNamedItem("fixedWidth") != null)
                		sw.println("column"+id+"_"+i+".setFixedWidth("+col.getAttributes().getNamedItem("fixedWidth").getNodeValue()+");");
                	if(col.getAttributes().getNamedItem("sort") != null)
                		sw.println("column"+id+"_"+i+".setSortable("+col.getAttributes().getNamedItem("sort").getNodeValue()+");");
                	if(col.getAttributes().getNamedItem("filter") != null)
                		sw.println("column"+id+"_"+i+".setFilterable("+col.getAttributes().getNamedItem("filter").getNodeValue()+");");
                	if(col.getAttributes().getNamedItem("query") != null)
                		sw.println("column"+id+"_"+i+".setQuerayable("+col.getAttributes().getNamedItem("query").getNodeValue()+");");
    	            if(col.getAttributes().getNamedItem("align") != null){
                		String align = col.getAttributes().getNamedItem("align").getNodeValue();
                        if (align.equals("left"))
                            sw.println("column"+id+"_"+i+".setAlign(HasAlignment.ALIGN_LEFT);");
                        if (align.equals("center"))
                            sw.println("column"+id+"_"+i+".setAlign(HasAlignment.ALIGN_CENTER);");
                        if (align.equals("right"))
                            sw.println("column"+id+"_"+i+".setAlign(HasAlignment.ALIGN_RIGHT);");
                	}
    	            sw.println("column"+id+"_"+i+".columnIndex = "+i+";");
                	NodeList editor = col.getChildNodes();
                	for(int j = 0; j < editor.getLength(); j++){
                		if(editor.item(j).getNodeType() == Node.ELEMENT_NODE) {
                			int child = ++count;
                			if(!createWidget(editor.item(j),child)){
                				count--;
                				continue;
                			}
                			//sw.println("if(wid"+child+" instanceof HasBlurHandlers)");
                			//sw.println("((HasBlurHandlers)wid"+child+").addBlurHandler(wid"+id+");");
                			sw.println("column"+id+"_"+i+".setColumnWidget(wid"+child+");");
                			break;
                		}
                	}
                	sw.println("wid"+id+".getColumns().add(column"+id+"_"+i+");");
                }
                sw.println("wid"+id+".setTableWidth(wid"+id+".getTableWidth()+\"px\");");
                sw.println("wid"+id+".init();");
                sw.println("wid"+id+".setStyleName(\"ScreenTable\");");
                //setDefaults(node,"wid"+id);
            	StringBuffer sb = new StringBuffer();
                if (node.getAttributes().getNamedItem("style") != null){
                    String[] styles = node.getAttributes().getNamedItem("style").getNodeValue().split(",");
                    sw.println("wid"+id+".setStyleName(\""+styles[0]+"\");");
                    for(int i = 1; i < styles.length; i++){
                        sw.println("wid"+id+".addStyleName(\""+styles[i]+"\");");
                    }
                }
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
                
                sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
                sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
                sw.println("panel.addFocusHandler(wid"+id+");");
				sw.println("wid"+id+".addFocusHandler(panel);");
				
                
                //if(def != null)
                	//def.panel.addClickHandler(table);
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.table.TableWidget");
    			composer.addImport("org.openelis.gwt.widget.table.TableColumn");
    			composer.addImport("org.openelis.gwt.widget.table.TableView.VerticalScroll");
    			composer.addImport("com.google.gwt.user.client.ui.HasAlignment");
    		}
    	});
    	factoryMap.put("dropdown", new Factory(){
    		public void getNewInstance(Node node, int id){
    		    if(node.getAttributes().getNamedItem("field") != null){
    		        if(node.getAttributes().getNamedItem("field").getNodeValue().equals("String")){
    		            sw.println("Dropdown<String> wid"+id+" = new Dropdown<String>();");
    		            factoryMap.get("String").getNewInstance(node,id);
    		        }else {
    		            sw.println("Dropdown<Integer> wid"+id+" = new Dropdown<Integer>();");
    		            factoryMap.get("Integer").getNewInstance(node,id);
    		        }
    		    }else{
    		        sw.println("Dropdown<Integer> wid"+id+"= new Dropdown<Integer>();");
    		        factoryMap.get("Integer").getNewInstance(node,id);
    		    }
    		    
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
			    
    		    sw.println("wid"+id+".setField(field"+id+");");
    		    		
    		    sw.println("wid"+id+".setMultiSelect(false);");
    		        	                
    	        if (node.getAttributes().getNamedItem("multiSelect") != null && node.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true"))
    	        	sw.println("wid"+id+".setMultiSelect(true);");
    	        
    	        if (node.getAttributes().getNamedItem("text") != null)
    	        	sw.println("wid"+id+".textBoxDefault = \""+node.getAttributes().getNamedItem("text").getNodeValue()+"\";");
    	        
    	        if (node.getAttributes().getNamedItem("width") != null){
    	        	sw.println("wid"+id+".dropwidth = \""+node.getAttributes().getNamedItem("width").getNodeValue()+"\";");
    	        }
    	        
    	        if (node.getAttributes().getNamedItem("delay") != null) {
    	        	sw.println("wid"+id+".setDelay("+node.getAttributes().getNamedItem("delay").getNodeValue()+");");
    	        }
    	        
    	        if (node.getAttributes().getNamedItem("popWidth") != null)
    	            sw.println("wid"+id+".setTableWidth(\""+node.getAttributes().getNamedItem("popWidth").getNodeValue()+"\");");
    	        else
    	        	sw.println("wid"+id+".setTableWidth(\"auto\");");
                NodeList colList = ((Element)node).getElementsByTagName("col");
                sw.println("wid"+id+".setColumns(new ArrayList<TableColumn>());");
                if(colList.getLength() > 0){
                    for(int i = 0; i < colList.getLength(); i++) {
                        Node col = colList.item(i);
                        sw.println("TableColumn column"+id+"_"+i+" = new TableColumn();");
                        sw.println("column"+id+"_"+i+".controller = wid"+id+";");
                        if(col.getAttributes().getNamedItem("key") != null)
                            sw.println("column"+id+"_"+i+".setKey(\""+col.getAttributes().getNamedItem("key").getNodeValue()+"\");");
                        if(col.getAttributes().getNamedItem("header") != null) {
                            sw.println("column"+id+"_"+i+".setHeader(\""+col.getAttributes().getNamedItem("header").getNodeValue()+"\");");
                            sw.println("wid"+id+".showHeader(true);");
                        }
                        if(col.getAttributes().getNamedItem("width") != null)
                            sw.println("column"+id+"_"+i+".setCurrentWidth("+col.getAttributes().getNamedItem("width").getNodeValue()+");");
                        if(col.getAttributes().getNamedItem("sort") != null)
                            sw.println("column"+id+"_"+i+".setSortable(\""+col.getAttributes().getNamedItem("sort").getNodeValue()+"\");");
                        if(col.getAttributes().getNamedItem("filter") != null)
                            sw.println("column"+id+"_"+i+".setFilterable(\""+col.getAttributes().getNamedItem("filter").getNodeValue()+"\");");
                        if(col.getAttributes().getNamedItem("align") != null){
                            String align = col.getAttributes().getNamedItem("align").getNodeValue();
                            if (align.equals("left"))
                                sw.println("column"+id+"_"+i+".setAlign(HasAlignment.ALIGN_LEFT);");
                            if (align.equals("center"))
                                sw.println("column"+id+"_"+i+".setAlign(HasAlignment.ALIGN_CENTER);");
                            if (align.equals("right"))
                                sw.println("column"+id+"_"+i+".setAlign(HasAlignment.ALIGN_RIGHT);");
                        }
                        sw.println("column"+id+"_"+i+".columnIndex = "+i+";");
                        boolean widgetDefined = false;
                        NodeList editor = col.getChildNodes();
                        for(int j = 0; j < editor.getLength(); j++){
                            if(editor.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                int child = ++count;
                                if(!createWidget(editor.item(j),child)){
                                    count--;
                                    continue;
                                }
                                widgetDefined = true;
                                sw.println("column"+id+"_"+i+".setColumnWidget(wid"+child+");");
                                break;
                            }
                        }
                        if(!widgetDefined){
                            int child = ++count;
                            factoryMap.get("label").getNewInstance(colList.item(i),child);
                            sw.println("column"+id+"_"+i+".setColumnWidget(wid"+child+");");
                        }
                        sw.println("wid"+id+".getColumns().add(column"+id+"_"+i+");");  
                    }
                }else{
                	sw.println("TableColumn column"+id+" = new TableColumn();");
                	sw.println("column"+id+".controller = wid"+id+";");
                	 if (node.getAttributes().getNamedItem("width") != null) {
         	        	String width  = node.getAttributes().getNamedItem("width").getNodeValue();
         	        	int wid = 0;
         	        	if(width.indexOf("px") > -1)
         	        		wid = Integer.parseInt(width.substring(0,width.indexOf("px")));
            			else
            				wid = Integer.parseInt(width);
         	        	sw.println("column"+id+".setCurrentWidth("+wid+");");
                	 }else{
                		 sw.println("column"+id+".setCurrentWidth(100);");
                	 }
                	sw.println("org.openelis.gwt.widget.Label label"+id+" = new org.openelis.gwt.widget.Label();");
                	sw.println("label"+id+".setField(new StringField());");
                	sw.println("column"+id+".setColumnWidget(label"+id+");");
                	sw.println("wid"+id+".getColumns().add(column"+id+");");
                }
                sw.println("wid"+id+".setup();");
				if (node.getAttributes().getNamedItem("case") != null){
					String fieldCase = node.getAttributes().getNamedItem("case")
					.getNodeValue().toUpperCase();
					sw.println("wid"+id+".textbox.setCase(TextBox.Case.valueOf(\""+fieldCase+"\"));");
	        	}
				sw.println("panel.addFocusHandler(wid"+id+");");
				sw.println("wid"+id+".addFocusHandler(panel);");
				setDefaults(node,"wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.Dropdown");
    			composer.addImport("org.openelis.gwt.widget.table.TableColumn");
    			composer.addImport("com.google.gwt.user.client.ui.HasAlignment");
    			composer.addImport("org.openelis.gwt.widget.TextBox");
    		}
    	});
    	factoryMap.put("autoComplete", new Factory() {
    		public void getNewInstance(Node node, int id) {
                if(node.getAttributes().getNamedItem("field") != null){
                    if(node.getAttributes().getNamedItem("field").getNodeValue().equals("String")){
                        sw.println("AutoComplete<String> wid"+id+" = new AutoComplete<String>();");
                        factoryMap.get("String").getNewInstance(node,id);
                    }else if(node.getAttributes().getNamedItem("field").getNodeValue().equals("Integer")) {
                        sw.println("AutoComplete<Integer> wid"+id+" = new AutoComplete<Integer>();");
                        factoryMap.get("Integer").getNewInstance(node,id);
                    }else {
    					sw.println("AutoComplete<Long> wid"+id+" = new AutoComplete<Long>();");
    					factoryMap.get("Long").getNewInstance(node,id);
    				}
                }else{
                    sw.println("AutoComplete<Integer> wid"+id+" = new AutoComplete<Integer>();");
                    factoryMap.get("Long").getNewInstance(node,id);
                }
                sw.println("wid"+id+".setField(field"+id+");");
                
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
			    
    	        if (node.getAttributes().getNamedItem("multiSelect") != null && node.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true"))
    	        	sw.println("wid"+id+".multiSelect(true);");
    	        
    	        if (node.getAttributes().getNamedItem("text") != null)
    	        	sw.println("wid"+id+".textBoxDefault = \""+node.getAttributes().getNamedItem("text").getNodeValue()+"\";");
    	        else
    	            sw.println("wid"+id+".textBoxDefault = \"\";");
    	        
    	        if (node.getAttributes().getNamedItem("max") != null)
    	        	sw.println("wid"+id+".textbox.setLength("+node.getAttributes().getNamedItem("max").getNodeValue()+");");
    	        
    	        if (node.getAttributes().getNamedItem("width") != null)
    	        	sw.println("wid"+id+".setWidth(\""+node.getAttributes().getNamedItem("width").getNodeValue()+"\");");
    	        
    	        if (node.getAttributes().getNamedItem("delay") != null) 
    	        	sw.println("wid"+id+".setDelay("+node.getAttributes().getNamedItem("delay").getNodeValue()+");");
    	        
    	        if (node.getAttributes().getNamedItem("popWidth") != null)
    	            sw.println("wid"+id+".setTableWidth(\""+node.getAttributes().getNamedItem("popWidth").getNodeValue()+"\");");
    	        else
    	        	sw.println("wid"+id+".setTableWidth(\"auto\");");
    	        if(node.getAttributes().getNamedItem("case") != null){
    	            String textCase = node.getAttributes().getNamedItem("case").getNodeValue().toUpperCase();
    	            sw.println("wid"+id+".textbox.setCase(TextBox.Case.valueOf(\""+textCase+"\"));");
    	        }
                NodeList colList = ((Element)node).getElementsByTagName("col");
                sw.println("wid"+id+".setColumns(new ArrayList<TableColumn>());");
                if(colList.getLength() > 0){
                	for(int i = 0; i < colList.getLength(); i++) {
                		Node col = colList.item(i);
                		sw.println("TableColumn column"+id+"_"+i+" = new TableColumn();");
                		sw.println("column"+id+"_"+i+".controller = wid"+id+";");
                		if(col.getAttributes().getNamedItem("key") != null)
                			sw.println("column"+id+"_"+i+".setKey(\""+col.getAttributes().getNamedItem("key").getNodeValue()+"\");");
                		if(col.getAttributes().getNamedItem("header") != null) {
                			sw.println("column"+id+"_"+i+".setHeader(\""+col.getAttributes().getNamedItem("header").getNodeValue()+"\");");
                			sw.println("wid"+id+".showHeader(true);");
                		}
                		if(col.getAttributes().getNamedItem("width") != null)
                			sw.println("column"+id+"_"+i+".setCurrentWidth("+col.getAttributes().getNamedItem("width").getNodeValue()+");");
                		if(col.getAttributes().getNamedItem("sort") != null)
                			sw.println("column"+id+"_"+i+".setSortable(\""+col.getAttributes().getNamedItem("sort").getNodeValue()+"\");");
                		if(col.getAttributes().getNamedItem("filter") != null)
                			sw.println("column"+id+"_"+i+".setFilterable(\""+col.getAttributes().getNamedItem("filter").getNodeValue()+"\");");
                		if(col.getAttributes().getNamedItem("align") != null){
                			String align = col.getAttributes().getNamedItem("align").getNodeValue();
                			if (align.equals("left"))
                				sw.println("column"+id+"_"+i+".setAlign(HasAlignment.ALIGN_LEFT);");
                			if (align.equals("center"))
                				sw.println("column"+id+"_"+i+".setAlign(HasAlignment.ALIGN_CENTER);");
                			if (align.equals("right"))
                				sw.println("column"+id+"_"+i+".setAlign(HasAlignment.ALIGN_RIGHT);");
                		}
                		sw.println("column"+id+"_"+i+".columnIndex = "+i+";");
                		boolean widgetDefined = false;
                    	NodeList editor = col.getChildNodes();
                    	for(int j = 0; j < editor.getLength(); j++){
                    		if(editor.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    			int child = ++count;
                    			if(!createWidget(editor.item(j),child)){
                    				count--;
                    				continue;
                    			}
                    			widgetDefined = true;
                    			sw.println("column"+id+"_"+i+".setColumnWidget(wid"+child+");");
                    			break;
                    		}
                    	}
                    	if(!widgetDefined){
                    		int child = ++count;
                    		factoryMap.get("label").getNewInstance(colList.item(i),child);
                    		sw.println("column"+id+"_"+i+".setColumnWidget(wid"+child+");");
                    	}
                		sw.println("wid"+id+".getColumns().add(column"+id+"_"+i+");");	
                	}
                }else{
                	sw.println("TableColumn column"+id+" = new TableColumn();");
                	sw.println("column"+id+".setCurrentWidth(100);");
                	sw.println("org.openelis.gwt.widget.Label label"+id+" = new org.openelis.gwt.widget.Label();");
                	sw.println("label"+id+".setField(new StringField());");
                	sw.println("column"+id+".setColumnWidget(label"+id+");");
                	sw.println("column"+id+".controller = wid"+id+";");
                	sw.println("wid"+id+".getColumns().add(column"+id+");");
                }
                sw.println("wid"+id+".init();");
				sw.println("wid"+id+".addFocusHandler(panel);");
				if (node.getAttributes().getNamedItem("case") != null){
					String fieldCase = node.getAttributes().getNamedItem("case")
					.getNodeValue().toUpperCase();
					sw.println("wid"+id+".textbox.setCase(TextBox.Case.valueOf(\""+fieldCase+"\"));");
	        	}
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.AutoComplete");
    			composer.addImport("org.openelis.gwt.widget.table.TableColumn");
    			composer.addImport("com.google.gwt.user.client.ui.HasAlignment");
    			composer.addImport("org.openelis.gwt.widget.TextBox");
    		}
    	});
    	factoryMap.put("tree", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("TreeWidget wid"+id+" = new TreeWidget();");
    			
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
                
                sw.println("wid"+id+".setTreeWidth(\""+node.getAttributes().getNamedItem("width").getNodeValue()+"\");");
                sw.println("wid"+id+".setMaxRows("+node.getAttributes().getNamedItem("maxRows").getNodeValue()+");");

                if(node.getAttributes().getNamedItem("title") != null){
                        sw.println("wid"+id+".title = \""+node.getAttributes().getNamedItem("title").getNodeValue()+"\";");
                }
                if(node.getAttributes().getNamedItem("showScroll") != null){
                	String showScroll = node.getAttributes().getNamedItem("showScroll").getNodeValue();
                    sw.println("wid"+id+".setShowScroll(TreeView.VerticalScroll.valueOf(\""+showScroll+"\"));");
                }
                if(node.getAttributes().getNamedItem("multiSelect") != null){
                    if(node.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true"))
                        sw.println("wid"+id+".enableMultiSelect(true);");
                }
                Node header  = ((Element)node).getElementsByTagName("header").item(0);
                if(header != null){
                	boolean showHeader = false;
                    NodeList colList = ((Element)header).getElementsByTagName("col");
                    sw.println("wid"+id+".setHeaders(new ArrayList<TreeColumn>("+colList.getLength()+"));");
                    for(int i = 0; i < colList.getLength(); i++) {
                    	sw.println("TreeColumn col"+id+"_"+i+" = new TreeColumn();");
                		if(colList.item(i).getAttributes().getNamedItem("header") != null){
                			sw.println("col"+id+"_"+i+".setHeader(\""+colList.item(i).getAttributes().getNamedItem("header").getNodeValue()+"\");");
                			//sw.println("wid"+id+".showHeader(true);");
                			showHeader = true;
                		}
                		if(colList.item(i).getAttributes().getNamedItem("width") != null)
                			sw.println("col"+id+"_"+i+".setCurrentWidth("+colList.item(i).getAttributes().getNamedItem("width").getNodeValue()+");");
                    	if(colList.item(i).getAttributes().getNamedItem("minWidth") != null)
                    		sw.println("col"+id+"_"+i+".setMinWidth("+colList.item(i).getAttributes().getNamedItem("minWidth").getNodeValue()+");");
                    	if(colList.item(i).getAttributes().getNamedItem("fixedWidth") != null)
                    		sw.println("col"+id+"_"+i+".setFixedWidth("+colList.item(i).getAttributes().getNamedItem("fixedWidth").getNodeValue()+");");
                		if(colList.item(i).getAttributes().getNamedItem("sort") != null){
                			sw.println("col"+id+"_"+i+".setSortable(true);");
                			String[] sortLeaves = colList.item(i).getAttributes().getNamedItem("sort").getNodeValue().split(",");
                			sw.println("col"+id+"_"+i+".sortLeaves = new ArrayList<String>();");
                			for(String leaf : sortLeaves) 
                				sw.println("col"+id+"_"+i+".sortLeaves.add(\""+leaf+"\");");
                		}
                		sw.println("wid"+id+".getHeaders().add(col"+id+"_"+i+");");
                				
                    }
                    if(showHeader)
                    	sw.println("wid"+id+".showHeader(true);");
                }
                NodeList leafList = ((Element)node).getElementsByTagName("leaf");
                sw.println("wid"+id+".setColumns(new HashMap<String,ArrayList<TreeColumn>>());");
                for(int h = 0; h < leafList.getLength(); h++) {
                	NodeList colList = leafList.item(h).getChildNodes();
                	sw.println("ArrayList<TreeColumn> cols"+id+"_"+h+" = new ArrayList<TreeColumn>();");
                	int colIndex = -1;
                	for(int i = 0; i < colList.getLength(); i++) {
                    	Node col = colList.item(i);
                    	if(col.getNodeType() != Node.ELEMENT_NODE || !col.getNodeName().equals("col"))
                    		continue;
                    	colIndex++;
                		sw.println("TreeColumn column"+id+"_"+h+"_"+i+" = new TreeColumn();");
                		if(col.getAttributes().getNamedItem("key") != null)
                			sw.println("column"+id+"_"+h+"_"+i+".setKey(\""+col.getAttributes().getNamedItem("key").getNodeValue()+"\");");
                		if(header != null) {
                			sw.println("column"+id+"_"+h+"_"+i+".setCurrentWidth(wid"+id+".getHeaders().get("+i+").currentWidth);");		
                		}
                		if(col.getAttributes().getNamedItem("align") != null){
                			String align = col.getAttributes().getNamedItem("align").getNodeValue();
                			if (align.equals("left"))
                				sw.println("column"+id+"_"+h+"_"+i+".setAlign(HasAlignment.ALIGN_LEFT);");
                			if (align.equals("center"))
                				sw.println("column"+id+"_"+h+"_"+i+".setAlign(HasAlignment.ALIGN_CENTER);");
                			if (align.equals("right"))
                				sw.println("column"+id+"_"+h+"_"+i+".setAlign(HasAlignment.ALIGN_RIGHT);");
                		}
                		NodeList editor = col.getChildNodes();
                		for(int j = 0; j < editor.getLength(); j++){
                			if(editor.item(j).getNodeType() == Node.ELEMENT_NODE) {
                				int child = ++count;
                				if(!createWidget(editor.item(j),child)){
                					count--;
                					continue;
                				}
                				//sw.println("if(wid"+child+" instanceof HasBlurHandlers)");
                					//sw.println("((HasBlurHandlers)wid"+child+").addBlurHandler(wid"+id+");");
                				sw.println("column"+id+"_"+h+"_"+i+".setColumnWidget(wid"+child+");");
                				break;
                			}
                		}
                		sw.println("column"+id+"_"+h+"_"+i+".controller = wid"+id+";");
                		sw.println("column"+id+"_"+h+"_"+i+".columnIndex = "+colIndex+";");
                		sw.println("cols"+id+"_"+h+".add(column"+id+"_"+h+"_"+i+");");
                	}
                	sw.println("wid"+id+".getColumns().put(\""+leafList.item(h).getAttributes().getNamedItem("key").getNodeValue()+"\", cols"+id+"_"+h+");");
                }
                sw.println("wid"+id+".init();");
                sw.println("wid"+id+".setStyleName(\"ScreenTree\");");
                setDefaults(node,"wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
                sw.println("wid"+id+".addBlurHandler(Util.focusHandler);");
                sw.println("wid"+id+".addFocusHandler(Util.focusHandler);");
                sw.println("panel.addFocusHandler(wid"+id+");");
				sw.println("wid"+id+".addFocusHandler(panel);");
                
                
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.tree.TreeWidget");
    			composer.addImport("org.openelis.gwt.widget.tree.TreeColumn");
    			composer.addImport("com.google.gwt.user.client.ui.HasAlignment");
    			composer.addImport("org.openelis.gwt.widget.tree.TreeView");
    		}
    	});
    	factoryMap.put("appButton", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("AppButton wid"+id+" = new AppButton();");
    			
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
			    
    			if(node.getAttributes().getNamedItem("action") != null) 
    				sw.println("wid"+id+".setAction(\""+node.getAttributes().getNamedItem("action").getNodeValue()+"\");");
    			
    	        if(node.getAttributes().getNamedItem("toggle") != null)
   	                sw.println("wid"+id+".setToggle("+node.getAttributes().getNamedItem("toggle").getNodeValue()+");");
    	        
    	        NodeList widgets = node.getChildNodes();
    	        for (int l = 0; l < widgets.getLength(); l++) {
    	            if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	            	if(!loadWidget(widgets.item(l),child)){
     	            	   count--;
     	            	   continue;
     	               }
    	                sw.println("wid"+id+".setWidget(wid"+child+");");
    	            }
    	        }

    	        setDefaults(node, "wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.AppButton");
    		}
    	});
    	factoryMap.put("icon", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("IconContainer wid"+id+"= new IconContainer();");
    			
				if(node.getAttributes().getNamedItem("tab") != null) {
		    		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
					String[] tabs = tab.split(",");
					String key = node.getAttributes().getNamedItem("key").getNodeValue();
					sw.println("wid"+id+".addTabHandler(new TabHandler(\""+tabs[0]+"\",\""+tabs[1]+"\",this,\""+key+"\"));");
				}
				if (node.getAttributes().getNamedItem("shortcut") != null)
					addShortcutHandler(node,"wid"+id);
    	        NodeList widgets = node.getChildNodes();
    	        for (int k = 0; k < widgets.getLength(); k++) {
    	            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
    	            	int child = ++count;
    	                if(!loadWidget(widgets.item(k),child)){
    	                	count--;
    	                	continue;
    	                }
    	                if(node.getAttributes().getNamedItem("align") != null)
    	                    sw.println("DOM.setElementProperty(wid"+id+".getElement(),\"align\","+node.getAttributes().getNamedItem("align").getNodeValue()+");");
    	                sw.println("wid"+id+".add(wid"+child+");");
    	            }
    	        }
    	        setDefaults(node,"wid"+id);
    	        if (node.getAttributes().getNamedItem("enable") != null){
    	        	sw.println("wid"+id+".enable("+node.getAttributes().getNamedItem("enable").getNodeValue()+");");
    	        }
    	        
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.IconContainer");
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
    	
    	factoryMap.put("percentBar", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			NodeList ranges;
    			Node range,attrib;
    			String width,barWidth;
    			
    			width = (attrib = node.getAttributes().getNamedItem("width")) != null ? withUnits(attrib) : null;
    			barWidth = (attrib = node.getAttributes().getNamedItem("barWidth")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("PercentBar wid"+id+" = new PercentBar();");
    			
    			if(width != null)
    				sw.println("wid"+id+".setWidth(\""+width+"\");");
    			
    			if(barWidth != null) {
    				sw.println("wid"+id+".setBarWidth("+barWidth+");");
    			}
    			
    			ranges = ((Element)node).getElementsByTagName("range");
    			if(ranges.getLength() > 0) {
    				sw.print("wid"+id+".setColors(");
    				for(int i = 0; i < ranges.getLength(); i++){
    					range = ranges.item(i);
    					if(i > 0)
    						sw.print(", ");
    					sw.print("new PercentBar.ColorRange("+range.getAttributes().getNamedItem("threshold").getNodeValue()+", \""+range.getAttributes().getNamedItem("color").getNodeValue()+"\")");
    				}
    				sw.println(");");
    			}
    		}
    		
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.PercentBar");
    		}
    	});
    	
    	factoryMap.put("tableImg", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("TableImage wid"+id+" = new TableImage();");
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.table.TableImage");
    		}
    	});
    	
    	factoryMap.put("String", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("StringField field"+id+" = new StringField();");
    			if(node.getAttributes().getNamedItem("max") != null) 
    				sw.println("field"+id+".setMax("+node.getAttributes().getNamedItem("max").getNodeValue()+");");
    			if(node.getAttributes().getNamedItem("min") != null)
    				sw.println("field"+id+".setMin("+node.getAttributes().getNamedItem("min").getNodeValue()+");");
    			if(node.getAttributes().getNamedItem("required") != null)
    				sw.println("field"+id+".required = "+node.getAttributes().getNamedItem("required").getNodeValue()+";");
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.StringField");
    		}
    	});
    	factoryMap.put("Date", new Factory() {
    		public void getNewInstance(Node node, int id){
    			sw.println("DateField field"+id+" = new DateField();");
    	        if (node.getAttributes().getNamedItem("required") != null)
    	            sw.println("field"+id+".required = "+node.getAttributes().getNamedItem("required").getNodeValue()+";");
    	        if (node.getAttributes().getNamedItem("begin") != null)
    	            sw.println("field"+id+".setBegin(Byte.parseByte(\""+node.getAttributes().getNamedItem("begin").getNodeValue()+"\"));");
    	        if (node.getAttributes().getNamedItem("end") != null)
    	            sw.println("field"+id+".setEnd(Byte.parseByte(\""+node.getAttributes().getNamedItem("end").getNodeValue()+"\"));");
    	        if (node.getAttributes().getNamedItem("maxValue") != null)
    	            sw.println("field"+id+".setMax("+node.getAttributes().getNamedItem("maxValue").getNodeValue()+");");
    	        if (node.getAttributes().getNamedItem("minValue") != null)
    	            sw.println("field"+id+".setMin("+node.getAttributes().getNamedItem("minValue").getNodeValue()+");");
    	        if (node.hasChildNodes()) {
    	            String deflt = node.getFirstChild().getNodeValue();
    	            Date dat = null;
    	            if (deflt.equals("current"))
    	                dat = new Date();
    	            else
    	                dat = new Date(deflt);
    	            sw.println("field"+id+".setValue(Datetime.getInstance(field"+id+".getBegin(),field"+id+".getEnd(),dat));");
    	        }
    	        if(node.getAttributes().getNamedItem("pattern") != null){
    	            sw.println("field"+id+".setFormat(\""+node.getAttributes().getNamedItem("pattern").getNodeValue()+"\");");
    	        }   
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.DateField");
    		}
    	});
    	factoryMap.put("Check", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			sw.println("CheckField field"+id+" = new CheckField();");
    	        if (node.getAttributes().getNamedItem("required") != null)
    	            sw.println("field"+id+".required = "+node.getAttributes().getNamedItem("required").getNodeValue()+";");
    	        if (node.hasChildNodes()) {
    	            sw.println("field"+id+".setValue(\""+node.getFirstChild().getNodeValue()+"\");");
    	        }
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.CheckField");
    		}
    	});
    	factoryMap.put("Integer", new Factory(){
    		public void getNewInstance(Node node, int id) {
    			sw.println("IntegerField field"+id+" = new IntegerField();");
    			if(node.getAttributes().getNamedItem("required") != null)
    				sw.println("field"+id+".required = "+node.getAttributes().getNamedItem("required").getNodeValue()+";");
    			if(node.getAttributes().getNamedItem("maxValue") != null)
    				sw.println("field"+id+".setMax(new Integer("+node.getAttributes().getNamedItem("maxValue").getNodeValue()+"));");
    			if(node.getAttributes().getNamedItem("minValue") != null)
    				sw.println("field"+id+".setMin(new Integer("+node.getAttributes().getNamedItem("minValue").getNodeValue()+"));");
    	        if (node.getAttributes().getNamedItem("pattern") != null) {
    	            sw.println("field"+id+".setFormat(\""+node.getAttributes().getNamedItem("pattern").getNodeValue()+"\");");
    	        }
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.IntegerField");
    		}
    	});
    	factoryMap.put("Long", new Factory(){
    		public void getNewInstance(Node node,int id) {
    			sw.println("LongField field"+id+" = new LongField();");
    			if(node.getAttributes().getNamedItem("required") != null)
    				sw.println("field"+id+".required = "+node.getAttributes().getNamedItem("required").getNodeValue()+";");
    			if(node.getAttributes().getNamedItem("maxValue") != null)
    				sw.println("field"+id+".setMax(new Long("+node.getAttributes().getNamedItem("maxValue").getNodeValue()+"));");
    			if(node.getAttributes().getNamedItem("minValue") != null)
    				sw.println("field"+id+".setMin(new Long("+node.getAttributes().getNamedItem("minValue").getNodeValue()+"));");
    	        if (node.getAttributes().getNamedItem("pattern") != null) {
    	            sw.println("field"+id+".setFormat(\""+node.getAttributes().getNamedItem("pattern").getNodeValue()+"\");");
    	        }
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.LongField");
    		}
    	});
    	factoryMap.put("Double", new Factory() {
    		public void getNewInstance(Node node, int id) {
    			StringBuffer sb = new StringBuffer();
    			sw.println("DoubleField field"+id+" = new DoubleField();");
    	        if (node.getAttributes().getNamedItem("required") != null)
    	        	sw.println("field"+id+".required = "+node.getAttributes().getNamedItem("required").getNodeValue()+";");
    	        if (node.getAttributes().getNamedItem("maxValue") != null)
    	            sw.println("field"+id+".setMax(new Double("+node.getAttributes().getNamedItem("maxValue").getNodeValue()+"));");
    	        if (node.getAttributes().getNamedItem("minValue") != null)
    	            sw.println("field"+id+".setMin(new Double("+node.getAttributes().getNamedItem("minValue").getNodeValue()+"));");
    	        if (node.getAttributes().getNamedItem("pattern") != null) {
    	            sw.println("field"+id+".setFormat(\""+node.getAttributes().getNamedItem("pattern").getNodeValue()+"\");");
    	        }
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.DoubleField");
    		}
    	});
    	
    	factoryMap.put("linkButton", new Factory() {    		
    		public void getNewInstance(Node node, int id) {
    			String icon,label,width,height,popup,style;
    			Node attrib;
    			
    			icon = (attrib = node.getAttributes().getNamedItem("icon")) != null ? attrib.getNodeValue() : "";
    			label = (attrib = node.getAttributes().getNamedItem("label")) != null ? attrib.getNodeValue() : "";
    			width= (attrib = node.getAttributes().getNamedItem("width")) != null ? attrib.getNodeValue() : null;
    			height = (attrib = node.getAttributes().getNamedItem("height")) != null ? attrib.getNodeValue() : null;
    			popup = (attrib = node.getAttributes().getNamedItem("popup")) != null ? attrib.getNodeValue() : "";
    			style = (attrib = node.getAttributes().getNamedItem("style")) != null ? attrib.getNodeValue() : null;
    			
    			sw.println("org.openelis.gwt.widget.web.LinkButton wid"+id+" = new org.openelis.gwt.widget.web.LinkButton(\""+icon+"\",\""+label+"\",\""+popup+"\","+width+","+height+");");
    			
    			if(style != null)
    				sw.println("wid"+id+".setStyleName(\""+style+"\");");
    		}
    		public void addImport() {
    			composer.addImport("org.openelis.gwt.widget.web.LinkButton");
    		}
    	});
    }
    
    
    private String withUnits(Node node) {
        String val;
        
        val = node.getNodeValue();
        
        if(val == null)
            return null;
        
        try {
            Integer.parseInt(val);
        }catch(Exception e) {
            return val;
        }
        
        return val+"px";
                        
    }


    
    
}
