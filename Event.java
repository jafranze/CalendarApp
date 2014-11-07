public class Event
{
   String eventName;
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
   int eventRecurrence = -1;
   int eventDate, startTime, endTime;
   boolean allDay;
   
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
   void setStartTime(int newTime)
   {
      startTime = newTime;
   }
   void setEndTime(int newTime)
   {
      endTime = newTime;
   }
   int getStartTime()
   {
      return startTime;
   }
   int getEndTime()
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
    **************/
   void setRecurrence(int recurrence)
   {
      eventRecurrence = recurrence;
   }
   int getRecurrence()
   {
      return eventRecurrence;
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
