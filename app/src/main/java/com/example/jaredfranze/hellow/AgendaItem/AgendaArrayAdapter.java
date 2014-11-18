package com.example.jaredfranze.hellow.AgendaItem;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jaredfranze.hellow.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by izuchukwuelechi on 11/13/14.
 */
public class AgendaArrayAdapter extends ArrayAdapter<Item> {


    private Context context;
    private ArrayList<Item> items;
    private LayoutInflater layoutInflater;

    public AgendaArrayAdapter(Context context, ArrayList<Item> agendaEventItems) {
        super(context, 0, agendaEventItems);
        this.context = context;
        this.items = agendaEventItems;

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final Item item = items.get(position);
        if (item != null) {
            if(item.isHeaderItem()){
                DayHeaderItem dayHeaderItem = (DayHeaderItem)item;
                view = layoutInflater.inflate(R.layout.agenda_item_day, null);

                view.setOnClickListener(null);
                view.setOnLongClickListener(null);
                view.setLongClickable(false);

                TextView titleView = (TextView) view.findViewById(R.id.agenda_item_day);
                System.out.println(dayHeaderItem.getTitle());
                System.out.println(titleView.toString());
                titleView.setText(dayHeaderItem.getTitle());

            }else{
                EventItem eventItem = (EventItem)item;
                view = layoutInflater.inflate(R.layout.agenda_item_event, null);

                TextView titleView = (TextView)view.findViewById(R.id.agenda_item_event_title);
                TextView timeView = (TextView)view.findViewById(R.id.agenda_item_event_time);
                ImageView iconView = (ImageView)view.findViewById(R.id.agenda_item_event_icon);
                ImageButton reminderButton = (ImageButton)view.findViewById(R.id.agenda_item_event_reminder);

                titleView.setText(eventItem.getTitle());
                timeView.setText(eventItem.getStartTime());
                //iconView.setImageDrawable(eventItem.getIcon().getIconDrawable());
                reminderButton.setVisibility((eventItem.hasReminders() ? View.VISIBLE : View.INVISIBLE));
            }
        }
        return view;
    }
}
