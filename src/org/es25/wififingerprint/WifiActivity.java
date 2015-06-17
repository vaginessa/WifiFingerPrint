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
	
	private MyBroadcastAPList APListUpdateReceiver;
	String Test;
	
	private static final String TAG = "WifiActivity";

	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_wifi);
		final Intent WifiIntent = new Intent(this, WifiFingerPrintDaemon.class);
		final Intent LearningIntent = new Intent(this, WifiFingerPrintLearning.class);
		
		
		 final Button startBT = (Button) findViewById(R.id.startBT);
		 final Button learningBT = (Button)findViewById(R.id.learning_bt);
		 
		  startBT.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		       	 startService(WifiIntent);
		        }
		    });		
		  
		  learningBT.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	startService(LearningIntent);
		        	
		        }
		    });	
		  
		IntentFilter intentFilter = new IntentFilter(WifiFingerPrintDaemon.NOTIFICATION);
		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
	    registerReceiver(APListUpdateReceiver, intentFilter);
	}
	
	
	public class MyBroadcastAPList extends BroadcastReceiver {

		  @Override
		  public void onReceive(Context context, Intent intent) {
			  System.out.println("TEST ===========> DICK");
		   Test = intent.getStringExtra(WifiFingerPrintDaemon.APRESULT);
		  
		  }
		 }
	
  
	
	
}
