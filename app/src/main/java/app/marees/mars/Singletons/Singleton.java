package app.marees.mars.Singletons;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singleton extends Application{
    private static Singleton mInstance;
    private RequestQueue mRequestQueue;




    private Singleton(Context context) {
        mRequestQueue = Volley.newRequestQueue(Myapp.getAppContext().getApplicationContext());

    }

    public static synchronized Singleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Singleton(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

}
