package org.openelis.gwt.client.widget;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.openelis.gwt.client.screen.ScreenBase;
import org.openelis.gwt.common.DatetimeRPC;
import java.util.Date;
/**
 * FormCalendarWidget is used to tie a CalendarWidget to a specific 
 * field in a form.
 * @author tschmidt
 *
 */
public class FormCalendarWidget extends Composite implements
                                                 ClickListener,
                                                 KeyboardListener,
                                                 SourcesChangeEvents {
    protected TextBox textbox = new TextBox() {
        public void onBrowserEvent(Event event) {
            if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB)
                    screen.doTab(event, comp);
            } else {
                super.onBrowserEvent(event);
            }
        }
    };
    protected Image calendar = new Image("Images/1day.png");
    protected HorizontalPanel hp = new HorizontalPanel();
    protected byte begin;
    protected byte end;
    private ScreenBase screen;
    protected Widget comp;
    protected boolean week;
    protected Date weekDate;
    protected ChangeListenerCollection changeListeners = new ChangeListenerCollection();

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public FormCalendarWidget() {
    }

    public FormCalendarWidget(byte begin, byte end, boolean week) {
        this.begin = begin;
        this.end = end;
        this.week = week;
        textbox.addKeyboardListener(this);
        hp.add(textbox);
        hp.add(calendar);
        comp = this;
        initWidget(hp);
        calendar.setStyleName("ScreenCalendar-button");
        textbox.setStyleName("ScreenCalendar-box");
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
            textbox.setText(from + " - " + to);
            weekDate = date.getDate();
        } else
            textbox.setText(date.toString());
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
        doCalendar(sender, begin, end);
    }

    public void setEnabled(boolean enabled) {
        //textbox.setEnabled(enabled);
        calendar.removeClickListener(this);
        if (enabled == false)
            calendar.removeClickListener(this);
        else
            calendar.addClickListener(this);
        textbox.setReadOnly(!enabled);
    }

    public void addFocusListener(FocusListener listener) {
        textbox.removeFocusListener(listener);
        textbox.addFocusListener(listener);
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
        final CalendarWidget calendar = new CalendarWidget(week);
        calendar.window.setPopupPosition(sender.getAbsoluteLeft(),
                                         sender.getAbsoluteTop());
        calendar.setCurrentTime();
        calendar.window.show();
        final Widget change = this;
        calendar.addChangeListener(new ChangeListener() {
            public void onChange(Widget sender) {
                Date newDate = calendar.getDate();
                newDate.setHours(calendar.getHour());
                newDate.setMinutes(calendar.getMinutes());
                setDate(DatetimeRPC.getInstance(begin, end, newDate));
                calendar.window.close();
                changeListeners.fireChange(change);
            }
        });
    }

    public void setForm(ScreenBase screen) {
        this.screen = screen;
    }

    public void removeChangeListener(ChangeListener listener) {
        // TODO Auto-generated method stub
    }

    public void clear() {
        // TODO Auto-generated method stub

    }

    public Object getValue() {
        // TODO Auto-generated method stub
        Date date = new Date(getText().replaceAll("-", "/"));
        return DatetimeRPC.getInstance(begin, end, date);
    }

    public void setValue(Object val) {
        // TODO Auto-generated method stub
        if (val != null)
            setText(((DatetimeRPC)val).toString());
        else
            setText("");
    }

    public Object getDisplay(String title) {
        // TODO Auto-generated method stub
        Label tl = new Label();
        tl.setText(getText());
        tl.setWordWrap(false);
        if (title != null)
            tl.setTitle(title);
        return tl;
    }

    public Widget getEditor() {
        // TODO Auto-generated method stub
        return this;
    }

    public Date getWeekDate() {
        return weekDate;
    }
}
