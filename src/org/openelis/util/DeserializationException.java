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

public class DeserializationException extends Exception {
    int lineNumber = -1;

    /** Constructs an empty DeserializationException.
      */
    private DeserializationException() {
        super();
    }

    /** Constructs a DeserializationException with the descriptive message
      * <b>string</b> and <b>lineNumber</b>, the line number on which
      * the error occurred.
      */
    public DeserializationException(String string, int lineNumber) {
        super(string);
        this.lineNumber = lineNumber;
    }

    /** Returns the line number at which the DeserializationException
      * occurred.
      */
    public int lineNumber() {
        return lineNumber;
    }
}
