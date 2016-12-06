package com.weinbergsoftware.www.myguardpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Locale;


public class UpdateRank extends AppCompatActivity {


    //******************** Variables *******************
    private int CurrentYear;
    //**************************************************


    //***************** UI Variables *******************
    // Input
    private Spinner RankSpinner;
    private Spinner MonthSpinner;
    private EditText YearEditText;

    // Display
    private TextView TISTextView;
    private TextView PayPerMUTATextView;
    //**************************************************





    //***************** My Methods *******************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rank);


        // Setup Variable
        Calendar now = Calendar.getInstance();
        CurrentYear = now.get(Calendar.YEAR);


        // Assign UI Variables
        RankSpinner = (Spinner) findViewById(R.id.rankSpinner);
        MonthSpinner = (Spinner) findViewById(R.id.EnlMonth);
        YearEditText = (EditText) findViewById(R.id.EnlYear);
        TISTextView = (TextView) findViewById(R.id.timeInService);
        PayPerMUTATextView = (TextView) findViewById(R.id.payPerMuta);


        // Setup UI Listeners
        RankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateTISAndPayViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Intentionally Left Blank
            }
        });
        MonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateTISAndPayViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Intentionally Left Blank
            }
        });

        // Listener for YearEditText
        YearEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    yearChanged();
                    return true;
                }
                return false;
            }
        });



        // Get Rank and Enlistment Information from MainActivity
        Intent intent = getIntent();
        int rank = intent.getIntExtra("Rank", 0);
        int enlistmentMonth = intent.getIntExtra("Month", 0);
        int enlistmentYear = intent.getIntExtra("Year", 0);

        // Populate RankSpinner with ranks
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.rank_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RankSpinner.setAdapter(adapter);

        // Set to rank passed from intent
        RankSpinner.setSelection(rank);

        // Populate Enlistment Month Spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.month_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MonthSpinner.setAdapter(adapter2);

        // Set to month passed from intent
        MonthSpinner.setSelection(enlistmentMonth);

        // Set up enlistment year text view
        YearEditText.setText(String.format(Locale.US, "%d", enlistmentYear));

        // Reflect changes in TIS and Per MUTA Views
        updateTISAndPayViews();

    }



    // Checks if the entered year is valid, changes if its not
    private void yearChanged() {

        int enlYear = Integer.parseInt(YearEditText.getText().toString());

        // Max out at current year
        if (enlYear > CurrentYear) YearEditText.setText(String.format(Locale.US, "%d", CurrentYear));

        // Min out at current year minus 50 years (typical enlistments dont last over 45 years)
        int minYear = CurrentYear - 50;
        if (enlYear < minYear) YearEditText.setText(String.format(Locale.US, "%d", minYear));

        updateTISAndPayViews();
    }



    // Update TIS and Pay textviews
    private void updateTISAndPayViews() {

        // Retrieve Rank, Enlistment Month, Enlistment Year from UI
        int rank = RankSpinner.getSelectedItemPosition();
        int enlMonth = MonthSpinner.getSelectedItemPosition();
        int enlYear = Integer.parseInt(YearEditText.getText().toString());

        // Get Time in Service and Per MUTA Pay from static RankAndTIS
        int TIS = RankAndTIS.getYearsInService(enlMonth, enlYear);
        double MUTAPay = RankAndTIS.getPayPerMuta(rank, TIS);

        // Update Years in Service
        String yearStr = "Years";
        if (TIS == 1) yearStr = "Year";
        TISTextView.setText(String.format(Locale.US, "%d %s", TIS, yearStr));

        // Update Pay Per MUTA
        PayPerMUTATextView.setText(String.format(Locale.US, "$%.2f", MUTAPay));

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

        // Called because the user might hit the save changes button while editing the year
        // Dont let the user put '1' as their enlistment year!!
        yearChanged();

        Intent intent = new Intent();
        intent.putExtra("Rank", RankSpinner.getSelectedItemPosition());
        intent.putExtra("Month", MonthSpinner.getSelectedItemPosition());
        intent.putExtra("Year", Integer.parseInt(YearEditText.getText().toString()));
        setResult(RESULT_OK, intent);
        finish();
    }


}
