package com.weinbergsoftware.www.myguardpay;

import java.util.Locale;

/**
 * Created by Mike Weinberg on 11/28/2016.
 */

public class OrdersObject implements Comparable {


    //***************** Variables **********************
    boolean PayedOut; // Determines whether to return its values or not

    private int OrdersType;
    private int Rank;
    private int EnlMonth;
    private int EnlYear;

    // Start Date
    private int StartDate_day;
    private int StartDate_month;
    private int StartDate_year;

    // End Date
    private int EndDate_day;
    private int EndDate_month;
    private int EndDate_year;

    // CalculatedVariables
    private int MUTACount;
    private double DollarAmount;
    private int TIS;

    //**************************************************



    // Constructor
    public OrdersObject(boolean payedOut, int ordersType, int rank, int[] EnlDate, int[] startDate, int[] endDate) {

        // Setup variables
        PayedOut = payedOut;

        OrdersType = ordersType;
        Rank = rank;
        EnlMonth = EnlDate[0];
        EnlYear = EnlDate[1];

        StartDate_day = startDate[0];
        StartDate_month = startDate[1];
        StartDate_year = startDate[2];

        EndDate_day = endDate[0];
        EndDate_month = endDate[1];
        EndDate_year = endDate[2];

        // Calculate TIS, MUTAs, and Pay from inputs
        calculateTIS(); // IMPORTANT calculateTIS() MUST be called prior to calculateMUTAsAndPay
        calculateMUTAsAndPay();
    }


    // For loading purposes
    public OrdersObject(String codeString) {

        // Payed out code
        int payedOutCode = Integer.parseInt(codeString.substring(0, 1));
        PayedOut = true;
        if (payedOutCode == 0) PayedOut = false;

        // Orders Type
        OrdersType = Integer.parseInt(codeString.substring(1, 2));

        // Rank
        Rank = Integer.parseInt(codeString.substring(2, 4));

        // Enl Month
        EnlMonth = Integer.parseInt(codeString.substring(4, 6));

        // Enl Year
        EnlYear = Integer.parseInt(codeString.substring(6, 10));

        // Start Day
        StartDate_day = Integer.parseInt(codeString.substring(10, 12));

        // Start Month
        StartDate_month = Integer.parseInt(codeString.substring(12, 14));

        // Start Year
        StartDate_year = Integer.parseInt(codeString.substring(14, 18));


        // End Day
        EndDate_day = Integer.parseInt(codeString.substring(18, 20));

        // End Month
        EndDate_month = Integer.parseInt(codeString.substring(20, 22));

        // End Year
        EndDate_year = Integer.parseInt(codeString.substring(22, 26));


        calculateTIS();
        calculateMUTAsAndPay();
    }


    // To string for saving purposes
    public String toString() {

        // Payout code 1 Digit
        int payoutCode = 0;
        if (PayedOut) payoutCode = 1;

        // OrdersType 1 digit, probably wont change

        // Rank 2 digits (for future ranks)
        String rankFormatCode = "0%d";
        if (Rank >= 10) rankFormatCode = "%d";
        String rankCode = String.format(Locale.US, rankFormatCode, Rank);

        // Enlisted Month 2 digits
        String enlMnthFormatCode = "0%d";
        if (EnlMonth >= 10) enlMnthFormatCode = "%d";
        String enlMnthCode = String.format(Locale.US, enlMnthFormatCode, EnlMonth);

        // Enlisted Year 4 digits (Its going to be a little while before 5 digits are needed)

        // Start Day 2 digits
        String startDateFormatCode = "0%d";
        if (StartDate_day >= 10) startDateFormatCode = "%d";
        String startDayCode = String.format(Locale.US, startDateFormatCode, StartDate_day);

        // Start Month 2 digits
        String startMonthFormatCode = "0%d";
        if (StartDate_month >= 10) startMonthFormatCode = "%d";
        String startMonthCode = String.format(Locale.US, startMonthFormatCode, StartDate_month);

        // Start Year 4 digits

        // End day 2 digits
        String endDateFormatCode = "0%d";
        if (EndDate_day >= 10) endDateFormatCode = "%d";
        String endDayCode = String.format(Locale.US, endDateFormatCode, EndDate_day);


        // End month 2 digits
        String endMonthFormatCode = "0%d";
        if (EndDate_month >= 10) endMonthFormatCode = "%d";
        String endMonthCode = String.format(Locale.US, endMonthFormatCode, EndDate_month);


        // End year 4 digits

        return String.format(Locale.US, "%d%d%s%s%d%s%s%d%s%s%d", payoutCode, OrdersType, rankCode, enlMnthCode, EnlYear, startDayCode, startMonthCode, StartDate_year, endDayCode, endMonthCode, EndDate_year);
    }







    @Override
    public int compareTo(Object o) {

        // Because casting doesn't seem to work, declare a new object
        OrdersObject theObject;

        // Make sure the passed object is actually an instance of OrdersObject
        if (o instanceof OrdersObject) theObject = (OrdersObject)o;
        else return -10;


        // Retrieve start and end date information from passed object
        int[] c_startDate = theObject.getStartDate();
        int[] c_endDate = theObject.getEndDate();



        // CONDITION 1 If this object end_date is before passed object start_date, pass -1

        // If this objects end year is less than passed objects start year, no further calculations need be made
        if (EndDate_year < c_startDate[2]) return -1;

        // If the years match, but this month is less than the passed month, no further calculations
        else if (EndDate_year == c_startDate[2] && EndDate_month < c_startDate[1]) return -1;

        // If the years match, months match, but this day is less than passed day, yada yada
        else if (EndDate_year == c_startDate[2] && EndDate_month == c_startDate[1] && EndDate_day < c_startDate[0]) return -1;



        // CONDITION 2 If this object start_date is after passed object end_date, pass 1

        // If this objects start year is greater than passed objects end year, no further calculations need be made
        if (StartDate_year > c_endDate[2]) return 1;

            // If the years match, but this month is greater than the passed month, no further calculations
        else if (StartDate_year == c_endDate[2] && StartDate_month > c_endDate[1]) return 1;

            // If the years match, months match, but this day is greater than passed day, yada yada
        else if (StartDate_year == c_endDate[2] && StartDate_month == c_endDate[1] && StartDate_day > c_endDate[0]) return 1;



        // If both above conditions are not met, intersection is occurring
        // Since objects intersect, pass 0
        return 0;
    }






    private void calculateTIS() {
        TIS = RankAndTIS.getYearsInService(EnlMonth, EnlYear);
    }


    private void calculateMUTAsAndPay() {
        int[] startDate = {StartDate_day, StartDate_month, StartDate_year};
        int[] endDate = {EndDate_day, EndDate_month, EndDate_year};
        MUTACount = RankAndTIS.getMUTAsFor(OrdersType, startDate, endDate);

        DollarAmount = MUTACount * RankAndTIS.getPayPerMuta(Rank, TIS);
    }




    public void setPayedOut(boolean payed) {PayedOut = payed;}
    public boolean getPayedOut() {return PayedOut;}



    // Methods that will be called to get the totals for the MainActivity
    public int getMUTACount() {
        if (PayedOut) return 0;
        return MUTACount;
    }
    public double getDollarAmount() {
        if (PayedOut) return 0.00;
        return DollarAmount;
    }



    public void setOrdersType(int type) {
        OrdersType = type;
        calculateMUTAsAndPay();
    }
    public int getOrdersType() {return OrdersType;}



    public void setRank(int rank) {
        Rank = rank;
        calculateMUTAsAndPay();
    }
    public int getRank() {return Rank;}


    public void setEnlDate(int[] EnlDate) {
        EnlMonth = EnlDate[0];
        EnlYear = EnlDate[1];
        calculateTIS();
        calculateMUTAsAndPay();
    }
    public int[] getEnlDate() {
        return new int[]{EnlMonth, EnlYear};
    }




    public void setStartDate(int[] startDate) {
        StartDate_day = startDate[0];
        StartDate_month = startDate[1];
        StartDate_year = startDate[2];
        calculateMUTAsAndPay();
    }
    public int[] getStartDate() {
        return new int[]{StartDate_day, StartDate_month, StartDate_year};
    }


    public void setEndDate(int[] endDate) {
        EndDate_day = endDate[0];
        EndDate_month = endDate[1];
        EndDate_year = endDate[2];
        calculateMUTAsAndPay();
    }
    public int[] getEndDate() {
        return new int[]{EndDate_day, EndDate_month, EndDate_year};
    }

}
