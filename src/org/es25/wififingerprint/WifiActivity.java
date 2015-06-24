/*
 * Copyright (C) 2015 Armin Leghissa, Christian Rauecker
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */


package org.es25.wififingerprint;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Document that piece of shit, please.
 *
 * @author Armin Leghissa
 */
public class WifiActivity extends Activity {

	public Boolean runLoop = false;
	public Intent threadIntent;

	Thread thread = new Thread() {
		@Override
		public void run() {
			try {
				while (runLoop) {
					Thread.sleep(7000);
					startService(threadIntent);
					System.out.println("RUN FOREST RUN !!!");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi);

		IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		ResponseReceiver receiver = new ResponseReceiver();
		registerReceiver(receiver, filter);

		final Intent WifiIntent = new Intent(this, WifiFingerPrintDaemon.class);
		final Intent LearningIntent = new Intent(this, LearnLocation.class);

		threadIntent = WifiIntent;

		final Button startBT = (Button) findViewById(R.id.startBT);
		final Button learningBT = (Button) findViewById(R.id.learning_bt);

		startBT.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (!runLoop) {
					runLoop = true;
					thread.start();
					System.out.println("START RUNNING FOREST");
				} else {
					runLoop = false;
					System.out.println("STOP RUNNING FOREST STOP !!!");
				}

				//startService(WifiIntent);
			}
		});

		learningBT.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(LearningIntent);

			}
		});
	}


	/**
	 * Dafuq!
	 */
	public class ResponseReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP
				= "org.ex25.wififingerprint.MESSAGE_PROCESSED";


		@Override
		public void onReceive(Context context, Intent intent) {
			TextView showLocation = (TextView) findViewById(R.id.show_location_txt);
			String locationTxt = intent.getStringExtra(WifiFingerPrintDaemon.PARAM_OUTPUT);
			showLocation.setText(locationTxt);
		}
	}
}
