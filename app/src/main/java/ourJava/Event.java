package ourJava;

import java.util.Calendar;

/**
 * Created by jfranze0226 on 11/10/14.
 */
class Event
{
    /*
       eventReminder is an integer value representing the minutes
       prior to the startTime of the Event.
       ex. eventReminder = 15 would indicate a reminder set for
       15 minutes prior to the startTime.
       eventReminder = -1 indicate no reminder is set.
    */
    int eventReminder = -1;
    int eventDate, allDay;
    String eventName, location, timezone, recurrence, eventDescription;
    boolean boolAllDay = false;

    long id, calendar_id, duration, startTime, endTime;

    public Event(long eID, long currCalID, String eTitle, String eDescr, String eLocation,
                 long eDTStart, long eDTEnd, int eDuration, int eAllDay, String eTimezone, String eRRule)
    {
        id = eID;
        calendar_id = currCalID;
        eventName = eTitle;
        eventDescription = eDescr;
        location = eLocation;
        startTime = eDTStart;
        endTime = eDTEnd;
        duration = eDuration;
        allDay = eAllDay; //set to boolean based on 1 or 0
        if(allDay == 1)
        {
            boolAllDay = true;
        }
        timezone = eTimezone;
        recurrence = eRRule;
    }

    /*********
     EVENT ID
     *********/
    public long getID()
    {
        return id;
    }
    public void setID(long eID)
    {
        id = eID;
    }
    /*******
     CAL ID
     *******/
    public long getCalID()
    {
        return calendar_id;
    }
    public void setCalID(long cID)
    {
        calendar_id = cID;
    }
    /*********
     LOCATION
     *********/
    public String getLocation()
    {
        return location;
    }
    public void setLoction(String eLocation)
    {
        location = eLocation;
    }
    /*********
     DURATION
     *********/
    public long getDuration()
    {
        return duration;
    }
    public void setDuration(long eDuration)
    {
        duration = eDuration;
    }
    /*********
     TIMEZONE
     *********/
    public String getTimezone()
    {
        return timezone;
    }
    public void setTimezone(String eTimezone)
    {
        timezone = eTimezone;
    }
    /**************
     EVENT NAME
     **************/
    void changeName(String name)
    {
        eventName = name;
    }
    String getName()
    {
        return eventName;
    }
    /*********************
     EVENT DESCRIPTION
     *********************/
    void changeDescription(String description)
    {
        eventDescription = description;
    }
    String getDescription()
    {
        return eventDescription;
    }
    /******************
     START/END TIME
     ******************/
    void setStartTime(long newTime)
    {
        startTime = newTime;
    }
    void setEndTime(long newTime)
    {
        endTime = newTime;
    }
    long getStartTime()
    {
        return startTime;
    }
    long getEndTime()
    {
        return endTime;
    }
    /************
     REMINDER
     ************/
    void setReminder(int reminder)
    {
        eventReminder = reminder;
    }
    int getReminder()
    {
        return eventReminder;
    }
    /***********
     RECURRENCE
    ************/
    String getRecurrence()
    {
        return recurrence;
    }
    /********
     DATE
     ********/
    void setDate(int date)
    {
        eventDate = date;
    }
    int getDate()
    {
        return eventDate;
    }
    /*********
     ALLDAY??
     *********/
    public boolean getAllDay()
    {
        return boolAllDay;
    }
}

