package com.example.shreyaprabhu.calenderevents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shreya Prabhu on 7/20/2016.
 */
public class EventsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Events.Db";
    private static final int DATABASE_VERSION = 1;
    public static final String EVENT_TABLE_NAME = "events";
    public static final String _id = "_id";
    public static final String EVENT_COLUMN_NAME = "event_name";
    public static final String EVENT_COLUMN_DATE = "event_dueDate";
    public static final String EVENT_COLUMN_TIME = "event_time";
    public static final String EVENT_COLUMN_OCCURRENCE = "event_occurrence";

    public EventsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EVENT_TABLE_NAME + "(" +
                        _id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        EVENT_COLUMN_NAME + " TEXT, " +
                        EVENT_COLUMN_DATE + " TEXT, " +
                        EVENT_COLUMN_TIME + " TEXT " +
                        //EVENT_COLUMN_OCCURRENCE + "TEXT " +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertEvent(String eventName, String eventDueDate, String eventTime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_COLUMN_NAME, eventName);
        contentValues.put(EVENT_COLUMN_DATE, eventDueDate);
        contentValues.put(EVENT_COLUMN_TIME, eventTime);
        //contentValues.put(EVENT_COLUMN_OCCURRENCE, eventOccurrence);
        db.insert(EVENT_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateEvent(Integer id, String eventName, String eventDueDate, String eventTime, String eventOccurrence) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_COLUMN_NAME, eventName);
        contentValues.put(EVENT_COLUMN_DATE, eventDueDate);
        contentValues.put(EVENT_COLUMN_TIME, eventTime);
        contentValues.put(EVENT_COLUMN_OCCURRENCE, eventOccurrence);
        db.update(EVENT_TABLE_NAME, contentValues, _id + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Cursor getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + EVENT_TABLE_NAME + " WHERE " +
                _id + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + EVENT_TABLE_NAME, null);
        return res;
    }

    public Integer deleteEvent(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(EVENT_TABLE_NAME,
                _id + " = ? ",
                new String[]{Long.toString(id)});
    }
}
