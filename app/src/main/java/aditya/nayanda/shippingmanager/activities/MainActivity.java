package aditya.nayanda.shippingmanager.activities;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import aditya.nayanda.shippingmanager.R;

public class MainActivity extends AppCompatActivity {

    private int navigationIndex = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_active_jobs:
                return selectBottomNavigationBy(0);
            case R.id.navigation_pending:
                return selectBottomNavigationBy(1);
            case R.id.navigation_history:
                return selectBottomNavigationBy(2);
            case R.id.navigation_user_menu:
                return selectBottomNavigationBy(3);
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationIndex = getIntentIndex();
        selectBottomNavigationBy(navigationIndex);

        BottomNavigationViewEx navigation = findViewById(R.id.navigation);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(itemSelectedListener);
    }

    private int getIntentIndex() {
        try {
            Integer index = getIntent().getExtras().getInt("INDEX");
            if (index < 4 && index > 0) return index;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean selectBottomNavigationBy(int index) {
        switch (index) {
            case 0:
                setTitle(R.string.title_active_jobs);
                return true;
            case 1:
                setTitle(R.string.title_pending);
                return true;
            case 2:
                setTitle(R.string.title_user_menu);
                return true;
            case 3:
                setTitle(R.string.title_history);
                return true;
            default:
                return false;
        }
    }
}
