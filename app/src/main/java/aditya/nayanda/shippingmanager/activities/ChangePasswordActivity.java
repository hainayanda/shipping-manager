package aditya.nayanda.shippingmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import aditya.nayanda.shippingmanager.R;


/**
 * Created by mongk on 3/25/2018.
 */

public class ChangePasswordActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getIntent().getExtras().getParcelable("AGENT");
        TextView oldPass = findViewById(R.id.old_password);
        TextView newPass = findViewById(R.id.new_password);
        TextView confPass = findViewById(R.id.confirm_password);

        Button confirmButton = findViewById(R.id.button_change_password);
        confirmButton.setOnClickListener(v -> {
            
        });

    }

}
