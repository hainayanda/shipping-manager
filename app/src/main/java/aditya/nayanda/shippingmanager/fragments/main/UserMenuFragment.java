package aditya.nayanda.shippingmanager.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activities.ChangePasswordActivity;
import aditya.nayanda.shippingmanager.activities.LoginActivity;

/**
 * Created by nayanda on 19/03/18.
 */

public class UserMenuFragment extends Fragment {

    private LayoutInflater inflater;

    public static UserMenuFragment newInstance(Bundle args) {
        UserMenuFragment fragment = new UserMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_user_menu, container, false);
        Button buttonChangePass = view.findViewById(R.id.button_change_password);
        buttonChangePass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IntentChangePass = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(IntentChangePass);
            }
        });
        Button buttonLogout = view.findViewById(R.id.button_logout);
        buttonChangePass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IntentLogout = new Intent(getActivity(), LoginActivity.class);
                startActivity(IntentLogout);
            }
        });

        return view;
    }

}
