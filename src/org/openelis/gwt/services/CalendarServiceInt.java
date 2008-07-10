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
package org.openelis.gwt.services;

import org.openelis.gwt.common.RPCException;

public interface CalendarServiceInt extends AppScreenServiceInt {
    
    public String getMonth(String month, String year, String date) throws RPCException;
    
    public String getMonthSelect(String month, String year) throws RPCException;

}
