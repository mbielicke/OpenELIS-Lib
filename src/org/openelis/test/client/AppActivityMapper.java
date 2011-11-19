package org.openelis.test.client;

import org.openelis.test.client.dropdown.DropdownTestActivity;
import org.openelis.test.client.dropdown.DropdownTestPlace;
import org.openelis.test.client.table.TableTestActivity;
import org.openelis.test.client.table.TableTestPlace;
import org.openelis.test.client.textbox.TextBoxTestActivity;
import org.openelis.test.client.textbox.TextBoxTestPlace;
import org.openelis.test.client.tree.TreeTestActivity;
import org.openelis.test.client.tree.TreeTestPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class AppActivityMapper implements ActivityMapper {
	
	ClientFactory cf;
	
	public AppActivityMapper(ClientFactory cf){
		super();
		this.cf = cf;
	}

	@Override
	public Activity getActivity(Place place) {
		if(place instanceof IndexPlace)
			return new IndexActivity(cf);
		else if(place instanceof TextBoxTestPlace)
			return new TextBoxTestActivity();
		else if(place instanceof DropdownTestPlace)
			return new DropdownTestActivity();
		else if(place instanceof TableTestPlace)
			return new TableTestActivity();
		else if(place instanceof TreeTestPlace)
			return new TreeTestActivity();
		return null;
	}

}
