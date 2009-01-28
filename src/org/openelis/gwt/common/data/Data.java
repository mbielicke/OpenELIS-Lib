package org.openelis.gwt.common.data;

import java.io.Serializable;

/**
 * This interface is used to mark objects that can be used in 
 * Service interfaces that have been set up in the OpenELIS application.
 * The clone method is provided so that it can be called on the interface
 * itself and not just on the implementing class.
 * @author tschmidt
 *
 */
public interface Data extends Serializable, Cloneable {
    
    public Object clone();

}
