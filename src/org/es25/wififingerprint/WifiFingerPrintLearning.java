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
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.Set;
import org.es25.wififingerprint.struct.LocationMap;
import org.es25.wififingerprint.struct.Station;
import org.es25.wififingerprint.util.Algo;
import org.es25.wififingerprint.util.Corpus;
import org.es25.wififingerprint.util.IO;


/**
 * The RSSI learning service.
 * It loads the location map from csv-file, learns some new locations and
 * finally bashes all the stuff back to the csv database.
 *
 * @author Armin Leghissa
 */
public class WifiFingerPrintLearning extends IntentService {

	private static WifiManager wifimgr;
	private static final String TAG = "WifiFingerPrintLearning";
	public static final String PARAM_INPUT = "imsg";
	public static final String PARAM_OUTPUT = "omsg";

	private LocationMap locationMap = null;


	/**
	 * Construct that bastard.
	 */
	public WifiFingerPrintLearning() {
		super("org.es25.wififingerprint.WifiFingerPrintLearning");
	}


	/**
	 * The main loop of this thread.
	 *
	 * @param intent some goddam intent.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		String locationName = intent.getStringExtra(PARAM_INPUT);
		locationMap = new LocationMap();

		try {
			IO.loadMap(
					locationMap,
					openFileInput(IO.MAP_FILE));
		} catch (FileNotFoundException ex) {
			IO.loadMap(
					locationMap,
					new ByteArrayInputStream(Corpus.data.getBytes()));
		}

		System.out.println();
		System.out.println(
				"II CURRENTLY KNOWN LOCATIONS\n====================================================================");
		System.out.println(locationMap.getNames());
		System.out.println();

		Log.d(TAG, "========START WIFI-ING============");
		wifimgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifimgr.setWifiEnabled(true);

		Log.d(TAG, "========START WIFI-SCAN============");
		wifimgr.startScan();
		Set<Station> stations = Algo.filterScan(wifimgr.getScanResults(), true);

		System.out.println();
		System.out.println(
				"II SCANNED STATIONS: (for " + locationName + "):\n====================================================================");
		System.out.println(stations);
		System.out.println();

		// writing new scan and location to the location map.
		locationMap.add(locationName, stations);

		Log.d(TAG, "========START Writing============");
		try {
			IO.storeMap(
					locationMap,
					openFileOutput(IO.MAP_FILE, Context.MODE_PRIVATE));
		} catch (FileNotFoundException ex) {
			System.out.println("ERROR!! - " + ex.getMessage());
		}
	}


	/**
	 * Perform final crap and stuff.
	 */
	@Override
	public void onDestroy() {
		// write the csv file to stdout since it is not possible to:
		// - get the file with adb pull on non-modded monkey-fucking cellphones,
		// - anonymously send it per email to my cock-sucking address.
		IO.storeMap(locationMap, System.out);
	}
}
