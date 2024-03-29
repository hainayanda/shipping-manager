package aditya.nayanda.shippingmanager.activity.main;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activity.main.fragment.ActiveJobsFragment;
import aditya.nayanda.shippingmanager.activity.main.fragment.JobsHistoryFragment;
import aditya.nayanda.shippingmanager.activity.main.fragment.PendingJobsFragment;
import aditya.nayanda.shippingmanager.activity.main.fragment.UserMenuFragment;
import aditya.nayanda.shippingmanager.model.ListOfJobs;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationViewEx navigation;
    private ViewPager fragmentContainer;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        int index = fragmentContainer.getCurrentItem();
        if (index == 0) {
            super.onBackPressed();
            return;
        }
        selectBottomNavigationBy(0);
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
                getSupportActionBar().setTitle(R.string.title_active_jobs);
                return true;
            case 1:
                getSupportActionBar().setTitle(R.string.title_pending);
                return true;
            case 2:
                getSupportActionBar().setTitle(R.string.title_history);
                return true;
            case 3:
                getSupportActionBar().setTitle(R.string.title_user_menu);
                return true;
            default:
                return false;
        }
    }

    private ListOfJobs getJobsFromIntent() {
        try {
            ListOfJobs jobs = getIntent().getParcelableExtra("JOBS");
            return jobs;
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
        }
        return null;
    }

    protected class MainPagerAdapter extends FragmentPagerAdapter {

        MainPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("JOBS", getJobsFromIntent());
                    ActiveJobsFragment fragment = ActiveJobsFragment.newInstance(bundle);
                    fragment.setHasOptionsMenu(true);
                    return fragment;
                case 1:
                    return PendingJobsFragment.newInstance(new Bundle());
                case 2:
                    return JobsHistoryFragment.newInstance(new Bundle());
                default:
                    return UserMenuFragment.newInstance(new Bundle());
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
