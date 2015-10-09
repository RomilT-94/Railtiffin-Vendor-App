package com.railtiffin.vendor_application;

import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import adapter.LazyAdapterCancelledOrders;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

public class CancelledOrdersActivity extends Activity {

	public static final String KEY_ORDER = "order_info";
	public static final String KEY_SUBORDER = "suborder_id";
	public static final String KEY_STATION = "station";
	public static final String KEY_TRAIN_NO = "train";
	public static final String KEY_TRAIN_NAME = "train_name";
	public static final String KEY_COACH = "coach";
	public static final String KEY_SEAT = "seat";
	public static final String KEY_STATUS_FLAG = "status";
	public static final String KEY_STA = "delivery_time";
	public static final String KEY_ETA = "eta";
	public static final String KEY_DELIVERY_DAY = "delivery_time";
	public static final String KEY_CUSTOMER_NAME = "name";
	public static final String KEY_CUSTOMER_NUMBER = "mobile";
	public static final String KEY_AMOUNT = "amount_to_be_collected";
	public static final String KEY_ORDER_DETAILS = "Order_details";
	public static final String KEY_ORDER_ITEM = "Order_item";
	public static final String KEY_ORDER_NAME = "Order_item_name";
	public static final String KEY_ORDER_QUANTITY = "Order_quantity";
	public static final String KEY_ORDER_DESC = "Order_Discription";
	public static final String KEY_STANDARD_COMMENT = "comment";
	public static final String KEY_ADDNL_COMMENT = "addn_comment";

	ArrayList<HashMap<String, String>> generalDetailsList = new ArrayList<HashMap<String, String>>();
	ListView list;
	LazyAdapterCancelledOrders adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_cancel_orders);

		SharedPreferences cancelOrderCheck = getSharedPreferences(
				"cancelledPref", Context.MODE_PRIVATE);
		String cancelID = cancelOrderCheck.getString("cancelID", "0");
		new FetchCancelledDetailsTask().execute(cancelID);
	}

	class FetchCancelledDetailsTask extends
			AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(CancelledOrdersActivity.this);
			pDialog.setMessage("Fetching Orders...");
			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {
			// TODO Auto-generated method stub

			String[] ids = params[0].split(",");

			final String URL = ApplicationConstants.NEW_ORDER_SERVER_URL;

			for (int i = 0; i < ids.length; i++) {

				System.out.println("ID" + i + " = " + ids[i]);

				XMLParser parser = new XMLParser(CancelledOrdersActivity.this);
				String xml = parser.getXmlFromUrl(URL + ids[i]);

				System.out.println("Order Detail: " + xml);

				Document doc = parser.getDomElement(xml);
				NodeList nl = doc.getElementsByTagName(KEY_ORDER);

				for (int k = 0; k < nl.getLength(); k++) {
					// creating new HashMap
					HashMap<String, String> mapGenDetails = new HashMap<String, String>();
					Element e = (Element) nl.item(k);
					// adding each child node to HashMap key => value
					mapGenDetails.put(KEY_SUBORDER,
							parser.getValue(e, KEY_SUBORDER));
					// mapGenDetails.put(KEY_STATION,
					// parser.getValue(e, KEY_STATION));
					mapGenDetails.put(KEY_TRAIN_NO,
							parser.getValue(e, KEY_TRAIN_NO));
					mapGenDetails.put(KEY_TRAIN_NAME,
							parser.getValue(e, KEY_TRAIN_NAME));
					// mapGenDetails.put(KEY_COACH, parser.getValue(e,
					// KEY_COACH));
					// mapGenDetails.put(KEY_SEAT, parser.getValue(e,
					// KEY_SEAT));
					// mapGenDetails.put(KEY_STA, parser.getValue(e, KEY_STA));
					// mapGenDetails.put(KEY_ETA, parser.getValue(e, KEY_ETA));
					mapGenDetails.put(KEY_DELIVERY_DAY,
							parser.getValue(e, KEY_DELIVERY_DAY));
					mapGenDetails.put(KEY_CUSTOMER_NAME,
							parser.getValue(e, KEY_CUSTOMER_NAME));
					mapGenDetails.put(KEY_CUSTOMER_NUMBER,
							parser.getValue(e, KEY_CUSTOMER_NUMBER));
					// mapGenDetails.put(KEY_AMOUNT,
					// parser.getValue(e, KEY_AMOUNT));
					// mapGenDetails.put(KEY_STATUS_FLAG,
					// parser.getValue(e, KEY_STATUS_FLAG));
					// mapGenDetails.put(KEY_STANDARD_COMMENT,
					// parser.getValue(e, KEY_STANDARD_COMMENT));
					// mapGenDetails.put(KEY_ADDNL_COMMENT,
					// parser.getValue(e, KEY_ADDNL_COMMENT));

					generalDetailsList.add(mapGenDetails);
				}

			}

			return generalDetailsList;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pDialog.dismiss();

			list = (ListView) findViewById(R.id.list);

			adapter = new LazyAdapterCancelledOrders(
					CancelledOrdersActivity.this, result);
			list.setAdapter(adapter);

		}

	}

}
