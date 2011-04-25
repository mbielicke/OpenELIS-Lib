/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.widget;

import org.openelis.gwt.common.Util;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditBox extends TextBox<String> {

    /**
     * Used for EditBox display
     */
    protected HorizontalPanel hp;
    protected Button          button;
    protected PopupPanel      popup;
    protected TextArea        ta;
    
    /**
     * Default no-arg constructor
     */
    public EditBox() {
    }

    public void init() {

        final EditBox source = this;

        /*
         * Final instance of the private class KeyboardHandler
         */
        final KeyboardHandler keyHandler = new KeyboardHandler();

        hp = new HorizontalPanel();
        textbox = new com.google.gwt.user.client.ui.TextBox();

        button = new Button();
        AbsolutePanel image = new AbsolutePanel();
        image.setStyleName("DotsButton");
        button.setDisplay(image, false);

        hp.add(textbox);
        hp.add(button);

        initWidget(hp);

        textbox.setStyleName("TextboxUnselected");

        textbox.addFocusHandler(new FocusHandler() {
            public void onFocus(FocusEvent event) {
                FocusEvent.fireNativeEvent(event.getNativeEvent(), source);
            }
        });

        textbox.addBlurHandler(new BlurHandler() {
            public void onBlur(BlurEvent event) {
                BlurEvent.fireNativeEvent(event.getNativeEvent(), source);
            }
        });

        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                showPopup();
            }
        });

        addHandler(keyHandler, KeyDownEvent.getType());
    }

    private void showPopup() {
        if (popup == null) {
            popup = new PopupPanel(true);
            popup.setPreviewingAllNativeEvents(false);
            popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                public void onClose(CloseEvent<PopupPanel> event) {
                    if (event.isAutoClosed()) {
                        setValue(ta.getText(), true);
                    }
                }
            });
            VerticalPanel vp = new VerticalPanel();

            ta = new TextArea();
            ta.setStyleName("ScreenTextArea");
            
            Button ok = new Button();
            Label<String> okText = new Label<String>("OK");
            okText.setStyleName("ScreenLabel");
            ok.setDisplay(okText);
            ok.setStyleName("Button");
            
            Button cancel = new Button();
            Label<String> cancelText = new Label<String>("Cancel");
            cancelText.setStyleName("ScreenLabel");
            cancel.setDisplay(cancelText);
            cancel.setStyleName("Button");

            ok.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    textbox.setText(ta.getText());
                    popup.hide();
                }
            });

            cancel.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    popup.hide();
                }
            });
            
            vp.add(ta);

            HorizontalPanel hp = new HorizontalPanel();
            hp.add(ok);
            hp.add(cancel);

            vp.add(hp);
            vp.setCellHorizontalAlignment(hp, HasAlignment.ALIGN_CENTER);
            vp.setStyleName("BlueContentPanel");
            

            popup.setWidget(vp);
            popup.setStyleName("DropdownPopup");

        }
        ta.setText(getText());
        popup.showRelativeTo(this);
        ta.setFocus(true);

    }

    public void setWidth(String width) {
        /*
         * Set the outer panel to full width;
         */
        if (hp != null)
            hp.setWidth(width);

        /*
         * set the Textbox to width - 16 to account for button.
         */
        textbox.setWidth( (Util.stripUnits(width) - 16) + "px");
    }


    public void setEnabled(boolean enabled) {
        if (isEnabled() == enabled)
            return;
        button.setEnabled(enabled);
        if (enabled)
            sinkEvents(Event.ONKEYDOWN);
        else
            unsinkEvents(Event.ONKEYDOWN);
        super.setEnabled(enabled);
    }

    // ********** Table Keyboard Handling ****************************

    protected class KeyboardHandler implements KeyDownHandler {
        /**
         * This method handles all key down events for this table
         */
        public void onKeyDown(KeyDownEvent event) {

            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_TAB:
                    if (popup != null && popup.isShowing())
                        popup.hide();
                    event.stopPropagation();
                    break;
                case KeyCodes.KEY_ENTER:
                    if (popup == null || !popup.isShowing())
                        showPopup();
                    else
                        popup.hide();
                    event.stopPropagation();
                    break;
            }
        }
    }
}
