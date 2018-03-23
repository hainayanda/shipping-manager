package aditya.nayanda.shippingmanager.activities.helper;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import aditya.nayanda.shippingmanager.R;

/**
 * Created by nayanda on 22/03/18.
 */

public class ActivityHelper {

    private ActivityHelper() {
    }

    public static TextView setToCustomActionBar(Activity activity) {
        if (activity instanceof AppCompatActivity)
            return setToCustomActionBar((AppCompatActivity) activity);
        try {
            ActionBar actionBar = activity.getActionBar();
            actionBar.setCustomView(R.layout.bar_custom_action_bar);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(android.R.color.transparent);
            actionBar.setElevation(0);
            Toolbar toolbar = (Toolbar) actionBar.getCustomView().getParent();
            toolbar.setContentInsetsAbsolute(0, 0);
            toolbar.getContentInsetEnd();
            toolbar.setPadding(0, 0, 0, 0);
            return actionBar.getCustomView().findViewById(R.id.action_bar_title);
        } catch (NullPointerException e) {
            Log.e("ERROR", "error to set custom action bar :\n" + e.toString());
            return null;
        }
    }

    public static TextView setToCustomActionBar(AppCompatActivity activity) {
        try {
            android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setCustomView(R.layout.bar_custom_action_bar);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(android.R.color.transparent);
            actionBar.setElevation(0);
            android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) actionBar.getCustomView().getParent();
            toolbar.setContentInsetsAbsolute(0, 0);
            toolbar.getContentInsetEnd();
            toolbar.setPadding(0, 0, 0, 0);
            return actionBar.getCustomView().findViewById(R.id.action_bar_title);
        } catch (NullPointerException e) {
            Log.e("ERROR", "error to set custom action bar :\n" + e.toString());
            return null;
        }
    }
}
