package org.openelis.gwt.client.widget;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Class is deprecated should be deleted
 * @author tschmidt
 *
 */
public class MenuTree extends Composite {

    public Tree menu = new Tree();
    private VerticalPanel vp = new VerticalPanel();
    private ScrollPanel scroll = new ScrollPanel();

    public MenuTree(boolean size) {
        vp.add(menu);
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

    private void setBrowserHeight() {
        if (menu.isVisible()) {
            menu.setHeight((Window.getClientHeight() - menu.getAbsoluteTop() -10) + "px");
        }
    }

}
