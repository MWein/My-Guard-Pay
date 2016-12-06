package com.weinbergsoftware.www.myguardpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    // TODO Codify Request Codes Somewhere

    // TODO If the app was never opened, prompt the user to fill out rank information


    //***************** UI Variables *******************
    // Input
    private ImageButton RankButton;

    // Display
    private TextView RankTISView;
    private TextView AmountOwedView;
    private TextView MUTAsOwedView;

    // ListView Stuff
    private OrdersAdapter mOrdersAdapter;
    private ListView OrdersListView;
    //**************************************************



    //***************** Variables **********************
    private int Rank;
    private int EnlistmentMonth;
    private int EnlistmentYear;
    //**************************************************





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Setup UI Variables
        RankButton = (ImageButton) findViewById(R.id.updateRank);
        RankTISView = (TextView) findViewById(R.id.RankText);
        AmountOwedView = (TextView) findViewById(R.id.DollarAmount);
        MUTAsOwedView = (TextView) findViewById(R.id.MUTAs);
        OrdersListView = (ListView) findViewById(R.id.OrdersList);




        // TODO Test ListView

        //public OrdersObject(int ordersType, int rank, int[] EnlDate, int[] startDate, int[] endDate)

        int[] enlDate = {1, 2011};
        int[] startDate = {28, 10, 2016};
        int[] endDate = {28, 10, 2016};
        OrdersObject testObj = new OrdersObject(0, 3, enlDate, startDate, endDate);

        OrdersObject testObj2 = new OrdersObject(0, 3, enlDate, startDate, endDate);
        OrdersObject testObj3 = new OrdersObject(0, 3, enlDate, startDate, endDate);
        OrdersObject testObj4 = new OrdersObject(0, 3, enlDate, startDate, endDate);
        OrdersObject testObj5 = new OrdersObject(0, 3, enlDate, startDate, endDate);
        OrdersObject testObj6 = new OrdersObject(0, 3, enlDate, startDate, endDate);
        OrdersObject testObj7 = new OrdersObject(0, 3, enlDate, startDate, endDate);
        OrdersObject testObj8 = new OrdersObject(0, 3, enlDate, startDate, endDate);


        ArrayList<OrdersObject> myShit = new ArrayList<OrdersObject>();

        /*
        myShit.add(testObj);
        myShit.add(testObj2);
        myShit.add(testObj3);
        myShit.add(testObj4);
        myShit.add(testObj5);
        myShit.add(testObj6);
        myShit.add(testObj7);
        myShit.add(testObj8);
        */

        mOrdersAdapter = new OrdersAdapter(this, myShit);
        OrdersListView.setAdapter(mOrdersAdapter);

        // TODO Test ListView






        // Load everything from file
        loadSavedData();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    // Save everything to file
    public void saveData() {

        // TODO Write

    }


    // Load up everything from file
    public void loadSavedData() {

        // TODO Load Rank and Enlistment Date From File
        Rank = 0;
        EnlistmentMonth = 4;
        EnlistmentYear = 2010;


        // TODO Load Fucking Everything Else


        // Update Rank, Money Owed, and MUTAs owed Displays
        updateRankDisplay();
        updateMoneyAndMUTAs();
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
        String yearStr = "Years";
        if (TimeInService == 1) yearStr = "Year";
        RankTISView.setText(String.format(Locale.US, "%s, %d %s", RankAndTIS.rankForCode(Rank), TimeInService, yearStr));

    }


    // Update MUTA's and Money Owed
    public void updateMoneyAndMUTAs() {

        // TODO Comment this shit

        double[] data = mOrdersAdapter.getTotalMUTAsAndPay();
        double totalPay = data[0];
        int MUTACount = (int)data[1];

        String mutaStr = "MUTAs";
        if (MUTACount == 1) mutaStr = "MUTA";
        MUTAsOwedView.setText(String.format(Locale.US, "%d %s", MUTACount, mutaStr));

        AmountOwedView.setText(String.format(Locale.US, "$%.2f", totalPay));

    }


    // Called when user selects the rank button OR the update rank menu item
    // Begins UpdateRank activity
    public void startUpdateRank(View view) {
        Intent intent = new Intent(this, UpdateRank.class);
        intent.putExtra("Rank", Rank);
        intent.putExtra("Month", EnlistmentMonth);
        intent.putExtra("Year", EnlistmentYear);
        startActivityForResult(intent, 0);
    }



    // Called when user selects the add order menu item
    // Begins AddOrders activity
    public void startAddOrders() {
        Intent intent = new Intent(this, Add_Orders.class);

        // TODO Put anything that might come up
        intent.putExtra("Rank", Rank);
        intent.putExtra("TIS", RankAndTIS.getYearsInService(EnlistmentMonth, EnlistmentYear));

        startActivityForResult(intent, 1);
    }



    // Called when user selects the new LES menu item
    // Begins NewLES activity
    public void startNewLES() {

        // TODO Write this!!

        // Intent Code 2

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Request Code 0 is UpdateRank
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Rank = data.getIntExtra("Rank", 0);
            EnlistmentMonth = data.getIntExtra("Month", 0);
            EnlistmentYear = data.getIntExtra("Year", 0);

            updateRankDisplay();
            updateMoneyAndMUTAs();
            saveData();
        }

        // Request Code 1 is AddOrders
        else if (requestCode == 1 && resultCode == RESULT_OK) {

            // Load Orders Type
            int ordersType = data.getIntExtra("Type", 0);

            // Load Start Date
            int sDay = data.getIntExtra("sDay", 0);
            int sMonth = data.getIntExtra("sMonth", 0);
            int sYear = data.getIntExtra("sYear", 0);

            // Load End Date
            int eDay = data.getIntExtra("eDay", 0);
            int eMonth = data.getIntExtra("eMonth", 0);
            int eYear = data.getIntExtra("eYear", 0);


            // Create array parameters
            int[] EnlDate = {EnlistmentMonth, EnlistmentYear};
            int[] startDate = {sDay, sMonth, sYear};
            int[] endDate = {eDay, eMonth, eYear};

            // Create OrdersObject
            OrdersObject newObj = new OrdersObject(ordersType, Rank, EnlDate, startDate, endDate);

            // Add newObj to the adapter
            mOrdersAdapter.addOrders(newObj);

            updateMoneyAndMUTAs();
            saveData();
        }

        // Request Code 2 is NewLES
        else if (requestCode == 2 && resultCode == RESULT_OK) {


            // TODO Delete Selected Orders


            updateMoneyAndMUTAs();
            saveData();
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
            startUpdateRank(findViewById(R.id.updateRank));
            return true;
        }

        // Add Orders
        else if (id == R.id.AddOrders) {
            startAddOrders();
            return true;
        }

        // New LES
        else if (id == R.id.NewLES) {
            startNewLES();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
