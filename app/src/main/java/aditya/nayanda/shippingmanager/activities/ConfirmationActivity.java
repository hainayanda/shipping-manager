package aditya.nayanda.shippingmanager.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activities.helper.ActivityHelper;
import aditya.nayanda.shippingmanager.fragments.dialog.RejectDialogFragment;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        String title = getSupportActionBar().getTitle().toString();
        ActivityHelper.setCustomActionBarWith(title, this);
        findViewById(R.id.button_reject).setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            RejectDialogFragment dialogFragment = RejectDialogFragment.newInstance(0.9f);
            dialogFragment.show(fragmentManager, "reject_dialog");
        });
    }
}
