package com.railtiffin.vendor_application;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Romil
 * 
 */
public class ProcOrderActivity extends FragmentActivity {

	private ViewPager mPager;
	private SlidingTabLayout mTabs;
	private String[] tabs = { "General Details", "Order Details",
			"Order Status" };

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.activity_processing_order);

		mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
		mPager = (ViewPager) findViewById(R.id.pager);

		mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

		mTabs.setBackgroundColor(getResources().getColor(R.color.red));
		mTabs.setDistributeEvenly(true);

		mTabs.setViewPager(mPager);
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

			case 2:

				return new ProcessStatusActivity();

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
			return 3;
		}

	}

}
