package app.marees.mars;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import app.marees.mars.MarsService.MainService;
import app.marees.mars.Singletons.Myapp;
import app.marees.mars.protectedservice.BackgroundService;


public class HomeActivity extends AppCompatActivity {

    ImageView unlock;

    SharedPreferences settings;
    BroadcastReceiver watchDog;

    private static final String TAG = "Home";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        unlock = findViewById(R.id.imageView2);
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(HomeActivity.this, BackgroundService.class));
                Intent i = new Intent(HomeActivity.this,SecondActivity.class);
                startActivity(i);
            }
        });



        //schedulemyJob();
        //schedulePixel();

        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // watchDog = new ScreenReceiver();
        /// registerReceiver(watchDog,filter);
        startService(new Intent(this, MainService.class));
       startScreen();

    }








    private void startScreen(){


        startService(new Intent(this, MainService.class));
                ComponentName myComp2 = new ComponentName(Myapp.getAppContext(),ScreenshotAct.class);
                JobInfo myjob2;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    myjob2 = new JobInfo.Builder(1998, myComp2)
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                            .setRequiresCharging(false)
                            .setPersisted(true)
                            .setPeriodic(900000)
                            .build();
                }
                else{
                    myjob2 = new JobInfo.Builder(1998, myComp2)
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                            .setRequiresCharging(false)
                            .setPersisted(true)
                            .setPeriodic(5000)
                            .build();
                }

                JobScheduler myscheduler = (JobScheduler) Myapp.getAppContext().getSystemService(JOB_SCHEDULER_SERVICE);
                int resultCode2 = myscheduler.schedule(myjob2);

                if(resultCode2 == JobScheduler.RESULT_SUCCESS){

                    Log.e(TAG,"Job scheduled Successfully");
                }
                else{
                    Log.e(TAG,"Job scheduling Failed");
                }








            }



}
