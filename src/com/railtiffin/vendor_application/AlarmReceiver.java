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
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	Context c;
	static final String KEY_ACCEPTED = "info";
	static final String KEY_ACCEPTED_ORDER = "suborder_id";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		c = context;

		Vibrator v = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 500 milliseconds
		v.vibrate(500);

		SharedPreferences sharedPrefVendorCheck = context.getSharedPreferences(
				"vendorIDPref", Context.MODE_PRIVATE);
		String vendID = sharedPrefVendorCheck.getString("vendorID", "0");
		new FetchPendingOrders().execute(vendID);

		Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

	}

	class FetchPendingOrders extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		String res = "";

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			try {
				String vendorID = URLEncoder.encode(params[0], "utf-8");

				HttpPost httpPost = new HttpPost(
						ApplicationConstants.PENDING_ORDERS_SERVER_URL
								+ vendorID);

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

			// pDialog.dismiss();

			Toast.makeText(c, result, Toast.LENGTH_SHORT).show();

			if (result.trim().equals("<pending></pending>")) {
				deleteFile();
				deleteNormalFile();
				deleteAcceptedFile();
				deleteAcceptedNormalFile();
				SharedPreferences sharedPrefVendorCheck = c
						.getSharedPreferences("vendorIDPref",
								Context.MODE_PRIVATE);
				String vendID = sharedPrefVendorCheck
						.getString("vendorID", "0");
				new ContactServerAcceptedOrders().execute(vendID);

			} else {

				String res = result.replace("<pending>", "");
				String res2 = res.replace(",</pending>", "");

				deleteFile();
				deleteNormalFile();

				String myOrders = res2.trim();

				new ContactServerPendingOrders().execute(myOrders);
			}

		}

	}

	class ContactServerPendingOrders extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		String res = "";

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String[] subOrderIds = params[0].split(",");
			for (int i = 0; i < subOrderIds.length; i++) {

				Log.d("MYLOG", subOrderIds[i]);

				String new_order = downloadFile(subOrderIds[i]);
				String previousOrders = readFile();
				makeNewFile(new_order, previousOrders);
			}

			return res;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			deleteAcceptedFile();
			deleteAcceptedNormalFile();
			SharedPreferences sharedPrefVendorCheck = c.getSharedPreferences(
					"vendorIDPref", Context.MODE_PRIVATE);
			String vendID = sharedPrefVendorCheck.getString("vendorID", "0");
			new ContactServerAcceptedOrders().execute(vendID);

		}

	}

	class ContactServerAcceptedOrders extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		String res = "";

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			XMLParser parser = new XMLParser(c);
			String xml = parser
					.getXmlFromUrl(ApplicationConstants.ACCEPTED_ORDERS_SERVER_URL
							+ params[0]);
			Document doc = parser.getDomElement(xml); // getting DOM element

			NodeList nl = doc.getElementsByTagName(KEY_ACCEPTED);
			// looping through all song nodes <song>
			List<String> orderIds = new ArrayList<String>();
			for (int i = 0; i < nl.getLength(); i++) {
				Element e = (Element) nl.item(i);
				orderIds.add(parser.getValue(e, KEY_ACCEPTED_ORDER));

			}

			System.out.println(orderIds.toString());

			for (int i = 0; i < orderIds.size(); i++) {

				String details = downloadFile(orderIds.get(i));
				String older = readAcceptedFile();
				makeAcceptedNewFile(details, older);

			}

			return res;
		}

	}

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

	public String readAcceptedFile() {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		String uriString = (file.getAbsolutePath() + "/"
				+ "myAcceptedOrderFile" + ".txt");
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

		// deleteNormalFile();

	}

	public void makeAcceptedNewFile(String s1, String s2) {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		if (!file.exists()) {
			file.mkdirs();
		}

		System.out.println("Broadcast Download File Function2");

		String uriString = (file.getAbsolutePath() + "/"
				+ "myAcceptedOrderFile" + ".txt");

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
			acceptedEncrypt();
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// deleteAcceptedNormalFile();

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

	public void acceptedEncrypt() throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException {

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

	public boolean deleteNormalFile() {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath()
				+ "/RailTiffin/Documents/Sensitive/myNewOrderFile.txt");
		boolean deleted = file.delete();
		return deleted;

	}

	public boolean deleteAcceptedNormalFile() {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath()
				+ "/RailTiffin/Documents/Sensitive/myAcceptedOrderFile.txt");
		boolean deleted = file.delete();
		return deleted;

	}

	// Delete the encrypted file

	public boolean deleteFile() {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath()
				+ "/RailTiffin/Documents/Sensitive/myNewOrderEncryptedFile.txt");
		boolean deleted = file.delete();
		return deleted;

	}

	public boolean deleteAcceptedFile() {

		File file = new File(
				Environment.getExternalStorageDirectory().getPath()
						+ "/RailTiffin/Documents/Sensitive/myAcceptedOrderEncryptedFile.txt");
		boolean deleted = file.delete();
		return deleted;

	}

}
