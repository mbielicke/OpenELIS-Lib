package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
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
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
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
public class TableViewSmall extends Composite implements ScrollListener {
    
    private class Delay extends Timer {
        public int pos;

        public Delay(int pos, int time) {
            this.pos = pos;
            this.schedule(time);
        }

        public void run() {
            if (scrollBar.getScrollPosition() == pos) {
                controller.scrollLoad(pos);
            }
        }
    };
    
    public boolean loaded;
    public ScrollPanel cellView = new ScrollPanel();
    public ScrollPanel scrollBar = new ScrollPanel();
    public AbsolutePanel headerView = new AbsolutePanel();
    public AbsolutePanel rowsView = new AbsolutePanel();
    private AbsolutePanel statView = new AbsolutePanel();
    private FlexTable ft = new FlexTable();
    private final HorizontalPanel titlePanel = new HorizontalPanel();
    private final Label titleLabel = new Label();
    public Grid table = new Grid();
    public FlexTable header = new FlexTable();
    public Grid rows = new Grid();
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
    public String height;
    public TableControllerSmall controller = null;
    
    public TableViewSmall() {
        initWidget(vp);
    }
    
    public void initTable(TableControllerSmall controller) {
        this.controller = controller;
        if(headers != null){
            header.setCellSpacing(0);
            
            if(title != null && !title.equals("")){
            	titleLabel.setText(title);
            	titlePanel.add(titleLabel);
            	titlePanel.addStyleName("TitlePanel");
            }
            int j = 0;
            for (int i = 0; i < headers.length; i++) {
                if (i > 0) {
                    FocusPanel bar = new FocusPanel();
                    bar.addMouseListener(controller);
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
                if (!controller.sortable[i] && !controller.filterable[i])
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
/*            FocusPanel bar = new FocusPanel();
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
*/            
            header.setStyleName(headerStyle);
            header.addTableListener(controller);
        }
        //DOM.setStyleAttribute(statView.getElement(), "overflow", "hidden");
        headerView.add(header);
        rowsView.add(rows);
        DOM.setStyleAttribute(rowsView.getElement(), "overflow", "hidden");
        cellView.setWidget(table);
        DOM.setStyleAttribute(headerView.getElement(), "overflow", "hidden");
        cellView.addScrollListener(this);
        vp.add(titlePanel);
        if(controller.showRows) {
            ft.setWidget(0,1,headerView);
            ft.setWidget(1,0,rowsView);
            ft.getFlexCellFormatter().setVerticalAlignment(1, 0, HasAlignment.ALIGN_TOP);
            ft.setWidget(1,1,cellView);
            ft.setWidget(1, 2, scrollBar);
        }else{
            ft.setWidget(0,0,headerView);
            ft.setWidget(1,0,cellView);
            ft.setWidget(1,1,scrollBar);
        }
        vp.add(ft);
        table.setCellSpacing(1);
        table.addStyleName(tableStyle);
        cellView.setWidget(table);
        ft.setCellSpacing(0);
        scrollBar.setWidth("15px");
        scrollBar.addScrollListener(this);
        AbsolutePanel ap = new AbsolutePanel();
        ap.setWidth("15px");
        ap.setHeight("15px");
        DOM.setStyleAttribute(scrollBar.getElement(), "overflowX", "hidden");
        scrollBar.setWidget(ap);
    }
    
    
    public void setHeight(String height) {
        cellView.setHeight(height);
        rowsView.setHeight(height);
        scrollBar.setHeight(height);
        headerView.setHeight("18px");
        this.height = height;
    }

    public void setWidth(String width) {
        this.width = width;
        cellView.setWidth(width);
        headerView.setWidth(width);
        rows.setWidth("25px");
        rowsView.setWidth("25px");
    }

    public void setTableListener(TableListener listener) {
        table.addTableListener(listener);
        controller = (TableControllerSmall)listener;
    }

    public void setCell(Widget widget, int row, int col) {
        table.setWidget(row, col, widget);
        if (widget instanceof FocusWidget)
            ((FocusWidget)widget).setFocus(true);
    }

    public void reset(int row, int col) {
        table.resize(row,col);
        for(int i = 0; i < row; i++){
            for(int j= 0; j < col; j++){
                table.getCellFormatter().addStyleName(i,
                                                      j,
                                                      cellStyle);
            if (controller.colAlign != null && controller.colAlign[j] != null) {
                table.getCellFormatter()
                          .setHorizontalAlignment(i, j, controller.colAlign[j]);
            }
            table.getCellFormatter().setWidth(i, j, controller.curColWidth[j] + "px");
            table.getRowFormatter().addStyleName(i, rowStyle);
            if(i % 2 == 1){
                DOM.setStyleAttribute(table.getRowFormatter().getElement(i), "background", "#f8f8f9");
            }
        }
        }
        if(controller.showRows && row > 0){
            rows.resize(row,1);
            for(int i = 0; i < row; i++){
                Label rowNum = new Label(String.valueOf(i+1));
                rows.setWidget(i,0,rowNum);
                rows.getCellFormatter().setStyleName(i, 0, "RowNum");
            }
            rows.setCellSpacing(1);
            rowsView.add(rows);
            DOM.setStyleAttribute(rowsView.getElement(), "overflow", "hidden");
        }
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

        HTML prevNav = new HTML("<a class='navIndex' value='-1'>&lt;</a>");
        prevNav.addClickListener(controller);
        
        
        HTML noprevNav = new HTML("<a class='navIndex' style = 'color:lightgrey;'>&lt;</a>");       
        
        HTML nextNav = new HTML("<a class='navIndex' value='+1'>&gt;</a>");
        nextNav.addClickListener(controller);               
        
        HTML nonextNav = new HTML("<a class='navIndex' style = 'color:lightgrey;' >&gt;</a>");
                       
                        
        if (curIndex > 0) {            
            navPanel.add(prevNav);                                   
        }
        else{          
            navPanel.add(noprevNav);
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
       // if (curIndex < pages - 1) {            
        if(controller.model.rowsPerPage <= controller.model.numRows()){   
            navPanel.add(nextNav);    
            
        }
        else{
            
            navPanel.add(nonextNav);
        }
        
        vp.add(navPanel);
    }

    public void onScroll(Widget widget, int scrollLeft, final int scrollTop) {
        if(top != scrollTop){
            //new Delay(scrollTop, 250);
            controller.scrollLoad(scrollTop);
            if(controller.showRows){
                rowsView.setWidgetPosition(rows,0,-scrollTop);
            }
            top = scrollTop;
        }
        if(left != scrollLeft){
           headerView.setWidgetPosition(header, -scrollLeft, 0);
           left = scrollLeft;
        }
    }
    
    protected void onAttach() {
        // TODO Auto-generated method stub
        if(!loaded){
            controller.load();
            loaded = true;
        }
        super.onAttach();
    }
    
    public void setScrollHeight(int height) {
        scrollBar.getWidget().setHeight(height+"px");
    }
    
}