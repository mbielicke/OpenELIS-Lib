package org.openelis.test.client.table;

import java.util.ArrayList;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.AutoComplete;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CollapsePanel;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.Item;
import org.openelis.gwt.widget.Label;
import org.openelis.gwt.widget.PercentBar;
import org.openelis.gwt.widget.Selection;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.calendar.Calendar;
import org.openelis.gwt.widget.table.AutoCompleteCell;
import org.openelis.gwt.widget.table.CalendarCell;
import org.openelis.gwt.widget.table.CheckBoxCell;
import org.openelis.gwt.widget.table.Column;
import org.openelis.gwt.widget.table.DropdownCell;
import org.openelis.gwt.widget.table.ImageCell;
import org.openelis.gwt.widget.table.LabelCell;
import org.openelis.gwt.widget.table.PercentCell;
import org.openelis.gwt.widget.table.Row;
import org.openelis.gwt.widget.table.SelectionCell;
import org.openelis.gwt.widget.table.Table;
import org.openelis.gwt.widget.table.TextBoxCell;
import org.openelis.gwt.widget.table.TimeCell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class TableScreen extends Screen {
	
	protected Table test;
	protected TextBox<Integer> rows,rowHeight,width;
	protected TextBox<String> css;
	protected CheckBox enabled,multiSelect,query,hasHeader,fixScroll;
	protected Selection<String> vscroll,hscroll,logLevel;
	protected Table columns;
	protected Button set,add,remove,addRow,removeRow;
	
	public TableScreen() {
		super((ScreenDefInt)GWT.create(TableDef.class));
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				postConstructor();
			}
		});

	}
	
	private void postConstructor() {
		initialize();
		initializeDropdowns();
	}
	
	private void initialize() {
		ArrayList<Row> colModel;
		ArrayList<Item<String>> model;
		Row row;

		test = (Table)def.getWidget("test");
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

		query = (CheckBox)def.getWidget("query");
		query.setEnabled(true);
		query.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setQueryMode("Y".equals(event.getValue()));
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

		vscroll = (Selection<String>)def.getWidget("vscroll");
		vscroll.setEnabled(true);
		vscroll.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setVerticalScroll(Table.Scrolling.valueOf(event.getValue()));
			}
		});


		hscroll = (Selection<String>)def.getWidget("hscroll");
		hscroll.setEnabled(true);
		hscroll.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setHorizontalScroll(Table.Scrolling.valueOf(event.getValue()));
			}
		});
		
		css = (TextBox<String>)def.getWidget("css");
		css.setEnabled(true);
		css.setValue(test.getStyleName());
		css.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				test.setStyleName(event.getValue());
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
				test.addRow();
			}
		});

		removeRow = (Button)def.getWidget("removeRow");
		removeRow.setEnabled(true);
		removeRow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(test.isAnyRowSelected())
					test.removeRowAt(test.getSelectedRow());
			}
		});
		
		
		model = new ArrayList<Item<String>>();
		model.add(new Item<String>("textbox","TextBox"));
		model.add(new Item<String>("label","Label"));
		model.add(new Item<String>("dropdown","Dropdown"));
		model.add(new Item<String>("check","CheckBox"));
		model.add(new Item<String>("auto","AutoComplete"));
		model.add(new Item<String>("calendar","Calendar"));
		model.add(new Item<String>("image","Image"));
		model.add(new Item<String>("percent","PercentBar"));
		model.add(new Item<String>("time","Time"));
		model.add(new Item<String>("select","Selection"));
		
		((Dropdown)columns.getColumnWidget(2)).setModel(model);

		colModel = new ArrayList<Row>();
		colModel.add(new Row("Col 1",75,"textbox","N","N","Y","N","Y"));
		colModel.add(new Row("Col 2",75,"textbox","N","N","Y","N","Y"));
		colModel.add(new Row("Col 3",75,"textbox","N","N","Y","N","Y"));
		colModel.add(new Row("Col 4",75,"textbox","N","N","Y","N","Y"));
		colModel.add(new Row("Col 5",75,"textbox","N","N","Y","N","Y"));
		colModel.add(new Row("Col 6",75,"textbox","N","N","Y","N","Y"));

		columns.setModel(colModel);

		setColumns(colModel);
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				((CollapsePanel)def.getWidget("collapsePanel")).open();
			}
		});

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
	}
	
	protected void setColumns(ArrayList<Row> colModel) {
		ArrayList<Column> columns;
		Column column;
		int width = 0;
		
		columns = new ArrayList<Column>();
		
		for(Row row : colModel) {
			column = test.addColumn(null, (String)row.getCell(0));
			column.setWidth((Integer)row.getCell(1));
			width += (Integer)row.getCell(1);
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
			} else if("time".equals(editor)) {
				column.setCellRenderer(new TimeCell());
			} else if("select".equals(editor)) {
				Selection<String> sel = new Selection<String>();
				Table t = new Table();
				t.addColumn();
				sel.setPopupContext(t);
				ArrayList<Item<String>> model = new ArrayList<Item<String>>();
				model.add(new Item<String>("1","Option 1"));
				model.add(new Item<String>("2","Option 2"));
				model.add(new Item<String>("3","Option 3"));
				sel.setModel(model);
				column.setCellRenderer(new SelectionCell<String>(sel));
				sel.setMultiSelect(true);
			}
			column.setFilterable("Y".equals(row.getCell(3)));
			column.setSortable("Y".equals(row.getCell(4)));
			column.setResizable("Y".equals(row.getCell(5)));
			column.setRequired("Y".equals(row.getCell(6)));
			column.setEnabled("Y".equals(row.getCell(7)));
			columns.add(column);
		}
		
		test.setColumns(columns);
		
		if(this.width.getValue() == null)
			test.setWidth(width);
	}

}
