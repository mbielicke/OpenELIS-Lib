package org.openelis.interfaces;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.RPCException;
import org.openelis.util.ValidationException;
import org.apache.log4j.Category;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract parent class for all the action classes in inmsp project. This class
 * applies the Command design pattern and Template design pattern.
 * 
 * @author fyu
 */
public abstract class AbstractAction implements IAction {
    protected static Category log = Category.getInstance(AbstractAction.class.getName());

    /**
     * Constructor.
     * 
     * @see java.lang.Object#Object()
     */
    public AbstractAction() {
    }

    /**
     * Execute user request. This method contains work flow algothrim for
     * action. Subclass needs to provide the implementation if needed.
     * 
     * @param inmspRequest
     * @throws IOException
     */
    public abstract void execute(FormRPC form) throws SQLException,
                                              IOException,
                                              ValidationException,
                                              RPCException;

    public void execute(HttpServletRequest req, HttpServletResponse resp) throws SQLException,
                                                                         IOException,
                                                                         ValidationException {
    }

    /**
     * Perform update.
     * 
     * @param inmspRequest
     */
    public abstract boolean hasPermission(FormRPC form) throws SQLException,
                                                       IOException,
                                                       ValidationException;
}
