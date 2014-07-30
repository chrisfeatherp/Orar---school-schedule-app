package ro.liisorar.app.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import ro.liisorar.app.data.DataContract;

public class Event {

    public int id ;
    public String name ;
    public String date ;

    public Event(String name, String date) {
        this.name = name;
        this.date = date;
    }
    public Event(int id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DataContract.EventsColumns.EVENT_NAME, name);
        values.put(DataContract.EventsColumns.EVENT_DATE, date);
        return values;
    }

    public static Event fromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
        String name = cursor.getString(cursor.getColumnIndex(DataContract.EventsColumns.EVENT_NAME));
        String date = cursor.getString(cursor.getColumnIndex(DataContract.EventsColumns.EVENT_DATE));
        return new Event(id, name, date);
    }

}
