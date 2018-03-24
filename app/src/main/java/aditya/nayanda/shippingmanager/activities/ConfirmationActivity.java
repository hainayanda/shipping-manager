package aditya.nayanda.shippingmanager.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activities.helper.ActivityHelper;
import aditya.nayanda.shippingmanager.fragments.dialog.ConfirmDialogFragment;
import aditya.nayanda.shippingmanager.fragments.dialog.RejectDialogFragment;
import aditya.nayanda.shippingmanager.model.Job;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        String title = getSupportActionBar().getTitle().toString();
        ActivityHelper.setCustomActionBarWith(title, this);
        findViewById(R.id.button_reject).setOnClickListener(view -> {
            Job[] jobs = getJobsExtras();
            FragmentManager fragmentManager = getSupportFragmentManager();
            RejectDialogFragment dialogFragment = RejectDialogFragment.newInstance(0.9f, jobs);
            dialogFragment.show(fragmentManager, "reject_dialog");
        });
        findViewById(R.id.button_confirm).setOnClickListener(view -> {
            Job[] jobs = getJobsExtras();
            FragmentManager fragmentManager = getSupportFragmentManager();
            ConfirmDialogFragment dialogFragment = ConfirmDialogFragment.newInstance(0.9f, jobs);
            dialogFragment.show(fragmentManager, "confirm_dialog");
        });
    }

    private Job[] getJobsExtras() {
        try {
            return (Job[]) getIntent().getExtras().getParcelableArray("JOBS");
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        return new Job[0];
    }
}
