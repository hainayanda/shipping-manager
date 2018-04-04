package aditya.nayanda.shippingmanager.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activities.main.MainActivity;
import aditya.nayanda.shippingmanager.model.Agent;


/**
 * Created by mongk on 3/25/2018.
 */

public class ChangePasswordActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_change_password);
        Agent agent = getIntent().getExtras().getParcelable("AGENT");
        TextView oldPass = findViewById(R.id.old_password);
        TextView newPass = findViewById(R.id.new_password);
        TextView confPass = findViewById(R.id.confirm_password);

        Button confirmButton = findViewById(R.id.button_change_password);
        confirmButton.setOnClickListener(v -> {
            String oldPassUser = oldPass.getText().toString();
            String newPassUser = newPass.getText().toString();
            String confPassUser = confPass.getText().toString();
            if (oldPassUser.equals(agent.getUserPassword()) && confPassUser.equals(newPassUser)) {
                agent.setUserPassword(confPassUser);
                Intent intentChangePass = new Intent(MainActivity.ChangePasswordActivity.this, MainActivity.class);
                startActivity(intentChangePass);
            } else if (newPassUser.equals(" ")) {
                new AlertDialog.Builder(MainActivity.ChangePasswordActivity.this).setTitle("Failed")
                        .setMessage("Your password can't be empty").setPositiveButton("OK",
                        (dialogInterface, i) -> dialogInterface.dismiss()).show();
            } else {
                new AlertDialog.Builder(MainActivity.ChangePasswordActivity.this).setTitle("Failed")
                        .setMessage("Your password is incorrect").setPositiveButton("OK",
                        (dialogInterface, i) -> dialogInterface.dismiss()).show();
            }

        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
