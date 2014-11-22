package com.example.jaredfranze.hellow;

import java.util.Calendar;

/**
 * Created by Jared.Franze on 11/10/2014.
 */
public class Date
{
    int YEAR;
    int MONTH;
    int DAY;
	int HOURS;
    int MINUTES;
    int SECONDS;
    public Date(int year, int month, int day, int hours, int minutes, int seconds)
    {
        YEAR = year;
        MONTH = month;
        DAY = day;
		HOURS = hours;
        MINUTES = minutes;
        SECONDS = seconds;
    }
	public int getYear()
	{
		return YEAR;
	}
	public int getMonth()
	{
		return (MONTH + 1);
	}
    public int getMonthCal()
    {
        int month = getMonth();
        switch(month)
        {
            case 1:
                return Calendar.JANUARY;
            case 2:
                return Calendar.FEBRUARY;
            case 3:
                return Calendar.MARCH;
            case 4:
                return Calendar.APRIL;
            case 5:
                return Calendar.MAY;
            case 6:
                return Calendar.JUNE;
            case 7:
                return Calendar.JULY;
            case 8:
                return Calendar.AUGUST;
            case 9:
                return Calendar.SEPTEMBER;
            case 10:
                return Calendar.OCTOBER;
            case 11:
                return Calendar.NOVEMBER;
            case 12:
                return Calendar.DECEMBER;
            default:
                return Calendar.MAY;
        }
    }

	public int getDay()
	{
		return (DAY);
	}
	public int getHours()
	{
		return HOURS;
	}
	public int getMinutes()
	{
		return MINUTES;
	}
	public int getSeconds()
	{
		return SECONDS;
	}

    public long getDateInMillis()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(YEAR,MONTH,DAY,HOURS,MINUTES);
        return cal.getTimeInMillis();
    }


    public String toString()
    {
        String string = null;
        string = string + "YEAR IS: " + YEAR + " ";
        string = string + "DAY IS " + DAY + " ";
        string = string + "MONTH IS: " + MONTH + " ";
        string = string + "HOURS IS " + HOURS + " ";
        string = string + "MINUTES IS: " + MINUTES + " ";
        string = string + "SECONDS IS " + SECONDS + " ";

        return string;
    }
}
