package app.marees.mars.protectedservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import app.marees.mars.Singletons.Myapp;

public class ServiceStarter extends BroadcastReceiver {	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serviceLauncher = new Intent(Myapp.getAppContext(), BackgroundService.class);
		Myapp.getAppContext().startService(serviceLauncher);
	}
}
