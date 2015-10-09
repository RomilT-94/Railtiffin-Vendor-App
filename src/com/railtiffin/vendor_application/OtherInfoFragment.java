package com.railtiffin.vendor_application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.NoSuchPaddingException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Romil
 * 
 */
public class OtherInfoFragment extends Fragment {

	View rootView;

	ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	static final String KEY_GENERAL_TAG = "general";
	static final String KEY_MIN_ORDER = "min_order_value";
	static final String KEY_MIN_DELIVERY = "delivery_notice";
	static final String KEY_VENDOR_TYPE = "type";

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.activity_other_info, container,
				false);

		EditText minOrderValue = (EditText) rootView
				.findViewById(R.id.etMinValue);
		EditText minNotice = (EditText) rootView.findViewById(R.id.etMinNotice);
		EditText restType = (EditText) rootView.findViewById(R.id.etRestType);

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		String uriString = (file.getAbsolutePath() + "/"
				+ "myDecryptedVendorFile" + ".chuchu");

		FileInputStream in = null;
		try {
			in = new FileInputStream(uriString);

			StringBuilder builder = new StringBuilder();
			int ch;
			while ((ch = in.read()) != -1) {
				builder.append((char) ch);
			}

			XMLParser parser = new XMLParser(getActivity());
			Document doc = parser.getDomElement(builder.toString()); // getting
																		// DOM
			// element

			NodeList nl = doc.getElementsByTagName(KEY_GENERAL_TAG);
			// looping through all song nodes <song>
			songsList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < nl.getLength(); i++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				Element e = (Element) nl.item(i);
				// adding each child node to HashMap key => value
				map.put(KEY_MIN_ORDER, parser.getValue(e, KEY_MIN_ORDER));
				map.put(KEY_MIN_DELIVERY, parser.getValue(e, KEY_MIN_DELIVERY));
				map.put(KEY_VENDOR_TYPE, parser.getValue(e, KEY_VENDOR_TYPE));

				songsList.add(map);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		minOrderValue.setText("Rs. " + songsList.get(0).get(KEY_MIN_ORDER)
				+ "/-");
		minNotice.setText(songsList.get(0).get(KEY_MIN_DELIVERY) + " Minutes");
		restType.setText(songsList.get(0).get(KEY_VENDOR_TYPE));

		minOrderValue.setEnabled(false);
		minNotice.setEnabled(false);
		restType.setEnabled(false);

//		Button setTimings = (Button) rootView.findViewById(R.id.bTimings);
//		setTimings.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				new NetworkTask().execute();
//
//			}
//		});

		return rootView;
	}

	class NetworkTask extends AsyncTask<Void, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result.contentEquals("yes")) {

				pDialog.dismiss();
				Intent i = new Intent(getActivity(), TimingsActivity.class);
				startActivity(i);

			} else {

				Toast.makeText(getActivity(),
						"Something went wrong. Please try again.",
						Toast.LENGTH_LONG).show();

			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());

			pDialog.setMessage("Please Wait...");
			pDialog.setCancelable(false);
			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			pDialog.show();

		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			if (downloadFile()) {
				return "yes";
			} else {
				return "no";
			}

		}

	}

	public boolean downloadFile() {
		try {
			URL url = new URL(
					"http://192.168.2.9/railtiffin/index.php?route=test/chuchu/time");
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

			String uriString = (file.getAbsolutePath() + "/" + "timings" + ".chuchu");

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
		return true;
	}

}
