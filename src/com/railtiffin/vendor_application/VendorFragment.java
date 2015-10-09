package com.railtiffin.vendor_application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Romil
 *
 */
public class VendorFragment extends Fragment {

	private ViewPager mPager;
	private SlidingTabLayout mTabs;
	private String[] tabs = { "General Info", "Other Info" };
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.activity_vendor_profile,
				container, false);

		mTabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
		mPager = (ViewPager) rootView.findViewById(R.id.pager);

		mPager.setAdapter(new MyPagerAdapter(getActivity()
				.getSupportFragmentManager()));

		mTabs.setBackgroundColor(getResources().getColor(R.color.red));
		mTabs.setDistributeEvenly(true);

		mTabs.setViewPager(mPager);

		return rootView;
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
				return new GeneralInfoFragment();

			case 1:
				// Favorites fragment activity
				return new OtherInfoFragment();

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

}
