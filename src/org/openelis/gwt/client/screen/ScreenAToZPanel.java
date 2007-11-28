package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class ScreenAToZPanel extends ScreenWidget implements ClickListener{
	
	private HorizontalPanel mainHP = new HorizontalPanel();
	private HorizontalPanel hideablePanel = new HorizontalPanel();
	private VerticalPanel middleBar = new VerticalPanel();
	public HTML arrowButton = new HTML();

	
	/**
	 * Default Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "aToZ";
	/**
	 * Widget wrapped by this class
	 */

	public ScreenAToZPanel() {
    }

    public ScreenAToZPanel(Node node, ScreenBase screen) {
        super(node);
//      need to get the buttonPanel node
		final ScreenBase finalScreen = screen;
		DeferredCommand.addCommand(new Command() {
            public void execute() {
            	middleBar.setHeight(String.valueOf(finalScreen.getOffsetHeight()-20)+"px");
            }
        });
		
		//need to set the alignment before adding any widgets or it wont work
		middleBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		middleBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		        
		//set the height of table
		if (node.getAttributes().getNamedItem("height") != null) {
			mainHP.setHeight(node.getAttributes().getNamedItem("height").getNodeValue());
		}
		//set the width of table
		if (node.getAttributes().getNamedItem("width") != null) {
			mainHP.setWidth(node.getAttributes().getNamedItem("width").getNodeValue()); 
		}
		
		//set the click listener
		if (node.getAttributes().getNamedItem("onclick") != null) {
        	String listener = node.getAttributes().getNamedItem("onclick").getNodeValue();
        	if(listener.equals("this"))
        		arrowButton.addClickListener(screen);
        	else
        		arrowButton.addClickListener((ClickListener)ScreenBase.getWidgetMap().get(listener));
		}
		
		//make the hideable panel hide if visable="false"
		if (node.getAttributes().getNamedItem("visible") != null && 
				node.getAttributes().getNamedItem("visible").getNodeValue() == "false") {
		//build the arrow button
		arrowButton.setHTML("<img src=\"Images/arrow-right-unselected.png\" onmouseover=\"this.src='Images/arrow-right-selected.png';\" " +
				" onmouseout=\"this.src='Images/arrow-right-unselected.png';\" style=\"vertical-align:middle;\">");
		hideablePanel.setVisible(false);
		
		}else{
			arrowButton.setHTML("<img src=\"Images/arrow-left-unselected.png\" onmouseover=\"this.src='Images/arrow-left-selected.png';\" " +
				" onmouseout=\"this.src='Images/arrow-left-unselected.png';\" style=\"vertical-align:middle;\">");			
		}

		arrowButton.addClickListener(this);
		
		//add arrow button to middle panel
		NodeList widgets = node.getChildNodes();
	    for (int k = 0; k < widgets.getLength(); k++) {
	    	if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
	    		Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
	            hideablePanel.add(wid);
	        }
	    }
			
		middleBar.setStyleName("LeftMenuPanePanel");
		middleBar.add(arrowButton);
		
		mainHP.setSpacing(0);
		
		mainHP.add(hideablePanel);
		mainHP.add(middleBar);
		
		initWidget(mainHP);		
        setDefaults(node, screen);
    }
    
    public void setDefaults(Node node, ScreenBase screen) {
        super.setDefaults(node, screen);
        
        getWidget().setWidth("100%");
        getWidget().setHeight("100%");
        
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenAToZPanel(node, screen);
    }

	public void onClick(Widget sender) {
		if(sender == arrowButton){
			if(hideablePanel.isVisible()){
        		hideablePanel.setVisible(false);
        		arrowButton.setHTML("<img src=\"Images/arrow-right-unselected.png\" onmouseover=\"this.src='Images/arrow-right-selected.png';\" onmouseout=\"this.src='Images/arrow-right-unselected.png';\">");
        	}else{
        		hideablePanel.setVisible(true);
        		arrowButton.setHTML("<img src=\"Images/arrow-left-unselected.png\" onmouseover=\"this.src='Images/arrow-left-selected.png';\" onmouseout=\"this.src='Images/arrow-left-unselected.png';\">");
        	}	
		}		
	}
}
