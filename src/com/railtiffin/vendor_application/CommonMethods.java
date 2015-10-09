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
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class CommonMethods {

	public static final int notifyID = 9001;

	public static void showToast(String message, Context context) {

		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

	}

	public static void sendNotification(String msg, Context c, String title,
			Class<?> a) {

		Intent resultIntent = new Intent(c, a);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(c, 0,
				resultIntent, PendingIntent.FLAG_ONE_SHOT);

		NotificationCompat.Builder mNotifyBuilder;
		NotificationManager mNotificationManager;

		mNotificationManager = (NotificationManager) c
				.getSystemService(Context.NOTIFICATION_SERVICE);

		mNotifyBuilder = new NotificationCompat.Builder(c)
				.setContentTitle(title)
				.setContentText(msg)
				.setSmallIcon(R.drawable.ic_launcher)
				.setSound(
						Uri.parse("android.resource://" + c.getPackageName()
								+ "/" + R.raw.click));
		// Set pending intent
		mNotifyBuilder.setContentIntent(resultPendingIntent);

		// Set Vibrate, Sound and Light
		int defaults = 0;
		defaults = defaults | Notification.DEFAULT_LIGHTS;
		defaults = defaults | Notification.DEFAULT_VIBRATE;

		mNotifyBuilder.setDefaults(defaults);
		// Set the content for Notification
		mNotifyBuilder.setContentText(msg);
		// Set Auto Cancel
		mNotifyBuilder.setAutoCancel(true);
		// Post a notification
		mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	}

	public static String downloadFile(String suborder_id) {

		String res = "";

		try {
			String sid = URLEncoder.encode(suborder_id, "utf-8");

			System.out.println("Broadcast Download File Function");

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(
					ApplicationConstants.NEW_ORDER_SERVER_URL + sid);

			HttpResponse response = httpClient.execute(httpPost, localContext);
			res = EntityUtils.toString(response.getEntity());

			// URL url = new URL(ApplicationConstants.NEW_ORDER_SERVER_URL
			// + suborder_id);
			//
			// // URL url = new URL(
			// // "http://test.traveltreats.in/oc_railtiffin/orderStatic.xml");
			//
			// HttpURLConnection urlConnection = (HttpURLConnection) url
			// .openConnection();
			// urlConnection.setRequestMethod("GET");
			// urlConnection.setDoOutput(true);
			// urlConnection.connect();
			//
			// System.out.println("Broadcast Download File Function1");
			//
			// InputStream inputStream = urlConnection.getInputStream();
			//
			// byte[] buffer = new byte[1024];
			//
			// int bufferLength = 0;
			//
			// // Reading the Data from Internet
			// while ((bufferLength = inputStream.read(buffer)) > 0) {
			//
			// Charset charset = Charset.forName("UTF-8");
			// seq = new String(buffer, charset);
			// System.out.println("Broadcast Download File Function5");
			//
			// }

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.d("DOWNLOADED", res);

		return res;

	}

	public static String readFile(String fileName) {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		String uriString = (file.getAbsolutePath() + "/" + fileName + ".txt");
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

	public static void makeNewFile(String s1, String s2, String fileName) {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		if (!file.exists()) {
			file.mkdirs();
		}

		System.out.println("Broadcast Download File Function2");

		String uriString = (file.getAbsolutePath() + "/" + fileName + ".txt");

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
			if (fileName.contentEquals("myNewOrderFile")) {
				encryptNewOrder();
			} else {
				encryptAcceptedOrder();
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fileName.contentEquals("myNewOrderFile")) {
			deleteNewOrder();
		} else {
			deleteAcceptedOrder();
		}

	}

	// Encrypt the Order Details File

	@SuppressLint("TrulyRandom")
	/* Encryption is being done using 128 bit AES */
	public static void encryptNewOrder() throws IOException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException {

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

	public static void encryptAcceptedOrder() throws IOException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		String uriString = (file.getAbsolutePath() + "/"
				+ "myAcceptedOrderFile" + ".txt");
		String uriStringEncrypted = (file.getAbsolutePath() + "/"
				+ "myAcceptedOrderEncryptedFile" + ".txt");
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

	public static boolean deleteNewOrder() {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath()
				+ "/RailTiffin/Documents/Sensitive/myNewOrderFile.txt");
		boolean deleted = file.delete();
		return deleted;

	}

	public static boolean deleteAcceptedOrder() {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath()
				+ "/RailTiffin/Documents/Sensitive/myAcceptedOrderFile.txt");
		boolean deleted = file.delete();
		return deleted;

	}

	public static boolean deleteNewEncryptedOrder() {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath()
				+ "/RailTiffin/Documents/Sensitive/myNewOrderEncryptedFile.txt");
		boolean deleted = file.delete();
		return deleted;

	}

	public static boolean deleteAcceptedEncryptedOrder() {

		File file = new File(
				Environment.getExternalStorageDirectory().getPath()
						+ "/RailTiffin/Documents/Sensitive/myAcceptedOrderEncryptedFile.txt");
		boolean deleted = file.delete();
		return deleted;

	}

	// Code to decrypt File

	public void decryptNewOrder() throws IOException, NoSuchAlgorithmException,
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

}
