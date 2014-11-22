package com.example.jaredfranze.hellow;

import android.content.Context;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.CalendarContract;

/**
 * Created by Taber on 11/13/2014.
 */
public class EventUpdater
{
    //Create content resolver and content values and uri path to table
    ContentResolver cr;
    ContentResolver r_cr;
    ContentValues values;
    Uri updateUri;
    Uri e_uri = CalendarContract.Events.CONTENT_URI;
    Uri r_uri = CalendarContract.Reminders.CONTENT_URI;

    public EventUpdater(Context myContext)
    {
        cr = myContext.getContentResolver();
        r_cr = myContext.getContentResolver();
        values = new ContentValues();
        updateUri = null;
    }

    public Event updateEvent(Event oldEvent, Event myEvent)
    {
        //Input all attributes of the event into the values to be updated
        //This is done rather than tediously checking each tuple if it has been changed
        values.put(CalendarContract.Events.CALENDAR_ID, myEvent.getCalID());
        values.put(CalendarContract.Events.TITLE, myEvent.getName());
        values.put(CalendarContract.Events.DESCRIPTION, myEvent.getDescription());
        values.put(CalendarContract.Events.EVENT_LOCATION, myEvent.getLocation());
        values.put(CalendarContract.Events.DTSTART, myEvent.getStartTime());
        if(!myEvent.getAllDay()) {
            if (myEvent.getEndTime() != -1) {
                values.put(CalendarContract.Events.DTEND, myEvent.getEndTime());
            } else {
                values.put(CalendarContract.Events.DURATION, myEvent.getDuration());
            }
        }
        else {
            values.put(CalendarContract.Events.ALL_DAY, myEvent.getAllDay());
        }
        values.put(CalendarContract.Events.EVENT_TIMEZONE, myEvent.getTimezone());
        values.put(CalendarContract.Events.RRULE, myEvent.getRecurrence());

        updateUri = ContentUris.withAppendedId(e_uri, myEvent.getID());
        int rows = cr.update(updateUri, values, null, null);

        if(rows < 1)
        {
            return null;
        }

        if(oldEvent.getReminderID() > 0) {
            values = new ContentValues();
            values.put(CalendarContract.Reminders.MINUTES, myEvent.getReminder());
            updateUri = ContentUris.withAppendedId(r_uri, myEvent.getID());
            rows = r_cr.update(updateUri, values, null, null);
        }
        else //reminder not found - insert reminder
        {
            values = new ContentValues();
            values.put(CalendarContract.Reminders.EVENT_ID, myEvent.getID());
            values.put(CalendarContract.Reminders.MINUTES, myEvent.getReminder());
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_DEFAULT);
            updateUri = r_cr.insert(r_uri, values);

            long rID = Long.parseLong(updateUri.getLastPathSegment());
            myEvent.setReminderID(rID);
        }
        return myEvent;
    } //end run()
} // end class EventUpdater