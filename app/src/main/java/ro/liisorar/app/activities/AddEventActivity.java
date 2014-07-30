package ro.liisorar.app.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import ro.liisorar.app.R;
import ro.liisorar.app.data.DataContract;
import ro.liisorar.app.models.Event;

public class AddEventActivity extends ActionBarActivity  {

    EditText mEventName;

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;

    private int mEventYear;
    private int mEventMonth;
    private int mEventDay;

    private int mEventHour;
    private int mEventMinute;

    private EditText mEventDate;
    private EditText mEventTime;

    boolean mEventDateIsSet = false;
    boolean mEventTimeIsSet = false;

    private String mEventTimestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // aratam un buton "inapoi" ca va duce utilizatorul pe activiatea principala (lista orar , evenimente)
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // initializare var pt data si timp
        final Calendar c = Calendar.getInstance();
        mEventYear = c.get(Calendar.YEAR);
        mEventMonth = c.get(Calendar.MONTH);
        mEventDay = c.get(Calendar.DAY_OF_MONTH);
        mEventHour = c.get(Calendar.HOUR_OF_DAY);
        mEventMinute = c.get(Calendar.MINUTE);

        // initializare elemete interfata
        mEventName = (EditText) findViewById(R.id.add_event_name);

        mEventDate = (EditText) findViewById(R.id.add_event_day);
        mEventTime = (EditText) findViewById(R.id.add_event_time);

        Button mSubmitButton = (Button) findViewById(R.id.add_event_submit);


        // lasam dialogul corespunzator input-ului, atunci cand acesta este actionat
        mEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        mEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(TIME_DIALOG_ID);
            }
        });


        // in cazul in care butonul de adaugare este actionat verificam daca toate campurile au fost completate corect
        // dupa care adaugam evenimentul in baza de date
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEventName.getText().toString().isEmpty() == true) {
                    Toast.makeText(AddEventActivity.this, "Numele nu a fost fixat!", Toast.LENGTH_SHORT).show();
                }else if(isAlphaNumeric(mEventName.getText().toString()) == false) {
                    Toast.makeText(AddEventActivity.this, "Numele nu este format din caractere alfanumerice!", Toast.LENGTH_SHORT).show();
                }else if(mEventDateIsSet == false) {
                    Toast.makeText(AddEventActivity.this, "Ziua evenimentului nu a fost fixata!", Toast.LENGTH_SHORT).show();
                }else if(mEventTimeIsSet == false) {
                    Toast.makeText(AddEventActivity.this, "Ora evenimentului nu a fost fixata!", Toast.LENGTH_SHORT).show();
                }else {



                    mEventTimestamp =   mEventYear + "-" +
                                        mEventMonth + "-" +
                                        mEventDay + "-" +
                                        pad(mEventHour) + "-" +
                                        pad(mEventMinute);


                    Event mEvent = new Event(mEventName.getText().toString(), mEventTimestamp);

                    getContentResolver().insert(
                        DataContract.Events.CONTENT_URI,
                        mEvent.getContentValues());
                    Toast.makeText(AddEventActivity.this, "Evenimentul a fost salvat !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateEventDate()
    {   // actualizam campul dedicat datei cu informatiile alese din dialog
        mEventDate.setText(new StringBuilder()
                .append(mEventDay).append("/")
                .append(mEventMonth + 1).append("/") // Ianuarie este reprezentat prin 0
                .append(mEventYear).append(" "));

        mEventDateIsSet = true;
    }


    public void updateEventTime()
    {   // actualizam campul dedicat orei cu informatiile alese din dialog
        mEventTime.setText(new StringBuilder()
                .append(pad(mEventHour)).append(":")
                .append(pad(mEventMinute)));

        mEventTimeIsSet = true;
    }

    private static String pad(int c) {
        // adaugam un 0 in fata minutului in caz ca e < 9
        // 2 -> 02
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    // dialogul pentru data
    // salvam data in variabilele globale si actualizam input-ul pentru data
    private DatePickerDialog.OnDateSetListener mEventDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mEventYear = year;
                    mEventMonth = monthOfYear;
                    mEventDay = dayOfMonth;
                    updateEventDate();
                }
            };
    // dialogul pentru timp
    // salvam timp in variabilele globale si actualizam input-ul pentru timp
    private TimePickerDialog.OnTimeSetListener mEventTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                    mEventHour = hourOfDay;
                    mEventMinute = minute;
                    updateEventTime();
                }
            };

    // lansam dialogul corespunzator
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mEventDateSetListener, mEventYear, mEventMonth, mEventDay);

            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mEventTimeSetListener, mEventHour, mEventMinute, false);

        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // functie chemata de fiecare data cand un buton din bara de navigatie este actionat
        // action_info prezinta un dialog cu informatii (nume, clasa, profesor coordonator)
        int id = item.getItemId();
        if (id == R.id.action_info) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView title = new TextView(this);

            title.setText("Informatii");
            title.setGravity(Gravity.CENTER);
            title.setPadding(10, 10, 10, 10);
            title.setTextSize(20);
            builder.setCustomTitle(title)
                    .setMessage(Html.fromHtml("Aplicatie realizata de:" +
                            "<br/>" +
                            "Panaitescu Constantin  12 C" +
                            "<br/><br/>" +
                            "Profesor coordonator:" +
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


        }
        return super.onOptionsItemSelected(item);
    }

    // verificare daca un sir de caractere este alfanumeric + spatiu + .,! (elemente punctuatie)
    public boolean isAlphaNumeric(String s){
        String pattern= "[A-Za-z0-9 _.,!\"'/$]*";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }
}
