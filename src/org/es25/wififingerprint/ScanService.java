package org.es25.wififingerprint;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

public class ScanService extends IntentService {
	private boolean stopped = false;
	private WifiManager wifimgr;
	
	
	/**
	 * 
	 */
	public ScanService() {
		super("org.es25.wififingerprint.ScanService");
		this.wifimgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		this.wifimgr.setWifiEnabled(true);
	}

	
	/**
	 * 
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		
	}

}
