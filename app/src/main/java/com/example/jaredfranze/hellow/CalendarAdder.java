package com.example.jaredfranze.hellow;

/**
 * Created by Taber on 11/13/2014.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContentValues;
import android.provider.CalendarContract;
import android.net.Uri;

public class CalendarAdder
{
    myCalendar currCalendar;
    ContentResolver cr;
    ContentValues values;

    Uri c_uri = CalendarContract.Calendars.CONTENT_URI;

    public CalendarAdder(Context myContext)
    {
        cr = myContext.getContentResolver();
        currCalendar = null;
        values = new ContentValues();
    }

    public myCalendar addCalendar(myCalendar cal)
    {
        if(cal.getName().equals(null))
        {
            return null;
        }
        else {
            values.put(CalendarContract.Calendars.NAME, cal.getName());
            values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, cal.getName());

            Uri uri = cr.insert(c_uri, values);

            cal.setID(Long.parseLong(uri.getLastPathSegment()));
            return cal;
        }
    }
}
