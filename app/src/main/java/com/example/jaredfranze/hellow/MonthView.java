package com.example.jaredfranze.hellow;

/*
    Created by Andrei Stringfellow
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;

/**
 * Created by Andrei on 11/17/14.
 */

public class MonthView extends Activity
{

    Calendar currDate;
    CalendarView cal;
    int year, month, day;
    long date;
    Button selectMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_month_view);


        final Button swapView =(Button)findViewById(R.id.monthView);
        swapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),Agenda.class));
            }
        });

        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);
        cal = (CalendarView) findViewById(R.id.calendar_view);
        selectMonth = (Button) findViewById(R.id.selectMonth);

        date = cal.getDate();

        OnClickListener ocl = new OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                new DatePickerDialog(MonthView.this, dListener, year, month, day).show();
            }
        };

        selectMonth.setOnClickListener(ocl);

        cal.setOnDateChangeListener(new OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                if (cal.getDate() != date) {
                    date = cal.getDate();
                    Toast.makeText(view.getContext(), (month + 1) + "/" + dayOfMonth + "/" + year, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), Agenda.class);
                    i.putExtra("month", month);
                    i.putExtra("day", dayOfMonth);
                    i.putExtra("year", year);
                    startActivity(i);
                }
            }
        });

        /*
        OnDateChangeListener odc = new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int y, int m, int d) {

                Toast.makeText(getApplicationContext(), "Chosen date: " + (month + 1) + "-" + day + "-" +
                        year, Toast.LENGTH_SHORT);
            }
        };

        cal.setOnDateChangeListener(odc);
        */
    }

    private DatePickerDialog.OnDateSetListener dListener = new DatePickerDialog.OnDateSetListener()
    {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d)
            {
                currDate = Calendar.getInstance();
                currDate.set(y,m,d);
                cal.setDate(currDate.getTimeInMillis());
            }
     };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.month_view, menu);
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
