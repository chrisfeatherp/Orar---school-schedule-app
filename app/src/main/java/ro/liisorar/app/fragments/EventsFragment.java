package ro.liisorar.app.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ro.liisorar.app.R;
import ro.liisorar.app.adapters.EventsListAdapter;
import ro.liisorar.app.data.DataContract;
import ro.liisorar.app.models.Event;

public class EventsFragment extends ListFragment {

    ListView mListView;
    EventsListAdapter mAdapter;

    private String[] mMonths = {"Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate( R.layout.fragment_events, container, false);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mListView = (ListView) getListView();
        mListView.setItemsCanFocus(true);
        mListView.setCacheColorHint(Color.WHITE);
        mListView.setSelector(android.R.color.transparent);

        mAdapter = new EventsListAdapter(getActivity());
        setListAdapter(mAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // setting onItemLongClickListener and passing the position to the function
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                removeItemFromList(position);

                return true;
            }
        });

        readFromContentProvider();
    }
    @Override
    public void onResume() {
        super.onResume();
        readFromContentProvider();
    }

    private void readFromContentProvider() {
        Cursor cursor = getActivity().getContentResolver().query(DataContract.Events.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            mAdapter.clear();
            while (cursor.moveToNext()) {
                Event mTempEvent = Event.fromCursor(cursor);
                String[] mDate = mTempEvent.date.split("-");
                String mFinalDate = mDate[2] + " " + mMonths[Integer.parseInt(mDate[1])] + ", " + mDate[0] + " " +
                                    mDate[3] + ":" + mDate[4];
                mAdapter.addItem(new Event(mTempEvent.id, mTempEvent.name, mFinalDate));
            }
            cursor.close();
        }
    }
    private void removeItemFromList(int position) {
        final int deletePosition = position;

        AlertDialog.Builder mAlert = new AlertDialog.Builder(
                getActivity());

        mAlert.setTitle("Sterge");
        mAlert.setMessage("Esti sigur ca vrei sa stergi acest eveniment ?");
        mAlert.setPositiveButton("DA", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mAdapter.remove(deletePosition);
            }
        });

        mAlert.setNegativeButton("NU", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mAlert.show();

    }
}
