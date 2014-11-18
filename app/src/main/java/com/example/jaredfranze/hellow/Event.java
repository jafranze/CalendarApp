package com.example.jaredfranze.hellow;

import android.os.Environment;

import java.util.Calendar;

/**
 * Created by jfranze0226 on 11/10/14.
 */
class Event
{
    int allDay;
    String eventName, location, timezone, recurrence, eventDescription, recordingFileName;
    boolean boolAllDay, voiceMemoExists = false;
    long id, calendar_id, duration, startTime, endTime, reminder,reminder_id;
    Date startDate, endDate;

    //This Day in History
    String historyTitle, historyDescription;
    public Event()
    {
        id = 0;
        calendar_id = 0;
        eventName = null;
        eventDescription = null;
        location = null;
        startTime = 0;
        endTime = -1;
        duration = -1;
        allDay = 0; //set to boolean based on 1 or 0
        boolAllDay = false;
        timezone = null;
        recurrence = null;
        reminder = -1;
        reminder_id = -1;
    }
    public Event(long eID, long currCalID, String eTitle, String eDescr, String eLocation,
                 long eDTStart, long eDTEnd, int eDuration, int eAllDay, String eTimezone, String eRRule)
    {
        id = eID;
        calendar_id = currCalID;
        eventName = eTitle;
        eventDescription = eDescr;
        location = eLocation;
        startTime = eDTStart;
        recordingFileName = "recording" + Long.toString(id);

        allDay = eAllDay; //set to boolean based on 1 or 0
        if(allDay == 1)
        {
            boolAllDay = true;
        }
        timezone = eTimezone;
        recurrence = eRRule;
        if(recurrence != null) {
            duration = eDuration;
            setStartDate(startTime);
            setEndDate(endTime);
            endTime = -1;
        }
        else{
            endTime = eDTEnd;
            setStartDate(startTime);
            setEndDate(endTime);
            duration = -1;
        }
        reminder_id = -1;
    }

    public Event(long eID, long currCalID, String eTitle, String eDescr, String eLocation,
                 Date myStartDate, Date myEndDate, int eDuration, int eAllDay, String eTimezone, String eRRule)
    {
        id = eID;
        calendar_id = currCalID;
        eventName = eTitle;
        eventDescription = eDescr;
        location = eLocation;
        startTime = myStartDate.getDateInMillis();
        recordingFileName = "recording" + Long.toString(id);

        allDay = eAllDay; //set to boolean based on 1 or 0
        if(allDay == 1)
        {
            boolAllDay = true;
        }
        timezone = eTimezone;
        recurrence = eRRule;
        if(recurrence != null) {
            duration = eDuration;
            setStartDate(myStartDate);
            setEndDate(myEndDate);
            endTime = -1;
        }
        else{
            endTime = myEndDate.getDateInMillis();
            setStartDate(myStartDate);
            setEndDate(myEndDate);
            duration = -1;
        }
        reminder_id = -1;
    }
    public Event(long eID, long currCalID, String eTitle, String eDescr, String eLocation,
                 long eDTStart, long eDTEnd, int eDuration, int eAllDay, String eTimezone, String eRRule, long eReminder)
    {
        id = eID;
        calendar_id = currCalID;
        eventName = eTitle;
        eventDescription = eDescr;
        location = eLocation;
        startTime = eDTStart;
        reminder = eReminder;
        recordingFileName = "recording" + Long.toString(id);

        allDay = eAllDay; //set to boolean based on 1 or 0
        if(allDay == 1)
        {
            boolAllDay = true;
        }
        timezone = eTimezone;
        recurrence = eRRule;
        if(recurrence != null) {
            duration = eDuration;
            setStartDate(startTime);
            setEndDate(eDTEnd);
            endTime = -1;
        }
        else{
            endTime = eDTEnd;
            setStartDate(startTime);
            setEndDate(endTime);
            duration = -1;
        }
        reminder_id = -1;
    }

    public Event(long eID, long currCalID, String eTitle, String eDescr, String eLocation,
                 Date myStartDate, Date myEndDate, int eDuration, int eAllDay, String eTimezone, String eRRule, long eReminder)
    {
        id = eID;
        calendar_id = currCalID;
        eventName = eTitle;
        eventDescription = eDescr;
        location = eLocation;
        startTime = myStartDate.getDateInMillis();
        reminder = eReminder;
        recordingFileName = "recording" + Long.toString(id);

        allDay = eAllDay; //set to boolean based on 1 or 0
        if(allDay == 1)
        {
            boolAllDay = true;
        }
        timezone = eTimezone;
        recurrence = eRRule;
        if(recurrence != null) {
            duration = eDuration;
            setStartDate(myStartDate);
            setEndDate(myEndDate);
            endTime = -1;
        }
        else{
            endTime = myEndDate.getDateInMillis();
            setStartDate(myStartDate);
            setEndDate(myEndDate);
            duration = -1;
        }
        reminder_id = -1;
    }

    public void setStartDate(long startInMillis) {
        int YEAR, MONTH, HOUR, DAY, MINUTES, SECONDS;
        Calendar calendar = Calendar.getInstance();
        //Start Date
        calendar.setTimeInMillis(startInMillis);
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DAY = calendar.get(Calendar.DAY_OF_MONTH);
        HOUR = calendar.get(Calendar.HOUR);
        MINUTES = calendar.get(Calendar.MINUTE);
        SECONDS = calendar.get(Calendar.SECOND);
        startDate = new Date(YEAR, MONTH, DAY, HOUR, MINUTES, SECONDS);
    }

    public void setStartDate(Date myDate)
    {
        startDate = myDate;
    }

    public void setEndDate(long endInMillis)
    {
        //End Date
        int YEAR, MONTH, HOUR, DAY, MINUTES, SECONDS;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endInMillis);
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DAY = calendar.get(Calendar.DAY_OF_MONTH);
        HOUR = calendar.get(Calendar.HOUR);
        MINUTES = calendar.get(Calendar.MINUTE);
        SECONDS = calendar.get(Calendar.SECOND);
        endDate = new Date(YEAR, MONTH, DAY, HOUR, MINUTES, SECONDS);
    }

    public void setEndDate(Date myDate)
    {
        endDate = myDate;
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
    /***********
     RECURRENCE
    ************/
    String getRecurrence()
    {
        return recurrence;
    }
    /*********
     ALLDAY??
     *********/
    public boolean getAllDay()
    {
        return boolAllDay;
    }
    /********************
     RETURN DATE OBJECTS
     ********************/
    public Date getStartDate()
    {
        return startDate;
    }
    public Date getEndDate()
    {
        return endDate;
    }

    public long getReminder() { return reminder; }
    public void setReminder(long rem) { reminder = rem; }

    public void setReminderID(long id){ reminder_id = id; }
    public long getReminderID(){ return reminder_id; }

    public String toString()
    {
        String myEvent;
        myEvent = "Event " + this.getID() + " in Calendar " +
                this.getCalID() + " named " + this.getName() + " (Description: "
                + this.getDescription() + ") located "
                + this.getLocation() + " starts at "
                + this.getStartDate().getMonth() + " "
                + this.getStartDate().getDay() + ", "
                + this.getStartDate().getYear() + " "
                + this.getStartDate().getHours() + ":"
                + this.getStartDate().getMinutes() + " and ends "
                + this.getEndDate().getHours() + ":"
                + this.getEndDate().getMinutes() + " with a reminder "
                + this.getReminder() + " minutes prior."
                + "\n";
        return myEvent;
    }
//DONE WITH INTENTS
/*
    public void createVoiceMemo()
    {
        VoiceMemo memo = new VoiceMemo(this);
        voiceMemoExists = true;
    }
*/
    public String getRecordingFileName()
    {
        return recordingFileName;
    }
    public String getRecordingFilePath()
    {
        return  Environment.getExternalStorageDirectory().
                getAbsolutePath() + getRecordingFileName();
    }

    public String getHistoryTitle()
    {
        return historyTitle;
    }
    public String getHistoryDescription()
    {
        return historyDescription;
    }
}

