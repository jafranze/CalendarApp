package com.example.jaredfranze.hellow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jaredfranze.hellow.AgendaItem.AgendaArrayAdapter;
import com.example.jaredfranze.hellow.AgendaItem.DayHeaderItem;
import com.example.jaredfranze.hellow.AgendaItem.EventItem;
import com.example.jaredfranze.hellow.AgendaItem.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class Agenda extends Activity {

    //
    // UI vars
    //

    // Action Bar Day Text Views

    TextView todayViewDayTextView;
    TextView todayViewDayOfWeekTextView;
    TextView todayViewMonthTextView;

    // WeekView UI Items

    ArrayList<TextView> weekViewDayTextView;
    ArrayList<TextView> weekViewBusyTextView;

    // Events

    HashMap<String, ArrayList<Event>> events;

    // Agenda

    ListView agendaListView;
    ArrayList<Item> agendaItems;
    AgendaArrayAdapter adapter;

    //
    // Methods
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        // Initialize Action Bar TextViews

        todayViewDayTextView = (TextView)findViewById(R.id.todayView_day);
        todayViewDayOfWeekTextView = (TextView)findViewById(R.id.todayView_today);
        todayViewMonthTextView = (TextView)findViewById(R.id.todayView_month);

        // Set Action Bar to Today

        setActionBarDay();

        // Initialize Week View UI

        weekViewDayTextView = new ArrayList<TextView>();
        weekViewBusyTextView = new ArrayList<TextView>();

        weekViewDayTextView.add(0, (TextView) findViewById(R.id.weekViewDay1));
        weekViewDayTextView.add(1, (TextView) findViewById(R.id.weekViewDay2));
        weekViewDayTextView.add(2, (TextView) findViewById(R.id.weekViewDay3));
        weekViewDayTextView.add(3, (TextView) findViewById(R.id.weekViewDay4));
        weekViewDayTextView.add(4, (TextView) findViewById(R.id.weekViewDay5));
        weekViewDayTextView.add(5, (TextView) findViewById(R.id.weekViewDay6));
        weekViewDayTextView.add(6, (TextView) findViewById(R.id.weekViewDay7));

        weekViewBusyTextView.add(0, (TextView) findViewById(R.id.weekViewBusy1));
        weekViewBusyTextView.add(1, (TextView) findViewById(R.id.weekViewBusy2));
        weekViewBusyTextView.add(2, (TextView) findViewById(R.id.weekViewBusy3));
        weekViewBusyTextView.add(3, (TextView) findViewById(R.id.weekViewBusy4));
        weekViewBusyTextView.add(4, (TextView) findViewById(R.id.weekViewBusy5));
        weekViewBusyTextView.add(5, (TextView) findViewById(R.id.weekViewBusy6));
        weekViewBusyTextView.add(6, (TextView) findViewById(R.id.weekViewBusy7));

        // Initialize Events

        events = new HashMap<String, ArrayList<Event>>();
       // loadEvents();
        //System.out.println(events.toString());

        // Set Week View to Today

        setWeekViewToThisWeek();

        // Initialize Agenda

        agendaListView = (ListView)findViewById(R.id.agendaListView);
        agendaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(adapterView.getAdapter().getItem(i).toString());
                EventItem eitem = (EventItem)adapterView.getAdapter().getItem(i);

                if (eitem.itemType() == EventItem.ITEM_TYPE_EVENT) {
                    Event event = eitem.getEvent();

                    Intent intent = new Intent(getApplicationContext(), AddEvent.class);
                    intent.putExtra("event_id", event.getID());
                    startActivity(intent);
                } else if(eitem.itemType() == EventItem.ITEM_TYPE_HISTORY) {

                    ThisDayInHistory history = new ThisDayInHistory();
                    String historyTitle = history.getTitle();
                    String historyDescription = history.getDescription();

                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage(historyDescription)
                            .setTitle(historyTitle);

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        events.clear();
        loadEvents();
        loadAgenda();
        setWeekViewToThisWeek();
    }

    // Agenda

    private void loadAgenda() {

        System.out.println("I EXIST la");

        ArrayList<String> dateKeys = new ArrayList<String>();

        for (HashMap.Entry<String,ArrayList<Event>> entry : events.entrySet()) {
            dateKeys.add(entry.getKey());
        }

        // always add today, always add tomorrow

        String todayKey = getDayKey(Calendar.getInstance());
        if (!events.containsKey(todayKey)) dateKeys.add(todayKey);

        Calendar tomorrowc = Calendar.getInstance();
        tomorrowc.add(Calendar.DAY_OF_YEAR, 1);
        String tomorrowKey = getDayKey(tomorrowc);
        if (!events.containsKey(tomorrowKey)) dateKeys.add(tomorrowKey);

        // sort up the keys and get to loading the events

        Collections.sort(dateKeys, new dateKeyComparator());

        ArrayList<Item> items = new ArrayList<Item>();

        // this day

        boolean hist = false;

        for (int i = 0; i < dateKeys.size(); i++) {
            //System.out.println("KEY: " + dateKeys.get(i));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Calendar date = Calendar.getInstance();
                date.setTime(formatter.parse(dateKeys.get(i)));
                DayHeaderItem dayHeaderItem = new DayHeaderItem(date);
                items.add(dayHeaderItem);

                if (!hist) {
                    EventItem eventItem = new EventItem(null, "This Day in History", null, null, EventItem.ITEM_TYPE_HISTORY, false);
                    items.add(eventItem);

                    hist = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ArrayList<Event> dayEvents = new ArrayList<Event>();

            if (events.containsKey(dateKeys.get(i))) {
                dayEvents = events.get(dateKeys.get(i));
                Collections.sort(dayEvents, new eventComparator());

                for (int e = 0; e < dayEvents.size(); e++) {
                    Event event = dayEvents.get(e);
                    Calendar eventc = Calendar.getInstance();
                    eventc.set(event.getStartDate().getYear(), (event.getStartDate().getMonth() - 1), (event.getStartDate().getDay() - 1), event.getStartDate().getHours(), event.getStartDate().getMinutes(), event.getStartDate().getSeconds());
                    EventItem eventItem = new EventItem(event, event.eventName, eventc, null, EventItem.ITEM_TYPE_EVENT, (event.getReminder() != 0));
                    items.add(eventItem);
                }
            }
        }

        if (adapter == null) {
            adapter = new AgendaArrayAdapter(this, items);
            agendaListView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
            agendaListView.invalidateViews();
            agendaListView.refreshDrawableState();
        }
    }

    // Week View

    private void setWeekViewToThisWeek() {
        setWeekViewToWeekOf(Calendar.getInstance());
    }

    private void setWeekViewToWeekOf(Date date) {
        Calendar datec = Calendar.getInstance();
        datec.set(date.getYear(), date.getMonth(), date.getDay(), date.getHours(), date.getMinutes(), date.getSeconds());
        setWeekViewToWeekOf(datec);
    }

    private void setWeekViewToWeekOf(Calendar datec) {
        // Get the first day of the week of this day

        while (datec.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            datec.add(Calendar.DAY_OF_YEAR, -1);
        }

        // Got Sunday, set the whole week

        for (int day = Calendar.SUNDAY; day < (Calendar.SATURDAY + 1); day++) {
            TextView dayTextView = weekViewDayTextView.get(day - 1);
            String dayOfTheWeek = Integer.valueOf(datec.get(Calendar.DAY_OF_MONTH)).toString();
            dayTextView.setText(dayOfTheWeek);

            Calendar todayc = Calendar.getInstance();
            todayc.set(Calendar.HOUR, 0);
            todayc.set(Calendar.MINUTE, 0);
            todayc.set(Calendar.SECOND, 0);

            if (datec.before(todayc)) {
                dayTextView.setAlpha(0.25f);
            }

            // Get the day's day key & check for events

            TextView busyTextView = weekViewBusyTextView.get(day - 1);
            //busyTextView.setVisibility(events.containsKey(getDayKey(datec)) ? View.VISIBLE : View.INVISIBLE);
            busyTextView.setAlpha(events.containsKey(getDayKey(datec)) ? 1.0f : 0.25f);

            todayc = Calendar.getInstance();
            if (datec.get(Calendar.YEAR) == todayc.get(Calendar.YEAR)
                    && datec.get(Calendar.DAY_OF_YEAR) == todayc.get(Calendar.DAY_OF_YEAR)) {
                // It's today
                weekViewDayTextView.get(day - 1).setTextColor(Color.parseColor("#FC6666"));
            }

            datec.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    // Event Dictionary Initialization

    private void loadEvents() {

        CalDBCommun calDatabase = new CalDBCommun(this);

        List<Event> eventList = calDatabase.retrieveEventList();
        Iterator<Event> eventIterator = eventList.iterator();
        while(eventIterator.hasNext()) {
            Event event = eventIterator.next();

            // Get event's day key
            Calendar todayc = Calendar.getInstance();
            todayc.set(Calendar.HOUR, 0);
            todayc.set(Calendar.MINUTE, 0);
            todayc.set(Calendar.SECOND, 0);

            Calendar eventc = Calendar.getInstance();
            eventc.set(event.getStartDate().getYear(), (event.getStartDate().getMonth() - 1), (event.getStartDate().getDay() - 1), event.getStartDate().getHours(), event.getStartDate().getMinutes(), event.getStartDate().getSeconds());
            String key = getDayKey(eventc);

            // event check, kill past events

            if (eventc.before(todayc)) {
                continue;
            }

            // If it's in the map, add it to the array, if not, start anew

            if (events.containsKey(key)) {
                ArrayList<Event> dayEvents = events.get(key);
                dayEvents.add(event);
                events.put(key, dayEvents);
            } else {
                ArrayList<Event> dayEvents = new ArrayList<Event>();
                dayEvents.add(event);
                events.put(key, dayEvents);
            }
        }
    }

    String getDayKey(Calendar day) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(day.getTime());
    }

    // Action Bar

    private void setActionBarDay() {
        setActionBarDay(Calendar.getInstance());
    }

    private void setActionBarDay(Date date) {
        Calendar datec = Calendar.getInstance();
        datec.set(date.getYear(), date.getMonth(), date.getDay(), date.getHours(), date.getMinutes(), date.getSeconds());
        setActionBarDay(datec);
    }

    private void setActionBarDay(Calendar datec) {
        Calendar todayc = Calendar.getInstance();

        Calendar yesterdayc = Calendar.getInstance();
        yesterdayc.add(Calendar.DAY_OF_YEAR, -1);

        Calendar tomorrowc = Calendar.getInstance();
        tomorrowc.add(Calendar.DAY_OF_YEAR, 1);

        String dayString;

        if (datec.get(Calendar.YEAR) == todayc.get(Calendar.YEAR)
                && datec.get(Calendar.DAY_OF_YEAR) == todayc.get(Calendar.DAY_OF_YEAR)) {
            // it's today
            dayString = "Today";
        } else if (datec.get(Calendar.YEAR) == yesterdayc.get(Calendar.YEAR)
                && datec.get(Calendar.DAY_OF_YEAR) == yesterdayc.get(Calendar.DAY_OF_YEAR)) {
            // it's yesterday
            dayString = "Yesterday";
        } else if (datec.get(Calendar.YEAR) == tomorrowc.get(Calendar.YEAR)
                && datec.get(Calendar.DAY_OF_YEAR) == tomorrowc.get(Calendar.DAY_OF_YEAR)) {
           // it's tomorrow
            dayString = "Tomorrow";
        } else {
            dayString = datec.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        }

        todayViewDayOfWeekTextView.setText(dayString);
        todayViewMonthTextView.setText(datec.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()).toUpperCase());

        String day = Integer.valueOf(datec.get(Calendar.DAY_OF_MONTH)).toString();
        if (datec.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + day;
        }
        todayViewDayTextView.setText(day);
    }

    // onclick

    public void didClickAddEvent(View v) {
        startActivity(new Intent(getApplicationContext(), AddEvent.class));
    }

    public void didClickMonthView(View v) {
        startActivity(new Intent(getApplicationContext(), MonthView.class));
    }

    // App Lifecycle

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agenda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class dateKeyComparator implements Comparator<String>
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        public int compare(String lhs, String rhs) {
            try {
                return dateFormat.parse(lhs).compareTo(dateFormat.parse(rhs));
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    class eventComparator implements Comparator<Event>
    {
        public int compare(Event lhs, Event rhs) {
            Calendar lhsc = Calendar.getInstance();
            lhsc.set(lhs.getStartDate().getYear(), (lhs.getStartDate().getMonth() - 1), (lhs.getStartDate().getDay() - 1), lhs.getStartDate().getHours(), lhs.getStartDate().getMinutes(), lhs.getStartDate().getSeconds());

            Calendar rhsc = Calendar.getInstance();
            rhsc.set(rhs.getStartDate().getYear(), (rhs.getStartDate().getMonth() - 1), (rhs.getStartDate().getDay() - 1), rhs.getStartDate().getHours(), rhs.getStartDate().getMinutes(), rhs.getStartDate().getSeconds());

            return lhsc.compareTo(rhsc);
        }
    }
}