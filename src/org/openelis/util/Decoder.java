package org.openelis.util;

import java.math.BigDecimal;

/**
 * A static class that decodes objects to the requested type, if possible.
 */
public class Decoder {
    /**
     * Private constructor
     */
    private Decoder() {
    }

    /**
     * Decodes the passed argument to String.
     */
    public static String decodeString(Object obj) {
        String value;
        value = null;
        if (obj != null) {
            value = obj.toString().trim();
            if (value.length() == 0)
                value = null;
        }
        return value;
    }

    /**
     * Decodes the passed argument to Short.
     */
    public static Short decodeShort(Object obj) {
        Short value;
        value = null;
        if (obj == null || obj instanceof Short)
            value = (Short)obj;
        else
            value = Short.decode(obj.toString());
        return value;
    }

    /**
     * Decodes the passed argument to Integer.
     */
    public static Integer decodeInteger(Object obj) {
        Integer value;
        value = null;
        if (obj == null || obj instanceof Integer)
            value = (Integer)obj;
        else
            value = Integer.decode(obj.toString());
        return value;
    }

    /**
     * Decodes the passed argument to Float.
     */
    public static Float decodeFloat(Object obj) {
        Float value;
        value = null;
        if (obj == null || obj instanceof Float)
            value = (Float)obj;
        else
            value = new Float(obj.toString());
        return value;
    }

    /**
     * Decodes the passed argument to Double.
     */
    public static Double decodeDouble(Object obj) {
        Double value;
        value = null;
        if (obj == null || obj instanceof Double)
            value = (Double)obj;
        else
            value = new Double(obj.toString());
        return value;
    }

    /**
     * Decodes the passed argument to BigDecimal.
     */
    public static BigDecimal decodeBigDecimal(Object obj) {
        BigDecimal value;
        value = null;
        if (obj == null || obj instanceof BigDecimal)
            value = (BigDecimal)obj;
        else
            value = new BigDecimal(obj.toString());
        return value;
    }

    /**
     * Decodes the passed argument to BigDecimal with fixed 2 digit scale.
     */
    public static BigDecimal decodeMoney(Object obj) {
        String temp;
        BigDecimal value;
        value = null;
        if (obj != null) {
            if (obj instanceof BigDecimal) {
                value = (BigDecimal)obj;
            } else {
                temp = obj.toString().trim();
                value = new BigDecimal(temp);
            }
            //
            // round it to cents.
            //
            if (value.scale() != 2)
                value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return value;
    }

    /**
     * Decodes the passed argument to Datetime with specified start,end range.
     */
    public static Datetime decodeDatetime(Object obj, byte start, byte end) {
        Datetime value;
        value = null;
        if (obj != null) {
            if (obj instanceof Datetime) {
                value = (Datetime)obj;
                if (value.getStartCode() != start || value.getEndCode() != end)
                    value = value.extend(start, end);
            } else {
                value = new Datetime(start, end, obj);
            }
        }
        return value;
    }

    /**
     * Decodes the passed argument to String.
     */
    public static String decodeInterval(Object obj) {
        return decodeString(obj);
    }
}
