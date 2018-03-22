package aditya.nayanda.shippingmanager.fragments.main;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.model.Job;
import aditya.nayanda.shippingmanager.view.holder.JobViewHolder;

/**
 * Created by nayanda on 19/03/18.
 */

public class ActiveJobsFragment extends Fragment implements ListAdapter {

    private List<Job> jobs = new LinkedList<>();
    private LayoutInflater inflater;

    public static ActiveJobsFragment newInstance(Bundle args) {
        ActiveJobsFragment fragment = new ActiveJobsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        for (int i = 0; i < 18; i++) {
            jobs.add(Job.newDummyInstance());
        }
        this.inflater = (LayoutInflater.from(getContext()));
        View view = inflater.inflate(R.layout.fragment_active_jobs, container, false);
        ListView jobListView = view.findViewById(R.id.list_active_jobs);
        jobListView.setAdapter(this);
        return view;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public int getCount() {
        return jobs.size();
    }

    @Override
    public Object getItem(int i) {
        return jobs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Job job = (Job) getItem(i);
        if (view != null) {
            JobViewHolder holder = (JobViewHolder) view.getTag();
            holder.apply(job);
        } else {
            view = inflater.inflate(R.layout.content_jobs, null, false);
            JobViewHolder holder = new JobViewHolder(getContext(), view);
            view.setTag(holder);
            holder.apply(job);
        }
        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return jobs.isEmpty();
    }
}
