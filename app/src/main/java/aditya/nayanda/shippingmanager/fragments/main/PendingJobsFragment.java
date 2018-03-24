package aditya.nayanda.shippingmanager.fragments.main;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.model.Job;
import aditya.nayanda.shippingmanager.model.ListOfJobs;
import aditya.nayanda.shippingmanager.view.holder.JobViewHolder;
import aditya.nayanda.shippingmanager.view.holder.ListOfJobViewHolder;

/**
 * Created by nayanda on 19/03/18.
 */

public class PendingJobsFragment extends Fragment implements ExpandableListAdapter {

    private LayoutInflater inflater;
    private List<ListOfJobs> pendingJobs = new ArrayList<>();

    public static PendingJobsFragment newInstance(Bundle args) {
        PendingJobsFragment fragment = new PendingJobsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = (LayoutInflater.from(getContext()));
        View view = inflater.inflate(R.layout.fragment_pending_jobs, container, false);
        ExpandableListView pendingJobListView = view.findViewById(R.id.list_pending_jobs);
        pendingJobListView.setAdapter(this);

//        setListListener(pendingJobListView);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceData) {
        super.onCreate(savedInstanceData);
        for (int i = 0; i <= 5; i++) {
            pendingJobs.add(ListOfJobs.newDummyInstance(i));
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
        return pendingJobs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return pendingJobs.get(groupPosition).getTotalJobs();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return pendingJobs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return pendingJobs.get(groupPosition).getJobById(childPosition);
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
            holder.apply(listOfJobs);
        } else {
            convertView = inflater.inflate(R.layout.content_list_of_jobs, null, false);
            ListOfJobViewHolder holder = new ListOfJobViewHolder(getContext(), convertView);
            Button button = convertView.findViewById(R.id.content_start_job);
            button.setFocusable(false);
            convertView.setTag(holder);
            holder.apply(listOfJobs);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Job job = (Job) getChild(groupPosition, childPosition);
        if (convertView != null) {
            JobViewHolder holder = (JobViewHolder) convertView.getTag();
            holder.apply(job);
        } else {
            convertView = inflater.inflate(R.layout.content_jobs, null, false);
            JobViewHolder holder = new JobViewHolder(getContext(), convertView);
            convertView.setTag(holder);
            holder.apply(job);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return pendingJobs.isEmpty();
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

//    private void setListListener(ListView pendingJobsListView) {
//    }
}
