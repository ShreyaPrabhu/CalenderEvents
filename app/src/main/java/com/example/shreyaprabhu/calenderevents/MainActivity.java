package com.example.shreyaprabhu.calenderevents;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AddEvent.OnEventAdded {

    ListView listview;
    EventsDbHelper eventsDbHelper;
    Toolbar toolbar;
    ImageView delete;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        (MainActivity.this).setSupportActionBar(toolbar);


        delete = (ImageView) findViewById(R.id.delete);
        listview = (ListView) findViewById(R.id.event_list_view);


        eventsDbHelper = new EventsDbHelper(this);


        cursor = eventsDbHelper.getAllEvents();
        String[] columns = new String[]{
                //EventsDbHelper._id,
                EventsDbHelper.EVENT_COLUMN_NAME,
                EventsDbHelper.EVENT_COLUMN_DATE,
                EventsDbHelper.EVENT_COLUMN_TIME,
                //EventsDbHelper.EVENT_COLUMN_OCCURRENCE
        };


        int[] widgets = new int[]{
                //R.id.Eid,
                R.id.Eevent,
                R.id.Edate,
                R.id.Etime,
                //R.id.Eoccurance
        };

        final SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.event_item,
                cursor, columns, widgets, 0);
        listview.setAdapter(cursorAdapter);
        listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventsDbHelper.deleteEvent(id);
                cursorAdapter.notifyDataSetChanged();
                cursor.requery();
                Toast.makeText(MainActivity.this, "Event Deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void EventAdded(){
        cursor.requery();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addEvent) {

            Intent i = new Intent(MainActivity.this, AddEvent.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
