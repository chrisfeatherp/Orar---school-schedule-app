package ro.liisorar.app.models;

import android.content.ContentValues;
import android.database.Cursor;

import ro.liisorar.app.data.DataContract;

public class Version {

    private String objectId;
    private int data_version;

    public Version(int data_version){
        this.data_version = data_version;
    }
    public Version(String id, int data_version){
        this.objectId = id;
        this.data_version = data_version;
    }

    public int getVersion(){
        return this.data_version;
    }
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DataContract.VersionsColumns.VERSION_ID, objectId);
        values.put(DataContract.VersionsColumns.VERSION_NR, data_version);
        return values;
    }

    public static Version fromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(DataContract.VersionsColumns.VERSION_ID));
        int data_version = cursor.getInt(cursor.getColumnIndex(DataContract.VersionsColumns.VERSION_NR));
        return new Version(id, data_version);
    }
}
