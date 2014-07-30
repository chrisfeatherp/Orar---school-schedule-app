package ro.liisorar.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ro.liisorar.app.R;
import ro.liisorar.app.data.DataContract;
import ro.liisorar.app.models.Event;

public class EventsListAdapter extends BaseAdapter {
    //  adapter-ul pentru evenimente
    //  afiseaza pt fiecare element din lista informatiile corespunzatoare

    private Activity mContext;
    private LayoutInflater mLayoutInflater = null;

    // lista in care tinem evenimetele
    private ArrayList<Event> mListItems = new ArrayList<Event>();

    public EventsListAdapter(Activity context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // functie pt a elimina evenimentele din lista
    public void clear() {
        mListItems.clear();
        // aceasta functie reactualizeaza lista (reapeleaza getView() pr fiecare element din lista )
        notifyDataSetChanged();
    }

    // functie pt a elimina un eveniment corespunzator pozitiei date
    public void remove(int pos) {

        Log.d("ORAR",DataContract.Events.CONTENT_URI.buildUpon().appendPath(Integer.toString(mListItems.get(pos).id)).build().toString());
        int numDeleted = mContext.getContentResolver().delete(
                DataContract.Events.CONTENT_URI.buildUpon().appendPath(Integer.toString(mListItems.get(pos).id)).build(), null, null);
        mListItems.remove(pos);
        notifyDataSetChanged();
    }

    // functie pt a adauga un eveniment (la finalul listei)
    public void addItem(final Event event) {
        mListItems.add(event);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int pos) {
        return mListItems.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getCount() {
        return mListItems.size();
    }

    // functie ce returneaza view-ul corespunzator pt fiecare element
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Event mEvent = (Event) mListItems.get(position);

        // initializam view-ul cu layout-ul corespinzator doar in cazul in care acesta nu a fost initializat deja
        // pt fiecare element foloseste acelasi layout, view-ul va fi initializat doar pt primul element
        if (v == null) {
            v = mLayoutInflater.inflate(R.layout.list_item_event, null);
        }

        final TextView name = (TextView) v.findViewById(R.id.event_title);
        final TextView date = (TextView) v.findViewById(R.id.event_subtitle);

        name.setText(mEvent.name);
        date.setText(mEvent.date);

        return v;
    }


}
