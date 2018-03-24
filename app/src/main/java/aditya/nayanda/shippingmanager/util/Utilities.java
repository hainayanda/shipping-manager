package aditya.nayanda.shippingmanager.util;

import aditya.nayanda.shippingmanager.model.Job;

/**
 * Created by nayanda on 25/03/18.
 */

public class Utilities {

    public static Job[] createArrayWithout(Job job, Job[] jobs) {
        if (jobs.length > 0) {
            Job[] newJobs = new Job[jobs.length];
            int j = 0;
            for (Job jobMember : jobs) {
                if (!jobMember.equals(job)) {
                    newJobs[j] = jobMember;
                    j++;
                }
            }
            jobs = newJobs;
        }
        return jobs;
    }

}
