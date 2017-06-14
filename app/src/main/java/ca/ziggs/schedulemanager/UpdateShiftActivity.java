package ca.ziggs.schedulemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Robby Sharma on 6/12/2017.
 */

public class UpdateShiftActivity extends AppCompatActivity {

    EditText editTitle,editName,editDate,editTime,editDuration,editBreaks;
    Button btnCancel,btnUpdate;
    String startTime,endTime;
    DBHandler db = new DBHandler(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shift);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize all TextBoxes and Buttons
        editTitle = (EditText)findViewById(R.id.editJobTitleUpdate);
        editName = (EditText)findViewById(R.id.editWorkplaceNameUpdate);
        editDate = (EditText)findViewById(R.id.editShiftDateUpdate);
        editTime = (EditText)findViewById(R.id.editShiftTimeUpdate);
        editDuration = (EditText)findViewById(R.id.editDurationUpdate);
        editBreaks = (EditText)findViewById(R.id.editBreaksUpdate);
        btnCancel = (Button)findViewById(R.id.btnCancelUpdate);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean updateCheck  = db.updateEntry(0,editTitle.getText().toString(),editName.getText().toString(),editDuration.getText().toString(),startTime,endTime,editBreaks.getText().toString(),editDate.getText().toString(),editDate.getText().toString(),editTime.getText().toString(),editTime.getText().toString());

                if(updateCheck == true){
                    Toast.makeText(getApplicationContext(),"Shift Updated",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Error Occurred. Make sure everything is filled out.",Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}
