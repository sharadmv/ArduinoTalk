package com.sharad.android;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.flotype.bridge.*;

import com.flotype.bridge.Service;

public class MainActivity extends Activity {
	String current = "";
	PushClient pusher;
	TextView view;
	EditText text;
	String name;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		
// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		alert.setTitle("Enter your name");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					name = input.getText().toString();
					// Do something with value!
				}
			});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Canceled.
				}
			});
		
		alert.show();
		view = (TextView) findViewById(R.id.view);
		text = (EditText) findViewById(R.id.text);
		// final Bridge bridge = new Bridge().setApiKey("abcdefgh");
		// bridge.connect();
		final Handler handler = new Handler();
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		final Bridge bridge = new Bridge().setApiKey("abcdefgh");
		bridge.setEventHandler(new BridgeEventHandler() {
				public Runnable display= new Runnable(){
					public void run(){
						updateMessage();
					}
				};
			public void onReady() {
				bridge.joinChannel("andruino", new Service() {
					public void push(String name, String message) {
						System.out.println(name + ": " + message);
						current+=name+": "+message+"\n";
						handler.post(display);
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
	public void updateMessage() {
		view.setText(current);
	}

	public void sendMessage() {
		System.out.println("SENDING");
		pusher.push(name, ((EditText) findViewById(R.id.text)).getText()
				.toString());
		((EditText) findViewById(R.id.text)).setText("");
	}
}
