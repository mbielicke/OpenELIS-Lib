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
