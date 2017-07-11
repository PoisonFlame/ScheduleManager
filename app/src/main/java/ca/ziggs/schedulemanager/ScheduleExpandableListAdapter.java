package ca.ziggs.schedulemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.HashMap;
import java.util.List;

import ca.ziggs.schedulemanager.fragments.ScheduleFragment;

/**
 * Created by Robby Sharma on 7/7/2017.
 */

public class ScheduleExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mScheduleHeaders;
    private HashMap<String, List<Item>> mScheduleList;
    private DBHandler db;

    public ScheduleExpandableListAdapter(Context mContext, List<String> mScheduleHeaders, HashMap<String, List<Item>> mScheduleList) {
        this.mContext = mContext;
        this.mScheduleHeaders = mScheduleHeaders;
        this.mScheduleList = mScheduleList;
    }

    @Override
    public int getGroupCount() {
        return mScheduleHeaders.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mScheduleList.get(mScheduleHeaders.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return mScheduleHeaders.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mScheduleList.get(mScheduleHeaders.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String) getGroup(i);
        LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = infalInflater.inflate(R.layout.listview_schedule_headers, null);
        }

        ImageView imgArrowUpOrDown = (ImageView)view.findViewById(R.id.imgArrowUpDown);
        TextView lblListHeader = (TextView) view.findViewById(R.id.textScheduleHeaderDate);

        if(b){
            imgArrowUpOrDown.setImageResource(R.drawable.ic_action_arrow_down);
        }else{
            imgArrowUpOrDown.setImageResource(R.drawable.ic_action_arrow_up);
        }

        if (getChildrenCount(i)==0){
            imgArrowUpOrDown.setMaxHeight(0);
        }


        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return view;

    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.listview_schedule_item, null);
        }

//        LinearLayout layout_NoshiftThisWeek = (LinearLayout)view.findViewById(R.id.layout_NoshiftThisWeek);
//
//        if(mScheduleList.get(mScheduleHeaders.get(i)).isEmpty()){
//            layout_NoshiftThisWeek.setVisibility(View.VISIBLE);
//        }else{
//            layout_NoshiftThisWeek.setVisibility(View.GONE);
//        }
//
//        Toast.makeText(mContext,mScheduleHeaders.get(i) + " " + mScheduleList.get(mScheduleHeaders.get(i)).size(),Toast.LENGTH_SHORT).show();

        TextView textName = (TextView)view.findViewById(R.id.textTitle);
        TextView textDuration = (TextView)view.findViewById(R.id.textDuration);
        TextView textTime = (TextView)view.findViewById(R.id.textTime);
        TextView textDate = (TextView)view.findViewById(R.id.textDateOfShift);
        ImageView imgIcon = (ImageView)view.findViewById(R.id.imgIcon);
        TextDrawable drawable = TextDrawable.builder().buildRound(mScheduleList.get(mScheduleHeaders.get(i)).get(i1).getIconURL(), Color.DKGRAY);
        ImageView imgMoreOptions = (ImageView)view.findViewById(R.id.btnMore);
        db = new DBHandler(mContext);

        textName.setText(mScheduleList.get(mScheduleHeaders.get(i)).get(i1).getName());
        textDuration.setText(mScheduleList.get(mScheduleHeaders.get(i)).get(i1).getDuration());
        textTime.setText(mScheduleList.get(mScheduleHeaders.get(i)).get(i1).getTime());
        imgIcon.setImageDrawable(drawable);
        textDate.setText(mScheduleList.get(mScheduleHeaders.get(i)).get(i1).getDate());
        view.setTag(mScheduleList.get(mScheduleHeaders.get(i)).get(i1).getId());


        imgMoreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mContext, view);
                popup.getMenuInflater().inflate(R.menu.schedule_options_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.remove:
                                //Toast.makeText(mContext,"You removed" + mItemList.get(i).getId(),Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertDial = new AlertDialog.Builder(mContext);
                                alertDial.setMessage("Are you sure you want to remove this shift?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int ila) {
                                        //Yes.
                                        //Toast.makeText(mContext,"ID: "+mItemList.get(i).getId(),Toast.LENGTH_SHORT).show();
                                        db.deleteEntry(mScheduleList.get(mScheduleHeaders.get(i)).get(i1).getId());
                                        //FragmentManager manager = ((Activity)mContext).getSupportFragmentManager();
                                        ScheduleFragment fragment = new ScheduleFragment();
                                        FragmentTransaction fragmentTransaction = ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.content_frame,fragment,"fragment_schedule");
                                        fragmentTransaction.commit();
                                        //ScheduleFragment.restartFragment();
                                    }
                                })
                                        .setNegativeButton("No",new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //No.
                                                dialogInterface.cancel();
                                            }
                                        });

                                AlertDialog alert = alertDial.create();
                                alert.setTitle("Confirm");
                                alert.show();
                                break;
                            case R.id.edit:
                                Intent mIntent = new Intent(mContext,UpdateShiftActivity.class);
                                mIntent.putExtra("contentID",mScheduleList.get(mScheduleHeaders.get(i)).get(i1).getId());
                                //mIntent.putExtra("contentID",1212);
                                mContext.startActivity(mIntent);
                                break;
                            default:
                                Toast.makeText(mContext, "You selected the action : " + item.getTitle() +" on ID: " + mScheduleList.get(mScheduleHeaders.get(i)).get(i1).getId(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
