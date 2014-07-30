package ro.liisorar.app.models;

import android.content.ContentValues;
import android.database.Cursor;

import ro.liisorar.app.data.DataContract.BlocksColumns;


public class Block {


    public String objectId ;
    public String name ;
    public String classId ;
    public String day;
    public String start ;
    public String end ;



    public Block(String id, String name, String _class, String day, String start, String end) {
        this.objectId = id;
        this.name = name;
        this.day = day;
        this.classId = _class;
        this.start = start;
        this.end = end;
    }
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(BlocksColumns.BLOCK_ID, objectId);
        values.put(BlocksColumns.BLOCK_NAME, name);
        values.put(BlocksColumns.CLASS_ID, classId);
        values.put(BlocksColumns.BLOCK_DAY, day);
        values.put(BlocksColumns.BLOCK_START, start);
        values.put(BlocksColumns.BLOCK_END, end);
        return values;
    }


    public static Block fromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(BlocksColumns.BLOCK_ID));
        String name = cursor.getString(cursor.getColumnIndex(BlocksColumns.BLOCK_NAME));
        String _class = cursor.getString(cursor.getColumnIndex(BlocksColumns.CLASS_ID));
        String day = cursor.getString(cursor.getColumnIndex(BlocksColumns.BLOCK_DAY));
        String start = cursor.getString(cursor.getColumnIndex(BlocksColumns.BLOCK_START));
        String end = cursor.getString(cursor.getColumnIndex(BlocksColumns.BLOCK_END));
        return new Block(id, name, _class, day, start, end);
    }


}

