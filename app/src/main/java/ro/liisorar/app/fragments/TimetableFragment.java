package ro.liisorar.app.fragments;

import android.accounts.Account;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ro.liisorar.app.R;
import ro.liisorar.app.adapters.TimetableListAdapter;
import ro.liisorar.app.data.DataContract;
import ro.liisorar.app.models.Block;
import ro.liisorar.app.models.Class;
import ro.liisorar.app.models.ListBlock;
import ro.liisorar.app.models.ListSectionLabel;
import ro.liisorar.app.models.Section;
import ro.liisorar.app.models.Version;
import ro.liisorar.app.views.QuickReturnListView;
import ro.liisorar.app.views.QuickReturnViewType;

public class TimetableFragment extends ListFragment {

    private View noDataView;
    private View DataView;
    private Button mSyncButton;

    private TimetableListAdapter mAdapter;
    private List<Block> mBlocks = new ArrayList<Block>();
    private List<Block> mTempBlocks = new ArrayList<Block>();
    private String[] mDays = {"","", "Luni", "Marti", "Miercuri", "Joi", "Vineri"};
    private String[] mMonths = {"Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"};
    private List<Class> mClasses = new ArrayList<Class>();


    private QuickReturnListView mListView;
    private LinearLayout mQuickReturnView;
    private Spinner mClassesSpinner;


    public static final String AUTHORITY = "ro.liisorar.app.data";
    public static final String ACCOUNT_TYPE = "liisorar.ro";
    public static final String ACCOUNT = "SyncAccount";
    Account mAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timetable, null);

        noDataView = view.findViewById(R.id.no_data_view);
        DataView = view.findViewById(R.id.data_view);

        mQuickReturnView = (LinearLayout) DataView.findViewById(R.id.list_footer);

        mClassesSpinner = (Spinner) DataView.findViewById(R.id.get_class);

        mSyncButton = (Button) noDataView.findViewById(R.id.sync);

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        mSyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        checkData();
                    }
                }, 4000);

            }
        });

        mListView = (QuickReturnListView) getListView();

        mListView.setItemsCanFocus(true);
        mListView.setCacheColorHint(Color.WHITE);
        mListView.setSelector(android.R.color.transparent);

        mListView.setQuickReturnView(mQuickReturnView);
        mListView.setReturnViewType(QuickReturnViewType.BOTTOM);

        mAdapter = new TimetableListAdapter(getActivity());

        setListAdapter(mAdapter);

        checkData();


        mClassesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                readClassesFromContentProvider();
                readFromContentProvider(getClassId(mClassesSpinner.getSelectedItem().toString()));
                sortData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });


    }

    private void checkData(){
        if(isDataSet()) {
            hideShowView(DataView,true);
            hideShowView(noDataView,false);
            refreshDataUi();
        }else{
            sync();
            hideShowView(DataView,false);
            hideShowView(noDataView,true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void readFromContentProvider(String classId) {

        mBlocks.clear();
        Log.d("TEST","class id:" + classId);

        Cursor cursor = getActivity().getContentResolver().query(DataContract.Blocks.CONTENT_URI, null,
                    DataContract.BlocksColumns.CLASS_ID + " LIKE ?", new String[]{classId + "%" }, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                mBlocks.add(Block.fromCursor(cursor));
            }
            cursor.close();
        }

    }
    private void readClassesFromContentProvider() {
        Cursor cursor = getActivity().getContentResolver().query(DataContract.Classes.CONTENT_URI, null, null, null, null);

        mClasses.clear();

        if (cursor != null) {
            while (cursor.moveToNext())
                mClasses.add(Class.fromCursor(cursor));
            cursor.close();
        }

    }
    private ArrayAdapter<CharSequence> getClassesNamesAdapter() {


        List<CharSequence> mClassesStrings = new ArrayList<CharSequence>();

        for(Class _class : mClasses){
            mClassesStrings.add(_class.name);

        }

        return new ArrayAdapter<CharSequence>(getActivity(),
                R.layout.spinner_text, mClassesStrings);
    }

    private boolean isDataSet() {
        Cursor cursor = getActivity().getContentResolver().query(DataContract.Versions.CONTENT_URI, null, null, null, null);

        List<Version> mVersions = new ArrayList<Version>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                mVersions.add(Version.fromCursor(cursor));
            }
            cursor.close();
        }
        if(mVersions.isEmpty())
            return false;
        else
            return true;
    }
    public void hideShowView(View view, boolean enabled) {
        if(enabled)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                hideShowView(group.getChildAt(idx), enabled);
            }
        }
    }
    private String getClassId(String name){

        for(Class _class : mClasses){
            Log.d("TEST", "class name:" + _class.name);
            if(_class.name.equals(name))
                return _class.objectId;

        }
        return "0";
    }

    private void refreshDataUi(){
        readClassesFromContentProvider();
        ArrayAdapter<CharSequence> mClassesAdapter = getClassesNamesAdapter();
        mClassesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mClassesSpinner.setAdapter(mClassesAdapter);

    }

    private void sortData(){

        mAdapter.clear();

        Calendar calendar = Calendar.getInstance();
        int mCurrentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int mWeekDay;
        boolean isEmpty;
        for (int i = 1; i <= 5; i++){
            mWeekDay = i + mCurrentDayOfWeek - 1;

            if(mWeekDay > 5)
                mWeekDay = mWeekDay - 5;

            if(i > 1)
               // avanseaza cu cate o zi
               calendar.add(calendar.DAY_OF_MONTH, 1);

            int _CurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
            int _CurrentMonth = calendar.get(Calendar.MONTH);


            isEmpty = true;

            for (int j = 0; j < mBlocks.size(); j++){
                if(mBlocks.get(j).day.equals(mDays[mWeekDay])){

                    isEmpty = false;
                    mTempBlocks.add(mBlocks.get(j));
                }
            }
            if(isEmpty == false){
                mAdapter.addLabelItem(new ListSectionLabel(new Section(mDays[mWeekDay]+", "+Integer.toString(_CurrentDay) + " " + mMonths[_CurrentMonth])));
                for (int z = 0; z < mTempBlocks.size(); z++)
                   mAdapter.addItem(new ListBlock(mTempBlocks.get(z)));
                mTempBlocks.clear();
            }

        }


    }

    public void sync() {

        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
    }




}
