package com.weinbergsoftware.www.myguardpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;



public class MainActivity extends AppCompatActivity {


    // TODO Get one of them add buttons up in this bitch to add orders


    //***************** UI Variables *******************
    // Input
    private ImageButton RankButton;

    // Display
    private TextView RankTISView;
    private TextView AmountOwedView;
    private TextView MUTAsOwedView;

    // ListView Stuff
    private OrdersAdapter OrdersManager;
    //**************************************************



    //***************** Variables **********************
    private int Rank;
    private int EnlistmentMonth;
    private int EnlistmentYear;
    //**************************************************




    /*
    Called when the activity is created... which is when the app is first opened
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        

        // Setup UI Variables
        RankButton = (ImageButton) findViewById(R.id.UpdateRank);
        RankTISView = (TextView) findViewById(R.id.RankText);
        AmountOwedView = (TextView) findViewById(R.id.DollarAmount);
        MUTAsOwedView = (TextView) findViewById(R.id.MUTAs);
        ListView OrdersListView = (ListView) findViewById(R.id.OrdersList);



        // Load rank information
        if (!loadRankData()) {
            // Start update rank activity to retrieve rank from user if none is saved
            startUpdateRank(findViewById(R.id.UpdateRank));
        }


        // Create OrdersAdapter
        OrdersManager = new OrdersAdapter(this);
        OrdersManager.setRemovePayedButton((Button) findViewById(R.id.PayoutButton)); // Give the payout button to Orders Manager
        OrdersListView.setAdapter(OrdersManager);



        // Create listener for listview if editing is a thing I want to do
        /*
        final Context context = this;
        OrdersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                OrdersObject selectedOrders = (OrdersObject) OrdersManager.getItem(position);

                Intent editIntent = new Intent(context, Add_Orders.class);

                editIntent.putExtra(getString(R.string.orders_type), selectedOrders.getOrdersType()); // Orders Type
                editIntent.putExtra(getString(R.string.saved_rank), Rank);
                editIntent.putExtra(getString(R.string.saved_TIS), RankAndTIS.getYearsInService(EnlistmentMonth, EnlistmentYear));

                // Finish this up
                // Start Date Day
                // Start Date Month
                // Start Date Year

                // End Date Day
                // End Date Month
                // End Date Year


                startActivityForResult(editIntent, 2);
            }

        });
        */



        // Update rank and TIS display
        updateRankDisplay();
        updateMoneyAndMUTAs();

    }



    // Save rank data to preference file
    public void saveRankData() {
        // Load shared preferences object and editor
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Apply Rank information to editor
        editor.putInt(getString(R.string.saved_rank), Rank);
        editor.putInt(getString(R.string.saved_enl_month), EnlistmentMonth);
        editor.putInt(getString(R.string.saved_enl_year), EnlistmentYear);

        // Apply changes
        editor.apply();
    }


    /*
    Loads rank information from preferences
    If preference data does not exist, sets rank variables to defaults and returns false
    Otherwise, returns true if successful
     */
    public boolean loadRankData() {
        // Load shared preferences object
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        // Load Rank information from sharedPref
        Rank = sharedPref.getInt(getString(R.string.saved_rank), -1);
        EnlistmentMonth = sharedPref.getInt(getString(R.string.saved_enl_month), 0);
        EnlistmentYear = sharedPref.getInt(getString(R.string.saved_enl_year), 2010);

        /* If the default value for rank is returned, the file is empty
         Return false */
        if (Rank == -1) {
            Rank = 0;
            return false;
        }

        // If everything loaded from file
        return true;
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }





    // Update Rank Display
    public void updateRankDisplay() {

        // Update Rank Button
        if (Rank == 0) RankButton.setImageResource(R.mipmap.e0);
        else if (Rank == 1) RankButton.setImageResource(R.mipmap.e2);
        else if (Rank == 2) RankButton.setImageResource(R.mipmap.e3);
        else if (Rank == 3) RankButton.setImageResource(R.mipmap.e4);
        else if (Rank == 4) RankButton.setImageResource(R.mipmap.e5);
        else if (Rank == 5) RankButton.setImageResource(R.mipmap.e6);
        else if (Rank == 6) RankButton.setImageResource(R.mipmap.e7);

        // Update Rank Text
        int TimeInService = RankAndTIS.getYearsInService(EnlistmentMonth, EnlistmentYear);
        String yearStr = getString(R.string.year_string_plural);
        if (TimeInService == 1) yearStr = getString(R.string.year_string_singular);
        RankTISView.setText(String.format(Locale.US, "%s, %d %s", RankAndTIS.rankForCode(Rank), TimeInService, yearStr));
    }


    // Update MUTA's and Money Owed
    public void updateMoneyAndMUTAs() {

        // Retrieve orders and pay information from Orders Manager
        double[] data = OrdersManager.getTotalMUTAsAndPay();
        double totalPay = data[0];
        int MUTACount = (int)data[1];

        // MUTA formatting to read 'x MUTAs'
        String mutaStr = getString(R.string.muta_string_plural);
        if (MUTACount == 1) mutaStr = getString(R.string.muta_string_singular);
        MUTAsOwedView.setText(String.format(Locale.US, "%d %s", MUTACount, mutaStr));

        // Dollar formatting
        AmountOwedView.setText(String.format(Locale.US, "$%.2f", totalPay));

    }





    // Called when user selects the rank button OR the update rank menu item
    // Begins UpdateRank activity
    public void startUpdateRank(View view) {
        Intent intent = new Intent(this, UpdateRank.class);
        intent.putExtra(getString(R.string.saved_rank), Rank);
        intent.putExtra(getString(R.string.saved_enl_month), EnlistmentMonth);
        intent.putExtra(getString(R.string.saved_enl_year), EnlistmentYear);
        startActivityForResult(intent, 0);
    }



    // Called when user selects the add order menu item
    // Begins AddOrders activity
    public void startAddOrders() {
        Intent intent = new Intent(this, Add_Orders.class);
        intent.putExtra(getString(R.string.saved_rank), Rank);
        intent.putExtra(getString(R.string.saved_TIS), RankAndTIS.getYearsInService(EnlistmentMonth, EnlistmentYear));

        // TODO Pass array with dates

        startActivityForResult(intent, 1);
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Request Code 0 is UpdateRank
        if (requestCode == 0 && resultCode == RESULT_OK) {

            // Gather data
            Rank = data.getIntExtra(getString(R.string.saved_rank), 0);
            EnlistmentMonth = data.getIntExtra(getString(R.string.saved_enl_month), 0);
            EnlistmentYear = data.getIntExtra(getString(R.string.saved_enl_year), 0);

            // Save rank data
            saveRankData();

            // Tell OrdersManager whats up
            OrdersManager.updateRankAndEnlDate(Rank, new int[]{EnlistmentMonth, EnlistmentYear});

            // Update UI to reflect new rank
            updateRankDisplay();
            updateMoneyAndMUTAs();
        }

        // Request Code 1 is AddOrders
        else if (requestCode == 1 && resultCode == RESULT_OK) {

            // Load Orders Type
            int ordersType = data.getIntExtra(getString(R.string.orders_type), 0);

            // Load Start Date
            int sDay = data.getIntExtra(getString(R.string.start_day), 0);
            int sMonth = data.getIntExtra(getString(R.string.start_month), 0);
            int sYear = data.getIntExtra(getString(R.string.start_year), 0);

            // Load End Date
            int eDay = data.getIntExtra(getString(R.string.end_day), 0);
            int eMonth = data.getIntExtra(getString(R.string.end_month), 0);
            int eYear = data.getIntExtra(getString(R.string.end_year), 0);


            // Create array parameters
            int[] EnlDate = {EnlistmentMonth, EnlistmentYear};
            int[] startDate = {sDay, sMonth, sYear};
            int[] endDate = {eDay, eMonth, eYear};

            // Create OrdersObject
            OrdersObject newObj = new OrdersObject(false, ordersType, Rank, EnlDate, startDate, endDate);

            // Add newObj to the adapter
            OrdersManager.addOrders(newObj);

            // Update money and MUTAs display to reflect new information
            updateMoneyAndMUTAs();
        }


        // Request Code 2 is EditOrders
        else if (requestCode == 2 && resultCode == RESULT_OK) {

            // TODO Pass info back to OrdersManager for updated view

            // TODO Come back to this

        }

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        // Edit Rank
        if (id == R.id.EditRank) {
            startUpdateRank(findViewById(R.id.UpdateRank));
            return true;
        }

        // Add Orders
        else if (id == R.id.AddOrders) {
            startAddOrders();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
