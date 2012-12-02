package com.android.apps.widget.Freedom;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import org.joda.time.Period;

public class Widget extends AppWidgetProvider {

    // ===========================================================
    // Constants
    // ===========================================================
    public static final String WIDGET_BTN_SETT_PUSH = "OPEN_SETTINGS";
    public static final String WIDGET_BTN_UPD_PUSH = "BTN_UPD";

    // ===========================================================
    // Fields
    // ===========================================================
    private static RemoteViews remoteViews;
    private static ComponentName component;
    private static PendingIntent pushUpdate, pushSettings;
    private static StringBuffer sb;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        Intent intent = new Intent(context, Widget.class);

        intent.setAction(WIDGET_BTN_UPD_PUSH);
        pushUpdate = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btnUpd, pushUpdate);

        intent.setAction(WIDGET_BTN_SETT_PUSH);
        pushSettings = PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.btnSett, pushSettings);

        remoteViews.setTextViewText(R.id.tvPeriod, periodToString(SmokeInfo.getPeriod(context))+" ");
        remoteViews.setTextViewText(R.id.tvMoney, SmokeInfo.getMoney(context) + " ");
        remoteViews.setTextViewText(R.id.tvCigCount, SmokeInfo.getCigCount(context) + " "+"Cig ");

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        Log.e("action",action);

//      update widget layout
        if (WIDGET_BTN_UPD_PUSH.equals(action)) {
            component = new ComponentName(context, Widget.class);
            remoteViews.setTextViewText(R.id.tvPeriod, periodToString(SmokeInfo.getPeriod(context))+" ");
            remoteViews.setTextViewText(R.id.tvMoney, SmokeInfo.getMoney(context) + " ");
            remoteViews.setTextViewText(R.id.tvCigCount, SmokeInfo.getCigCount(context) + " ");
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(component, remoteViews);

            Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show();
        }
//      start settings activity
        if (WIDGET_BTN_SETT_PUSH.equals(action)) {
            Intent push = new Intent(context, Settings.class);
            push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(push);
        }
        super.onReceive(context, intent);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    /*
     * Creating period output
     */
    private String periodToString(Period period) {
        sb = new StringBuffer();
        if (period.getMinutes() != 0) {
            sb.insert(0, period.getMinutes() + "Min");
            if (period.getHours() != 0) {
                sb.insert(0, period.getHours() + "H:");
                if (period.getDays() != 0) {
                    sb.insert(0, period.getDays() + "D:");
                    if (period.getMonths() != 0) {
                        sb.insert(0, period.getMonths() + "M:");
                        if (period.getYears() != 0) {
                            sb.insert(0, period.getYears() + "Y:");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
