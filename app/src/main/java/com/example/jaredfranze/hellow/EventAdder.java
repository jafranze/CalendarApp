package com.example.jaredfranze.hellow;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.CalendarContract;
import android.content.Context;

/**
 * Created by Jared.Franze on 11/13/2014.
 */
public class EventAdder
{
    //Necessary or optional attributes for storing
    Event currEvent;

    ContentResolver cr;
    ContentValues values;

    Uri e_uri = CalendarContract.Events.CONTENT_URI;
    Uri r_uri = CalendarContract.Reminders.CONTENT_URI;

    boolean tempSuccess;

    public EventAdder(Context myContext) {
        cr = myContext.getContentResolver();
        currEvent = null;
        tempSuccess = false;
    }

    public Event addEvent(Event myEvent) {
        values = new ContentValues();
        currEvent = myEvent;

        //Check if necessary attributes are included
        if ((currEvent.getStartTime() != -1)
                && (currEvent.getTimezone() != null)
                && ((currEvent.getEndTime() != -1)
                || (currEvent.getDuration() != -1))) {
            values.put(CalendarContract.Events.CALENDAR_ID, currEvent.getCalID());
            values.put(CalendarContract.Events.DTSTART, currEvent.getStartTime());
            values.put(CalendarContract.Events.EVENT_TIMEZONE, currEvent.getTimezone());
            if (!currEvent.getAllDay()) {
                if (currEvent.getEndTime() != -1) {
                    values.put(CalendarContract.Events.DTEND, currEvent.getEndTime());
                } else {
                    values.put(CalendarContract.Events.DURATION, currEvent.getDuration());
                }
            }

            tempSuccess = true;
        }
        //Continue adding other non-null attributes
        values.put(CalendarContract.Events.TITLE, currEvent.getName());
        values.put(CalendarContract.Events.DESCRIPTION, currEvent.getDescription());
        values.put(CalendarContract.Events.EVENT_LOCATION, currEvent.getLocation());
        values.put(CalendarContract.Events.ALL_DAY, currEvent.getAllDay());
        values.put(CalendarContract.Events.RRULE, currEvent.getRecurrence());

        if (tempSuccess) {
            Uri uri = cr.insert(e_uri, values); //add values to CalendarContract.Events table

            //access the event ID last inserted (the current event)
            long eventID = Long.parseLong(uri.getLastPathSegment());
            //Add that id to the event prior to adding it to the list
            /***************************************************
             * NOTE: ONLY ACCEPTED TIME TO EDIT AN EVENT'S ID
             ***************************************************
             */
            currEvent.setID(eventID);

            if (currEvent.getReminder() >= 0) {
                values = new ContentValues();
                values.put(CalendarContract.Reminders.EVENT_ID, currEvent.getID());
                values.put(CalendarContract.Reminders.MINUTES, (int)currEvent.getReminder());
                values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_DEFAULT);
                uri = cr.insert(r_uri, values);

                long reminder_id = Long.parseLong(uri.getLastPathSegment());
                currEvent.setReminderID(reminder_id);
            }
            return currEvent;
        }
        else {
            return null;
        }
    } //end run()
} //end class EventAdder