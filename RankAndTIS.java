package com.weinbergsoftware.www.myguardpay;

import java.util.Calendar;


/**
 * Created by Mike Weinberg on 11/27/2016.
 */


    // TODO Create a new class with a different name. With blackjack, and hookers



    // Static methods for Rank strings and Time in Service operations
    // Days in month method is here too because I got sick of looking at it in Add_Orders Activity code


public class RankAndTIS {


    // Returns Time in Service in Years
    public static int getYearsInService(int month, int year) {

        // Get current date
        Calendar now = Calendar.getInstance();
        int CurrentYear = now.get(Calendar.YEAR);
        int CurrentMonth = now.get(Calendar.MONTH);

        // Figure out the difference
        int diff = CurrentYear - year;
        if (month > CurrentMonth) diff--;

        return diff;
    }


    // Returns Pay for Rank and TIS
    // Yeah, its a bit much but it would take awhile to make an XML, also would take more juice to read an XML anyway
    public static double getPayPerMuta(int rank, int TIS) {

        // 2016 Paychart used below. Update for 2017 once available

        double returnVal = 0.00;

        // E1
        if (rank == 0) returnVal = 1566.90;

        // E2
        else if (rank == 1) returnVal = 1756.50;

        // E3
        else if (rank == 2) {
            if (TIS < 2) returnVal = 1847.10;
            else if (TIS >= 2 && TIS < 3) returnVal = 1963.20;
            else if (TIS >= 3) returnVal = 2082.00;
        }

        // E4
        else if (rank == 3) {
            if (TIS < 2) returnVal = 2046.00;
            else if (TIS >= 2 && TIS < 3) returnVal = 2150.40;
            else if (TIS >= 3 && TIS < 4) returnVal = 2267.10;
            else if (TIS >= 4 && TIS < 6) returnVal = 2382.00;
            else if (TIS >= 6) returnVal = 2483.40;
        }

        // E5
        else if (rank == 4) {
            if (TIS < 2) returnVal = 2231.40;
            else if (TIS >= 2 && TIS < 3) returnVal = 2381.40;
            else if (TIS >= 3 && TIS < 4) returnVal = 2496.60;
            else if (TIS >= 4 && TIS < 6) returnVal = 2614.20;
            else if (TIS >= 6 && TIS < 8) returnVal = 2797.80;
            else if (TIS >= 8 && TIS < 10) returnVal = 2989.80;
            else if (TIS >= 10 && TIS < 12) returnVal = 3147.60;
            else if (TIS >= 12) returnVal = 3166.20;
        }

        // E6
        else if (rank == 5) {
            if (TIS < 2) returnVal = 2435.70;
            else if (TIS >= 2 && TIS < 3) returnVal = 2680.20;
            else if (TIS >= 3 && TIS < 4) returnVal = 2798.40;
            else if (TIS >= 4 && TIS < 6) returnVal = 2913.60;
            else if (TIS >= 6 && TIS < 8) returnVal = 3033.60;
            else if (TIS >= 8 && TIS < 10) returnVal = 3303.30;
            else if (TIS >= 10 && TIS < 12) returnVal = 3408.60;
            else if (TIS >= 12 && TIS < 14) returnVal = 3612.30;
            else if (TIS >= 14 && TIS < 16) returnVal = 3674.40;
            else if (TIS >= 16 && TIS < 18) returnVal = 3719.70;
            else if (TIS >= 18) returnVal = 3772.50;
        }

        // E7
        else if (rank == 6) {
            if (TIS < 2) returnVal = 2816.10;
            else if (TIS >= 2 && TIS < 3) returnVal = 3073.50;
            else if (TIS >= 3 && TIS < 4) returnVal = 3191.40;
            else if (TIS >= 4 && TIS < 6) returnVal = 3347.10;
            else if (TIS >= 6 && TIS < 8) returnVal = 3468.90;
            else if (TIS >= 8 && TIS < 10) returnVal = 3678.00;
            else if (TIS >= 10 && TIS < 12) returnVal = 3795.60;
            else if (TIS >= 12 && TIS < 14) returnVal = 4004.70;
            else if (TIS >= 14 && TIS < 16) returnVal = 4178.70;
            else if (TIS >= 16 && TIS < 18) returnVal = 4297.50;
            else if (TIS >= 18 && TIS < 20) returnVal = 4423.80;
            else if (TIS >= 20 && TIS < 22) returnVal = 4472.70;
            else if (TIS >= 22 && TIS < 24) returnVal = 4637.10;
            else if (TIS >= 24 && TIS < 26) returnVal = 4725.30;
            else if (TIS >= 26) returnVal = 5061.30;
        }



        /*  TEMPLATE FOR LATER RANKS IF I FEEL LIKE IT
        // E7
        else if (rank == 6) {
            if (TIS < 2) returnVal = 2816.10;
            else if (TIS >= 2 && TIS < 3) returnVal = 3073.50;
            else if (TIS >= 3 && TIS < 4) returnVal = 3191.40;
            else if (TIS >= 4 && TIS < 6) returnVal = 3347.10;
            else if (TIS >= 6 && TIS < 8) returnVal = 3468.90;
            else if (TIS >= 8 && TIS < 10) returnVal = 3678.00;
            else if (TIS >= 10 && TIS < 12) returnVal = 3795.60;
            else if (TIS >= 12 && TIS < 14) returnVal = 4004.70;
            else if (TIS >= 14 && TIS < 16) returnVal = 4178.70;
            else if (TIS >= 16 && TIS < 18) returnVal = 4297.50;
            else if (TIS >= 18 && TIS < 20) returnVal = 4423.80;
            else if (TIS >= 20 && TIS < 22) returnVal = 4472.70;
            else if (TIS >= 22 && TIS < 24) returnVal = 4637.10;
            else if (TIS >= 24 && TIS < 26) returnVal = 4725.30;
            else if (TIS >= 26) returnVal = 5061.30;
        }
        */


        return (returnVal / 30); // 1 MUTA = 1/30 basic pay for a month. DoD releases pay charts by month
    }



    // Returns Rank String for Rank Code
    public static String rankForCode(int rank) {
        switch (rank) {
            case 0: return "PVT";
            case 1: return "PV2";
            case 2: return "PFC";
            case 3: return "SPC";
            case 4: return "SGT";
            case 5: return "SSG";
            case 6: return "SFC";
            default: return "";
        }
    }


    // Static method for returning the 3 letter month string from month number
    public static String getStringForMonth(int month) {
        switch (month) {
            case 0: return "Jan";
            case 1: return "Feb";
            case 2: return "Mar";
            case 3: return "Apr";
            case 4: return "May";
            case 5: return "Jun";
            case 6: return "Jul";
            case 7: return "Aug";
            case 8: return "Sep";
            case 9: return "Oct";
            case 10: return "Nov";
            case 11: return "Dec";
            default: return "";
        }
    }


    // Static method for determining the amount of days in a month
    public static int daysInMonth(int month, int year) {

        // January
        if (month == 0) return 31;

            // February
        else if (month == 1) {
            boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
            if (isLeapYear) return 29;
            else return 28;
        }

        // March
        else if (month == 2) return 31;

            // April
        else if (month == 3) return 30;

            // May
        else if (month == 4) return 31;

            // June
        else if (month == 5) return 30;

            // July
        else if (month == 6) return 31;

            // August
        else if (month == 7) return 31;

            // September
        else if (month == 8) return 30;

            // October
        else if (month == 9) return 31;

            // November
        else if (month == 10) return 30;

            // December
        else if (month == 11) return 31;

        else return 0;
    }




    public static int getMUTAsFor(int ordersType, int[] startDate, int[] endDate) {

        // Orders Type 0 indicates MFH duty. Only 1 MUTA possible. Avoid all the kerfuffle and end this now.
        if (ordersType == 0) return 1;


        int MUTACount = 0;

        int startDay = startDate[0];
        int startMonth = startDate[1];
        int startYear = startDate[2];

        int endDay = endDate[0];
        int endMonth = endDate[1];
        int endYear = endDate[2];


        // Loop through each day until start and end dates match
        boolean dateMatched = false;
        while (!dateMatched) {
            // Thats another MUTA!
            MUTACount++;

            // Exit condition
            if (startDay == endDay && startMonth == endMonth && startYear == endYear)
                dateMatched = true;
            else {
                // Start condition not met, increase the day
                startDay++;

                // If the day exceeds the number of days in the month...
                if (startDay > RankAndTIS.daysInMonth(startMonth, startYear)) {
                    // reset the day and...
                    startDay = 1;
                    // increment the month!
                    startMonth++;
                    // If the month enters super December
                    if (startMonth > 11) {
                        // Reset the month...
                        startMonth = 0;
                        // And increment the year!
                        startYear++;
                    }
                }
            }
        }

        // Special case. 1 drill day counts for 2 MUTAs
        if (ordersType == 1) MUTACount = MUTACount * 2;

        return MUTACount;
    }


}
