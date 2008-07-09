package org.openelis.gwt.widget;

import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.screen.ScreenWindow;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class EditBox extends Composite implements ClickListener{
	
	private TextArea text = new TextArea();
	private FocusPanel fp = new FocusPanel();
	private HorizontalPanel hp = new HorizontalPanel();
	private PopupPanel pop;
	private static String editorScreen = "<screen><display><VerticalPanel><textarea key='editor' tools='false' width='300px' height='200px' showError='false'/><HorizontalPanel halign='center'>"+
										"<appButton action='ok' key='ok' onclick='this' style='Button'>"+
									   		"<text>OK</text>"+
									    "</appButton>" +
									    "<appButton action='cancel' key='cancel' onclick='this' style='Button'>"+
									       "<text>Cancel</text>" +
	                                    "</appButton>" +
	                                    "</HorizontalPanel>" +
	                                    "</VerticalPanel>"+
	                                    "</display>"+
	                                    "<rpc key='display'/>"+
	                                    "</screen>";
	private String formattedText;
	
	private class Editor extends AppScreen implements ClickListener {
	    
		public Editor(){
			drawScreen(editorScreen);
			afterDraw(true);
		}
		
		@Override
		public void afterDraw(boolean sucess) {
			super.afterDraw(sucess);
			((TextArea)getWidget("editor")).setText(text.getText());
		}
		
		public void onClick(Widget sender) {
			// TODO Auto-generated method stub
			if(sender == getWidget("ok")){
				text.setText(((TextArea)getWidget("editor")).getText());
				//formattedText = ((RichTextWidget)getWidget("editor")).getText();
				pop.hide();
			}
			if(sender == getWidget("cancel")){
				pop.hide();
			}
			
		}
		
	}

	public EditBox() {
		initWidget(hp);
		text.setStyleName("ScreenTextArea");
		text.setHeight("18px");
		hp.add(text);
		hp.add(fp);
		fp.setStyleName("CalendarButton");
		fp.addClickListener(this);
	}

	public void onClick(Widget sender) {
		if(sender == fp){
			if(pop == null){
				pop = new PopupPanel();
			}
				Editor editor = new Editor();
	            ScreenWindow win = new ScreenWindow(pop,"","standardNotePicker","",false);
	            //win.setStyleName("ErrorWindow");
	            win.setContent(editor);
	            win.setVisible(true);
				pop.setPopupPosition(text.getAbsoluteLeft(), text.getAbsoluteTop()+text.getOffsetHeight());
				pop.setWidget(win);
				//pop.center();
				pop.show();
			
		}	
	}
	
	public void setText(String txt){
		text.setText(txt);
	}
	
	public String getText() {
		return text.getText();
	}
	
	public void setReadOnly(boolean read){
		text.setReadOnly(read);
	}
	
	public void addFocusListener(FocusListener listener){
		text.addFocusListener(listener);
	}
	
	public void removeFocusListener(FocusListener listener){
		text.removeFocusListener(listener);
	}
	
	public void setFocus(boolean focus){
		text.setFocus(focus);
	}
	
	public boolean isReadOnly(){
		return text.isReadOnly();
	}
	
	public void setWidth(String width){
		text.setWidth(width);
	}

}
