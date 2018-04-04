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
import aditya.nayanda.shippingmanager.activities.LoginActivity;
import aditya.nayanda.shippingmanager.fragments.dialog.helper.DialogHelper;

/**
 * Created by nayanda on 31/03/18.
 */

public class LogoutDialogFragment extends DialogFragment {

    public static LogoutDialogFragment newInstance(float widthPercent) {
        Bundle args = new Bundle();
        args.putFloat("width", widthPercent);
        LogoutDialogFragment fragment = new LogoutDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_logout, container);
        view.findViewById(R.id.button_continue).setOnClickListener(button -> {
            Intent intentLogout = new Intent(getActivity(), LoginActivity.class);
            startActivity(intentLogout);
            getActivity().finish();
        });
        view.findViewById(R.id.button_cancel_continue).setOnClickListener(button -> {
            this.dismiss();
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
}
