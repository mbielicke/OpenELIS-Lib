package org.openelis.gwt.widget;

import org.openelis.gwt.common.Datetime;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NotesPanel extends Composite {
	
	private ScrollPanel scroll = new ScrollPanel();
	private VerticalPanel notes = new VerticalPanel();
	private String width;
	
	public NotesPanel() {
		initWidget(scroll);
		scroll.setWidget(notes);
	}
	
	public void setWidth(String width) {
		this.width = width;
		scroll.setWidth(width);
	}
	
	public void setHeight(String height) {
		scroll.setHeight(height);
	}
	
	public void addNote(String subject, String userName, String text, Datetime time) {
		VerticalPanel note = new VerticalPanel();
		HorizontalPanel topRow = new HorizontalPanel();
		DateHelper field = new DateHelper();
		field.setBegin(Datetime.YEAR);
		field.setEnd(Datetime.SECOND);
		field.setPattern("yyyy-MM-dd HH:mm");
		Label<String> titleText = new Label<String>(subject);
		Label<String> userDateText = new Label<String>(" by "+userName + " on " + field.format(time));
		Label<String> bodyText = new Label<String>(text);
		bodyText.setWordWrap(true);
		note.setWidth(width);
		topRow.setWidth(width);
		if(subject != null){
		    note.add(titleText);
		    note.add(userDateText);
		}
		note.add(bodyText);
		titleText.setStyleName("noteSubjectText");
		userDateText.setStyleName("noteAuthorText");
		bodyText.setStyleName("noteBodyText");
		
		notes.add(note);
		
		if(notes.getWidgetCount() % 2 == 0)
			note.addStyleName("noteAltTableRow");
		else
			note.addStyleName("noteTableRow");
		
	}
	
	public void clearNotes() {
		notes.clear();
	}

}
