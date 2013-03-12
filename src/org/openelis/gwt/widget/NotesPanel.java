package org.openelis.gwt.widget;

import org.openelis.ui.common.Datetime;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NotesPanel extends Composite {

    private ScrollPanel   scroll;
    private VerticalPanel notes;
    private String        width;
    private DateField     headerDate;
    
    public NotesPanel() {
        scroll = new ScrollPanel();
        notes  = new VerticalPanel();

        headerDate = new DateField();
        headerDate.setBegin(Datetime.YEAR);
        headerDate.setEnd(Datetime.SECOND);
        headerDate.setFormat("yyyy-MM-dd HH:mm");
        
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
        Label<String> subjectText, userDateText;
        HTML bodyText;
        VerticalPanel note;
        
        if (subject == null && text == null)
            return;
        
        note = new VerticalPanel();
        note.setWidth(width);
        note.addStyleName("noteTableRow");

        if (subject != null) {
            subjectText = new Label<String>(subject);
            subjectText.setStyleName("noteSubjectText");
            note.add(subjectText);

            headerDate.setValue(time);
            userDateText = new Label<String>("by " + userName + " on " + headerDate.format());
            userDateText.setStyleName("noteAuthorText");
            note.add(userDateText);
        }
        if (text != null) {
            bodyText = new HTML("<pre>"+encode(text)+"</pre>");
            bodyText.setStyleName("noteBodyText");
            note.add(bodyText);
        }
        notes.add(note);
    }

    public void clearNotes() {
        notes.clear();
    }
    
    private String encode(String text) {
        return text.replaceAll("<", "&lt;");
    }
}
