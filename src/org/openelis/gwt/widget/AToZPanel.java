package org.openelis.gwt.widget;

import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataModelWidget;
import org.openelis.gwt.screen.AppScreenForm;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.ScreenButtonPanel;
import org.openelis.gwt.screen.ScreenLabel;
import org.openelis.gwt.screen.ScreenVertical;
import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.widget.table.TableController;
import org.openelis.gwt.widget.table.TableView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AToZPanel extends TableController implements ClickListener, ChangeListener {
	private HorizontalPanel mainHP = new HorizontalPanel();
	private ScreenVertical alphabetButtonVP = new ScreenVertical();
	private VerticalPanel tablePanel = new VerticalPanel();
	private HorizontalPanel hideablePanel = new HorizontalPanel();
	private AbsolutePanel middleBar = new AbsolutePanel();
    private FocusPanel arrow = new FocusPanel();
    protected DataModel dm;
    protected DataModelWidget modelWidget;
    protected ButtonPanel bpanel;
    protected AppButton selectedButton;
    protected boolean locked;
	
	
	public AToZPanel() { 
		//DeferredCommand.addCommand(new Command() {
        //    public void execute() {
        //    	middleBar.setHeight(String.valueOf(getParent().getOffsetHeight()-20)+"px");
        //    }
       // });
		//tablePanel.setHeight("100%");		
		//tablePanel.setWidth("150px");
		mainHP.setHeight("100%");
		
		//need to set the alignment before adding any widgets or it wont work
		//middleBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		//middleBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        middleBar.setHeight("100%");
        view = new TableView();
        view.setTableListener(this);
		tablePanel.add(view);
		
		//make the hideable panel hide if visable="false"
		//if (node.getAttributes().getNamedItem("visible") != null && 
		//		node.getAttributes().getNamedItem("visible").getNodeValue() == "false") {
		//build the arrow button
        hideablePanel.setVisible(false);
        middleBar.setStyleName("LeftMenuPanePanelClosed");
        arrow.setStyleName("LeftMenuPanePanelDiv");
        arrow.addClickListener(this);
        arrow.addMouseListener(this);
        middleBar.add(arrow);
		
		//add arrow button to middle panel
		hideablePanel.add(alphabetButtonVP);
		hideablePanel.add(tablePanel);
		
		//imagePanel.add(arrowButton);
		
		//middleBar.setStyleName("LeftMenuPanePanel");
		//middleBar.add(arrowButton);
		
		mainHP.setSpacing(0);
		//alphabetButtonVP.setSpacing(1);
		tablePanel.setSpacing(1);
		
		mainHP.add(hideablePanel);
		mainHP.add(middleBar);
		//mainHP.setCellVerticalAlignment(arrowButton,HasVerticalAlignment.ALIGN_MIDDLE);
		
		initWidget(mainHP);		
	}
    
    public void setButtonPanel(Widget wid) {
        alphabetButtonVP.add(wid);
        if(wid instanceof ScreenButtonPanel){
            bpanel = (ButtonPanel)((ScreenWidget)wid).getWidget();
            bpanel.addChangeListener(this);
        }
    }

	public void onClick(Widget sender) {

		if(sender == arrow){
			if(hideablePanel.isVisible()){
        		hideablePanel.setVisible(false);
                middleBar.setStyleName("LeftMenuPanePanelClosed");
                arrow.setFocus(false);
        	}else{
        		hideablePanel.setVisible(true);
                middleBar.setStyleName("LeftMenuPanePanelOpen");
                arrow.setFocus(false);
                DeferredCommand.addCommand(new Command() {
                    public void execute() {    
                  		sizeTable();
                    }
                });
        	}	
		}
        if(sender == view.nextNav){
            modelWidget.getModel().selecttLast(false);
            modelWidget.setPage(modelWidget.getPage()+1);
        }
        if(sender == view.prevNav){
            modelWidget.getModel().selecttLast(false);
            modelWidget.setPage(modelWidget.getPage()-1);
        }
    }

    public void scrollLoad(int scrollPos) {
        try{
            int rowsPer = maxRows;
            if(maxRows > dm.size())
                rowsPer = dm.size();
            start = (scrollPos)/(cellHeight);
            if(start+rowsPer > dm.size())
                start = start - ((start+rowsPer) - dm.size());
            for(int i = 0; i < rowsPer; i++){
                loadRow(i);
            }
        }catch(Exception e){
            Window.alert("scrollLoad "+e.getMessage());
        }
    }
    
    private void loadRow(int index){
        for(int i = 0; i < curColWidth.length; i++){
            ScreenLabel label = (ScreenLabel)view.table.getWidget(index,i);
            label.label.setText(dm.get(start+index).getObject(i).getValue().toString());
            label.setUserObject(dm.get(start+index).getKey().getValue());
        }
        view.table.getRowFormatter().removeStyleName(index, TableView.selectedStyle);
    }
    
    private void createRow(int index){
        for(int i = 0; i < curColWidth.length; i++){
            ScreenLabel label = new ScreenLabel("   ",null);
            view.table.setWidget(index, i, label);
            label.label.setWordWrap(false);
            DOM.setStyleAttribute(label.getElement(), "overflowX", "hidden");
            label.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
            label.setWidth(curColWidth[i]+"px");
            view.table.getFlexCellFormatter().setWidth(index, i, curColWidth[i] + "px");
            view.table.getFlexCellFormatter().setHeight(index, i, cellHeight+"px");
            view.table.getFlexCellFormatter().addStyleName(index,
                                                           i,
                                                           TableView.cellStyle);
        }
        view.table.getRowFormatter().addStyleName(index, TableView.rowStyle);
        if(index % 2 == 1){
            DOM.setStyleAttribute(view.table.getRowFormatter().getElement(index), "background", "#f8f8f9");
        }
        if(showRows){
            Label rowNum = new Label(String.valueOf(index+1));
            view.rows.setWidget(index,0,rowNum);
            view.rows.getFlexCellFormatter().setStyleName(index, 0, "RowNum");
            view.rows.getFlexCellFormatter().setHeight(index,0,cellHeight+"px");
        }
    }

    public boolean onEventPreview(Event event) {
        // TODO Auto-generated method stub
        return true;
    }
    
    public void onChange(Widget sender) {
        if(sender instanceof DataModelWidget){
            if(((DataModelWidget)sender).event == DataModelWidget.REFRESH) {
                modelWidget = (DataModelWidget)sender;
                dm = ((DataModelWidget)sender).getModel();
                if(dm != null && dm.size() > 0){
                    int num = maxRows;
                    if(dm.size() < maxRows)
                        num = dm.size();
                    for(int i = 0; i < num; i++){
                        createRow(i);
                    }
                    view.setScrollHeight((dm.size()*cellHeight)+(dm.size()*cellSpacing)+cellSpacing);
                    view.setNavPanel(0, 0, false);
                    scrollLoad(0);
                }
            }
            if(((DataModelWidget)sender).event == DataModelWidget.SELECTION){
                if(selectedRow > -1){
                    view.table.getRowFormatter().removeStyleName(selectedRow,TableView.selectedStyle);
                }
                selectedRow = modelWidget.getSelectedIndex() - start;
                view.table.getRowFormatter().addStyleName(selectedRow,TableView.selectedStyle);
            }
            return;
        }
        if(sender instanceof AppScreenForm) {
            if(bpanel != null){
                switch(((AppScreenForm)sender).state) {
                    case FormInt.ADD:
                    case FormInt.DELETE:
                    case FormInt.QUERY:
                    case FormInt.UPDATE:
                        bpanel.setPanelState(ButtonPanel.LOCKED);
                        locked = true;
                        break;
                    case FormInt.DEFAULT:
                    case FormInt.DISPLAY:
                    case FormInt.BROWSE:
                        bpanel.setPanelState(ButtonPanel.ENABLED);
                        locked = false;
                        break;
                }
            }
            return;
        }
        if(sender == bpanel){
            if(selectedButton != null){
                selectedButton.changeState(AppButton.UNPRESSED);
            }
            selectedButton = bpanel.buttonClicked;
        }   
    }
    
    public void setTableWidth(String width) {
        view.setWidth(width);
    }
    
    public void onMouseEnter(Widget sender) {
        if(sender == arrow){
            arrow.addStyleName("Hover");
            middleBar.addStyleName("Hover");
        }
        
    }

    public void onMouseLeave(Widget sender) {
        if(sender == arrow){
            arrow.removeStyleName("Hover");
            middleBar.addStyleName("Hover");
        }
        
    }
    
    public void onCellClicked(SourcesTableEvents sender, int row, int col){
        if(selectedRow == row || locked){
            return;
        }
        if(selectedRow > -1){
            view.table.getRowFormatter().removeStyleName(selectedRow,TableView.selectedStyle);
        }
        selectedRow = row;
        view.table.getRowFormatter().addStyleName(selectedRow,TableView.selectedStyle);
        modelWidget.select(start+row);
    }
}
