package org.openelis.gwt.client.widget;

import org.openelis.gwt.client.screen.Screen;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class AToZPanel extends Composite implements ClickListener {
	protected HTML aButton = new HTML();
	protected HTML bButton = new HTML();
	protected HTML cButton = new HTML();
	protected HTML dButton = new HTML();
	protected HTML eButton = new HTML();
	protected HTML fButton = new HTML();
	protected HTML gButton = new HTML();
	protected HTML hButton = new HTML();
	protected HTML iButton = new HTML();
	protected HTML jButton = new HTML();
	protected HTML kButton = new HTML();
	protected HTML lButton = new HTML();
	protected HTML mButton = new HTML();
	protected HTML nButton = new HTML();
	protected HTML oButton = new HTML();
	protected HTML pButton = new HTML();
	protected HTML qButton = new HTML();
	protected HTML rButton = new HTML();
	protected HTML sButton = new HTML();
	protected HTML tButton = new HTML();
	protected HTML uButton = new HTML();
	protected HTML vButton = new HTML();
	protected HTML wButton = new HTML();
	protected HTML xButton = new HTML();
	protected HTML yButton = new HTML();
	protected HTML zButton = new HTML();
	private HorizontalPanel mainHP = new HorizontalPanel();
	private VerticalPanel alphabetButtonVP = new VerticalPanel();
	private VerticalPanel tablePanel = new VerticalPanel();
	private HorizontalPanel hideablePanel = new HorizontalPanel();
	public FormTable leftTable = null;
	private CheckBox checkBox = new CheckBox();
	private VerticalPanel middleBar = new VerticalPanel();
	//private HorizontalPanel imagePanel = new HorizontalPanel();
	protected HTML arrowButton = new HTML();
	
	public AToZPanel(Node node, final Screen screen) {
		//build the letter buttons
		aButton.setHTML("<a class='navIndex'>A</a>");
		aButton.addClickListener(this);
		bButton.setHTML("<a class='navIndex'>B</a>");
		bButton.addClickListener(this);
		cButton.setHTML("<a class='navIndex'>C</a>");
		cButton.addClickListener(this);
		dButton.setHTML("<a class='navIndex'>D</a>");
		dButton.addClickListener(this);
		eButton.setHTML("<a class='navIndex'>E</a>");
		eButton.addClickListener(this);
		fButton.setHTML("<a class='navIndex'>F</a>");
		fButton.addClickListener(this);
		gButton.setHTML("<a class='navIndex'>G</a>");
		gButton.addClickListener(this);
		hButton.setHTML("<a class='navIndex'>H</a>");
		hButton.addClickListener(this);
		iButton.setHTML("<a class='navIndex'>I</a>");
		iButton.addClickListener(this);
		jButton.setHTML("<a class='navIndex'>J</a>");
		jButton.addClickListener(this);
		kButton.setHTML("<a class='navIndex'>K</a>");
		kButton.addClickListener(this);
		lButton.setHTML("<a class='navIndex'>L</a>");
		lButton.addClickListener(this);
		mButton.setHTML("<a class='navIndex'>M</a>");
		mButton.addClickListener(this);
		nButton.setHTML("<a class='navIndex'>N</a>");
		nButton.addClickListener(this);
		oButton.setHTML("<a class='navIndex'>O</a>");
		oButton.addClickListener(this);
		pButton.setHTML("<a class='navIndex'>P</a>");
		pButton.addClickListener(this);
		qButton.setHTML("<a class='navIndex'>Q</a>");
		qButton.addClickListener(this);
		rButton.setHTML("<a class='navIndex'>R</a>");
		rButton.addClickListener(this);
		sButton.setHTML("<a class='navIndex'>S</a>");
		sButton.addClickListener(this);
		tButton.setHTML("<a class='navIndex'>T</a>");
		tButton.addClickListener(this);
		uButton.setHTML("<a class='navIndex'>U</a>");
		uButton.addClickListener(this);
		vButton.setHTML("<a class='navIndex'>V</a>");
		vButton.addClickListener(this);
		wButton.setHTML("<a class='navIndex'>W</a>");
		wButton.addClickListener(this);
		xButton.setHTML("<a class='navIndex'>X</a>");
		xButton.addClickListener(this);
		yButton.setHTML("<a class='navIndex'>Y</a>");
		yButton.addClickListener(this);
		zButton.setHTML("<a class='navIndex'>Z</a>");
		zButton.addClickListener(this);
		
		//put buttons in panel
		alphabetButtonVP.add(aButton);
		alphabetButtonVP.add(bButton);
		alphabetButtonVP.add(cButton);
		alphabetButtonVP.add(dButton);
		alphabetButtonVP.add(eButton);
		alphabetButtonVP.add(fButton);
		alphabetButtonVP.add(gButton);
		alphabetButtonVP.add(hButton);
		alphabetButtonVP.add(iButton);
		alphabetButtonVP.add(jButton);
		alphabetButtonVP.add(kButton);
		alphabetButtonVP.add(lButton);
		alphabetButtonVP.add(mButton);
		alphabetButtonVP.add(nButton);
		alphabetButtonVP.add(oButton);
		alphabetButtonVP.add(pButton);
		alphabetButtonVP.add(qButton);
		alphabetButtonVP.add(rButton);
		alphabetButtonVP.add(sButton);
		alphabetButtonVP.add(tButton);
		alphabetButtonVP.add(uButton);
		alphabetButtonVP.add(vButton);
		alphabetButtonVP.add(wButton);
		alphabetButtonVP.add(xButton);
		alphabetButtonVP.add(yButton);
		alphabetButtonVP.add(zButton);
		
		DeferredCommand.addCommand(new Command() {
            public void execute() {
            	middleBar.setHeight(String.valueOf(screen.getOffsetHeight()-20)+"px");
            }
        });
		//middleBar.setHeight(String.valueOf(height)+"px");
		//imagePanel.setHeight("100%");
		tablePanel.setHeight("100%");		
		tablePanel.setWidth("150px");
		mainHP.setHeight("100%");
		//hideablePanel.setHeight("100%");
		
		//need to set the alignment before adding any widgets or it wont work
		middleBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		middleBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		leftTable = new FormTable(node);
		leftTable.controller.model.paged = true;
		leftTable.controller.model.rowsPerPage = 20;
		leftTable.controller.model.totalPages = leftTable.controller.model.shown / leftTable.controller.model.rowsPerPage;
		leftTable.controller.model.pageIndex = 0;
		leftTable.controller.model.shown = leftTable.controller.model.shown;
		leftTable.controller.model.totalRows = leftTable.controller.model.numRows();
		leftTable.controller.model.showIndex = false;
		leftTable.setStyleName("ScreenTable");
        
        
		//set the height of table
		if (node.getAttributes().getNamedItem("height") != null) {
			leftTable.setHeight(node.getAttributes().getNamedItem("height").getNodeValue());
		}
		//set the width of table
		if (node.getAttributes().getNamedItem("width") != null) {
			leftTable.setWidth(node.getAttributes().getNamedItem("width").getNodeValue()); 
		}
		tablePanel.add(leftTable);
				
		//build the check box
		checkBox.setText("Show Inactive");
		tablePanel.add(checkBox);
		
		//make the hideable panel hide if visable="false"
		if (node.getAttributes().getNamedItem("visable") != null && 
				node.getAttributes().getNamedItem("visable").getNodeValue() == "false") {
		//build the arrow button
		arrowButton.setHTML("<img src=\"Images/arrow-right-unselected.png\" onmouseover=\"this.src='Images/arrow-right-selected.png';\" " +
				" onmouseout=\"this.src='Images/arrow-right-unselected.png';\" style=\"vertical-align:middle;\">");
		hideablePanel.setVisible(false);
		
		}else{
			arrowButton.setHTML("<img src=\"Images/arrow-left-unselected.png\" onmouseover=\"this.src='Images/arrow-left-selected.png';\" " +
				" onmouseout=\"this.src='Images/arrow-left-unselected.png';\" style=\"vertical-align:middle;\">");			
		}
		
		//arrowButton.setStyleName("LeftMenuArrowImage");
		//arrowButton.setHeight("100%");
		arrowButton.addClickListener(this);
		
		//add arrow button to middle panel
		hideablePanel.add(alphabetButtonVP);
		hideablePanel.add(tablePanel);
		
		//imagePanel.add(arrowButton);
		
		middleBar.setStyleName("LeftMenuPanePanel");
		middleBar.add(arrowButton);
		
		mainHP.setSpacing(5);
		alphabetButtonVP.setSpacing(1);
		tablePanel.setSpacing(1);
		
		mainHP.add(hideablePanel);
		mainHP.add(middleBar);
		//mainHP.setCellVerticalAlignment(arrowButton,HasVerticalAlignment.ALIGN_MIDDLE);
		
		initWidget(mainHP);		
	}

	public void onClick(Widget sender) {
		if(sender == aButton){
			
		}else if(sender == bButton){
			
		}else if(sender == cButton){
			
		}else if(sender == dButton){
			
		}else if(sender == eButton){
			
		}else if(sender == fButton){
			
		}else if(sender == gButton){
			
		}else if(sender == hButton){
			
		}else if(sender == iButton){
			
		}else if(sender == jButton){
			
		}else if(sender == kButton){
			
		}else if(sender == lButton){
			
		}else if(sender == mButton){
			
		}else if(sender == nButton){
			
		}else if(sender == oButton){
			
		}else if(sender == pButton){
			
		}else if(sender == qButton){
			
		}else if(sender == rButton){
			
		}else if(sender == sButton){
			
		}else if(sender == tButton){
			
		}else if(sender == uButton){
			
		}else if(sender == vButton){
			
		}else if(sender == wButton){
			
		}else if(sender == xButton){
			
		}else if(sender == yButton){
			
		}else if(sender == zButton){
			
		}else if(sender == arrowButton){
			if(hideablePanel.isVisible()){
        		hideablePanel.setVisible(false);
        		// HTML html = new HTML("<img src=\"Images/close_left_panel.png\">");
        		arrowButton.setHTML("<img src=\"Images/arrow-right-unselected.png\" onmouseover=\"this.src='Images/arrow-right-selected.png';\" onmouseout=\"this.src='Images/arrow-right-unselected.png';\">");
        		 //screenHtml.initWidget(html);
        		// html.setStyleName("ScreenHTML");
        	}else{
        		hideablePanel.setVisible(true);
        		arrowButton.setHTML("<img src=\"Images/arrow-left-unselected.png\" onmouseover=\"this.src='Images/arrow-left-selected.png';\" onmouseout=\"this.src='Images/arrow-left-unselected.png';\">");
        	}	
		}
	}
	
	//need to create a method that will fetch data for the letter clicked
	private void getDataForLetter(String letter){
	       
    /*    SecurityService.getInstance().getPersonLetter(letter,"P",
                                                        new AsyncCallback() {
                                                            public void onSuccess(Object result) {
                                                                ((FormTable)getWidget("staffTable")).controller.setModel((TableModel)result);
                                                            }

                                                            public void onFailure(Throwable caught) {
                                                                Window.alert(caught.getMessage());
                                                            }
                                                        });
                                                        */
	}
}
