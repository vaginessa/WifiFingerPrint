package org.es25.wififingerprint;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WifiActivity extends Activity {
	

	
	private static final String TAG = "WifiActivity";

	
	

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
		

		
		 final Button startBT = (Button) findViewById(R.id.startBT);
		 final Button learningBT = (Button)findViewById(R.id.learning_bt);
		 
		  startBT.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		       	 startService(WifiIntent);
		        }
		    });		
		  
		  learningBT.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	//startService(LearningIntent);
		        	startActivity(LearningIntent);
		        	
		        }
		    });	
		  
		

	}
	
	
	public class ResponseReceiver extends BroadcastReceiver {
		   public static final String ACTION_RESP =    
		      "org.ex25.wififingerprint.MESSAGE_PROCESSED";
		    
		   @Override
		   public void onReceive(Context context, Intent intent) {
		       TextView showLocation = (TextView) findViewById(R.id.show_location_txt);
		       String locationTxt = intent.getStringExtra(WifiFingerPrintDaemon.PARAM_OUTPUT);
		       showLocation.setText(locationTxt);
		    }
		}
	
  
	
	
}
