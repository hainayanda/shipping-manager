package aditya.nayanda.shippingmanager.fragments.main;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

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

    private static boolean isRunning(AsyncTask task) {
        if (task == null) return false;
        AsyncTask.Status status = task.getStatus();
        return status == AsyncTask.Status.RUNNING;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 9; i++) {
            jobs.add(Job.newDummyInstance(i));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = (LayoutInflater.from(getContext()));
        View view = inflater.inflate(R.layout.fragment_active_jobs, container, false);
        ListView jobListView = view.findViewById(R.id.list_active_jobs);
        jobListView.setAdapter(this);
        jobListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            AsyncTask<Object, Void, View[]> task;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                ListView listView = (ListView) absListView;
                int totalItem = listView.getCount();
                Log.i("INFO", "Scroll state change : lastVisiblePos " + listView.getLastVisiblePosition());
                Log.i("INFO", "Scroll state change : totalItem " + totalItem);
                Log.i("INFO", "Scroll state change : scrollState " + scrollState);
                if (scrollState == SCROLL_STATE_IDLE && listView.getLastVisiblePosition() == totalItem - 1) {
                    Log.i("INFO", "State pass");
                    if (!isRunning(task)) {
                        Log.i("INFO", "Running");
                        task = new ItemLoader();
                        task.execute(ActiveJobsFragment.this.getActivity(), totalItem, jobs, listView);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firsVisibleIndex, int visibleCount, int totalItem) {
            }
        });
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

    private static class ItemLoader extends AsyncTask<Object, Void, View[]> {

        @Override
        protected View[] doInBackground(Object[] params) {
            final ListView listView = (ListView) params[3];
            Activity activity = (Activity) params[0];

            ProgressBar progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleLarge);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(150, 150);
            layoutParams.gravity = Gravity.CENTER;
            progressBar.setLayoutParams(layoutParams);
            FrameLayout layout = new FrameLayout(activity);
            layout.addView(progressBar);

            activity.runOnUiThread(() -> {
                listView.addFooterView(layout);
            });

            List<Job> list = (List<Job>) params[2];
            int start = (Integer) params[1];
            int end = start + 9;
            for (int i = start; i < end; i++) {
                list.add(Job.newDummyInstance(i));
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.e("ERROR", e.toString());
            }
            return new View[]{listView, layout};
        }

        @Override
        protected void onPostExecute(View[] views) {
            ListView listView = (ListView) views[0];
            FrameLayout progressBar = (FrameLayout) views[1];
            listView.removeFooterView(progressBar);
            listView.invalidateViews();
        }
    }
}
