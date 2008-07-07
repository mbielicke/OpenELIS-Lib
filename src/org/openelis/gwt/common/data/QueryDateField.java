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
package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.DatetimeRPC;

import java.util.Date;

public class QueryDateField extends QueryField {


    private static final long serialVersionUID = 1L;
    private byte begin;
    private byte end;
    private Integer max;
    private Integer min;
    public static final String TAG_NAME = "rpc-queryDate";
    
    public QueryDateField() {
        
    }
    
    public QueryDateField(Node node){
        setKey(node.getAttributes().getNamedItem("key").getNodeValue());
        setBegin(Byte.parseByte(node.getAttributes()
                                          .getNamedItem("begin")
                                          .getNodeValue()));
        setEnd(Byte.parseByte(node.getAttributes()
                                        .getNamedItem("end")
                                        .getNodeValue()));
        if (node.getAttributes().getNamedItem("max") != null)
            setMax(new Integer(node.getAttributes()
                                         .getNamedItem("max")
                                         .getNodeValue()));
        if (node.getAttributes().getNamedItem("min") != null)
            setMin(new Integer(node.getAttributes()
                                         .getNamedItem("min")
                                         .getNodeValue()));
    }

    public void validate() {
        for (String param : parameter) {
            try {
                Date date = new Date(param.replaceAll("-", "/"));
            } catch (Exception e) {
                addError("Not a Valid Date");
                valid = false;
                return;
            }
        }
        if (value != null && !isInRange()) {
            valid = false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uiowa.uhl.inmsp.interfaces.IField#isRequired()
     */
    public boolean isRequired() {
        // TODO Auto-generated method stub
        return required;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uiowa.uhl.inmsp.interfaces.IField#isInRange()
     */
    public boolean isInRange() {
        // TODO Auto-generated method stub
        for (String param : parameter) {
            String[] params = param.split("..");
            for (int i = 0; i < params.length; i++) {
                Date date = new Date(params[i].replaceAll("-", "/"));
                DatetimeRPC dVal = DatetimeRPC.getInstance(begin, end, date);
                if (min != null && dVal.before(DatetimeRPC.getInstance()
                                                          .add(-min.intValue()))) {
                    addError("Date is too far in the past");
                    return false;
                }
                if (max != null && dVal.after(DatetimeRPC.getInstance()
                                                         .add(max.intValue()))) {
                    addError("Date is too far in the future");
                    return false;
                }
            }
        }
        return true;
    }

    public void setBegin(byte begin) {
        this.begin = begin;
    }

    public void setEnd(byte end) {
        this.end = end;
    }

    public void setMin(Object min) {
        this.min = (Integer)min;
    }

    public void setMax(Object max) {
        this.max = (Integer)max;
    }

    public byte getBegin() {
        return this.begin;
    }

    public byte getEnd() {
        return this.end;
    }

    public QueryDateField getInstance() {
        QueryDateField obj = new QueryDateField();
        obj.setBegin(begin);
        obj.setEnd(end);
        obj.setMax(max);
        obj.setMin(min);
        obj.setValue(value);
        return obj;
    }

    public QueryDateField getInstance(Node node) {
        return new QueryDateField(node);
    }
}
