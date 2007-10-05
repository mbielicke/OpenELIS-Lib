package org.openelis.gwt.client.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;
import java.util.Date;
/**
 * Calendar Widget will display a calendar and text boxes with the current time in
 * a popup panel.  A user can then select a date and time that will be returned back
 * to a form.
 * @author tschmidt
 *
 */
public class CalendarWidget implements ClickListener, SourcesChangeEvents {
    private class NavBar extends Composite implements ClickListener {
        public final DockPanel bar = new DockPanel();
        public final Button prevMonth = new Button("&lt;", this);
        public final Button prevYear = new Button("&lt;&lt;", this);
        public final Button nextYear = new Button("&gt;&gt;", this);
        public final Button nextMonth = new Button("&gt;", this);
        public final HTML title = new HTML();
        private final CalendarWidget calendar;

        public NavBar(CalendarWidget calendar) {
            this.calendar = calendar;
            initWidget(bar);
            bar.setStyleName("navbar");
            title.setStyleName("header");
            HorizontalPanel prevButtons = new HorizontalPanel();
            prevButtons.add(prevMonth);
            prevButtons.add(prevYear);
            HorizontalPanel nextButtons = new HorizontalPanel();
            nextButtons.add(nextYear);
            nextButtons.add(nextMonth);
            bar.add(prevButtons, DockPanel.WEST);
            bar.setCellHorizontalAlignment(prevButtons, DockPanel.ALIGN_LEFT);
            bar.add(nextButtons, DockPanel.EAST);
            bar.setCellHorizontalAlignment(nextButtons, DockPanel.ALIGN_RIGHT);
            bar.add(title, DockPanel.CENTER);
            bar.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
            bar.setCellHorizontalAlignment(title, HasAlignment.ALIGN_CENTER);
            bar.setCellVerticalAlignment(title, HasAlignment.ALIGN_MIDDLE);
            bar.setCellWidth(title, "100%");
        }

        public void onClick(Widget sender) {
            if (sender == prevMonth) {
                calendar.prevMonth();
            } else if (sender == prevYear) {
                calendar.prevYear();
            } else if (sender == nextYear) {
                calendar.nextYear();
            } else if (sender == nextMonth) {
                calendar.nextMonth();
            }
        }
    }

    private static class CellHTML extends HTML {
        private int day;

        public CellHTML(String text, int day) {
            super(text);
            this.day = day;
        }

        public int getDay() {
            return day;
        }
    }

    private class TimeBar extends Composite implements ClickListener {
        public final DockPanel tbar = new DockPanel();
        public final HTML hour = new HTML();
        public final HTML minute = new HTML();
        public final HTML colon = new HTML(":");
        private final CalendarWidget calendar;

        public TimeBar(CalendarWidget calendar) {
            this.calendar = calendar;
            initWidget(tbar);
            tbar.setStyleName("timebar");
            HorizontalPanel time = new HorizontalPanel();
            time.add(hour);
            time.add(colon);
            time.add(minute);
            time.setStyleName("header");
            DOM.setStyleAttribute(time.getElement(), "cursor", "pointer");
            tbar.add(time, DockPanel.CENTER);
            tbar.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
            tbar.setCellHorizontalAlignment(time, HasAlignment.ALIGN_CENTER);
            tbar.setCellVerticalAlignment(time, HasAlignment.ALIGN_MIDDLE);
            // tbar.setCellWidth(time, "100%");
            hour.addClickListener(this);
            minute.addClickListener(this);
        }

        public void setTimeText() {
            hour.setText(String.valueOf(calendar.hour));
            if (calendar.minute < 10)
                minute.setText("0" + String.valueOf(calendar.minute));
            else
                minute.setText(String.valueOf(calendar.minute));
        }

        public void onClick(Widget sender) {
            if (sender == hour)
                calendar.incrementHour();
            if (sender == minute)
                calendar.incrementMinute();
        }
    }

    private final NavBar navbar = new NavBar(this);
    private final DockPanel outer = new DockPanel();
    private final TimeBar timebar = new TimeBar(this);
    private int selectedRow;
    private int selectedCol;
    private String widgetFocus = "D";
    private final FlexTable grid = new FlexTable() {
        public boolean clearCell(int row, int column) {
            boolean retValue = super.clearCell(row, column);
            Element td = getCellFormatter().getElement(row, column);
            DOM.setInnerHTML(td, "");
            return retValue;
        }
    };
    private Date date = new Date();
    private int hour = date.getHours();
    private int minute;
    public PopupWindow window;
    private boolean week;
    private ChangeListenerCollection changeListeners;
    private String[] days = new String[] {"Sunday",
                                          "Monday",
                                          "Tuesday",
                                          "Wednesday",
                                          "Thursday",
                                          "Friday",
                                          "Saturday"};
    private String[] months = new String[] {"January",
                                            "February",
                                            "March",
                                            "April",
                                            "May",
                                            "June",
                                            "July",
                                            "August",
                                            "September",
                                            "October",
                                            "November",
                                            "December"};

    public CalendarWidget(boolean week) {
        this.week = week;
        window = new PopupWindow("Calendar");
        minute = date.getMinutes() - (date.getMinutes() % 5);
        grid.setStyleName("table");
        grid.setCellSpacing(0);
        // if(!week)
        outer.add(timebar, DockPanel.SOUTH);
        outer.setCellHorizontalAlignment(timebar, HasAlignment.ALIGN_CENTER);
        outer.add(navbar, DockPanel.NORTH);
        outer.add(grid, DockPanel.CENTER);
        drawCalendar();
        outer.setStyleName("CalendarWidget");
        window.content.add(outer);
        window.content.setStyleName("Content");
        window.setContentPanel(window.content);
    }

    private void drawCalendar() {
        int year = getYear();
        int month = getMonth();
        int day = getDay();
        setHeaderText(year, month);
        timebar.setTimeText();
        grid.getRowFormatter().setStyleName(0, "weekheader");
        for (int i = 0; i < days.length; i++) {
            grid.getCellFormatter().setStyleName(0, i, "days");
            grid.setText(0, i, days[i].substring(0, 3));
        }
        Date now = new Date();
        int sameDay = now.getDate();
        int today = (now.getMonth() == month && now.getYear() + 1900 == year) ? sameDay
                                                                             : 0;
        int firstDay = new Date(year - 1900, month, 1).getDay();
        int numOfDays = getDaysInMonth(year, month);
        int j = 0;
        for (int i = 1; i < 7; i++) {
            for (int k = 0; k < 7; k++, j++) {
                int displayNum = (j - firstDay + 1);
                if (j < firstDay || displayNum > numOfDays) {
                    grid.getCellFormatter().setStyleName(i, k, "empty");
                    grid.setHTML(i, k, "&nbsp;");
                } else {
                    if (displayNum == 1) {
                        selectedRow = i;
                        selectedCol = k;
                    }
                    HTML html = new CellHTML("<span>"        + String.valueOf(displayNum)
                                                             + "</span>",
                                             displayNum);
                    html.addClickListener(this);
                    grid.getCellFormatter().setStyleName(i, k, "cell");
                    if (displayNum == today) {
                        grid.getCellFormatter().addStyleName(i, k, "today");
                        selectedRow = i;
                        selectedCol = k;
                    } else if (displayNum == sameDay) {
                        grid.getCellFormatter().addStyleName(i, k, "day");
                    }
                    grid.setWidget(i, k, html);
                }
                if (k == 0 || k == 6) {
                    grid.getCellFormatter().addStyleName(i, k, "weekend");
                }
            }
        }
        if (week)
            grid.getRowFormatter().addStyleName(selectedRow, "selectedRow");
        else
            grid.getCellFormatter().addStyleName(selectedRow,
                                                 selectedCol,
                                                 "selected");
        timebar.hour.removeStyleName("selected");
        timebar.minute.removeStyleName("selected");
    }

    protected void setHeaderText(int year, int month) {
        navbar.title.setText(months[month] + ", " + year);
    }

    private int getDaysInMonth(int year, int month) {
        switch (month) {
            case 1:
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                    return 29; // leap year
                else
                    return 28;
            case 3:
                return 30;
            case 5:
                return 30;
            case 8:
                return 30;
            case 10:
                return 30;
            default:
                return 31;
        }
    }

    public void prevMonth() {
        int month = getMonth() - 1;
        if (month < 0) {
            setDate(getYear() - 1, 11, getDay());
        } else {
            setMonth(month);
        }
        if (week)
            grid.getRowFormatter().removeStyleName(selectedRow, "selectedRow");
        else
            grid.getCellFormatter().removeStyleName(selectedRow,
                                                    selectedCol,
                                                    "selected");
        drawCalendar();
    }

    public void nextMonth() {
        int month = getMonth() + 1;
        if (month > 11) {
            setDate(getYear() + 1, 0, getDay());
        } else {
            setMonth(month);
        }
        if (week)
            grid.getRowFormatter().removeStyleName(selectedRow, "selectedRow");
        else
            grid.getCellFormatter().removeStyleName(selectedRow,
                                                    selectedCol,
                                                    "selected");
        drawCalendar();
    }

    public void prevYear() {
        setYear(getYear() - 1);
        drawCalendar();
    }

    public void nextYear() {
        setYear(getYear() + 1);
        drawCalendar();
    }

    private void setDate(int year, int month, int day) {
        date = new Date(year - 1900, month, day);
    }

    private void setYear(int year) {
        date.setYear(year - 1900);
    }

    private void setMonth(int month) {
        date.setMonth(month);
    }

    public int getYear() {
        return 1900 + date.getYear();
    }

    public int getMonth() {
        return date.getMonth();
    }

    public int getDay() {
        return date.getDate();
    }

    public String getDayString() {
        int day = getDay();
        if (day < 10)
            return "0" + day;
        return String.valueOf(day);
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minute;
    }

    public Date getDate() {
        return date;
    }

    public String getDateTime() {
        return getMonth() + "-"
               + getDayString()
               + "-"
               + getYear()
               + " "
               + hour
               + ":"
               + minute;
    }

    public void onClick(Widget sender) {
        CellHTML cell = (CellHTML)sender;
        setDate(getYear(), getMonth(), cell.getDay());
        if (week) {
            int day = date.getDay();
            if (day > 0) {
                day = date.getDate() - day;
                setDate(getYear(), getMonth(), day);
            }
        }
        // drawCalendar();
        changeListeners.fireChange(window);
    }

    public void addChangeListener(ChangeListener listener) {
        if (changeListeners == null)
            changeListeners = new ChangeListenerCollection();
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        if (changeListeners != null)
            changeListeners.remove(listener);
    }

    public void incrementHour() {
        hour++;
        if (hour == 24) {
            hour = 0;
        }
        timebar.setTimeText();
    }

    public void decrementHour() {
        hour--;
        if (hour == -1)
            hour = 23;
        timebar.setTimeText();
    }

    public void incrementMinute() {
        minute += 5;
        if (minute >= 60)
            minute = 0;
        timebar.setTimeText();
    }

    public void decrementMinute() {
        minute -= 5;
        if (minute < 0)
            minute = 55;
        timebar.setTimeText();
    }

    public void setCurrentTime() {
        // TODO Auto-generated method stub
        if (week)
            return;
        this.date = new Date();
        this.minute = date.getMinutes() - (date.getMinutes() % 5);
        this.hour = date.getHours();
        this.timebar.setTimeText();
        // window.content.add(outer);
    }

    public boolean onKeyUpPreview(char key, int modifier) {
        if (key == 'h' || key == 'H') {
            widgetFocus = "H";
            grid.getCellFormatter().addStyleName(selectedRow,
                                                 selectedCol,
                                                 "calinactive");
            timebar.hour.addStyleName("selected");
            timebar.minute.removeStyleName("selected");
        }
        if (key == 'm' || key == 'M') {
            widgetFocus = "M";
            grid.getCellFormatter().addStyleName(selectedRow,
                                                 selectedCol,
                                                 "calinactive");
            timebar.hour.removeStyleName("selected");
            timebar.minute.addStyleName("selected");
        }
        if (key == 'd' || key == 'D') {
            widgetFocus = "D";
            grid.getCellFormatter().removeStyleName(selectedRow,
                                                    selectedCol,
                                                    "calinactive");
            timebar.hour.removeStyleName("selected");
            timebar.minute.removeStyleName("selected");
        }
        if ((int)key == KeyboardListener.KEY_TAB) {
            if (widgetFocus.equals("D")) {
                widgetFocus = "H";
                grid.getCellFormatter().addStyleName(selectedRow,
                                                     selectedCol,
                                                     "calinactive");
                timebar.hour.addStyleName("selected");
                timebar.minute.removeStyleName("selected");
            } else if (widgetFocus.equals("H")) {
                widgetFocus = "M";
                grid.getCellFormatter().addStyleName(selectedRow,
                                                     selectedCol,
                                                     "calinactive");
                timebar.hour.removeStyleName("selected");
                timebar.minute.addStyleName("selected");
            } else if (widgetFocus.equals("M")) {
                widgetFocus = "D";
                grid.getCellFormatter().removeStyleName(selectedRow,
                                                        selectedCol,
                                                        "calinactive");
                timebar.hour.removeStyleName("selected");
                timebar.minute.removeStyleName("selected");
            }
        }
        if ((int)key == KeyboardListener.KEY_RIGHT && !week) {
            if (widgetFocus.equals("D")) {
                grid.getCellFormatter().removeStyleName(selectedRow,
                                                        selectedCol,
                                                        "selected");
                selectedCol++;
                if (selectedCol == 7) {
                    selectedCol = 0;
                    selectedRow++;
                    if (selectedRow == 7)
                        selectedRow = 1;
                }
                String style = grid.getCellFormatter()
                                   .getStyleName(selectedRow, selectedCol);
                if (grid.getCellFormatter()
                        .getStyleName(selectedRow, selectedCol)
                        .indexOf("empty") >= 0) {
                    selectedRow = 1;
                    selectedCol = 0;
                    while (grid.getCellFormatter()
                               .getStyleName(selectedRow, selectedCol)
                               .indexOf("empty") >= 0)
                        selectedCol++;
                }
                grid.getCellFormatter().addStyleName(selectedRow,
                                                     selectedCol,
                                                     "selected");
            }
            if (widgetFocus.equals("H")) {
                incrementHour();
            }
            if (widgetFocus.equals("M")) {
                incrementMinute();
            }
        }
        if ((int)key == KeyboardListener.KEY_LEFT && !week) {
            if (widgetFocus.equals("D")) {
                grid.getCellFormatter().removeStyleName(selectedRow,
                                                        selectedCol,
                                                        "selected");
                selectedCol--;
                if (selectedCol == -1) {
                    selectedCol = 6;
                    selectedRow--;
                    if (selectedRow == 0)
                        selectedRow = 6;
                }
                if (grid.getCellFormatter()
                        .getStyleName(selectedRow, selectedCol)
                        .indexOf("empty") >= 0) {
                    selectedRow = 6;
                    selectedCol = 6;
                    while (grid.getCellFormatter()
                               .getStyleName(selectedRow, selectedCol)
                               .indexOf("empty") >= 0)
                        selectedCol--;
                }
                grid.getCellFormatter().addStyleName(selectedRow,
                                                     selectedCol,
                                                     "selected");
            }
            if (widgetFocus.equals("H")) {
                decrementHour();
            }
            if (widgetFocus.equals("M")) {
                decrementMinute();
            }
        }
        if ((int)key == KeyboardListener.KEY_DOWN) {
            if (widgetFocus.equals("D")) {
                if (week)
                    grid.getRowFormatter().removeStyleName(selectedRow,
                                                           "selectedRow");
                else
                    grid.getCellFormatter().removeStyleName(selectedRow,
                                                            selectedCol,
                                                            "selected");
                selectedRow++;
                if (selectedRow == 7) {
                    selectedRow = 1;
                }
                if (grid.getCellFormatter()
                        .getStyleName(selectedRow, selectedCol)
                        .indexOf("empty") >= 0) {
                    selectedRow = 1;
                    while (grid.getCellFormatter()
                               .getStyleName(selectedRow, selectedCol)
                               .indexOf("empty") >= 0)
                        selectedRow++;
                }
                if (week)
                    grid.getRowFormatter().addStyleName(selectedRow,
                                                        "selectedRow");
                else
                    grid.getCellFormatter().addStyleName(selectedRow,
                                                         selectedCol,
                                                         "selected");
            }
            if (widgetFocus.equals("H")) {
                decrementHour();
            }
            if (widgetFocus.equals("M")) {
                decrementMinute();
            }
        }
        if ((int)key == KeyboardListener.KEY_UP) {
            if (widgetFocus.equals("D")) {
                if (week)
                    grid.getRowFormatter().removeStyleName(selectedRow,
                                                           "selectedRow");
                else
                    grid.getCellFormatter().removeStyleName(selectedRow,
                                                            selectedCol,
                                                            "selected");
                selectedRow--;
                if (selectedRow == 0) {
                    selectedRow = 6;
                }
                if (grid.getCellFormatter()
                        .getStyleName(selectedRow, selectedCol)
                        .indexOf("empty") >= 0) {
                    selectedRow = 6;
                    while (grid.getCellFormatter()
                               .getStyleName(selectedRow, selectedCol)
                               .indexOf("empty") >= 0)
                        selectedRow--;
                }
                if (week)
                    grid.getRowFormatter().addStyleName(selectedRow,
                                                        "selectedRow");
                else
                    grid.getCellFormatter().addStyleName(selectedRow,
                                                         selectedCol,
                                                         "selected");
            }
            if (widgetFocus.equals("H")) {
                incrementHour();
            }
            if (widgetFocus.equals("M")) {
                incrementMinute();
            }
        }
        if ((int)key == KeyboardListener.KEY_ENTER) {
            if (week)
                selectedCol = 0;
            CellHTML cell = (CellHTML)grid.getWidget(selectedRow, selectedCol);
            setDate(getYear(), getMonth(), cell.getDay());
            changeListeners.fireChange(window);
        }
        return window.onKeyUpPreview(key, modifier);
    }
}
