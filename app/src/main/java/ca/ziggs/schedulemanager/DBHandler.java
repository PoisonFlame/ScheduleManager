package ca.ziggs.schedulemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

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
    private static final String TABLE_PAYCHECK_SETTINGS = "paycheckSettings";
    //private static final String TABLE_JOB_SCHEDULE = "jobScheduleTEST";

    //Main Table's Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_START_TIME = "startTime";
    public static final String KEY_END_TIME = "endTime";
    public static final String KEY_BREAKS  = "breaks";
    public static final String KEY_DATE = "date";
    public static final String KEY_FORMATTED_TIME = "formattedTime";
    public static final String KEY_FORMATTED_DATE = "formattedDate";
    public static final String KEY_DATETIME = "dateTime";


    //Paycheck Settings Table Column Names
    public static final String KEY_SALARY = "salary";
    public static final String KEY_SALARY_TYPE = "salaryType";
    public static final String KEY_PAYCHECK_DATE = "paycheckDate";
    public static final String KEY_FORMATTED_PAYCHECK_DATE = "formattedPaycheckDate";
    public static final String KEY_PAY_PERIOD = "payPeriod";
    public static final String KEY_WEEK_START_DAY = "weekStartDay";
    public static final String KEY_USE_CANADIAN_TAXES = "useCanadianTaxes";
    public static final String KEY_PROVINCE = "province";
    public static final String KEY_TAX_EXEMPTIONS = "taxExempt";
    public static final String KEY_CPP_EXEMPT = "exemptCPP";
    public static final String KEY_EI_EXEMPT = "exemptEI";

    public Context mContext;

    public DBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.mContext = context;
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

    public void createTestDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        String CREATE_JOB_SCHEDULE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_JOB_SCHEDULE + "("
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

        db.execSQL(CREATE_JOB_SCHEDULE_TABLE);
    }

    public void createPaycheckSettingsDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_PAYCHECK_SETTINGS,null,null);
        String CREATE_PAYCHECK_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PAYCHECK_SETTINGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SALARY + " TEXT,"
                + KEY_SALARY_TYPE + " TEXT,"
                + KEY_PAYCHECK_DATE + " TEXT,"
                + KEY_PAY_PERIOD + " TEXT,"
                + KEY_WEEK_START_DAY + " TEXT,"
                + KEY_USE_CANADIAN_TAXES + " TEXT,"
                + KEY_PROVINCE + " TEXT,"
                + KEY_CPP_EXEMPT + " TEXT,"
                + KEY_EI_EXEMPT + " TEXT,"
                + KEY_FORMATTED_PAYCHECK_DATE + " TEXT " + ")";

        db.execSQL(CREATE_PAYCHECK_SETTINGS_TABLE);
    }

    public void defaultPaycheckSettings(){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT id FROM " + TABLE_PAYCHECK_SETTINGS;
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.getCount() <= 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_SALARY, "10.00");
            values.put(KEY_SALARY_TYPE, "Hour");
            values.put(KEY_PAYCHECK_DATE, "");
            values.put(KEY_PAY_PERIOD, "Bi-Weekly");
            values.put(KEY_WEEK_START_DAY, "Monday");
            values.put(KEY_USE_CANADIAN_TAXES, "true");
            values.put(KEY_PROVINCE, "AB");
            values.put(KEY_CPP_EXEMPT, "false");
            values.put(KEY_EI_EXEMPT, "false");
            values.put(KEY_FORMATTED_PAYCHECK_DATE, "Click to set date.");

            db.insert(TABLE_PAYCHECK_SETTINGS, null, values);
        }
        db.close();
    }

    public String getPaycheckColumn(String column){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + column + " FROM " + TABLE_PAYCHECK_SETTINGS;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            String answer = cursor.getString(0);
            db.close();
            cursor.close();
            return answer;
        }
        db.close();
        cursor.close();
        return "";
    }


    public boolean updatePaycheckSettings(String salary, String salaryType, String firstPaycheckDate, String payPeriod, String weekStartDay, String useCanadianTaxes, String province, String cppExempt, String eiExempt, String formattedStartDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SALARY,salary);
        values.put(KEY_SALARY_TYPE,salaryType);
        values.put(KEY_PAYCHECK_DATE, firstPaycheckDate);
        values.put(KEY_PAY_PERIOD,payPeriod);
        values.put(KEY_WEEK_START_DAY,weekStartDay);
        values.put(KEY_USE_CANADIAN_TAXES, useCanadianTaxes);
        values.put(KEY_PROVINCE,province);
        values.put(KEY_CPP_EXEMPT,cppExempt);
        values.put(KEY_EI_EXEMPT, eiExempt);
        values.put(KEY_FORMATTED_PAYCHECK_DATE,formattedStartDate);
        return db.update(TABLE_PAYCHECK_SETTINGS,values,KEY_ID + "=1",null) > 0;
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


    //WEEK STATS

    public Integer getWeekWorkingDays(String startDate, String endDate){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT DISTINCT " + KEY_DATE + " FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor cursor = db.rawQuery(query,null);
        int days = cursor.getCount();
        cursor.close();
        db.close();
        return  days;
    }

   public String getWeekWorkingHours(String startDate,String endDate){
       SQLiteDatabase db = this.getWritableDatabase();
       String query = "SELECT " + KEY_START_TIME+","+KEY_END_TIME+" FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE + " BETWEEN '" + startDate + "' AND '" + endDate+"'";
       Cursor cursor = db.rawQuery(query,null);
       double totalTimeInMilli = 0.0;

       if(cursor.moveToFirst()){
           do{
               try{
                   SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                   Date startTime = sdf.parse(cursor.getString(0));
                   Date endTime = sdf.parse(cursor.getString(1));
                   long difference = endTime.getTime() - startTime.getTime();
                   if(difference<0){
                       Date dateMax = sdf.parse("24:00");
                       Date dateMin = sdf.parse("00:00");
                       difference=(dateMax.getTime() -startTime.getTime() )+(endTime.getTime()-dateMin.getTime());
                   }
                   Double doubleDiff  = Double.valueOf(difference/3600000.00);
                   if(doubleDiff <= 5){
                       totalTimeInMilli += difference;
                   }else if((doubleDiff > 5 ) && (doubleDiff < 7)){
                       totalTimeInMilli += (difference - 1800000.00);
                   }else if(doubleDiff >= 7){
                       totalTimeInMilli += (difference - 3600000.00);
                       //Toast.makeText(mContext,(String.valueOf(doubleDiff)),Toast.LENGTH_SHORT).show();
                   }else {
                       totalTimeInMilli += difference;
                   }
                   //return sdf.parse(cursor.getString(0)).toString();
               }catch (ParseException e){
                   //hi
               }
               //return cursor.getString(0)+","+cursor.getString(1);
           }while (cursor.moveToNext());
       }

       //return cursor.getString(0);
       double differenceInHours = totalTimeInMilli/3600000;
       //return new SimpleDateFormat("HH:mm").format(dateObject2);
//       if(totalTimeInMilli > 1){
//           return String.format("%.2f",differenceInHours) + " hours";
//       }else {
//           return String.format("%.2f",differenceInHours) + " hour";
//       }
       return String.format("%.2f",differenceInHours);
   }

   //public String getWeekHoursOfUnpaidBreaks()

   public String getMoneyEarned(Double hours, Double hourlyWage){

       Double grossPay = hours * hourlyWage;

       //Initialize Other Deciding Variables
       //26 below is biweekly(26 weeks in a year)
       //11,635 is the federal basic tax exempt
       //0.15 is the rate of tax exempt.
       //For tomorrow add in the different clauses pertaining to grossPay in federal and provincial tax;
       //Also add in different options for province and pay periods

       Double FEDERAL_TAX_CLAIM = 11635.00;
       Double EMPLOYEE_CREDIT = 1177.00;
       Double CPP_EXEMPTION = 3500.00;
       Double CPP_PERCENTAGE = 0.0495;
       Double EI_PERCENTAGE = 0.0163;
       Integer PAY_PERIOD = 26; //BiWeekly

       //Provincial Tax Claims
       Double MANITOBA_PROVINCIAL_TAX_CLAIM = 9271.00;
       Double NEWFOUNDLND_PROVINCIAL_TAX_CLAIM = 8978.00;
       Double NOVASCOTIA_PROVINCIAL_TAX_CLAIM = 8481.00;
       Double PEI_PROVINCIAL_TAX_CLAIM = 8000.00;
       Double NEWBRUNSWICK_PROVINCIAL_TAX_CLAIM = 9895.00;
       Double QUEBEC_PROVINCIAL_TAX_CLAIM = 11635.00;
       Double ONTARIO_PROVINCIAL_TAX_CLAIM = 10171.00;
       Double SASKATCHEWAN_PROVINCIAL_TAX_CLAIM = 16065.00;
       Double ALBERTA_PROVINCIAL_TAX_CLAIM = 18690.00;
       Double BC_PROVINCIAL_TAX_CLAIM = 10208.00;
       Double YUKON_PROVINCIAL_TAX_CLAIM = 11635.00;
       Double NORTHWEST_PROVINCIAL_TAX_CLAIM = 14278.00;
       Double NUNAVAT_PROVINCIAL_TAX_CLAIM = 13128.00;

       //FEDERAL TAX TIERS
       Double MAX_FED_TAX_TIER1 = 45916.00;
       Double MAX_FED_TAX_TIER1_CONSTANT = 0.00;
       Double MAX_FED_TAX_TIER1_PERCENT = 0.15;
       Double MAX_FED_TAX_TIER2 = 91831.00;
       Double MAX_FED_TAX_TIER2_CONSTANT = 2525.00;
       Double MAX_FED_TAX_TIER2_PERCENT = 0.205;
       Double MAX_FED_TAX_TIER3 = 142353.00;
       Double MAX_FED_TAX_TIER3_CONSTANT = 7576.00;
       Double MAX_FED_TAX_TIER3_PERCENT = 0.260;
       Double MAX_FED_TAX_TIER4 = 202800.00;
       Double MAX_FED_TAX_TIER4_CONSTANT = 11847.00;
       Double MAX_FED_TAX_TIER4_PERCENT = 0.290;
       Double MAX_FED_TAX_TIER5 = 0.00;//ANYTHING OVER $202,800
       Double MAX_FED_TAX_TIER5_CONSTANT = 19959.00;
       Double MAX_FED_TAX_TIER5_PERCENT = 0.330;



       //Do Tax Deductions
       Double CPP = (((grossPay*PAY_PERIOD)-CPP_EXEMPTION)/PAY_PERIOD)*CPP_PERCENTAGE;
       Double EI  = grossPay*EI_PERCENTAGE;

       if(CPP < 0 || this.getPaycheckColumn(KEY_CPP_EXEMPT).equals("true")){
           CPP = 0.00;
       }

       if(EI < 0 || this.getPaycheckColumn(KEY_EI_EXEMPT).equals("true")){
           EI = 0.00;
       }

       Double FED = ((((grossPay - CPP - EI)*PAY_PERIOD) - EMPLOYEE_CREDIT - FEDERAL_TAX_CLAIM)*.15)/PAY_PERIOD;
       Double PRO = ((((grossPay - CPP - EI)*PAY_PERIOD)-MANITOBA_PROVINCIAL_TAX_CLAIM)/PAY_PERIOD)*0.108;

       if(FED < 0){
           FED = 0.00;
       }

       if(PRO < 0){
           PRO = 0.00;
       }

       Double netPay = grossPay - CPP - EI - FED - PRO;

       if(this.getPaycheckColumn(KEY_USE_CANADIAN_TAXES).equals("true")) {
           return String.format("$%.2f", netPay);
       }else{
           return String.format("$%.2f", grossPay);
       }
   }

   public String getWeekNextShift(String startDate, String endDate){
       SQLiteDatabase db = this.getWritableDatabase();
       Date date = new Date();
       String today = new SimpleDateFormat("yyyy-MM-dd").format(date);
      // String query = "SELECT " + KEY_DATE+" FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE + " BETWEEN '" + startDate + "' AND '" + endDate+"'";
       String query = "SELECT " + KEY_DATE + " FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE + " >= date('now','localtime') AND " + KEY_DATE + " BETWEEN '"+ startDate+"' AND '"+ endDate+"' ORDER BY " + KEY_DATE + " ASC LIMIT 1";
       Cursor cursor = db.rawQuery(query,null);
       if(cursor.moveToFirst()){
           do{
               String nextWorkingDate = cursor.getString(0);
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               try{
                   Date date1 = sdf.parse(nextWorkingDate);
                   if(sdf.format(date).equals(sdf.format(date1))){
                       return "Today";
                   }else {
                       //Toast.makeText(mContext,"Date:"+new SimpleDateFormat("yyyy-MM-dd").format(date)+" Date1:"+new SimpleDateFormat("yyyy-MM-dd").format(date1),Toast.LENGTH_LONG).show();
                       return new SimpleDateFormat("EE").format(date1);
                   }
               }catch (ParseException e){
                   return "N/AE";
               }
           }while(cursor.moveToNext());
       }
       String amount = String.valueOf(cursor.getCount());

       //return nextWorkingDate;
       return "N/A";
   }

   //MONTH STATS

    public Integer getMonthWorkingDays(){
        SQLiteDatabase db = this.getWritableDatabase();
        Date today = new Date();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,Integer.valueOf(new SimpleDateFormat("yyyy").format(today)));
        cal.set(Calendar.MONTH,Integer.valueOf(new SimpleDateFormat("MM").format(today)));
        int FirstDayOfMonth = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        int NumOfDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
        String startDate = sdf.format(today)+"-01";
        String endDate = sdf.format(today)+"-"+String.valueOf(NumOfDaysInMonth);
        //Toast.makeText(mContext,"Start:"+startDate+" End:"+endDate,Toast.LENGTH_LONG).show();
        String query = "SELECT DISTINCT " + KEY_DATE + " FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor cursor = db.rawQuery(query,null);
        int days = cursor.getCount();
        cursor.close();
        db.close();
        return  days;
    }

    public String getMonthWorkingHours(){
        SQLiteDatabase db = this.getWritableDatabase();
        Date today = new Date();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,Integer.valueOf(new SimpleDateFormat("yyyy").format(today)));
        cal.set(Calendar.MONTH,Integer.valueOf(new SimpleDateFormat("MM").format(today)));
        int FirstDayOfMonth = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        int NumOfDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
        String startDate = sdf1.format(today)+"-01";
        String endDate = sdf1.format(today)+"-"+String.valueOf(NumOfDaysInMonth);

        String query = "SELECT " + KEY_START_TIME+","+KEY_END_TIME+" FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE + " BETWEEN '" + startDate + "' AND '" + endDate+"'";
        Cursor cursor = db.rawQuery(query,null);
        double totalTimeInMilli = 0.0;

        if(cursor.moveToFirst()){
            do{
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Date startTime = sdf.parse(cursor.getString(0));
                    Date endTime = sdf.parse(cursor.getString(1));
                    long difference = endTime.getTime() - startTime.getTime();
                    if(difference<0){
                        Date dateMax = sdf.parse("24:00");
                        Date dateMin = sdf.parse("00:00");
                        difference=(dateMax.getTime() -startTime.getTime() )+(endTime.getTime()-dateMin.getTime());
                    }
                    Double doubleDiff  = Double.valueOf(difference/3600000.00);
                    if(doubleDiff <= 5){
                        totalTimeInMilli += difference;
                    }else if((doubleDiff > 5 ) && (doubleDiff < 7)){
                        totalTimeInMilli += (difference - 1800000.00);
                    }else if(doubleDiff >= 7){
                        totalTimeInMilli += (difference - 3600000.00);
                        //Toast.makeText(mContext,(String.valueOf(doubleDiff)),Toast.LENGTH_SHORT).show();
                    }else {
                        totalTimeInMilli += difference;
                    }
                    //return sdf.parse(cursor.getString(0)).toString();
                }catch (ParseException e){
                    //hi
                }
                //return cursor.getString(0)+","+cursor.getString(1);
            }while (cursor.moveToNext());
        }

        //return cursor.getString(0);
        double differenceInHours = totalTimeInMilli/3600000;
        //return new SimpleDateFormat("HH:mm").format(dateObject2);
//       if(totalTimeInMilli > 1){
//           return String.format("%.2f",differenceInHours) + " hours";
//       }else {
//           return String.format("%.2f",differenceInHours) + " hour";
//       }
        return String.format("%.2f",differenceInHours);
    }


    public String getMonthNextShift(){
        SQLiteDatabase db = this.getWritableDatabase();

        Date todaysDD = new Date();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,Integer.valueOf(new SimpleDateFormat("yyyy").format(todaysDD)));
        cal.set(Calendar.MONTH,Integer.valueOf(new SimpleDateFormat("MM").format(todaysDD)));
        int FirstDayOfMonth = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        int NumOfDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
        String startDate = sdf1.format(todaysDD)+"-01";
        String endDate = sdf1.format(todaysDD)+"-"+String.valueOf(NumOfDaysInMonth);

        Date date = new Date();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(date);
        // String query = "SELECT " + KEY_DATE+" FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE + " BETWEEN '" + startDate + "' AND '" + endDate+"'";
        String query = "SELECT " + KEY_DATE + " FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE + " >= date('now','localtime') AND " + KEY_DATE + " BETWEEN '"+ startDate+"' AND '"+ endDate+"' ORDER BY " + KEY_DATE + " ASC LIMIT 1";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                String nextWorkingDate = cursor.getString(0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try{
                    Date date1 = sdf.parse(nextWorkingDate);
                    if(sdf.format(date).equals(sdf.format(date1))){
                        return "Today";
                    }else {
                        //Toast.makeText(mContext,"Date:"+new SimpleDateFormat("yyyy-MM-dd").format(date)+" Date1:"+new SimpleDateFormat("yyyy-MM-dd").format(date1),Toast.LENGTH_LONG).show();
                        return new SimpleDateFormat("MMM dd").format(date1);
                    }
                }catch (ParseException e){
                    return "N/AE";
                }
            }while(cursor.moveToNext());
        }
        String amount = String.valueOf(cursor.getCount());

        //return nextWorkingDate;
        return "N/A";
    }

    public int getDaysOffThisMonth(int daysWorking){
        Date today = new Date();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,Integer.valueOf(new SimpleDateFormat("yyyy").format(today)));
        cal.set(Calendar.MONTH,Integer.valueOf(new SimpleDateFormat("MM").format(today)));
        int NumOfDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        return NumOfDaysInMonth - daysWorking;
    }

    public String getJobStatus(Double weekHours){
        if(weekHours > 35.00){
            return "Full-time";
        }else{
            return "Part-time";
        }
    }

    public Integer getNumOfUniqueJobs(){
        SQLiteDatabase db = this.getWritableDatabase();

        Date todaysDD = new Date();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,Integer.valueOf(new SimpleDateFormat("yyyy").format(todaysDD)));
        cal.set(Calendar.MONTH,Integer.valueOf(new SimpleDateFormat("MM").format(todaysDD)));
        int FirstDayOfMonth = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        int NumOfDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
        String startDate = sdf1.format(todaysDD)+"-01";
        String endDate = sdf1.format(todaysDD)+"-"+String.valueOf(NumOfDaysInMonth);

        String query = "SELECT DISTINCT " + KEY_LOCATION + " FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE +" BETWEEN '"+startDate+"' AND '"+endDate+"'";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
        return  count;
    }


   public Boolean isDBPopulated(){
       SQLiteDatabase db = this.getWritableDatabase();
       String query = "SELECT * FROM " + TABLE_JOB_SCHEDULE;
       Cursor cursor =  db.rawQuery(query,null);

       if(cursor.getCount() <= 0){
           return  false;
       }else{
           return  true;
       }
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


    public List<JobEntry> getShiftFromID(String id){
        List<JobEntry> shiftList =  new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_ID + "='" + id+"'";
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

    public List<JobEntry> getAllShifts(String condition){
        List<JobEntry> shiftList = new ArrayList<>();
        String query;
        if(condition.equals("now")){
            query = "SELECT * FROM " + TABLE_JOB_SCHEDULE+ " WHERE " + KEY_DATE + " >= date('now','localtime') ORDER BY " + KEY_DATETIME + " ASC";
        }else if(condition.equals("all")){
            query = "SELECT * FROM " + TABLE_JOB_SCHEDULE+ " ORDER BY " + KEY_DATETIME + " ASC";
        }else{
            query = "SELECT * FROM " + TABLE_JOB_SCHEDULE+ " WHERE " + KEY_DATETIME + " >= datetime('now','localtime') ORDER BY " + KEY_DATETIME + " ASC";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);


        if(cursor.moveToFirst()){
            do{
                //Toast.makeText(mContext,cursor.getString(10),Toast.LENGTH_SHORT).show();
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


    public List<PayCheck> getPaycheckData(String salaryDate, int ID){
        Date salaryDateA;
        String beginOfPayPeriod;
        Calendar c = Calendar.getInstance();
        String endOfPayPeriod;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            salaryDateA = sdf.parse(salaryDate);
        }catch (ParseException e){
            salaryDateA = new Date();
        }

        c.setTime(salaryDateA);
        c.add(Calendar.DATE,-7);
        endOfPayPeriod = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        c.add(Calendar.DATE,-13);
        beginOfPayPeriod = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

        List<PayCheck> paycheckList = new ArrayList<>();
        TreeSet<String> locationOfJobs = new TreeSet<>();

        Double totalHours = Double.valueOf(this.getWeekWorkingHours(beginOfPayPeriod,endOfPayPeriod));
        String totalSalary = this.getMoneyEarned(totalHours,Double.valueOf(this.getPaycheckColumn(KEY_SALARY)));
        String payType;
        if(this.getPaycheckColumn(KEY_USE_CANADIAN_TAXES).equals("true")){
            payType="Net Pay";
        }else{
            payType="Gross Pay";
        }
        //payType="hi";
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_JOB_SCHEDULE + " WHERE " + KEY_DATE + " BETWEEN '" + beginOfPayPeriod + "' AND '" + endOfPayPeriod+"'";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                //totalHours += Double.valueOf(cursor.getString(0));
                //locationName = cursor.getString(2);
                locationOfJobs.add(cursor.getString(2));
            }while(cursor.moveToNext());
        }

        //Toast.makeText(mContext,"" + locationOfJobs.size(),Toast.LENGTH_SHORT).show();

        String allJobs="";
        for(String jobLocation:locationOfJobs){
            if(allJobs.isEmpty()){
                allJobs = jobLocation;
            }else {
                allJobs = allJobs.concat("/" + jobLocation);
            }
        }

       // for(String jobLocation:locationOfJobs){
        paycheckList.add(new PayCheck(ID,beginOfPayPeriod.replace("-","/")+" - "+endOfPayPeriod.replace("-","/"),allJobs,salaryDate,totalSalary,payType));
        //}
        return paycheckList;
    }

    public void truncateShiftsTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_JOB_SCHEDULE;
        db.execSQL(query);
        db.close();
    }

    public void dropPaycheckSettings(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DROP TABLE IF EXISTS " + TABLE_PAYCHECK_SETTINGS;
        db.execSQL(query);
    }

    public void truncatePaycheckSettings(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_PAYCHECK_SETTINGS;
        db.execSQL(query);
        db.close();
    }

}
