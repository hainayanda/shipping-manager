package aditya.nayanda.shippingmanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.ImageView;

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
            ImageView logo = findViewById(R.id.app_logo);
            ImageView pertaminaLogo = findViewById(R.id.pertamina_logo);
            Pair<View, String> logoPair = new Pair<>(logo, "logo");
            Pair<View, String> pertaminaLogoPair = new Pair<>(pertaminaLogo, "pertamina_logo");
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(SplashScreenActivity.this, pertaminaLogoPair, logoPair);
            startActivity(i, options.toBundle());
            finish();
        }, 1500);
    }
}