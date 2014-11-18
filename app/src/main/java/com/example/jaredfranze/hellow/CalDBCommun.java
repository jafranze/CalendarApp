package com.example.jaredfranze.hellow;

/**
 * Created by taber on 11/9/14.
 */
import java.util.*;
import java.util.Iterator;
import android.net.Uri;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.CalendarContract;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

public class CalDBCommun
{
    Context context;

    Uri c_uri = CalendarContract.Calendars.CONTENT_URI;
    Uri e_uri = CalendarContract.Events.CONTENT_URI;
    Uri r_uri = CalendarContract.Reminders.CONTENT_URI;

    //List and longs for maintaining all calendars and events
    List<Event> eventList;
    List<myCalendar> calendarList;

    //Objects assigned to threads for handling DB communication
    ListInit myInit;
    EventUpdater myUpdater;
    EventAdder myAdder;
    EventRemover myRemover;
    CalendarAdder myCalAdder;

    public CalDBCommun(Context myContext)
    {
        context = myContext;
        eventList = new ArrayList<Event>(); //initiate list of events
        calendarList = new ArrayList<myCalendar>(); //initialize list of calendars

        myInit = new ListInit(myContext);
        myUpdater = new EventUpdater(myContext);
        myAdder = new EventAdder(myContext);
        myRemover = new EventRemover(myContext);
        myCalAdder = new CalendarAdder(myContext);

        calendarList = myInit.initializeCalendarList();
        eventList = myInit.initializeEventList();
    }
    /*
    public CalDBCommun(List<Event> myEvents)
    {
        eventList = myEvents;
        calendarList = new ArrayList<myCalendar>();
        currEventID = 0;
        lastEventID = 0;

        initializeThreads();
    }

    public CalDBCommun(List<Event> myEvents, List<myCalendar> myCalendars) {
        eventList = myEvents;
        calendarList = myCalendars;
        currEventID = 0;
        lastEventID = 0;

        initializeThreads();
    }
    */

    public int findIndexOfEventWithID(long id) {
        Event indexedEvent;
        if ((id < eventList.size()) && (id >= 0)) {
            if (eventList.get((int) id).getID() == id) //check if the id is in correct index
            {
                return (int) id;
            }
        }
        ListIterator<Event> myIterator = eventList.listIterator();
        while (myIterator.hasNext()) {
            if (myIterator.next().getID() == id) {
                return eventList.indexOf(myIterator.previous());
            }
        }
        return -1;
    }

    public int deleteAllEvents() {
        Event myEvent;
        int totalRows = 0;
        int rows = 0;
        Iterator<Event> myIterator = eventList.iterator();
        while (myIterator.hasNext()) {
            rows = myRemover.deleteEvent(myIterator.next());
            if(rows > 0)
            {
                myIterator.remove();
            }
        }
        return totalRows; //returns number of rows deleted
    }

    public boolean addEvent(Event myEvent)
    {
        Event currEvent = myAdder.addEvent(myEvent);
        if (currEvent != null) {
            eventList.add(currEvent);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean updateEvent(int index, Event myEvent) {
        Event currEvent = myUpdater.updateEvent(eventList.get(index), myEvent);
        if (currEvent != null) {
            eventList.set(index, currEvent);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateEvent(Event oldEvent, Event newEvent) {
        int index = findIndexOfEventWithID(oldEvent.getID());
        Event currEvent = myUpdater.updateEvent(eventList.get(index), newEvent);
        if (currEvent != null) {
            eventList.set(index, currEvent);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeEvent(long eID) {
        int rows = myRemover.deleteEvent(eID);
        if (rows <= 0) {
            return false;
        } else if (rows == 1) {
            eventList.remove(findIndexOfEventWithID(eID));
            return true;
        } else {
            /* This means that more than one event had the same ID which is an error in the CalendarContract database */
            return true;
        }
    }

    public boolean removeEvent(Event myEvent)
    {
        int rows = myRemover.deleteEvent(myEvent);
        if(rows <= 0)
        {
            return false;
        }
        else if(rows == 1)
        {
            eventList.remove(findIndexOfEventWithID(myEvent.getID()));
            return true;
        }
        else
        {
            //More than one row was deleted, meaning more than one event shared an id
            return true;
        }
    }

    public Event retrieveEvent(int index)
    {
        return eventList.get(index);
    }

    //Returns entire list of events
    public List<Event> retrieveEventList() {
        return eventList;
    }

    public List<myCalendar> retrieveCalendarList() {
        return calendarList;
    }

    public boolean insertCalendar(myCalendar cal) {
        myCalendar currCalendar = myCalAdder.addCalendar(cal);
        if (currCalendar != null) {
            calendarList.add(currCalendar);
            return true;
        } else {
            return false;
        }
    }

    /*hasConflic() checks if a newly added event creates a scheduling conflict with any event in current list
     *Return Options:
     ***False: There was no scheduling conflict
     ***True: The event possesses a scheduling conflict with one or more events in current list
    */
    public boolean hasConflict(Event myEvent) {
        Iterator<Event> eIterator = eventList.iterator();
        Event currEvent;
        while (eIterator.hasNext()) {
            currEvent = eIterator.next();
            //check to see if next event was removed recently or still exists
            if (currEvent != null) {
                //Determine which event starts later to check end times accordingly
                if (myEvent.getStartTime() > currEvent.getStartTime()) {
                    //myEvent starts after, so check it's start time with the end time of currEvent in the list
                    if ((myEvent.getStartTime() - currEvent.getEndTime()) < 0) //end time of one is after start time of another
                    {
                        return true; //events overlap
                    }
                } else {
                    //myEvent starts before, so check it's end time with the start time of currEvent in the list
                    if ((currEvent.getStartTime() - myEvent.getEndTime()) < 0) //end time of one is after start time of another
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

    public void clearLists()
    {
        calendarList.clear();
        eventList.clear();
    }

}

