package com.railtiffin.vendor_application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * @author Romil
 * 
 */
public class GCMNotificationIntentService extends IntentService {
	// Sets an ID for the notification, so it can be updated
	public static final int notifyID = 9001;
	NotificationCompat.Builder builder;
	int newOrderCounter;

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		System.out.println("Broadcast onReceive");

		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {

			/*
			 * 
			 * The below code can be used to check the key for which GCM ping is
			 * for what Activity
			 */

			// Code For Filtering Sub Order ID Push

			if (extras.get(ApplicationConstants.MSG_KEY) != null) {

				if (extras.get(ApplicationConstants.MSG_KEY).toString()
						.contains("suborder_id")) {

					System.out.println("Broadcast in IF Clause");

					String[] suborder_details = extras
							.get(ApplicationConstants.MSG_KEY).toString()
							.split("=");
					String suborder_id = suborder_details[1];

					String new_order = downloadFile(suborder_id);
					String previousOrders = readFile();
					makeNewFile(new_order, previousOrders);
					sendNotification(suborder_id);

				}
			}
		}
		PushyReceiver.completeWakefulIntent(intent);
	}

	// Method to send Notification

	private void sendNotification(String msg) {

		Intent resultIntent = new Intent(this, NewOrdersFragment.class);
		resultIntent.putExtra("myChatMsg", msg);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_ONE_SHOT);

		NotificationCompat.Builder mNotifyBuilder;
		NotificationManager mNotificationManager;

		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotifyBuilder = new NotificationCompat.Builder(this)
				.setContentTitle("New Order")
				.setContentText("NEW ORDER with Order ID - " + msg)
				.setSmallIcon(R.drawable.ic_launcher);
		// Set pending intent
		mNotifyBuilder.setContentIntent(resultPendingIntent);

		// Set Vibrate, Sound and Light
		int defaults = 0;
		defaults = defaults | Notification.DEFAULT_LIGHTS;
		defaults = defaults | Notification.DEFAULT_VIBRATE;
		defaults = defaults | Notification.DEFAULT_SOUND;

		mNotifyBuilder.setDefaults(defaults);
		// Set the content for Notification
		mNotifyBuilder.setContentText("NEW ORDER with Order ID - " + msg);
		// Set Auto Cancel
		mNotifyBuilder.setAutoCancel(true);
		// Post a notification
		mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	}

	// Method to fetch all the Order Data

	public String downloadFile(String suborder_id) {

		CharSequence seq = "";

		try {

			System.out.println("Broadcast Download File Function");

			URL url = new URL(ApplicationConstants.NEW_ORDER_SERVER_URL
					+ suborder_id);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();

			System.out.println("Broadcast Download File Function1");

			InputStream inputStream = urlConnection.getInputStream();

			byte[] buffer = new byte[1024];

			int bufferLength = 0;

			// Reading the Data from Internet
			while ((bufferLength = inputStream.read(buffer)) > 0) {

				Charset charset = Charset.forName("UTF-8");
				seq = new String(buffer, charset);
				System.out.println("Broadcast Download File Function5");

			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return seq.toString();

	}

	public String readFile() {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		String uriString = (file.getAbsolutePath() + "/" + "myNewOrderFile" + ".txt");
		String line;
		String entireFile = "";

		// Reading the File
		FileInputStream is;
		try {
			is = new FileInputStream(uriString);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			while ((line = br.readLine()) != null) {
				entireFile += (line + "\n");
				System.out.println("Broadcast Download File Function4");
			}
			System.out.println("Read File \n" + entireFile);
			is.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		return entireFile;

	}

	public void makeNewFile(String s1, String s2) {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		if (!file.exists()) {
			file.mkdirs();
		}

		System.out.println("Broadcast Download File Function2");

		String uriString = (file.getAbsolutePath() + "/" + "myNewOrderFile" + ".txt");

		System.out.println("Broadcast Download File Function3");

		FileWriter fileWrite;
		try {
			fileWrite = new FileWriter(uriString);
			fileWrite.write("<order>" + s1 + s2 + "</order>"); // LENGTH
			// ERROR
			System.out.println("Broadcast Download File Function6");
			fileWrite.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			encrypt();
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		deleteFile();

	}

	// Encrypt the Order Details File

	@SuppressLint("TrulyRandom")
	/* Encryption is being done using 128 bit AES */
	public void encrypt() throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		String uriString = (file.getAbsolutePath() + "/" + "myNewOrderFile" + ".txt");
		String uriStringEncrypted = (file.getAbsolutePath() + "/"
				+ "myNewOrderEncryptedFile" + ".txt");
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

	// Delete the non encrypted file

	public boolean deleteFile() {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath()
				+ "/RailTiffin/Documents/Sensitive/myNewOrderFile.txt");
		boolean deleted = file.delete();
		return deleted;

	}

	// Check if an Activity is in Background

	public boolean isForeground(String myClass) {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager
				.getRunningTasks(1);
		ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
		return componentInfo.getClassName().equals(myClass);
	}

	// Code to decrypt File

	public void decrypt() throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		String uriString = (file.getAbsolutePath() + "/"
				+ "myNewOrderEncryptedFile" + ".txt");
		String uriDecryptedString = (file.getAbsolutePath() + "/"
				+ "myNewOrderFile" + ".txt");

		FileInputStream fis = new FileInputStream(uriString);

		FileOutputStream fos = new FileOutputStream(uriDecryptedString);
		SecretKeySpec sks = new SecretKeySpec("Romil_Tiwar1-i_@".getBytes(),
				"AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, sks);
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		int b;
		byte[] d = new byte[8];
		while ((b = cis.read(d)) != -1) {
			fos.write(d, 0, b);
		}
		fos.flush();
		fos.close();
		cis.close();
	}

	public String getStringFromFile(String filePath) throws Exception {
		File fl = new File(filePath);
		FileInputStream fin = new FileInputStream(fl);
		String ret = convertStreamToString(fin);
		// Make sure you close all streams.
		fin.close();
		return ret;
	}

	public String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

	// public void chatFunction(){
	//
	// if (isForeground("com.railtiffin.vendor_application.ChatFragment")) {
	//
	// // Do Something
	//
	// Intent resultIntent = new Intent(this, ChatFragment.class);
	// resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// resultIntent
	// .putExtra("myChatMsg",
	// extras.get(ApplicationConstants.MSG_KEY)
	// .toString());
	// startActivity(resultIntent);
	//
	// } else {
	//
	// sendNotification(extras.get(ApplicationConstants.MSG_KEY)
	// .toString());
	// }
	//
	// }

}
