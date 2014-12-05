package com.example.jaredfranze.hellow.AgendaItem;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by izuchukwuelechi on 11/13/14.
 */

public class DayHeaderItem implements Item {
    public Calendar day;

    @Override
    public Calendar getRepresentativeDate() {
        return day;
    }

    @Override
    public boolean isHeaderItem() {
        return true;
    }

    public DayHeaderItem(Calendar day) {
        this.day = day;
    }

    public String getTitle() {
        Calendar todayc = Calendar.getInstance();

        Calendar yesterdayc = Calendar.getInstance();
        yesterdayc.add(Calendar.DAY_OF_YEAR, -1);

        Calendar tomorrowc = Calendar.getInstance();
        tomorrowc.add(Calendar.DAY_OF_YEAR, 1);

        String dayString;

        if (day.get(Calendar.YEAR) == todayc.get(Calendar.YEAR)
                && day.get(Calendar.DAY_OF_YEAR) == todayc.get(Calendar.DAY_OF_YEAR)) {
            // it's today
            dayString = "Today";
        } else if (day.get(Calendar.YEAR) == yesterdayc.get(Calendar.YEAR)
                && day.get(Calendar.DAY_OF_YEAR) == yesterdayc.get(Calendar.DAY_OF_YEAR)) {
            // it's yesterday
            dayString = "Yesterday";
        } else if (day.get(Calendar.YEAR) == tomorrowc.get(Calendar.YEAR)
                && day.get(Calendar.DAY_OF_YEAR) == tomorrowc.get(Calendar.DAY_OF_YEAR)) {
            // it's tomorrow
            dayString = "Tomorrow";
        } else {
            dayString = day.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d");

        return dayString + "  ·  " + formatter.format(day.getTime());
    }
}
