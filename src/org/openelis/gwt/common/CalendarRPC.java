package org.openelis.gwt.common;

public class CalendarRPC implements RPC {

    private static final long serialVersionUID = 1L;

    public int[][] cells = new int[6][7];
    public int month = -1, year = -1;
    public String monthDisplay;
    public byte begin, end;
    public Datetime date;
}
