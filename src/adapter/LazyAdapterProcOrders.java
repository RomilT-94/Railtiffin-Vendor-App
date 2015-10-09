package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.railtiffin.vendor_application.NewOrdersFragment;
import com.railtiffin.vendor_application.ProcOrdersFragment;
import com.railtiffin.vendor_application.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LazyAdapterProcOrders extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	static final String KEY_ACCEPTED = "accepted";
	static final String KEY_ACCEPTED_ORDER = "suborder_id";

	public LazyAdapterProcOrders(Activity a,
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
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.proc_orders_list_row, null);

		TextView orderNumber = (TextView) vi.findViewById(R.id.tvOrderNumber);
		TextView trainDetails = (TextView) vi.findViewById(R.id.tvTrainDetails);
		TextView deliveryTime = (TextView) vi.findViewById(R.id.tvDeliveryTime);
		TextView status = (TextView) vi.findViewById(R.id.tvOrderStatus);

		HashMap<String, String> generalDetailsRow = new HashMap<String, String>();
		generalDetailsRow = data.get(position);

		String statusId = generalDetailsRow
				.get(ProcOrdersFragment.KEY_SUBORDER_STATUS);
		String KEY_ORDER_STATUS = "";

		if (statusId.contentEquals("2")) {
			KEY_ORDER_STATUS = "Processing";
		} else if (statusId.contentEquals("21")) {
			KEY_ORDER_STATUS = "Cooking";
		} else if (statusId.contentEquals("22")) {
			KEY_ORDER_STATUS = "Packing";
		} else if (statusId.contentEquals("23")) {
			KEY_ORDER_STATUS = "Ready for Delivery";
		} else if (statusId.contentEquals("24")) {
			KEY_ORDER_STATUS = "Delivery Dispatched";
		} else if (statusId.contentEquals("8")) {
			KEY_ORDER_STATUS = "Delivered";
		} else {
			KEY_ORDER_STATUS = "Error";
		}

		status.setText(KEY_ORDER_STATUS);

		orderNumber.setText("Order No: "
				+ generalDetailsRow.get(ProcOrdersFragment.KEY_SUBORDER));
		trainDetails.setText(generalDetailsRow
				.get(ProcOrdersFragment.KEY_TRAIN_NO)
				+ "/ "
				+ generalDetailsRow.get(ProcOrdersFragment.KEY_TRAIN_NAME));
		String[] delivery = generalDetailsRow
				.get(NewOrdersFragment.KEY_DELIVERY_DAY).trim().split(" ");
		String date = delivery[0];
		String time = delivery[1];
		String[] dates = date.trim().split("/");
		String newDate = dates[2] + "/" + dates[1] + "/" + dates[0];

		deliveryTime.setText("Date: " + newDate + "   Time: " + time);

		return vi;
	}

}
