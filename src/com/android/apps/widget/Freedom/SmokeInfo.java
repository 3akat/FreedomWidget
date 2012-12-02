package com.android.apps.widget.Freedom;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.Period;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SmokeInfo {
    // ===========================================================
    // Constants
    // ===========================================================
    private static final int minInDay = 1440;

    // ===========================================================
    // Fields
    // ===========================================================
    private static int cigNotSmoked;
    private static double moneySaved;
    private static SharedPreferences sPref;

    private static LocalDateTime mLDateToday, mLDateStop;
    private static Period mPeriod;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public static Period getPeriod(Context context) {
        sPref = context.getSharedPreferences("date_time", Context.MODE_WORLD_READABLE);

        mLDateToday = new LocalDateTime();
        mLDateStop =
                new LocalDateTime(
                        sPref.getInt("year", 2000),
                        sPref.getInt("month", 1),
                        sPref.getInt("day", 1),
                        sPref.getInt("hour", 12),
                        sPref.getInt("minutes", 30));
        mPeriod = new Period(mLDateStop, mLDateToday);

        return mPeriod;
    }

    public static String getMoney(Context context) {
        sPref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        float costPerCig = Float.parseFloat(sPref.getString("pack_cost", "")) / Float.parseFloat(
                sPref.getString("cig_in_a_pack", ""));
        float cigPerMin = Float.parseFloat(sPref.getString("cig_per_day", "0")) / minInDay;
        Minutes minutes = Minutes.minutesBetween(mLDateStop, mLDateToday);

        float moneySaved = minutes.getMinutes() * cigPerMin * costPerCig;

//      output formatting
        try {
            int pack_cost = Integer.parseInt(sPref.getString("pack_cost", ""));
            return new BigDecimal(moneySaved).setScale(0, RoundingMode.HALF_UP) +" "+ sPref.getString("currency", "");
        } catch (NumberFormatException e) {
            float pack_cost = Float.parseFloat(sPref.getString("pack_cost", "0"));
            return new BigDecimal(moneySaved).setScale(2, RoundingMode.HALF_UP) + " " + sPref.getString("currency", "");
        }
    }

    public static String getCigCount(Context context) {
        sPref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        float cigPerMin = Float.parseFloat(sPref.getString("cig_per_day", "0")) / minInDay;
        Minutes minutes = Minutes.minutesBetween(mLDateStop, mLDateToday);

        return  Math.round(cigPerMin*minutes.getMinutes())+"";
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


}
