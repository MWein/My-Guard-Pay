package com.weinbergsoftware.www.myguardpay;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Mike Weinberg on 11/28/2016.
 */





public class OrdersAdapter extends BaseAdapter {


    //*************** UI Variables *********************
    private Button RemovePayedButton;
    int PayedItems; // Updates as payed toggle buttons are changed. Value determines whether "Remove Payed" button is enabled
    //**************************************************


    //***************** Variables **********************
    private int CurrentYear;
    private MainActivity mainActivity;
    private LayoutInflater mInflater;
    private ArrayList<OrdersObject> mDataSource;
    //**************************************************



    public OrdersAdapter(MainActivity theActivity) {

        // Get the current year
        // Current year is used for formatting purposes in getView method
        Calendar now = Calendar.getInstance();
        CurrentYear = now.get(Calendar.YEAR);

        // Make sure this is called prior to loading
        mainActivity = theActivity;

        // Load orders from preferences
        loadData();

        // Load up other variables
        PayedItems = 0;
        mInflater = (LayoutInflater) theActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    // Assigns RemovePayedButton to OrdersAdapter
    public void setRemovePayedButton(Button payoutButton) {
        RemovePayedButton = payoutButton;

        // Setup listener for button
        RemovePayedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteSelectedOrders();
            }
        });

        // Determines if PayoutButton should be enabled or not based on number of payed out items
        if (PayedItems > 0) RemovePayedButton.setEnabled(true);
        else RemovePayedButton.setEnabled(false);
    }





    public void loadData() {
        mDataSource = new ArrayList<>();

        // Load MainActivity's preferences
        SharedPreferences sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE);

        int objCount = sharedPref.getInt("size", 0);

        if (objCount != 0) {
            String dataString = sharedPref.getString("data", "");

            System.out.println(dataString);

            String[] dataArray = dataString.split("B", objCount);
            for (String str : dataArray) {
                OrdersObject newObj = new OrdersObject(str);
                mDataSource.add(newObj);
            }
        }
    }




    public void saveChanges() {

        // Load MainActivity's preferences
        SharedPreferences sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Create string from OrdersObjects
        int mSize = mDataSource.size();

        int counter = 0;
        String dataString = "";

        for (OrdersObject obj : mDataSource) {
            String orderCode = obj.toString();
            String formatStr = "%sB%s";
            if (counter == 0) formatStr = "%s%s";
            dataString = String.format(Locale.US, formatStr, dataString, orderCode);
            counter++;
        }

        // Get the string information to editor
        editor.putInt("size", mSize);
        editor.putString("data", dataString);

        // Apply changes
        editor.apply();
    }



    // Called when a payed toggle button is selected
    public void payoutButtonToggled(int position, boolean isChecked) {

        // If the button that passed this event is checked, increase PayedItems
        // Otherwise, decrease it
        if (isChecked) PayedItems++;
        else PayedItems--;

        // If there are any 'payed' buttons checked, enable the 'Remove Payed' button
        if (PayedItems > 0) RemovePayedButton.setEnabled(true);
        else RemovePayedButton.setEnabled(false);

        // Tell associated orders object to update its payed out value
        mDataSource.get(position).setPayedOut(isChecked);

        // Tell MainActivity to update its MUTAs and Money Owed views
        mainActivity.updateMoneyAndMUTAs();

        saveChanges();
    }



    // Updates rank and enlistment date between all orders objects
    public void updateRankAndEnlDate(int rank, int[] enlDate) {

        // Update every OrdersObject
        for (OrdersObject curObj : mDataSource) {
            curObj.setRank(rank);
            curObj.setEnlDate(enlDate);
        }

        // Update ListView
        notifyDataSetChanged();

        saveChanges();
    }



    // Returns total MUTAs and pay from data source
    public double[] getTotalMUTAsAndPay() {

        // Initialize variables
        int totalMutas = 0;
        double totalPay = 0.00;

        // Add up total MUTAs and total pay from data source
        for (OrdersObject curObj : mDataSource) {
            totalMutas = totalMutas + curObj.getMUTACount();
            totalPay = totalPay + curObj.getDollarAmount();
        }

        // Return the result in a double array
        return new double[]{totalPay, totalMutas};
    }





    // Add orders to the list, and sort the list
    public void addOrders(OrdersObject newOrd) {

        // Add the new guy to the list
        mDataSource.add(newOrd);

        // Sort the list
        Collections.sort(mDataSource, new Comparator<OrdersObject>() {
            @Override
            public int compare(OrdersObject o1, OrdersObject o2) {
                // Return the opposite returned to sort in descending order
                return o1.compareTo(o2) * -1;
            }
        });

        // Update ListView
        notifyDataSetChanged();

        saveChanges();
    }


    // Sniffs out orders marked "payed" and removes them
    public void deleteSelectedOrders() {

        // Initialize the kill list
        ArrayList<OrdersObject> doomedOrders = new ArrayList<>();

        // Gooose. Check for bogeys
        for (OrdersObject curObj : mDataSource) {
            // Add the OrdersObject to the kill list if its payed out
            if (curObj.getPayedOut()) doomedOrders.add(curObj);
        }

        // TAKE THE SHOT!!!
        for (OrdersObject doomedObj : doomedOrders) {
            mDataSource.remove(doomedObj);
        }

        // Does not do a count and automatically sets PayedItems to 0 and disables RemovePayedButton
        // This method deletes all selected items. Pretty safe to assume there arent many payed items after this
        PayedItems = 0;
        RemovePayedButton.setEnabled(false);

        // Aaaaaand update the ListView
        notifyDataSetChanged();


        saveChanges();
    }












    // TODO Option to select orders to edit











    //***************** Override Methods **********************

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Initialize the rowView from Inflater
        // TODO Follow this things advice
        View rowView = mInflater.inflate(R.layout.orders_list_view, parent, false);


        // Setup UI elements
        TextView OrderDatesTextView = (TextView) rowView.findViewById(R.id.order_dates);
        TextView OrderValueTextView = (TextView) rowView.findViewById(R.id.order_value);
        TextView OrderTypeTextView = (TextView) rowView.findViewById(R.id.order_type);
        ToggleButton PayedToggleButton = (ToggleButton) rowView.findViewById(R.id.PaidButton);


        // Setup event handler for toggle button
        PayedToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                payoutButtonToggled(position, isChecked);
            }
        });



        // Get Data and populate
        OrdersObject ordObj = (OrdersObject) getItem(position);


        // Set toggle button based on pay out value for orders object
        PayedToggleButton.setChecked(ordObj.getPayedOut());


        // ************** Populate dates text view *******************
        int type = ordObj.getOrdersType();

        int[] startDate = ordObj.getStartDate();
        int startDay = startDate[0];
        int startMonth = startDate[1];
        int startYear = startDate[2];
        String dateString = String.format(Locale.US, "%d%s", startDay, RankAndTIS.getStringForMonth(startMonth));

        if (startYear != CurrentYear) dateString = String.format(Locale.US, "%s%d", dateString, (startYear - 2000));

        int[] endDate = ordObj.getEndDate();
        int endDay = endDate[0];
        int endMonth = endDate[1];
        int endYear = endDate[2];

        boolean daysAreEqual = false;
        if (startDay == endDay && startMonth == endMonth && startYear == endYear) daysAreEqual = true;

        if (!daysAreEqual) {
            // Add end date
            dateString = String.format(Locale.US, "%s - %d%s", dateString, endDay, RankAndTIS.getStringForMonth(endMonth));

            if (endYear != CurrentYear) dateString = String.format(Locale.US, "%s%d", dateString, (endYear - 2000));
        }

        OrderDatesTextView.setText(dateString);

        // ************** End Populate dates text view *******************




        // Populate value text view
        boolean payed = ordObj.getPayedOut(); // Get current payed out value
        ordObj.setPayedOut(false); // Set payed out to false so that the orders object returns its value for display purposes
        OrderValueTextView.setText(String.format(Locale.US, "$%.2f", ordObj.getDollarAmount()));
        ordObj.setPayedOut(payed); // Set payed out back to its current value


        // Populate type text view
        String typeStr = "";
        if (type == 0) typeStr = "MFH";
        else if (type == 1) typeStr = "DRILL";
        else if (type == 2) typeStr = "AT";
        else if (type == 3) typeStr = "OTHER";
        OrderTypeTextView.setText(String.format("%s               ", typeStr)); // Spaces ensure the view doesn't go off screen



        return rowView;
    }

    //**************************************************







}
