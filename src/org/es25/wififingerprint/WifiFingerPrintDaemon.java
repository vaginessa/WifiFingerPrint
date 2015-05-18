package org.es25.wififingerprint;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;


public class WifiFingerPrintDaemon extends IntentService {
	static private WifiManager wifimgr;
	private List<ScanResult> apList = null;
	public static final String NOTIFICATION = "org.es25.wififingerprint";
	public static final String APRESULT = "apresult";
	private static final String TAG = "WifiFingerPrintDaemon";
	private static final String RSSILogFile = "RssiLogFile.csv";

	public WifiFingerPrintDaemon() {
		super("org.es25.wififingerprint.WifiFingerPrintDaemon");
	}


	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.d(TAG, "LogFile Directory" + getFilesDir());

		FileOutputStream outStream;

		Log.d(TAG, "========START WIFI-ING============");
		wifimgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifimgr.setWifiEnabled(true);
		registerReceiver(wifiReceiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		Log.d(TAG, "========START WIFI-SCAN============");
		wifimgr.startScan();
		apList = wifimgr.getScanResults();

		try {
			Log.d(TAG, "========START Writing============" + apList);
			outStream = openFileOutput(RSSILogFile, Context.MODE_PRIVATE);
			for (String a : buildRssiList(apList)) {
				outStream.write(a.getBytes());
			}
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * //send update Intent intentUpdate = new Intent();
		 * intentUpdate.setAction(NOTIFICATION);
		 * intentUpdate.addCategory(Intent.CATEGORY_DEFAULT); //
		 * intentUpdate.putExtra(APRESULT, TestString);
		 * sendBroadcast(intentUpdate);
		 */
	}

	
	@Override
	public void onDestroy() {
		unregisterReceiver(wifiReceiver);

	}

	
	private List<String> buildRssiList(final List<ScanResult> scr) {
		List<String> rssiList = new ArrayList<String>();

		for (ScanResult r : scr) {
			// !!! dont forget the calculateSignalLevel achtung bug in android
			// 2.3.3!!!
			rssiList.add(String.format("%s,%s,%d\n", r.SSID, r.BSSID, r.level));
		}
		return rssiList;
	}

	
	private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// WifiManager
			// wifiMan=(WifiManager).getSystemService(Context.WIFI_SERVICE);
			// wifiMan.startScan();
			// int newRssi = wifiMan.getConnectionInfo().getRssi();
			// Toast.makeText(getActivity(), ""+newRssi,
			// Toast.LENGTH_SHORT).show();

			apList = wifimgr.getScanResults();
			System.out.println(apList);
		}
	};
}
