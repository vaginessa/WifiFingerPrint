package org.es25.wififingerprint;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiFingerPrintLearning extends IntentService {

	static private WifiManager wifimgr;
	private List<ScanResult> apList = null;	
	private static final String RSSILearningMap ="RssiLearningMap.csv";
	private static final String TAG = "WifiFingerPrintLearning";
	
	
	public WifiFingerPrintLearning() {
		super("org.es25.wififingerprint.WifiFingerPrintLearning");
	
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		File file = new File(getFilesDir(), RSSILearningMap);
		Log.d(TAG,"LogFile Directory" + getFilesDir());
		
		FileOutputStream outStream;

		Log.d(TAG,"========START WIFI-ING============");
		wifimgr = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		wifimgr.setWifiEnabled(true);		
		
			  
			 Log.d(TAG,"========START WIFI-SCAN============");
			 wifimgr.startScan();			 
		     apList = wifimgr.getScanResults();		
		
			 try {
				 Log.d(TAG,"========START Writing============" + apList);
				  outStream = openFileOutput(RSSILearningMap, Context.MODE_PRIVATE);
				  for(String a : buildRssiList(apList)){
					  outStream.write(a.getBytes()); 
				  }				  
				  outStream.close();
				} catch (Exception e) {
				  e.printStackTrace();
				}
	}
	
	
	
	
	private List<String> buildRssiList(final List<ScanResult> scr){		
		 List<String> rssiList = new ArrayList<String>();
		 
		 for(ScanResult r : scr){
			 //!!! dont forget the calculateSignalLevel achtung bug in android 2.3.3!!!
			 rssiList.add(String.format("%s,%s,%d\n",r.SSID, r.BSSID,r.level));
		 }		 
		 return rssiList;
	}

}
