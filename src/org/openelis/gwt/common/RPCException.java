/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.common;

import java.io.Serializable;

public class RPCException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    private String msg;
    public String appMsg;

    public RPCException() {
        super();
    }

    public RPCException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }
}
