package org.openelis.gwt.screen;

import java.io.Serializable;
import java.util.ArrayList;

import org.openelis.ui.common.data.Query;
import org.openelis.gwt.widget.AppButton;
import org.openelis.gwt.widget.table.TableDataRow;
import org.openelis.gwt.widget.table.TableRow;
import org.openelis.gwt.widget.table.TableWidget;
import org.openelis.gwt.widget.table.event.BeforeCellEditedEvent;
import org.openelis.gwt.widget.table.event.BeforeCellEditedHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;

/**
 * This class is used by screens to manage paged queries.
 * 
 * To use this class, you must instantiate and override three methods that
 * provide query execution, screen data fetching, and converting a list of
 * returned data to a table model.
 * 
 * <code>
 * nav = new ScreenNavigator(screenDefinition) {
 *    public void executeQuery(final Query query) {
 *       service.callList(...);
 *    }
 *    public boolean fetch(RPC entry) {
 *       return fetchById((entry==null)?null:((IdNameVO)entry).getId());
 *    }
 *    public ArrayList<TableDataRow> getModel() {
 *       result = nav.getQueryResult();
 *       model = new ArrayList<TableDataRow>();
 *       if (result != null)
 *           for (IdNameVO entry : result)
 *       return model;
 *    }
 * }
 * </code>
 * 
 * For all screen queries, call nav.setQuery(query).
 */
public abstract class ScreenNavigator<T extends Serializable> {
    protected int         selection, oldPage;
    protected boolean     byRow, enable;
    protected ArrayList   result;
    protected Query       query;
    protected TableWidget table;
    protected AppButton   atozNext, atozPrev;

    public ScreenNavigator(ScreenDefInt def) {
        oldPage = -1;
        selection = -1;
        initialize(def);
    }

    protected void initialize(ScreenDefInt def) {
        table = (TableWidget)def.getWidget("atozTable");
        if (table != null) {
            table.addBeforeSelectionHandler(new BeforeSelectionHandler<TableRow>() {
                public void onBeforeSelection(BeforeSelectionEvent<TableRow> event) {
                    // since we don't know if the fetch will succeed, we are
                    // going
                    // cancel this selection and select the table row ourselves.
                    if (enable)
                        select(event.getItem().modelIndex);
                    event.cancel();
                }
            });
            table.addBeforeCellEditedHandler(new BeforeCellEditedHandler() {
                public void onBeforeCellEdited(BeforeCellEditedEvent event) {
                    event.cancel();
                }
            });
            // we don't want the table to get focus; we can still select because
            // we will get the onBeforeSelection event.
            table.enable(false);
        }

        atozNext = (AppButton)def.getWidget("atozNext");
        if (atozNext != null) {
            atozNext.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    setPage(query.getPage() + 1);
                }
            });
        }

        atozPrev = (AppButton)def.getWidget("atozPrev");
        if (atozPrev != null) {
            atozPrev.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    setPage(query.getPage() - 1);
                }
            });
        }
    }

    /**
     * Set the query parameters and starts the query execution.
     */
    public void setQuery(Query query) {
        oldPage = -1;
        byRow = false;

        this.query = query;
        executeQuery(query);
    }

    /**
     * Set the query result list.
     * 
     * @param result
     *        should be null to indicate no records were found.
     */
    public void setQueryResult(ArrayList result) {
        int row;

        enable(true);
        //
        // if next page failed, reset the query page # to the old page #
        //
        if (result == null && oldPage != -1) {
            query.setPage(oldPage);
            oldPage = -1;
            return;
        }

        row = 0;
        this.result = result;
        if (table != null)
            table.load(getModel());
        //
        // we are going back a page and we want to select the last row in
        // in the list
        //
        if (result != null && oldPage > query.getPage() && byRow) {
            row = result.size() - 1;
            byRow = false;
        }

        select(row);
    }

    public ArrayList getQueryResult() {
        return result;
    }

    /**
     * enable the table and next previous page buttons
     */
    public void enable(boolean enable) {
        if (atozNext != null)
            atozNext.enable(enable && result != null);
        if (atozPrev != null)
            atozPrev.enable(enable && result != null);
        this.enable = enable;
    }

    /**
     * Selects the next element in the result list. If at the end of result
     * list, attempt is made to fetch the next page.
     */
    public void next() {
        if (enable) {
            byRow = true;
            if (selection != -1)
                select(selection + 1);
        }
    }

    /**
     * Selects the previous element in the result list. If the current selection
     * is at the beginning of the list, attempt is made to fetch the previous
     * page.
     */
    public void previous() {
        if (enable) {
            byRow = true;
            if (selection != -1)
                select(selection - 1);
        }
    }

    /**
     * This method is called when the screen needs to update its data to
     * represent the selection. A null RPC parameter tells the screen to clear
     * its data.
     */
    public abstract boolean fetch(T entry);

    /**
     * This method is called when a new query needs to be executed. The screen
     * implementation will need to call the setQueryResult method once the
     * result is available.
     */
    public abstract void executeQuery(Query query);

    /**
     * Returns the table data model representing the query result.
     * 
     * @return model that is used to set the atoz table; This model cannot be null.
     */
    public abstract ArrayList<TableDataRow> getModel();

    /**
     * Select a row within the result set
     */
    protected void select(int row) {
        if (result == null || result.size() == 0) {
            try {
                fetch(null);
            } catch (Exception e) {
                // this should not happen since we are telling them
                // to clear their screen
                e.printStackTrace();
            } finally {
                selection = -1;
            }
            if (table != null)
                table.unselect(selection);
            if (atozNext != null)
                atozNext.enable(false);
            if (atozPrev != null)
                atozPrev.enable(false);
        } else if (row > result.size() - 1) {
            setPage(query.getPage() + 1);
        } else if (row < 0) {
            setPage(query.getPage() - 1);
        } else {
            if (fetch((T)result.get(row))) {
                selection = row;
                if (table != null)
                    table.selectRow(selection);
                if (atozNext != null)
                    atozNext.enable(true);
                if (atozPrev != null)
                    atozPrev.enable(true);
            } else {
                selection = -1;
                if (table != null)
                    table.unselect(selection);
            }
        }
    }

    /*
     * Sets the query page # for going forward/back and executes the query.
     * SetQueryResult resets the page # if we can't go forward.
     */
    protected void setPage(int page) {
        if (page < 0)
            return;

        enable(false);

        oldPage = query.getPage();
        query.setPage(page);

        executeQuery(query);
    }
}
