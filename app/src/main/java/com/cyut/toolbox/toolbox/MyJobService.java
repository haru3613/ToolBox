package com.cyut.toolbox.toolbox;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;


public class MyJobService extends JobService {
    private static final String TAG = "MyJob";

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here
        Log.d(TAG, "onStartJob: "+job);
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d(TAG, "onStopJob: "+job);
        return false; // Answers the question: "Should this job be retried?"
    }
}