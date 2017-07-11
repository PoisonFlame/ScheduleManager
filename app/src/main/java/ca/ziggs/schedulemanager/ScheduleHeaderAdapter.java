package ca.ziggs.schedulemanager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Robby Sharma on 6/26/2017.
 */

public class ScheduleHeaderAdapter extends BaseAdapter {
    private Context mContext;
    private List<SchedHeader> mScheduleItems;
    private String duration;
    private ListView lvItems;
    private ItemListAdapter adapter;
    private List<Item> mItemList;
    DBHandler db;

    public ScheduleHeaderAdapter(Context mContext, List<SchedHeader> mScheduleItems) {
        this.mContext = mContext;
        this.mScheduleItems = mScheduleItems;
    }

    @Override
    public int getCount() {
        return mScheduleItems.size();
    }

    @Override
    public Object getItem(int position){
        return mScheduleItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.listview_schedule_headers, null);
        TextView textScheduleHeader = (TextView)v.findViewById(R.id.textScheduleHeaderDate);
        ListView listScheduleHeader = (ListView)v.findViewById(R.id.listScheduleHeader);
        LinearLayout layoutNoJobs = (LinearLayout)v.findViewById(R.id.layoutThisWeekNoSched);
        db = new DBHandler(mContext);
        mItemList = new ArrayList<>();
        textScheduleHeader.setText(mScheduleItems.get(i).getTest());
        List<JobEntry> shiftList = db.getAllShifts("betweenStart="+mScheduleItems.get(i).getStartTime()+"End="+mScheduleItems.get(i).getEndTime());




        for (JobEntry shift : shiftList){
            try{
                final SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
                final SimpleDateFormat sdf4 = new SimpleDateFormat("kk");
                final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd kk:mm");
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
                //Date shiftEnd = new Date();
                Date now = new Date();
                Date jobDate = sdf3.parse(shift.getDate());
                String nowDateString = sdf3.format(now);
                String jobDateString = sdf3.format(jobDate);
                Date strictShiftEndTime = sdf.parse(shift.getEndTime());
                String stringShiftEndTime = sdf4.format(strictShiftEndTime);
                Date shiftEnd;
                if(nowDateString.equals(jobDateString) && (Integer.valueOf(stringShiftEndTime) < 12)){
                    Calendar c = Calendar.getInstance();
                    c.setTime(jobDate);
                    c.add(Calendar.DATE,1);
                    String modifiedDate = sdf3.format(c.getTime());
                    shiftEnd = sdf2.parse(modifiedDate + " " + shift.getEndTime());
                }else{
                    shiftEnd = sdf2.parse(shift.getDate() + " " + shift.getEndTime());
                }
                //Toast.makeText(getContext(),"" + stringShiftEndTime,Toast.LENGTH_LONG).show();

                final Date dateObject = sdf.parse(shift.getStartTime());
                int difference = now.compareTo(shiftEnd);
                //Toast.makeText(getContext(),"" + nowDateString+" hi " + jobDateString,Toast.LENGTH_LONG).show();
                boolean isTimeValid = now.after(shiftEnd);
                //Toast.makeText(getContext(),shiftEnd.toString(),Toast.LENGTH_SHORT).show();
                //final Date dateObject2 = sdf.parse(shift.getEndTime());
                String shiftTime = new SimpleDateFormat("hh:mm aa").format(dateObject);//shiftTime.setText(new SimpleDateFormat("hh:mm aa").format(dateObject));
                //String shiftEndTime = new SimpleDateFormat("HH:mm").format(dateObject2);
                //if(!isTimeValid){
                if(difference < 0) {

                    SimpleDateFormat sdfGetDate = new SimpleDateFormat("yyyy-MM-dd");
                    Date getDate;
                    String displayDate;
                    try{
                        getDate = sdfGetDate.parse(shift.getDate());
                    }catch (ParseException e){
                        getDate = new Date();
                    }

                    if(new SimpleDateFormat("yyyy-MM-dd").format(now).equals(shift.getDate())){
                        displayDate = "Today (" + new SimpleDateFormat("dd MMMM, yyyy").format(getDate)+")";
                    }else{
                        displayDate = shift.getFormattedDate();
                    }

                    mItemList.add(new Item(shift.getId(), shift.getLocation(), displayDate, shiftTime, shift.getDuration(), shift.getLocation().substring(0, 1), shift.getFormattedTime(), shift.getDateTime()));

                    if(mItemList.isEmpty()){
                        layoutNoJobs.setVisibility(View.VISIBLE);
                    }else{
                        layoutNoJobs.setVisibility(View.GONE);
                    }
                }
            }catch (ParseException e){
                // Do Jack
            }
        }

        Toast.makeText(mContext,"Shifts: " + mItemList.size(),Toast.LENGTH_SHORT).show();

        adapter = new ItemListAdapter(mContext,mItemList);
        listScheduleHeader.setAdapter(adapter);
        //ScheduleFragment.setListViewHeightBasedOnChildren(listScheduleHeader);
        v.setTag(mScheduleItems.get(i).getId());
        return v;
    }

}
