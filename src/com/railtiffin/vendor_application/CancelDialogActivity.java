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
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CancelDialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_cancel_dialog);

		final EditText query = (EditText) findViewById(R.id.etReplyVendor);
		Button sendResponse = (Button) findViewById(R.id.bSendResponse);

		sendResponse.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String vendorReason = query.getText().toString();
				if (vendorReason.contentEquals("")) {

					Toast.makeText(getApplicationContext(),
							"Please submit Reason", Toast.LENGTH_LONG).show();

				} else {

					SharedPreferences prefs = getSharedPreferences(
							"UserDetails", Context.MODE_PRIVATE);
					String deviceID = prefs
							.getString(LoginActivity.REG_ID, "0");

					new ContactServer().execute("25", getIntent()
							.getStringExtra("suborder_id").trim(), deviceID,
							vendorReason);

				}

			}
		});

	}

	class ContactServer extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(CancelDialogActivity.this);
			pDialog.setMessage("Please Wait...");
			pDialog.setCancelable(false);
			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			pDialog.show();

		}

		String res = "";

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			try {
				String key = URLEncoder.encode(params[0], "utf-8");
				String suborderID = URLEncoder.encode(params[1], "utf-8");
				String deviceID = URLEncoder.encode(params[2], "utf-8");
				String reason = URLEncoder.encode(params[3], "utf-8");

				HttpPost httpPost = new HttpPost(
						ApplicationConstants.STATUS_ORDERS_SERVER_URL + key
								+ "&sid=" + suborderID + "&by=" + deviceID
								+ "&msg=" + reason);

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

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pDialog.dismiss();
			startActivity(new Intent(CancelDialogActivity.this,
					MainActivity.class));
			finish();

		}

	}

}
