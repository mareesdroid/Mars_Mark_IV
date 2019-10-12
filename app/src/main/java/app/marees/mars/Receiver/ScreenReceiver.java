package app.marees.mars.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import app.marees.mars.ScreenshotAct;


public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){

            wasScreenOn = false;
            Log.e("from","ScreenReceiver");

            ScreenshotAct s = new ScreenshotAct();


            ScreenshotAct.ImageAvailableListener turnOff = s.new ImageAvailableListener();
            turnOff.ScreenOff();
        }
        else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){


            wasScreenOn = true;
        }
    }
}
