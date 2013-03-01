package org.openelis.gwt.common;

public class CalendarRPC implements RPC {

    private static final long serialVersionUID = 1L;

    public int[][] cells = new int[6][7];
    public org.openelis.ui.common.Datetime date;
    public String xml;
    public int month = -1;
    public int year = -1;
    public String monthDisplay;
    public byte begin;
    public byte end;

}
