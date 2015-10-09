package com.railtiffin.vendor_application;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

public class AttachDialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.attach_dialog);

		ImageButton attachPhoto = (ImageButton) findViewById(R.id.ibAttachImage);
		ImageButton attachPdf = (ImageButton) findViewById(R.id.ibAttachPdf);

	}

}
