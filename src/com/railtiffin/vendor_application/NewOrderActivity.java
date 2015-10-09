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
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NewOrderActivity extends AppCompatActivity {

	private ViewPager mPager;
	private SlidingTabLayout mTabs;
	private String[] tabs = { "General Details", "Order Details" };
	String acceptedIds;
	boolean error = false;
	String y = "";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.activity_new_orders);
		getSupportActionBar().show();
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
		mPager = (ViewPager) findViewById(R.id.pager);

		mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

		mTabs.setBackgroundColor(getResources().getColor(R.color.red));
		mTabs.setDistributeEvenly(true);

		mTabs.setViewPager(mPager);

		View customNav = LayoutInflater.from(this).inflate(
				R.layout.accept_decline, null); // layout which contains your
												// button.
		Button accept = (Button) customNav.findViewById(R.id.ibAcceptButton);
		Button decline = (Button) customNav.findViewById(R.id.ibDeclineButton);
		Intent i = getIntent();
		String suborder_id = i.getStringExtra("suborder").trim();
		CommonMethods.showToast(suborder_id, getApplicationContext());

		accept.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Toast.makeText(getApplicationContext(), "Accepted",
						Toast.LENGTH_SHORT).show();
				SharedPreferences prefs = v.getContext().getSharedPreferences(
						"UserDetails", Context.MODE_PRIVATE);
				String deviceID = prefs.getString(LoginActivity.REG_ID, "0");

				Intent i = getIntent();
				String suborder_id = i.getStringExtra("suborder").trim();

				CommonMethods.showToast(deviceID + "----" + suborder_id,
						getApplicationContext());

				new ContactServer().execute("2", suborder_id, deviceID);

			}
		});

		decline.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Toast.makeText(getApplicationContext(), "Declined",
						Toast.LENGTH_SHORT).show();
				SharedPreferences prefs = v.getContext().getSharedPreferences(
						"UserDetails", Context.MODE_PRIVATE);
				String deviceID = prefs.getString(LoginActivity.REG_ID, "0");

				Intent i = getIntent();
				String suborder_id = i.getStringExtra("suborder").trim();
				Intent in = new Intent(NewOrderActivity.this, CancelDialogActivity.class);
				in.putExtra("suborder_id", suborder_id);
				startActivity(in);
				finish();

				// new ContactServer().execute("25", suborder_id, deviceID);

			}
		});

		getSupportActionBar().setCustomView(customNav);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
	}

	class ContactServer extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(NewOrderActivity.this);
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

				if (params[0].contentEquals("2")) {

					String suborderID = URLEncoder.encode(params[1], "utf-8");
					String devID = URLEncoder.encode(params[2], "utf-8");

					HttpPost httpPost = new HttpPost(
							ApplicationConstants.STATUS_ORDERS_SERVER_URL + key
									+ "&sid=" + suborderID + "&by=" + devID);

					HttpResponse response = httpClient.execute(httpPost,
							localContext);
					res = EntityUtils.toString(response.getEntity());

				} else if (params[0].contentEquals("25")) {

					Intent i = getIntent();
					String suborder_id = i.getStringExtra("suborder").trim();

					String suborderID = URLEncoder.encode(params[1], "utf-8");
					String devID = URLEncoder.encode(params[2], "utf-8");

					HttpPost httpPost = new HttpPost(
							ApplicationConstants.STATUS_ORDERS_SERVER_URL + key
									+ "&sid=" + suborderID + "&by=" + devID);

					HttpResponse response = httpClient.execute(httpPost,
							localContext);
					res = EntityUtils.toString(response.getEntity());
					if (res.contains("<error>")) {
						res = "already accepted";
					} else {
						res = "done";
					}

				}

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
			if (result.contentEquals("already accepted")) {
				CommonMethods.showToast("Order Already Accepted",
						getApplicationContext());
			}
			startActivity(new Intent(NewOrderActivity.this, MainActivity.class));
			finish();

		}

	}

	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public android.support.v4.app.Fragment getItem(int arg0) {
			// TODO Auto-generated method stub

			switch (arg0) {
			case 0:

				return new OrderDetailsFragment();

			case 1:

				return new FoodFragment();

			}

			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub

			return tabs[position];
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.action_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case android.R.id.home:

			onBackPressed();

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
