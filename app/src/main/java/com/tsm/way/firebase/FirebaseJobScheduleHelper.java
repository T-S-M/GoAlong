package com.tsm.way.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.tsm.way.services.PendingPlansNotifierJobService;

import java.util.concurrent.TimeUnit;

public class FirebaseJobScheduleHelper {

    private static final String APP_PENDING_JOB_TAG = "Sync_pending";
    private static int INTERVAL_HOUR = 6;
    private static final int INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(INTERVAL_HOUR);
    private static final int FLEXTIME_SECONDS = INTERVAL_SECONDS / 3;
    private static boolean sInitialized;
    private static int AppSyncConstraint = Constraint.ON_ANY_NETWORK;

    synchronized public static void initialize(@NonNull final Context context) {
        if (sInitialized) {
            return;
        }
        sInitialized = true;
        scheduleFirebaseJobDispatcherSync(context);
    }

    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(driver);

        Job syncPendingToNotifyJob = jobDispatcher.newJobBuilder()
                .setService(PendingPlansNotifierJobService.class)
                .setTag(APP_PENDING_JOB_TAG)
                .addConstraint(AppSyncConstraint)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(INTERVAL_SECONDS,
                        INTERVAL_SECONDS + FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        jobDispatcher.mustSchedule(syncPendingToNotifyJob);
        Log.v("Job", "Scheduled");
    }

}
