package com.example.jaredfranze.hellow.AgendaItem;


import com.example.jaredfranze.hellow.EventIcon;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EventItem implements Item {

    private String title;
    private Calendar startDate;
    private EventIcon icon;
    private boolean isWeatherItem;
    private boolean hasReminders;

    @Override
    public boolean isHeaderItem() {
        return false;
    }

    public EventItem(String title, Calendar startDate, EventIcon icon, boolean isWeatherItem, boolean hasReminders) {
        this.title = title;
        this.startDate = startDate;
        this.icon = icon;
        this.isWeatherItem = isWeatherItem;
        this.hasReminders = hasReminders;
    }

    public String getTitle() {
        return title;
    }

    public String getStartTime() {
        if (isWeatherItem) return null;

        int ampm = startDate.get(Calendar.AM_PM);

        if ((ampm == 0) && (startDate.get(Calendar.HOUR) == 12)) {
            return "Midnight";
        } else if ((ampm == 1) && (startDate.get(Calendar.HOUR) == 12)) {
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

    public boolean isWeatherItem() {
        return isWeatherItem;
    }

    public boolean hasReminders() { return hasReminders; }
}
