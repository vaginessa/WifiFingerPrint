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

import android.content.Context;
import android.net.wifi.ScanResult;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.es25.wififingerprint.struct1.Location;
import org.es25.wififingerprint.struct1.LocationMap;
import org.es25.wififingerprint.struct1.Station;


/**
 * Common utility functions for this program.
 *
 * @author Armin Leghissa
 */
public class Util {
	private static final String DRUGS_MSG = "Whitch fucking drugs did they take ???";

	/** File name for the csv log file. */
	public static final String LOG_FILE = "Leghissa_1357869_Rauecker_0757491.log";

	/** File name for the {@link LocationMap} database. */
	public static final String MAP_FILE = "RssiLearningMap.csv";


	/**
	 * Calculates a percentage value for the signal quality from a given rssi value.
	 *
	 * @param dBm The dBm/RSSI value to convert.
	 * @return Quality level as a percentage value.
	 */
	public static int rssi2quality(int dBm) {
		if (dBm >= -50) {
			return 100;
		} else if (dBm <= -100) {
			return 0;
		} else {
			return 2 * (dBm + 100);
		}
	}


	/**
	 * Performs a median filter on the given set of {@link Station}s.
	 * All elements lower than the median of the sorted list will be dropped.
	 *
	 * @param stations Stations set to perform the filter on.
	 */
	public static void medianFilter(TreeSet<Station> stations) {
		int median = stations.size() / 2;

		for (int i = 0; i < median; i++)
			stations.pollFirst();
	}


	/**
	 * Performs a median filter on a raw scan and converts the rssi values to quality levels.
	 *
	 * @param raw_scan List of {@link ScanResult}s coming from the {@link WifiManager}.
	 * @return A beautiful set of {@link Station}s, ready for locality estimation.
	 */
	public static Set<Station> filterScan(List<ScanResult> raw_scan) {
		TreeSet<Station> scan = new TreeSet<Station>();

		for (ScanResult res : raw_scan) {
			Station station = new Station(
					res.BSSID, // the mac address
					rssi2quality(res.level));

			scan.add(station);
		}

		Util.medianFilter(scan);
		return scan;
	}


	/**
	 * Calculates the euclidian distance between a bunch of stations from the DB and a station set from a runtime scan.
	 *
	 * @param loc A {@link Location} object from the {@link LocationMap}, the database.
	 * @param scan A set of {@link Station}s created from a runtime scan.
	 * @return the euclidian distance between the intersecting access points.
	 */
	public static float eucDist(Location loc, Set<Station> scan) {
		double res = 0;

		for (Station r : scan) {
			Integer loc_rssi;

			if ((loc_rssi = loc.getRssiFor(r.mac)) != null)
				res += Math.pow((loc_rssi - r.rssi), 2);
		}

		return (float) Math.sqrt(res);
	}


	/**
	 * Triangulates the {@link Location} in the {@link LocationMap} DB for a given scanning set.
	 * It uses the euclidian distance algorithem to determin the location with the lowest distance to the given set.
	 *
	 * @param learned the location map of learned rssi fingerprints.
	 * @param scanned a set constructed by a runtime scan.
	 * @return The triangulated location with the common stations with db locations.
	 */
	public static Location triangulateLocation(LocationMap learned, Set<Station> scanned) {
		Float dist = null;
		Location loc = null;

		for (Location curr : learned) {
			float curr_dist = eucDist(curr, scanned);

			if (dist == null || dist < curr_dist) {
				dist = curr_dist;
				loc = curr;
			}
		}

		TreeSet<Station> rstations = new TreeSet<Station>((TreeSet<Station>) scanned);
		rstations.retainAll(loc.getStations());
		Location resloc = new Location(loc.getName());

		for (Station stat : rstations)
			resloc.addStation(stat);

		return resloc;
	}


	/**
	 * Loads a {@link LocationMap} from a csv "database", specifyed by an {@link InputStream}.
	 * All readers and streams will be closed by this function.
	 * NOTE Given instream should be retrieved by {@link Context#openFileInput(String)}!
	 *
	 * @param file The csv file to load from.
	 * @return the {@link LocationMap} represented by the database.
	 */
	public static LocationMap loadMap(InputStream in) {
		LocationMap map = new LocationMap();
		CSVReader csvrd = new CSVReader(new InputStreamReader(in));
		String[] line;

		try {
			while ((line = csvrd.readNext()) != null)
				map.add(line[0], line[1], Integer.parseInt(line[2]));
		} catch (IOException ex) {
			System.out.println("ERROR !! - " + ex.getMessage());
		} finally {
			try {
				csvrd.close();
				in.close();
			} catch (IOException ex) {
				System.out.println(DRUGS_MSG);
			}
		}

		return map;
	}


	/**
	 * Stores a {@link LocationMap} to a csv database specifyed by a {@link FileOutputStream}.
	 * All writers and streams will be closed by this function.
	 * NOTE Given outstream should be retrieved by {@link Context#openFileOutput(String, int)}, using mode
	 * {@link Context#MODE_PRIVATE}!
	 *
	 * @param map Map to store to database.
	 * @param os Outpu stream to write to.
	 */
	public static void storeMap(LocationMap map, FileOutputStream os) {
		CSVWriter csvwr = new CSVWriter(new OutputStreamWriter(os));

		for (String name : map.getNames()) {
			Location loc = map.getLocation(name);
			for (Station ap : loc.getStations()) {
				String[] line = new String[3];
				line[0] = name;
				line[1] = ap.mac;
				line[2] = String.valueOf(ap.rssi);
				csvwr.writeNext(line);
			}
		}

		try {
			csvwr.flush();
		} catch (IOException ex) {
			System.out.println("ERROR !! - " + ex.getMessage());
		} finally {
			try {
				csvwr.close();
				os.close();
			} catch (IOException ex) {
				System.out.println(DRUGS_MSG);
			}
		}
	}


	/**
	 * Appends a locationing result log to a logfile represented by a {@link FileOutputStream}.
	 * NOTE Given outstream should be retrieved by {@link Context#openFileOutput(String, int)}, using mode
	 * {@link Context#MODE_APPEND}!
	 *
	 * @param loc a result location to write to log.
	 * @param os a {@link FileOutputStream} for the log file.
	 */
	public static void appendToLogfile(Location loc, FileOutputStream os) {
		CSVWriter csvwr = new CSVWriter(new OutputStreamWriter(os));
		int linelen = loc.getStations().size() * 2 + 1;
		String[] line = new String[linelen];

		line[0] = loc.getName();
		int i = 1;

		for (Station stat : loc.getStations()) {
			line[i] = stat.mac;
			line[i + 1] = String.valueOf(stat.rssi);
			i += 2;
		}
		/// and so forth... depending on result type/signature we don't know yet...
		csvwr.writeNext(line);

		try {
			csvwr.flush();
		} catch (IOException ex) {
			System.out.println("ERROE !! - " + ex.getMessage());
		} finally {
			try {
				csvwr.close();
				os.close();
			} catch (IOException ex) {
				System.out.println(DRUGS_MSG);
			}
		}
	}
}
