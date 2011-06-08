package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

public class PercentBar extends Composite implements HasField<Double>{
	
	private Grid grid;
	private AbsolutePanel bar;
	private ColorRange[] colors;
	
	public PercentBar() {
		grid = new Grid(1,2);
		grid.setCellPadding(0);
		grid.setCellSpacing(0);
		DOM.setStyleAttribute(grid.getElement(),"background", "transparent");
		bar = new AbsolutePanel();
		initWidget(grid);
		grid.setWidget(0, 0, bar);
		bar.setHeight("12px");
		DOM.setStyleAttribute(bar.getElement(), "border", "1px solid black");
	}
	
	public void setWidth(int width) {
		bar.setWidth(width-37+"px");
		grid.setWidth(width+"px");
	}
	
	public void setPercent(Double percent) {
		AbsolutePanel panel;
		
		panel = new AbsolutePanel();
		bar.clear();
		bar.add(panel);
		panel.setHeight("100%");
		panel.setWidth(percent.intValue()+"%");
		for(int i = 0; i < colors.length; i++) {
			if(percent < colors[i].getThreshHold()) {
				DOM.setStyleAttribute(panel.getElement(), "background", colors[i].getColor());
				break;
			}
		}
		grid.setText(0, 1, NumberFormat.getFormat("###0.0").format(percent)+"%");
		grid.getCellFormatter().setStyleName(0, 1, "ScreenLabel TableWidget");
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasAlignment.ALIGN_RIGHT);
		
	}
	
	public void setColors(ColorRange... colors) {
		this.colors = colors;
	}
	
	public ColorRange[] getColors() {
		return colors;
	}
		

	public static class ColorRange {
		int threshHold;
		String color;
		
		public ColorRange(int threshHold, String color) {
			this.threshHold = threshHold;
			this.color = color;
		}
		
		public int getThreshHold() {
			return threshHold;
		}
		
		public String getColor() {
			return color;
		}
	}


	@Override
	public Field<Double> getField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setField(Field<Double> field) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addException(LocalizedException exception) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearExceptions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setQueryMode(boolean query) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkValue() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getQuery(ArrayList<QueryData> list, String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<LocalizedException> getExceptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enable(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Double getFieldValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFieldValue(Double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getWidgetValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addExceptionStyle(String style) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeExceptionStyle(String style) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler<Double> handler) {
		// TODO Auto-generated method stub
		return null;
	}
}
