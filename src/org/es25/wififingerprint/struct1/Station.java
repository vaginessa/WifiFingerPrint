

package org.es25.wififingerprint.struct1;

import org.es25.wififingerprint.RssiUtils;


/**
 * A Station, consisting of a mac adress (bssid) and a dB/rssi value.
 * One location can have multiple stations.
 */
public class Station implements Comparable<Station> {
	public final String mac;
	public final int rssi;


	public Station(String bssid, int dbvalue) {
		this.mac = bssid;
		this.rssi = RssiUtils.rssi2quality(dbvalue);
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
