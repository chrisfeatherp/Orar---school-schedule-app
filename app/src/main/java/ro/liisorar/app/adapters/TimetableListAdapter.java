package ro.liisorar.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.liisorar.app.R;
import ro.liisorar.app.models.Block;
import ro.liisorar.app.models.ListBlock;
import ro.liisorar.app.models.ListItem;
import ro.liisorar.app.models.ListSectionLabel;
import ro.liisorar.app.models.Section;


public class TimetableListAdapter extends BaseAdapter {
    //  adapter-ul pentru ore
    //  afiseaza pt fiecare ora din lista informatiile corespunzatoare
    //  difera fata de adapter-ul pt evenimente prin faptul ca avem doua tipuri de elemente (ora si separatorul care semnaleaza o noua zi)

    private Activity mContext;
    private LayoutInflater mLayoutInflater = null;

    // mListItems contine toate elementele
    // mLabels contine doar pozitia separatorilor din mListItems
    private ArrayList<ListItem> mListItems = new ArrayList<ListItem>();
    private List<Integer> mLabels = new ArrayList<Integer>();



    private static final int TYPE_BLOCK = 0;
    private static final int TYPE_LABEL = 1;
    private static final int TYPE_MAX_COUNT = TYPE_LABEL + 1;

    public TimetableListAdapter(Activity context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void clear() {
        mListItems.clear();
        mLabels.clear();
        notifyDataSetChanged();
    }


    public void addItem(final ListBlock object) {
        mListItems.add(object);
        notifyDataSetChanged();
    }

    public void addLabelItem(final ListSectionLabel object) {
        mListItems.add(object);
        // salvam pozitia separatorului
        mLabels.add(mListItems.size()-1);
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }
    @Override
    public int getCount() {
        return mListItems.size();
    }
    @Override
    public int getItemViewType(int position) {
        return mLabels.contains(position) ? TYPE_LABEL : TYPE_BLOCK;
    }
    @Override
    public Object getItem(int pos) {
        if(getItemViewType(pos) == TYPE_BLOCK)
            return (ListBlock) mListItems.get(pos);
        else
            return (ListSectionLabel) mListItems.get(pos);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled (){
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ListItem mListItem = (ListItem) mListItems.get(position);
        final int type = getItemViewType(position);

        // in acest caz avem doua tipuri de elemente de afisat
        // in primul rand verificam lista ce contine pozitia separatorilor pentru a determina tipul elementului
        // dupa care tratam elemenul corespunzator
        switch (type) {
            case TYPE_BLOCK: {
                if (v == null) {
                    v = mLayoutInflater.inflate(R.layout.list_item_orar, null);
                }
                ListBlock mListBlock = (ListBlock) getItem(position);
                Block mBlock = mListBlock.getBlock();

                final TextView start = (TextView) v.findViewById(R.id.block_start_time);
                final TextView end = (TextView) v.findViewById(R.id.block_end_time);
                final TextView title = (TextView) v.findViewById(R.id.block_title);

                start.setText(mBlock.start);
                end.setText(mBlock.end);
                title.setText(mBlock.name);

                return v;
            }
            case TYPE_LABEL: {
                if (v == null) {
                    v = mLayoutInflater.inflate(R.layout.list_item_label, null);
                }

                ListSectionLabel mListSectionLabel = (ListSectionLabel) getItem(position);
                Section mSectionLabel = mListSectionLabel.getSection();

                final TextView title = (TextView) v.findViewById(R.id.label_text);
                title.setText(mSectionLabel.getTitle());

                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);
                v.setEnabled(false);

                return v;
            }

            default:
                return v;


        }
    }




}
