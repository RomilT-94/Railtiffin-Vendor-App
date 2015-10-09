package com.railtiffin.vendor_application;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

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

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Romil
 * 
 */
public class OrderDetailsFragment extends Fragment {

	TextView trainDetails; // Train Number
	TextView trainNameView; // Train Name

	TextView runningFlag; // tvTimeLateFlag
	TextView sta, eta, dateDelivery, coachNumber, seatNumber, custName, custNo,
			amount;
	TextView orderNumber; // tvOrderNo

	TextView station; // tvStation

	View rootView;

	static final String KEY_DETAILS = "status_info";
	static final String KEY_ETA = "eta";
	static final String KEY_STAT = "train_status";

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.activity_order_detail, container,
				false);

		trainDetails = (TextView) rootView.findViewById(R.id.tvTrain);
		trainNameView = (TextView) rootView.findViewById(R.id.tvTrainName);
		runningFlag = (TextView) rootView.findViewById(R.id.tvTimeLateFlag);
		sta = (TextView) rootView.findViewById(R.id.tvSTA);
		eta = (TextView) rootView.findViewById(R.id.tvETA);
		dateDelivery = (TextView) rootView.findViewById(R.id.tvTodayTomFlag);
		coachNumber = (TextView) rootView.findViewById(R.id.tvCoach);
		seatNumber = (TextView) rootView.findViewById(R.id.tvSeat);
		custName = (TextView) rootView.findViewById(R.id.tvCustomerName);
		custNo = (TextView) rootView.findViewById(R.id.tvCustomerNumber);
		amount = (TextView) rootView.findViewById(R.id.tvAmount);
		orderNumber = (TextView) rootView.findViewById(R.id.tvOrderNo);
		station = (TextView) rootView.findViewById(R.id.tvStation);

		Intent i = getActivity().getIntent();

		String stationString, trainNo, trainName, coach, seat, status, staString, etaString, delTime, custNameString, custNoString, amountString;

		stationString = i.getStringExtra("station");
		trainNo = i.getStringExtra("trainNo");
		trainName = i.getStringExtra("trainName");
		coach = i.getStringExtra("coach");
		seat = i.getStringExtra("seat");
		status = i.getStringExtra("status");
		staString = i.getStringExtra("sta");
		etaString = i.getStringExtra("eta");
		delTime = i.getStringExtra("delTime");
		custNameString = i.getStringExtra("custName");
		custNoString = i.getStringExtra("custNo");
		amountString = i.getStringExtra("amount");
		String suborderId = i.getStringExtra("suborder");

		new FetchEtaTask().execute(suborderId);

		orderNumber.setText(suborderId);
		station.setText(stationString);
		trainDetails.setText(trainNo);
		trainNameView.setText(trainName);

		String[] scheduleSta = staString.split(" ");
		String[] staS = scheduleSta[1].split(":");
		String tts = staS[0] + ":" + staS[1];

		if (Integer.valueOf(staS[0]) > 12) {
			if (Integer.valueOf(staS[0]) > 9) {
				int hour = Integer.valueOf(staS[0]) - 12;
				sta.setText("0" + String.valueOf(hour) + ":" + staS[1] + " PM");
			} else {
				int hour = Integer.valueOf(staS[0]) - 12;
				sta.setText(String.valueOf(hour) + ":" + staS[1] + " PM");
			}
		} else {

			sta.setText(tts + " AM");
		}

		String[] delivery = delTime.split(" ");
		String date = delivery[0];
		String[] dates = date.trim().split("/");
		String newDate = dates[2] + "/" + dates[1] + "/" + dates[0];

		dateDelivery.setText(newDate);
		coachNumber.setText(coach);
		seatNumber.setText(seat);
		custName.setText(custNameString);
		custNo.setText(custNoString);
		amount.setText(amountString);

		custNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String uri = "tel:" + "0" + custNo.getText().toString().trim();
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse(uri));
				startActivity(intent);

			}
		});

		return rootView;
	}

	class FetchEtaTask extends AsyncTask<String, Void, Boolean> {

		ArrayList<HashMap<String, String>> foodDetailsList = new ArrayList<HashMap<String, String>>();
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Getting ETA..");
			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			boolean checker = false;
			try {

				String suborderID = URLEncoder.encode(params[0], "utf-8");

				HttpPost httpPost = new HttpPost(ApplicationConstants.ETA_URL
						+ suborderID);

				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				String res = EntityUtils.toString(response.getEntity());

				if (res.contains("No Data Available")) {
					// Show NA
					checker = false;
				} else {
					checker = true;
					final String URL = ApplicationConstants.ETA_URL
							+ suborderID;
					XMLParser parser = new XMLParser(getActivity());
					String xml = parser.getXmlFromUrl(URL);
					Document doc = parser.getDomElement(xml);
					NodeList nl = doc.getElementsByTagName(KEY_DETAILS);

					for (int j = 0; j < nl.getLength(); j++) {
						HashMap<String, String> mapFoodDetails = new HashMap<String, String>();
						Element eFood = (Element) nl.item(j);
						mapFoodDetails.put(KEY_ETA,
								parser.getValue(eFood, KEY_ETA));
						mapFoodDetails.put(KEY_STAT,
								parser.getValue(eFood, KEY_STAT));

						foodDetailsList.add(mapFoodDetails);
					}

				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return checker;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (result) {
				eta.setText(foodDetailsList.get(0).get(KEY_ETA));
				runningFlag.setText(foodDetailsList.get(0).get(KEY_STAT));
			} else {
				eta.setText("NA");
				runningFlag.setText("NA");
			}

		}

	}

}
