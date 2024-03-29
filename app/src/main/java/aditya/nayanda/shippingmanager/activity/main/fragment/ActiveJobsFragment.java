package aditya.nayanda.shippingmanager.activity.main.fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import aditya.nayanda.shippingmanager.R;
import aditya.nayanda.shippingmanager.activity.secondary.ConfirmationActivity;
import aditya.nayanda.shippingmanager.activity.secondary.MapsActivity;
import aditya.nayanda.shippingmanager.model.Job;
import aditya.nayanda.shippingmanager.model.ListOfJobs;
import aditya.nayanda.shippingmanager.view.holder.JobViewHolder;

/**
 * Created by nayanda on 19/03/18.
 */

public class ActiveJobsFragment extends Fragment implements ListAdapter {

    private ListOfJobs jobs;
    private LayoutInflater inflater;

    public static ActiveJobsFragment newInstance(Bundle args) {
        ActiveJobsFragment fragment = new ActiveJobsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobs = getJobs();
    }

    private ListOfJobs getJobs() {
        if (jobs != null) {
            if (jobs.getJobs().size() > 0) return jobs;
        }
        try {
            ListOfJobs jobs = getArguments().getParcelable("JOBS");
            if (jobs == null) jobs = ListOfJobs.newDummyInstance(0);
            return jobs;
        } catch (NullPointerException e) {
            Log.e("ERROR", e.toString());
            return ListOfJobs.newDummyInstance(0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = (LayoutInflater.from(getContext()));
        View view = inflater.inflate(R.layout.fragment_active_jobs, container, false);
        ListView jobListView = view.findViewById(R.id.list_active_jobs);
        jobListView.setAdapter(this);
        setListListener(jobListView);
        return view;
    }

    //BELOW IS CODE RELATED TO LIST ADAPTER

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
        return jobs.getTotalJobs();
    }

    @Override
    public Object getItem(int i) {
        return jobs.getJobs().get(i);
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
        return jobs.getJobs().isEmpty();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_active_jobs, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_go_to_map) {
            Intent mapIntent = new Intent(getContext(), MapsActivity.class);
            mapIntent.putExtra("JOBS", jobs);
            startActivity(mapIntent);
            getActivity().finish();
        } else {
            ((AppCompatActivity) getActivity()).onSupportNavigateUp();
        }
        return true;
    }

    private void setListListener(ListView jobListView) {
        /**
         jobListView.setOnScrollListener(new AbsListView.OnScrollListener() {

         AsyncTask<Object, Void, Object[]> task;

         @Override public void onScrollStateChanged(AbsListView absListView, int scrollState) {
         ListView listView = (ListView) absListView;
         int totalItem = listView.getCount();
         if (scrollState == SCROLL_STATE_IDLE && listView.getLastVisiblePosition() == totalItem - 1) {
         if (!isRunning(task)) {
         task = new ItemLoader();
         task.execute(ActiveJobsFragment.this, totalItem, jobs, listView);
         }
         }
         }

         @Override public void onScroll(AbsListView view, int firsVisibleIndex, int visibleCount, int totalItem) {
         }

         private boolean isRunning(AsyncTask task) {
         if (task == null) return false;
         AsyncTask.Status status = task.getStatus();
         return status == AsyncTask.Status.RUNNING;
         }
         });
         **/
        jobListView.setOnItemClickListener((adapterView, view, position, id) -> {
            ActiveJobsFragment adapter = (ActiveJobsFragment) adapterView.getAdapter();
            Job job = (Job) adapter.getItem(position);
            ListOfJobs jobs = adapter.getJobs();
            Intent jobDetailsIntent = new Intent(ActiveJobsFragment.this.getContext(), ConfirmationActivity.class);
            jobDetailsIntent.putExtra("JOB", job);
            jobDetailsIntent.putExtra("JOBS", jobs);
            ImageView itemIcon = ((JobViewHolder) view.getTag()).getItemIcon();
            TextView itemName = ((JobViewHolder) view.getTag()).getItemName();
            Pair<View, String> iconPair = new Pair<>(itemIcon, "item_icon");
            Pair<View, String> itemNamePair = new Pair<>(itemName, "item_name");
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(ActiveJobsFragment.this.getActivity(),
                            iconPair, itemNamePair);
            startActivity(jobDetailsIntent, options.toBundle());
        });
    }

    /**
     private static class ItemLoader extends AsyncTask<Object, Void, Object[]> {

     private static FrameLayout setProgressBar(Activity activity, final ListView listView) {
     ProgressBar progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleLarge);
     FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(150, 150);
     layoutParams.gravity = Gravity.CENTER;
     progressBar.setLayoutParams(layoutParams);
     FrameLayout layout = new FrameLayout(activity);
     layout.addView(progressBar);
     activity.runOnUiThread(() -> {
     listView.addFooterView(layout);
     listView.setSelection(listView.getCount());
     });
     return layout;
     }

     @Override protected void onPreExecute() {
     super.onPreExecute();
     }

     @Override protected Object[] doInBackground(Object[] params) {
     ListView listView = (ListView) params[3];
     ActiveJobsFragment fragment = (ActiveJobsFragment) params[0];
     Activity activity = fragment.getActivity();
     FrameLayout layout = setProgressBar(activity, listView);

     List<Job> list = new ArrayList<>();
     list.addAll((List<Job>) params[2]);

     int start = (Integer) params[1];
     int end = start + 9;
     for (int i = start; i < end; i++) {
     list.add(Job.newDummyInstance(i));
     }
     try {
     Thread.sleep(1800);
     } catch (InterruptedException e) {
     Log.e("ERROR", e.toString());
     }
     return new Object[]{list, listView, layout, fragment};
     }

     @Override protected void onPostExecute(Object[] results) {
     List<Job> newList = (List<Job>) results[0];
     ListView listView = (ListView) results[1];
     ActiveJobsFragment fragment = (ActiveJobsFragment) results[3];
     fragment.jobs = newList;
     FrameLayout progressBar = (FrameLayout) results[2];
     listView.removeFooterView(progressBar);
     listView.invalidateViews();
     }
     }
     **/
}
