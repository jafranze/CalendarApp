package com.example.jaredfranze.hellow;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;
import java.util.*;

import java.util.Calendar;

public class hellur extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hellur);
        TextView textView = (TextView) findViewById(R.id.myTextView);

        List<myCalendar> calList;
        List<Event> eList;

        calList = new ArrayList<myCalendar>();
        eList = new ArrayList<Event>();

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2014, Calendar.NOVEMBER, 13, 21, 05);
        long start = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2014, Calendar.NOVEMBER, 13, 22, 30);
        long end = endTime.getTimeInMillis();

        TimeZone myTimeZone = TimeZone.getDefault();
        String timez = myTimeZone.toString();

        CalDBCommun myDB = new CalDBCommun(this);

        Event myEvent;
/*
        myEvent = new Event(62, 1, "Test Your Project","Test Description","Test Location",start,end,0,0,timez,null,1);
        myDB.addEvent(myEvent);
*/
        calList = myDB.retrieveCalendarList();
        eList = myDB.retrieveEventList();

        myCalendar cal;
        long myReminder = 10;
        String myString = null;
        //myEvent = new Event(0, 1, "Test Your Project","Test Description","Test Location",start,end,0,0,timez,null,myReminder);
        //myDB.addEvent(myEvent);
/*
        for(int i = 0; i < 100; i++)
        {
            myEvent = new Event(0, 1, "Test Your Project","Test Description","Test Location",start,end,0,0,timez,null,myReminder);
            myDB.addEvent(myEvent);
        }
*/
        boolean myBool;
        myBool = myDB.updateEvent(myDB.findIndexOfEventWithID(479),new Event(64, 1, "Test Update Project","Test Update Description","Test Update Location",start,end,0,0,timez,null,15));
        myString = myString + "Update Event was " + Boolean.toString(myBool) + "\n";

        myBool = myDB.hasConflict(myDB.retrieveEvent(5));
        myString = myString + "Has Conflict (supposed to be true) returns " + Boolean.toString(myBool) + "\n";


        myString = myString + "Calendar list from Calendar Contract.\n";
        Iterator<myCalendar> calIterator = calList.iterator();
        while(calIterator.hasNext()){
            cal = calIterator.next();
            myString = myString + "Calendar " + cal.getID() + " named " + cal.getName() + ".\n";
        }

        myString = myString + "\n\nEvent list from CalendarContract\n";
        Iterator<Event> DBIterator = eList.iterator();
        while(DBIterator.hasNext())
        {
            myString = myString + DBIterator.next().toString();
        }

        //int rows = myDB.deleteAllEvents();
/*
        myString = myString + "After DB deleted all rows (rows deleted: " + rows + ")\n\n";

        eList = myDB.retrieveEventList();
        Iterator<Event> eIterator = eList.iterator();
        while(eIterator.hasNext())
        {
            myString = myString + eIterator.next().toString();
        }
*/
        textView.setText(myString);
        myDB.clearLists();
        eList.clear();
        calList.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hellur, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
