package com.railtiffin.vendor_application;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Romil
 * 
 */
public class ChatFragment extends ActionBarActivity {

	private ListView listView;
	private EditText chatText;
	private Button buttonSend;
	ChatArrayAdapter chatArrayAdapter;
	List<String> your_array_list;

	Intent intent;
	private boolean side = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_chat);
		getSupportActionBar().show();

		buttonSend = (Button) findViewById(R.id.buttonSend);

		listView = (ListView) findViewById(R.id.list);

		chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(),
				R.layout.activity_chat_singlemessage);
		listView.setAdapter(chatArrayAdapter);

		chatText = (EditText) findViewById(R.id.chatText);
		chatText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					return sendChatMessage();
				}
				return false;
			}
		});
		buttonSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sendChatMessage();
			}
		});

		listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listView.setAdapter(chatArrayAdapter);
		listView.setDividerHeight(0);

		// to scroll the list view to bottom on data change
		chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				listView.setSelection(chatArrayAdapter.getCount() - 1);
			}
		});

		processExtraData();
	}

	private boolean sendChatMessage() {

		new EmailTask().execute(chatText.getText().toString());

		chatArrayAdapter.add(new ChatMessage(side, chatText.getText()
				.toString()));
		chatText.setText("");
		side = !side;
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

		setIntent(intent);
		processExtraData();
	}

	private String processExtraData() {
		Intent intent = getIntent();
		// use the data received here

		String message = intent.getStringExtra("myChatMsg");
		if (message == null) {

			// Do Nothing

		} else {
			chatArrayAdapter.add(new ChatMessage(side, message));
			// chatArrayAdapter.notifyDataSetChanged();
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
					.show();
		}

		return message;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.chat_menu, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.attach:
			// Attach file function

			startActivity(new Intent(this, AttachDialogActivity.class));
		case R.id.clickPicture:

			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public class EmailTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String res = "";
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();

			try {

				HttpPost httpPost = new HttpPost(
						"http://192.168.2.30/gcm/gcm.php?mymsg=" + params[0]
								+ "&f=5");
				System.out.println(params[0]);

				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				res = EntityUtils.toString(response.getEntity());

				System.out.println(response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return res;
		}

	}

}
