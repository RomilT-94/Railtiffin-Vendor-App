package com.railtiffin.vendor_application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import adapter.LazyAdapterProcOrders;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Romil
 * 
 */
public class ProcOrdersFragment extends Fragment {

	ListView list;
	LazyAdapterProcOrders adapter;
	View rootView;
	String status;
	boolean myFlag;
	Intent intent;
	String KEY_ORDER_STATUS = "";

	// Keys used for Parsing

	public static final String KEY_ORDER = "order_info";
	public static final String KEY_SUBORDER = "suborder_id";
	public static final String KEY_SUBORDER_STATUS = "order_status_id";
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

	// Keys used for Parsing

	ArrayList<HashMap<String, String>> generalDetailsList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> foodDetailsList = new ArrayList<HashMap<String, String>>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.new_orders_fragment, container,
				false);

		SharedPreferences sharedPrefVendor = getActivity()
				.getSharedPreferences("vendorIDPref", Context.MODE_PRIVATE);
		String vendID = sharedPrefVendor.getString("vendorID", "0");
		new GetAcceptedOrders().execute(vendID);

		return rootView;

	}

	class GetAcceptedOrders extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Fetching Orders...");
			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		String res;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();

			String vendorID;
			try {
				vendorID = URLEncoder.encode(params[0], "utf-8");
				HttpPost httpPost = new HttpPost(
						ApplicationConstants.ACCEPTED_ORDERS_SERVER_URL
								+ vendorID);
				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				res = EntityUtils.toString(response.getEntity());
				System.out.println("Accepted Orders are: " + res);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String xyz = res.trim();
			if (xyz.contentEquals("<accepted></accepted>")) {
				return "error";
			} else {

				String x = res.replaceAll("<accepted>", "");
				String y = x.replaceAll(",</accepted>", "");

				return y.trim();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (result.contentEquals("error")) {
				CommonMethods.showToast("No Processing Orders", getActivity());
			} else {

				new SetupOrders().execute(result);
			}
		}

	}

	class SetupOrders extends
			AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Setting Orders..");
			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {
			// TODO Auto-generated method stub

			final String URL = ApplicationConstants.NEW_ORDER_SERVER_URL;

			String[] ids = params[0].split(",");

			for (int i = 0; i < ids.length; i++) {

				System.out.println("ID" + i + " = " + ids[i]);

				XMLParser parser = new XMLParser(getActivity());
				String xml = parser.getXmlFromUrl(URL + ids[i]);

				System.out.println("Order Detail: " + xml);

				Document doc = parser.getDomElement(xml);
				NodeList nl = doc.getElementsByTagName(KEY_ORDER);
				NodeList nlFood = doc.getElementsByTagName(KEY_ORDER_ITEM);
				for (int j = 0; j < nlFood.getLength(); j++) {
					HashMap<String, String> mapFoodDetails = new HashMap<String, String>();
					Element eFood = (Element) nlFood.item(j);
					mapFoodDetails.put(KEY_ORDER_NAME,
							parser.getValue(eFood, KEY_ORDER_NAME));
					mapFoodDetails.put(KEY_ORDER_QUANTITY,
							parser.getValue(eFood, KEY_ORDER_QUANTITY));
					mapFoodDetails.put(KEY_ORDER_DESC,
							parser.getValue(eFood, KEY_ORDER_DESC));
					foodDetailsList.add(mapFoodDetails);
				}

				for (int k = 0; k < nl.getLength(); k++) {
					// creating new HashMap
					HashMap<String, String> mapGenDetails = new HashMap<String, String>();
					Element e = (Element) nl.item(k);
					// adding each child node to HashMap key => value
					mapGenDetails.put(KEY_SUBORDER,
							parser.getValue(e, KEY_SUBORDER));
					mapGenDetails.put(KEY_SUBORDER_STATUS,
							parser.getValue(e, KEY_SUBORDER_STATUS));
					mapGenDetails.put(KEY_STATION,
							parser.getValue(e, KEY_STATION));
					mapGenDetails.put(KEY_TRAIN_NO,
							parser.getValue(e, KEY_TRAIN_NO));
					mapGenDetails.put(KEY_TRAIN_NAME,
							parser.getValue(e, KEY_TRAIN_NAME));
					mapGenDetails.put(KEY_COACH, parser.getValue(e, KEY_COACH));
					mapGenDetails.put(KEY_SEAT, parser.getValue(e, KEY_SEAT));
					mapGenDetails.put(KEY_STA, parser.getValue(e, KEY_STA));
					mapGenDetails.put(KEY_ETA, parser.getValue(e, KEY_ETA));
					mapGenDetails.put(KEY_DELIVERY_DAY,
							parser.getValue(e, KEY_DELIVERY_DAY));
					mapGenDetails.put(KEY_CUSTOMER_NAME,
							parser.getValue(e, KEY_CUSTOMER_NAME));
					mapGenDetails.put(KEY_CUSTOMER_NUMBER,
							parser.getValue(e, KEY_CUSTOMER_NUMBER));
					mapGenDetails.put(KEY_AMOUNT,
							parser.getValue(e, KEY_AMOUNT));
					mapGenDetails.put(KEY_STATUS_FLAG,
							parser.getValue(e, KEY_STATUS_FLAG));
					mapGenDetails.put(KEY_STANDARD_COMMENT,
							parser.getValue(e, KEY_STANDARD_COMMENT));
					mapGenDetails.put(KEY_ADDNL_COMMENT,
							parser.getValue(e, KEY_ADDNL_COMMENT));

					generalDetailsList.add(mapGenDetails);
				}

			}

			return generalDetailsList;
		}

		@Override
		protected void onPostExecute(
				final ArrayList<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			list = (ListView) rootView.findViewById(R.id.list);

			adapter = new LazyAdapterProcOrders(getActivity(), result);
			list.setAdapter(adapter);
			if (list.getCount() == 0) {
				rootView.findViewById(R.id.empty).setVisibility(View.VISIBLE);
			} else {
				rootView.findViewById(R.id.empty).setVisibility(View.INVISIBLE);
				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub

						Toast.makeText(getActivity(),
								"Clicked Item: " + position, Toast.LENGTH_SHORT)
								.show();

						String suborder, station, trainNo, trainName, coach, seat, status, sta, eta, delTime, custName, custNo, amount, stdComment, adnlComment;

						suborder = result.get(position).get(KEY_SUBORDER);
						station = result.get(position).get(KEY_STATION);
						trainNo = result.get(position).get(KEY_TRAIN_NO);
						trainName = result.get(position).get(KEY_TRAIN_NAME);
						coach = result.get(position).get(KEY_COACH);
						seat = result.get(position).get(KEY_SEAT);
						status = result.get(position).get(KEY_STATUS_FLAG);
						sta = result.get(position).get(KEY_STA);
						eta = result.get(position).get(KEY_ETA);
						delTime = result.get(position).get(KEY_DELIVERY_DAY);
						custName = result.get(position).get(KEY_CUSTOMER_NAME);
						custNo = result.get(position).get(KEY_CUSTOMER_NUMBER);
						amount = result.get(position).get(KEY_AMOUNT);
						stdComment = result.get(position).get(
								KEY_STANDARD_COMMENT);
						adnlComment = result.get(position).get(
								KEY_ADDNL_COMMENT);

						intent = new Intent(getActivity(),
								ProcOrderActivity.class);

						intent.putExtra("suborder", suborder);
						intent.putExtra("station", station);
						intent.putExtra("trainNo", trainNo);
						intent.putExtra("trainName", trainName);
						intent.putExtra("coach", coach);
						intent.putExtra("seat", seat);
						intent.putExtra("status", status);
						intent.putExtra("sta", sta);
						intent.putExtra("eta", eta);
						intent.putExtra("delTime", delTime);
						intent.putExtra("custName", custName);
						intent.putExtra("custNo", custNo);
						intent.putExtra("amount", amount);
						intent.putExtra("stdComment", stdComment);
						intent.putExtra("adnlComment", adnlComment);
						intent.putExtra("food", foodDetailsList);
						startActivity(intent);

					}
				});
			}
		}

	}

}
