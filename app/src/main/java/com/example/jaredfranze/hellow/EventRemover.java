package com.example.jaredfranze.hellow;

import android.content.Context;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.CalendarContract;

/**
 * Created by Jared.Franze on 11/13/2014.
 */
public class EventRemover
{
    Event currEvent;
    ContentResolver cr;
    ContentValues values;
    Uri deleteUri;
    Uri e_uri;
    long eID;

    public EventRemover(Context myContext) {
        eID = 0;
        cr = myContext.getContentResolver();
        values = new ContentValues();
        deleteUri = null;
        e_uri = CalendarContract.Events.CONTENT_URI;
    }

    public int deleteEvent(long id)
    {
        eID = id;
        deleteUri = ContentUris.withAppendedId(e_uri, eID);
        int rows = cr.delete(deleteUri, null, null);

        return rows;
    }

    public int deleteEvent(Event myEvent)
    {
        eID = myEvent.getID();
        deleteUri = ContentUris.withAppendedId(e_uri, eID);
        int rows = cr.delete(deleteUri, null, null);

        return rows;
    }
}