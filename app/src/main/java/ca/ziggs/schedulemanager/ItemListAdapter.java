package ca.ziggs.schedulemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

import ca.ziggs.schedulemanager.fragments.ScheduleFragment;

/**
 * Created by Robby Sharma on 6/10/2017.
 */

public class ItemListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Item> mItemList;
    DBHandler db;

    public ItemListAdapter(Context mContext, List<Item> mItemList) {
        this.mContext = mContext;
        this.mItemList = mItemList;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position){
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.listview_schedule_item, null);
        TextView textName = (TextView)v.findViewById(R.id.textTitle);
        TextView textDuration = (TextView)v.findViewById(R.id.textDuration);
        TextView textTime = (TextView)v.findViewById(R.id.textTime);
        TextView textDate = (TextView)v.findViewById(R.id.textDateOfShift);
        ImageView imgIcon = (ImageView)v.findViewById(R.id.imgIcon);
        TextDrawable drawable = TextDrawable.builder().buildRound(mItemList.get(i).getIconURL(), Color.DKGRAY);
        ImageView imgMoreOptions = (ImageView)v.findViewById(R.id.btnMore);
        db = new DBHandler(mContext);



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
                                        db.deleteEntry(mItemList.get(i).getId());
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
                                mIntent.putExtra("contentID",mItemList.get(i).getId());
                                //mIntent.putExtra("contentID",1212);
                                mContext.startActivity(mIntent);
                                break;
                            default:
                                Toast.makeText(mContext, "You selected the action : " + item.getTitle() +" on ID: " + mItemList.get(i).getId(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });




        textName.setText(mItemList.get(i).getName());
        textDuration.setText(mItemList.get(i).getDuration());
        textTime.setText(mItemList.get(i).getTime());
        imgIcon.setImageDrawable(drawable);




        textDate.setText(mItemList.get(i).getDate());
        //textDate.setText("hi");

        v.setTag(mItemList.get(i).getId());
        return v;
    }
}
