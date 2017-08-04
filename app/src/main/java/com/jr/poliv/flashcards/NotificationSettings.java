package com.jr.poliv.flashcards;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.jr.poliv.flashcards.service.NotificationJobService;


public class NotificationSettings extends AppCompatActivity { //TODO: ADD CODE TO CREATE JOB

    private Switch notificationSwitch;
    private Spinner frequencySpinner;
    private Button button;
    private SharedPref shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        notificationSwitch = (Switch) findViewById(R.id.notificationSwitch);
        frequencySpinner = (Spinner) findViewById(R.id.frequency);
        button = (Button) findViewById(R.id.save);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shared = new SharedPref();
        notificationSwitch.setChecked(shared.isNotification());
        frequencySpinner.setSelection(shared.getFrequency());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });

    }

    private void onSave() {

        shared.saveChanges(notificationSwitch.isChecked(), frequencySpinner.getSelectedItemPosition());

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
        Log.d("Paul", "on save called");

        if(notificationSwitch.isChecked()) {
            ComponentName notificationService = new ComponentName(getPackageName(), NotificationJobService.class.getName());
            JobInfo jobInfo = new JobInfo.Builder(1, notificationService).setPersisted(true)
                    .setPeriodic(time(frequencySpinner.getSelectedItem().toString()))
                    .build();
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                PersistableBundle bundle = new PersistableBundle();
                bundle.putInt("MinimumLatency", time(frequencySpinner.getSelectedItem().toString()));
                jobInfo = new JobInfo.Builder(1, notificationService).setPersisted(true)
                        .setMinimumLatency(time(frequencySpinner.getSelectedItem().toString()))
                        .setExtras(bundle)
                        .build();

            }else{
                Log.d("Paul", "not minimum");
            }*/
            Log.d("Paul", "job scheduled");
            jobScheduler.schedule(jobInfo);
        }
    }

    private int time(String time){

        switch(time){
            case "Hour":{
                return 1000*60*60;
            }
            case "10 Hours":{
                return 1000*60*60*10;
            }
            case "Day":{
                return 1000*60*60*24;
            }
            default:{
                return 1000000000;
            }
        }
    }


    private class SharedPref{
        SharedPreferences file;
        SharedPreferences.Editor editor;
        private final String NOTIFICATION = "NOTIFICATION";
        private boolean notification;
        private final String FREQUENCY = "FREQUENCY";
        private int frequency;

        public boolean isNotification() {
            return notification;
        }

//        public void setNotification(boolean notification) {
//            this.notification = notification;
//            editor.putBoolean(NOTIFICATION, notification).apply();
//        }

        public int getFrequency() {
            return frequency;
        }

//        public void setFrequency(int frequency) {
//            this.frequency = frequency;
//            editor.putInt(FREQUENCY, frequency).apply();
//        }

        public void saveChanges(boolean notification, int frequency){
            this.notification = notification;
            this.frequency = frequency;
            editor.putBoolean(NOTIFICATION, notification);
            editor.putInt(FREQUENCY, frequency);
            editor.apply();

        }

        public SharedPref(){

            file = getSharedPreferences(getString(R.string.file_name), Context.MODE_PRIVATE);
            editor = file.edit();

            if(!file.contains(NOTIFICATION)){
                notification = notificationSwitch.isChecked();
                editor.putBoolean(NOTIFICATION, notification);
                frequency = frequencySpinner.getSelectedItemPosition();
                editor.putInt(FREQUENCY, frequency);
                editor.apply();
            }else{
                notification = file.getBoolean(NOTIFICATION, notificationSwitch.isChecked());
                frequency = file.getInt(FREQUENCY, frequencySpinner.getSelectedItemPosition());

            }
        }


    }
}
