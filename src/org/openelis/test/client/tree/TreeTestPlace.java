package org.openelis.test.client.tree;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class TreeTestPlace extends Place {
    
	private String name;

    public TreeTestPlace(String token) {
        this.name = token;
    }

    public String getName() {
        return name;
    }

    public static class Tokenizer implements PlaceTokenizer<TreeTestPlace> {
        @Override
        public String getToken(TreeTestPlace place) {
            return place.getName();
        }

        @Override
        public TreeTestPlace getPlace(String token) {
            return new TreeTestPlace(token);
        }
    }
}
