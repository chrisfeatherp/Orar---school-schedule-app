package ro.liisorar.app.sync;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParseComServer {

    private final static String APP_ID = "uvAl4wgg7mhT4e0pIHrndGOK8wMn6Dgvxxxxxxxxx";
    private final static String REST_API_KEY = "F5gHObYUpXEFeyA0Da311l3Tles2KQ1xxxxxxxxx";


    public static List<Header> getAppParseComHeaders() {
        List<Header> ret = new ArrayList<Header>();
        ret.add(new BasicHeader("X-Parse-Application-Id", APP_ID));
        ret.add(new BasicHeader("X-Parse-REST-API-Key", REST_API_KEY));
        return ret;
    }


    public static class ParseComError implements Serializable {
        public int code;
        public String error;
    }

}
