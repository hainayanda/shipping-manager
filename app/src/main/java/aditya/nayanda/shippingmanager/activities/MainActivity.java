package aditya.nayanda.shippingmanager.activities;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activities.helper.ActivityHelper;
import aditya.nayanda.shippingmanager.adapter.MainPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationViewEx navigation;
    private ViewPager fragmentContainer;
    private TextView actionBarTitle;
    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener
            = item -> {
        int index = -1;
        switch (item.getItemId()) {
            case R.id.navigation_active_jobs:
                index = 0;
                break;
            case R.id.navigation_pending:
                index = 1;
                break;
            case R.id.navigation_history:
                index = 2;
                break;
            case R.id.navigation_user_menu:
                index = 3;
                break;
            default:
                break;
        }
        return selectBottomNavigationBy(index);
    };
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (setTitleByIndex(position)) navigation.getMenu().getItem(position).setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    //BELOW IS CODE RELATED TO BOTTOM NAVIGATION

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBarTitle = ActivityHelper.setToCustomActionBar(this);
        setContentView(R.layout.activity_main);
        fragmentContainer = findViewById(R.id.main_frame);
        fragmentContainer.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        fragmentContainer.addOnPageChangeListener(pageChangeListener);

        navigation = findViewById(R.id.navigation_bottom);
        navigation.enableShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(itemSelectedListener);

        int navigationIndex = getIntentIndex();
        navigation.getMenu().getItem(navigationIndex).setChecked(true);
        selectBottomNavigationBy(navigationIndex);
    }

    private int getIntentIndex() {
        try {
            Integer index = getIntent().getExtras().getInt("INDEX");
            if (index < 4 && index >= 0) return index;
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        return 0;
    }

    private boolean selectBottomNavigationBy(int index) {
        boolean isSuccess = setTitleByIndex(index);
        if (isSuccess) fragmentContainer.setCurrentItem(index, true);
        return isSuccess;
    }

    boolean setTitleByIndex(int index) {
        switch (index) {
            case 0:
                actionBarTitle.setText(R.string.title_active_jobs);
                return true;
            case 1:
                actionBarTitle.setText(R.string.title_pending);
                return true;
            case 2:
                actionBarTitle.setText(R.string.title_history);
                return true;
            case 3:
                actionBarTitle.setText(R.string.title_user_menu);
                return true;
            default:
                return false;
        }
    }
}
