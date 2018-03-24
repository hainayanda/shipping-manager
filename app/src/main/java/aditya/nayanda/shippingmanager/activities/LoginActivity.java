package aditya.nayanda.shippingmanager.activities;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import aditya.nayanda.shippingmanager.R;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(view -> {
//                attemptLogin();
            FakeLogin();
        });
    }

    private void FakeLogin() {
        Intent IntentHome = new Intent(this, MainActivity.class);
        startActivity(IntentHome);
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

