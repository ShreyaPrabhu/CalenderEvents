package com.example.shreyaprabhu.calenderevents;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddEvent extends AppCompatActivity {

    EditText date;
    static final int DATE_PICKER_ID = 1111;
    private int year;
    private int month;
    private int day;
    private EditText time;
    static final int TIME_DIALOG_ID = 0;
    private String format = "";
    private int hour;
    private int min;
    private Button declineButton;
    private EditText repeat;
    private Button Ok;
    CheckBox d, w, m, y;
    EventsDbHelper eventsDbHelper;
    EditText eventTitle;
    Toolbar toolbar;


    public interface OnEventAdded {
        public void EventAdded();
    }

    OnEventAdded onEventAdded;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        (AddEvent.this).setSupportActionBar(toolbar);

        date = (EditText) findViewById(R.id.Date);
        eventTitle = (EditText) findViewById(R.id.EventTitle);
        time = (EditText) findViewById(R.id.time);
        repeat = (EditText) findViewById(R.id.EventOccurance);
        declineButton = (Button) findViewById(R.id.declineButton);


        date.setText(Calendar.getInstance().toString());
        eventsDbHelper = new EventsDbHelper(this);

        Calendar calendar = Calendar.getInstance();

        //SHOW DATE
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        showDate(day, month, year);

        //SHOW TIME
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);

        //DATE DIALOG BOX
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });

        //TIME DIALOG BOX
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        //SET EVENT OCCURRENCE DIALOG BOX
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createDialog();

            }
        });

        //ADD EVENT BUTTON
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventsDbHelper.insertEvent(eventTitle.getText().toString(),
                        date.getText().toString(),
                        time.getText().toString())) {
                    //,repeat.getText().toString()
                    Toast.makeText(getApplicationContext(), "Event Added", Toast.LENGTH_SHORT).show();

                    //onEventAdded.EventAdded();
                    finish();
                    Intent i = new Intent(AddEvent.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Could not Add Event", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void createDialog() {

        final Dialog dialog = new Dialog(AddEvent.this);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Add Event Dialog");
        dialog.show();
        d = (CheckBox) dialog.findViewById(R.id.daily);
        w = (CheckBox) dialog.findViewById(R.id.weekly);
        m = (CheckBox) dialog.findViewById(R.id.monthly);
        y = (CheckBox) dialog.findViewById(R.id.yearly);
        Ok = (Button) dialog.findViewById(R.id.Ok);

        Ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String result = new String();
                int checked = 0;

                if (d.isChecked()) {
                    result = "Daily";
                    checked++;
                }
                if (w.isChecked()) {
                    result = "Weekly";
                    checked++;
                }
                if (m.isChecked()) {
                    result = "Monthly";
                    checked++;
                }
                if (y.isChecked()) {
                    result = "Yearly";
                    checked++;
                }

                if (checked == 0 || checked > 1) {
                    Toast.makeText(AddEvent.this, "You have either selected none or selected more than one option", Toast.LENGTH_LONG).show();
                }

                if (checked == 1) {
                    repeat.setText(result);
                    dialog.dismiss();
                }
            }

        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month, day);

            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, hour, min, false);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
            date.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));


        }
    };


    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hour = hourOfDay;
                    min = minute;
                    showTime(hour, min);

                }
            };

    public void showDate(int day, int month, int year) {
        date.setText(new StringBuilder().append(month + 1)
                .append("-").append(day).append("-").append(year)
                .append(" "));

    }


    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        time.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

