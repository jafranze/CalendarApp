package com.example.jaredfranze.hellow;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jaredfranze.hellow.Agenda;import com.example.jaredfranze.hellow.CalDBCommun;import com.example.jaredfranze.hellow.Event;import com.example.jaredfranze.hellow.R;import java.lang.Integer;import java.lang.Override;import java.lang.String;import java.lang.System;import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by izuchukwuelechi on 12/4/14.
 */
public class ScreenSlidePageFragment extends Fragment {

    Calendar weekOfDate;

    ArrayList<TextView> weekViewDayTextView;
    ArrayList<TextView> weekViewBusyTextView;

    // Events

    HashMap<String, ArrayList<Event>> events;
    boolean awaitingAttach;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle data = getArguments();
        weekOfDate = Calendar.getInstance();
        weekOfDate.setTimeInMillis(data.getLong("date"));

        awaitingAttach = false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //events = new HashMap<String, ArrayList<Event>>();
        //loadEvents();
        Agenda agendaActivity = (Agenda) getActivity();
        events = agendaActivity.getEvents();

        if (awaitingAttach) {
            awaitingAttach = false;
            setWeekViewToWeekOf(weekOfDate);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.agenda_week_view, container, false);

        weekViewDayTextView = new ArrayList<TextView>();
        weekViewBusyTextView = new ArrayList<TextView>();

        weekViewDayTextView.add(0, (TextView) rootView.findViewById(R.id.weekViewDay1));
        weekViewDayTextView.add(1, (TextView) rootView.findViewById(R.id.weekViewDay2));
        weekViewDayTextView.add(2, (TextView) rootView.findViewById(R.id.weekViewDay3));
        weekViewDayTextView.add(3, (TextView) rootView.findViewById(R.id.weekViewDay4));
        weekViewDayTextView.add(4, (TextView) rootView.findViewById(R.id.weekViewDay5));
        weekViewDayTextView.add(5, (TextView) rootView.findViewById(R.id.weekViewDay6));
        weekViewDayTextView.add(6, (TextView) rootView.findViewById(R.id.weekViewDay7));

        weekViewBusyTextView.add(0, (TextView) rootView.findViewById(R.id.weekViewBusy1));
        weekViewBusyTextView.add(1, (TextView) rootView.findViewById(R.id.weekViewBusy2));
        weekViewBusyTextView.add(2, (TextView) rootView.findViewById(R.id.weekViewBusy3));
        weekViewBusyTextView.add(3, (TextView) rootView.findViewById(R.id.weekViewBusy4));
        weekViewBusyTextView.add(4, (TextView) rootView.findViewById(R.id.weekViewBusy5));
        weekViewBusyTextView.add(5, (TextView) rootView.findViewById(R.id.weekViewBusy6));
        weekViewBusyTextView.add(6, (TextView) rootView.findViewById(R.id.weekViewBusy7));

        if (getActivity() == null) {
            awaitingAttach = true;
        } else {
            setWeekViewToWeekOf(weekOfDate);
        }

        return rootView;
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

    String getDayKey(Calendar day) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(day.getTime());
    }

    // Event Dictionary Initialization

    private void loadEvents() {

        CalDBCommun calDatabase = new CalDBCommun(getActivity().getApplicationContext());

        List<Event> eventList = calDatabase.retrieveEventList();
        Iterator<Event> eventIterator = eventList.iterator();
        while(eventIterator.hasNext()) {
            Event event = eventIterator.next();

            // Get event's day key
            Calendar todayc = Calendar.getInstance();
            todayc.set(Calendar.HOUR_OF_DAY, 0);
            todayc.set(Calendar.MINUTE, 0);
            todayc.set(Calendar.SECOND, 0);

            Calendar eventc = Calendar.getInstance();
            eventc.set(event.getStartDate().getYear(), (event.getStartDate().getMonth() - 1), (event.getStartDate().getDay()), event.getStartDate().getHours(), event.getStartDate().getMinutes(), event.getStartDate().getSeconds());
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
            System.out.println(event.toString());
        }
    }
}