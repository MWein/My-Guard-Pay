package com.weinbergsoftware.www.myguardpay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Mike Weinberg on 11/28/2016.
 */


public class OrdersAdapter extends BaseAdapter {


    //***************** Variables **********************
    int CurrentYear;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<OrdersObject> mDataSource;
    //**************************************************



    public OrdersAdapter(Context context, ArrayList<OrdersObject> items) {

        Calendar now = Calendar.getInstance();
        CurrentYear = now.get(Calendar.YEAR);

        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    public double[] getTotalMUTAsAndPay() {

        // TODO Comment this shit

        int totalMutas = 0;
        double totalPay = 0.00;

        Iterator<OrdersObject> myIt = mDataSource.iterator();

        while (myIt.hasNext()) {

            OrdersObject currentObj = myIt.next();

            totalMutas = totalMutas + currentObj.getMUTACount();
            totalPay = totalPay + currentObj.getDollarAmount();

        }

        double[] returnVal = {totalPay, totalMutas};
        return returnVal;
    }



    public void updateEnlistmentDate(int[] date) {

        // Write this!!!

    }



    public void addOrders(OrdersObject newOrd) {

        // TODO Generate unique ID
        int newUID = 0;

        // Assign new ID to newOrd
        newOrd.setUID(newUID);

        // Add the new guy to the list
        mDataSource.add(newOrd);

        // TODO Sort the list, bubble sort should do it
        // TODO Alternatively, find an insertion point to avoid sorting

        // Update ListView
        notifyDataSetChanged();
    }


    public void deleteOrders(int[] UIDs) {

        // TODO Write this!!

    }


    // TODO Method to edit orders
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
    public View getView(int position, View convertView, ViewGroup parent) {

        // Initialize the rowView from Inflater
        View rowView = mInflater.inflate(R.layout.orders_list_view, parent, false);


        // Setup UI elements
        // TODO Decide what to do with the image field... if anything
        TextView OrderDatesTextView = (TextView) rowView.findViewById(R.id.order_dates);
        TextView OrderValueTextView = (TextView) rowView.findViewById(R.id.order_value);
        TextView OrderTypeTextView = (TextView) rowView.findViewById(R.id.order_type);


        // Get Data and populate
        OrdersObject ordObj = (OrdersObject) getItem(position);

        // TODO Decide what to do with image field... if anything



        // TODO Populate dates text view

        int type = ordObj.getOrdersType();
        String dateString = "";

        int[] startDate = ordObj.getStartDate();
        int startDay = startDate[0];
        int startMonth = startDate[1];
        int startYear = startDate[2];
        dateString = String.format(Locale.US, "%d%s", startDay, RankAndTIS.getStringForMonth(startMonth));

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





        // Populate value text view
        OrderValueTextView.setText(String.format(Locale.US, "$%.2f", ordObj.getDollarAmount()));


        // Populate type text view
        String typeStr = "";

        // TODO Replace with switch

        if (type == 0) typeStr = "MFH";
        else if (type == 1) typeStr = "DRILL";
        else if (type == 2) typeStr = "AT";
        else if (type == 3) typeStr = "OTHER";

        OrderTypeTextView.setText(String.format("%s               ", typeStr));



        return rowView;
    }

    //**************************************************







}
