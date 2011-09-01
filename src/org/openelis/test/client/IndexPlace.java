package org.openelis.test.client;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class IndexPlace extends Place {

    private String indexName;

    public IndexPlace(String token) {
        this.indexName = token;
    }

    public String getIndexName() {
        return indexName;
    }

    public static class Tokenizer implements PlaceTokenizer<IndexPlace> {
        @Override
        public String getToken(IndexPlace place) {
            return place.getIndexName();
        }

        @Override
        public IndexPlace getPlace(String token) {
            return new IndexPlace(token);
        }
    }
	
}
