package com.railtiffin.vendor_application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DeviceBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			/* Setting the alarm here */

			Intent alarmIntent = new Intent(context, AlarmReceiver.class);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, alarmIntent, 0);

			AlarmManager manager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			int interval = 15 * 60 * 1000;

			manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis(), interval, pendingIntent);

			Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
		}
	}
}
