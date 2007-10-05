package org.openelis.gwt.client.widget;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
/**
 * Deprecated Class probably will be deleted
 * @author tschmidt
 *
 */
public class AppTableWidget extends Composite {
    protected AbsolutePanel outer = new AbsolutePanel();
    protected AbsolutePanel hPanel = new AbsolutePanel();
    protected AbsolutePanel bPanel = new AbsolutePanel();
    protected FlexTable header = new FlexTable();
    protected FlexCellFormatter hFormatter = header.getFlexCellFormatter();
    protected HorizontalPanel navPanel = new HorizontalPanel();
    protected FlexTable body = new FlexTable();
    protected ScrollPanel scrollPanel = new ScrollPanel();
    protected int activeRow = -1;
    protected BodyListener bodyListener = new BodyListener();
    protected boolean ignore;
    protected boolean ignoreKey;
    protected Image refresh = new Image("Images/reload3.gif");
    protected HorizontalPanel titlePanel = new HorizontalPanel();

    public AppTableWidget() {
        initWidget(outer);
        hPanel.add(header);
        hPanel.setWidth("100%");
        outer.add(hPanel);
        outer.add(scrollPanel);
        outer.add(navPanel);
        bPanel.add(body);
        scrollPanel.addStyleName("Scroll");
        scrollPanel.setHeight("400px");
        scrollPanel.setWidth("100%");
        scrollPanel.add(bPanel);
    }

    public void setActive(int row) {
        if (row < 0) {
            if (activeRow >= 0) {
                body.getRowFormatter().removeStyleName(activeRow, "ActiveRow");
            }
            activeRow = -1;
        } else if (activeRow < 0) {
            body.getRowFormatter().addStyleName(row, "ActiveRow");
            activeRow = row;
        } else if (activeRow == row) {
            body.getRowFormatter().removeStyleName(row, "ActiveRow");
            activeRow = -1;
        } else {
            body.getRowFormatter().removeStyleName(activeRow, "ActiveRow");
            body.getRowFormatter().addStyleName(row, "ActiveRow");
            activeRow = row;
        }
    }

    private class BodyListener implements TableListener {
        public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
            if (ignore) {
                ignore = false;
                return;
            }
            setActive(row);
        }
    }
}
