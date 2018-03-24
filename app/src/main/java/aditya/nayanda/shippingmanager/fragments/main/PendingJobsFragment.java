package aditya.nayanda.shippingmanager.fragments.main;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import aditya.nayanda.shippingmanager.R;

/**
 * Created by nayanda on 19/03/18.
 */

public class PendingJobsFragment extends Fragment implements ListAdapter {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_jobs, container, false);
        ListView listview = view.findViewById(R.id.fragment_pending_jobs);
        listview.setAdapter(this);
        return view;
    }

//    private class ListViewHolder{
//        private ListViewHolder(){
//            View view = View.inflate(getContext(), R.layout.content_jobs, parent);
//
//        }
//    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView != null) return convertView;
//        View view = View.inflate(getContext(), R.layout.content_jobs, parent);
//        TextView deliveryNumber = view.findViewById(R.id.jobs_delivery_number);
//        TextView shipToParty = view.findViewById(R.id.jobs_party_ship_to);
//        deliveryNumber.setText("12345");
//        shipToParty.setText("PT. ABCD");
//        return view;
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
