package ca.ziggs.schedulemanager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Robby Sharma on 6/21/2017.
 */

public class ActivityPaycheckSettings extends AppCompatActivity {

    DBHandler db;
    Calendar calendar;
    EditText editFirstPaycheckDate;
    String rawDate;
    String formattedDate;
    String optionCadTax,optionEIExempt,optionCPPExempt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paycheck_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final CheckBox checkUseCanadianTaxes = (CheckBox)findViewById(R.id.checkUseCATax);
        final TextView textProvince = (TextView)findViewById(R.id.textProvince);
        final Spinner spinProvince = (Spinner)findViewById(R.id.spinProvince);
        final TextView textTaxExemptions = (TextView)findViewById(R.id.textTaxExemptions);
        final CheckBox checkCPPExclude = (CheckBox)findViewById(R.id.checkCPPExclude);
        final CheckBox checkEIExclude = (CheckBox)findViewById(R.id.checkEIExclude);
        final Button btnCancel = (Button)findViewById(R.id.btnCancel);
        final Button btnUpdate = (Button)findViewById(R.id.btnSubmit);
        final EditText editSalary = (EditText)findViewById(R.id.editSalary);
        final Spinner spinSalaryType = (Spinner)findViewById(R.id.spinSalaryType);
        final LinearLayout useCADTaxesLayout = (LinearLayout)findViewById(R.id.taxOptionsLayout);
         editFirstPaycheckDate = (EditText)findViewById(R.id.editFirstPaycheckDate);
        final Spinner spinPayPeriod  = (Spinner)findViewById(R.id.spinPayPeriod);
        final Spinner spinWeekStartDay = (Spinner)findViewById(R.id.spinWeekStartDay);
        db = new DBHandler(getApplicationContext());

        calendar = Calendar.getInstance();
        editFirstPaycheckDate.setKeyListener(null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //String[] SalaryTypes = new String[]{"Hour", "Day", "Week", "Month", "Year"};
        String[] SalaryTypes = new String[]{"Hour"};
        ArrayAdapter<String> adapterSalaryType = new ArrayAdapter<>(this, R.layout.spinner_item, SalaryTypes);
        adapterSalaryType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSalaryType.setAdapter(adapterSalaryType);

        String[] PayPeriods = new String[]{"Bi-Weekly", "Weekly", "Monthly", "Quarterly", "Yearly", "Semi-Monthly"};
        ArrayAdapter<String> adapterPayPeriod = new ArrayAdapter<>(this, R.layout.spinner_item, PayPeriods);
        adapterPayPeriod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPayPeriod.setAdapter(adapterPayPeriod);

        String[] WeekDays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapterWeekStartDay = new ArrayAdapter<>(this, R.layout.spinner_item, WeekDays);
        adapterWeekStartDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinWeekStartDay.setAdapter(adapterWeekStartDay);

        String[] CanadianProvinces = new String[]{"AB", "BC", "MB", "NB", "NL", "NS", "NT", "NU", "ON", "PE", "QC", "SK", "YT"};
        ArrayAdapter<String> adapterCADProvinces = new ArrayAdapter<>(this, R.layout.spinner_item, CanadianProvinces);
        adapterCADProvinces.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinProvince.setAdapter(adapterCADProvinces);


        editFirstPaycheckDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(ActivityPaycheckSettings.this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setMessage("Select First Salary Date");
                dialog.show();
            }
        });

        checkUseCanadianTaxes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkUseCanadianTaxes.isChecked()){
                    useCADTaxesLayout.setVisibility(View.VISIBLE);
                    optionCadTax = "true";
                }else{
                    useCADTaxesLayout.setVisibility(View.GONE);
                    optionCadTax = "false";
                }
            }
        });

        checkCPPExclude.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkCPPExclude.isChecked()){
                    optionCPPExempt = "true";
                }else{
                    optionCPPExempt = "false";
                }
            }
        });

        checkEIExclude.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkEIExclude.isChecked()){
                    optionEIExempt = "true";
                }else{
                    optionEIExempt = "false";
                }
            }
        });


        //Toast.makeText(getApplicationContext(),db.getPaycheckColumn("salary"),Toast.LENGTH_SHORT).show();
        if(db.getPaycheckColumn("salary") != null){
            editSalary.setText(db.getPaycheckColumn("salary"));
        }

        if(db.getPaycheckColumn("salaryType") != null){
            int index = adapterSalaryType.getPosition(db.getPaycheckColumn("salaryType"));
            spinSalaryType.setSelection(index);
        }

        if(db.getPaycheckColumn("formattedPaycheckDate") != null){
            editFirstPaycheckDate.setText(db.getPaycheckColumn("formattedPaycheckDate"));
        }

        if(db.getPaycheckColumn("payPeriod") != null){
            int index = adapterPayPeriod.getPosition(db.getPaycheckColumn("payPeriod"));
            spinPayPeriod.setSelection(index);
        }

        if(db.getPaycheckColumn("weekStartDay") != null){
            int index = adapterWeekStartDay.getPosition(db.getPaycheckColumn("weekStartDay"));
            spinWeekStartDay.setSelection(index);
        }

        if(db.getPaycheckColumn("useCanadianTaxes").equals("true")){
            checkUseCanadianTaxes.setChecked(true);
            useCADTaxesLayout.setVisibility(View.VISIBLE);
            optionCadTax = "true";
        }else{
            checkUseCanadianTaxes.setChecked(false);
            useCADTaxesLayout.setVisibility(View.GONE);
            optionCadTax = "false";
        }

        if(db.getPaycheckColumn("province") != null){
            int index = adapterCADProvinces.getPosition(db.getPaycheckColumn("province"));
            spinProvince.setSelection(index);
        }

        if(db.getPaycheckColumn("exemptCPP").equals("true")){
            checkCPPExclude.setChecked(true);
            optionCPPExempt = "true";
        }else{
            checkCPPExclude.setChecked(false);
            optionCPPExempt = "false";
        }

        if(db.getPaycheckColumn("exemptEI").equals("true")){
            checkEIExclude.setChecked(true);
            optionEIExempt = "true";
        }else{
            checkEIExclude.setChecked(false);
            optionEIExempt = "false";
        }

        if(checkUseCanadianTaxes.isChecked()){
            textProvince.setEnabled(true);
            spinProvince.setEnabled(true);
            textTaxExemptions.setEnabled(true);
            checkCPPExclude.setEnabled(true);
            checkEIExclude.setEnabled(true);
        }else{
            textProvince.setEnabled(false);
            spinProvince.setEnabled(false);
            textTaxExemptions.setEnabled(false);
            checkCPPExclude.setEnabled(false);
            checkEIExclude.setEnabled(false);
        }

        if(db.getPaycheckColumn("paycheckDate") != null){
            rawDate = db.getPaycheckColumn("paycheckDate");
        }

        if(db.getPaycheckColumn("formattedPaycheckDate") != null){
            formattedDate = db.getPaycheckColumn("formattedPaycheckDate");
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editSalary.getText().toString().trim() == "" || spinSalaryType.getSelectedItem().toString().trim() == "" ||
                        editFirstPaycheckDate.getText().toString().trim() == "" || spinPayPeriod.getSelectedItem().toString().trim() == "" ||
                        spinWeekStartDay.getSelectedItem().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Please don't leave any fields empty.", Toast.LENGTH_LONG).show();
                } else {
                    // Add to DB
                    //rawDateAndStartTime = rawDate + " " + rawStartTimeFormatted + ":00";
                    //db.addNewEntry(new JobEntry(editTitle.getText().toString(), editName.getText().toString(), editDuration.getText().toString(), rawStartTime, rawEndTime, editBreaks.getText().toString(), rawDate, editDate.getText().toString(), shiftTime.getText().toString(), rawDateAndStartTime));
                    AlertDialog.Builder alertDial = new AlertDialog.Builder(view.getContext());
                    alertDial.setMessage("Update Paycheck Options?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Yes.
                              if(db.updatePaycheckSettings(String.format("%.2f",Double.valueOf(editSalary.getText().toString())),spinSalaryType.getSelectedItem().toString(),rawDate,spinPayPeriod.getSelectedItem().toString(),spinWeekStartDay.getSelectedItem().toString(),optionCadTax,spinProvince.getSelectedItem().toString(),optionCPPExempt,optionEIExempt,formattedDate)){
                                  finish();
                                  Intent mIntent = new Intent(getApplicationContext(),MainActivity.class);
                                  mIntent.putExtra("loadFragment","paycheckFragment");
                                  startActivity(mIntent);
                                  Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                              }else{
                                  dialogInterface.cancel();
                                  Toast.makeText(getApplicationContext(),"An Error Occurred. Try again.",Toast.LENGTH_LONG).show();
                              }
//                            Intent mIntent = getIntent();
//                            finish();
//                            startActivity(mIntent);
                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    //No.
//                                    finish();
//                                    Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
//                                    mIntent.putExtra("loadFragment", "scheduleFragment");
//                                    startActivity(mIntent);
                                    //ScheduleFragment fragment = new ScheduleFragment();
                                    //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    //fragmentTransaction.replace(R.id.content_frame,fragment,"fragment_schedule");
                                    //fragmentTransaction.commit();
                                }
                            });

                    AlertDialog alert = alertDial.create();
                    alert.setTitle("Confirm Changes");
                    alert.show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDial = new AlertDialog.Builder(view.getContext());
                alertDial.setMessage("Are you sure you want to cancel?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Yes.
                        finish();
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
                alert.setTitle("Are you sure?");
                alert.show();
            }
        });

        checkUseCanadianTaxes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkUseCanadianTaxes.isChecked()){
                    textProvince.setEnabled(true);
                    spinProvince.setEnabled(true);
                    textTaxExemptions.setEnabled(true);
                    checkCPPExclude.setEnabled(true);
                    checkEIExclude.setEnabled(true);
                }else{
                    textProvince.setEnabled(false);
                    spinProvince.setEnabled(false);
                    textTaxExemptions.setEnabled(false);
                    checkCPPExclude.setEnabled(false);
                    checkEIExclude.setEnabled(false);
                }
            }
        });


    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            String day_formatted, month_formatted;
            if(String.valueOf(day).endsWith("11")) {
                day_formatted = String.valueOf(day) + "th";
            }else if(String.valueOf(day).endsWith("12")) {
                day_formatted = String.valueOf(day) + "th";
            }else if(String.valueOf(day).endsWith("13")){
                day_formatted = String.valueOf(day) + "th";
            }else if(String.valueOf(day).endsWith("1")){
                day_formatted = String.valueOf(day) + "st";
            }else if(String.valueOf(day).endsWith("2")){
                day_formatted = String.valueOf(day) + "nd";
            }else if(String.valueOf(day).endsWith("3")){
                day_formatted = String.valueOf(day) + "rd";
            }else{
                day_formatted = String.valueOf(day) + "th";
            }
            month += 1;
            if(month == 1){
                month_formatted = "January";
            }else if(month == 2){
                month_formatted = "February";
            }else if(month == 3){
                month_formatted = "March";
            }else if(month == 4){
                month_formatted = "April";
            }else if(month == 5){
                month_formatted = "May";
            }else if(month == 6){
                month_formatted = "June";
            }else if(month == 7){
                month_formatted = "July";
            }else if(month == 8){
                month_formatted = "August";
            }else if(month == 9){
                month_formatted = "September";
            }else if(month == 10){
                month_formatted = "October";
            }else if(month == 11){
                month_formatted = "November";
            }else if(month == 12){
                month_formatted = "December";
            }else{
                month_formatted = "";
            }

            //calendar.set(year,month, day);
            calendar.set(year,month-1,day);

            //GregorianCalendar GregorianCal = new GregorianCalendar(year,month,day -2);
            //int dayOfWeek = GregorianCal.get(Calendar.DAY_OF_WEEK);
            String dayName;

            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            try{
                Date date = inFormat.parse(day+"-"+month+"-"+year);
                SimpleDateFormat outFormat  = new SimpleDateFormat("EEEE");
                dayName = outFormat.format(date);
            }catch (ParseException e){
                dayName = "";
            }

//            if(dayOfWeek == Calendar.MONDAY){
//                dayName = "Monday";
//            }else if(dayOfWeek == Calendar.TUESDAY){
//                dayName = "Tuesday";
//            }else if(dayOfWeek == Calendar.WEDNESDAY){
//                dayName = "Wednesday";
//            }else if (dayOfWeek == Calendar.THURSDAY){
//                dayName = "Thursday";
//            }else if (dayOfWeek == Calendar.FRIDAY){
//                dayName = "Friday";
//            }else if(dayOfWeek == Calendar.SATURDAY){
//                dayName = "Saturday";
//            }else if(dayOfWeek == Calendar.SUNDAY){
//                dayName = "Sunday";
//            }else{
//                dayName = "";
//            }


            editFirstPaycheckDate.setText(dayName +", " + day_formatted + " " + month_formatted + " " + year);
            formattedDate = dayName + ", " + day_formatted + " " + month_formatted + " " + year;
            //rawDate = day+"/"+month+"/"+year;
            try{
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                final Date dateObject = sdf.parse(year+"-"+(month)+"-"+day);
                String datess = new SimpleDateFormat("yyyy-mm-dd").format(dateObject);
                rawDate = datess;
                //Toast.makeText(getApplicationContext(),rawDate,Toast.LENGTH_LONG).show();
            }catch (ParseException e){
                // DO CRAP.
                rawDate = year+"-"+month+"-"+day;
            }

            //rawDate = year+"-"+month+"-"+day;
        }

    };

}
