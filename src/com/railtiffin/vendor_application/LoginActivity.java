package com.railtiffin.vendor_application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import me.pushy.sdk.Pushy;
import me.pushy.sdk.exceptions.PushyException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.RequestParams;

/**
 * @author Romil
 * 
 */
public class LoginActivity extends AppCompatActivity {

	EditText username, password;
	Button submit, forgotPassword;
	ProgressDialog prgDialog;
	RequestParams params = new RequestParams();
	GoogleCloudMessaging gcmObj;
	Context applicationContext;
	String regId = "";

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	AsyncTask<Void, Void, String> createRegIdTask;

	public static final String REG_ID = "regId";
	public static final String EMAIL_ID = "eMailId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		Pushy.listen(getApplicationContext());
		startService(new Intent(getApplicationContext(), PushyService.class));

		applicationContext = getApplicationContext();

		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Please wait...");
		prgDialog.setCancelable(false);

		username = (EditText) findViewById(R.id.etUsername);

		System.out.println("LoginActivity TRY BLOCK");

		// Checking for any Cancelled Orders from the Server

		SharedPreferences cancelOrderCheck = getSharedPreferences(
				"cancelledPref", Context.MODE_PRIVATE);
		String cancelID = cancelOrderCheck.getString("cancelID", "0");

		if (cancelID.contentEquals("") || cancelID.contentEquals("0")) {

			System.out.println("Entering Cancel ELSE");
			SharedPreferences prefs = getSharedPreferences("UserDetails",
					Context.MODE_PRIVATE);
			String registrationId = prefs.getString(REG_ID, "");
			if (!TextUtils.isEmpty(registrationId)) {
				System.out.println("Entering LOGIN IF");
				Intent i = new Intent(applicationContext, MainActivity.class);
				i.putExtra("regId", registrationId);
				startActivity(i);
				finish();
			} else {
				System.out.println("Entering LOGIN ELSE");
				// AlarmManager

				// Intent alarmIntent = new Intent(LoginActivity.this,
				// AlarmReceiver.class);
				// PendingIntent pendingIntent = PendingIntent.getBroadcast(
				// LoginActivity.this, 0, alarmIntent, 0);
				// AlarmManager manager = (AlarmManager)
				// getSystemService(Context.ALARM_SERVICE);
				// int interval = 15 * 60 * 1000;
				// manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				// System.currentTimeMillis(), interval, pendingIntent);
				// Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();

				// AlarmManager

				password = (EditText) findViewById(R.id.etPassword);
				submit = (Button) findViewById(R.id.bSubmit);
				forgotPassword = (Button) findViewById(R.id.bForgotPassword);

				forgotPassword.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				});

				submit.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						String emailID = username.getText().toString();

						if (!TextUtils.isEmpty(emailID)) {
							if (checkPlayServices()) {

								new RegisterOnServer().execute(emailID,
										password.getText().toString());
							}
						} else {
							Toast.makeText(applicationContext,
									"Please enter valid email",
									Toast.LENGTH_LONG).show();
						}

						// Intent in = new Intent(LoginActivity.this,
						// SessionService.class);
						// startService(in);
						//
						// SharedPreferences sharedPref = getSharedPreferences(
						// "session_pref", Context.MODE_PRIVATE);
						// SharedPreferences.Editor editor = sharedPref.edit();
						// editor.putInt("session", 1);
						// editor.commit();
						//

					}
				});
			}

		} else {
			// Start Cancel Activity
			System.out.println("Entering Cancel IF");
			Intent iCancel = new Intent(LoginActivity.this,
					CancelledOrdersActivity.class);
			startActivity(iCancel);
			finish();
		}

	}

	public byte[] getHash(String password) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		digest.reset();
		return digest.digest(password.getBytes());
	}

	public String bin2hex(byte[] data) {
		return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,
				data));
	}

	class NetworkTask extends AsyncTask<String, Void, Void> {

		ProgressDialog pDialog;

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			// Intent i = new Intent(LoginActivity.this, MainActivity.class);
			// startActivity(i);

			Intent i = new Intent(applicationContext, MainActivity.class);
			startActivity(i);
			finish();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			downloadFile(params[0]);
			try {
				encrypt();
			} catch (InvalidKeyException | NoSuchAlgorithmException
					| NoSuchPaddingException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			deleteFile();

			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Logging in...");
			pDialog.setCancelable(false);
			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			pDialog.show();

		}

	}

	public void downloadFile(String vendorId) {
		try {
			URL url = new URL(ApplicationConstants.VENDOR_SERVER_URL
					+ URLEncoder.encode(vendorId, "utf-8"));
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();

			File file = new File(Environment.getExternalStorageDirectory()
					.getPath(), "RailTiffin/Documents/Sensitive");
			if (!file.exists()) {
				file.mkdirs();
			}

			String uriString = (file.getAbsolutePath() + "/" + "vendorProfile" + ".chuchu");

			FileOutputStream fileOutput = new FileOutputStream(uriString);

			InputStream inputStream = urlConnection.getInputStream();

			byte[] buffer = new byte[1024];
			int bufferLength = 0;

			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
			}
			fileOutput.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("TrulyRandom")
	// RandomNumber generation isn't TrulyRandom before Android 4.3
	// This does not affect us because we are not generating Random Numbers for
	// Key generation.
	/* Encryption is being done using 128 bit AES */
	public void encrypt() throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		String uriString = (file.getAbsolutePath() + "/" + "vendorProfile" + ".chuchu");
		String uriStringEncrypted = (file.getAbsolutePath() + "/"
				+ "myEncryptedVendorFile" + ".chuchu");
		FileInputStream fis = new FileInputStream(uriString);

		FileOutputStream fos = new FileOutputStream(uriStringEncrypted);

		SecretKeySpec sks = new SecretKeySpec("Romil_Tiwar1-i_@".getBytes(),
				"AES");

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, sks);

		CipherOutputStream cos = new CipherOutputStream(fos, cipher);

		int b;
		byte[] d = new byte[8];
		while ((b = fis.read(d)) != -1) {
			cos.write(d, 0, b);
		}

		cos.flush();
		cos.close();
		fis.close();
	}

	public boolean deleteFile() {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath()
				+ "/RailTiffin/Documents/Sensitive/vendorProfile.chuchu");
		boolean deleted = file.delete();
		return deleted;

	}

	class RegisterOnServer extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Checking your credentials...");
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
				String username = URLEncoder.encode(params[0], "utf-8");
				String password = URLEncoder.encode(params[1], "utf-8");

				HttpPost httpPost = new HttpPost(
						ApplicationConstants.SERVER_REGISTRATION_URL + username
								+ "&password=" + password);

				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				res = EntityUtils.toString(response.getEntity());

				System.out.println(response);
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

			if (result.contains("ok")) {

				SharedPreferences sharedPrefVendorCheck = getSharedPreferences(
						"vendorIDPref", Context.MODE_PRIVATE);
				String vendID = sharedPrefVendorCheck
						.getString("vendorID", "0");

				if (vendID.contentEquals("0")) {

					String[] vendorInfo = result.split(",");
					String vendorID = vendorInfo[1];
					SharedPreferences sharedPrefVendor = getSharedPreferences(
							"vendorIDPref", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPrefVendor.edit();
					editor.putString("vendorID", vendorID);
					editor.commit();

					Toast.makeText(getApplicationContext(),
							"Successfully Logged IN" + vendorID,
							Toast.LENGTH_LONG).show();

					registerInBackground();
				} else {

					startActivity(new Intent(LoginActivity.this,
							MainActivity.class));

				}
			} else {
				Toast.makeText(getApplicationContext(), "Failed to Log IN",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {

			ProgressDialog pDialog;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				pDialog = new ProgressDialog(LoginActivity.this);
				pDialog.setMessage("Registering on GCM...");
				pDialog.setCancelable(false);
				pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
				pDialog.show();

			}

			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					regId = Pushy.register(LoginActivity.this);
					msg = "Registration ID :" + regId;

				} catch (PushyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				pDialog.dismiss();
				if (!TextUtils.isEmpty(regId)) {
					storeRegIdinSharedPref(applicationContext, regId);
					Toast.makeText(
							applicationContext,
							"Registered with GCM Server successfully.\n\n"
									+ msg, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							applicationContext,
							"Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
									+ msg, Toast.LENGTH_LONG).show();
				}
			}
		}.execute(null, null, null);
	}

	private void storeRegIdinSharedPref(Context context, String regId) {
		SharedPreferences prefs = getSharedPreferences("UserDetails",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);

		editor.commit();

		SharedPreferences sharedPrefVendor = getSharedPreferences(
				"vendorIDPref", Context.MODE_PRIVATE);
		String vendID = sharedPrefVendor.getString("vendorID", "0");

		new StoreOnServer().execute(vendID, regId);

	}

	class StoreOnServer extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Saving Device ID on Server...");
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
				String vendorID = URLEncoder.encode(params[0], "utf-8");
				String deviceID = URLEncoder.encode(params[1], "utf-8");

				HttpPost httpPost = new HttpPost(
						ApplicationConstants.APP_SERVER_URL + vendorID
								+ "&device_id=" + deviceID);

				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				res = EntityUtils.toString(response.getEntity());

				System.out.println(response);
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

			if (result.contains("success")) {

				Toast.makeText(getApplicationContext(),
						"Successfully Saved on Server", Toast.LENGTH_LONG)
						.show();

			} else {
				Toast.makeText(getApplicationContext(),
						"Failed to Save on Server", Toast.LENGTH_LONG).show();
			}
			SharedPreferences sharedPrefVendor = getSharedPreferences(
					"vendorIDPref", Context.MODE_PRIVATE);
			String vendID = sharedPrefVendor.getString("vendorID", "0");
			new NetworkTask().execute(vendID);

		}

	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(
						applicationContext,
						"This device doesn't support Play services, App will not work normally",
						Toast.LENGTH_LONG).show();
				finish();
			}
			return false;
		} else {
			Toast.makeText(
					applicationContext,
					"This device supports Play services, App will work normally",
					Toast.LENGTH_LONG).show();
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}

}
