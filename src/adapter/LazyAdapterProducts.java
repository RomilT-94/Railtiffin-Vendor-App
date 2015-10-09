package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.railtiffin.vendor_application.R;

/**
 * @author Romil
 *
 */
public class LazyAdapterProducts extends BaseAdapter {

	private Activity activity;
	String orderNumberAll[] = { "Product 1", "Product 2", "Product 3",
			"Product 4", "Product 5", "Product 6", "Product 7", "Product 8",
			"Product 9", };
	int productIcons[] = { R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, };
	private static LayoutInflater inflater = null;

	public LazyAdapterProducts(Activity a) {
		activity = a;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 9;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.products_list_row, null);

		TextView orderNumber = (TextView) vi.findViewById(R.id.tvOrderNumber);
		ImageView prodIcon = (ImageView) vi.findViewById(R.id.ivProductIcon);
		prodIcon.setImageResource(productIcons[position]);
		orderNumber.setText(orderNumberAll[position]);

		return vi;
	}

}
