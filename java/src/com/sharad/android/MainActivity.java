package com.sharad.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

import com.flotype.bridge.Bridge;
import com.flotype.bridge.BridgeEventHandler;
import com.flotype.bridge.Service;

public class MainActivity extends Activity {
	String current = "";
	PushClient pusher;
	TextView view;
	EditText text;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		view = (TextView) findViewById(R.id.view);
		text = (EditText) findViewById(R.id.text);
		// final Bridge bridge = new Bridge().setApiKey("abcdefgh");
		// bridge.connect();
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		final Bridge bridge = new Bridge().setApiKey("abcdefgh");
		bridge.setEventHandler(new BridgeEventHandler() {

			public void onReady() {
				bridge.joinChannel("andruino", new Service() {
					public void push(String name, String message) {
						System.out.println(name + ": " + message);
						current += name + ": " + message + "\n";
						view.setText(name + ": " + message + "\n");
					}
				});
				pusher = bridge.getChannel("andruino", PushClient.class);
				findViewById(R.id.button).setOnClickListener(
						new OnClickListener() {

							public void onClick(View arg0) {
								sendMessage();
							}

						});
				text.setOnKeyListener(new OnKeyListener() {

					public boolean onKey(View arg0, int keyCode, KeyEvent event) {
						if (event.getAction() == KeyEvent.ACTION_DOWN) {
							switch (keyCode) {
							case KeyEvent.KEYCODE_DPAD_CENTER:
							case KeyEvent.KEYCODE_ENTER:
								sendMessage();
								return true;
							default:
								break;
							}
						}
						return false;
					}

				});
			}

		});

		bridge.connect();
	}

	public void sendMessage() {
		System.out.println("SENDING");
		pusher.push("Android", ((EditText) findViewById(R.id.text)).getText()
				.toString());
		((EditText) findViewById(R.id.text)).setText("");
	}
}
