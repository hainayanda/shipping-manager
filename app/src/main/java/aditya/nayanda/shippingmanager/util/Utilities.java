package aditya.nayanda.shippingmanager.util;

import android.os.Parcelable;

import java.util.ArrayList;

import aditya.nayanda.shippingmanager.model.Job;

/**
 * Created by nayanda on 25/03/18.
 */

public class Utilities {

    public static Job[] createArrayWithout(Job job, Job[] jobs) {
        if (jobs.length > 0) {
            ArrayList<Job> jobArrayList = new ArrayList<>();
            for (Job jobMember : jobs) {
                if (!jobMember.equals(job)) {
                    jobArrayList.add(jobMember);
                }
            }
            return jobArrayList.toArray(new Job[jobArrayList.size()]);
        }
        return jobs;
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
