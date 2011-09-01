package org.openelis.test.client.dropdown;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class DropdownTestPlace extends Place {
    
	private String dropdownTestName;

    public DropdownTestPlace(String token) {
        this.dropdownTestName = token;
    }

    public String getDropdownTestName() {
        return dropdownTestName;
    }

    public static class Tokenizer implements PlaceTokenizer<DropdownTestPlace> {
        @Override
        public String getToken(DropdownTestPlace place) {
            return place.getDropdownTestName();
        }

        @Override
        public DropdownTestPlace getPlace(String token) {
            return new DropdownTestPlace(token);
        }
    }
}
