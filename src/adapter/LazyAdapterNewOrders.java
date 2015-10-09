package adapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.railtiffin.vendor_application.ApplicationConstants;
import com.railtiffin.vendor_application.CancelDialogActivity;
import com.railtiffin.vendor_application.LoginActivity;
import com.railtiffin.vendor_application.MainActivity;
import com.railtiffin.vendor_application.NewOrdersFragment;
import com.railtiffin.vendor_application.R;
import com.railtiffin.vendor_application.XMLParser;

/**
 * @author Romil
 * 
 */
public class LazyAdapterNewOrders extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	static final String KEY_ACCEPTED = "info";
	static final String KEY_ACCEPTED_ORDER = "suborder_id";

	public LazyAdapterNewOrders(Activity a, ArrayList<HashMap<String, String>> d) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.new_orders_list_row, null);

		TextView orderNumber = (TextView) vi.findViewById(R.id.tvOrderNumber);
		TextView trainDetails = (TextView) vi.findViewById(R.id.tvTrainDetails);
		TextView deliveryTime = (TextView) vi.findViewById(R.id.tvDeliveryTime);

		HashMap<String, String> generalDetailsRow = new HashMap<String, String>();
		generalDetailsRow = data.get(position);

		orderNumber.setText("Order No: "
				+ generalDetailsRow.get(NewOrdersFragment.KEY_SUBORDER));
		trainDetails.setText(generalDetailsRow
				.get(NewOrdersFragment.KEY_TRAIN_NO)
				+ "/ "
				+ generalDetailsRow.get(NewOrdersFragment.KEY_TRAIN_NAME));
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
