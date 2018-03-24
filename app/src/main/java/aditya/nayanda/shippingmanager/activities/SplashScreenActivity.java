package aditya.nayanda.shippingmanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import aditya.nayanda.shippingmanager.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation_bottom/system bar) with user interaction.
 */
public class SplashScreenActivity extends Activity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 1500);
    }
}