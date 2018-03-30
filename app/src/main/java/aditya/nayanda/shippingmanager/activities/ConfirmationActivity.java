package aditya.nayanda.shippingmanager.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.fragments.dialog.ConfirmDialogFragment;
import aditya.nayanda.shippingmanager.fragments.dialog.RejectDialogFragment;
import aditya.nayanda.shippingmanager.model.Job;
import aditya.nayanda.shippingmanager.model.ListOfJobs;
import aditya.nayanda.shippingmanager.model.Receiver;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Job job = getJobExtras();
        applyView(job);

        findViewById(R.id.button_reject).setOnClickListener(view -> {
            ListOfJobs jobs = getJobsExtras();
            FragmentManager fragmentManager = getSupportFragmentManager();
            RejectDialogFragment dialogFragment = RejectDialogFragment.newInstance(0.9f, job, jobs);
            dialogFragment.show(fragmentManager, "reject_dialog");
        });
        findViewById(R.id.button_confirm).setOnClickListener(view -> {
            ListOfJobs jobs = getJobsExtras();
            FragmentManager fragmentManager = getSupportFragmentManager();
            ConfirmDialogFragment dialogFragment = ConfirmDialogFragment.newInstance(0.9f, job, jobs);
            dialogFragment.show(fragmentManager, "confirm_dialog");
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void applyView(Job job) {
        ImageView photo = findViewById(R.id.receiver_photo);
        TextView name = findViewById(R.id.receiver_name);
        TextView fullName = findViewById(R.id.receiver_detail);
        ImageView icon = findViewById(R.id.item_details_icon);
        TextView itemName = findViewById(R.id.item_name_details);
        TextView itemDetails = findViewById(R.id.item_detail_details);
        TextView itemAddress = findViewById(R.id.item_address_details);

        applyItemIcon(job, icon);

        Receiver receiver = job.getReceiver();
        applyReceiverPhoto(receiver, photo);
        name.setText(receiver.getLastName());
        fullName.setText(receiver.getFirstName() + " " + receiver.getLastName());
        itemName.setText(job.getItemName());
        itemDetails.setText(job.getItemDetail());
        itemAddress.setText(job.getAddress());

    }

    private void applyReceiverPhoto(Receiver receiver, ImageView photo) {
        String iconName;
        switch (receiver.getGender()) {
            case Female:
                iconName = "female_receiver";
                break;
            default:
                iconName = "male_receiver";
                break;
        }
        try {
            int id = this.getResources().getIdentifier(iconName,
                    "raw", this.getPackageName());
            InputStream in = this.getResources().openRawResource(id);
            Drawable drawable = Drawable.createFromStream(in, iconName);
            photo.setImageDrawable(drawable);
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
    }

    private void applyItemIcon(Job job, ImageView icon) {
        String iconName;
        switch (job.getType()) {
            case GAS:
                iconName = "gas";
                break;
            case OIL:
                iconName = "oil";
                break;
            case LUBE:
                iconName = "lube";
                break;
            case PETROCHEMICAL:
                iconName = "petrochemical";
                break;
            default:
                iconName = "other";
                break;
        }
        try {
            int id = this.getResources().getIdentifier(iconName,
                    "raw", this.getPackageName());
            InputStream in = this.getResources().openRawResource(id);
            Drawable drawable = Drawable.createFromStream(in, iconName);
            icon.setImageDrawable(drawable);
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
    }

    private Job getJobExtras() {
        try {
            return (Job) getIntent().getExtras().getParcelable("JOB");
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        return null;
    }

    private ListOfJobs getJobsExtras() {
        try {
            ListOfJobs jobs = getIntent().getParcelableExtra("JOBS");
            return jobs;
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        return null;
    }
}
