package org.openelis.gwt.common;

import java.io.Serializable;

public class CalendarRPC implements Serializable {

    private static final long serialVersionUID = 1L;

    public int[][] cells = new int[6][7];
    public Datetime date;
    public String xml;
    public int month = -1;
    public int year = -1;
    public String monthDisplay;
    public byte begin;
    public byte end;

}
