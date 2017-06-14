package ca.ziggs.schedulemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robby Sharma on 6/11/2017.
 */

public class DBHandler extends SQLiteOpenHelper {
    //DB Version
    private static final int DATABASE_VERSION = 1;

    //DB Name
    private static final String DATABASE_NAME = "job";

    //Main Table
    private static final String TABLE_JOB_SCHEDULE = "jobSchedule";

    //Main Table's Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_BREAKS  = "breaks";
    private static final String KEY_DATE = "date";
    private static final String KEY_FORMATTED_TIME = "formattedTime";
    private static final String KEY_FORMATTED_DATE = "formattedDate";
    private static final String KEY_DATETIME = "dateTime";

    public DBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //Create Table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_JOB_SCHEDULE_TABLE = "CREATE TABLE " + TABLE_JOB_SCHEDULE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_DURATION + " TEXT,"
                + KEY_START_TIME + " TEXT,"
                + KEY_END_TIME + " TEXT,"
                + KEY_BREAKS + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_FORMATTED_DATE + " TEXT,"
                + KEY_FORMATTED_TIME + " TEXT,"
                + KEY_DATETIME + " TEXT " + ")";

        sqLiteDatabase.execSQL(CREATE_JOB_SCHEDULE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Drop Old Table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB_SCHEDULE);
        //Create Again
        onCreate(sqLiteDatabase);
    }

    void addNewEntry(JobEntry newEntry){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, newEntry.getTitle());
        values.put(KEY_LOCATION, newEntry.getLocation());
        values.put(KEY_DURATION, newEntry.getDuration());
        values.put(KEY_START_TIME, newEntry.getStartTime());
        values.put(KEY_END_TIME, newEntry.getEndTime());
        values.put(KEY_BREAKS, newEntry.getBreaks());
        values.put(KEY_DATE, newEntry.getDate());
        values.put(KEY_FORMATTED_DATE, newEntry.getFormattedDate());
        values.put(KEY_FORMATTED_TIME, newEntry.getFormattedTime());
        values.put(KEY_DATETIME,newEntry.getDateTime());

        db.insert(TABLE_JOB_SCHEDULE,null,values);
        db.close();

    }

    public boolean updateEntry(int updateID, String title, String location, String duration, String startTime, String endTime, String breaks, String date, String formattedDate, String formattedTime, String dateTime){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_LOCATION, location);
        values.put(KEY_DURATION, duration);
        values.put(KEY_START_TIME, startTime);
        values.put(KEY_END_TIME, endTime);
        values.put(KEY_BREAKS, breaks);
        values.put(KEY_DATE, date);
        values.put(KEY_FORMATTED_DATE, formattedDate);
        values.put(KEY_FORMATTED_TIME, formattedTime);
        values.put(KEY_DATETIME, dateTime);
        return db.update(TABLE_JOB_SCHEDULE,values,KEY_ID + "=" + updateID,null) > 0;
    }

    public boolean deleteEntry(int deleteID){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_JOB_SCHEDULE,KEY_ID +"=" + deleteID,null) > 0;
    }


    public boolean checkTodayShift(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE + " = date('now','localtime')";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount() <=0){
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public Integer getNumOfTodaysShifts(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE + " = date('now','localtime')";
        Cursor cursor = db.rawQuery(query,null);
        int shifts = cursor.getCount();
        cursor.close();
        db.close();
        return shifts;
    }

    public List<JobEntry> getTodaysShifts(){
        List<JobEntry> shiftList =  new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE + " = date('now','localtime') ORDER BY " + KEY_DATETIME + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                JobEntry shift = new JobEntry();
                shift.setId(Integer.parseInt(cursor.getString(0)));
                shift.setTitle(cursor.getString(1));
                shift.setLocation(cursor.getString(2));
                shift.setDuration(cursor.getString(3));
                shift.setStartTime(cursor.getString(4));
                shift.setEndTime(cursor.getString(5));
                shift.setBreaks(cursor.getString(6));
                shift.setDate(cursor.getString(7));
                shift.setFormattedDate(cursor.getString(8));
                shift.setFormattedTime(cursor.getString(9));
                shift.setDateTime(cursor.getString(10));

                shiftList.add(shift);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return shiftList;
    }

    public List<JobEntry> getAllShifts(){
        List<JobEntry> shiftList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_JOB_SCHEDULE+ " WHERE " + KEY_DATETIME + " >= date('now','localtime') ORDER BY " + KEY_DATETIME + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                JobEntry shift = new JobEntry();
                shift.setId(Integer.parseInt(cursor.getString(0)));
                shift.setTitle(cursor.getString(1));
                shift.setLocation(cursor.getString(2));
                shift.setDuration(cursor.getString(3));
                shift.setStartTime(cursor.getString(4));
                shift.setEndTime(cursor.getString(5));
                shift.setBreaks(cursor.getString(6));
                shift.setDate(cursor.getString(7));
                shift.setFormattedDate(cursor.getString(8));
                shift.setFormattedTime(cursor.getString(9));
                shift.setDateTime(cursor.getString(10));

                shiftList.add(shift);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return shiftList;
    }

    public void truncateShiftsTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_JOB_SCHEDULE;
        db.execSQL(query);
        db.close();
    }


}
