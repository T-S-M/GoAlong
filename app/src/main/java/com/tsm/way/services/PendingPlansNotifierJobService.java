package com.tsm.way.services;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class PendingPlansNotifierJobService extends JobService {

    private AsyncTask<Void, Void, Void> mainTask;

    @Override
    public boolean onStartJob(final JobParameters job) {

        mainTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                Context context = getApplicationContext();
                NotifyUserOfPendingTask.syncDataThenNotify(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job, false);
            }
        };

        mainTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mainTask != null) {
            mainTask.cancel(true);
        }
        return true;
    }
}
