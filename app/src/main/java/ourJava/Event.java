package ourJava;

import java.util.Calendar;

/**
 * Created by jfranze0226 on 11/10/14.
 */
class Event
{
    String eventName, location, timezone, recurrence;
    String eventDescription = "NONE";
    /*
       eventReminder is an integer value representing the minutes
       prior to the startTime of the Event.
       ex. eventReminder = 15 would indicate a reminder set for
       15 minutes prior to the startTime.
       eventReminder = -1 indicate no reminder is set.
    */
    int eventReminder = -1;
    /*
       eventRecurrence can be one of 4 integer values.
       If daily, eventRecurrence = 0
       If weekly, eventRecurrence = 1
       If monthly, eventRecurrence = 2
       If yearly, eventRecurrence = 3
       eventRecurrence = -1 indicate no recurrence is set.
    */
    //int eventRecurrence = -1;
    int eventDate;
    int allDay;

    long id, calendar_id, duration, startTime, endTime;
   /*
   void Event(String name, int date, String description, int reminder, int recurrence)
   {
      eventName = name;
      if(description != "NONE")
      {
         eventDescription = description;
      }
      if(reminder != -1)
      {
         eventReminder = reminder;
      }
      if(recurrence != -1)
      {
         eventRecurrence = recurrence;
      }
      startTime = -1;
      endTime = -1;
      eventDate = date;
      allDay = true;
   }
   void Event(String name, int start, int end, int date, String description, int reminder, int recurrence)
   {
      eventName = name;
      if(description != "NONE")
      {
         eventDescription = description;
      }
      if(reminder != -1)
      {
         eventReminder = reminder;
      }
      if(recurrence != -1)
      {
         eventRecurrence = recurrence;
      }
      startTime = start;
      endTime = end;
      eventDate = date;
      allDay = false;
   }
   */

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
        timezone = eTimezone;
        recurrence = eRRule;
    }

    public long getID()
    {
        return id;
    }

    public void setID(long eID)
    {
        id = eID;
    }

    public long getCalID()
    {
        return calendar_id;
    }

    public void setCalID(long cID)
    {
        calendar_id = cID;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLoction(String eLocation)
    {
        location = eLocation;
    }

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long eDuration)
    {
        duration = eDuration;
    }

    public String getTimezone()
    {
        return timezone;
    }

    public void setTimezone(String eTimezone)
    {
        timezone = eTimezone;
    }
    public int getAllDay()
    {
        return allDay;
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
    /**************
     RECURRENCE
     *************
     void setRecurrence(int recurrence)
     {
     eventRecurrence = recurrence;
     }
     */
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
}

