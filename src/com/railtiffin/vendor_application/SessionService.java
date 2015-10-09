package com.railtiffin.vendor_application;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

/**
 * @author Romil
 *
 */
public class SessionService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Timer timer = new Timer();
		timer.schedule(new mainTask(), 90000000);

		return super.onStartCommand(intent, flags, startId);
	}

	private class mainTask extends TimerTask {
		public void run() {
			sessionReset();

		}
	}

	private void sessionReset() {

		SharedPreferences sharedPref = this.getSharedPreferences(
				"session_pref", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("session", 0);
		editor.commit();

	}

}
