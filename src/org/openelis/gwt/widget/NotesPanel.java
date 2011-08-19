package org.openelis.gwt.widget;

import org.openelis.gwt.common.Datetime;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NotesPanel extends Composite {

    private ScrollPanel   scroll = new ScrollPanel();
    private VerticalPanel notes  = new VerticalPanel();
    private String        width;

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
        Label subjectText, userDateText, bodyText;
        VerticalPanel note;
        HorizontalPanel topRow;
        DateField field;
        
        field = new DateField();
        field.setBegin(Datetime.YEAR);
        field.setEnd(Datetime.SECOND);
        field.setFormat("yyyy-MM-dd HH:mm");
        field.setValue(time);

        userDateText = new Label(" by " + userName + " on " + field.format());
        userDateText.setStyleName("noteAuthorText");

        bodyText = new Label(text);
        bodyText.setWordWrap(true);
        bodyText.setStyleName("noteBodyText");

        note = new VerticalPanel();
        note.setWidth(width);

        topRow = new HorizontalPanel();
        topRow.setWidth(width);
        if (subject != null) {
            subjectText = new Label(subject);
            subjectText.setStyleName("noteSubjectText");
            note.add(subjectText);
            note.add(userDateText);
        }
        note.add(bodyText);

        notes.add(note);
        if (notes.getWidgetCount() % 2 == 0)
            note.addStyleName("noteAltTableRow");
        else
            note.addStyleName("noteTableRow");
    }

    public void clearNotes() {
        notes.clear();
    }

}
