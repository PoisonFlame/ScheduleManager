package ca.ziggs.schedulemanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ca.ziggs.schedulemanager.AddShiftActivity;
import ca.ziggs.schedulemanager.DBHandler;
import ca.ziggs.schedulemanager.Item;
import ca.ziggs.schedulemanager.ItemListAdapter;
import ca.ziggs.schedulemanager.JobEntry;
import ca.ziggs.schedulemanager.R;
import ca.ziggs.schedulemanager.SchedHeader;
import ca.ziggs.schedulemanager.ScheduleExpandableListAdapter;
import ca.ziggs.schedulemanager.ScheduleHeaderAdapter;


/**
 * Created by Robby Sharma on 6/8/2017.
 */

public class ScheduleFragment extends Fragment {

    //private ListView lvItems;
    private ExpandableListView lvItems;
    private ItemListAdapter adapter;
    private ScheduleHeaderAdapter adapter2;
    private ScheduleExpandableListAdapter adapter3;
    private ListView lvHeaders;
    //private ScheduleHeaderAdapter adapter;
    private List<Item> mItemList;
    private List<Item> mItemList2;
    private List<List<Item>> mAllItemList;
    private List<SchedHeader> mHeaderList;
    private List<String> mHeaders;
    private HashMap<String, List<Item>> mSchedule;
    private List<String> mStartDates;
    private List<String> mEndDates;
    private DBHandler db;

    public ScheduleFragment(){

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        db = new DBHandler(getContext());
        LinearLayout noShiftsLayout = (LinearLayout) view.findViewById(R.id.layout_noShiftsDialog);
        Button btnNewShifts = (Button)view.findViewById(R.id.btnScheduleShift);
        DBHandler db = new DBHandler(view.getContext());
        //lvItems = (ListView) view.findViewById(R.id.listview_schedule);
        lvItems = (ExpandableListView)view.findViewById(R.id.listview_schedule);
        lvHeaders = (ListView)view.findViewById(R.id.listview_schedule);
        mItemList = new ArrayList<>();
        mItemList2 = new ArrayList<>();
        mAllItemList = new ArrayList<>();
        mHeaderList = new ArrayList<>();
        //db.truncateShiftsTable();
        //List<JobEntry> shiftList = db.getAllShifts("betweenStart=2017-07-07End=2017-07-16");
        List<JobEntry> shiftList = db.getAllShifts("now");
        if(shiftList.isEmpty()){
            noShiftsLayout.setVisibility(View.VISIBLE);
        }else{
            noShiftsLayout.setVisibility(View.GONE);
        }

        btnNewShifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getContext(), AddShiftActivity.class);
                getActivity().startActivity(mIntent);
            }
        });

        lvItems.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getContext(),mSchedule.get(mHeaders.get(groupPosition)).get(childPosition).getFormattedTime(),Toast.LENGTH_LONG).show();
                return false;
            }
        });

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

        String lastShiftDate;
        int numOfShifts = shiftList.size();
        if(!shiftList.isEmpty()) {
            noShiftsLayout.setVisibility(View.GONE);
            lastShiftDate = shiftList.get(numOfShifts - 1).getDate();
            SimpleDateFormat sdfShiftDate = new SimpleDateFormat("yyyy-MM-dd");
            Date shiftDate;
            try {
                shiftDate = sdfShiftDate.parse(lastShiftDate);
            } catch (ParseException e) {
                shiftDate = new Date();
            }
            Date someOtherDate = new Date();
            Date date = new Date();
            cal.setTime(date);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - cal.getFirstDayOfWeek();
            int headerID = 0;
            cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
            while (shiftDate.after(someOtherDate)) {

                Date weekStart = cal.getTime();
                if (weekStart.after(date)) {
                    cal.add(Calendar.DAY_OF_MONTH, -7);
                    weekStart = cal.getTime();
                }
                cal.add(Calendar.DAY_OF_MONTH, 6);
                Date weekEnd = cal.getTime();

                SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd");
                String textWeekStart = sdf5.format(weekStart);
                String textWeekEnd = sdf5.format(weekEnd);
                someOtherDate = cal.getTime();
                cal.add(Calendar.DAY_OF_MONTH, 8);

                //Make ItemListAdapter Here For Each Week.

                mHeaderList.add(new SchedHeader(headerID, textWeekStart, textWeekEnd, textWeekStart + " to " + textWeekEnd));
                headerID += 1;

                //Toast.makeText(getContext(),textWeekStart + " to " + textWeekEnd,Toast.LENGTH_SHORT).show();
            }

            //cal.setFirstDayOfWeek(Calendar.SATURDAY);


//        for (JobEntry shift : shiftList){
//            try{
//                final SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
//                final SimpleDateFormat sdf4 = new SimpleDateFormat("kk");
//                final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd kk:mm");
//                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
//                //Date shiftEnd = new Date();
//                Date now = new Date();
//                Date jobDate = sdf3.parse(shift.getDate());
//                String nowDateString = sdf3.format(now);
//                String jobDateString = sdf3.format(jobDate);
//                Date strictShiftEndTime = sdf.parse(shift.getEndTime());
//                String stringShiftEndTime = sdf4.format(strictShiftEndTime);
//                Date shiftEnd;
//                if(nowDateString.equals(jobDateString) && (Integer.valueOf(stringShiftEndTime) < 12)){
//                    Calendar c = Calendar.getInstance();
//                    c.setTime(jobDate);
//                    c.add(Calendar.DATE,1);
//                    String modifiedDate = sdf3.format(c.getTime());
//                    shiftEnd = sdf2.parse(modifiedDate + " " + shift.getEndTime());
//                }else{
//                    shiftEnd = sdf2.parse(shift.getDate() + " " + shift.getEndTime());
//                }
//                //Toast.makeText(getContext(),"" + stringShiftEndTime,Toast.LENGTH_LONG).show();
//
//                final Date dateObject = sdf.parse(shift.getStartTime());
//                int difference = now.compareTo(shiftEnd);
//                //Toast.makeText(getContext(),"" + nowDateString+" hi " + jobDateString,Toast.LENGTH_LONG).show();
//                boolean isTimeValid = now.after(shiftEnd);
//                //Toast.makeText(getContext(),shiftEnd.toString(),Toast.LENGTH_SHORT).show();
//                //final Date dateObject2 = sdf.parse(shift.getEndTime());
//                String shiftTime = new SimpleDateFormat("hh:mm aa").format(dateObject);//shiftTime.setText(new SimpleDateFormat("hh:mm aa").format(dateObject));
//                //String shiftEndTime = new SimpleDateFormat("HH:mm").format(dateObject2);
//                //if(!isTimeValid){
//               if(difference < 0) {
//
//                   SimpleDateFormat sdfGetDate = new SimpleDateFormat("yyyy-MM-dd");
//                   Date getDate;
//                   String displayDate;
//                   try{
//                       getDate = sdfGetDate.parse(shift.getDate());
//                   }catch (ParseException e){
//                       getDate = new Date();
//                   }
//
//                   if(new SimpleDateFormat("yyyy-MM-dd").format(now).equals(shift.getDate())){
//                       displayDate = "Today (" + new SimpleDateFormat("dd MMMM, yyyy").format(getDate)+")";
//                   }else{
//                       displayDate = shift.getFormattedDate();
//                   }
//
//                    mItemList.add(new Item(shift.getId(), shift.getLocation(), displayDate, shiftTime, shift.getDuration(), shift.getLocation().substring(0, 1), shift.getFormattedTime(), shift.getDateTime()));
//                }
//            }catch (ParseException e){
//                // Do Jack
//            }
//           }

//        if(mItemList.isEmpty()){
//            noShiftsLayout.setVisibility(View.VISIBLE);
//        }else{
//            noShiftsLayout.setVisibility(View.GONE);
//        }

//        mItemList.add(new Item(1,"Walmart","Monday, June 12th 2017","03:15 pm","5h 45m","W"));
//        mItemList.add(new Item(2,"Walmart","Wednesday June 14th 2017","12:00 pm","6h 45m","W"));
//        mItemList.add(new Item(3,"Walmart","Friday, June 16th 2017","11:00 am","8h 15m","W"));
//        mItemList.add(new Item(4,"Walmart","Tuesday, June 20th 2017","11:15 am","6h 45m","W"));
//        mItemList.add(new Item(5,"Walmart","Wednesday, June 21st 2017","11:00 am","8h 0m","W"));
//        mItemList.add(new Item(6,"Walmart","Thursday, June 22nd 2017","01:15 pm","4h 0m","W"));
//        mItemList.add(new Item(7,"Walmart","Friday, June 23rd 2017","11:30 am","6h 45m","W"));

            setData(shiftList);

            adapter2 = new ScheduleHeaderAdapter(getContext(), mHeaderList);
            adapter = new ItemListAdapter(getContext(), mItemList);
            adapter3 = new ScheduleExpandableListAdapter(getContext(), mHeaders, mSchedule);
            lvItems.setAdapter(adapter3);
            for (int i = 0; i < adapter3.getGroupCount(); i++) {
                lvItems.expandGroup(i);
            }
        }else{
            noShiftsLayout.setVisibility(View.VISIBLE);
        }
        //setListViewHeightBasedOnChildren(lvItems);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //DO SOMETHING HEHEHEHEH

                Toast.makeText(getContext(),mItemList.get(position).getFormattedTime(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getShiftForWeek(List<JobEntry> shiftList){

        mItemList = new ArrayList<>();
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
                }
            }catch (ParseException e){
                // Do Jack
            }
        }
    }

    private void setData(List<JobEntry> shiftList){
        mHeaders = new ArrayList<>();
        mSchedule = new HashMap<>();
        mStartDates= new ArrayList<>();
        mEndDates = new ArrayList<>();

        //Adding Headers.
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

        int numOfShifts = shiftList.size();
        String lastShiftDate = shiftList.get(numOfShifts-1).getDate();
        SimpleDateFormat sdfShiftDate = new SimpleDateFormat("yyyy-MM-dd");
        Date shiftDate;
        try{
            shiftDate = sdfShiftDate.parse(lastShiftDate);
        }catch (ParseException e){
            shiftDate = new Date();
        }
        Date someOtherDate = new Date();
        Date date = new Date();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - cal.getFirstDayOfWeek();
        int headerID = 0;
        cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
        while(shiftDate.after(someOtherDate)){

            Date weekStart = cal.getTime();
            if(weekStart.after(date)){
                cal.add(Calendar.DAY_OF_MONTH, -7);
                weekStart = cal.getTime();
            }
            cal.add(Calendar.DAY_OF_MONTH,6);
            Date weekEnd = cal.getTime();

            SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd");
            String textWeekStart = sdf5.format(weekStart);
            String textWeekEnd = sdf5.format(weekEnd);
            someOtherDate = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH,8);

            //Make ItemListAdapter Here For Each Week.
            if(headerID == 0){
                mHeaders.add("This Week");
            }else if(headerID == 1){
                mHeaders.add("Next Week");
            }else if(headerID >= 2){
                mHeaders.add("In " + headerID + " Weeks");
            }

            mStartDates.add(textWeekStart);
            mEndDates.add(textWeekEnd);
            //mHeaderList.add(new SchedHeader(headerID,textWeekStart,textWeekEnd,textWeekStart + " to " + textWeekEnd));
            headerID += 1;

            //Toast.makeText(getContext(),textWeekStart + " to " + textWeekEnd,Toast.LENGTH_SHORT).show();
            //mItemList.add(new Item(1,"wal","today","hi","bye","cye","h","hi"));


        }
        for(int x=0; x <= mHeaders.size()-1;x++) {
            List<JobEntry> listOfShiftsThisWeek = db.getAllShifts("betweenStart=" + mStartDates.get(x).toString() + "End=" + mEndDates.get(x).toString());
            getShiftForWeek(listOfShiftsThisWeek);
            mSchedule.put(mHeaders.get(x), mItemList);
            }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container,false);

        return rootView;
    }
}
