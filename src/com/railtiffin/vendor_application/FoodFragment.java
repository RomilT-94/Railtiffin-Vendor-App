package com.railtiffin.vendor_application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Romil
 * 
 */
public class FoodFragment extends Fragment {

	View rootView;
	TableLayout tlOrder;
	TextView itemName, itemQuantity;

	static final String KEY_ORDER_NAME = "Order_item_name";
	static final String KEY_ORDER_QUANTITY = "Order_quantity";
	static final String KEY_ORDER_DESC = "Order_Discription";

	List<String> foodName = new ArrayList<String>();
	List<String> foodQuant = new ArrayList<String>();
	List<String> foodDesc = new ArrayList<String>();

	ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.food_fragment, container, false);
		tlOrder = (TableLayout) rootView.findViewById(R.id.tlOrder);
		tlOrder.removeAllViews();

		// Getters and Setters

		songsList = (ArrayList<HashMap<String, String>>) getActivity()
				.getIntent().getSerializableExtra("food");
		String adCmt = getActivity().getIntent().getStringExtra("adnlComment");
		String sCmt = getActivity().getIntent().getStringExtra("stdComment");
		String suborderID = getActivity().getIntent()
				.getStringExtra("suborder");

		for (int i = 0; i < songsList.size(); i++) {

			String name = songsList.get(i).get(KEY_ORDER_NAME);
			String desc = songsList.get(i).get(KEY_ORDER_DESC);
			String quant = songsList.get(i).get(KEY_ORDER_QUANTITY);

			if (name.contains(suborderID + "chuchu")) {
				String[] names = name.split(suborderID + "chuchu");
				foodName.add(names[1]);
			}
			if (desc.contains(suborderID + "chuchu")) {
				String[] descs = desc.split(suborderID + "chuchu");
				foodDesc.add(descs[1]);
			}
			if (quant.contains(suborderID + "chuchu")) {
				String[] quants = quant.split(suborderID + "chuchu");
				foodQuant.add(quants[1]);
			}

		}

		init();

		TextView standComments = (TextView) rootView
				.findViewById(R.id.tvStandComments);
		TextView addnComments = (TextView) rootView
				.findViewById(R.id.tvAddnComments);

		standComments.setText(adCmt);
		addnComments.setText(sCmt);

		return rootView;
	}

	public void init() {

		for (int i = 0; i < foodName.size(); i++) {

			TableRow row = new TableRow(getActivity());

			TableRow.LayoutParams lp = new TableRow.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT);
			lp.setMargins(20, 10, 0, 10);
			row.setLayoutParams(lp);

			itemName = new TextView(getActivity());

			itemQuantity = new TextView(getActivity());

			itemName.setLayoutParams(lp);

			itemName.setText(foodName.get(i) + foodDesc.get(i));
			itemQuantity.setText(foodQuant.get(i));

			row.setBackgroundResource(R.drawable.table_bg);

			Display display = getActivity().getWindowManager()
					.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;

			itemName.setWidth((width * 2) / 3);
			itemQuantity.setWidth(width / 3);
			itemQuantity.setGravity(Gravity.CENTER);

			itemName.setTextColor(getResources().getColor(R.color.black));
			itemName.setTextSize(18);

			itemQuantity.setTextColor(getResources().getColor(R.color.black));
			itemQuantity.setTextSize(18);
			itemQuantity.setWidth(200);

			row.addView(itemName);
			row.addView(itemQuantity);

			tlOrder.addView(row, i);
		}
	}

}
