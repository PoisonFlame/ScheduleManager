package ca.ziggs.schedulemanager.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.ziggs.schedulemanager.AddShiftActivity;
import ca.ziggs.schedulemanager.DBHandler;
import ca.ziggs.schedulemanager.JobEntry;
import ca.ziggs.schedulemanager.R;

/**
 * Created by Robby Sharma on 6/8/2017.
 */

public class MainFragment extends Fragment{

    int currentFocusedShift =1;
    int maxFocusedShifts;

    public MainFragment(){

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        LinearLayout layout_no_shifts_today = (LinearLayout)view.findViewById(R.id.layout_no_shifts_today);
        RelativeLayout layout_todays_shift = (RelativeLayout) view.findViewById(R.id.today_layer_layout);
        final TextView textTitle = (TextView)view.findViewById(R.id.textTitle);
        final TextView textLocation = (TextView)view.findViewById(R.id.textLocation);
        final TextView textBreaks = (TextView)view.findViewById(R.id.textBreak);
        final TextView textDuration = (TextView)view.findViewById(R.id.textDuration69);
        final TextView textShiftTime = (TextView)view.findViewById(R.id.textWorkTime);
        ImageButton btnNextJob = (ImageButton)view.findViewById(R.id.btnNextJob);
        TextView textWeekLabel = (TextView)view.findViewById(R.id.textWeeklyStats);
        TextView textMonthLabel = (TextView)view.findViewById(R.id.textMonthlyStats);
        TextView textHoursWorkingInWeek = (TextView)view.findViewById(R.id.textTimeWorking);
        TextView textSalary = (TextView)view.findViewById(R.id.textPayWage);
        TextView textDaysWorkingInWeek = (TextView)view.findViewById(R.id.textDaysWorking);
        TextView textDaysOfThisMonth = (TextView)view.findViewById(R.id.textDaysOffThisMonth);
        TextView textMooneyEarnedInMonth = (TextView)view.findViewById(R.id.textMoneyEarnedMonth);
        TextView textDaysWorkingInMonth = (TextView)view.findViewById(R.id.textDaysWorkingThisMonth);
        TextView textNextShiftInMonth = (TextView)view.findViewById(R.id.textNextShiftMonth);
        TextView textNumOfJobs = (TextView)view.findViewById(R.id.textNumberOfJobs);
        TextView textJobStatus = (TextView)view.findViewById(R.id.textJobStatus);
        TextView textHoursWorkingInMonth = (TextView)view.findViewById(R.id.textTimeWorkingMonth);
        TextView textNextShiftInWeek = (TextView)view.findViewById(R.id.textNextShiftWeek);
        TextView textMoneyEarnedInWeek = (TextView)view.findViewById(R.id.textMoneyEarned);
        LinearLayout layout_noStatsAvailable = (LinearLayout)view.findViewById(R.id.layout_noStatsAvailable);
        Button btnAddShifts = (Button)view.findViewById(R.id.btnMainFragAddShifts);
        final ImageView imageJobTitle = (ImageView) view.findViewById(R.id.imgJobTitle);
        final TextView textNumOfShifts = (TextView)view.findViewById(R.id.textLabelNumOfShifts);
        DBHandler db = new DBHandler(getContext());

        if(db.checkTodayShift()){
            layout_no_shifts_today.setVisibility(View.GONE);
            layout_todays_shift.setVisibility(View.VISIBLE);

            final List<JobEntry> todayShiftList = db.getTodaysShifts();
            int numOfTodayShifts = db.getNumOfTodaysShifts();

            textTitle.setText(todayShiftList.get(0).getTitle());
            textLocation.setText(todayShiftList.get(0).getLocation());
            textBreaks.setText(todayShiftList.get(0).getBreaks());
            textDuration.setText(todayShiftList.get(0).getDuration());
            textShiftTime.setText(todayShiftList.get(0).getFormattedTime());
            TextDrawable drawable = TextDrawable.builder()
                    .buildRoundRect(todayShiftList.get(0).getLocation().toString().substring(0,1), Color.DKGRAY,60);
            ImageView image = (ImageView) view.findViewById(R.id.imgJobTitle);
            image.setImageDrawable(drawable);

            if(numOfTodayShifts == 1){

                textNumOfShifts.setVisibility(View.GONE);
                btnNextJob.setVisibility(View.GONE);
                //textNumOfShifts.setText("1/"+numOfTodayShifts);
            }else{
                textNumOfShifts.setVisibility(View.VISIBLE);
                btnNextJob.setVisibility(View.VISIBLE);
                maxFocusedShifts = numOfTodayShifts;
                textNumOfShifts.setText("1/"+numOfTodayShifts);
            }

            btnAddShifts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent = new Intent(getContext(), AddShiftActivity.class);
                    getActivity().startActivity(mIntent);
                }
            });


            btnNextJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(currentFocusedShift < maxFocusedShifts){
                        currentFocusedShift += 1;
                        textNumOfShifts.setText(currentFocusedShift + "/" + maxFocusedShifts);
                    }else if(currentFocusedShift == maxFocusedShifts){
                        currentFocusedShift = 1;
                        textNumOfShifts.setText(currentFocusedShift + "/" + maxFocusedShifts);
                    }

                    textTitle.setText(todayShiftList.get(currentFocusedShift -1).getTitle());
                    textLocation.setText(todayShiftList.get(currentFocusedShift -1).getLocation());
                    textBreaks.setText(todayShiftList.get(currentFocusedShift -1).getBreaks());
                    textDuration.setText(todayShiftList.get(currentFocusedShift -1).getDuration());
                    textShiftTime.setText(todayShiftList.get(currentFocusedShift -1).getFormattedTime());
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRoundRect(todayShiftList.get(currentFocusedShift -1).getLocation().toString().substring(0,1), Color.DKGRAY,60);
                    imageJobTitle.setImageDrawable(drawable);

                }
            });

        }else{
            layout_no_shifts_today.setVisibility(View.VISIBLE);
            layout_todays_shift.setVisibility(View.GONE);
        }


        if(db.isDBPopulated()){
            //Shifts Exist in DB

            layout_noStatsAvailable.setVisibility(View.GONE);

            Date date = new Date();
            Calendar cal = Calendar.getInstance();

            //SET FIRST DAY OF WEEK.
            if(db.getPaycheckColumn("weekStartDay").equals("Monday")){
                cal.setFirstDayOfWeek(Calendar.MONDAY);
            }else if(db.getPaycheckColumn("weekStartDay").equals("Tuesday")){
                cal.setFirstDayOfWeek(Calendar.TUESDAY);
            }else if(db.getPaycheckColumn("weekStartDay").equals("Wednesday")){
                cal.setFirstDayOfWeek(Calendar.WEDNESDAY);
            }else if(db.getPaycheckColumn("weekStartDay").equals("Thursday")){
                cal.setFirstDayOfWeek(Calendar.THURSDAY);
            }else if(db.getPaycheckColumn("weekStartDay").equals("Friday")){
                cal.setFirstDayOfWeek(Calendar.FRIDAY);
            }else if(db.getPaycheckColumn("weekStartDay").equals("Saturday")){
                cal.setFirstDayOfWeek(Calendar.SATURDAY);
            }else if(db.getPaycheckColumn("weekStartDay").equals("Sunday")){
                cal.setFirstDayOfWeek(Calendar.SUNDAY);
            }else{
                cal.setFirstDayOfWeek(Calendar.MONDAY);
            }
            //cal.setFirstDayOfWeek(Calendar.SATURDAY);
            cal.setTime(date);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - cal.getFirstDayOfWeek();
            cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

            Date weekStart = cal.getTime();
            if(weekStart.after(date)){
                cal.add(Calendar.DAY_OF_MONTH, -7);
                weekStart = cal.getTime();
            }
            cal.add(Calendar.DAY_OF_MONTH,6);
            Date weekEnd = cal.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
            String textWeekStart = sdf.format(weekStart);
            String textWeekEnd = sdf.format(weekEnd);

            Typeface face;
            face = Typeface.createFromAsset(getActivity().getAssets(),"Prototype.ttf");
            textWeekLabel.setTypeface(face);
            textMonthLabel.setTypeface(face);
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM yyyy");
            String textMonthName = sdf2.format(cal.getTime());
            textWeekLabel.setText("THIS WEEK("+textWeekStart.toString()+" - "+textWeekEnd.toString()+")");
            textMonthLabel.setText("THIS MONTH("+textMonthName+")");

            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
            int workingDaysInWeek = db.getWeekWorkingDays(sdf3.format(weekStart),sdf3.format(weekEnd));
            if(workingDaysInWeek == 1){
                textDaysWorkingInWeek.setText(workingDaysInWeek + " day");
            }else{
                textDaysWorkingInWeek.setText(workingDaysInWeek + " days");
            }

            if(Double.valueOf(db.getWeekWorkingHours(sdf3.format(weekStart),sdf3.format(weekEnd)))>1){
                textHoursWorkingInWeek.setText(db.getWeekWorkingHours(sdf3.format(weekStart),sdf3.format(weekEnd)) + "");
            }else{
                textHoursWorkingInWeek.setText(db.getWeekWorkingHours(sdf3.format(weekStart),sdf3.format(weekEnd)) + "");
            }

            textMoneyEarnedInWeek.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(sdf3.format(weekStart),sdf3.format(weekEnd))),11.45));
            textNextShiftInWeek.setText(db.getWeekNextShift(sdf3.format(weekStart),sdf3.format(weekEnd)));

            if(db.getMonthWorkingDays() == 1){
                textDaysWorkingInMonth.setText(String.valueOf(db.getMonthWorkingDays()) + " day");
            }else{
                textDaysWorkingInMonth.setText(String.valueOf(db.getMonthWorkingDays()) + " days");
            }

            if(Double.valueOf(db.getMonthWorkingHours()) == 1.00){
                textHoursWorkingInMonth.setText(db.getMonthWorkingHours()+ "");
            }else{
                textHoursWorkingInMonth.setText(db.getMonthWorkingHours() + "");
            }

            textMooneyEarnedInMonth.setText(db.getMoneyEarned(Double.valueOf(db.getMonthWorkingHours()),11.45));
            textNextShiftInMonth.setText(db.getMonthNextShift());

            if(db.getDaysOffThisMonth(db.getMonthWorkingDays()) == 1){
                textDaysOfThisMonth.setText(db.getDaysOffThisMonth(db.getMonthWorkingDays()) + " day");
            }else{
                textDaysOfThisMonth.setText(db.getDaysOffThisMonth(db.getMonthWorkingDays()) + " days");
            }



            textJobStatus.setText(db.getJobStatus(Double.valueOf(db.getWeekWorkingHours(sdf3.format(weekStart),sdf3.format(weekEnd)))));
            textNumOfJobs.setText(String.valueOf(db.getNumOfUniqueJobs()));
            textSalary.setText(db.getPaycheckColumn("salary"));
            //textDaysWorkingInMonth.setText("hello");
            //textHoursWorkingInWeek.setText(db.getWeekWorkingHours(sdf3.format(weekStart),sdf3.format(weekEnd)));
            //textHoursWorkingInWeek.setText(db.isDBPopulated().toString());
        }else{
            layout_noStatsAvailable.setVisibility(View.VISIBLE);
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container,false);
        return rootView;

    }
}
