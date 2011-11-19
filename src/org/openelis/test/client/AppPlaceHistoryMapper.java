package org.openelis.test.client;

import org.openelis.test.client.dropdown.DropdownTestPlace;
import org.openelis.test.client.table.TableTestPlace;
import org.openelis.test.client.textbox.TextBoxTestPlace;
import org.openelis.test.client.tree.TreeTestPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({IndexPlace.Tokenizer.class, 
	             TextBoxTestPlace.Tokenizer.class, 
	             DropdownTestPlace.Tokenizer.class,
	             TableTestPlace.Tokenizer.class,
	             TreeTestPlace.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}
