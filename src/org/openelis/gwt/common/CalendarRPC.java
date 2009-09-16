package org.openelis.gwt.common;

public class CalendarRPC implements RPC {

    private static final long serialVersionUID = 1L;

    public String[][][] cells = new String[6][7][2];
    public Datetime date;
    public String xml;
    public int month = -1;
    public int year = -1;
    public String monthDisplay;
    public byte begin;
    public byte end;

}
