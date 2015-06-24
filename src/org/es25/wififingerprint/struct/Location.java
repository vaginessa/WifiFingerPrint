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


package org.es25.wififingerprint.struct;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Location object.
 * It consists of a location name which is typically an user-defined name or the ESSID
 * as well as a list of {@link Station}s, the actual access points belonging to ths location.
 *
 * @author Armin Leghissa
 */
public class Location implements Iterable<Station> {
	private final String name;
	private final Map<String, Station> stations;


	/**
	 * Creates a new location specifying it's name.
	 *
	 * @param name User-defined name or ESSID.
	 */
	public Location(String name) {
		this.name = name;
		stations = new HashMap<String, Station>();
	}


	/**
	 * Adds a new acces point specifying mac address and rssi value.
	 *
	 * @param mac MAC address.
	 * @param rssi RSSI/quality value.
	 */
	public void addStation(String mac, int rssi) {
		Station station = stations.get(mac);

		if (station == null)
			stations.put(mac, new Station(mac, rssi));
		else
			station.add(rssi);
	}


	/**
	 * Adds a new access point specifying a {@link Station} object.
	 *
	 * @param station A station to add.
	 */
	public void addStation(Station station) {
		Station stat = stations.get(station.mac());

		if (stat == null)
			stations.put(station.mac(), station);
		else
			stat.add(station.rssi());
	}


	/**
	 * Gets the name of this location.
	 *
	 * @return this station's name.
	 */
	public String getName() {
		return this.name;
	}


	/**
	 * Gets a list of all accespoints hold by this location.
	 *
	 * @return this station's access points.
	 */
	public Collection<Station> getStations() {
		return this.stations.values();
	}


	/**
	 * Gets the RSSI value for the specifyed mac address.
	 *
	 * @param mac A MAC address which appropriate RSSI value to retrieve.
	 * @return the appropriate RSSI value or NULL if it does not exist.
	 */
	public Integer getRssiFor(String mac) {
		Station cur = stations.get(mac);

		if (cur == null)
			return null;
		else
			return cur.rssi();
	}


	/**
	 * Returns a pretty printed version of this location.
	 *
	 * @return well formatted string.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.name);
		sb.append('\n');

		for (Station station : getStations()) {
			sb.append('\t');
			sb.append(sb.append(station.toString()));
			sb.append('\n');
		}

		return sb.toString();
	}


	/**
	 * Returns an iterator over all stations held by this location.
	 *
	 * @return a stations iterator.
	 */
	@Override
	public Iterator<Station> iterator() {
		return stations.values().iterator();
	}
}
