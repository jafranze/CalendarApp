package com.example.jaredfranze.hellow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class AddEvent extends Activity {

    Event event;
    boolean newEvent;

    Date startDate;
    Date endDate;

    long remindertime;

    TextView datetv;
    TextView daytv;
    TextView monthtv;

    TextView starttv;
    TextView endtv;
    TextView titletv;

    ImageButton reminderib;
    ImageButton voiceib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);

        datetv = (TextView)findViewById(R.id.add_event_day);
        daytv = (TextView)findViewById(R.id.add_event_dow);
        monthtv = (TextView)findViewById(R.id.add_event_month);

        starttv = (TextView)findViewById(R.id.add_event_time_start);
        endtv = (TextView)findViewById(R.id.add_event_end);
        titletv = (TextView)findViewById(R.id.add_event_title);

        reminderib = (ImageButton)findViewById(R.id.add_event_reminder);
        voiceib = (ImageButton)findViewById(R.id.add_event_voice);

        voiceib.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                didLongClickEventVoice(view);
                return false;
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("event_id")) {
            CalDBCommun myDB = new CalDBCommun(this);
            //myDB.retrieveEventList();

            long eventID = (long)extras.getLong("event_id");
            event = myDB.retrieveEvent(myDB.findIndexOfEventWithID(eventID));
        }

        if (event != null) {
            newEvent = false;

            startDate = event.startDate;
            endDate = event.endDate;
            remindertime = event.getReminder();

            titletv.setText(event.getName());

            reminderib.setAlpha(event.reminder != 0 ? 1.0f : 0.3f);

            refreshAllDates();
        } else {
            event = new Event();
            newEvent = true;
            event.changeName("All-Nighter");

            Calendar today = Calendar.getInstance();

            startDate = new Date(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE), today.get(Calendar.SECOND));

            today.add(Calendar.HOUR, 1);
            endDate = new Date(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE), today.get(Calendar.SECOND));

            voiceib.setVisibility(View.INVISIBLE);

            reminderib.setAlpha(0.3f);
            remindertime = 0;

            refreshAllDates();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (new File(event.getRecordingFilePath()).exists()) {
            voiceib.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
        }
    }

    private void refreshAllDates() {

        Calendar datec = Calendar.getInstance();

        // refresh big date

        Date date = startDate;
        datec.set(date.getYear(), (date.getMonth() - 1), (date.getDay()), date.getHours(), date.getMinutes(), date.getSeconds());

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
        } else {
            dayString = datec.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        }

        daytv.setText(dayString);
        monthtv.setText(datec.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()).toUpperCase());

        String day = Integer.valueOf(datec.get(Calendar.DAY_OF_MONTH)).toString();
        if (datec.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + day;
        }
        datetv.setText(day);

        // refresh start & end times

        starttv.setText(getTimeString(true));
        endtv.setText(getTimeString(false));
    }

    // utility

    private String getTimeString(boolean isStart) {

        Calendar datec = Calendar.getInstance();
        Date date = (isStart ? date = startDate : endDate);

        datec.set(date.getYear(), (date.getMonth() - 1), (date.getDay()), date.getHours(), date.getMinutes(), date.getSeconds());

        int ampm = datec.get(Calendar.AM_PM);

        if ((ampm == 0) && (datec.get(Calendar.HOUR) == 0)) {
            return "Midnight";
        } else if ((ampm == 1) && (datec.get(Calendar.HOUR) == 0)) {
            return "Noon";
        }

        if (datec.get(Calendar.MINUTE) == 0) {
            SimpleDateFormat formatter = new SimpleDateFormat("h");
            return formatter.format(datec.getTime()) + ((ampm == 0) ? "a" : "p");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm");
            return formatter.format(datec.getTime()) + ((ampm == 0) ? "a" : "p");
        }
    }

    // onClick Listeners

    public void didClickEventDate(View v) {
        new DatePickerDialog(this, dateListener, startDate.getYear(), startDate.getMonth() - 1, startDate.getDay()).show();
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d)
        {
            startDate.MONTH = m;
            startDate.YEAR = y;
            startDate.DAY = d;
            endDate.MONTH = m;
            endDate.YEAR = y;
            endDate.DAY = d;
            refreshAllDates();
        }
    };

    public void didClickEventTime(View v) {
        new TimePickerDialog(this, timeListener, startDate.getHours(), startDate.getMinutes(), false).show();
    }

    private TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker picker, int h, int m)
        {
            startDate.HOURS = h;
            startDate.MINUTES = m;
            refreshAllDates();
        }
    };

    public void didClickEventEnd(View v) {
        new TimePickerDialog(this, endTimeListener, endDate.getHours(), endDate.getMinutes(), false).show();
    }

    private TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker picker, int h, int m)
        {
            endDate.HOURS = h;
            endDate.MINUTES = m;
            refreshAllDates();
        }
    };

    public void didClickEventTitle(View v) {
        final EditText input = new EditText(this);
        input.setText(event.getName());
        input.selectAll();
        new AlertDialog.Builder(this)
                .setTitle("Event Title")
                .setMessage("")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        event.changeName(value.toString());
                        titletv.setText(value.toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    public void didClickEventReminder(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Reminder")
                .setItems(R.array.reminders_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item

                        int m = 0;

                        switch (which) {
                            case 0:
                                m = 5;
                                break;
                            case 1:
                                m = 10;
                                break;
                            case 2:
                                m = 15;
                                break;
                            case 3:
                                m = 30;
                                break;
                            case 4:
                                m = 60;
                                break;
                            case 5:
                                m = 120;
                                break;
                            case 6:
                                m = 0;
                                break;
                        }

                        remindertime = m;

                        reminderib.setAlpha(m != 0 ? 1.0f : 0.3f);
                    }
                });
        builder.create().show();
    }

    public void didClickEventVoice(View v) {
        if (!newEvent) {
            if (new File(event.getRecordingFilePath()).exists()) {
                MediaPlayer m = new MediaPlayer();
                try {
                    m.setDataSource(event.getRecordingFilePath());
                    m.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                m.start();
                Toast.makeText(getApplicationContext(), "Playing Voice Memo", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(getApplicationContext(), VoiceMemo.class);
                i.putExtra("event", event.getID());
                startActivity(i);
            }
        }
    }

    public void didLongClickEventVoice(View v) {
        if (!newEvent && (new File(event.getRecordingFilePath()).exists())) {
            Intent i = new Intent(getApplicationContext(), VoiceMemo.class);
            i.putExtra("event", event.getID());
            startActivity(i);
        }
    }

    public void didClickEventCancel(View v) {
        finish();
    }

    public void didClickEventDone(View v) {
        CalDBCommun myDB = new CalDBCommun(this);

        List<myCalendar> calList = new ArrayList<myCalendar>();
        List<Event>eList = new ArrayList<Event>();

        calList = myDB.retrieveCalendarList();
        eList = myDB.retrieveEventList();

        TimeZone myTimeZone = TimeZone.getDefault();
        String timez = myTimeZone.toString();

        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setReminder(remindertime);
        event.changeName(titletv.getText().toString());
        event.setCalID(calList.get(0).getID());
        event.setTimezone(timez);

        if (newEvent) {
            myDB.addEvent(event);
        } else {
            myDB.updateEvent(myDB.findIndexOfEventWithID(event.getID()), event);
        }

        System.out.println(startDate.toString());
        System.out.println(endDate.toString());

        finish();
    }

    // More

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_add_event, menu);
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
}
