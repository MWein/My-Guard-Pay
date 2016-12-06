package com.weinbergsoftware.www.myguardpay;

/**
 * Created by Mike Weinberg on 11/28/2016.
 */

public class OrdersObject {


    // TODO Codify Order Types


    //***************** Variables **********************
    private int UID;

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
    public OrdersObject(int ordersType, int rank, int[] EnlDate, int[] startDate, int[] endDate) {

        // Setup variables
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




    private void calculateTIS() {
        TIS = RankAndTIS.getYearsInService(EnlMonth, EnlYear);
    }

    private void calculateMUTAsAndPay() {
        int[] startDate = {StartDate_day, StartDate_month, StartDate_year};
        int[] endDate = {EndDate_day, EndDate_month, EndDate_year};
        MUTACount = RankAndTIS.getMUTAsFor(OrdersType, startDate, endDate);

        DollarAmount = MUTACount * RankAndTIS.getPayPerMuta(Rank, TIS);
    }


    // Main methods that will be called to get the totals for the MainActivity
    public int getMUTACount() {return MUTACount;}
    public double getDollarAmount() {return DollarAmount;}



    public void setUID(int newID) {
        UID = newID;
    }
    public int getUID() {return UID;}


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



    public void setStartDate(int[] startDate) {
        StartDate_day = startDate[0];
        StartDate_month = startDate[1];
        StartDate_year = startDate[2];
        calculateMUTAsAndPay();
    }
    public int[] getStartDate() {
        int [] theDate = {StartDate_day, StartDate_month, StartDate_year};
        return theDate;
    }


    public void setEnlDate(int[] EnlDate) {
        EnlMonth = EnlDate[0];
        EnlYear = EnlDate[1];
        calculateTIS();
    }
    public int[] getEnlDate() {
        int [] theDate = {EnlMonth, EnlYear};
        return theDate;
    }


    public void setEndDate(int[] endDate) {
        EndDate_day = endDate[0];
        EndDate_month = endDate[1];
        EndDate_year = endDate[2];
        calculateMUTAsAndPay();
    }
    public int[] getEndDate() {
        int [] theDate = {EndDate_day, EndDate_month, EndDate_year};
        return theDate;
    }




}
