package aditya.nayanda.shippingmanager.fragments.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activities.MapsActivity;
import aditya.nayanda.shippingmanager.fragments.dialog.helper.DialogHelper;
import aditya.nayanda.shippingmanager.model.Job;

/**
 * Created by nayanda on 24/03/18.
 */

public class ContinueDialogFragment extends DialogFragment {

    public static ContinueDialogFragment newInstance(float widthPercent, Job[] jobs) {
        Bundle args = new Bundle();
        args.putFloat("width", widthPercent);
        args.putParcelableArray("JOBS", jobs);
        ContinueDialogFragment fragment = new ContinueDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_continue, container);
        view.findViewById(R.id.button_continue).setOnClickListener(button -> {
            Job[] jobs = getJobsArguments();
            Intent mapIntent = new Intent(getContext(), MapsActivity.class);
            mapIntent.putExtra("JOBS", jobs);
            startActivity(mapIntent);
        });
        view.findViewById(R.id.button_cancel_continue).setOnClickListener(button -> this.dismiss());
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

    private Job[] getJobsArguments() {
        try {
            return (Job[]) getArguments().getParcelableArray("JOBS");
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        return new Job[0];
    }
}
