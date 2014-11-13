package ourJava;

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
		return MONTH;
	}
	public int getDay()
	{
		return DAY;
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
}
