package aditya.nayanda.shippingmanager.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import aditya.nayanda.shippingmanager.fragments.main.ActiveJobsFragment;
import aditya.nayanda.shippingmanager.fragments.main.PendingJobsFragment;

/**
 * Created by nayanda on 22/03/18.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                return ActiveJobsFragment.newInstance(new Bundle());
            case 1:
                return PendingJobsFragment.newInstance(new Bundle());
            case 2:
                return ActiveJobsFragment.newInstance(new Bundle());
            case 3:
                return ActiveJobsFragment.newInstance(new Bundle());
            default:
                return ActiveJobsFragment.newInstance(new Bundle());
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
