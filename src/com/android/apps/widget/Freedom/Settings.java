package com.android.apps.widget.Freedom;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class Settings extends PreferenceActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, SharedPreferences.OnSharedPreferenceChangeListener {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final String MYLOG = "myLog";

    // ===========================================================
    // Fields
    // ===========================================================
    private CustomDatePickerDialog mCustomDatePickerDialog;
    private CustomTimePickerDialog mCustomTimePickerDialog;

    private SharedPreferences mSharedPreferences;
    private Preference datePicker, timePicker;
    private ListPreference currencyPicker;
    private EditTextPreference pack_cost, cigInAPack, cigPerDay;
    private MenuItem saveSett;
    private int appWidgetId;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        setResult(RESULT_CANCELED);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        datePicker = findPreference("datePicker");
        timePicker = findPreference("timePicker");
        currencyPicker = (ListPreference) findPreference("currency");
        pack_cost = (EditTextPreference) findPreference("pack_cost");
        cigInAPack = (EditTextPreference) findPreference("cig_in_a_pack");
        cigPerDay = (EditTextPreference) findPreference("cig_per_day");

        saveSett = (MenuItem) findViewById(R.id.saveSett);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isSettingsOk()) {
            timePicker.setSummary(
                    getSharedPreferences("date_time", MODE_WORLD_READABLE).getInt("hour", 0) + ":" +
                            getSharedPreferences("date_time", MODE_WORLD_READABLE).getInt("minutes", 0));

            datePicker.setSummary(
                    getSharedPreferences("date_time", MODE_WORLD_READABLE).getInt("day", 0) + "." +
                            (getSharedPreferences("date_time", MODE_WORLD_READABLE).getInt("month", 0) + 1) + "." +
                            getSharedPreferences("date_time", MODE_WORLD_READABLE).getInt("year", 0));

            currencyPicker.setSummary(mSharedPreferences.getString("currency", "error: null"));
            pack_cost.setSummary(mSharedPreferences.getString("pack_cost", "error: null"));
            cigPerDay.setSummary(mSharedPreferences.getString("cig_per_day", "error: null"));
            cigInAPack.setSummary(mSharedPreferences.getString("cig_in_a_pack", "error: null"));
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        final String key = preference.getKey();
        if ("datePicker".equals(key)) {
            mCustomDatePickerDialog = new CustomDatePickerDialog(this, this,
                    getSharedPreferences("date_time", MODE_WORLD_READABLE).getInt("year", 2000),
                    getSharedPreferences("date_time", MODE_WORLD_READABLE).getInt("month", 1),
                    getSharedPreferences("date_time", MODE_WORLD_READABLE).getInt("day", 1));
            mCustomDatePickerDialog.setTitle(getResources().getString(R.string.date_title));
            mCustomDatePickerDialog.show();
        } else if ("timePicker".equals(key)) {
            mCustomTimePickerDialog = new CustomTimePickerDialog(this, this,
                    getSharedPreferences("date_time", MODE_WORLD_READABLE).getInt("hours", 0),
                    getSharedPreferences("date_time", MODE_WORLD_READABLE).getInt("minutes", 0),
                    true);
            mCustomTimePickerDialog.setTitle(getResources().getString(R.string.time_title));
            mCustomTimePickerDialog.show();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        getSharedPreferences("date_time", MODE_WORLD_READABLE).edit().
                putInt("year", i).
                putInt("month", i1).
                putInt("day", i2).
                commit();

        this.datePicker.setSummary(i2 + "." + (i1 + 1) + "." + i);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        getSharedPreferences("date_time", MODE_WORLD_READABLE).edit().
                putInt("hour", i).
                putInt("minutes", i1).
                commit();

        this.timePicker.setSummary(i + ":" + i1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.smenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveSett:
                Intent result = new Intent();
                result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, result);
                finish();
//              TODO auto-update after prefs changed
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case "currency":
                currencyPicker.setSummary(
                        sharedPreferences.getString(s, getResources().getString(R.string.currency_summary)));
                break;
            case "pack_cost":
                pack_cost.setSummary(
                        sharedPreferences.getString(s, getResources().getString(R.string.pack_cost_summary)));
                break;
            case "cig_per_day":
                cigPerDay.setSummary(
                        sharedPreferences.getString(s, getResources().getString(R.string.cigarettes_per_day_summary)));
                break;
            case "cig_in_a_pack":
                cigInAPack.setSummary(sharedPreferences
                        .getString(s, getResources().getString(R.string.cigarettes_in_a_pack_summary)));
                break;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================
    private boolean isSettingsOk() {
        if (mSharedPreferences.contains("currency") || mSharedPreferences.contains("pack_cost") ||
                mSharedPreferences.contains("cig_per_day") || mSharedPreferences.contains("cig_in_a_pack")) {
            return true;
        }
        return false;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


}
