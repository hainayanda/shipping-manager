package aditya.nayanda.shippingmanager.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activities.ChangePasswordActivity;
import aditya.nayanda.shippingmanager.fragments.dialog.LogoutDialogFragment;
import aditya.nayanda.shippingmanager.model.Agent;

/**
 * Created by nayanda on 19/03/18.
 */

public class UserMenuFragment extends Fragment {

    public static UserMenuFragment newInstance(Bundle args) {
        UserMenuFragment fragment = new UserMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Agent agent = Agent.newDummyInstance();
        View view = inflater.inflate(R.layout.fragment_user_menu, container, false);

        TextView userEmployeeId = view.findViewById(R.id.employee_id);
        userEmployeeId.setText(agent.getEmployeeId());

        TextView agentName = view.findViewById(R.id.agent_name);
        TextView nameUser = view.findViewById(R.id.full_name);
        nameUser.setText(agent.getFirstName() + " " + agent.getLastName());
        agentName.setText(agent.getFirstName() + " " + agent.getLastName());

        TextView phone = view.findViewById(R.id.phone);
        phone.setText(agent.getUserPhone());

        TextView email = view.findViewById(R.id.email);
        email.setText(agent.getUserEmail());

        Button buttonChangePass = view.findViewById(R.id.button_change_password);
        buttonChangePass.setOnClickListener(view1 -> {
            Intent intentChangePass = new Intent(getActivity(), ChangePasswordActivity.class);
            intentChangePass.putExtra("AGENT", agent);
            startActivity(intentChangePass);
        });
        Button buttonLogout = view.findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            LogoutDialogFragment dialogFragment = LogoutDialogFragment.newInstance(0.8f);
            dialogFragment.show(fragmentManager, "logout_dialog");
        });

        return view;
    }
}
