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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Document that piece of shit, please.
 *
 * @author Armin Leghissa
 */
public class LearnLocation extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_location);

		final Intent LearningIntent = new Intent(this,
				WifiFingerPrintLearning.class);

		final Button perfomLearnBT = (Button) findViewById(R.id.learnLocation_bt);

		perfomLearnBT.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				EditText locationInput = (EditText) findViewById(R.id.locationName_txt);
				String msg = locationInput.getText().toString();
				if (msg.isEmpty()) {
					msg = "New Location";
				}
				locationInput.getText().clear();
				LearningIntent.putExtra(WifiFingerPrintLearning.PARAM_INPUT,
						msg);
				startService(LearningIntent);
			}
		});
	}
}
