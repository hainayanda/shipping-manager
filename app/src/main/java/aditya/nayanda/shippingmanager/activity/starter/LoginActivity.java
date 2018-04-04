package aditya.nayanda.shippingmanager.activity.starter;

import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activity.main.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final AutoCompleteTextView loginForm = findViewById(R.id.email);
        final EditText passwordForm = findViewById(R.id.password);
        final ProgressBar progressBar = findViewById(R.id.login_progress);
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                if (loginForm.getText().toString().equals(passwordForm.getText().toString())) {
                    FakeLogin();
                } else {
                    new AlertDialog.Builder(LoginActivity.this).setTitle("Failed")
                            .setMessage("Failed to log you in").setPositiveButton("OK",
                            (dialogInterface, i) -> dialogInterface.dismiss()).show();
                }
            }, 1500);
        });
    }

    private void FakeLogin() {
        Intent intentHome = new Intent(this, MainActivity.class);
        intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentHome);
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

