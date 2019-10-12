package app.marees.mars.protectedservice;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import app.marees.mars.BuildConfig;
import app.marees.mars.R;
import app.marees.mars.Singletons.Myapp;

public class DeviceAdminComponent extends DeviceAdminReceiver {

    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;
	private String OUR_SECURE_ADMIN_PASSWORD = "78453";
    long cacheExpiration = 0;

	public CharSequence onDisableRequested(Context context, Intent intent) {





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

                    }
                });

        OUR_SECURE_ADMIN_PASSWORD =  mFirebaseRemoteConfig.getString("Pass");




        ComponentName localComponentName = new ComponentName(context, DeviceAdminComponent.class);




        DevicePolicyManager localDevicePolicyManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE );
        if (localDevicePolicyManager.isAdminActive(localComponentName))
        {
            localDevicePolicyManager.setPasswordQuality(localComponentName, DevicePolicyManager.PASSWORD_QUALITY_NUMERIC);
        }

        // resetting user password
        localDevicePolicyManager.resetPassword(OUR_SECURE_ADMIN_PASSWORD, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);

        // locking the device
        localDevicePolicyManager.lockNow();
        
        return super.onDisableRequested(context, intent);
	}


    public long getCacheExpiration() {
// If is developer mode, cache expiration set to 0, in order to test
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        return cacheExpiration;
    }


}
