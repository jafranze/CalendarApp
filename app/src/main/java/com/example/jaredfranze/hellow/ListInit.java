package com.example.jaredfranze.hellow;

import android.content.ContentResolver;
import android.database.Cursor;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Taber on 11/13/2014.
 */

public class ListInit
{
    //Indeces for calendar projection
    private static final int C_PROJECTION_ID_INDEX = 0;
    private static final int C_PROJECTION_OWNER_ACCOUNT_INDEX = 1;
    private static final int C_PROJECTION_DISPLAY_NAME_INDEX = 2;

    //Indeces for events projection
    private static final int E_PROJECTION_ID_INDEX = 0;
    private static final int E_PROJECTION_CID_INDEX = 1;
    private static final int E_PROJECTION_TITLE_INDEX = 2;
    private static final int E_PROJECTION_DESCRIPTION_INDEX = 3;
    private static final int E_PROJECTION_LOCATION_INDEX = 4;
    private static final int E_PROJECTION_DTSTART_INDEX = 5;
    private static final int E_PROJECTION_DTEND_INDEX = 6;
    private static final int E_PROJECTION_DURATION_INDEX = 7;
    private static final int E_PROJECTION_ALL_DAY_INDEX = 8;
    private static final int E_PROJECTION_TIMEZONE_INDEX = 9;
    private static final int E_PROJECTION_RRULE_INDEX = 10;
    private static final int E_PROJECTION_DELETED_INDEX = 11;

    //Indexes for reminder projection
    private static final int R_PROJECTION_ID_INDEX = 0;
    private static final int R_PROJECTION_EVENT_ID_INDEX = 1;
    private static final int R_PROJECTION_MINUTES_INDEX = 2;
    private static final int R_PROJECTION_METHOD_INDEX = 3;

    Uri c_uri = CalendarContract.Calendars.CONTENT_URI;
    Uri e_uri = CalendarContract.Events.CONTENT_URI;
    Uri r_uri = CalendarContract.Reminders.CONTENT_URI;

    public static final String[] CALENDAR_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.OWNER_ACCOUNT,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
    };

    //Projection for querying all events by calendar id found by calendar projection
    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Events._ID, //unique id to the event. Type: long
            CalendarContract.Events.CALENDAR_ID, //id of the calendar it belongs to. Type: long
            CalendarContract.Events.TITLE, //title of the event. Type: String
            CalendarContract.Events.DESCRIPTION, //description Type: String
            CalendarContract.Events.EVENT_LOCATION, //location given to event. Type: String
            CalendarContract.Events.DTSTART, //time of start. Type: long (millis since epoch)
            CalendarContract.Events.DTEND, //time of end. Type: long (millis since epoch)
            CalendarContract.Events.DURATION, //Type: int - represents minutes after start time
            CalendarContract.Events.ALL_DAY, //is the event an all day event. Type: boolean
            CalendarContract.Events.EVENT_TIMEZONE, //timezone the event is scheduled in. Type: String
            CalendarContract.Events.RRULE, //recurrence rule. Type: String
            CalendarContract.Events.DELETED
    };

    public static final String[] REMINDER_PROJECTION = new String[]{
            CalendarContract.Reminders._ID, //reminder id used to update the reminder
            CalendarContract.Reminders._ID, //event id the reminder belongs to
            CalendarContract.Reminders.MINUTES, //minutes prior to the event the reminder should occur
            CalendarContract.Reminders.METHOD //method of the reminder
    };

    public static final String R_SELECTION = "(" + CalendarContract.Reminders.EVENT_ID + " = ?)";

    //Variables used to define and traverse calendars
    long currCalID;
    String displayName;
    String ownerName;
    myCalendar tempCal;

    //Variables used to define and traverse events
    long eID;
    String eTitle;
    String eDescr;
    String eLocation;
    long eDTStart;
    long eDTEnd;
    int eDuration;
    int eAllDay;
    String eTimezone;
    String eRRule;
    long reminder;
    long reminder_id;
    int eDeleted;
    String[] selectionArgs;
    Event tempEvent;

    //Create cursor for traversing result set from queries
    Cursor cur;
    Cursor r_cur;

    ContentResolver cr;
    ContentResolver ccr;

    public ListInit(Context myContext)
    {
        currCalID = 0;
        displayName = null;
        ownerName = null;
        cur = null;
        cr = myContext.getContentResolver();
        ccr = myContext.getContentResolver();
        r_cur = null;
    }

    public List<myCalendar> initializeCalendarList() {
        List<myCalendar> calendarList = new ArrayList<myCalendar>();
        //Submit query to retrieve all calendars and set pointer prior to first tuple of result set
        cur = ccr.query(c_uri, CALENDAR_PROJECTION, null, null, CalendarContract.Calendars._ID);

        //Traverse result set, starting by using moveToNext method to move to first tuple
        while (cur.moveToNext()) {
            currCalID = 0;
            displayName = null;
            ownerName = null;

            currCalID = cur.getLong(C_PROJECTION_ID_INDEX);
            ownerName = cur.getString(C_PROJECTION_OWNER_ACCOUNT_INDEX);
            displayName = cur.getString(C_PROJECTION_DISPLAY_NAME_INDEX);

            //Set tempCal to the newly gathered calendar. Resets for each row in result set
            tempCal = new myCalendar(currCalID, displayName, null, 0, ownerName);
            calendarList.add(tempCal); //add new calendar to total list
        }

        cur.close();

        return calendarList;
    }

    public List<Event> initializeEventList(){
        List<Event> eventList = new ArrayList<Event>();
        //Query all events, adding them in order so index in List will be equivalent to eID
        cur = cr.query(e_uri,EVENT_PROJECTION,null,null,CalendarContract.Events._ID);

        while(cur.moveToNext()) {
            resetEventVars(); //reset event variables

            //Set variables from projection of tuple in result set
            eID = cur.getLong(E_PROJECTION_ID_INDEX);
            currCalID = cur.getLong(E_PROJECTION_CID_INDEX);
            eTitle = cur.getString(E_PROJECTION_TITLE_INDEX);
            eDescr = cur.getString(E_PROJECTION_DESCRIPTION_INDEX);
            eLocation = cur.getString(E_PROJECTION_LOCATION_INDEX);
            eDTStart = cur.getLong(E_PROJECTION_DTSTART_INDEX);
            eDTEnd = cur.getLong(E_PROJECTION_DTEND_INDEX);
            eDuration = (int) cur.getLong(E_PROJECTION_DURATION_INDEX);
            eAllDay = cur.getInt(E_PROJECTION_ALL_DAY_INDEX);
            eTimezone = cur.getString(E_PROJECTION_TIMEZONE_INDEX);
            eRRule = cur.getString(E_PROJECTION_RRULE_INDEX);
            eDeleted = cur.getInt(E_PROJECTION_DELETED_INDEX);

            selectionArgs = new String[]{Long.toString(eID)};

            if(eDeleted > 0)
            {
                continue;
            }

            r_cur = ccr.query(r_uri, REMINDER_PROJECTION, R_SELECTION, selectionArgs, CalendarContract.Reminders.MINUTES);

            /*********************************************************************************
             * Assuming there is only one reminder, otherwise reminder and reminder will
             * reset so only the last reminder retrieved will be stored by newEvent.
             *
             * Make List<Long> for both reminder and reminder_id in both Event.java and here
             * to allow multiple reminders to be implemented.
             */
            while (r_cur.moveToNext()) {
                reminder = r_cur.getLong(R_PROJECTION_MINUTES_INDEX);
                reminder_id = r_cur.getLong(R_PROJECTION_ID_INDEX);
            }
            r_cur.close();


            Event newEvent = new Event(eID, currCalID, eTitle, eDescr, eLocation, eDTStart, eDTEnd, eDuration, eAllDay, eTimezone, eRRule, reminder);
            newEvent.setReminderID(reminder_id);

            //Add new event with given attributes to the list
            eventList.add(newEvent);
        }
        cur.close();

        return eventList;

    } //end run()

    public void resetEventVars()
    {
            /* resetVars resets each variable used in defining events
             * to its default value.
             * NOTE: This method ignores the calendar id which should be
             * ignored since it is only reset when making new query with new cal_id
            */
        eID = 0;
        currCalID = 0;
        eTitle = null;
        eDescr = null;
        eLocation = null;
        eDTStart = 0;
        eDTEnd = 0;
        eDuration = 0;
        eAllDay = 0;
        eTimezone = null;
        eRRule = null;
    }
} // end class ListInit