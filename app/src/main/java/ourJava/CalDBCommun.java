package ourJava;
/**
 * Created by taber on 11/9/14.
 */
import java.util.*;
import java.util.Iterator;
import java.util.Calendar;
import android.net.Uri;
import android.content.ContentValues;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.CalendarContract;
import android.content.ContentUris;
import android.database.Cursor;

public class CalDBCommun implements Runnable
{
    //Thread indeces for specific purposes
    private final static int T_INITIALIZE_LIST_INDEX = 0;
    private final static int T_EVENT_ADDER_INDEX = 1;
    private final static int T_EVENT_UPDATER_INDEX = 2;
    private final static int T_EVENT_REMOVER_INDEX = 3;
    
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
    
    //Projection for querying calendar id's to a particular user
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
        CalendarContract.Events.RRULE //recurrence rule. Type: String
    };
    
    Uri c_uri = CalendarContract.Calendars.CONTENT_URI;
    Uri e_uri = CalendarContract.Events.CONTENT_URI;
    
    //List and longs for maintaining all events and id's of relevant events
    long currEventID;
    long lastEventID;
    List<Event> eventList;
    List<myCalendar> calendarList;
    
    //Thread array for handling queries
    Thread[] myThread;
    
    //Objects assigned to threads for handling DB communication
    ListInit myInit;
    EventUpdater myUpdater;
    EventAdder myAdder;
    EventRemover myRemover;
    
    /* addSuccess, updateSuccess, removeSuccess: Boolean
     * These boolean variables are shared by EVENT_ADDER/EVENT_UPDATER thread and main thread to determine if insert/update was successful or not
     * Unless the thread fully completes and sets the variable to true, the function will return false.
     *
     * Default: false
    */
    boolean addSuccess;
    boolean updateSuccess;
    boolean removeSuccess;
    
    @Override
    public void run(){}
    
    public CalDBCommun()
    {
        //Initialize variables
       //Context context;
        eventList = new ArrayList<Event>(); //initiate list of events
        calendarList = new ArrayList<myCalendar>();
        currEventID = 0;
        lastEventID = 0;
        
        initializeThreads();
    }
    
    public CalDBCommun(List<Event> myEvents)
    {
        eventList = myEvents;
        calendarList = new ArrayList<myCalendar>();
        currEventID = 0;
        lastEventID = 0;
        
        initializeThreads();
    }
    
    public CalDBCommun(List<Event> myEvents, List<myCalendar> myCalendars)
    {
        eventList = myEvents;
        calendarList = myCalendars;
        currEventID = 0;
        lastEventID = 0;
        
        initializeThreads();
    }
    
    private void initializeThreads()
    {
        myThread = new Thread[4];
        
        myInit = new ListInit();
        myThread[T_INITIALIZE_LIST_INDEX] = new Thread(myInit);
        myThread[T_INITIALIZE_LIST_INDEX].start();
        try{
            myThread[T_INITIALIZE_LIST_INDEX].join();
        }
        catch(InterruptedException e){}
    }
    
    public boolean addEvent(Event myEvent)
    {
        addSuccess = false;
        myAdder = new EventAdder(myEvent);
        myThread[T_EVENT_ADDER_INDEX] = new Thread(myAdder);
        myThread[T_EVENT_ADDER_INDEX].start();
        
        try{
            myThread[T_EVENT_ADDER_INDEX].join();
        }
        catch(InterruptedException e){}
        
        //addSuccess returns false if exception occurs or thread terminates irregularly
        return addSuccess;
    }
    
    public boolean updateEvent(Event myEvent)
    {
        updateSuccess = false;
        myUpdater = new EventUpdater(myEvent);
        myThread[T_EVENT_UPDATER_INDEX] = new Thread(myUpdater);
        myThread[T_EVENT_UPDATER_INDEX].start();
        try{
            myThread[T_EVENT_UPDATER_INDEX].join();
        }
        catch(InterruptedException e){}
        
        //updateSuccess returns false if exception occurs or thread terminates irregularly
        return updateSuccess;
    }
    
    public boolean removeEvent(Event myEvent)
    {
        removeSuccess = false;
        myRemover = new EventRemover(myEvent);
        myThread[T_EVENT_REMOVER_INDEX] = new Thread(myRemover);
        myThread[T_EVENT_REMOVER_INDEX].start();
        try{
            myThread[T_EVENT_REMOVER_INDEX].join();
        }
        catch(InterruptedException e){}
        
        //removeSuccess will return false if exception occurs or thread terminates
        return removeSuccess;
    }
    
    //Returns entire list of events
    public List<Event> retrieveEventList()
    {
        return eventList;
    }
    
    /*hasConflic() checks if a newly added event creates a scheduling conflict with any event in current list
     *Return Options:
     ***False: There was no scheduling conflict
     ***True: The event possesses a scheduling conflict with one or more events in current list
    */
    public boolean hasConflict(Event myEvent)
    {
        Iterator<Event> eIterator = eventList.iterator();
        Event currEvent;
        while(eIterator.hasNext())
        {
            currEvent = eIterator.next();
            //check to see if next event was removed recently or still exists
            if(currEvent != null)
            {
                //Determine which event starts later to check end times accordingly
                if(myEvent.getStartTime() > currEvent.getStartTime())
                {
                    //myEvent starts after, so check it's start time with the end time of currEvent in the list
                    if((myEvent.getStartTime() - currEvent.getEndTime()) < 0) //end time of one is after start time of another
                    {
                        return true; //events overlap
                    }
                }
                else
                {
                    //myEvent starts before, so check it's end time with the start time of currEvent in the list
                    if((currEvent.getStartTime() - myEvent.getEndTime()) < 0) //end time of one is after start time of another
                    {
                        return true; //events overlap
                    }
                }
            }
        } //end while(eIterator.hasNext())
        
        /* If the function has exited the while loop without reaching conflict, then the event added does not conflict with any other events and is valid
         * Therefore,
         * Return is false
        */
        return false;
    }
    
    private class EventAdder implements Runnable
    {
        //Necessary or optional attributes for storing
        Event currEvent;

        Context context;
        ContentResolver cr;
        ContentValues values;
        
        boolean tempSuccess;
        boolean arbSuccess; //arbitrary boolean for excess data insertions
        
        public EventAdder(Event myEvent)
        {
            currEvent = myEvent;
            
            cr = context.getContentResolver();
            values = new ContentValues();
            
            //Only set to true if required attributes are included
            tempSuccess = false;
        }
        
        @Override
        public void run()
        {
            //Check if necessary attributes are included
            if((currEvent.getCalID() != -1)
                    &&(currEvent.getStartTime()!=-1)
                    &&(currEvent.getTimezone()!=null)
                    &&((currEvent.getEndTime()!=-1)
                    ||(currEvent.getDuration()!=-1)))
            {
                values.put(CalendarContract.Events.CALENDAR_ID,currEvent.getCalID());
                values.put(CalendarContract.Events.DTSTART,currEvent.getStartTime());
                values.put(CalendarContract.Events.EVENT_TIMEZONE,currEvent.getTimezone());
                values.put(CalendarContract.Events.DTEND,currEvent.getEndTime());
                values.put(CalendarContract.Events.DURATION,currEvent.getDuration());
                tempSuccess = true;
            }
            //Continue adding other non-null attribues
            values.put(CalendarContract.Events.TITLE,currEvent.getName());
            values.put(CalendarContract.Events.DESCRIPTION,currEvent.getDescription());
            values.put(CalendarContract.Events.EVENT_LOCATION,currEvent.getLocation());
            values.put(CalendarContract.Events.ALL_DAY,currEvent.getAllDay());
            values.put(CalendarContract.Events.RRULE,currEvent.getRecurrence());
            
            if(tempSuccess)
            {
                Uri uri = cr.insert(e_uri, values); //add values to CalendarContract.Events table
                
                //access the event ID last inserted (the current event)
                long eventID = Long.parseLong(uri.getLastPathSegment());
                //Add that id to the event prior to adding it to the list
                /***************************************************
                 * NOTE: ONLY ACCEPTED TIME TO EDIT AN EVENT'S ID
                 ***************************************************
                 */
                currEvent.setID(eventID);
                eventList.add(currEvent); //add myEvent to the stored list of events
                addSuccess = true; //set to true since thread successfully ended
            }
            else
            {
                addSuccess = false;
            }
        } //end run()
    } //end class EventAdder
    
    private class EventRemover implements Runnable
    {
        Context context;
        Event currEvent;
        ContentResolver cr;
        ContentValues values;
        Uri deleteUri;
        
        public EventRemover(Event myEvent)
        {
            currEvent = myEvent;
            cr = context.getContentResolver();
            values = new ContentValues();
            deleteUri = null;
        }
        
        @Override
        public void run()
        {
            deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, currEvent.getID());
            int rows = context.getContentResolver().delete(deleteUri, null, null);
            if(rows < 1)
            {
                removeSuccess = false;
            }
            else
            {
                removeSuccess = true;
            }
        }
    }
    
    private class EventUpdater implements Runnable
    {
        //Create event from input into constructor to perform update
        Event currEvent;
        
        //Create content resolver and content values and uri path to table
        Context context;
        ContentResolver cr;
        ContentValues values;
        Uri updateUri;
        
        public EventUpdater(Event myEvent)
        {

            currEvent = myEvent;
            cr = context.getContentResolver();
            values = new ContentValues();
            updateUri = null;
        }
        
        @Override
        public void run()
        {
            //Input all attributes of the event into the values to be updated
            //This is done rather than tediously checking each tuple if it has been changed
            values.put(CalendarContract.Events.CALENDAR_ID, currEvent.getCalID());
            values.put(CalendarContract.Events.TITLE, currEvent.getName());
            values.put(CalendarContract.Events.DESCRIPTION, currEvent.getDescription());
            values.put(CalendarContract.Events.EVENT_LOCATION, currEvent.getLocation());
            values.put(CalendarContract.Events.DTSTART, currEvent.getStartTime());
            values.put(CalendarContract.Events.DTEND, currEvent.getEndTime());
            values.put(CalendarContract.Events.DURATION, currEvent.getDuration());
            values.put(CalendarContract.Events.ALL_DAY, currEvent.getAllDay());
            values.put(CalendarContract.Events.EVENT_TIMEZONE, currEvent.getTimezone());
            values.put(CalendarContract.Events.RRULE, currEvent.getRecurrence());
            
            updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, currEvent.getID());
            int rows = context.getContentResolver().update(updateUri, values, null, null);
            if(rows < 1)
            {
                //No rows were updated, so return false, nothing was updated
                updateSuccess = false;
            }
            else
            {
                updateSuccess = true;
            }
        } //end run()
    } // end class EventUpdater
    
    private class ListInit implements Runnable
    {
        //Variables used to define and traverse calendars
        Context context;
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
        Event tempEvent;
        
        //Create cursor for traversing result set from queries
        Cursor cur;
        
        //Create content resolver to hold result set
        ContentResolver cr = context.getContentResolver();
        
        public ListInit()
        {
            currCalID = 0;
            displayName = null;
            ownerName = null;
            cur = null;
        }
        
        @Override
        public void run()
        {
            /*Initial step of ListInit is to gather all calendars and their respective
             * ID's to gather all events to that specific person.
             * Once all of the calendar id's have been gathered and stored. The events under
             * those ID's are loaded into a result set of a query.
            
             * Initialize step one by generating calendar query.
            */
            //Submit query to retrieve all calendars and set pointer prior to first tuple of result set
            cur = cr.query(c_uri, CALENDAR_PROJECTION, null, null,null);
            
            //Traverse result set, starting by using moveToNext method to move to first tuple
            while(cur.moveToNext())
            {
                currCalID = 0;
                displayName = null;
                ownerName = null;
                
                currCalID = cur.getLong(C_PROJECTION_ID_INDEX);
                ownerName = cur.getString(C_PROJECTION_OWNER_ACCOUNT_INDEX);
                displayName = cur.getString(C_PROJECTION_DISPLAY_NAME_INDEX);
                
                //Set tempCal to the newly gathered calendar. Resets for each row in result set
                tempCal = new myCalendar(currCalID,displayName,null,0,ownerName);
                calendarList.add(tempCal); //add new calendar to total list
            }
            
            /* Now that all of the calendars have been loaded, we can then query all of those calendar id's
             * using the events table.
            
             * Initialize second step by generating events query 
            */
            //Query all events, adding them in order so index in List will be equivalent to eID
            cur = cr.query(e_uri,EVENT_PROJECTION,null,null,null);
            
            while(cur.moveToNext())
            {
                resetEventVars(); //reset event variables
                
                //Set variables from projection of tuple in result set
                eID = cur.getLong(E_PROJECTION_ID_INDEX);
                currCalID = cur.getLong(E_PROJECTION_CID_INDEX);
                eTitle = cur.getString(E_PROJECTION_TITLE_INDEX);
                eDescr = cur.getString(E_PROJECTION_DESCRIPTION_INDEX);
                eLocation = cur.getString(E_PROJECTION_LOCATION_INDEX);
                eDTStart = cur.getLong(E_PROJECTION_DTSTART_INDEX);
                eDTEnd = cur.getLong(E_PROJECTION_DTEND_INDEX);
                eDuration = (int)cur.getLong(E_PROJECTION_DURATION_INDEX);
                eAllDay = cur.getInt(E_PROJECTION_ALL_DAY_INDEX);
                eTimezone = cur.getString(E_PROJECTION_TIMEZONE_INDEX);
                eRRule = cur.getString(E_PROJECTION_RRULE_INDEX);
                    
                //Add new event with given attributes to the list
                eventList.add(new Event(eID,currCalID,eTitle,eDescr,eLocation,eDTStart,eDTEnd,eDuration,eAllDay,eTimezone,eRRule));
            }
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
} // end CalDBCommun