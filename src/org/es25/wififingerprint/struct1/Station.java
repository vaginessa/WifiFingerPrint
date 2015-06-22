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


/**
 * A Station, consisting of a mac adress (bssid) and a dB/rssi value.
 * One location can have multiple stations.
 */
public class Station implements Comparable<Station> {
	public final String mac;
	public final int rssi;


	public Station(String bssid, int dbvalue) {
		this.mac = bssid;
		this.rssi = dbvalue;
	}


	/**
	 * Pretty string representation as (mac,rssi) 2-tuple.
	 *
	 * @return Human readable format.
	 */
	@Override
	public String toString() {
		return String.format("(%s, %d)", this.mac, this.rssi);
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
		int res = this.rssi - other.rssi;
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
}
