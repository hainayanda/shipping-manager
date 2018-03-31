package aditya.nayanda.shippingmanager.fragments.main;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.fragments.dialog.JobDetailsDialogFragment;
import aditya.nayanda.shippingmanager.model.Job;
import aditya.nayanda.shippingmanager.model.ListOfJobs;
import aditya.nayanda.shippingmanager.view.holder.ChildJobViewHolder;
import aditya.nayanda.shippingmanager.view.holder.ListOfJobViewHolder;

/**
 * Created by nayanda on 19/03/18.
 */

public class JobsHistoryFragment extends Fragment implements ExpandableListAdapter {

    private LayoutInflater inflater;
    private List<ListOfJobs> jobsHistory = new ArrayList<>();

    public static JobsHistoryFragment newInstance(Bundle args) {
        JobsHistoryFragment fragment = new JobsHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = (LayoutInflater.from(getContext()));
        View view = inflater.inflate(R.layout.fragment_pending_jobs, container, false);
        ExpandableListView pendingJobListView = view.findViewById(R.id.list_pending_jobs);
        pendingJobListView.setAdapter(this);
        setListListener(pendingJobListView);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceData) {
        super.onCreate(savedInstanceData);
        for (int i = 0; i <= 5; i++) {
            jobsHistory.add(ListOfJobs.newDummyHistoryInstance(i));
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return jobsHistory.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return jobsHistory.get(groupPosition).getTotalJobs();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return jobsHistory.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return jobsHistory.get(groupPosition).getJobById(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 10 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ListOfJobs listOfJobs = (ListOfJobs) getGroup(groupPosition);
        if (convertView != null) {
            ListOfJobViewHolder holder = (ListOfJobViewHolder) convertView.getTag();
            holder.apply(listOfJobs, isExpanded);
        } else {
            convertView = inflater.inflate(R.layout.content_list_of_jobs, null, false);
            ListOfJobViewHolder holder = new ListOfJobViewHolder(getContext(), convertView);
            ImageButton button = convertView.findViewById(R.id.content_start_job);
            button.setVisibility(View.GONE);
            convertView.setTag(holder);
            holder.apply(listOfJobs, isExpanded, R.raw.history_checklist);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Job job = (Job) getChild(groupPosition, childPosition);
        if (convertView != null) {
            ChildJobViewHolder holder = (ChildJobViewHolder) convertView.getTag();
            holder.apply(job, isLastChild);
        } else {
            convertView = inflater.inflate(R.layout.content_child_list_of_jobs, null, false);
            ChildJobViewHolder holder = new ChildJobViewHolder(getContext(), convertView);
            convertView.setTag(holder);
            holder.apply(job, isLastChild);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return jobsHistory.isEmpty();
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return getChildId((int) groupId, (int) childId);
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return getGroupId((int) groupId);
    }

    private void setListListener(ExpandableListView expandableList) {
        expandableList.setOnChildClickListener((expandableListView, view, groupPosition, childPosition, id) -> {
            JobsHistoryFragment adapter = (JobsHistoryFragment) expandableList.getExpandableListAdapter();
            Job job = (Job) adapter.getChild(groupPosition, childPosition);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            JobDetailsDialogFragment dialogFragment = JobDetailsDialogFragment.newInstance(0.9f, job);
            dialogFragment.show(fragmentManager, "reject_dialog");
            return true;
        });

        expandableList.setOnScrollListener(new AbsListView.OnScrollListener() {
            AsyncTask<Object, Void, Object[]> task;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                ExpandableListView newPendingJobs = (ExpandableListView) absListView;
                int totalItem = newPendingJobs.getCount();
                if (scrollState == SCROLL_STATE_IDLE && newPendingJobs.getLastVisiblePosition() == totalItem - 1) {
                    if (!isRunning(task)) {
                        task = new ItemLoader();
                        task.execute(JobsHistoryFragment.this, totalItem, jobsHistory, newPendingJobs);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firsVisibleIndex, int visibleCount, int totalItem) {
            }

            private boolean isRunning(AsyncTask task) {
                if (task == null) return false;
                AsyncTask.Status status = task.getStatus();
                return status == AsyncTask.Status.RUNNING;
            }
        });
    }

    private static class ItemLoader extends AsyncTask<Object, Void, Object[]> {
        private static FrameLayout setProgressBar(Activity activity, final ExpandableListView expandableListView) {
            ProgressBar progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleLarge);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(150, 150);
            layoutParams.gravity = Gravity.CENTER;
            progressBar.setLayoutParams(layoutParams);
            FrameLayout layout = new FrameLayout(activity);
            layout.addView(progressBar);
            activity.runOnUiThread(() -> {
                expandableListView.addFooterView(layout);
                expandableListView.setSelection(expandableListView.getCount());
            });
            return layout;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object[] doInBackground(Object[] params) {
            ExpandableListView expandableListView = (ExpandableListView) params[3];
            JobsHistoryFragment fragment = (JobsHistoryFragment) params[0];
            Activity activity = fragment.getActivity();
            FrameLayout layout = setProgressBar(activity, expandableListView);

            List<ListOfJobs> list = new ArrayList<>();
            list.addAll((List<ListOfJobs>) params[2]);

            int start = (Integer) params[1];
            int end = start + 5;
            for (int i = start; i < end; i++) {
                list.add(ListOfJobs.newDummyInstance(i));
            }
            try {
                Thread.sleep(1800);
            } catch (InterruptedException e) {
                Log.e("ERROR", e.toString());
            }
            return new Object[]{list, expandableListView, layout, fragment};
        }

        @Override
        protected void onPostExecute(Object[] results) {
            List<ListOfJobs> newList = (List<ListOfJobs>) results[0];
            ExpandableListView expandableListView = (ExpandableListView) results[1];
            JobsHistoryFragment fragment = (JobsHistoryFragment) results[3];
            fragment.jobsHistory = newList;
            FrameLayout progressBar = (FrameLayout) results[2];
            expandableListView.removeFooterView(progressBar);
            expandableListView.invalidateViews();
        }
    }
}
