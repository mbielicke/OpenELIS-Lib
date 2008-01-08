package org.openelis.gwt.client.widget;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import org.openelis.gwt.client.screen.ScreenBase;
import org.openelis.gwt.client.screen.ScreenWidget;
import org.openelis.gwt.client.widget.table.TableCellWidget;
import org.openelis.gwt.client.widget.table.TableManager;
import org.openelis.gwt.common.data.AbstractField;

import java.util.HashMap;

/**
 * WidgetMap is a class that is used by a Screen Widget to
 * hold references to all widgets that are available to an 
 * application.  A WidgetMap should be created in an onModuleLoad 
 * method of an entry point class.  Load the map with widgets created with
 * the no-arg default constructor and either the default TAG_NAME defined 
 * int he widget, from one of the static strings in this class or a custom one.
 * @author tschmidt
 *
 */
public class WidgetMap {

    public static final String PANEL_HORIZONTAL = "panel-horizontal", // ScreenHPVP
                    PANEL_VERTICAL = "panel-vertical", // ScreenHPVP
                    PANEL_TABLE = "panel-table", // ScreenTablePanel
                    PANEL_ABSOLUTE = "panel-absolute", // ScreenAbsolute
                    PANEL_DECK = "panel-deck", // ScreenDeck
                    PANEL_DISCLOSURE = "panel-disclosure", // ScreenDisclosure
                    PANEL_DOCK = "panel-dock", // ScreenDock
                    PANEL_HSPLIT = "panel-hsplit", // ScreenSplit
                    PANEL_VSPLIT = "panel-vsplit", // ScreenSplit
                    PANEL_STACK = "panel-stack", // ScreenStack
                    PANEL_TAB = "panel-tab", // ScreenTab
                    PANEL_TITLED = "panel-titled", //ScrennTitledPanel
                    APP_MESSAGE = "appMessage", //ScreenAppMessage
                    AUTO = "auto", // ScreenAuto
                    BUTTON = "button", // ScreenButton
                    BUTTON_PANEL = "buttonPanel", // ScreenButtonPanel
                    CALENDAR = "calendar", // ScreenCalendar
                    CHECKBOX = "check", // ScreenCheck
                    CONTSTANT = "const", // ScreenConstant
                    DRAG_SELECT = "dragselect", // ScreenDragSelect
                    ERROR = "error", // ScreenError
                    HTML = "html", // ScreenHTML
                    IMAGE = "image", // ScreenImage
                    LABEL = "label", // ScreenLabel
                    MASKED_BOX = "maskedbox", // ScreenMaskedBox
                    MENU_BAR = "menubar", // ScreenMenuBar
                    MENU_LABEL = "menulabel", //ScreenMenuLabel
                    MENU_PANEL = "menupanel", //ScreenMenuPanel
                    OPTION_LIST = "option", // ScreenOption
                    PASSWORD = "password", // ScreenPassword
                    RADIO_BUTTON = "radio", // ScreenRadio
                    TAB_BROWSER = "tabbrowser", // ScreenTabBrowser
                    TABLE = "table", // ScreenTable
                    TEXT = "text", // ScreenText
                    TEXT_AREA = "textarea", // ScreenTextArea
                    TEXBOX = "textbox", // ScreenTexBox
                    TOGGLE_BUTTON = "toggle", //ScreenToggleButton
                    TREE = "tree", // ScreenTree
                    DRAGLIST = "draglist", // ScreenDragList
                    WINBROWSER = "winbrowser", //ScreenWindowBrowser
                    TABLE_CALENDAR = "table-calendar", // TableCalendar
                    TABLE_CHECKBOX = "table-check", // TableCheck
                    TABLE_COLLECTION = "table-collection", // TableCollection
                    TABLE_IMAGE = "table-image", // TableImage
                    TABLE_LABEL = "table-label", // TableLabel
                    TABLE_LINK = "table-link", // TableLink
                    TABLE_MASKED_BOX = "table-maskedbox", // TableMaskedBox
                    TABLE_OPTION_LIST = "table-option", // TableOption
                    TABLE_TEXTBOX = "table-textbox", // TableTextBox
                    RPC_CHECKBOX = "rpc-check", // CheckField
                    RPC_COLLECTION = "rpc-collection", // CollectionField
                    RPC_DATE = "rpc-date", // DateField
                    RPC_NUMBER = "rpc-number", // NumberField
                    RPC_OPTION = "rpc-option", // OptionField
                    RPC_STRING = "rpc-string", // StringField
                    RPC_TABLE = "rpc-table", // TableField
                    RPC_PAGED_TREE = "rpc-tree", // PagedTreeField
                    RPC_QUERY_CHECK = "rpc-queryCheck", // QueryCheckField
                    RPC_QUERY_DATE = "rpc-queryDate", // QueryDateField
                    RPC_QUERY_NUMBER = "rpc-queryNumber", // QueryNumberField
                    RPC_QUERY_OPTION = "rpc-queryOption", // QueryOptionField
                    RPC_QUERY_STRING = "rpc-queryString", // QueryStringField
    				LEFT_MENU_PANEL = "aToZ", //ScreenLeftMenuPanel
    				AUTO_DROPDOWN = "autoDropdown", //ScreenAutoDropdown
                    APP_BUTTON = "appButton"; //ScreenAppButton

    private HashMap widgets = new HashMap();

    /**
     * Returns the object mapped to the key passed in.
     * @param key
     * @return
     */
    public Object get(String key) {
        return widgets.get(key);
    }

    /**
     * Returns a new Widget created by calling getInstance(node, screen) for the widget 
     * who's tag in the passed node maps. 
     * @param node
     * @param screen
     * @return
     */
    public Widget getWidget(Node node, ScreenBase screen) {
        String widName = node.getNodeName();
        if (widName.equals("panel"))
            widName += "-" + node.getAttributes()
                                 .getNamedItem("layout")
                                 .getNodeValue();
        return (Widget)((ScreenWidget)widgets.get(widName)).getInstance(node,
                                                                        screen);
    }
    
    /**
     * Returns a TableCellWidget from the passed in node's tag name.
     * @param node
     * @return
     */
    public TableCellWidget getCellWidget(Node node) {
        String widName = "table-" + node.getNodeName();
        return (TableCellWidget)((TableCellWidget)widgets.get(widName)).getInstance(node);
    }

    /** 
     * Returns the AbstractField from the passed in node's tag name.
     * @param node
     * @return
     */
    public AbstractField getField(Node node) {
        String fName = "rpc-" + node.getNodeName();
        return (AbstractField)((AbstractField)widgets.get(fName)).getInstance(node);
    }

    /**
     * Returns a TableManager from from the name passed in to the method 
     * @param name
     * @return
     */
    public TableManager getTableManager(String name) {
        return (TableManager)widgets.get(name);
    }

    /**
     * Adds an object to the map to make it available to an application.
     * @param name
     * @param obj
     */
    public void addWidget(String name, Object obj) {
        widgets.put(name, obj);
    }

    /**
     * Removes the mapped widget from the name passed in 
     * @param name
     */
    public void removeWidget(String name) {
        widgets.remove(name);
    }

}
