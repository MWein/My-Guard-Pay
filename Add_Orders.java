package com.weinbergsoftware.www.myguardpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class Add_Orders extends AppCompatActivity {


    // TODO Get 'next' button on keyboard to jump to proper positions


    //***************** Variables **********************
    private int MinYear; // Minimum allowable year

    // User info
    private int Rank;
    private int TIS;

    // Modes
    private boolean SingleDateMode;
    //**************************************************


    //***************** UI Variables *******************
    private Spinner OrderTypeSpinner;

    // Display
    private TextView MUTACountView;
    private TextView DollarAmountView;
    private TextView WarningStatement;

    // Start Date
    private EditText StartDay;
    private Spinner StartMonth;
    private EditText StartYear;

    // End Date
    private EditText EndDay;
    private Spinner EndMonth;
    private EditText EndYear;

    private Button AddOrdersButton;
    //**************************************************



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__orders);

        // Load data from MainActivity
        Intent intent = getIntent();
        Rank = intent.getIntExtra(getString(R.string.saved_rank), 0);
        TIS = intent.getIntExtra(getString(R.string.saved_TIS), 0);

        // Assign UI Variables
        OrderTypeSpinner = (Spinner) findViewById(R.id.OrderType);
        MUTACountView = (TextView) findViewById(R.id.mutaCount);
        DollarAmountView = (TextView) findViewById(R.id.dollarAmount);
        WarningStatement = (TextView) findViewById(R.id.warningText);
        StartDay = (EditText) findViewById(R.id.startDay);
        StartMonth = (Spinner) findViewById(R.id.startMonth);
        StartYear = (EditText) findViewById(R.id.startYear);
        EndDay = (EditText) findViewById(R.id.endDay);
        EndMonth = (Spinner) findViewById(R.id.endMonth);
        EndYear = (EditText) findViewById(R.id.endYear);
        AddOrdersButton = (Button) findViewById(R.id.addButton);



        // Setup variables
        Calendar now = Calendar.getInstance();
        MinYear = now.get(Calendar.YEAR) - 50;


        // Setup UI Listeners
        OrderTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                determineSingleDateMode();
                calculateMUTAsAndPay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Intentionally Left Blank
            }
        });
        StartMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateFieldChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dateFieldChanged();
            }
        });
        EndMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateFieldChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dateFieldChanged();
            }
        });

        // Start date change listeners
        StartDay.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    dateFieldChanged();
                    return true;
                }
                return false;
            }
        });
        StartYear.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    dateFieldChanged();
                    return true;
                }
                return false;
            }
        });

        // End date change listeners
        EndDay.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    dateFieldChanged();
                    return true;
                }
                return false;
            }
        });
        EndYear.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    dateFieldChanged();
                    return true;
                }
                return false;
            }
        });


        // Populate OrderType Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.orders_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        OrderTypeSpinner.setAdapter(adapter);


        // Populate StartMonth and EndMonth
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.month_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StartMonth.setAdapter(adapter2);
        EndMonth.setAdapter(adapter2);



        // TODO Retrieve dates and order type if this is an edit call


        // Populate Start and End Date Fields with Current Date
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);

        StartDay.setText(String.format(Locale.US, "%d", day));
        StartMonth.setSelection(month);
        StartYear.setText(String.format(Locale.US, "%d", year));

        EndDay.setText(String.format(Locale.US, "%d", day));
        EndMonth.setSelection(month);
        EndYear.setText(String.format(Locale.US, "%d", year));


        // Set Modes
        determineSingleDateMode();
        WarningStatement.setVisibility(View.INVISIBLE);

        // Update MUTA and Pay Displays
        calculateMUTAsAndPay();



        // TODO Retrieve array dates
    }


    // Start date changed
    private void dateFieldChanged() {

        // Load variables
        int startDay = Integer.parseInt(StartDay.getText().toString());
        int startMonth = StartMonth.getSelectedItemPosition();
        int startYear = Integer.parseInt(StartYear.getText().toString());

        int endDay = Integer.parseInt(EndDay.getText().toString());
        int endMonth = EndMonth.getSelectedItemPosition();
        int endYear = Integer.parseInt(EndYear.getText().toString());


        // Check for valid year, fix if invalid
        if (startYear < MinYear) {
            startYear = MinYear;
            StartYear.setText(String.format(Locale.US, "%d", MinYear));
        }
        if (endYear < MinYear) {
            endYear = MinYear;
            EndYear.setText(String.format(Locale.US, "%d", MinYear));
        }


        // Check for valid day based on month, fix if invalid
        int validDays = RankAndTIS.daysInMonth(startMonth, startYear);
        if (startDay <= 0 || startDay > validDays) {
            if (startDay <= 0) startDay = 1;
            if (startDay > validDays) startDay = validDays;
            StartDay.setText(String.format(Locale.US, "%d", startDay));
        }
        validDays = RankAndTIS.daysInMonth(endMonth, endYear);
        if (endDay <= 0 || endDay > validDays) {
            if (endDay <= 0) endDay = 1;
            if (endDay > validDays) endDay = validDays;
            EndDay.setText(String.format(Locale.US, "%d", endDay));
        }


        // SingleDateMode
        if (SingleDateMode) {
            // Match end date fields with start date values... only one date possible with SingleDateMode
            EndDay.setText(StartDay.getText());
            EndMonth.setSelection(StartMonth.getSelectedItemPosition());
            EndYear.setText(StartYear.getText());
        } else {

            // If end date is before start date, this is where it's fixed

            // Since the end year should always be equal to or greater than start date, look that over first
            if (startYear > endYear) {
                endYear = startYear;
                EndYear.setText(String.format(Locale.US, "%d", startYear));
            }

            // If the years are the same, the months need to be checked
            if (startYear == endYear && startMonth > endMonth) {
                endMonth = startMonth;
                EndMonth.setSelection(startMonth);
            }

            // If the months and years are the same, the days need to be checked
            if (startYear == endYear && startMonth == endMonth && startDay > endDay) {
                EndDay.setText(String.format(Locale.US, "%d", startDay));
            }


        }


        calculateMUTAsAndPay();
    }






    // Update MUTAs and DollarAmount based on dates selected and pay per MUTA
    private void calculateMUTAsAndPay() {

        // Load variables
        int[] startDate = {Integer.parseInt(StartDay.getText().toString()), StartMonth.getSelectedItemPosition(), Integer.parseInt(StartYear.getText().toString())};
        int[] endDate = {Integer.parseInt(EndDay.getText().toString()), EndMonth.getSelectedItemPosition(), Integer.parseInt(EndYear.getText().toString())};

        // Get MUTA count
        int MUTACount = RankAndTIS.getMUTAsFor(OrderTypeSpinner.getSelectedItemPosition(), startDate, endDate);


        // Update MUTACountView
        String mutaStr = getString(R.string.muta_string_plural);
        if (MUTACount == 1) mutaStr = getString(R.string.muta_string_singular);
        MUTACountView.setText(String.format(Locale.US, "%d %s", MUTACount, mutaStr));

        // Retrieve Pay per MUTA from RankAndTIS
        double payPerMuta = RankAndTIS.getPayPerMuta(Rank, TIS);

        // Update Dollar Display with above amount, multiply pay rate by MUTACount
        DollarAmountView.setText(String.format(Locale.US, "$%.2f", (MUTACount * payPerMuta)));
    }


    // Determine SingleDateMode
    // If MFH Duty is selected, set mode to true
    // Otherwise, set mode to false
    private void determineSingleDateMode() {
        if (OrderTypeSpinner.getSelectedItemPosition() == 0) {
            // SingleDateMode on
            SingleDateMode = true;
            EndDay.setEnabled(false);
            EndMonth.setEnabled(false);
            EndYear.setEnabled(false);

            // Match end date fields with start date fields
            EndDay.setText(StartDay.getText());
            EndMonth.setSelection(StartMonth.getSelectedItemPosition());
            EndYear.setText(StartYear.getText());
        } else {
            // SingleDateMode off
            SingleDateMode = false;
            EndDay.setEnabled(true);
            EndMonth.setEnabled(true);
            EndYear.setEnabled(true);
        }
    }


    // Back Button Pressed, Action Cancelled
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }



    // Save changes and exit
    public void saveChanges(View view) {

        // Get variables
        int startDay = Integer.parseInt(StartDay.getText().toString());
        int startMonth = StartMonth.getSelectedItemPosition();
        int startYear = Integer.parseInt(StartYear.getText().toString());
        int endDay = Integer.parseInt(EndDay.getText().toString());
        int endMonth = EndMonth.getSelectedItemPosition();
        int endYear = Integer.parseInt(EndYear.getText().toString());


        // TODO Check for intersecting dates


        // Check to see if every input is valid (end date not greater than start date, years in allowable ranges, etc)
        // Make necessary changes before passing information to MainActivity
        dateFieldChanged();

        Intent intent = new Intent();
        intent.putExtra(getString(R.string.orders_type), OrderTypeSpinner.getSelectedItemPosition());

        // Start Date
        intent.putExtra(getString(R.string.start_day), startDay);
        intent.putExtra(getString(R.string.start_month), startMonth);
        intent.putExtra(getString(R.string.start_year), startYear);

        // End Date
        intent.putExtra(getString(R.string.end_day), endDay);
        intent.putExtra(getString(R.string.end_month), endMonth);
        intent.putExtra(getString(R.string.end_year), endYear);

        setResult(RESULT_OK, intent);
        finish();
    }


}
