package org.openelis.interfaces;

import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.RPCException;
import org.openelis.util.ValidationException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * The parent class for all Action subclass.
 * 
 * @author fyu
 */
public interface IAction {
    public void execute(FormRPC form) throws SQLException,
                                     IOException,
                                     ValidationException,
                                     RPCException;

    public boolean hasPermission(FormRPC form) throws SQLException,
                                              IOException,
                                              ValidationException;

    public AbstractField query(FormRPC form) throws SQLException,
                                     IOException,
                                     ValidationException,
                                     RPCException;
}
