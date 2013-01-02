/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used by reports to build a user input request. Reports may
 * combine multiple prompts to request for a wide variety of inputs.
 * 
 */

public class Prompt implements Serializable {

    private static final long           serialVersionUID = 1L;
    protected String                    name, prompt, mask, defaultValue;
    protected Type                      type;
    protected Case                      shift; 
    protected Datetime                  dt_StartCode, dt_EndCode;    
    protected Integer                   length, width;    
    protected ArrayList<OptionListItem> optionList;
    protected boolean                   required, hidden, multiSelect;

    public enum Type {
        ARRAY, ARRAYMULTI, CHECK, SHORT, INTEGER, STRING, FLOAT, DOUBLE, BIGDECIMAL, MONEY,
        DATETIME
    };

    public enum Case {
        UPPER, LOWER, MIXED
    };
    
    public enum Datetime { 
        YEAR, MONTH, DAY, HOUR, MINUTE, SECOND
    };

    /**
     * Constructors
     */

    /**
     * A no-argument constructor is needed for serialization to work
     */
    protected Prompt() {        
    }
    
    public Prompt(String name) {
        this(name, null);
    }

    public Prompt(String name, Type type) {
        setName(name);
        setType(type);
    }

    /**
     * get/set the prompt name. Prompt name acts like a variable name.
     */
    public String getName() {
        return name;
    }

    public Prompt setName(String name) {
        this.name = DataBaseUtil.trim(name);
        return this;
    }

    /**
     * get/set prompt description. The client GUI uses this string as the prompt
     * before the response entry field.
     */
    public String getPrompt() {
        return prompt;
    }

    public Prompt setPrompt(String prompt) {
        this.prompt = DataBaseUtil.trim(prompt);
        return this;
    }

    /**
     * get/set the type of response allowed for this prompt. See predefined
     * TYPE_xxxx for valid types.
     */
    public Type getType() {
        return type;
    }

    public Prompt setType(Type type) {
        this.type = type;
        return this;
    }

    /**
     * get/set upper/lower shift for text type responses.
     */
    public Case getCase() {
        return shift;
    }

    public Prompt setCase(Case shift) {
        this.shift = shift;
        return this;
    }
    
    /**     
     * get/set start and end code for date and time 
     */
    public Datetime getDatetimeStartCode() {
        return dt_StartCode;
    }

    public Prompt setDatetimeStartCode(Datetime startCode) {
        this.dt_StartCode = startCode;
        return this;
    }

    public Datetime getDatetimeEndCode() {
        return dt_EndCode;
    }

    public Prompt setDatetimeEndCode(Datetime datetimeEndCode) {
        this.dt_EndCode = datetimeEndCode;
        return this;
    }

    /**
     * get/set the maxiumn number of characters allowed for response.
     */
    public Integer getLength() {
        return length;
    }

    public Prompt setLength(int length) {
        this.length = new Integer(length);
        return this;
    }

    /**
     * get/set the display width of response field
     */
    public Integer getWidth() {
        return width;
    }

    public Prompt setWidth(int width) {
        this.width = new Integer(width);
        return this;
    }

    /**
     * get/set response input mask. Mask characters: # for any digit A for any
     * letter X for any character Beside the mask characters, you may use any
     * other character in the mask. You may use a \ character to escape a mask.
     */
    public String getMask() {
        return mask;
    }

    public Prompt setMask(String mask) {
        this.mask = DataBaseUtil.trim(mask);
        return this;
    }

    /**
     * get/set a list of optional parameters depending on prompt type. The valid
     * options for prompt type are: DATETIME OPTION_DATETIME_from,
     * OPTION_DATETIME_to ARRAY list of elements of type (title|response) where
     * title is the element displayed in popup list and response is the string
     * returned as parameter.
     */
    public ArrayList<OptionListItem> getOptionList() {
        return optionList;
    }

    public Prompt setOptionList(ArrayList<OptionListItem> optionList) {
        this.optionList = optionList;
        return this;
    }

    /**
     * get/set the multiSelect option for OptionList
     */
    public Prompt setMutiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
        return this;
    }

    public boolean getMultiSelect() {
        return multiSelect;
    }

    /**
     * get/set the default response value.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    public Prompt setDefaultValue(String defaultValue) {
        this.defaultValue = DataBaseUtil.trim(defaultValue);
        return this;
    }

    /**
     * get/set whether this prompt is sent to the GUI.
     */
    public boolean isHidden() {
        return hidden;
    }

    public Prompt setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    /**
     * get/set if a response is required.
     */
    public boolean isRequired() {
        return required;
    }

    public Prompt setRequired(boolean required) {
        this.required = required;
        return this;
    }

    /**
     * Overridden.
     */
    public String toString() {
        StringBuffer s;

        s = new StringBuffer();

        s.append('{')
         .append("name=")
         .append(name)
         .append(';')
         .append("type=")
         .append(type)
         .append(';');
        if (prompt != null)
            s.append("prompt=\"").append(prompt).append("\";");
        if (optionList != null) {
            s.append("option_list=(");
            for (int i = 0; i < optionList.size(); i++ ) {
                if (i != 0)
                    s.append(',');
                s.append('"').append(optionList.get(i)).append('"');
            }
            s.append(");");
        }
        if (mask != null)
            s.append("mask=\"").append(mask).append("\";");
        if (shift != null)
            s.append("shift=\"").append(shift).append("\";");
        if (defaultValue != null)
            s.append("default_value=\"").append(defaultValue).append("\";");
        if (length != null)
            s.append("length=").append(length).append(';');
        if (width != null)
            s.append("width=").append(width).append(';');
        s.append('}');

        return s.toString();
    }
}