package com.railtiffin.vendor_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * @author Romil
 *
 */
public class CuisineActivity extends Activity {

	Button cuisineSelection;
	String cuisine = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_cuisine);

		cuisineSelection = (Button) findViewById(R.id.bSubmitSelection);
		cuisineSelection.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(CuisineActivity.this, MainActivity.class);
				i.putExtra("cuisine", cuisine);
				i.putExtra("vendor", "vendor");
				startActivity(i);
				finish();

			}
		});

	}

	public void onCheckboxClicked(View view) {
		// Is the button now checked?
		boolean checked = ((CheckBox) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.cbItalian:
			if (checked) {
				if (cuisine.contentEquals("")) {
					cuisine = cuisine + "Italian";
				} else {
					cuisine = cuisine + ", Italian";
				}
			} else {
				if (cuisine.contentEquals("Italian")) {
					cuisine = cuisine.replaceAll("Italian", "");
				} else {
					cuisine = cuisine.replaceAll(", Italian", "");
				}
			}
			break;
		case R.id.cbChinese:
			if (checked) {
				if (cuisine.contentEquals("")) {
					cuisine = cuisine + "Chinese";
				} else {
					cuisine = cuisine + ", Chinese";
				}
			} else {
				if (cuisine.contentEquals("Chinese")) {
					cuisine = cuisine.replaceAll("Chinese", "");
				} else {
					cuisine = cuisine.replaceAll(", Chinese", "");
				}
			}
			break;
		case R.id.cbMexican:
			if (checked) {
				if (cuisine.contentEquals("")) {
					cuisine = cuisine + "Mexican";
				} else {
					cuisine = cuisine + ", Mexican";
				}
			} else {
				if (cuisine.contentEquals("Mexican")) {
					cuisine = cuisine.replaceAll("Mexican", "");
				} else {
					cuisine = cuisine.replaceAll(", Mexican", "");
				}
			}
			break;
		case R.id.cbMughlai:
			if (checked) {
				if (cuisine.contentEquals("")) {
					cuisine = cuisine + "Mughlai";
				} else {
					cuisine = cuisine + ", Mughlai";
				}
			} else {
				if (cuisine.contentEquals("Mughlai")) {
					cuisine = cuisine.replaceAll("Mughlai", "");
				} else {
					cuisine = cuisine.replaceAll(", Mughlai", "");
				}
			}
			break;
		case R.id.cbPunjabi:
			if (checked) {
				if (cuisine.contentEquals("")) {
					cuisine = cuisine + "Punjabi";
				} else {
					cuisine = cuisine + ", Punjabi";
				}
			} else {
				if (cuisine.contentEquals("Punjabi")) {
					cuisine = cuisine.replaceAll("Punjabi", "");
				} else {
					cuisine = cuisine.replaceAll(", Punjabi", "");
				}
			}
			break;
		case R.id.cbSouth:
			if (checked) {
				if (cuisine.contentEquals("")) {
					cuisine = cuisine + "South Indian";
				} else {
					cuisine = cuisine + ", South Indian";
				}
			} else {
				if (cuisine.contentEquals("South Indian")) {
					cuisine = cuisine.replaceAll("South Indian", "");
				} else {
					cuisine = cuisine.replaceAll(", South Indian", "");
				}
			}
			break;
		case R.id.cbJain:
			if (checked) {
				if (cuisine.contentEquals("")) {
					cuisine = cuisine + "Jain";
				} else {
					cuisine = cuisine + ", Jain";
				}
			} else {
				if (cuisine.contentEquals("Jain")) {
					cuisine = cuisine.replaceAll("Jain", "");
				} else {
					cuisine = cuisine.replaceAll(", Jain", "");
				}
			}
			break;
		}
	}

}
