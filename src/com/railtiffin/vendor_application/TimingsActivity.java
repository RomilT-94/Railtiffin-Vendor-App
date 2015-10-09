package com.railtiffin.vendor_application;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * @author Romil
 * 
 */
public class TimingsActivity extends FragmentActivity {

	private ViewPager mPager;
	private SlidingTabLayout mTabs;
	private String[] tabs = { "Slot 1", "Slot 2", "Slot 3" };

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.activity_timings);

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
				// NGO fragment activity
				return new SlotOneFragment();

			case 1:
				// Favorites fragment activity
				return new SlotTwoFragment();

			case 2:
				// Favorites fragment activity
				return new SlotThreeFragment();
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
