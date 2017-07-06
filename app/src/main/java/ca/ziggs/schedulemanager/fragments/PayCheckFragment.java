package ca.ziggs.schedulemanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import ca.ziggs.schedulemanager.ActivityPaycheckSettings;
import ca.ziggs.schedulemanager.DBHandler;
import ca.ziggs.schedulemanager.PayCheck;
import ca.ziggs.schedulemanager.PaycheckListAdapter;
import ca.ziggs.schedulemanager.R;

/**
 * Created by Robby Sharma on 6/8/2017.
 */

public class PayCheckFragment extends Fragment {

    private ListView lvPaychecks;
    private PaycheckListAdapter adapter;
    private List<PayCheck> mPaycheckList;

    public PayCheckFragment(){

    }

    DBHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_paycheck, container,false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Button btnOpenPaycheckOptions = (Button)view.findViewById(R.id.btnOpenPaycheckSettings);
        lvPaychecks = (ListView)view.findViewById(R.id.listPaychecks);
        mPaycheckList = new ArrayList<>();
        TreeSet<String> salaryDates = new TreeSet<>(Collections.reverseOrder());
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date firstPaycheckDate;
        Date checkNumOfPaychecks;
        int numOfPaychecks =1;
        String allDates;
        db = new DBHandler(getContext());
        //db.dropPaychecks();
        db.createPaychecksDB();
        try{
            firstPaycheckDate = sdf.parse(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE));
            checkNumOfPaychecks = sdf.parse(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE));
            c.setTime(firstPaycheckDate);
            salaryDates.add(sdf.format(checkNumOfPaychecks));
            allDates = sdf.format(checkNumOfPaychecks);

            //Toast.makeText(getContext(),sdf.format(c.getTime()),Toast.LENGTH_SHORT).show();
        }catch (ParseException e){
            firstPaycheckDate = new Date();
            checkNumOfPaychecks = new Date();
            allDates = "";
        }

        //Get number of paychecks in between the two dates.
        while(now.after(checkNumOfPaychecks)){
            numOfPaychecks += 1;
            c.add(Calendar.DATE,14);
            checkNumOfPaychecks = c.getTime();
            //if(now.after(checkNumOfPaychecks)) {
                salaryDates.add(sdf.format(c.getTime()));
                allDates = allDates.concat("," + sdf.format(c.getTime()));
            //}
            //checkNumOfPaychecks
        }

        //allDates = allDates.concat(";;;");
        TextView test = (TextView)view.findViewById(R.id.textPaycheckInfoLabel);
        //test.setText(salaryDates.toString());
        //test.setText(db.getPaycheckData("2017-05-19"));
        //test.setText("" + allDates);
        // s

        if(salaryDates.isEmpty()){
            lvPaychecks.setVisibility(View.GONE);
        }else{
            lvPaychecks.setVisibility(View.VISIBLE);
        }

        //Toast.makeText(getContext(),"" + salaryDates.size(),Toast.LENGTH_SHORT).show();

         int id = 0;
        for(String salaryDate : salaryDates){
            List<PayCheck> paychecksInList = db.getPaycheckData(salaryDate,id);
            //Toast.makeText(getContext(),"" + paychecksInList.size() + " " + salaryDate,Toast.LENGTH_SHORT).show();
            id += 1;
            for(PayCheck paycheck : paychecksInList) {
                mPaycheckList.add(paycheck);
            }
            //mPaycheckList.add(new PayCheck(1,"2017/05/12 - 2017/05/27","Walmart",salaryDate,"$323.23","Net-Pay"));
        }

        if(mPaycheckList.isEmpty()){
            lvPaychecks.setVisibility(View.GONE);
        }else{
            lvPaychecks.setVisibility(View.VISIBLE);
        }

//        mPaycheckList.add(new PayCheck(1,"2017/05/12 - 2017/05/27","Walmart","1212","$323.23","Net-Pay"));
//        mPaycheckList.add(new PayCheck(2,"2017/05/28 - 2017/06/02","Walmart","1212","$343.23","Net-Pay"));
//        mPaycheckList.add(new PayCheck(3,"2017/06/03 - 2017/06/17","Walmart","1212","$293.23","Net-Pay"));
//        mPaycheckList.add(new PayCheck(4,"2017/06/18 - 2017/06/28","Walmart","1212","$523.23","Gross-Pay"));

        btnOpenPaycheckOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getContext(), ActivityPaycheckSettings.class);
                startActivity(mIntent);
            }
        });

        adapter = new PaycheckListAdapter(getContext(),mPaycheckList);
        lvPaychecks.setAdapter(adapter);

        //super.onViewCreated(view, savedInstanceState);
    }
}
