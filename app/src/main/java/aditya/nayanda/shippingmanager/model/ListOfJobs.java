package aditya.nayanda.shippingmanager.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by adity on 3/24/2018.
 */

public class ListOfJobs implements Parcelable {

    public static final Creator<ListOfJobs> CREATOR = new Creator<ListOfJobs>() {
        @Override
        public ListOfJobs createFromParcel(Parcel in) {
            return new ListOfJobs(in);
        }

        @Override
        public ListOfJobs[] newArray(int size) {
            return new ListOfJobs[size];
        }
    };
    private String shipmentNumber;
    private WareHouse wareHouse;
    private List<Job> jobs;

    private ListOfJobs(String shipmentNumber, WareHouse wareHouse, @NonNull List<Job> jobs) {
        this.shipmentNumber = shipmentNumber;
        this.jobs = jobs;
        this.wareHouse = wareHouse;
    }

    protected ListOfJobs(Parcel in) {
        shipmentNumber = in.readString();
        wareHouse = in.readParcelable(WareHouse.class.getClassLoader());
        jobs = in.createTypedArrayList(Job.CREATOR);
    }

    public static ListOfJobs newDummyInstance(int i) {
        String number;
        number = String.format("%010d", i);
        int totalJobs = new Random().nextInt(6) + 3;
        List<Job> jobs = new ArrayList<>(totalJobs);
        for (int j = 0; j < totalJobs; j++) {
            jobs.add(Job.newDummyHistoryInstance(j));
        }
        return new ListOfJobs(number, WareHouse.newDummyInstance(), jobs);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shipmentNumber);
        dest.writeParcelable(wareHouse, flags);
        dest.writeTypedList(jobs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getShipmentNumber() {
        return shipmentNumber;
    }

    public int getTotalJobs() {
        return jobs.size();
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public Job getJobById(int index) {
        return this.jobs.get(index);
    }

    public WareHouse getWareHouse() {
        return wareHouse;
    }
}
