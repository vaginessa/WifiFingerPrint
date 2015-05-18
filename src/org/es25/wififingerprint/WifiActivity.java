package org.es25.wififingerprint;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class WifiActivity extends Activity {
	private MyBroadcastAPList APListUpdateReceiver;
	String Test;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi);
		final Intent WifiIntent = new Intent(this, WifiFingerPrintDaemon.class);

		final Button startBT = (Button) findViewById(R.id.startBT);

		startBT.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startService(WifiIntent);
			}
		});

		IntentFilter intentFilter =
				new IntentFilter(WifiFingerPrintDaemon.NOTIFICATION);
		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(APListUpdateReceiver, intentFilter);
	}

	
	public class MyBroadcastAPList extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Test = intent.getStringExtra(WifiFingerPrintDaemon.APRESULT);
		}
	}
}
