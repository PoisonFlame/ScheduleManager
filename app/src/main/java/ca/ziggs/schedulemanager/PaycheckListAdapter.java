package ca.ziggs.schedulemanager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Robby Sharma on 6/26/2017.
 */

public class PaycheckListAdapter extends BaseAdapter {
    private Context mContext;
    private List<PayCheck> mPaycheckList;
    DBHandler db;

    public PaycheckListAdapter(Context mContext, List<PayCheck> mPaycheckList) {
        this.mContext = mContext;
        this.mPaycheckList = mPaycheckList;
    }

    @Override
    public int getCount() {
        return mPaycheckList.size();
    }

    @Override
    public Object getItem(int position){
        return mPaycheckList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.listview_paycheck_item, null);
        final TextView textPayPeriod = (TextView) v.findViewById(R.id.textPayPeriod);
        final TextView textName = (TextView) v.findViewById(R.id.textName);
        TextView textSalary = (TextView) v.findViewById(R.id.textSalary);
        final TextView textPayday = (TextView) v.findViewById(R.id.textPayday);
        TextView textSalaryType = (TextView) v.findViewById(R.id.textPayType);
        final ImageView imgArrowUpDown = (ImageView)v.findViewById(R.id.imgArrowUpDown);
        final LinearLayout layoutPaycheckDetails = (LinearLayout)v.findViewById(R.id.layoutPaycheckDetails);
        LinearLayout layoutPaycheckDataTopBar = (LinearLayout)v.findViewById(R.id.layoutPaycheckDataTopBar);
        Button btnMoreDetails = (Button) v.findViewById(R.id.btnMoreDetails);
        Date payDate;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            payDate = sdf.parse(mPaycheckList.get(i).getSalaryPayDate());
        }catch (ParseException e){
            payDate = new Date();
        }

        textPayPeriod.setText(mPaycheckList.get(i).getPaycheckPeriod());
        textName.setText(mPaycheckList.get(i).getName());
        textSalary.setText(mPaycheckList.get(i).getSalary());
        textSalaryType.setText(mPaycheckList.get(i).getSalaryType());
        textPayday.setText(new SimpleDateFormat("EE, dd MMM yyyy").format(payDate));

        if(layoutPaycheckDetails.getVisibility() == View.VISIBLE){
            //layoutPaycheckDetails.setVisibility(View.GONE);
            imgArrowUpDown.setImageResource(R.drawable.ic_action_arrow_up);
        }else{
            //layoutPaycheckDetails.setVisibility(View.VISIBLE);
            imgArrowUpDown.setImageResource(R.drawable.ic_action_arrow_down);
        }

        if(mPaycheckList.get(i).getId() == 0){
            layoutPaycheckDetails.setVisibility(View.VISIBLE);
            imgArrowUpDown.setImageResource(R.drawable.ic_action_arrow_up);
        }else{
            layoutPaycheckDetails.setVisibility(View.GONE);
            imgArrowUpDown.setImageResource(R.drawable.ic_action_arrow_down);
        }

        layoutPaycheckDataTopBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layoutPaycheckDetails.getVisibility() == View.VISIBLE){
                    layoutPaycheckDetails.setVisibility(View.GONE);
                    imgArrowUpDown.setImageResource(R.drawable.ic_action_arrow_down);
                }else{
                    layoutPaycheckDetails.setVisibility(View.VISIBLE);
                    imgArrowUpDown.setImageResource(R.drawable.ic_action_arrow_up);
                }
            }
        });

        btnMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext,"ID Selected: " + mPaycheckList.get(i).getId(),Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(view.getContext(),PaycheckDetailsActivity.class);
                mIntent.putExtra("salaryDate",mPaycheckList.get(i).getSalaryPayDate());
                mIntent.putExtra("payPeriod", textPayPeriod.getText().toString());
                mIntent.putExtra("employer", textName.getText().toString());
                mIntent.putExtra("payDay", textPayday.getText().toString());
                view.getContext().startActivity(mIntent);
            }
        });

        imgArrowUpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layoutPaycheckDetails.getVisibility() == View.VISIBLE){
                    layoutPaycheckDetails.setVisibility(View.GONE);
                    imgArrowUpDown.setImageResource(R.drawable.ic_action_arrow_down);
                }else{
                    layoutPaycheckDetails.setVisibility(View.VISIBLE);
                    imgArrowUpDown.setImageResource(R.drawable.ic_action_arrow_up);
                }
            }
        });

        db = new DBHandler(mContext);

        String[] payPeriod = textPayPeriod.getText().toString().split("-");
        String beginPeriod = payPeriod[0].trim().replace("/","-");
        String endPeriod = payPeriod[1].trim().replace("/","-");

        //db.setPaycheckInDB(textName.getText().toString(),mPaycheckList.get(i).getSalaryPayDate(),db.getPaycheckColumn(db.KEY_SALARY),db.getWeekWorkingHours(beginPeriod, endPeriod),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "grosspay"),"0.00","0.00","0.00","0.00","0.00","0.00",db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "grosspay"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "fed"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "pro"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "cpp"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "ei"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "default"));
        setPaycheckASyncTask setPaycheckASyncTask = new setPaycheckASyncTask();
        setPaycheckASyncTask.execute(textName.getText().toString(),mPaycheckList.get(i).getSalaryPayDate(),beginPeriod,endPeriod);
        //setPaycheckASyncTask.execute(textName.getText().toString(),mPaycheckList.get(i).getSalaryPayDate(),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "grosspay"),"0.00","0.00","0.00","0.00","0.00","0.00",db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "grosspay"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "fed"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "pro"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "cpp"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "ei"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "default"));

        v.setTag(mPaycheckList.get(i).getId());
        return v;
    }

    private class setPaycheckASyncTask extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {


           // db.setPaycheckInDB(strings[0],strings[1],db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "grosspay"),"0.00","0.00","0.00","0.00","0.00","0.00",db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "grosspay"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "fed"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "pro"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "cpp"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "ei"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "default"));

            db.setPaycheckInDB(strings[0],strings[1],db.getPaycheckColumn(db.KEY_SALARY),db.getWeekWorkingHours(strings[2], strings[3]),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "grosspay"),"0.00","0.00","0.00","0.00","0.00","0.00",db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "grosspay"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "fed"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "pro"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "cpp"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "ei"),db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(strings[2], strings[3])), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "default"));
            //db.setPaycheckInDB(strings[0],strings[1],strings[2],strings[3],strings[4],strings[5],strings[6],strings[7],strings[8],
              //      strings[9],strings[10],strings[11],strings[12],strings[13],strings[14],strings[15],strings[16]);
            return null;
        }
    }

}
