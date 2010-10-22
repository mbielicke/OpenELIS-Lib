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
package org.openelis.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

public class ReportUtil {
    
    protected String            baseURL, title;
    protected URL               reportURL;
    public Vector               reportParameters;

    protected Object[]          componentValueList;

    public static final String NAME_KEY          = "name",
                               PROMPT_KEY        = "prompt",
                               TYPE_KEY          = "type",
                               LENGTH_KEY        = "length",
                               MASK_KEY          = "mask",
                               SHIFT_KEY         = "shift",
                               OPTION_LIST_KEY   = "option_list",
                               RANGE_BEGIN_KEY   = "range_begin",
                               RANGE_END_KEY     = "range_end",
                               DEFAULT_VALUE_KEY = "default_value";

    public static final String SHIFT_UPPER_KEY = "upper",
                               SHIFT_LOWER_KEY = "lower";

    public static final String TYPE_ARRAY_KEY      = "array",
                               TYPE_CHECK_KEY      = "check",
                               TYPE_SHORT_KEY      = "short",
                               TYPE_INTEGER_KEY    = "integer",
                               TYPE_STRING_KEY     = "string",
                               TYPE_FLOAT_KEY      = "float",
                               TYPE_DOUBLE_KEY     = "double",
                               TYPE_BIGDECIMAL_KEY = "bigdecimal",
                               TYPE_MONEY_KEY      = "money",
                               TYPE_DATETIME_KEY   = "datetime";

    public static final String TYPE_DATETIME_YEAR_KEY   = "year",
                               TYPE_DATETIME_MONTH_KEY  = "month",
                               TYPE_DATETIME_DAY_KEY    = "day",
                               TYPE_DATETIME_HOUR_KEY   = "hour",
                               TYPE_DATETIME_MINUTE_KEY = "minute",
                               TYPE_DATETIME_SECOND_KEY = "second";
    
    public static final int STRING         = 0,
                            SHORT          = 1,
                            INTEGER        = 2,
                            FLOAT          = 3,
                            DOUBLE         = 4,
                            BIGDECIMAL     = 5,
                            MONEY          = 6,
                            DATETIME       = 7,
                            INTERVAL       = 8;

    public static boolean debug = false;

    public ReportUtil() {
        baseURL             = null;
        title               = null;
        reportParameters    = null;

    }

    public void setBaseURL(String path) {
        baseURL = path;
    }

    public String baseURL() {
        return baseURL;
    }

    public void setTitle(String aTitle) {
        title = aTitle;
    }

    public String title() {
        return title;
    }


    public void openURL(String path) {
        //
        // open connection and get the params
        //
        try {
            reportURL = new URL(path);
        } catch (Exception e) {
            e.printStackTrace();
            reportURL = null;
        }
    }

    public void getReportParameters(String user) {
        Deserializer   deserializer;
        InputStream    stream = null;

        //
        // open the URL and get report specification
        //
        openURL(baseURL()+"?LOGNAME="+user);
        if (reportURL == null) {
            return;
        }

        try {
            stream = reportURL.openStream();

            deserializer = new Deserializer(stream);
            reportParameters = (Vector) deserializer.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (stream != null)
                stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    protected int decodeType(String type) {
        int decodeType = STRING;

        if (TYPE_ARRAY_KEY.equals(type))
            decodeType = STRING;
        if (TYPE_CHECK_KEY.equals(type))
            decodeType = STRING;
        else if (TYPE_SHORT_KEY.equals(type))
            decodeType = SHORT;
        else if (TYPE_INTEGER_KEY.equals(type))
            decodeType = INTEGER;
        else if (TYPE_STRING_KEY.equals(type))
            decodeType = STRING;
        else if (TYPE_FLOAT_KEY.equals(type))
            decodeType = FLOAT;
        else if (TYPE_DOUBLE_KEY.equals(type))
            decodeType = DOUBLE;
        else if (TYPE_BIGDECIMAL_KEY.equals(type))
            decodeType = BIGDECIMAL;
        else if (TYPE_MONEY_KEY.equals(type))
            decodeType = MONEY;
        else if (TYPE_DATETIME_KEY.equals(type))
            decodeType = DATETIME;

        return decodeType;
    }

    protected int decodeTypeDatetime(String type) {
        int decodeType = DBDatetime.YEAR;

        if (TYPE_DATETIME_YEAR_KEY.equals(type))
            decodeType = DBDatetime.YEAR;
        else if (TYPE_DATETIME_MONTH_KEY.equals(type))
            decodeType = DBDatetime.MONTH;
        else if (TYPE_DATETIME_DAY_KEY.equals(type))
            decodeType = DBDatetime.DAY;
        else if (TYPE_DATETIME_HOUR_KEY.equals(type))
            decodeType = DBDatetime.HOUR;
        else if (TYPE_DATETIME_MINUTE_KEY.equals(type))
            decodeType = DBDatetime.MINUTE;
        else if (TYPE_DATETIME_SECOND_KEY.equals(type))
            decodeType = DBDatetime.SECOND;

        return decodeType;
    }

    public String encodeURLParameters(String user) {
    	/*
        String       value ="";
        StringBuffer buffer = new StringBuffer(256);

        buffer.append("?LOGNAME="+user);
        Iterator keyIt = rpc.getFieldMap().keySet().iterator();
        while(keyIt.hasNext()) {
            value = "";
            String key = (String)keyIt.next();
            AbstractField field = rpc.getField(key);
            if (field.getValue() != null) {
                if(field instanceof DateField)
                    value = DBDatetime.getInstance(DBDatetime.YEAR, DBDatetime.DAY, ((DatetimeRPC)field.getValue()).getDate()).toString();
                else if(field instanceof DropDownField){
                    if(field.getValue() instanceof ArrayList){
                        ArrayList<DataSet> list = (ArrayList<DataSet>)field.getValue();
                        for(DataSet set : list){
                            if(list.indexOf(set) > 0)
                                value += ",";
                            value += (String)set.getKey().getValue();
                        }
                    }else{
                       value = field.getValue().toString();   
                    }
                    System.out.println("ArrayMulti selection = "+value);
                }else
                    value = field.getValue().toString();
                buffer.append("&")
                      .append(key)
                      .append("=")
                      .append(URLEncoder.encode(value));
             }
        }
        return buffer.toString();
        */
    	return null;
    }

}
