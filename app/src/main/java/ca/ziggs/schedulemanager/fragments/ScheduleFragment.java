package ca.ziggs.schedulemanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.ziggs.schedulemanager.AddShiftActivity;
import ca.ziggs.schedulemanager.DBHandler;
import ca.ziggs.schedulemanager.Item;
import ca.ziggs.schedulemanager.ItemListAdapter;
import ca.ziggs.schedulemanager.JobEntry;
import ca.ziggs.schedulemanager.R;

;

/**
 * Created by Robby Sharma on 6/8/2017.
 */

public class ScheduleFragment extends Fragment {

    private ListView lvItems;
    private ItemListAdapter adapter;
    private List<Item> mItemList;

    public ScheduleFragment(){

    }

    public void restartFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        LinearLayout noShiftsLayout = (LinearLayout) view.findViewById(R.id.layout_noShiftsDialog);
        Button btnNewShifts = (Button)view.findViewById(R.id.btnScheduleShift);
        DBHandler db = new DBHandler(view.getContext());
        lvItems = (ListView)view.findViewById(R.id.listview_schedule);
        mItemList = new ArrayList<>();
        //db.truncateShiftsTable();
        List<JobEntry> shiftList = db.getAllShifts();

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

        for (JobEntry shift : shiftList){
            try{
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObject = sdf.parse(shift.getStartTime());
                String shiftTime = new SimpleDateFormat("hh:mm aa").format(dateObject);//shiftTime.setText(new SimpleDateFormat("hh:mm aa").format(dateObject));
                mItemList.add(new Item(shift.getId(),shift.getLocation(),shift.getFormattedDate(),shiftTime,shift.getDuration(),shift.getLocation().substring(0,1),shift.getFormattedTime(),shift.getDateTime()));
            }catch (ParseException e){
                // Do Jack
            }
           }

//        mItemList.add(new Item(1,"Walmart","Monday, June 12th 2017","03:15 pm","5h 45m","W"));
//        mItemList.add(new Item(2,"Walmart","Wednesday June 14th 2017","12:00 pm","6h 45m","W"));
//        mItemList.add(new Item(3,"Walmart","Friday, June 16th 2017","11:00 am","8h 15m","W"));
//        mItemList.add(new Item(4,"Walmart","Tuesday, June 20th 2017","11:15 am","6h 45m","W"));
//        mItemList.add(new Item(5,"Walmart","Wednesday, June 21st 2017","11:00 am","8h 0m","W"));
//        mItemList.add(new Item(6,"Walmart","Thursday, June 22nd 2017","01:15 pm","4h 0m","W"));
//        mItemList.add(new Item(7,"Walmart","Friday, June 23rd 2017","11:30 am","6h 45m","W"));


        adapter = new ItemListAdapter(getContext(),mItemList);
        lvItems.setAdapter(adapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //DO SOMETHING HEHEHEHEH

                Toast.makeText(getContext(),mItemList.get(position).getFormattedTime(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container,false);

        return rootView;
    }
}