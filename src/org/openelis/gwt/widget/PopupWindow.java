/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.screen.ScreenImage;
/**
 * PopupWindow is used to display content in a Modal window in applications.
 * @author tschmidt
 *
 */
@Deprecated
public class PopupWindow extends PopupPanel {
    private DockPanel inner = new DockPanel();
    private DragListener dragListener = new DragListener();
    private FocusPanel caption = new FocusPanel();
    private Label captionText = new Label();
    private HorizontalPanel n = new HorizontalPanel();
    public ScrollPanel content = new ScrollPanel();
    private ScreenImage close = new ScreenImage("Images/close.png");
    private WindListener windListener = new WindListener();

    public PopupWindow(String text) {
        super(false,true);
        captionText.setText(text);
        captionText.setStyleName("ScreenWindowLabel");
        n.add(captionText);
        n.add(close);
        n.setCellHorizontalAlignment(close, HasAlignment.ALIGN_RIGHT);
        n.setWidth("100%");
        close.getWidget().setStyleName("CloseButton");
        close.addClickListener(windListener);
        caption.setStyleName("Caption");
        caption.addMouseListener(dragListener);
        caption.add(n);
        inner.add(caption, DockPanel.NORTH);
        inner.add(content, DockPanel.CENTER);
        inner.setStyleName("WindowPanel");
        add(inner);
        addStyleName("WindowPanel");
    }

    public void setContentPanel(Widget widget) {
        // content.add(widget);
        show();
    }

    public void setText(String text) {
        captionText.setText(text);
    }

    private class DragListener extends MouseListenerAdapter {
        private boolean dragging;
        private int dragStartX;
        private int dragStartY;

        public void onMouseDown(Widget sender, int x, int y) {
            dragging = true;
            dragStartX = x;
            DOM.setCapture(caption.getElement());
            dragStartY = y;
        }

        public void onMouseMove(Widget sender, int x, int y) {
            if (dragging) {
                int absX = x + sender.getAbsoluteLeft();
                int absY = y + sender.getAbsoluteTop();
                setPopupPosition(absX - dragStartX, absY - dragStartY);
            }
        }

        public void onMouseUp(Widget sender, int x, int y) {
            dragging = false;
            DOM.releaseCapture(caption.getElement());
        }
    }

    private class WindListener implements ClickListener {
        public void onClick(Widget sender) {
            if (sender == close)
                close();
        }
    }

    public void close() {
        hide();
    }

    public void size() {
        // TODO Auto-generated method stub
        if (this.content.getOffsetHeight() > (Window.getClientHeight() - 100))
            this.content.setHeight((Window.getClientHeight() - (this.getAbsoluteTop() + 50)) + "px");
    }

    public void size(String width, String height) {
        if (width != null) {
            this.content.setWidth(width);
        }
        if (height != null) {
            this.content.setHeight(height);
        }
    }

    public void position() {
        center();
    }

    public boolean onKeyUpPreview(char key, int modifier) {
        if (KeyboardListener.KEY_ESCAPE == (int)key) {
            close();
        }
        return true;
    }

    public boolean onKeyDownPreview(char key, int mod) {
        return true;
    }

}
