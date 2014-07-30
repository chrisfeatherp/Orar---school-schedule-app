package ro.liisorar.app.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ro.liisorar.app.data.DataContract;
import ro.liisorar.app.models.Block;
import ro.liisorar.app.models.Class;
import ro.liisorar.app.models.Version;


public class SyncAdapter extends AbstractThreadedSyncAdapter {


    ContentResolver mContentResolver;
    private List<Version> mVersions = new ArrayList<Version>();

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d("ORAR", "onPerformSync for account[" + account.name + "]");
        try {

            ParseComServerAccessor parseComService = new ParseComServerAccessor();


            readFromContentProvider();
            if(mVersions.isEmpty())
                mVersions.add(new Version("0",0));
            Version MaxLocalVersion = Collections.max(mVersions, new Comparator<Version>() {

                public int compare(Version o1, Version o2) {
                    return Integer.compare(o1.getVersion(), o2.getVersion());
                }
            });
            Log.d("ORAR", "max local data version " + MaxLocalVersion.getVersion() + "]");

            Version ServerDataVersion = parseComService.getServerDataVersion();
            if(ServerDataVersion.getVersion() > MaxLocalVersion.getVersion()){
                Log.d("ORAR", "New update required ");

                //  in acest caz baza de date de pe server trebuie copiata local
                //  stergem toate clasele existente in baza de date locala
                mContentResolver.delete(DataContract.Classes.CONTENT_URI, null, null);

                //  parcurgem lista cu clase obtinuta de la server si le introducem in baza de date locala
                for (Class _class : parseComService.getClasses()) {

                    mContentResolver.insert(DataContract.Classes.CONTENT_URI,
                            _class.getContentValues());
                }
                // la fel procedam pt toate tabelele existente in baza de date de pe server
                mContentResolver.delete(DataContract.Blocks.CONTENT_URI, null, null);
                for (Block block : parseComService.getBlocks()) {

                    mContentResolver.insert(DataContract.Blocks.CONTENT_URI,
                            block.getContentValues());
                }

                // dupa ce am copiat toate datele in baza de date locala salvam versunea curenta
                mContentResolver.insert(DataContract.Versions.CONTENT_URI,
                        ServerDataVersion.getContentValues());
                Log.d("ORAR", "Current data version " + ServerDataVersion.getVersion());
            }else {
                Log.d("ORAR", "Local data up to date ");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readFromContentProvider() {
        Cursor cursor = mContentResolver.query(DataContract.Versions.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                mVersions.add(Version.fromCursor(cursor));
            }
            cursor.close();
        }

    }
}