package org.openelis.gwt.screen;

import org.openelis.gwt.common.data.AbstractField;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class ScreenMenuBarComplex extends ScreenWidget {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "menubarcomplex";
	/**
	 * Widget wrapped by this class
	 */
	private ScreenHorizontal horizontalMenuPanel;
	
	//private MenuCommands mcs;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
	public ScreenMenuBarComplex() {
		
	}
	
	public ScreenMenuBarComplex(Node node, ScreenBase screen) {
		super(node);
		this.screen = screen;
		String key = "menuBarKey";
		
		if(node.getAttributes().getNamedItem("key") != null)
			key = node.getAttributes().getNamedItem("key").getNodeValue();

		if(node.getAttributes().getNamedItem("vertical") != null){
			//if(node.getAttributes().getNamedItem("vertical").getNodeValue().equals("true"))
			//	menuPopupPanel = new MenuBar(true);
			//else 
			//	menuPopupPanel = new MenuBar();
		}
		if(node.getAttributes().getNamedItem("autoHide") != null){
			if(node.getAttributes().getNamedItem("autoHide").getNodeValue().equals("true")){
				horizontalMenuPanel = new ScreenHorizontal(screen,key);
			}else{
				horizontalMenuPanel = new ScreenHorizontal(screen,key);
			}
		}
		//mcs = (MenuCommands)Screen.getWidgetMap().get(node.getAttributes().getNamedItem("commands").getNodeValue());
		createMenu(node,horizontalMenuPanel);
		initWidget(horizontalMenuPanel);
		setDefaults(node, screen);
	}

	public ScreenWidget getInstance(Node node, ScreenBase screen){
		return new ScreenMenuBarComplex(node,screen);
	}
	
    public void load(AbstractField field) {
        Document doc = XMLParser.parse((String)field.getValue());
        createMenu(doc.getDocumentElement(),horizontalMenuPanel);
    }

    public void load(String xml) {
        Document doc = XMLParser.parse(xml);
        createMenu(doc.getDocumentElement(),horizontalMenuPanel);
    }
    
    private void createMenu(Node node, ScreenHorizontal horizontalMenu){
    	NodeList items = node.getChildNodes();
        for (int i = 0; i < items.getLength(); i++) {
            if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
            	if(items.item(i).hasChildNodes()){
            		ScreenMenuPopupPanel subMenu = null;
            		//if(items.item(i).getAttributes().getNamedItem("vertical") != null){
            		//	if(items.item(i).getAttributes().getNamedItem("vertical").getNodeValue().equals("true"))
            		//		subMenu = new PopupPanel(true);
            		//	else
            		//		subMenu = new MenuBar();
            		//}
            		if(items.item(i).getAttributes().getNamedItem("autoHide") != null){
            			if(items.item(i).getAttributes().getNamedItem("autoHide").getNodeValue().equals("true")){
            				subMenu = new ScreenMenuPopupPanel(false,items.item(i).getAttributes().getNamedItem("text").getNodeValue()+"Panel",screen);
            			}else{
            				subMenu = new ScreenMenuPopupPanel(false,items.item(i).getAttributes().getNamedItem("text").getNodeValue()+"Panel",screen);
            			}
            		}
            		
            		//use the createMenu that create a new popupPanel
            		createMenu(items.item(i),subMenu);
            		
            		//add the label to the top horizontal panel
            		ScreenLabel horizontalMenuItem = new ScreenLabel(screen,items.item(i).getAttributes().getNamedItem("text").getNodeValue());
            		FocusPanel spacer = new FocusPanel();
            		spacer.setStyleName("TopMenuSpacer");
            		horizontalMenuItem.addClickListener(screen);
            		((Label)horizontalMenuItem.getWidget()).setText(items.item(i).getAttributes().getNamedItem("text").getNodeValue());
            		if(((HorizontalPanel)horizontalMenu.getWidget()).getWidgetCount() > 0)
            			((HorizontalPanel)horizontalMenu.getWidget()).add(spacer);
            		
            		((HorizontalPanel)horizontalMenu.getWidget()).add(horizontalMenuItem);
            
            	}else{
            		//add the label to the top horizontal panel
            		ScreenLabel horizontalMenuItem = new ScreenLabel(screen,items.item(i).getAttributes().getNamedItem("text").getNodeValue());
            		horizontalMenuItem.addClickListener(screen);
            		((Label)horizontalMenuItem.getWidget()).setText(items.item(i).getAttributes().getNamedItem("text").getNodeValue());          		
            		((HorizontalPanel)horizontalMenu.getWidget()).add(horizontalMenuItem);
            	}
            }
        }
    }
    
    private void createMenu(Node node, ScreenMenuPopupPanel verticalMenu){
    	NodeList items = node.getChildNodes();
    	VerticalPanel vp = new VerticalPanel();
    	((PopupPanel)verticalMenu.getWidget()).add(vp);
        for (int i = 0; i < items.getLength(); i++) {
            if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
            	if(items.item(i).hasChildNodes()){
            		ScreenMenuPopupPanel subMenu = null;
            		//if(items.item(i).getAttributes().getNamedItem("vertical") != null){
            		//	if(items.item(i).getAttributes().getNamedItem("vertical").getNodeValue().equals("true"))
            		//		subMenu = new PopupPanel(true);
            		//	else
            		//		subMenu = new MenuBar();
            		//}
            		if(items.item(i).getAttributes().getNamedItem("autoHide") != null){
            			if(items.item(i).getAttributes().getNamedItem("autoHide").getNodeValue().equals("true")){
            				//TODO place the menu on the page
            				subMenu = new ScreenMenuPopupPanel(false,items.item(i).getAttributes().getNamedItem("text").getNodeValue()+"Panel",screen);
            				((PopupPanel)subMenu.getWidget()).setPopupPosition(20,40);
            			}else{
            				subMenu = new ScreenMenuPopupPanel(false,items.item(i).getAttributes().getNamedItem("text").getNodeValue()+"Panel",screen);
            			}
            		}
            		
            		Node item = items.item(i);
            		String image = null;
            		if(item.getAttributes().getNamedItem("image") != null)
            			image = item.getAttributes().getNamedItem("image").getNodeValue();
        
            		String title = null;
            		if(item.getAttributes().getNamedItem("text") != null)
            			title = item.getAttributes().getNamedItem("text").getNodeValue();
            		
            		String desc = null;
            		if(item.getAttributes().getNamedItem("description") != null)
            			desc = item.getAttributes().getNamedItem("description").getNodeValue();
            		
            		boolean children = item.hasChildNodes();
            		createMenu(items.item(i),subMenu);
            		
            		vp.add(createMenuItemPanel(image,title,desc,children));

            	}else{
            		Node item = items.item(i);
            		String image = null;
            		if(item.getAttributes().getNamedItem("image") != null)
            			image = item.getAttributes().getNamedItem("image").getNodeValue();
        
            		String title = null;
            		if(item.getAttributes().getNamedItem("text") != null)
            			title = item.getAttributes().getNamedItem("text").getNodeValue();
            		
            		String desc = null;
            		if(item.getAttributes().getNamedItem("description") != null)
            			desc = item.getAttributes().getNamedItem("description").getNodeValue();
            		
            		boolean children = item.hasChildNodes();
            		
            		vp.add(createMenuItemPanel(image,title,desc,children));
            	}
            }
        }
    }
    
    public ScreenHorizontal createMenuItemPanel(String image, String title, String description, boolean childMenu){
    	ScreenHorizontal mainPanel = new ScreenHorizontal(screen,"");
    	ScreenVertical titleDescriptionPanel = new ScreenVertical(screen,"");
    	
    	Image iconImage = new Image();
    	
    	Image arrowImage = new Image();
    	arrowImage.setUrl("");
    	
    	ScreenLabel itemTitle = new ScreenLabel(screen,title);
    	((Label)itemTitle.getWidget()).setText(title);
    	itemTitle.addClickListener(screen);
//    	itemTitle.addStyleName("");
    	
    	Label itemDescription = new Label();
    	if(description != null)
    	itemDescription.setText(description);
    	
 //   	itemDescription.addStyleName("");
    	
    	((VerticalPanel)titleDescriptionPanel.getWidget()).add(itemTitle);
    	((VerticalPanel)titleDescriptionPanel.getWidget()).add(itemDescription);
    	
    	if(image != null){
    		iconImage.setUrl(image);
        	((HorizontalPanel)mainPanel.getWidget()).add(iconImage);
    	}
    
    	((HorizontalPanel)mainPanel.getWidget()).add(titleDescriptionPanel);
    	
    	if(childMenu)
    		((HorizontalPanel)mainPanel.getWidget()).add(arrowImage);
    	
    	return mainPanel;
    }
    
    public void destroy() {
    	horizontalMenuPanel = null;
        //mcs = null;
        super.destroy();
    }
}
