package app.marees.mars.MarsService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import app.marees.mars.Singletons.Myapp;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.


        intent = new Intent(Myapp.getAppContext(), MainService.class );
        Myapp.getAppContext().startService(intent);
        Log.e("receiver","inside job");

    }
}
