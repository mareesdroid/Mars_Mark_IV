package app.marees.mars.MarsService;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import app.marees.mars.camera.APictureCapturingService;

public class CamJob extends JobService {

    private static final String TAG ="CamJob";
    private boolean jobCanceled = false;
    private APictureCapturingService pictureService;

    @Override
    public boolean onStartJob(JobParameters params) {


        Log.e("","Job Started");




        return true;
    }


    private void doBackground(final JobParameters params){

        new Thread(new Runnable() {
            @Override
            public void run() {


            }
        }).start();



    }

    @Override
    public boolean onStopJob(JobParameters params) {

        Log.e(TAG,"Job Cancelled before Completion");
        jobCanceled = true;
        return true;
    }


}
