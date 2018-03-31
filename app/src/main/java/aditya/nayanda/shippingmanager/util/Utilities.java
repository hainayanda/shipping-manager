package aditya.nayanda.shippingmanager.util;

import android.os.Parcelable;

import aditya.nayanda.shippingmanager.model.Job;
import aditya.nayanda.shippingmanager.model.ListOfJobs;

/**
 * Created by nayanda on 25/03/18.
 */

public class Utilities {

    public static void removeJobFrom(ListOfJobs jobs, Job job) {
        jobs.getJobs().remove(job);
    }

    public static Job[] castParcelableToJobs(Parcelable[] parcelables) {
        if (parcelables == null) return new Job[0];
        if (parcelables.length == 0) return new Job[0];
        else {
            Job[] jobs = new Job[parcelables.length];

            for (int i = 0; i < parcelables.length; i++) {
                jobs[i] = (Job) parcelables[i];
            }
            return jobs;
        }
    }

}
