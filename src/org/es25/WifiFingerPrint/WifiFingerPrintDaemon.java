package org.es25.WifiFingerPrint;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;


public class WifiFingerPrintDaemon extends IntentService {
	static private WifiManager wifimgr;
	private IntentFilter wifiFilter = new IntentFilter();
	private BroadcastReceiver wifiReceiver;
	private List<ScanResult> apList = null;
	
	
	public WifiFingerPrintDaemon(String name) {
		super(name);
		wifimgr = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		wifimgr.setWifiEnabled(true);		
		wifiFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		
		
		wifiReceiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent wifiFilter) {
		      apList = wifimgr.getScanResults();
		    }
		  };
		//nicht vergessen bei einem destroy wieder unregister zu machen
		//noch keinen plan wo
		 registerReceiver(wifiReceiver, wifiFilter);
		
	}


	@Override
	protected void onHandleIntent(Intent arg0) {		
		this.startWifiScan();
	}
	
	
	private void startWifiScan(){
			
		if(wifimgr.isWifiEnabled()){
			wifimgr.startScan();			
		}			
		
	}
	
	

}
