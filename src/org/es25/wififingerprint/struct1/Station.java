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


package org.es25.wififingerprint.struct1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * A Station, consisting of a mac adress (bssid) and a dB/rssi value.
 * One location can have multiple stations.
 */
public class Station implements
		Comparable<Station>,
		Iterable<Integer> {
	/** Comparator which compares stations only by their mac adresses instead of rssi values. */
	public static final Comparator<Station> MAC_COMPARATOR = new Comparator<Station>() {
		@Override
		public int compare(Station lhs, Station rhs) {
			return lhs.mac.compareTo(rhs.mac);
		}
	};

	private String mac;
	private final ArrayList<Integer> rssi_list;


	public Station(String mac, int rssi) {
		this.rssi_list = new ArrayList<Integer>();
		this.rssi_list.add(rssi);
		this.mac = mac;
	}


	/**
	 * Pretty string representation as (mac,rssi) 2-tuple.
	 *
	 * @return Human readable format.
	 */
	@Override
	public String toString() {
		return String.format("(%s, %d)", mac, rssi());
	}


	/**
	 * Gets this station's mean rssi value.
	 *
	 * @return rssi mean value.
	 */
	public int rssi() {
		if (rssi_list.size() == 1)
			return rssi_list.get(0);
		else {
			int mean_rssi = 0;

			for (int rssi : rssi_list)
				mean_rssi += rssi;

			return (mean_rssi / rssi_list.size());
		}
	}


	/**
	 * Adds the given rssi value.
	 *
	 * @param rssi rssi value to add.
	 */
	public void add(int rssi) {
		rssi_list.add(rssi);
	}


	/**
	 * Gets the rssi values of this station.
	 *
	 * @return list of rssi values.
	 */
	public List<Integer> values() {
		return this.rssi_list;
	}


	/**
	 * Gets this Station's mac address.
	 *
	 * @return MAC
	 */
	public String mac() {
		return this.mac;
	}


	/**
	 * Compares stations (=APs) by their RSSI values or better said by their signal quality.
	 *
	 * @param other Another station to compare to.
	 * @return a negative number if this is 'smaller' than the other, 0 on equality or a positive number if other is
	 * 'greater'
	 */
	@Override
	public int compareTo(Station other) {
		int res = rssi() - other.rssi();
		if (res == 0)
			return this.mac.compareTo(other.mac);
		else
			return res;
	}


	/**
	 * Returns equality of this station and another object.
	 * The MAC adress is unique, so it is the only measure of equality.
	 *
	 * @param o Anoter object to compare to.
	 * @return true if this and the other have the same MAC adress.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof Station) {
			Station other = (Station) o;
			return this.mac.equals(other.mac);
		} else
			return false;
	}


	/**
	 * Returns an iterator over this station's rssi values.
	 *
	 * @return an rssi-iterator.
	 */
	@Override
	public Iterator<Integer> iterator() {
		return rssi_list.iterator();
	}
}
