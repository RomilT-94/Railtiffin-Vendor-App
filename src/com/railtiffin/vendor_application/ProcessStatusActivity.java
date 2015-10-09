package com.railtiffin.vendor_application;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author Romil
 * 
 */
public class ProcessStatusActivity extends Fragment {

	View rootView;
	ImageView ivProc, ivCook, ivPack, ivReady, ivDispatch, ivDeliver;
	ImageButton ivClickedPicture;
	int STATUS_KEY = 0;
	Uri imageUri;
	ProgressDialog prgDialog;
	String encodedString;
	RequestParams params = new RequestParams();
	private static final int CAMERA_REQUEST = 1888;
	List<String> deliveryGuysList;
	Spinner spinner;
	Button goAhead;
	Button uploadImage, uploadContact;
	EditText newDelContactName, newDelContactNum;
	String status = "", statusBg = "";
	TextView counter;
	int imageCounter = 0;

	public static final String KEY_CONTACT = "contact_person";
	public static final String KEY_CONTACT_NAME = "name";
	public static final String KEY_CONTACT_NUMBER = "mobile";
	public static final String KEY_CONTACT_DESIGNATION = "designation";
	public static final String KEY_CONTACT_ID = "person_id";
	String KEY_ORDER_STATUS = "";

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.activity_processing_status,
				container, false);

		Button next = (Button) rootView.findViewById(R.id.bNext);
		uploadContact = (Button) rootView.findViewById(R.id.bUploadContact);
		newDelContactName = (EditText) rootView.findViewById(R.id.etDGuyName);
		newDelContactNum = (EditText) rootView.findViewById(R.id.etDGuyNumber);
		counter = (TextView) rootView.findViewById(R.id.tvPhotoContactCounter);
		counter.setVisibility(View.GONE);

		ivClickedPicture = (ImageButton) rootView
				.findViewById(R.id.ivClickedPicture);

		ivClickedPicture.setVisibility(View.GONE);
		uploadContact.setVisibility(View.GONE);
		newDelContactName.setVisibility(View.GONE);
		newDelContactNum.setVisibility(View.GONE);
		uploadImage = (Button) rootView.findViewById(R.id.bUploadPicture);
		uploadImage.setVisibility(View.GONE);
		spinner = (Spinner) rootView.findViewById(R.id.delivery_guys);
		spinner.setVisibility(View.GONE);
		goAhead = (Button) rootView.findViewById(R.id.bForward);
		goAhead.setVisibility(View.GONE);

		ivProc = (ImageView) rootView.findViewById(R.id.ivProc);
		ivCook = (ImageView) rootView.findViewById(R.id.ivCook);
		ivPack = (ImageView) rootView.findViewById(R.id.ivPack);
		ivReady = (ImageView) rootView.findViewById(R.id.ivReady);
		ivDispatch = (ImageView) rootView.findViewById(R.id.ivDispatch);
		ivDeliver = (ImageView) rootView.findViewById(R.id.ivDelivered);

		Intent i = getActivity().getIntent();
		String suborder = i.getStringExtra("suborder");
		new FetchStatusTask().execute(suborder);

		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "New Picture");
		values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
		imageUri = getActivity().getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		final Button goAhead = (Button) rootView.findViewById(R.id.bForward);
		goAhead.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (spinner.getSelectedItem().toString().trim()
						.contentEquals("Add Another Contact")) {

					uploadContact.setVisibility(View.VISIBLE);
					newDelContactName.setVisibility(View.VISIBLE);
					newDelContactNum.setVisibility(View.VISIBLE);

				} else {
					uploadContact.setVisibility(View.GONE);
					newDelContactName.setVisibility(View.GONE);
					newDelContactNum.setVisibility(View.GONE);

					Toast.makeText(getActivity(),
							spinner.getSelectedItem().toString(),
							Toast.LENGTH_LONG).show();

					counter.setText(String.valueOf(imageCounter)
							+ " images uploaded. Delivery Boy details shared are: "
							+ spinner.getSelectedItem().toString());

					new TagContactToOrderTask().execute(getActivity()
							.getIntent().getStringExtra("suborder").trim(),
							guysList.get(spinner.getSelectedItemPosition())
									.get(KEY_CONTACT_ID));

				}

			}
		});

		uploadContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!newDelContactName.getText().toString().contentEquals("")
						&& !newDelContactNum.getText().toString()
								.contentEquals("")) {

					if (newDelContactNum.getText().toString().length() != 10) {
						CommonMethods.showToast(
								"Please Enter a valid 10 digit Number",
								getActivity());
					} else {

						SharedPreferences sharedPrefVendor = getActivity()
								.getSharedPreferences("vendorIDPref",
										Context.MODE_PRIVATE);
						String vendID = sharedPrefVendor.getString("vendorID",
								"0");

						counter.setText(String.valueOf(imageCounter)
								+ " images uploaded. Delivery Boy details shared are: "
								+ newDelContactName.getText().toString() + ", "
								+ newDelContactNum.getText().toString());

						new UploadContactTask().execute(vendID,
								newDelContactName.getText().toString(),
								newDelContactNum.getText().toString(),
								"Delivery Boy");

					}

				} else {
					CommonMethods.showToast("Please fill both fields.",
							getActivity());
				}

			}
		});

		ivClickedPicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CAMERA_REQUEST);

			}
		});

		uploadImage.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (sendPhoto != null) {

					imageCounter++;
					counter.setText(String.valueOf(imageCounter)
							+ " images uploaded.");

					String timeStamp = new SimpleDateFormat(
							"yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

					SharedPreferences sharedPrefVendorCheck = v.getContext()
							.getSharedPreferences("vendorIDPref",
									Context.MODE_PRIVATE);
					String vendID = sharedPrefVendorCheck.getString("vendorID",
							"0");

					params.put("filename", vendID + "image" + timeStamp
							+ ".jpg");
					prgDialog.setMessage("Converting Image to Binary Data");
					prgDialog.show();
					// Convert image to String using Base64
					encodeImagetoString(vendID + "image" + timeStamp + ".jpg");
					ivClickedPicture.setImageResource(R.drawable.image_click);
					// When Image is not selected from Gallery
				} else {
					Toast.makeText(getActivity(),
							"You must click image before you try to upload",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (STATUS_KEY == 1) {

					STATUS_KEY = 2;
					ivProc.setImageResource(R.drawable.block);
					ivCook.setImageResource(R.drawable.block1);

					new MarkStatusTask().execute("21");

				} else if (STATUS_KEY == 2) {

					STATUS_KEY = 3;
					ivCook.setImageResource(R.drawable.block);
					ivPack.setImageResource(R.drawable.block1);

					new MarkStatusTask().execute("22");

				} else if (STATUS_KEY == 3) {

					STATUS_KEY = 4;
					ivPack.setImageResource(R.drawable.block);
					ivReady.setImageResource(R.drawable.block1);

					ivClickedPicture.setVisibility(View.VISIBLE);
					uploadImage.setVisibility(View.VISIBLE);
					counter.setVisibility(View.VISIBLE);
					counter.setText(String.valueOf(imageCounter)
							+ " images uploaded.");

					new MarkStatusTask().execute("23");

					SharedPreferences sharedPrefVendor = getActivity()
							.getSharedPreferences("vendorIDPref",
									Context.MODE_PRIVATE);
					String vendID = sharedPrefVendor.getString("vendorID", "0");

					new GetDeliveryGuys().execute(vendID);

				} else if (STATUS_KEY == 4) {

					STATUS_KEY = 5;
					ivReady.setImageResource(R.drawable.block);
					ivDispatch.setImageResource(R.drawable.block1);

					ivClickedPicture.setVisibility(View.GONE);
					uploadImage.setVisibility(View.GONE);

					spinner.setVisibility(View.VISIBLE);
					goAhead.setVisibility(View.VISIBLE);
					// uploadContact.setVisibility(View.VISIBLE);
					// newDelContactName.setVisibility(View.VISIBLE);
					// newDelContactNum.setVisibility(View.VISIBLE);

					new MarkStatusTask().execute("24");

				} else if (STATUS_KEY == 5) {

					STATUS_KEY = 6;
					ivDispatch.setImageResource(R.drawable.block);
					ivDeliver.setImageResource(R.drawable.block1);

					spinner.setVisibility(View.GONE);
					goAhead.setVisibility(View.GONE);
					// uploadContact.setVisibility(View.GONE);
					// newDelContactName.setVisibility(View.GONE);
					// newDelContactNum.setVisibility(View.GONE);

					new MarkStatusTask().execute("8");

				} else if (STATUS_KEY == 6) {

					STATUS_KEY = 0;
					ivDeliver.setImageResource(R.drawable.block);

				}

			}
		});

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		prgDialog = new ProgressDialog(getActivity());
		// Set cancelable as False
		prgDialog.setCancelable(false);

		Intent i = getActivity().getIntent();
		String suborder = i.getStringExtra("suborder");
		new FetchStatusTask().execute(suborder);

		counter.setText(String.valueOf(imageCounter) + " images uploaded.");

	}

	public void encodeImagetoString(final String fName) {
		new AsyncTask<Void, Void, String>() {

			protected void onPreExecute() {

			};

			@Override
			protected String doInBackground(Void... params) {

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// Must compress the Image to reduce image size to make upload
				// easy
				sendPhoto.compress(Bitmap.CompressFormat.JPEG, 50, stream);
				byte[] byte_arr = stream.toByteArray();
				// Encode Image to String
				encodedString = Base64.encodeToString(byte_arr, 0);
				return "";
			}

			@Override
			protected void onPostExecute(String msg) {
				prgDialog.setMessage("Calling Upload");
				// Put converted Image string into Async Http Post param
				params.put("image", encodedString);
				// Trigger Image upload
				triggerImageUpload(fName, encodedString);
			}
		}.execute(null, null, null);
	}

	public void triggerImageUpload(String fName, String image) {
		makeHTTPCall();
	}

	public void makeHTTPCall() {
		prgDialog.setMessage("Uploading...");
		AsyncHttpClient client = new AsyncHttpClient();

		Intent i = getActivity().getIntent();
		String suborder = i.getStringExtra("suborder");
		params.put("sid", suborder);
		SharedPreferences sharedPrefVendorCheck = getActivity()
				.getSharedPreferences("vendorIDPref", Context.MODE_PRIVATE);
		String vendID = sharedPrefVendorCheck.getString("vendorID", "0");
		params.put("vid", vendID);

		client.post(ApplicationConstants.IMAGE_UPLOAD_SERVER_URL, params,
				new AsyncHttpResponseHandler() {
					// When the response returned by REST has Http
					// response code '200'
					@Override
					public void onSuccess(String response) {
						// Hide Progress Dialog
						prgDialog.hide();
						prgDialog.dismiss();
						Toast.makeText(getActivity(), response,
								Toast.LENGTH_LONG).show();
					}

					// When the response returned by REST has Http
					// response code other than '200' such as '404',
					// '500' or '403' etc
					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						// Hide Progress Dialog
						prgDialog.hide();
						// When Http response code is '404'
						if (statusCode == 404) {
							Toast.makeText(getActivity(),
									"Requested resource not found",
									Toast.LENGTH_LONG).show();
						}
						// When Http response code is '500'
						else if (statusCode == 500) {
							Toast.makeText(getActivity(),
									"Something went wrong at server end",
									Toast.LENGTH_LONG).show();
						}
						// When Http response code other than 404, 500
						else {
							Toast.makeText(
									getActivity(),
									"Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
											+ statusCode, Toast.LENGTH_LONG)
									.show();
						}
					}
				});
	}

	Bitmap thumbnail;
	Bitmap sendPhoto;

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

			try {
				thumbnail = MediaStore.Images.Media.getBitmap(getActivity()
						.getContentResolver(), imageUri);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Bitmap photo = (Bitmap) data.getExtras().get("data");
			// MediaStore.Images.Media.insertImage(getActivity()
			// .getContentResolver(), thumbnail, "originalClick",
			// "Original");
			if (thumbnail != null) {

				int original_width = thumbnail.getWidth();
				int original_height = thumbnail.getHeight();
				int new_width = original_width;
				int new_height = original_height;

				// first check if we need to scale width
				if (original_width > 800) {
					// scale width to fit
					new_width = 800;
					// scale height to maintain aspect ratio
					new_height = (new_width * original_height) / original_width;
				}

				// then check if we need to scale even with the new height
				if (new_height > 600) {
					// scale height to fit instead
					new_height = 600;
					// scale width to maintain aspect ratio
					new_width = (new_height * original_width) / original_height;
				}

				sendPhoto = Bitmap.createScaledBitmap(thumbnail, new_width,
						new_height, true);
				String filename = getFilename();
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(filename);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				sendPhoto.compress(Bitmap.CompressFormat.JPEG, 50, out);
				// MediaStore.Images.Media.insertImage(getActivity()
				// .getContentResolver(), sendPhoto, "compressedClick",
				// "Compressed");

				ivClickedPicture.setImageBitmap(thumbnail);
			} else {
				CommonMethods.showToast("Failed. Try Again", getActivity());
			}
		}
	}

	public String getFilename() {
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Media/SentImages");
		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/"
				+ System.currentTimeMillis() + ".jpg");
		return uriSting;

	}

	@SuppressWarnings("unused")
	private String saveToInternalSorage(Bitmap bitmapImage) {
		ContextWrapper cw = new ContextWrapper(getActivity());
		// path to /data/data/yourapp/app_data/imageDir
		File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
		// Create imageDir
		File mypath = new File(directory, "profile.jpg");

		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(mypath);

			// Use the compress method on the BitMap object to write image to
			// the OutputStream
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 0, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return directory.getAbsolutePath();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		prgDialog.dismiss();
	}

	class MarkStatusTask extends AsyncTask<String, Void, Void> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Marking Status...");
			pDialog.setCancelable(false);
			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();

			Intent i = getActivity().getIntent();
			String suborder_id = i.getStringExtra("suborder").trim();
			SharedPreferences prefs = getActivity().getSharedPreferences(
					"UserDetails", Context.MODE_PRIVATE);
			String deviceID = prefs.getString(LoginActivity.REG_ID, "0");

			String key;
			String suborderID;
			String devID;
			try {
				key = URLEncoder.encode(params[0], "utf-8");
				suborderID = URLEncoder.encode(suborder_id, "utf-8");
				devID = URLEncoder.encode(deviceID, "utf-8");

				HttpPost httpPost = new HttpPost(
						ApplicationConstants.STATUS_ORDERS_SERVER_URL + key
								+ "&sid=" + suborderID + "&by=" + devID);
				HttpResponse response = httpClient.execute(httpPost,
						localContext);

			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pDialog.dismiss();

		}

	}

	ArrayList<HashMap<String, String>> guysList;

	class GetDeliveryGuys extends AsyncTask<String, Void, List<String>> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Getting Delivery Guys...");
			pDialog.setCancelable(false);
			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			pDialog.show();
		}

		@Override
		protected List<String> doInBackground(String... params) {
			// TODO Auto-generated method stub
			String URL = "";
			try {
				URL = ApplicationConstants.VENDOR_SERVER_URL
						+ URLEncoder.encode(params[0], "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			XMLParser parser = new XMLParser(getActivity());
			String xml = parser.getXmlFromUrl(URL);
			Document doc = parser.getDomElement(xml);
			NodeList nl = doc.getElementsByTagName(KEY_CONTACT);
			guysList = new ArrayList<HashMap<String, String>>();
			for (int j = 0; j < nl.getLength(); j++) {
				HashMap<String, String> mapFoodDetails = new HashMap<String, String>();
				Element e = (Element) nl.item(j);

				if (parser.getValue(e, KEY_CONTACT_DESIGNATION).trim()
						.contentEquals("Delivery Boy")) {

					mapFoodDetails.put(KEY_CONTACT_NAME,
							parser.getValue(e, KEY_CONTACT_NAME) + ", "
									+ parser.getValue(e, KEY_CONTACT_NUMBER));
					mapFoodDetails.put(KEY_CONTACT_ID,
							parser.getValue(e, KEY_CONTACT_ID));

					System.out.println("C P: "
							+ parser.getValue(e, KEY_CONTACT_NAME));

				}
				guysList.add(mapFoodDetails);
			}
			List<String> list = new ArrayList<String>();
			for (int k = 0; k < nl.getLength(); k++) {
				list.add(guysList.get(k).get(KEY_CONTACT_NAME));
				System.out.println("List: "
						+ guysList.get(k).get(KEY_CONTACT_NAME));

			}
			list.add("Add Another Contact");

			return list;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_spinner_item, result);
			System.out.println("Guys: " + result.toString());

			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// // Apply the adapter to the spinner
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub

					if (spinner.getSelectedItem().toString().trim()
							.contentEquals("Add Another Contact")) {

						uploadContact.setVisibility(View.VISIBLE);
						newDelContactName.setVisibility(View.VISIBLE);
						newDelContactNum.setVisibility(View.VISIBLE);
						goAhead.setVisibility(View.GONE);

					} else {
						uploadContact.setVisibility(View.GONE);
						newDelContactName.setVisibility(View.GONE);
						newDelContactNum.setVisibility(View.GONE);
						goAhead.setVisibility(View.VISIBLE);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});

		}

	}

	class UploadContactTask extends AsyncTask<String, Void, Void> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Getting Delivery Guys...");
			pDialog.setCancelable(false);
			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			try {

				String vendorID = URLEncoder.encode(params[0], "utf-8");
				String name = URLEncoder.encode(params[1], "utf-8");
				String number = URLEncoder.encode(params[2], "utf-8");
				String designation = URLEncoder.encode(params[3], "utf-8");
				String suborder = URLEncoder.encode(getActivity().getIntent()
						.getStringExtra("suborder").trim(), "utf-8");

				HttpPost httpPost = new HttpPost(
						ApplicationConstants.VENDOR_CONTACT_SERVER_URL
								+ vendorID + "&name=" + name + "&mb=" + number
								+ "&des=" + designation + "&sid=" + suborder);

				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				String res = EntityUtils.toString(response.getEntity());

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
		}

	}

	class TagContactToOrderTask extends AsyncTask<String, Void, Void> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Sending Delivery Guy Info...");
			pDialog.setCancelable(false);
			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			try {

				String suborder = URLEncoder.encode(params[0], "utf-8");
				String personID = URLEncoder.encode(params[1], "utf-8");

				HttpPost httpPost = new HttpPost(
						ApplicationConstants.CONTACT_UPLOAD_SERVER_URL
								+ suborder + "&pid=" + personID);

				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				String res = EntityUtils.toString(response.getEntity());

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
		}

	}

	public class FetchStatusTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(getActivity());
			super.onPreExecute();
			// pDialog.setMessage("Posting Feedback....");
			pDialog.setCancelable(false);
			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			pDialog.setMessage("Please Wait...");
			pDialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != "") {
				pDialog.dismiss();
				status = result.trim();

				if (status.contentEquals("processing")) {

					ivProc.setImageResource(R.drawable.block1);
					STATUS_KEY = 1;

				} else if (status.contentEquals("cooking")) {

					ivCook.setImageResource(R.drawable.block1);
					STATUS_KEY = 2;

				} else if (status.contentEquals("packing")) {

					ivPack.setImageResource(R.drawable.block1);
					STATUS_KEY = 3;

				} else if (status.contentEquals("ready")) {

					ivReady.setImageResource(R.drawable.block1);
					STATUS_KEY = 4;
					ivClickedPicture.setVisibility(View.VISIBLE);
					uploadImage.setVisibility(View.VISIBLE);

				} else if (status.contentEquals("dispatched")) {

					ivDispatch.setImageResource(R.drawable.block1);
					STATUS_KEY = 5;
					SharedPreferences sharedPrefVendor = getActivity()
							.getSharedPreferences("vendorIDPref",
									Context.MODE_PRIVATE);
					String vendID = sharedPrefVendor.getString("vendorID", "0");

					new GetDeliveryGuys().execute(vendID);
					spinner.setVisibility(View.VISIBLE);
					goAhead.setVisibility(View.VISIBLE);
					// uploadContact.setVisibility(View.VISIBLE);
					// newDelContactName.setVisibility(View.VISIBLE);
					// newDelContactNum.setVisibility(View.VISIBLE);

				} else if (status.contentEquals("delivered")) {

					ivDeliver.setImageResource(R.drawable.block1);
					STATUS_KEY = 6;
					uploadContact.setVisibility(View.GONE);
					newDelContactName.setVisibility(View.GONE);
					newDelContactNum.setVisibility(View.GONE);

				}
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String res = "";
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();

			String suborder_id = params[0];

			try {
				String suborderID = URLEncoder.encode(suborder_id, "utf-8");
				HttpPost httpPost = new HttpPost(
						ApplicationConstants.ORDER_STATUS_SERVER_URL
								+ suborderID);

				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				res = EntityUtils.toString(response.getEntity());
				String x = res.replaceAll("<status>", "");
				String y = x.replaceAll("</status>", "");

				statusBg = y.trim();

				if (statusBg.contentEquals("2")) {
					KEY_ORDER_STATUS = "processing";
				} else if (statusBg.contentEquals("21")) {
					KEY_ORDER_STATUS = "cooking";
				} else if (statusBg.contentEquals("22")) {
					KEY_ORDER_STATUS = "packing";
				} else if (statusBg.contentEquals("23")) {
					KEY_ORDER_STATUS = "ready";
				} else if (statusBg.contentEquals("24")) {
					KEY_ORDER_STATUS = "dispatched";
				} else if (statusBg.contentEquals("8")) {
					KEY_ORDER_STATUS = "delivered";
				}

				System.out.println(response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return KEY_ORDER_STATUS;
		}

	}

}
