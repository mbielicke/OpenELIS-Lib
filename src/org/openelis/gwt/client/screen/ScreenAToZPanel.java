package org.openelis.gwt.client.screen;

import org.openelis.gwt.client.widget.AToZPanel;

import com.google.gwt.xml.client.Node;

public class ScreenAToZPanel extends ScreenWidget{
	
	/*private AToZPanel aToZ;

	//blank for the widget map
	public ScreenAToZPanel(){
		
	}
	
	public ScreenAToZPanel(Node node, Screen screen){
		super(node);
		aToZ = new AToZPanel();
		
		initWidget(aToZ);
		setDefaults(node, screen);
	}
	
	 public ScreenWidget getInstance(Node node, Screen screen) {
	        // TODO Auto-generated method stub
	        return new ScreenAToZPanel(node, screen);
	   }*/
	
	/**
	 * Default Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "aToZ";
	/**
	 * Widget wrapped by this class
	 */
	private AToZPanel test;

    public ScreenAToZPanel() {
    }

    public ScreenAToZPanel(Node node, ScreenBase screen) {
        super(node);
        test = null;
        //if (node.getAttributes().getNamedItem("buttons") != null)
        	test = new AToZPanel(node,screen);
        //else
        //	test = new AToZPanel();
        initWidget(test);
      //  bPanel.setStyleName("ScreenButtonPanel");
        setDefaults(node, screen);
    }
    
    public void setDefaults(Node node, ScreenBase screen) {
        this.screen = screen;
        
        if(node.getAttributes().getNamedItem("key") != null)
        	screen.widgets.put(node.getAttributes().getNamedItem("key").getNodeValue(), this);
        
        getWidget().setWidth("100%");
        getWidget().setHeight("100%");
        
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenAToZPanel(node, screen);
    }
}
