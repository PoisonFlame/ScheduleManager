package ca.ziggs.schedulemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaycheckDetailsActivity extends AppCompatActivity {

    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paycheck_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        db = new DBHandler(getApplicationContext());
        //General
        TextView textEmployer = (TextView)findViewById(R.id.textPaidBy);
        TextView textPayday = (TextView)findViewById(R.id.textPayday);
        //Regular Earnings
        TextView textRegularHours = (TextView)findViewById(R.id.textHours);
        TextView textRegularRate = (TextView)findViewById(R.id.textRate);
        TextView textRegularSalaryThisPeriod = (TextView)findViewById(R.id.textThisPeriod);
        TextView textRegularYTD = (TextView)findViewById(R.id.textYTD);
        //Overtime Earnings
        TextView textOvertimeHours = (TextView)findViewById(R.id.textHoursOvertime);
        TextView textOvertimeRate = (TextView)findViewById(R.id.textRateOvertime);
        TextView textOvertimeSalaryThisPeriod = (TextView)findViewById(R.id.textThisPeriodOvertime);
        TextView textOvertimeYTD = (TextView)findViewById(R.id.textYTDOvertime);
        //Stat Earnings
        TextView textStatHourts = (TextView)findViewById(R.id.textHoursStat);
        TextView textStatRate = (TextView)findViewById(R.id.textRateStat);
        TextView textStatSalaryThisPeriod = (TextView)findViewById(R.id.textThisPeriodStat);
        TextView textStatYTD = (TextView)findViewById(R.id.textYTDStat);
        //Gross Pay
        TextView textGrossPayThisPeriod = (TextView)findViewById(R.id.textGrossPayThisPeriod);
        TextView textGrossPayYTD = (TextView)findViewById(R.id.textYTDGrossPay);
        // Tax Deductions
        TextView textFederalTaxThisPeriod = (TextView)findViewById(R.id.textFederalTaxThisPeriod);
        TextView textFederalTaxYTD = (TextView)findViewById(R.id.textFederalTaxYTD);
        TextView textProvincialTaxThisPeriod = (TextView)findViewById(R.id.textProvincialTaxesThisPeriod);
        TextView textProvincialTaxYTD = (TextView)findViewById(R.id.textProvincialTaxesYTD);
        TextView textCPPThisPeriod = (TextView)findViewById(R.id.textCPPThisPeriod);
        TextView textCPPYTD = (TextView)findViewById(R.id.textCPPYTD);
        TextView textEIThisPeriod = (TextView)findViewById(R.id.textEIThisPeriod);
        TextView textEIYTD = (TextView)findViewById(R.id.textEIYTD);
        TextView textTotalTaxThisPeriod = (TextView)findViewById(R.id.textTotalTaxesThisPeriod);
        TextView textTotalTaxYTD = (TextView)findViewById(R.id.textTotalTaxYTD);
        //Net Pay
        TextView textNetPayThisPeriod = (TextView)findViewById(R.id.textNetPayThisPeriod);
        TextView textNetPayYTD = (TextView)findViewById(R.id.textYTDNetPay);
        //Close Button
        Button btnClose = (Button)findViewById(R.id.btnClose);

        LinearLayout layoutTaxExclusions = (LinearLayout)findViewById(R.id.layoutTaxDeductions);


        //Convert Payday to SQLite date format
        SimpleDateFormat sdf = new SimpleDateFormat("EE, dd MMM yyyy");
        Date payDayDate;
        try{
            payDayDate = sdf.parse(getIntent().getExtras().getString("payDay"));
        }catch (ParseException e){
            payDayDate = new Date();
        }
        String sqlitePayday = new SimpleDateFormat("yyy-MM-dd").format(payDayDate);


        String salary = getIntent().getExtras().getString("salaryDate");
        setTitle(getIntent().getExtras().getString("payPeriod"));
        textEmployer.setText(getIntent().getExtras().getString("employer"));
        textPayday.setText(getIntent().getExtras().getString("payDay"));

        String[] payPeriod = getIntent().getExtras().getString("payPeriod").split("-");
        String beginPeriod = payPeriod[0].trim().replace("/","-");
        String endPeriod = payPeriod[1].trim().replace("/","-");

        if(db.checkPaydayExists(sqlitePayday)){
            //Regular Earnings
            textRegularHours.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_REGULAR_HOURS));
            textRegularRate.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_REGULAR_PAYRATE));
            textRegularSalaryThisPeriod.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_REGULAR_EARNINGS_THIS_PERIOD));
            textRegularYTD.setText(String.format("%.2f",db.getPaycheckYTD(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE),endPeriod,"regular") + Double.valueOf(textRegularSalaryThisPeriod.getText().toString())));
            //Overtime Earnings
            textOvertimeHours.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_OVERTIME_HOURS));
            textOvertimeRate.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_OVERTIME_PAYRATE));
            textOvertimeSalaryThisPeriod.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_OVERTIME_EARNINGS_THIS_PERIOD));
            textOvertimeYTD.setText(String.format("%.2f",db.getPaycheckYTD(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE),endPeriod,"overtime") + Double.valueOf(textOvertimeSalaryThisPeriod.getText().toString())));
            //Stat Earnings
            textStatHourts.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_STAT_HOURS));
            textStatRate.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_STAT_PAYRATE));
            textStatSalaryThisPeriod.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_STAT_EARNINGS_THIS_PERIOD));
            textStatYTD.setText(String.format("%.2f",db.getPaycheckYTD(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE),endPeriod,"stat") + Double.valueOf(textStatSalaryThisPeriod.getText().toString())));
            //Gross Pay Earnings
            textGrossPayThisPeriod.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_GROSSPAY_THIS_PERIOD));
            textGrossPayYTD.setText(String.format("%.2f",db.getPaycheckYTD(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE),endPeriod,"grosspay") + Double.valueOf(textGrossPayThisPeriod.getText().toString())));
            //Tax Deductions
            textFederalTaxThisPeriod.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_FEDERAL_TAXES_THIS_PERIOD));
            textFederalTaxYTD.setText(String.format("%.2f",db.getPaycheckYTD(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE),endPeriod,"fed") + Double.valueOf(textFederalTaxThisPeriod.getText().toString())));
            textProvincialTaxThisPeriod.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_PROVINCIAL_TAXES_THIS_PERIOD));
            textProvincialTaxYTD.setText(String.format("%.2f",db.getPaycheckYTD(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE),endPeriod,"pro") + Double.valueOf(textProvincialTaxThisPeriod.getText().toString())));
            textCPPThisPeriod.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_CPP_THIS_PERIOD));
            textCPPYTD.setText(String.format("%.2f",db.getPaycheckYTD(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE),endPeriod,"cpp") + Double.valueOf(textCPPThisPeriod.getText().toString())));
           textEIThisPeriod.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_EI_THIS_PERIOD));
            textEIYTD.setText(String.format("%.2f",db.getPaycheckYTD(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE),endPeriod,"ei") + Double.valueOf(textEIThisPeriod.getText().toString())));
            textTotalTaxThisPeriod.setText(String.format("%.2f",(Double.valueOf(textFederalTaxThisPeriod.getText().toString()))+Double.valueOf(textProvincialTaxThisPeriod.getText().toString())+Double.valueOf(textCPPThisPeriod.getText().toString())+Double.valueOf(textEIThisPeriod.getText().toString())));
            textTotalTaxYTD.setText(String.format("%.2f",(Double.valueOf(textFederalTaxYTD.getText().toString()))+Double.valueOf(textProvincialTaxYTD.getText().toString())+Double.valueOf(textCPPYTD.getText().toString())+Double.valueOf(textEIYTD.getText().toString())));
            //Net Pay
            textNetPayThisPeriod.setText(db.getPaycheckDataColumn(sqlitePayday,db.KEY_NETPAY_THIS_PERIOD));
            textNetPayYTD.setText(String.format("%.2f",db.getPaycheckYTD(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE),endPeriod,"netpay") + Double.valueOf(textNetPayThisPeriod.getText().toString().replace("$",""))));
            //textNetPayYTD.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE), endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "default"));
            //Toast.makeText(getApplicationContext(),"Straight from Da Base",Toast.LENGTH_SHORT).show();
        }else {

            //Regular Earnings
            textRegularHours.setText(db.getWeekWorkingHours(beginPeriod, endPeriod));
            textRegularRate.setText(db.getPaycheckColumn(db.KEY_SALARY));
            textRegularSalaryThisPeriod.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "grosspay"));
            textRegularYTD.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE), endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "grosspay"));
            //Overtime Earnings
            //Stat Earnings
            //Gross Pay Earnings
            textGrossPayThisPeriod.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "grosspay"));
            textGrossPayYTD.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE), endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "grosspay"));
            //Tax Deductions
            textFederalTaxThisPeriod.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "fed"));
            textFederalTaxYTD.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE), endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "fed"));
            textProvincialTaxThisPeriod.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "pro"));
            textProvincialTaxYTD.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE), endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "pro"));
            textCPPThisPeriod.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "cpp"));
            textCPPYTD.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE), endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "cpp"));
            textEIThisPeriod.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "ei"));
            textEIYTD.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE), endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "ei"));
            //Net Pay
            textNetPayThisPeriod.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(beginPeriod, endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "default"));
            textNetPayYTD.setText(db.getMoneyEarned(Double.valueOf(db.getWeekWorkingHours(db.getPaycheckColumn(db.KEY_PAYCHECK_DATE), endPeriod)), Double.valueOf(db.getPaycheckColumn(db.KEY_SALARY)), "default"));

        }
        db.setPaycheckInDB(textEmployer.getText().toString(),sqlitePayday,textRegularRate.getText().toString(),textRegularHours.getText().toString(),textRegularSalaryThisPeriod.getText().toString(),textOvertimeRate.getText().toString(),textOvertimeHours.getText().toString(),textOvertimeSalaryThisPeriod.getText().toString(),textStatRate.getText().toString(),textStatHourts.getText().toString(),textStatSalaryThisPeriod.getText().toString(),textGrossPayThisPeriod.getText().toString(),textFederalTaxThisPeriod.getText().toString(),textProvincialTaxThisPeriod.getText().toString(),textCPPThisPeriod.getText().toString(),textEIThisPeriod.getText().toString(),textNetPayThisPeriod.getText().toString());

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if(db.getPaycheckColumn(db.KEY_USE_CANADIAN_TAXES).equals("false")){
            layoutTaxExclusions.setVisibility(View.GONE);
        }else{
            layoutTaxExclusions.setVisibility(View.VISIBLE);
        }

    }

}
