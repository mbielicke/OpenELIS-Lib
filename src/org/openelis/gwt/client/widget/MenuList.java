package org.openelis.gwt.client.widget;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.openelis.gwt.client.screen.ScreenLabel;
/**
 * Class is deprecated should be deleted
 * @author tschmidt
 *
 */
public class MenuList extends Composite{
    
    private VerticalPanel vp = new VerticalPanel();
    private ScrollPanel scroll = new ScrollPanel();
    
    public MenuList(boolean size){
        if (size) {
            scroll.add(vp);
            initWidget(scroll);
            Window.addWindowResizeListener(new WindowResizeListener() {
                public void onWindowResized(int width, int height) {
                    setBrowserHeight();
                }
            });
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    setBrowserHeight();
                }
            });
        }else{
            initWidget(vp);
        }
    }
    
    public void add(String text, String cat){
        Label label = new Label(text);
        label.addStyleName(cat);
        vp.add(label);
    }
    
    public void add(ScreenLabel label){
        vp.add(label);
    }
   
    private void setBrowserHeight() {
        if (isVisible()) {
            setHeight((Window.getClientHeight() - getAbsoluteTop() -10) + "px");
        }
    }
    
    public void clear() {
        vp.clear();
    }

}
