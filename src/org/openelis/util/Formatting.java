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
 * This class used to perform various formatting operations
 */
import java.text.DecimalFormat;

public class Formatting {
    public static String doublePrecision(Double value, Short precision) {
        if (value == null)
            return "";
        StringBuffer format = new StringBuffer("0");
        if (precision.intValue() > 0)
            format.append(".");
        for (int i = 0; i < precision.intValue(); i++) {
            format.append("0");
        }
        DecimalFormat fmt = new DecimalFormat(format.toString());
        return fmt.format(value);
    }
}
