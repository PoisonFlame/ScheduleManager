package ca.ziggs.schedulemanager.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

import ca.ziggs.schedulemanager.DBHandler;
import ca.ziggs.schedulemanager.JobEntry;
import ca.ziggs.schedulemanager.R;

/**
 * Created by Robby Sharma on 6/8/2017.
 */

public class MainFragment extends Fragment{

    public MainFragment(){

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        LinearLayout layout_no_shifts_today = (LinearLayout)view.findViewById(R.id.layout_no_shifts_today);
        RelativeLayout layout_todays_shift = (RelativeLayout) view.findViewById(R.id.today_layer_layout);
        TextView textTitle = (TextView)view.findViewById(R.id.textTitle);
        TextView textLocation = (TextView)view.findViewById(R.id.textLocation);
        TextView textBreaks = (TextView)view.findViewById(R.id.textBreak);
        TextView textDuration = (TextView)view.findViewById(R.id.textDuration69);
        TextView textShiftTime = (TextView)view.findViewById(R.id.textWorkTime);
        TextView textNumOfShifts = (TextView)view.findViewById(R.id.textLabelNumOfShifts);
        DBHandler db = new DBHandler(getContext());

        if(db.checkTodayShift()){
            layout_no_shifts_today.setVisibility(View.GONE);
            layout_todays_shift.setVisibility(View.VISIBLE);

            List<JobEntry> todayShiftList = db.getTodaysShifts();
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
                //textNumOfShifts.setText("1/"+numOfTodayShifts);
            }else{
                textNumOfShifts.setVisibility(View.VISIBLE);
                textNumOfShifts.setText("1/"+numOfTodayShifts);
            }

        }else{
            layout_no_shifts_today.setVisibility(View.VISIBLE);
            layout_todays_shift.setVisibility(View.GONE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container,false);
        return rootView;

    }
}
