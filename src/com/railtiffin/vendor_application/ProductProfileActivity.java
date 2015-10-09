package com.railtiffin.vendor_application;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ProductProfileActivity extends Activity {

	int[] mResources = { R.drawable.food1, R.drawable.food2, R.drawable.food3,
			R.drawable.food4, R.drawable.food5, R.drawable.food6 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_product_profile);

		CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this);

		ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mCustomPagerAdapter);

		EditText productName = (EditText) findViewById(R.id.etProductName);
		EditText productPrice = (EditText) findViewById(R.id.etProductPrice);
		productName.setEnabled(false);
		productPrice.setEnabled(false);

	}

	class CustomPagerAdapter extends PagerAdapter {

		Context mContext;
		LayoutInflater mLayoutInflater;

		public CustomPagerAdapter(Context context) {
			mContext = context;
			mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return mResources.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((LinearLayout) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View itemView = mLayoutInflater.inflate(R.layout.pager_item,
					container, false);

			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.ivProductImage);
			imageView.setImageResource(mResources[position]);

			container.addView(itemView);

			return itemView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((LinearLayout) object);
		}
	}

}
