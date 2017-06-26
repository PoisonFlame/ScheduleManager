package ca.ziggs.schedulemanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.ziggs.schedulemanager.ActivityPaycheckSettings;
import ca.ziggs.schedulemanager.AddShiftActivity;
import ca.ziggs.schedulemanager.R;

/**
 * Created by Robby Sharma on 6/8/2017.
 */

public class SettingsFragment extends Fragment {

    public SettingsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container,false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       Button btnAddShift = (Button)view.findViewById(R.id.btnAddShift);
       Button btnPaycheckOptions = (Button)view.findViewById(R.id.btnOpenPaycheckSettings);

        btnAddShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddShiftActivity.class);
                startActivity(i);
            }
        });

        btnPaycheckOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ActivityPaycheckSettings.class);
                startActivity(i);
            }
        });

    }
}
