package org.es25.wififingerprint;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.app.Activity;
import android.app.IntentService;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;


public class WifiFingerPrintDaemon extends IntentService {
	static private WifiManager wifimgr;
	private IntentFilter wifiFilter = new IntentFilter();
	private BroadcastReceiver wifiReceiver;
	private List<ScanResult> apList = null;
	public static final String NOTIFICATION = "org.es25.wififingerprint";
	public static final String APRESULT = "apresult";
	String TestString = "TESTANZEIGE";
	private static final String TAG = "WifiService";
	
	
	public WifiFingerPrintDaemon() {
		super("org.es25.wififingerprint.WifiFingerPrintDaemon");
				
	}


	@Override
	protected void onHandleIntent(Intent arg0) {
		System.out.println("========START WIFI ZEIG============");
		wifimgr = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		wifimgr.setWifiEnabled(true);		
		wifiFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		
		
		
		wifiReceiver = new BroadcastReceiver() {
			    @Override
			    public void onReceive(Context context, Intent wifiFilter) {
			      apList = wifimgr.getScanResults();
			      System.out.println("+++++++RECEIVER SCANRESULTS+++++++++");
			      System.out.println("APPPPPPPPPPPPPPP" + apList.toString());
			      
			    }
			  };
			//nicht vergessen bei einem destroy wieder unregister zu machen
			//noch keinen plan wo
			 registerReceiver(wifiReceiver, wifiFilter);
		
			 wifimgr.startScan();
			 System.out.println("RESULTSSSSSS====>" + wifimgr.getScanResults().toString());
		
		
		//this.startWifiScan();
		
		System.out.println("HALLO");
		Log.e("MSGSAFAFDSAFSAFSA", "TESTSTETSETSTES");
		
		 //send update 
		   Intent intentUpdate = new Intent();
		   intentUpdate.setAction(NOTIFICATION);
		   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
		   intentUpdate.putExtra(APRESULT, TestString);
		   sendBroadcast(intentUpdate);
		
	}
	
	
	private void startWifiScan(){
			
		if(wifimgr.isWifiEnabled()){
			System.out.println("PRINTINTINTINTINTINTNTINT --------------");
			wifimgr.startScan();			
		}		
		
	}


	@Override
	public void onDestroy(){
		
		unregisterReceiver(wifiReceiver);
		
	}
	
}
