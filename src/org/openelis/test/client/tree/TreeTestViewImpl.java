package org.openelis.test.client.tree;

import java.util.ArrayList;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.AutoComplete;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.Label;
import org.openelis.gwt.widget.PercentBar;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.calendar.Calendar;
import org.openelis.gwt.widget.table.AutoCompleteCell;
import org.openelis.gwt.widget.table.CalendarCell;
import org.openelis.gwt.widget.table.CheckBoxCell;
import org.openelis.gwt.widget.table.DropdownCell;
import org.openelis.gwt.widget.table.ImageCell;
import org.openelis.gwt.widget.table.LabelCell;
import org.openelis.gwt.widget.table.PercentCell;
import org.openelis.gwt.widget.table.Row;
import org.openelis.gwt.widget.table.Table;
import org.openelis.gwt.widget.table.TextBoxCell;
import org.openelis.gwt.widget.tree.Column;
import org.openelis.gwt.widget.tree.Tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class TreeTestViewImpl extends Screen implements TreeTestView {
	
	protected Tree test;
	protected TextBox<Integer> rows,rowHeight,width;
	protected CheckBox enabled,multiSelect,query,hasHeader,fixScroll;
	protected Dropdown<String> vscroll,hscroll;
	protected Table columns;
	protected Button set,add,remove,addRow,removeRow;
	
	public TreeTestViewImpl() {
		super((ScreenDefInt)GWT.create(TreeTestDef.class));
		initialize();
		initializeDropdowns();
	}
	
	public void initialize() {
		ArrayList<Row> colModel;
		Row row;
		
		test = (Tree)def.getWidget("test");
		test.setEnabled(true);
		
		rows = (TextBox<Integer>)def.getWidget("rows");
		rows.setEnabled(true);
		rows.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				test.setVisibleRows(event.getValue());
			}
		});
		
		rows.setValue(10);
		
		rowHeight = (TextBox<Integer>)def.getWidget("rowHeight");
		rowHeight.setEnabled(true);
		rowHeight.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			public void onValueChange(ValueChangeEvent<Integer> event) {
				test.setRowHeight(event.getValue());
			}
		});
		
		width = (TextBox<Integer>)def.getWidget("width");
		width.setEnabled(true);
		width.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			public void onValueChange(ValueChangeEvent<Integer> event) {
				test.setWidth(event.getValue());
			}
		});
		
		enabled = (CheckBox)def.getWidget("enabled");
		enabled.setEnabled(true);
		enabled.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setEnabled("Y".equals(event.getValue()));
			}
		});
		
		enabled.setValue("Y");
		
		multiSelect = (CheckBox)def.getWidget("multiSelect");
		multiSelect.setEnabled(true);
		multiSelect.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setAllowMultipleSelection("Y".equals(event.getValue()));
			}
		});
		
		hasHeader = (CheckBox)def.getWidget("hasHeader");
		hasHeader.setEnabled(true);
		hasHeader.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setHeader("Y".equals(event.getValue()));
			}
		});
		
		hasHeader.setValue("Y");
		
		fixScroll = (CheckBox)def.getWidget("fixScroll");
		fixScroll.setEnabled(true);
		fixScroll.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setFixScrollbar("Y".equals(event.getValue()));
			}
		});
		
		fixScroll.setValue("Y");
		
		vscroll = (Dropdown<String>)def.getWidget("vscroll");
		vscroll.setEnabled(true);
		vscroll.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setVerticalScroll(Tree.Scrolling.valueOf(event.getValue()));
			}
		});
		
		
		hscroll = (Dropdown<String>)def.getWidget("hscroll");
		hscroll.setEnabled(true);
		hscroll.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setHorizontalScroll(Tree.Scrolling.valueOf(event.getValue()));
			}
		});
		
		columns = (Table)def.getWidget("columns");
		columns.setEnabled(true);
		
		set = (Button)def.getWidget("set");
		set.setEnabled(true);
		set.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setColumns((ArrayList<Row>)columns.getModel());
			}
		});
		
		add = (Button)def.getWidget("add");
		add.setEnabled(true);
		add.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				columns.addRow();
			}
		});
		
		remove = (Button)def.getWidget("remove");
		remove.setEnabled(true);
		remove.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(columns.isAnyRowSelected())
					columns.removeRowAt(columns.getSelectedRow());
			}
		});
		
		addRow = (Button)def.getWidget("addRow");
		addRow.setEnabled(true);
		addRow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				test.addNode();
			}
		});
		
		removeRow = (Button)def.getWidget("removeRow");
		removeRow.setEnabled(true);
		removeRow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(test.isAnyNodeSelected())
					test.removeNodeAt(test.getSelectedNode());
			}
		});
		
		 colModel = new ArrayList<Row>();
		 colModel.add(new Row("Col 1",75,"textbox","Y","N","Y"));
		 colModel.add(new Row("Col 2",75,"textbox","Y","N","Y"));
		 colModel.add(new Row("Col 3",75,"textbox","Y","N","Y"));
		 colModel.add(new Row("Col 4",75,"textbox","Y","N","Y"));
		 colModel.add(new Row("Col 5",75,"textbox","Y","N","Y"));
		 colModel.add(new Row("Col 6",75,"textbox","Y","N","Y"));
		 
		 columns.setModel(colModel);
		 
		 setColumns(colModel);
		
		
		
	}
	
	protected void initializeDropdowns() {
		ArrayList<Item<String>> model = new ArrayList<Item<String>>();
		
		model.add(new Item<String>("ALWAYS","ALWAYS"));
		model.add(new Item<String>("NEVER","NEVER"));
		model.add(new Item<String>("AS_NEEDED","AS_NEEDED"));
		
		vscroll.setModel(model);
		hscroll.setModel(model);
		
		vscroll.setValue("AS_NEEDED");
		hscroll.setValue("AS_NEEDED");
		
		model = new ArrayList<Item<String>>();
		model.add(new Item<String>("textbox","TextBox"));
		model.add(new Item<String>("label","Label"));
		model.add(new Item<String>("dropdown","Dropdown"));
		model.add(new Item<String>("check","CheckBox"));
		model.add(new Item<String>("auto","AutoComplete"));
		model.add(new Item<String>("calendar","Calendar"));
		model.add(new Item<String>("image","Image"));
		model.add(new Item<String>("percent","PercentBar"));
		
		((Dropdown)columns.getColumnWidget(2)).setModel(model);
	}
	
	protected void setColumns(ArrayList<Row> colModel) {
		ArrayList<Column> columns;
		Column column;
		
		columns = new ArrayList<Column>();
		
		for(Row row : colModel) {
			column = test.addColumn(null, (String)row.getCell(0));
			column.setWidth((Integer)row.getCell(1));
			String editor = (String)row.getCell(2);
			if("textbox".equals(editor)) {
				TextBox<String> textbox = new TextBox<String>();
				column.setCellRenderer(new TextBoxCell<String>(textbox));
			} else if("label".equals(editor)) {
				Label<String> label = new Label<String>();
				column.setCellRenderer(new LabelCell<String>(label));
			} else if("dropdown".equals(editor)){
				Dropdown<String> dropdown = new Dropdown<String>();
				column.setCellRenderer(new DropdownCell<String>(dropdown));
			} else if("check".equals(editor)) {
				CheckBox check = new CheckBox();
				column.setCellRenderer(new CheckBoxCell(check));
			} else if("auto".equals(editor)) {
				AutoComplete auto = new AutoComplete();
				column.setCellRenderer(new AutoCompleteCell(auto));
			} else if("calendar".equals(editor)) {
				Calendar cal = new Calendar();
				column.setCellRenderer(new CalendarCell(cal));
			} else if("image".equals(editor)) {
				column.setCellRenderer(new ImageCell());
			} else if("percent".equals(editor)) {
				PercentBar bar = new PercentBar();
				column.setCellRenderer(new PercentCell(bar));
			}
			column.setResizable("Y".equals(row.getCell(3)));
			column.setRequired("Y".equals(row.getCell(4)));
			column.setEnabled("Y".equals(row.getCell(5)));
			columns.add(column);
		}
		
		test.setColumns(columns);
	}


}
