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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.io.FileNotFoundException;
import java.util.Set;
import org.es25.wififingerprint.WifiActivity.ResponseReceiver;
import org.es25.wififingerprint.struct1.Location;
import org.es25.wififingerprint.struct1.LocationMap;
import org.es25.wififingerprint.struct1.Station;


public class WifiFingerPrintDaemon extends IntentService {
	static private WifiManager wifimgr;
	public static final String PARAM_INPUT = "imsg";
	public static final String PARAM_OUTPUT = "omsg";
	public static final String NOTIFICATION = "org.es25.wififingerprint";
	public static final String APRESULT = "apresult";
	private static final String TAG = "WifiFingerPrintDaemon";
	private LocationMap learnedLocations;


	/**
	 * Construct the shit.
	 */
	public WifiFingerPrintDaemon() {
		super("org.es25.wififingerprint.WifiFingerPrintDaemon");
	}


	@Override
	protected void onHandleIntent(Intent arg0) {
		try {
			learnedLocations = Util.loadMap(openFileInput(Util.MAP_FILE));
		} catch (FileNotFoundException ex) {
			System.out.println("ERROR !! - Perform some learning scans first!");
		}

		Log.d(TAG, "========START WIFI-ING============");
		wifimgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifimgr.setWifiEnabled(true);

		////  START SCANNING AND FILTERING THE SHIT
		//////////////////////////////////////////////////////////////////////
		Log.d(TAG, "========START WIFI-SCAN============");
		wifimgr.startScan();
		Set<Station> stations = Util.filterScan(wifimgr.getScanResults());

		System.out.println(" ");
		System.out.println("LEARNED LOCATION MAP\n====================================================================");
		System.out.println(learnedLocations.getNames());
		System.out.println();

		System.out.println(" ");
		System.out.println("CURRENT SCAN RESULTS\n====================================================================");
		System.out.println(stations);
		System.out.println("");

		Location nearest = Util.triangulateLocation(learnedLocations, stations);

		System.out.println(" ");
		System.out.println("TRIANGULATED LOCATION\n====================================================================");
		System.out.println(nearest);
		System.out.println(" ");

		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra(PARAM_OUTPUT, nearest.getName());
		sendBroadcast(broadcastIntent);

		Log.d(TAG, "========Writing logfile============");
		try {
			Util.appendToLogfile(
					nearest,
					openFileOutput(Util.LOG_FILE, MODE_APPEND));
		} catch (FileNotFoundException ex) {
			System.out.println("ERROE !! - " + ex.getMessage());
		}
	}
}
