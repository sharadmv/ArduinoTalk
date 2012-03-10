package com.sharad.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.flotype.bridge.Bridge;
import com.flotype.bridge.BridgeEventHandler;
import com.flotype.bridge.Service;

public class MainActivity extends Activity {
	String current = "";
	PushClient pusher;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final Bridge bridge = new Bridge().setApiKey("abcdefgh");
		bridge.connect();
		bridge.joinChannel("andruino", new Service() {
			public void push(String name, String message) {
				current += name + ": " + message + "\n";
				((TextView) findViewById(R.id.view)).setText(current);
			}
		});
		pusher = bridge.getChannel("andruino", PushClient.class);
		findViewById(R.id.button).setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				System.out.println("SENDING");
				pusher.push("Android", ((TextView) findViewById(R.id.text))
						.getText().toString());
			}

		});
	}
}
