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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.util;

/**
 * Exception signalling that data is invalid. Classes throw this exception when
 * a change in data would render the underlying information base (database
 * record) invalid or inconsistent.
 */
public class ValidationException extends Exception {
    public ValidationException() {
        super();
    }

    public ValidationException(String msg) {
        super(msg);
    }
}
