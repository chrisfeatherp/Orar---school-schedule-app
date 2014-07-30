package ro.liisorar.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import ro.liisorar.app.R;
import ro.liisorar.app.adapters.HomePagerAdapter;


public class HomeActivity extends ActionBarActivity implements
        ActionBar.TabListener,
        ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private Context mContext;

    // Date pentru a cont un account ce va fi folosit pentru sync
    public static final String AUTHORITY = "ro.liisorar.app.data";
    public static final String ACCOUNT_TYPE = "liisorar.ro";
    public static final String ACCOUNT = "SyncAccount";

    Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this;
        // initializam un cont nou
        mAccount = CreateSyncAccount(this);


        // view-ul ce contine cele pagini (orar si evenimente)
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setPageMarginDrawable(R.drawable.grey_border);
        mViewPager.setPageMargin(getResources()
                .getDimensionPixelSize(R.dimen.page_margin_width));

        if (mViewPager != null) {
            mViewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
            mViewPager.setOnPageChangeListener(this);

            // initializam doua tab-uri din bara de navigatie ce corespund celor doua pagini
            final ActionBar actionBar = getSupportActionBar();

            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.addTab(actionBar.newTab()
                    .setText(R.string.title_orar)
                    .setTabListener(this));
            actionBar.addTab(actionBar.newTab()
                    .setText(R.string.title_evenimente)
                    .setTabListener(this));

            actionBar.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.actionbar_background_noshadow));
        }

        // de fiecare data cand aplicatia porneste verificam baza de date de pe server
        if (savedInstanceState == null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    sync();
                }
            }, 10000);
        }
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int position) {
        getSupportActionBar().setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // functie chemata de fiecare data cand un buton din bara de navigatie este actionat
        // action_info prezinta un dialog cu informatii (nume, clasa, profesor coordonator)
        // action_add_event porneste o activitate noua ce se va ocupa cu crearea evenimetelor
        int id = item.getItemId();
        if (id == R.id.action_info) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView title = new TextView(this);

            title.setText("Informatii");
            title.setGravity(Gravity.CENTER);
            title.setPadding(10, 10, 10, 10);
            title.setTextSize(20);
            builder.setCustomTitle(title)
                    .setMessage(Html.fromHtml("Aplicatie realizata de:"+
                                                "<br/>" +
                                                "Panaitescu Constantin  12 C"+
                                                "<br/><br/>" +
                                                "Profesor coordonator:"+
                                                "<br/>" +
                                                "Iuscinschi Simona"))
                    .setCancelable(false)
                    .setPositiveButton("INAPOI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();


        }else if (id == R.id.action_add_event) {
            if(mViewPager.getCurrentItem() == 0) {
                // in cazul in care ne aflam pe pagina cu orarul mergem pe pagina urmatoare
                // dupa care pornim activitatea pt crearea evenimentelor
                mViewPager.setCurrentItem(1,true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Intent intent = new Intent(mContext, AddEventActivity.class);
                        startActivity(intent);
                    }
                }, 200);
            }else {
                Intent intent = new Intent(mContext, AddEventActivity.class);
                startActivity(intent);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public static Account CreateSyncAccount(Context context) {

        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);


        return  newAccount;
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
