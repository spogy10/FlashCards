package com.jr.poliv.flashcards.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.jr.poliv.flashcards.R;

/**
 * Created by poliv on 8/4/2017.
 */

public class NotificationJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Notification.Builder notification = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Remember to study!!!");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification.build());
        Log.d("Paul", "the job");
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ComponentName notificationService = new ComponentName(getPackageName(), NotificationJobService.class.getName());
            JobInfo jobInfo = new JobInfo.Builder(1, notificationService).setPersisted(true)
                    .setMinimumLatency(params.getExtras().getInt("MinimumLatency"))
                    .setExtras(params.getExtras())
                    .build();
            JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(jobInfo);
            Log.d("Paul", String.valueOf(JobInfo.getMinPeriodMillis()));
        }else{
            Log.d("Paul", "not minimum");
        }*/
        jobFinished(params, true);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
