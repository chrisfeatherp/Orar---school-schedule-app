package ro.liisorar.app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ro.liisorar.app.fragments.EventsFragment;
import ro.liisorar.app.fragments.TimetableFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {
    //  adapter-ul pentru paginile din activitatea principala
    //  afiseaza pt fiecare pagina  fragmentul corespunzator

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TimetableFragment();

            case 1:

                return new EventsFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}

