package org.openelis.test.client.table;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class TableTestPlace extends Place {
    
	private String name;

    public TableTestPlace(String token) {
        this.name = token;
    }

    public String getName() {
        return name;
    }

    public static class Tokenizer implements PlaceTokenizer<TableTestPlace> {
        @Override
        public String getToken(TableTestPlace place) {
            return place.getName();
        }

        @Override
        public TableTestPlace getPlace(String token) {
            return new TableTestPlace(token);
        }
    }
}
