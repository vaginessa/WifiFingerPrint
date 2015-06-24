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


package org.es25.wififingerprint.util;

import android.net.wifi.ScanResult;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.es25.wififingerprint.struct.Location;
import org.es25.wififingerprint.struct.LocationMap;
import org.es25.wififingerprint.struct.Station;


/**
 * Utility class for all the algorithmic stuff.
 *
 * @author Armin Leghissa
 */
public class Algo {
	/** Constant declaring the min. # of APs needed for triangulation. */
	public static final int MIN_TRIANG_STATIONS = 3;


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
	 * @param useMedian Whether to apply {@link #medianFilter(TreeSet)}.
	 * @return A beautiful set of {@link Station}s, ready for locality estimation.
	 */
	public static Set<Station> filterScan(List<ScanResult> raw_scan, boolean useMedian) {
		TreeSet<Station> scan = new TreeSet<Station>();

		for (ScanResult res : raw_scan) {
			Station station = new Station(
					res.BSSID, // the mac address
					rssi2quality(res.level));

			scan.add(station);
		}

		if (useMedian)
			medianFilter(scan);

		return scan;
	}


	/**
	 * Calculates the euclidian distance between a bunch of stations from the DB and a station set from a runtime scan.
	 *
	 * @param loc A {@link Location} object from the {@link LocationMap}, the database.
	 * @param scan A set of {@link Station}s created from a runtime scan.
	 * @return the euclidian distance if both have at least {@link #MIN_TRIANG_STATIONS} common stations, else infinity.
	 */
	public static float eucDist(Location loc, Set<Station> scan) {
		double res = 0.0;
		int matches = 0;

		for (Station s : scan) {
			Integer loc_rssi;

			if ((loc_rssi = loc.getRssiFor(s.mac())) != null) {
				res += Math.pow((loc_rssi - s.rssi()), 2);
				matches++;
			}
		}

		if (matches < MIN_TRIANG_STATIONS)
			return Float.MAX_VALUE;
		else
			return (float) Math.sqrt(res / matches);
	}


	/**
	 * Caluculates the manhattan distance between a location and a bunch of stations.
	 *
	 * @param loc A location object from the DB.
	 * @param scan A set of stations from a runtime scan.
	 * @return the manhattan distance if both have at least {@link #MIN_TRIANG_STATIONS} common stations, else infinity.
	 */
	public static float manDist(Location loc, Set<Station> scan) {
		double res = 0.0;
		int matches = 0;

		for (Station s : scan) {
			Integer loc_rssi;

			if ((loc_rssi = loc.getRssiFor(s.mac())) != null) {
				res += Math.abs(loc_rssi - s.rssi());
				matches++;
			}
		}

		if (matches < MIN_TRIANG_STATIONS)
			return Float.MAX_VALUE;
		else
			return (float) (res / matches);
	}


	/**
	 * Triangulates the {@link Location} in the {@link LocationMap} DB for a given scanning set.
	 * It uses the euclidian distance algorithem to determin the location with the lowest distance to the given set.
	 *
	 * @param learned the location map of learned rssi fingerprints.
	 * @param scanned a set constructed by a runtime scan.
	 * @return The triangulated result location holding the common stations or null.
	 */
	public static Location triangulate(LocationMap learned, Set<Station> scanned) {
		float min_dist = Float.MAX_VALUE;
		Location loc = null;

		for (Location cur : learned) {
			float cur_dist = eucDist(cur, scanned);

			if (cur_dist < min_dist) {
				min_dist = cur_dist;
				loc = cur;
			}
		}

		if (min_dist == Float.MAX_VALUE)
			return null;

		TreeSet<Station> rstations = new TreeSet<Station>(Station.MAC_COMPARATOR);
		rstations.addAll((TreeSet<Station>) scanned);
		rstations.retainAll(loc.getStations());
		Location resloc = new Location(loc.getName());

		for (Station stat : rstations)
			resloc.addStation(stat);

		return resloc;
	}
}
