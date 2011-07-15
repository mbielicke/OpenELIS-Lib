package org.openelis.gwt.widget;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;

/**
 *  This widget is used to represent a Percentage as a colored portion of a box to the percentage set
 *  followed by a  text value of the percentage. 
 *
 */
public class PercentBar extends Composite {
	/**
	 * Base widget set for this composite
	 */
	private Grid grid;
	/**
	 * The Bar that will be filled in to show the pecentage
	 */
	private AbsolutePanel bar;
	/**
	 * This is an array of color thresholds for determining what color to fill the bar with 
	 */
	private ColorRange[] colors;
	
	/**
	 * No-Arg constructor
	 */
	public PercentBar() {
		grid = new Grid(1,2);
		grid.setCellPadding(0);
		grid.setCellSpacing(0);
		DOM.setStyleAttribute(grid.getElement(),"background", "transparent");
		bar = new AbsolutePanel();
		initWidget(grid);
		grid.setWidget(0, 0, bar);
		grid.getCellFormatter().setWidth(0,0, "100%");
		bar.setHeight("12px");
		DOM.setStyleAttribute(bar.getElement(), "border", "1px solid black");
		setStyleName("PercentBar");
	}
	
	/**
	 * Sets a fixes width to the bar.
	 * @param width
	 */
	public void setBarWidth(int width) {
		bar.setWidth(width+"px");
	}
	
	/**
	 * This method will fill the box and set the text to the value passed in as percent.
	 * @param percent
	 */
	public void setPercent(Double percent) {
		AbsolutePanel panel;
		ColorRange color = null;
		
		if(percent == null) 
			percent = 0.0;
		
		panel = new AbsolutePanel();
		bar.clear();
		bar.add(panel);
		panel.setHeight("100%");
		panel.setWidth(percent.intValue()+"%");
		/*
		 * Determines which color to use when filling in the bar.
		 */
		for(ColorRange range : colors) {
			if(percent < range.getThreshHold()) {
				color = range;
				break;
			}
		}
		if(color == null)
			color = colors[colors.length-1];
		DOM.setStyleAttribute(panel.getElement(), "background", color.getColor());
		grid.setText(0, 1, NumberFormat.getFormat("###0.0").format(percent)+"%");
		grid.getCellFormatter().setStyleName(0, 1, "ScreenLabel TableWidget");
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasAlignment.ALIGN_RIGHT);
		
	}
	
	/**
	 * Sets the Range of colors to use for this Percent bar.
	 * @param colors
	 */
	public void setColors(ColorRange... colors) {
		this.colors = colors;
	}
	
	/**
	 * Returns the Range of colors used by this Percent bar.
	 * @return
	 */
	public ColorRange[] getColors() {
		return colors;
	}
		

	/**
	 * Inner class used to hold a a pair of values to represent a color range. 
	 *
	 */
	public static class ColorRange {
		/**
		 * Max value for this range
		 */
		int threshHold;
		/**
		 * Color of the range as valid CSS value
		 */
		String color;
		
		/**
		 * Constructor that takes a threshold and the color to be used for this range.
		 * @param threshHold
		 * @param color
		 */
		public ColorRange(int threshHold, String color) {
			this.threshHold = threshHold;
			this.color = color;
		}
		
		/**
		 * Returns the max value for this range.
		 * @return
		 */
		public int getThreshHold() {
			return threshHold;
		}
		
		/**
		 * Returns the color to be used for this range.
		 * @return
		 */
		public String getColor() {
			return color;
		}
	}
}
