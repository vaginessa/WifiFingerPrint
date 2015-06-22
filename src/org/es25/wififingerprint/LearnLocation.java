package org.es25.wififingerprint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
				if(msg.isEmpty()){
					msg = "New Location";
				}
				LearningIntent.putExtra(WifiFingerPrintLearning.PARAM_INPUT,
						msg);

				startService(LearningIntent);
			}
		});

	}

}
