package ro.liisorar.app.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataContract {


    public interface EventsColumns {

        String EVENT_NAME = "event_name";
        String EVENT_DATE = "event_date";

    }
    public interface BlocksColumns {

        String BLOCK_ID = "block_id";
        String CLASS_ID = "class_id";
        String BLOCK_NAME = "block_name";
        String BLOCK_DAY = "block_day";
        String BLOCK_START = "block_start";
        String BLOCK_END = "block_end";
    }

    public interface ClassesColumns {

        String CLASS_ID = "class_id";
        String CLASS_NAME = "class_name";

    }

    public interface VersionsColumns {

        String VERSION_ID = "version_id";
        String VERSION_NR = "version_nr";

    }
    public static final String CONTENT_AUTHORITY = "ro.liisorar.app.data";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BLOCKS = "blocks";
    public static final String PATH_CLASSES = "classes";
    public static final String PATH_VERSIONS = "versions";
    public static final String PATH_EVENTS = "events";


    public static class Blocks implements BlocksColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BLOCKS).build();


        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.ro.liisorar.app.data.blocks";
        public static final String CONTENT_TYPE_DIR =
                "vnd.android.cursor.dir/vnd.ro.liisorar.app.data.blocks";
    }

    public static class Classes implements ClassesColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLASSES).build();


        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.ro.liisorar.app.data.classes";
        public static final String CONTENT_TYPE_DIR =
                "vnd.android.cursor.dir/vnd.ro.liisorar.app.data.classes";
    }
    public static class Versions implements VersionsColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VERSIONS).build();


        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.ro.liisorar.app.data.versions";
        public static final String CONTENT_TYPE_DIR =
                "vnd.android.cursor.dir/vnd.ro.liisorar.app.data.versions";
    }
    public static class Events implements EventsColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENTS).build();


        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.ro.liisorar.app.data.events";
        public static final String CONTENT_TYPE_DIR =
                "vnd.android.cursor.dir/vnd.ro.liisorar.app.data.events";
    }
}
