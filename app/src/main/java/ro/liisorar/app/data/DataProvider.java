package ro.liisorar.app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import ro.liisorar.app.data.DataContract.BlocksColumns;
import ro.liisorar.app.data.DataContract.ClassesColumns;
import ro.liisorar.app.data.DataContract.EventsColumns;

public class DataProvider extends ContentProvider {

    private DatabaseHelper dbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int BLOCKS = 100;
    private static final int BLOCKS_ID = 102;
    private static final int CLASSES = 200;
    private static final int CLASSES_ID = 202;

    private static final int VERSIONS = 300;

    private static final int EVENTS = 400;
    private static final int EVENTS_ID = 402;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "blocks", BLOCKS);
        matcher.addURI(authority, "blocks/*", BLOCKS_ID);

        matcher.addURI(authority, "blocks/*", BLOCKS_ID);

        matcher.addURI(authority, "classes", CLASSES);
        matcher.addURI(authority, "classes/*", CLASSES_ID);

        matcher.addURI(authority, "versions", VERSIONS);

        matcher.addURI(authority, "events", EVENTS);
        matcher.addURI(authority, "events/*", EVENTS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        Context ctx = getContext();
        dbHelper = new DatabaseHelper(ctx);
        return true;
    }




    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BLOCKS:
                return DataContract.Blocks.CONTENT_TYPE_DIR;
            case BLOCKS_ID:
                return DataContract.Blocks.CONTENT_ITEM_TYPE;
            case CLASSES:
                return DataContract.Classes.CONTENT_TYPE_DIR;
            case CLASSES_ID:
                return DataContract.Classes.CONTENT_ITEM_TYPE;
            case VERSIONS:
                return DataContract.Versions.CONTENT_TYPE_DIR;
            case EVENTS:
                return DataContract.Events.CONTENT_TYPE_DIR;
            case EVENTS_ID:
                return DataContract.Events.CONTENT_ITEM_TYPE;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {

            case BLOCKS: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(DatabaseHelper.Tables.BLOCKS);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            case BLOCKS_ID: {
                int blockId = (int) ContentUris.parseId(uri);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(BlocksColumns.BLOCK_ID);
                builder.setTables(BlocksColumns.BLOCK_NAME);
                builder.setTables(BlocksColumns.BLOCK_DAY);
                builder.setTables(BlocksColumns.BLOCK_START);
                builder.setTables(BlocksColumns.BLOCK_END);
                builder.appendWhere(BaseColumns._ID + "=" + blockId);
                return builder.query(db, projection, selection,selectionArgs, null, null, sortOrder);
            }

            case CLASSES: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(DatabaseHelper.Tables.CLASSES);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }

            case CLASSES_ID: {
                int classId = (int) ContentUris.parseId(uri);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(ClassesColumns.CLASS_ID);
                builder.setTables(ClassesColumns.CLASS_NAME);
                builder.appendWhere(BaseColumns._ID + "=" + classId);
                return builder.query(db, projection, selection,selectionArgs, null, null, sortOrder);
            }
            case VERSIONS: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(DatabaseHelper.Tables.VERSIONS);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            case EVENTS: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(DatabaseHelper.Tables.EVENTS);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }

            case EVENTS_ID: {
                int classId = (int) ContentUris.parseId(uri);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(EventsColumns.EVENT_NAME);
                builder.setTables(EventsColumns.EVENT_DATE);
                builder.appendWhere(BaseColumns._ID + "=" + classId);
                return builder.query(db, projection, selection,selectionArgs, null, null, sortOrder);
            }

            default:
                return null;
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BLOCKS: {
                long id = db.insert(DatabaseHelper.Tables.BLOCKS, null, values);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return DataContract.Blocks.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            }
            case CLASSES: {
                long id = db.insert(DatabaseHelper.Tables.CLASSES, null, values);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return DataContract.Classes.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            }
            case VERSIONS: {
                long id = db.insert(DatabaseHelper.Tables.VERSIONS, null, values);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return DataContract.Versions.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            }
            case EVENTS: {
                long id = db.insert(DatabaseHelper.Tables.EVENTS, null, values);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return DataContract.Events.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            }
            default: {
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted = -1;
        switch (match) {
            case (BLOCKS):
                rowsDeleted = db.delete(DatabaseHelper.Tables.BLOCKS, selection, selectionArgs);
                break;
            case (BLOCKS_ID):
                String blockIdWhereClause = BaseColumns._ID + "=" + uri.getLastPathSegment();
                if (!TextUtils.isEmpty(selection))
                    blockIdWhereClause += " AND " + selection;
                rowsDeleted = db.delete(DatabaseHelper.Tables.BLOCKS, blockIdWhereClause, selectionArgs);
                break;

            case (CLASSES):
                rowsDeleted = db.delete(DatabaseHelper.Tables.CLASSES, selection, selectionArgs);
                break;
            case (CLASSES_ID):
                String classesIdWhereClause = BaseColumns._ID + "=" + uri.getLastPathSegment();
                if (!TextUtils.isEmpty(selection))
                    classesIdWhereClause += " AND " + selection;
                rowsDeleted = db.delete(DatabaseHelper.Tables.CLASSES, classesIdWhereClause, selectionArgs);

                break;
            case (VERSIONS):
                rowsDeleted = db.delete(DatabaseHelper.Tables.VERSIONS, selection, selectionArgs);
                break;
            case (EVENTS):
                rowsDeleted = db.delete(DatabaseHelper.Tables.EVENTS, selection, selectionArgs);
                break;
            case (EVENTS_ID):
                String eventsIdWhereClause = BaseColumns._ID + "=" + uri.getLastPathSegment();
                if (!TextUtils.isEmpty(selection))
                    eventsIdWhereClause += " AND " + selection;
                rowsDeleted = db.delete(DatabaseHelper.Tables.EVENTS, eventsIdWhereClause, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (rowsDeleted != -1)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

}
