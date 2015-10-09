package com.railtiffin.vendor_application;

import java.io.IOException;
import java.net.URLEncoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * @author Romil
 * 
 */
public class PushyReceiver extends WakefulBroadcastReceiver {

	String suborder_id;
	Context c;

	static final String KEY_MAIN = "order_info"; // parent node
	static final String KEY_STAT = "order_status_id";

	@Override
	public void onReceive(Context context, Intent intent) {

		c = context;
		if (intent.getStringExtra("message").trim().contains("cancel")) {
			String[] ids = intent.getStringExtra("message").split("=");
			suborder_id = ids[1].trim();

			SharedPreferences cancelOrders = context.getSharedPreferences(
					"cancelledPref", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = cancelOrders.edit();
			editor.putString("cancelID", suborder_id + ",");
			editor.commit();

			CommonMethods.sendNotification("Order Number " + suborder_id
					+ " Cancelled.", context, "Cancelled Order",
					CancelledOrdersActivity.class);
		} else {

			String[] ids = intent.getStringExtra("message").split("=");
			suborder_id = ids[1].trim();

			SharedPreferences prefs = context.getSharedPreferences(
					"UserDetails", Context.MODE_PRIVATE);
			String deviceID = prefs.getString(LoginActivity.REG_ID, "0");

			new ReceivedTimeStampTask().execute(deviceID, suborder_id);

			CommonMethods.sendNotification("New Order with Order No: "
					+ suborder_id, context, "New Order", MainActivity.class);
		}

	}

	class ReceivedTimeStampTask extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		String res = "";

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			try {

				String deviceID = URLEncoder.encode(params[0], "utf-8");
				String suborderID = URLEncoder.encode(params[1], "utf-8");

				HttpPost httpPost = new HttpPost(
						ApplicationConstants.PUSH_RECEIVED_CALLBACK + deviceID
								+ "&sid=" + suborderID);

				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				res = EntityUtils.toString(response.getEntity());

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return res;
		}

	}

}
