package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.AbstractField;
/**
 * ScreenLabel wraps a GWT Label for display on a Screen.
 * @author tschmidt
 *
 */
public class ScreenLabel extends ScreenWidget implements SourcesClickEvents{

    private DelegatingClickListenerCollection clickListeners;
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "label";
	/**
	 * Widget wrapped by this class
	 */
    public Label label;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenLabel() {
    }

    public ScreenLabel(String text, Object value) {
        label = new Label(text);
        setUserObject(value);
        initWidget(label);
        label.setStyleName("ScreenLabel");
        
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;label key="string" wordwrap="boolean" text="string" onclick="string"/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenLabel(Node node, Screen screen) {
        super(node);
        label = new Label();
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
        if(node.getAttributes().getNamedItem("onClick") != null){
            String listener = node.getAttributes().getNamedItem("onClick").getNodeValue();
            if(listener.equals("this"))
                addClickListener(screen);
            else
                addClickListener((ClickListener)Screen.getWidgetMap().get(listener));
        }
        initWidget(label);
        label.setStyleName("ScreenLabel");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenLabel(node, screen);
    }

    public void load(AbstractField field) {
        label.setText(field.toString());
    }
    
    
    public ScreenLabel getDropInstance(){
        ScreenLabel slabel = new ScreenLabel();
        slabel.setDropTargets(getDropTargets());
        slabel.label = new Label(label.getText());
        slabel.label.setWordWrap(label.getWordWrap());
        slabel.initWidget(slabel.label);
        slabel.setUserObject(getUserObject());
        return slabel;
    }

    public void addClickListener(ClickListener arg0) {
       if(clickListeners == null)
           clickListeners = new DelegatingClickListenerCollection(this,label);
       if(clickListeners.contains(arg0))
           return;
       clickListeners.add(arg0);
        
    }

    public void removeClickListener(ClickListener arg0) {
        if(clickListeners != null)
            clickListeners.remove(arg0);
    }
  
}
