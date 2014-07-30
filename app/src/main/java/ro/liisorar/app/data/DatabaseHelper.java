package ro.liisorar.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import ro.liisorar.app.data.DataContract.BlocksColumns;
import ro.liisorar.app.data.DataContract.ClassesColumns;
import ro.liisorar.app.data.DataContract.VersionsColumns;
import ro.liisorar.app.data.DataContract.EventsColumns;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "orar.db";
    private static final int DATABASE_VERSION = 1;

    private final Context mContext;

    public interface Tables {
        String BLOCKS = "blocks";
        String CLASSES = "classes";
        String VERSIONS = "versions";
        String EVENTS = "events";
    }

    private interface References {
        String CLASS_ID = "REFERENCES " + Tables.CLASSES + "(" + ClassesColumns.CLASS_ID + ")";

    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Tables.BLOCKS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BlocksColumns.BLOCK_ID + " TEXT NOT NULL,"
                + BlocksColumns.CLASS_ID + " TEXT " + References.CLASS_ID + ","
                + BlocksColumns.BLOCK_DAY + " TEXT NOT NULL,"
                + BlocksColumns.BLOCK_NAME + " TEXT NOT NULL,"
                + BlocksColumns.BLOCK_START + " TEXT NOT NULL,"
                + BlocksColumns.BLOCK_END + " TEXT NOT NULL,"
                + "UNIQUE (" + BlocksColumns.BLOCK_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.CLASSES + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ClassesColumns.CLASS_ID + " TEXT NOT NULL,"
                + ClassesColumns.CLASS_NAME + " TEXT NOT NULL,"
                + "UNIQUE (" + ClassesColumns.CLASS_ID + " , " + ClassesColumns.CLASS_NAME +") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.VERSIONS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VersionsColumns.VERSION_ID + " TEXT NOT NULL,"
                + VersionsColumns.VERSION_NR + " INTEGER NOT NULL,"
                + "UNIQUE (" + VersionsColumns.VERSION_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.EVENTS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EventsColumns.EVENT_NAME + " TEXT NOT NULL,"
                + EventsColumns.EVENT_DATE + " TEXT NOT NULL,"
                + "UNIQUE (" + EventsColumns.EVENT_DATE + ") ON CONFLICT REPLACE)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.BLOCKS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.CLASSES);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.VERSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.EVENTS);

        onCreate(db);
    }


}
