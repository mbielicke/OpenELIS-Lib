package org.openelis.test.client.textbox;

import com.google.gwt.user.client.ui.IsWidget;

public interface TextBoxTestView extends IsWidget {

	public void setField(String value);
	public void setCase(String value);
	public void setEnabled(String value);
	public void setAlignment(String value);
	public void setQueryMode(String value);
	public void setMask(String value);
	public void setPattern(String value);
	public void setRequired(String value);
	public void setValue(String value);
	public void setMaxLength(Integer value);
	
	public String getField();
	public String getCase();
	public String getEnabled();
	public String getAlignment();
	public String getQueryMode();
	public String getMask();
	public String getPattern();
	public String getRequired();
	public String getValue();
	public Integer getMaxLength(Integer value);
	
}
