package org.openelis.gwt.widget.calendar;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.DateHelper;
import org.openelis.gwt.widget.TextBox;

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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * This class extends the TextBox<Datetime> and adds a button for using the
 * CalendarWidget to pick Dates.
 * 
 * @author tschmidt
 * 
 */
public class Calendar extends TextBox<Datetime> {

    /**
     * Used for Calendar display
     */
    protected HorizontalPanel hp;
    protected Button          button;
    protected PopupPanel      popup;
    protected CalendarWidget  calendar;
    protected MonthYearWidget monthYearWidget;

    /**
     * Default no-arg constructor
     */
    public Calendar() {

    }

    /**
     * This method will set the display of the Calendar and set up Event
     * Handlers
     */
    @Override
    public void init() {
        /*
         * Final instance of this class used Anonymous handlers
         */
        final Calendar source = this;

        /*
         * Final instance of the private class KeyboardHandler
         */
        final KeyboardHandler keyHandler = new KeyboardHandler();

        hp = new HorizontalPanel();
        textbox = new com.google.gwt.user.client.ui.TextBox();

        button = new Button();
        AbsolutePanel image = new AbsolutePanel();
        image.setStyleName("CalendarButton");
        button.setDisplay(image, false);

        hp.add(textbox);
        hp.add(button);

        initWidget(hp);

        hp.setStyleName("Calendar");
        textbox.setStyleName("TextboxUnselected");

        /*
         * Since HorizontalPanel is not a Focusable widget we need to listen to
         * the textbox focus and blur events and pass them through to the
         * handlers registered to source.
         */
        textbox.addFocusHandler(new FocusHandler() {
            public void onFocus(FocusEvent event) {
                FocusEvent.fireNativeEvent(event.getNativeEvent(), source);
            }
        });

        textbox.addBlurHandler(new BlurHandler() {
            public void onBlur(BlurEvent event) {
                BlurEvent.fireNativeEvent(event.getNativeEvent(), source);
                validateValue(true);
            }
        });

        /*
         * Register click handler to button to show the popup table
         */
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                showPopup();
            }
        });

        /*
         * Registers the keyboard handling this widget
         */
        addHandler(keyHandler, KeyDownEvent.getType());

    }

    /**
     * This method will initialize and show the popup panel for this widget.
     */
    private void showPopup() {

        if (popup == null) {
            popup = new PopupPanel(true);
            popup.setPreviewingAllNativeEvents(false);
            popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                public void onClose(CloseEvent<PopupPanel> event) {

                }
            });
        }
        try {
            if (calendar == null) {
                calendar = new CalendarWidget( ((DateHelper)helper).getBegin(),
                                              ((DateHelper)helper).getEnd());
                calendar.addValueChangeHandler(new ValueChangeHandler<Datetime>() {
                    public void onValueChange(ValueChangeEvent<Datetime> event) {
                        popup.hide();
                        textbox.setText(helper.format(event.getValue()));
                        textbox.setFocus(true);
                    }
                });
                calendar.addMonthSelectHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        if (monthYearWidget == null) {
                            monthYearWidget = new MonthYearWidget();
                            monthYearWidget.addOKHandler(new ClickHandler() {
                                public void onClick(ClickEvent event) {
                                    calendar.drawMonth(monthYearWidget.getYear(),
                                                       monthYearWidget.getMonth());
                                    popup.setWidget(calendar);
                                }
                            });
                            monthYearWidget.addCancelHandler(new ClickHandler() {
                                public void onClick(ClickEvent event) {
                                    popup.setWidget(calendar);
                                }
                            });
                        }
                        monthYearWidget.setYear(calendar.getYear());
                        monthYearWidget.setMonth(calendar.getMonth());
                        popup.setWidget(monthYearWidget);
                    }
                });
            }

            calendar.setDate(helper.getValue(getText()));
            popup.setWidget(calendar);

        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.showRelativeTo(this);

        DeferredCommand.addCommand(new Command() {
            public void execute() {
                ((Button)calendar.getDefinition().getWidget("today")).setFocus(true);
            }
        });

    }

    /**
     * This private class will handle key events for this widget
     * 
     * @author tschmidt
     * 
     */
    private class KeyboardHandler implements KeyDownHandler {

        public void onKeyDown(KeyDownEvent event) {
            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_ENTER:
                    showPopup();
            }
        }
    }

    /**
     * Overridden method from TextBox for enabling and disabling the widget
     */
    @Override
    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
        if (enabled)
            sinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
        else
            unsinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
        super.setEnabled(enabled);
    }

    /**
     * Overridden method from TextBox for setting the Exception style.
     */
    @Override
    public void addExceptionStyle(String style) {
        textbox.addStyleName(style);
    }

    /**
     * Overridden method from TextBox for removing the Exception style.
     */
    @Override
    public void removeExceptionStyle(String style) {
        textbox.removeStyleName(style);
    }

}
