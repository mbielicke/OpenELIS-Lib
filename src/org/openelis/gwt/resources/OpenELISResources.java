package org.openelis.gwt.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface OpenELISResources extends ClientBundle {
	public static final OpenELISResources INSTANCE = GWT.create(OpenELISResources.class);
	
	@Source("css/general.css")
	GeneralCSS general();
	
	@Source("css/button.css")
	ButtonCSS button();
	
	@Source("css/window.css")
	WindowCSS window();
	
	@Source("css/windownocap.css")
	WindowNoCap windowNoCap();
	
	@Source("css/dialog.css")
	DialogCSS dialog();
	
	@Source({"css/autoComplete.css","css/select.css"})
	AutoCompleteCSS autocomplete();
	
	@Source("css/checkbox.css")
	CheckboxCSS checkbox();
	
	@Source("css/collapse.css")
	CollapseCSS collapse();
	
	@Source({"css/confirm.css","css/window.css"})
	ConfirmCSS confirm();
	
	@Source({"css/dropdown.css","css/select.css","css/checkbox.css"})
	DropdownCSS dropdown();
	
	@Source("css/menu.css")
	MenuCSS menuCss();
	
	@Source("css/note.css")
	NoteCSS note();
	
	@Source("css/popup.css")
	PopupCSS popup();
	
	@Source("css/tabbarscroller.css")
	TabBarScrollerCSS tabBarScroller();
	
	@Source("css/tabpanel.css")
	@NotStrict
	TabPanelCSS tabpanel();
	
	@Source("css/text.css")
	TextCSS text();
	
	@Source({"css/calendar.css","css/select.css"})
	CalendarCSS calendar();
	
	@Source("css/table.css")
	TableCSS table();
	
	@Source("css/dragdrop.css")
	DragDropCSS dragDrop();
	
	@Source({"css/tree.css","css/table.css"})
	TreeCSS tree();
	
	@Source("css/buttonpanel.css")
	ButtonPanelCSS buttonPanel();
	
	@Source("css/progress.css")
	ProgressCSS progress();
	
	@Source("css/percent.css")
	PercentCSS percent();
	
	@Source("css/calendarui.css")
	CalendarUICSS calendarui();
	
	@Source("css/calendarbutton.css")
	ButtonCSS calendarbutton();
	
	@Source("images/close.png")
	ImageResource closeButton();
	
	@Source("images/minimize.gif")
	ImageResource minimizeButton();
	

	@Source("images/trans.gif")
	ImageResource glass();	
	
	@Source("images/transgrey.gif")
	ImageResource modal();
	
	@Source("images/OSXspinnerGIF.gif")
	ImageResource spinner();
	
	@Source("images/exclamation.png")
	ImageResource exclamation();
		
	@Source("images/trigger.gif")
	ImageResource trigger();
	
	@Source("images/apply.png")
	ImageResource apply();
	
	@Source("images/unapply.png")
	ImageResource unapply();
	
	@Source("images/unknow.png")
	ImageResource unknown();
	
	@Source("images/icon-warning.gif")
	ImageResource iconWarning();
	
	@Source("images/icon-error.gif")
	ImageResource iconError();
	
	@Source("images/icon-question.gif")
	ImageResource iconQuestion();
	
	@Source("images/select-arrow.jpg")
	ImageResource selectArrow();
	
	@Source("images/bullet_yellow.png")
	ImageResource warn();
	
	@Source("images/bullet_red.png")
	ImageResource error();
	
	@Source("images/go-up.png")
	ImageResource ascending();
	
	@Source("images/go-down.png")
	ImageResource descending();
	
	@Source("images/nextbuttonimage.gif")
	ImageResource menuArrow();
	
	@Source("images/menuup.gif")
	ImageResource menuUp();
	
	@Source("images/menudown.gif")
	ImageResource menuDown();
	
	@Source("images/moveright.gif")
	ImageResource moveRight();
	
	@Source("images/moveleft.gif")
	ImageResource moveLeft();
	
	@Source("images/fieldadvanced.png")
	ImageResource data();
	
	@Source("images/previousmonth.gif")
	ImageResource previousMonth();
	
	@Source("images/previousmonthhover.gif")
	ImageResource previousMonthHover();
	
	@Source("images/monthselect.gif")
	ImageResource monthSelect();
	
	@Source("images/monthselecthover.gif")
	ImageResource monthSelectHover();
	
	@Source("images/nextmonth.gif")
	ImageResource nextMonth();
	
	@Source("images/nextmonthhover.gif")
	ImageResource nextMonthHover();
	
	@Source("images/todaybar.gif")
	@ImageOptions(repeatStyle=RepeatStyle.Horizontal)
	ImageResource todayBar();
	
	@Source("images/datecell.gif")
	@ImageOptions(repeatStyle=RepeatStyle.Horizontal)
	ImageResource dateCell();
	
	@Source("images/daybar.gif")
	@ImageOptions(repeatStyle=RepeatStyle.Horizontal)
	ImageResource dayBar();
	
	@Source("images/date-trigger.gif")
	ImageResource dateTrigger();
	
	@Source("images/buttonbarbg.gif")
	@ImageOptions(repeatStyle=RepeatStyle.Horizontal)
	ImageResource rowSelection();
	
	@Source("images/commitbuttonimage.gif")
	ImageResource commitButton();
	
	@Source("images/stop.gif")
	ImageResource stop();
	
	@Source("images/group.gif")
	ImageResource group();
	
	@Source("images/report.png")
	ImageResource report();
	
	@Source("images/tree+.gif")
	ImageResource treePlus();
	
	@Source("images/tree-.gif")
	ImageResource treeMinus();
	
	@Source("images/buttondivider.gif")
	ImageResource buttonDivider();
		
	@Source("images/calculator_add.png")
	ImageResource calculatorAdd();
	
	@Source("images/calculator_adddisabled.png")
	ImageResource calculatorAddDisabled();
	
	@Source("images/calculator_delete.png")
	ImageResource calculatorDelete();
	
	@Source("images/calculator_deletedisabled.png")
	ImageResource calculatorDeleteDisabled();
	
	@Source("images/arrow_out.png")
	ImageResource arrowOut();
	
	@Source("images/arrow_outdisabled.png")
	ImageResource arrowOutDisabled();
	
	@Source("images/arrow_in.png")
	ImageResource arrowIn();
	
	@Source("images/arrow_indisabled.png")
	ImageResource arrowInDisabled();

	@Source("images/find.png")
	ImageResource find();
	
	@Source("images/finddisabled.png")
	ImageResource findDisabled();
	
	@Source("images/house.png")
	ImageResource house();
	
	@Source("images/querybuttonimage.gif")
	ImageResource query();
	
	@Source("images/querybuttonimagedisabled.gif")
	ImageResource queryDisabled();
	
	@Source("images/previousbuttonimage.gif")
	ImageResource previous();
	
	@Source("images/previousbuttonimagedisabled.gif")
	ImageResource previousDisabled();
	
	@Source("images/nextbuttonimage.gif")
	ImageResource next();
	
	@Source("images/nextbuttonimagedisabled.gif")
	ImageResource nextDisabled();
	
	@Source("images/addbuttonimage.gif")
	ImageResource add();
	
	@Source("images/addbuttonimagedisabled.gif")
	ImageResource addDisabled();
	
	@Source("images/addButtonIcon.png")
	ImageResource addButton();
	
	@Source("images/addButtonIcondisabled.png")
	ImageResource addButtonDisabled();
	
	@Source("images/updatebuttonimage.gif")
	ImageResource update();
	
	@Source("images/updatebuttonimagedisabled.gif")
	ImageResource updateDisabled();
	
	@Source("images/deletebuttonimage.gif")
	ImageResource delete();
	
	@Source("images/deletebuttonimagedisabled.gif")
	ImageResource deleteDisabled();
	
	@Source("images/deleteButtonIcon.png")
	ImageResource deleteIcon();
	
	@Source("images/deleteButtonIcondisabled.png")
	ImageResource deleteIconDisabled();
	
	@Source("images/commitbuttonimage.gif")
	ImageResource commit();
	
	@Source("images/commitbuttonimagedisabled.gif")
	ImageResource commitDisabled();
	
	@Source("images/cart_go.png")
	ImageResource process();
	
	@Source("images/cart_godisabled.png")
	ImageResource processDisabled();
	
	@Source("images/abortbuttonimage.gif")
	ImageResource abort();
	
	@Source("images/abortbuttonimagedisabled.gif")
	ImageResource abortDisabled();
	
	@Source("images/quickentrybuttonimage.gif")
	ImageResource duplicate();
	
	@Source("images/history.png")
	ImageResource history();
	
	@Source("images/calendar.png")
	ImageResource calendarButton();
	
	@Source("images/openleftbar.gif")
	ImageResource openLeftBar();
	
	@Source("images/closeleftbar.gif")
	ImageResource closeLeftBar();
	
	@Source("images/tabfirst.gif")
	ImageResource tabFirst();
	
	@Source("images/tab.gif")
	ImageResource tab();
	
	@Source("images/selectedtab.gif")
	ImageResource selectedTab();
	
	
}
