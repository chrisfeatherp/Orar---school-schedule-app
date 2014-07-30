package ro.liisorar.app.sync;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ro.liisorar.app.models.Block;
import ro.liisorar.app.models.Class;
import ro.liisorar.app.models.Version;

import static ro.liisorar.app.sync.ParseComServer.getAppParseComHeaders;
public class ParseComServerAccessor {

    public List<Class> getClasses() throws Exception {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.parse.com/1/classes/classes";

        HttpGet httpGet = new HttpGet(url);
        for (Header header : getAppParseComHeaders()) {
            httpGet.addHeader(header);
        }

        try {
            HttpResponse response = httpClient.execute(httpGet);

            String responseString = EntityUtils.toString(response.getEntity());
            Log.d("ORAR", "getClasses> Response= " + responseString);

            if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
                ParseComServer.ParseComError error = new Gson().fromJson(responseString, ParseComServer.ParseComError.class);
                throw new Exception("Error retrieving classes ["+error.code+"] - " + error.error);
            }

            Classes classes = new Gson().fromJson(responseString, Classes.class);
            return classes.results;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<Class>();
    }
    public List<Block> getBlocks() throws Exception {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.parse.com/1/classes/blocks";

        HttpGet httpGet = new HttpGet(url);
        for (Header header : getAppParseComHeaders()) {
            httpGet.addHeader(header);
        }

        try {
            HttpResponse response = httpClient.execute(httpGet);

            String responseString = EntityUtils.toString(response.getEntity());
            Log.d("ORAR", "getBlocks> Response= " + responseString);

            if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
                ParseComServer.ParseComError error = new Gson().fromJson(responseString, ParseComServer.ParseComError.class);
                throw new Exception("Error retrieving blocks ["+error.code+"] - " + error.error);
            }

            Blocks blocks = new Gson().fromJson(responseString, Blocks.class);
            return blocks.results;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<Block>();
    }

    public Version getServerDataVersion(){

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.parse.com/1/classes/sync_status";

        HttpGet httpGet = new HttpGet(url);
        for (Header header : getAppParseComHeaders()) {
            httpGet.addHeader(header);
        }

        try {
            HttpResponse response = httpClient.execute(httpGet);

            String responseString = EntityUtils.toString(response.getEntity());
            Log.d("ORAR", "getServerDataVersion> Response= " + responseString);

            if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
                ParseComServer.ParseComError error = new Gson().fromJson(responseString, ParseComServer.ParseComError.class);
                throw new Exception("Error retrieving versions [" + error.code + "] - " + error.error);
            }


            Versions versions = new Gson().fromJson(responseString, Versions.class);


            // sortam lista crescator si salvam elementul max
             Version MaxDataVersion = Collections.max(versions.results,new Comparator<Version>() {

                public int compare(Version o1, Version o2) {
                    return Integer.compare(o1.getVersion(), o2.getVersion());
                }
            });


            return MaxDataVersion;

        } catch (Exception e) {
            e.printStackTrace();
        }
        // veriunea 0 nu declanseaza sync-ul
        return new Version("0",0);
    }


    private class Versions implements Serializable {
        List<Version> results;
    }
    private class Classes implements Serializable {
        List<Class> results;
    }
    private class Blocks implements Serializable {
        List<Block> results;
    }

}
