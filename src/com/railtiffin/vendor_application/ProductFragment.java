package com.railtiffin.vendor_application;

import adapter.LazyAdapterProducts;
import android.content.Intent;
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
public class ProductFragment extends Fragment {

	ListView list;
	LazyAdapterProducts adapter;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.new_orders_fragment, container,
				false);
		list = (ListView) rootView.findViewById(R.id.list);

		adapter = new LazyAdapterProducts(getActivity());
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Toast.makeText(getActivity(), "Clicked Item: " + arg2,
						Toast.LENGTH_SHORT).show();

				Intent i = new Intent(getActivity(),
						ProductProfileActivity.class);
				startActivity(i);

			}
		});

		return rootView;

	}

}
