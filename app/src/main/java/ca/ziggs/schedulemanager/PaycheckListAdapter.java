package ca.ziggs.schedulemanager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        TextView textPayPeriod = (TextView) v.findViewById(R.id.textPayPeriod);
        TextView textName = (TextView) v.findViewById(R.id.textName);
        TextView textSalary = (TextView) v.findViewById(R.id.textSalary);
        TextView textPayday = (TextView) v.findViewById(R.id.textPayday);
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
                Toast.makeText(mContext,"ID Selected: " + mPaycheckList.get(i).getId(),Toast.LENGTH_SHORT).show();
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
        v.setTag(mPaycheckList.get(i).getId());
        return v;
    }

}
