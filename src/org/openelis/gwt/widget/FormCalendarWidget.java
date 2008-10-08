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

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.DatetimeRPC;
import org.openelis.gwt.screen.ScreenWidget;

import java.util.Date;

/**
 * FormCalendarWidget is used to tie a CalendarWidget to a specific field in a
 * form.
 * 
 * @author tschmidt
 * 
 */
public class FormCalendarWidget extends Composite implements
                                                 ClickListener,
                                                 KeyboardListener,
                                                 FocusListener,
                                                 MouseListener,
                                                 ChangeListener,
                                                 SourcesChangeEvents {
    public TextBox textbox = new TextBox();

    protected FocusPanel calendarImage = new FocusPanel();
    protected Image calendar = new Image("Images/1day.png");
    protected HorizontalPanel mainHp = new HorizontalPanel();
    protected byte begin;
    protected byte end;
    protected boolean week;
    protected Date weekDate;
    protected ChangeListenerCollection changeListeners;
    protected PopupPanel pop;

    public FormCalendarWidget() {
    }

    public FormCalendarWidget(byte begin, byte end, boolean week) {
        this.begin = begin;
        this.end = end;
        this.week = week;
    }
    
    public void init() {
        textbox.addKeyboardListener(this);
        textbox.addFocusListener(this);
        textbox.setStyleName("TextboxUnselected");

        mainHp.setSpacing(0);
        mainHp.addStyleName("Calendar");

        calendarImage.addMouseListener(this);
        calendarImage.addClickListener(this);
        calendarImage.setStyleName("CalendarButton");

        mainHp.add(textbox);
        mainHp.add(calendarImage);
        initWidget(mainHp);
    }

    public void setText(String date) {
        textbox.setText(date);
    }

    public void setDate(DatetimeRPC date) {
        if (week) {
            date.add(-(date.getDate().getDay()));
            DatetimeRPC endDate = DatetimeRPC.getInstance(DatetimeRPC.YEAR,
                                                          DatetimeRPC.DAY,
                                                          date.getDate());
            endDate.add(6);
            String from = DateTimeFormat.getFormat("EEEE MMM d, yyyy")
                                        .format(date.getDate());
            String to = DateTimeFormat.getFormat("EEEE MMM d, yyyy")
                                      .format(endDate.getDate());
            setText(from + " - " + to);
            weekDate = date.getDate();
        } else
            setText(date.toString());
    }

    public String getText() {
        return textbox.getText();
    }

    public void setWidth(String width) {
        textbox.setWidth(width);
    }

    public void getHeight(String height) {
        textbox.setHeight(height);
    }

    public void setFocus(boolean focus) {
        textbox.setFocus(focus);
    }

    public void onClick(Widget sender) {
        // we need to set the selected style name to the textbox
        textbox.addStyleName("TextboxSelected");
        textbox.removeStyleName("TextboxUnselected");
        textbox.setFocus(true);
        // textBox.setText("");
        calendarImage.addStyleName("Selected");

        doCalendar(sender, begin, end);
    }

    public void setEnabled(boolean enabled) {
        // textbox.setEnabled(enabled);
        calendarImage.removeClickListener(this);
        if (enabled == false)
            calendarImage.removeClickListener(this);
        else
            calendarImage.addClickListener(this);
        textbox.setReadOnly(!enabled);
    }

    public void addFocusListener(FocusListener listener) {
        textbox.removeFocusListener(listener);
        textbox.addFocusListener(listener);
    }

    public void removeFocusListener(FocusListener listener) {
        textbox.removeFocusListener(listener);
    }

    public void addKeyboardListener(KeyboardListener listener) {
        textbox.addKeyboardListener(listener);
    }

    public void onKeyUp(Widget sender, char key, int modifier) {
        if (KeyboardListener.KEY_ENTER == (int)key) {
            ((TextBox)sender).setFocus(false);
            doCalendar(sender, begin, end);
        }
    }

    public void onKeyPress(Widget sender, char key, int modifier) {
    }

    public void onKeyDown(Widget sender, char key, int modifier) {
    }

    public void setShortcutKey(char key) {
        textbox.setAccessKey(key);
    }

    protected void doCalendar(Widget sender, final byte begin, final byte end) {
        CalendarWidget cal = new CalendarWidget(textbox.getText());
        cal.addChangeListener(this);
        pop = new PopupPanel(true, false);
        pop.setWidth("150px");
        pop.setWidget(cal);
        pop.setPopupPosition(textbox.getAbsoluteLeft(),
                             textbox.getAbsoluteTop() + textbox.getOffsetHeight());
        pop.show();
    }

    public void removeChangeListener(ChangeListener listener) {
    }

    public void clear() {
    }

    public Object getValue() {
        if (getText().equals(""))
            return null;
        Date date = new Date(getText().replaceAll("-", "/"));
        return DatetimeRPC.getInstance(begin, end, date);
    }

    public void setValue(Object val) {
        if (val != null)
            setText(((DatetimeRPC)val).toString());
        else
            setText("");
    }

    public Object getDisplay(String title) {
        Label tl = new Label();
        tl.setText(getText());
        tl.setWordWrap(false);
        if (title != null)
            tl.setTitle(title);
        return tl;
    }

    public Widget getEditor() {
        return this;
    }

    public Date getWeekDate() {
        return weekDate;
    }

    public void onFocus(Widget sender) {
        if (!textbox.isReadOnly()) {
            if (sender == textbox) {
                // we need to set the selected style name to the textbox
                textbox.addStyleName("TextboxSelected");
                textbox.removeStyleName("TextboxUnselected");
                textbox.setFocus(true);
                // textBox.setText("");
                calendarImage.addStyleName("Selected");
            }
        }
    }

    public void onLostFocus(Widget sender) {
        if (!textbox.isReadOnly()) {
            if (sender == textbox) {
                // we need to set the unselected style name to the textbox
                textbox.addStyleName("TextboxUnselected");
                textbox.removeStyleName("TextboxSelected");

                calendarImage.removeStyleName("Selected");

            }
        }
    }

    public void onMouseDown(Widget sender, int x, int y) {
        if (!textbox.isReadOnly()) {
            if (sender == calendarImage) {
                calendarImage.addStyleName("Pressed");
            }
        }
    }

    public void onMouseEnter(Widget sender) {
        if (!textbox.isReadOnly()) {
            if (sender == calendarImage) {
                calendarImage.addStyleName("Hover");
            }
        }
    }

    public void onMouseLeave(Widget sender) {
        if (!textbox.isReadOnly()) {
            if (sender == calendarImage) {
                calendarImage.removeStyleName("Hover");
            }
        }
    }

    public void onMouseMove(Widget sender, int x, int y) {
    }

    public void onMouseUp(Widget sender, int x, int y) {
        if (!textbox.isReadOnly()) {
            if (sender == calendarImage) {
                calendarImage.removeStyleName("Pressed");
            }
        }
    }

    public void onChange(Widget sender) {
        pop.hide();
        Date date = new Date((String)((ScreenWidget)sender).getUserObject());
        setDate(DatetimeRPC.getInstance(begin, end, date));
        
        if (changeListeners != null){
            changeListeners.fireChange(this);
        }
    }

    public void addChangeListener(ChangeListener listener) {
        if (changeListeners == null)
            changeListeners = new ChangeListenerCollection();
        changeListeners.add(listener);
    }
}
