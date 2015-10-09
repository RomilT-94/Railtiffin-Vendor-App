package adapter;

import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.railtiffin.vendor_application.CancelledOrdersActivity;
import com.railtiffin.vendor_application.MainActivity;
import com.railtiffin.vendor_application.NewOrdersFragment;
import com.railtiffin.vendor_application.R;

/**
 * @author Romil
 * 
 */
public class LazyAdapterCancelledOrders extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public LazyAdapterCancelledOrders(Activity a,
			ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.cancel_orders_row, null);

		TextView orderNumber = (TextView) vi.findViewById(R.id.tvOrderNumber);
		TextView trainDetails = (TextView) vi.findViewById(R.id.tvTrainDetails);
		TextView deliveryTime = (TextView) vi.findViewById(R.id.tvDeliveryTime);
		TextView contact = (TextView) vi
				.findViewById(R.id.tvContactPersonDetails);

		HashMap<String, String> generalDetailsRow = new HashMap<String, String>();
		generalDetailsRow = data.get(position);
		final String suborderID = generalDetailsRow
				.get(CancelledOrdersActivity.KEY_SUBORDER);
		orderNumber.setText("Cancelled Order No: "
				+ generalDetailsRow.get(CancelledOrdersActivity.KEY_SUBORDER));
		trainDetails
				.setText(generalDetailsRow
						.get(CancelledOrdersActivity.KEY_TRAIN_NO)
						+ " / "
						+ generalDetailsRow
								.get(CancelledOrdersActivity.KEY_TRAIN_NAME));

		String[] delivery = generalDetailsRow
				.get(CancelledOrdersActivity.KEY_DELIVERY_DAY).trim()
				.split(" ");
		String date = delivery[0];
		String time = delivery[1];
		String[] dates = date.trim().split("/");
		String newDate = dates[2] + "/" + dates[1] + "/" + dates[0];

		deliveryTime.setText("Date: " + newDate + "   Time: " + time);

		contact.setText(generalDetailsRow
				.get(CancelledOrdersActivity.KEY_CUSTOMER_NAME)
				+ "    "
				+ generalDetailsRow
						.get(CancelledOrdersActivity.KEY_CUSTOMER_NUMBER));

		Button accept = (Button) vi.findViewById(R.id.bOK);

		accept.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				SharedPreferences cancelOrders = v.getContext()
						.getSharedPreferences("cancelledPref",
								Context.MODE_PRIVATE);
				String cancelID = cancelOrders.getString("cancelID", "0");
				String committer = cancelID.replaceAll(suborderID.trim() + ",",
						"");
				SharedPreferences.Editor editor = cancelOrders.edit();
				editor.putString("cancelID", committer);
				editor.commit();
				data.remove(position);
				notifyDataSetChanged();
				checkEmpty();

			}
		});

		return vi;
	}

	public void checkEmpty() {
		if (data.isEmpty()) {
			activity.startActivity(new Intent(activity, MainActivity.class));
		}
	}

}
