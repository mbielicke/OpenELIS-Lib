package org.openelis.gwt.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Panel;

public class AtoZButtons extends ButtonGroup {
	@UiTemplate("AtoZ.ui.xml")
	interface AtoZUiBinder extends UiBinder<Panel, AtoZButtons>{};
	public static final AtoZUiBinder uiBinder = GWT.create(AtoZUiBinder.class);
	
	@UiTemplate("AtoZX2.ui.xml")
	interface AtoZX2UiBinder extends UiBinder<Panel,AtoZButtons>{};
	public static final AtoZX2UiBinder x2UiBinder = GWT.create(AtoZX2UiBinder.class); 
	
	
	public @UiConstructor AtoZButtons(boolean multiColumn) {
		if(!multiColumn)
			setButtons(uiBinder.createAndBindUi(this));
		else
			setButtons(x2UiBinder.createAndBindUi(this));
	}

}
