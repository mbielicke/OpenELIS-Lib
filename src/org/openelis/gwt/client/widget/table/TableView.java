package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.MouseWheelListenerCollection;
import com.google.gwt.user.client.ui.MouseWheelVelocity;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesMouseWheelEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class represents the View of the table widget. It contains the logic for
 * displaying and styling a table on the screen.
 * 
 * @author tschmidt
 * 
 */
public class TableView extends Composite implements ScrollListener {

    private class Delay extends Timer {
        public int pos;

        public Delay(int pos, int time) {
            this.pos = pos;
            this.schedule(time);
        }

        public void run() {
            if (cellView.getScrollPosition() == pos) {
                controller.scrollLoad(pos);
            }
        }
    };
    
    public ScrollPanel cellView = new ScrollPanel();
    public AbsolutePanel headerView = new AbsolutePanel();
    private AbsolutePanel rowsView = new AbsolutePanel();
    private AbsolutePanel statView = new AbsolutePanel();
    private FlexTable ft = new FlexTable();
    private final HorizontalPanel titlePanel = new HorizontalPanel();
    private final Label titleLabel = new Label();
    public Grid table = new Grid();
    public FlexTable header = new FlexTable();
    public FlexTable rows = new FlexTable();
    private int left = 0;
    private int top = 0;
    private String[] headers;
    public Label[] hLabels;
    private String title = "";
    public String widgetStyle = "TableWidget";
    public String cellStyle = "TableCell";
    public String rowStyle = "TableRow";
    public String headerStyle = "Header";
    public String headerCellStyle = "HeaderCell";
    public String tableStyle = "Table";
    public String selectedStyle = "Selected";
    public String navLinks = "NavLinks";
    protected HorizontalPanel navPanel = new HorizontalPanel();
    private VerticalPanel vp = new VerticalPanel();
    public String width;
    public TableController controller = null;
    
    public TableView() {
        initWidget(vp);
    }
    
    public void initTable() {
        if(headers != null){
            header.setCellSpacing(0);
            titleLabel.setText(title);
            titlePanel.add(titleLabel);
            titlePanel.addStyleName("TitlePanel");
            int j = 0;
            for (int i = 0; i < headers.length; i++) {
                if (i > 0) {
                    FocusPanel bar = new FocusPanel();
                    HorizontalPanel hpBar = new HorizontalPanel();
                    AbsolutePanel ap1 = new AbsolutePanel();
                    ap1.addStyleName("HeaderBarPad");
                    AbsolutePanel ap2 = new AbsolutePanel();
                    ap2.addStyleName("HeaderBar");
                    AbsolutePanel ap3 = new AbsolutePanel();
                    ap3.addStyleName("HeaderBarPad");
                    hpBar.add(ap1);
                    hpBar.add(ap2);
                    hpBar.add(ap3);
                    bar.add(hpBar);
                    header.setWidget(0, j, bar);
                    header.getFlexCellFormatter().addStyleName(0,
                                                               j,
                                                               headerCellStyle);
                    header.getFlexCellFormatter().setWidth(0, j, "3px");
                    j++;
                }
                SimplePanel hp0 = new SimplePanel();
                DOM.setStyleAttribute(hp0.getElement(), "overflow", "hidden");
                DOM.setStyleAttribute(hp0.getElement(), "overflowX", "hidden");
                HorizontalPanel hp = new HorizontalPanel();
                hLabels[i].addStyleName("HeaderLabel");
                Image img = new Image("Images/unapply.png");
                img.setHeight("10px");
                img.setWidth("10px");
                DOM.setStyleAttribute(hLabels[i].getElement(),"overflowX","hidden");
                img.addStyleName("HeaderIMG");
                img.addStyleName("hide");
                hp.add(hLabels[i]);
                hLabels[i].setWordWrap(false);
                hp.add(img);
                hp.setCellHorizontalAlignment(hLabels[i],
                                              HasHorizontalAlignment.ALIGN_RIGHT);
                hp.setCellHorizontalAlignment(img,
                                              HasHorizontalAlignment.ALIGN_LEFT);
                hp0.setWidget(hp);
                DOM.setElementProperty(hp0.getElement(),"align","center");              
                header.setWidget(0, j, hp0);
                header.getFlexCellFormatter().addStyleName(0,
                                                           j,
                                                           headerCellStyle);
                j++;
            }
            FocusPanel bar = new FocusPanel();
            HorizontalPanel hpBar = new HorizontalPanel();
            AbsolutePanel ap1 = new AbsolutePanel();
            ap1.addStyleName("HeaderBarPad");
            AbsolutePanel ap2 = new AbsolutePanel();
            ap2.addStyleName("HeaderBar");
            AbsolutePanel ap3 = new AbsolutePanel();
            ap3.addStyleName("HeaderBarPad");
            hpBar.add(ap1);
            hpBar.add(ap2);
            hpBar.add(ap3);
            bar.add(hpBar);
            header.setWidget(0, j, bar);
            header.getFlexCellFormatter().addStyleName(0, j, headerCellStyle);
            header.getFlexCellFormatter().setWidth(0, j, "3px");
            header.setStyleName(headerStyle);
            
        }
        //DOM.setStyleAttribute(statView.getElement(), "overflow", "hidden");
        headerView.add(header);
        rowsView.add(rows);
        DOM.setStyleAttribute(rowsView.getElement(), "overflow", "hidden");
        cellView.setWidget(table);
        //cellView.setWidgetPosition(table, 0, 0);
        //DOM.setStyleAttribute(cellView.getElement(), "overflow", "hidden");
        DOM.setStyleAttribute(headerView.getElement(), "overflow", "hidden");
        //vScroll.setWidget(vsc);
        //hScroll.setWidget(hsc);
        cellView.addScrollListener(this);
        //vScroll.addScrollListener(this);
        //hScroll.addScrollListener(this);
        vp.add(titlePanel);
        //ft.setWidget(0,1,staticCols);
        ft.setWidget(0,0,headerView);
        //ft.setWidget(1,0,rowsView);
        //ft.setWidget(1,1,statView);
        ft.setWidget(1,0,cellView);
        //ft.setWidget(2,0,hScroll);
        //ft.setWidget(1,1,vScroll);
        vp.add(ft);
        //cellView.addMouseWheelListener(this);
    }
    
    
    public void setHeight(String height) {
        cellView.setHeight(height);
        //rowsView.setHeight(height);
        headerView.setHeight("18px");
    }

    public void setWidth(String width) {
        this.width = width;
        cellView.setWidth(width);
        headerView.setWidth(width);
        //rows.setWidth("20px");
        //rowsView.setWidth("18px");
    }

    public void setTableListener(TableListener listener) {
        table.addTableListener(listener);
        controller = (TableController)listener;
    }

    public void setCell(Widget widget, int row, int col) {
        table.setWidget(row, col, widget);
        if (widget instanceof FocusWidget)
            ((FocusWidget)widget).setFocus(true);
    }

    public void reset(int rows, int cols) {
        if (rows == 0)
            table = new Grid();
        else
            table = new Grid(rows,cols);
        table.setCellSpacing(1);
        table.addStyleName(tableStyle);
        cellView.setWidget(table);
    }
    
    public void setTable(){
        cellView.clear();
        cellView.setWidget(table);
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
        if(hLabels == null){
            hLabels = new Label[headers.length];
            for(int i = 0; i < headers.length; i++){
                hLabels[i] = new Label(headers[i]);
            }
        }else{
            for(int i = 0; i < headers.length; i++){
                hLabels[i].setText(headers[i]);
            }
        }
    }

    public void setTitle(String title) {
        this.title = title;
        titleLabel.setText(title);
    }

    public void setNavPanel(int curIndex, int pages, boolean showIndex) {
        vp.remove(navPanel);
        navPanel = new HorizontalPanel();
        navPanel.setSpacing(3);
        navPanel.addStyleName(navLinks);
        navPanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        DOM.setAttribute(navPanel.getElement(), "align", "center");
        if (curIndex > 0) {
            HTML nav = new HTML("<a class='navIndex' value='-1'>&lt;</a>");
            nav.addClickListener(controller);
            navPanel.add(nav);
        }
        if(showIndex){
            for (int i = 1; i <= pages; i++) {
                final int index = i - 1;
                HTML nav = null;
                if (index != curIndex) {
                    nav = new HTML("<a class='navIndex' value='" + index
                                   + "'>"
                                   + i
                                   + "</a>");
                    nav.addClickListener(controller);
                } else {
                    nav = new HTML("" + i);
                    nav.setStyleName("current");
                }
                navPanel.add(nav);
            }
        }
        if (curIndex < pages - 1) {
            HTML nav = new HTML("<a class='navIndex' value='+1'>&gt;</a>");
            nav.addClickListener(controller);
            navPanel.add(nav);
        }
        vp.add(navPanel);
    }

    public void onScroll(Widget widget, int scrollLeft, int scrollTop) {
        if(top != scrollTop){
            //new Delay(scrollTop, 25);
            controller.scrollLoad(scrollTop);
            top = scrollTop;
        }
        if(left != scrollLeft){
           headerView.setWidgetPosition(header, -scrollLeft, 0);
           left = scrollLeft;
        }
    }

}