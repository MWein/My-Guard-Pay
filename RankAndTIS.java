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
    // TODO More Efficient Way to Do This
    public static int getYearsInService(int month, int year) {

        // Get current date
        Calendar now = Calendar.getInstance();
        int CurrentYear = now.get(Calendar.YEAR);
        int CurrentMonth = now.get(Calendar.MONTH);

        int diff = CurrentYear - year;
        if (month > CurrentMonth) diff--;

        return diff;
    }


    // Returns Pay for Rank and TIS
    public static double getPayPerMuta(int rank, int TIS) {

        // TODO Write this!!

        return 10.25;
    }



    // Returns Rank String for Rank Code
    // TODO Replace below code with switch
    public static String rankForCode(int rank) {

        // TODO Integrate this with Resources somehow

        String returnVal = "";
        if (rank == 0) returnVal = "PVT";
        else if (rank == 1) returnVal = "PV2";
        else if (rank == 2) returnVal = "PFC";
        else if (rank == 3) returnVal = "SPC";
        else if (rank == 4) returnVal = "SGT";
        else if (rank == 5) returnVal = "SSG";
        else if (rank == 6) returnVal = "SFC";
        return returnVal;
    }


    // Static method for returning the month string from month number
    // TODO Replace below code with Switch
    public static String getStringForMonth(int month) {

        // TODO Integrate this with Resources somehow

        // January
        if (month == 0) return "Jan";

            // February
        else if (month == 1) return "Feb";

        // March
        else if (month == 2) return "Mar";

            // April
        else if (month == 3) return "Apr";

            // May
        else if (month == 4) return "May";

            // June
        else if (month == 5) return "Jun";

            // July
        else if (month == 6) return "Jul";

            // August
        else if (month == 7) return "Aug";

            // September
        else if (month == 8) return "Sep";

            // October
        else if (month == 9) return "Oct";

            // November
        else if (month == 10) return "Nov";

            // December
        else if (month == 11) return "Dec";

        else return "";

    }


    // Static method for determining the amount of days in a month
    // TODO Replace below code with Switch
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




    // TODO Comment this shit out so noobs can follow
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
