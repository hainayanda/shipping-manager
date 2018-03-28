package aditya.nayanda.shippingmanager.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.fragments.dialog.helper.DialogHelper;
import aditya.nayanda.shippingmanager.model.Job;
import aditya.nayanda.shippingmanager.model.ListOfJobs;

/**
 * Created by nayanda on 24/03/18.
 */

public class ConfirmDialogFragment extends DialogFragment {

    public static ConfirmDialogFragment newInstance(float widthPercent, Job job, ListOfJobs jobs) {
        Bundle args = new Bundle();
        args.putFloat("width", widthPercent);
        args.putParcelable("JOBS", jobs);
        args.putParcelable("JOB", job);
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm, container);
        view.findViewById(R.id.button_submit_confirm).setOnClickListener(button -> {
            ListOfJobs jobs = getJobsArguments();
            Job job = getJobArguments();
            jobs.getJobs().remove(job);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            ContinueDialogFragment dialogFragment = ContinueDialogFragment.newInstance(0.9f, jobs);
            dialogFragment.show(fragmentManager, "continue_dialog");
            this.dismiss();
        });
        view.findViewById(R.id.button_cancel_confirm).setOnClickListener(button -> this.dismiss());
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

    private Job getJobArguments() {
        try {
            return (Job) getArguments().getParcelable("JOB");
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        return null;
    }

}
