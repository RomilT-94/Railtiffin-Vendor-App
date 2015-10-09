package com.railtiffin.vendor_application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Romil
 * 
 */
public class GeneralInfoFragment extends Fragment {

	View rootView;
	boolean editButton = false;
	ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	static final String KEY_GENERAL_TAG = "general";
	static final String KEY_VENDOR_NAME = "name";
	static final String KEY_VENDOR_ADDRESS = "address";
	static final String KEY_VENDOR_CITY = "city";
	static final String KEY_VENDOR_STATE = "state";
	static final String KEY_VENDOR_ZIP = "zip";
	static final String KEY_VENDOR_EMAIL = "email";
	static final String KEY_VENDOR_CUISINE = "cuisines";

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.activity_general_info, container,
				false);

		final EditText name = (EditText) rootView.findViewById(R.id.etName);
		final EditText address = (EditText) rootView
				.findViewById(R.id.etAddress);
		final EditText city = (EditText) rootView.findViewById(R.id.etCity);
		final EditText state = (EditText) rootView.findViewById(R.id.etState);
		final EditText pincode = (EditText) rootView
				.findViewById(R.id.etPincode);
		// final EditText custName = (EditText) rootView
		// .findViewById(R.id.etContactPerson);
		// final EditText custNumber = (EditText) rootView
		// .findViewById(R.id.etContactNumber);
		// final EditText custEmail = (EditText) rootView
		// .findViewById(R.id.etEmail);
		final EditText cuisine = (EditText) rootView
				.findViewById(R.id.etCuisine);

		try {
			decrypt();
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		String uriString = (file.getAbsolutePath() + "/"
				+ "myDecryptedVendorFile" + ".chuchu");

		FileInputStream in = null;
		try {
			in = new FileInputStream(uriString);

			StringBuilder builder = new StringBuilder();
			int ch;
			while ((ch = in.read()) != -1) {
				builder.append((char) ch);
			}

			XMLParser parser = new XMLParser(getActivity());
			Document doc = parser.getDomElement(builder.toString()); // getting
																		// DOM
			// element

			NodeList nl = doc.getElementsByTagName(KEY_GENERAL_TAG);
			// looping through all song nodes <song>
			songsList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < nl.getLength(); i++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				Element e = (Element) nl.item(i);
				// adding each child node to HashMap key => value
				map.put(KEY_VENDOR_NAME, parser.getValue(e, KEY_VENDOR_NAME));
				map.put(KEY_VENDOR_ADDRESS,
						parser.getValue(e, KEY_VENDOR_ADDRESS));
				map.put(KEY_VENDOR_CITY, parser.getValue(e, KEY_VENDOR_CITY));
				map.put(KEY_VENDOR_STATE, parser.getValue(e, KEY_VENDOR_STATE));
				map.put(KEY_VENDOR_ZIP, parser.getValue(e, KEY_VENDOR_ZIP));
				map.put(KEY_VENDOR_EMAIL, parser.getValue(e, KEY_VENDOR_EMAIL));
				map.put(KEY_VENDOR_CUISINE,
						parser.getValue(e, KEY_VENDOR_CUISINE));

				songsList.add(map);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		name.setText(songsList.get(0).get(KEY_VENDOR_NAME));
		address.setText(songsList.get(0).get(KEY_VENDOR_ADDRESS));
		city.setText(songsList.get(0).get(KEY_VENDOR_CITY));
		state.setText(songsList.get(0).get(KEY_VENDOR_STATE));
		pincode.setText(songsList.get(0).get(KEY_VENDOR_ZIP));
		// custName.setText("Mr. XYZ");
		// custNumber.setText("9999999999");
		// custEmail.setText(songsList.get(0).get(KEY_VENDOR_EMAIL));
		cuisine.setText(songsList.get(0).get(KEY_VENDOR_CUISINE)
				.replace(",", " "));

		Intent i = getActivity().getIntent();
		String setCuisine = i.getStringExtra("cuisine");
		if (setCuisine != null) {

			cuisine.setText(setCuisine);
		}

		name.setEnabled(false);
		address.setEnabled(false);
		city.setEnabled(false);
		state.setEnabled(false);
		pincode.setEnabled(false);
		// custName.setEnabled(false);
		// custNumber.setEnabled(false);
		// custEmail.setEnabled(false);
		cuisine.setEnabled(false);

//		final Button edit = (Button) rootView.findViewById(R.id.bEdit);
		final Button editCuisine = (Button) rootView
				.findViewById(R.id.bEditCuisine);
		editCuisine.setVisibility(View.INVISIBLE);

//		edit.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (editButton) {
//					// TODO Auto-generated method stub
//					name.setText(name.getText().toString());
//					address.setText(address.getText().toString());
//					city.setText(city.getText().toString());
//					state.setText(state.getText().toString());
//					pincode.setText(pincode.getText().toString());
//					// custName.setText(custName.getText().toString());
//					// custNumber.setText(custNumber.getText().toString());
//					// custEmail.setText(custEmail.getText().toString());
//
//					edit.setText("EDIT");
//					editButton = false;
//
//					name.setEnabled(false);
//					address.setEnabled(false);
//					city.setEnabled(false);
//					state.setEnabled(false);
//					pincode.setEnabled(false);
//					// custName.setEnabled(false);
//					// custNumber.setEnabled(false);
//					// custEmail.setEnabled(false);
//					editCuisine.setVisibility(View.INVISIBLE);
//
//				} else {
//
//					edit.setText("SUBMIT");
//					editButton = true;
//					name.setEnabled(true);
//					address.setEnabled(true);
//					city.setEnabled(true);
//					state.setEnabled(true);
//					pincode.setEnabled(true);
//					// custName.setEnabled(true);
//					// custNumber.setEnabled(true);
//					// custEmail.setEnabled(true);
//					editCuisine.setVisibility(View.VISIBLE);
//
//				}
//			}
//		});

		editCuisine.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(getActivity(), CuisineActivity.class);
				startActivity(i);
				getActivity().finish();
			}
		});

		return rootView;

	}

	public void decrypt() throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException {

		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "RailTiffin/Documents/Sensitive");
		String uriString = (file.getAbsolutePath() + "/"
				+ "myEncryptedVendorFile" + ".chuchu");
		String uriDecryptedString = (file.getAbsolutePath() + "/"
				+ "myDecryptedVendorFile" + ".chuchu");

		FileInputStream fis = new FileInputStream(uriString);

		FileOutputStream fos = new FileOutputStream(uriDecryptedString);
		SecretKeySpec sks = new SecretKeySpec("Romil_Tiwar1-i_@".getBytes(),
				"AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, sks);
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		int b;
		byte[] d = new byte[8];
		while ((b = cis.read(d)) != -1) {
			fos.write(d, 0, b);
		}
		fos.flush();
		fos.close();
		cis.close();
	}

	public boolean deleteFile() {

		File file = new File(
				Environment.getExternalStorageDirectory().getPath()
						+ "/RailTiffin/Documents/Sensitive/myDecryptedVendorFile.chuchu");
		boolean deleted = file.delete();
		return deleted;

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		deleteFile();

	}

}
