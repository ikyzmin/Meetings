package com.ssau.meetings.database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Meet {

    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static final DateFormat CARD_FORMATTER = new SimpleDateFormat("dd/MMM/yy HH:mm");

    public String id;
    public String title;
    public String description;
    public String start;
    public String end;
    public String photoName;
    public List<User> users;
    public boolean participate;

    public Date getStartDate() throws ParseException {
        return DATE_FORMATTER.parse(start);
    }

    public Date getEndDate() throws ParseException {
        return DATE_FORMATTER.parse(end);
    }
}
