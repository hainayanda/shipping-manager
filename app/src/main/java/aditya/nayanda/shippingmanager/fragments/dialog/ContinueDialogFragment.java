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
import aditya.nayanda.shippingmanager.activities.MainActivity;
import aditya.nayanda.shippingmanager.activities.MapsActivity;
import aditya.nayanda.shippingmanager.fragments.dialog.helper.DialogHelper;
import aditya.nayanda.shippingmanager.model.ListOfJobs;

/**
 * Created by nayanda on 24/03/18.
 */

public class ContinueDialogFragment extends DialogFragment {

    public static ContinueDialogFragment newInstance(float widthPercent, ListOfJobs jobs) {
        Bundle args = new Bundle();
        args.putFloat("width", widthPercent);
        args.putParcelable("JOBS", jobs);
        ContinueDialogFragment fragment = new ContinueDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_continue, container);
        view.findViewById(R.id.button_continue).setOnClickListener(button -> {
            ListOfJobs jobs = getJobsArguments();
            Intent mapIntent = new Intent(getContext(), MapsActivity.class);
            mapIntent.putExtra("JOBS", jobs);
            mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mapIntent);
            getActivity().finish();
        });
        view.findViewById(R.id.button_cancel_continue).setOnClickListener(button -> {
            ListOfJobs jobs = getJobsArguments();
            Intent mainIntent = new Intent(getContext(), MainActivity.class);
            mainIntent.putExtra("JOBS", jobs);
            mainIntent.putExtra("INDEX", 0);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            getActivity().finish();
        });
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

    private ListOfJobs getJobsArguments() {
        try {
            ListOfJobs jobs = getArguments().getParcelable("JOBS");
            return jobs;
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        return null;
    }
}
