package aditya.nayanda.shippingmanager.activity.secondary.fragment.dialog;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activity.secondary.fragment.dialog.helper.DialogHelper;
import aditya.nayanda.shippingmanager.model.Job;
import aditya.nayanda.shippingmanager.model.Receiver;

public class JobDetailsDialogFragment extends DialogFragment {

    public static JobDetailsDialogFragment newInstance(float width, Job job) {
        Bundle args = new Bundle();
        args.putParcelable("JOB", job);
        args.putFloat("width", width);
        JobDetailsDialogFragment fragment = new JobDetailsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_job_details, container, false);
        final Job job = getJobExtras();
        applyView(view, job);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            float width = getArguments().getFloat("width");
            DialogHelper.setDialogWidthPercentage(width, getDialog(), getActivity());
        } catch (NullPointerException | IllegalArgumentException e) {
            Log.e("ERROR", e.toString());
        }
    }

    private void applyView(View view, Job job) {
        ImageView photo = view.findViewById(R.id.receiver_photo);
        TextView name = view.findViewById(R.id.receiver_name);
        TextView fullName = view.findViewById(R.id.receiver_detail);
        ImageView icon = view.findViewById(R.id.item_details_icon);
        TextView itemName = view.findViewById(R.id.item_name_details);
        TextView itemDetails = view.findViewById(R.id.item_detail_details);
        TextView itemAddress = view.findViewById(R.id.item_address_details);
        ImageButton closedButton = view.findViewById(R.id.closed_button);

        applyItemIcon(job, icon);

        Receiver receiver = job.getReceiver();
        applyReceiverPhoto(receiver, photo);
        name.setText(receiver.getLastName());
        fullName.setText(receiver.getFirstName() + " " + receiver.getLastName());
        itemName.setText(job.getItemName());
        itemDetails.setText(job.getItemDetail());
        itemAddress.setText(job.getAddress());
        closedButton.setOnClickListener(view1 -> JobDetailsDialogFragment.this.dismiss());

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
                    "raw", this.getContext().getPackageName());
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
                    "raw", this.getContext().getPackageName());
            InputStream in = this.getResources().openRawResource(id);
            Drawable drawable = Drawable.createFromStream(in, iconName);
            icon.setImageDrawable(drawable);
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
    }

    private Job getJobExtras() {
        try {
            return (Job) getArguments().getParcelable("JOB");
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        return null;
    }
}
