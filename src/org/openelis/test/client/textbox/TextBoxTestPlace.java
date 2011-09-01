package org.openelis.test.client.textbox;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class TextBoxTestPlace extends Place {
    
	private String textBoxTestName;

    public TextBoxTestPlace(String token) {
        this.textBoxTestName = token;
    }

    public String getTextBoxTestName() {
        return textBoxTestName;
    }

    public static class Tokenizer implements PlaceTokenizer<TextBoxTestPlace> {
        @Override
        public String getToken(TextBoxTestPlace place) {
            return place.getTextBoxTestName();
        }

        @Override
        public TextBoxTestPlace getPlace(String token) {
            return new TextBoxTestPlace(token);
        }
    }
}
