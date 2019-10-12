package app.marees.mars.Singletons;

import android.app.Application;
import android.content.Context;

public class HelperApplication extends Application {
    private static HelperApplication mInstance;
    private static Context mAppContext;








    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.setAppContext(getApplicationContext());

    }

    public static HelperApplication getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }


}