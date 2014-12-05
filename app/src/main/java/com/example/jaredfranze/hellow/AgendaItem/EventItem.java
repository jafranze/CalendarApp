package com.example.jaredfranze.hellow.AgendaItem;


import com.example.jaredfranze.hellow.EventIcon;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.jaredfranze.hellow.Event;

/**
 * Created by izuchukwuelechi on 11/13/14.
 */

public class EventItem implements Item {

    public static final int ITEM_TYPE_EVENT = 0;
    public static final int ITEM_TYPE_WEATHER = 1;
    public static final int ITEM_TYPE_HISTORY = 2;
    public static final int ITEM_TYPE_NOEVENT = 3;

    private Event event;
    private String title;
    private Calendar startDate;
    private EventIcon icon;
    private int itemType;
    private boolean hasReminders;

    @Override
    public Calendar getRepresentativeDate() {
        return startDate;
    }

    @Override
    public boolean isHeaderItem() {
        return false;
    }

    public EventItem(Event event, String title, Calendar startDate, EventIcon icon, int itemType, boolean hasReminders) {

        if (itemType == ITEM_TYPE_NOEVENT) {
            title = "No Events";
        }

        this.event = event;
        this.title = title;
        this.startDate = startDate;
        this.icon = icon;
        this.itemType = itemType;
        this.hasReminders = hasReminders;

    }

    public String getTitle() {
        return title;
    }

    public String getStartTime() {
        if (itemType != ITEM_TYPE_EVENT) return null;

        int ampm = startDate.get(Calendar.AM_PM);

        if ((ampm == 0) && (startDate.get(Calendar.HOUR) == 0) && (startDate.get(Calendar.MINUTE) == 0)) {
            return "Midnight";
        } else if ((ampm == 1) && (startDate.get(Calendar.HOUR) == 0) && (startDate.get(Calendar.MINUTE) == 0)) {
            return "Noon";
        }

        if (startDate.get(Calendar.MINUTE) == 0) {
            SimpleDateFormat formatter = new SimpleDateFormat("h");
            return formatter.format(startDate.getTime()) + ((ampm == 0) ? "a" : "p");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm");
            return formatter.format(startDate.getTime()) + ((ampm == 0) ? "a" : "p");
        }
    }

    public EventIcon getIcon() {
        return icon;
    }

    public int itemType() {
        return itemType;
    }

    public boolean hasReminders() { return hasReminders; }

    public Event getEvent() { return event; }

    @Override
    public String toString() {
        return title;
    }
}
