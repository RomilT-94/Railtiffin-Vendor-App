package com.railtiffin.vendor_application;

import me.pushy.sdk.Pushy;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PushyService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
		
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		Pushy.listen(getApplicationContext());
		
		return super.onStartCommand(intent, flags, startId);
	}

}
