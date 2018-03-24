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
import aditya.nayanda.shippingmanager.util.Utilities;

/**
 * Created by nayanda on 24/03/18.
 */

public class RejectDialogFragment extends DialogFragment {

    public static RejectDialogFragment newInstance(float widthPercent, Job job, Job[] jobs) {
        Bundle args = new Bundle();
        args.putFloat("width", widthPercent);
        args.putParcelableArray("JOBS", jobs);
        args.putParcelable("JOB", job);
        RejectDialogFragment fragment = new RejectDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_reject, container);
        view.findViewById(R.id.button_submit_reject).setOnClickListener(button -> {
            Job[] jobs = getJobsArguments();
            Job job = getJobArguments();
            jobs = Utilities.createArrayWithout(job, jobs);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            ContinueDialogFragment dialogFragment = ContinueDialogFragment.newInstance(0.9f, jobs);
            dialogFragment.show(fragmentManager, "continue_dialog");
            this.dismiss();
        });
        view.findViewById(R.id.button_cancel_reject).setOnClickListener(button -> this.dismiss());
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

    private Job getJobArguments() {
        try {
            return (Job) getArguments().getParcelable("JOB");
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        return null;
    }
}
