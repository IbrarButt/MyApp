package com.example.jobtask;

import android.app.job.JobParameters;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.jobdispatcher.JobService;

public class MyJob extends JobService {

    Backgroundtask backgroundtask;

    @Override
    public boolean onStartJob(@NonNull final com.firebase.jobdispatcher.JobParameters job) {
        backgroundtask = new Backgroundtask(){
            @Override
            protected void onPostExecute(String s) {

                Toast.makeText(getApplicationContext(),"Background Message: "+s,Toast.LENGTH_SHORT).show();
                jobFinished(job,false);
            }
        };
        backgroundtask.execute();


        return true;
    }

    @Override
    public boolean onStopJob(@NonNull com.firebase.jobdispatcher.JobParameters job) {
        return true;
    }

    public static class Backgroundtask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            return "Background service";
        }
    }
}
