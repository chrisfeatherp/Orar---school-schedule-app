package ro.liisorar.app.models;

import android.content.ContentValues;
import android.database.Cursor;

import ro.liisorar.app.data.DataContract.ClassesColumns;


public class Class {

    public String objectId ;
    public String name ;

    public Class(String id, String name) {
        this.objectId = id;
        this.name = name;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ClassesColumns.CLASS_ID, objectId);
        values.put(ClassesColumns.CLASS_NAME, name);
        return values;
    }

    public static Class fromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(ClassesColumns.CLASS_ID));
        String name = cursor.getString(cursor.getColumnIndex(ClassesColumns.CLASS_NAME));
        return new Class(id, name);
    }
}
