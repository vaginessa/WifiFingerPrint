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
import java.util.HashMap;
import java.util.Map;


/**
 * Location object.
 * It consists of a location name which is typically an user-defined name or the ESSID
 * as well as a list of {@link Station}s, the actual access points belonging to ths location.
 *
 * @author Armin Leghissa
 */
public class Location {
	private final String name;
	private final ArrayList<Station> stations;
	private final Map<String, Integer> ap_map;


	/**
	 * Creates a new location specifying it's name.
	 *
	 * @param name User-defined name or ESSID.
	 */
	public Location(String name) {
		this.name = name;
		stations = new ArrayList<Station>();
		ap_map = new HashMap<String, Integer>();
	}


	/**
	 * Adds a new acces point specifying mac address and rssi value.
	 *
	 * @param mac MAC address.
	 * @param rssi RSSI/quality value.
	 */
	public void addStation(String mac, int rssi) {
		stations.add(new Station(mac, rssi));
		ap_map.put(mac, rssi);
	}


	/**
	 * Adds a new access point specifying a {@link Station} object.
	 *
	 * @param station A station to add.
	 */
	public void addStation(Station station) {
		stations.add(station);
		ap_map.put(station.mac, station.rssi);
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
	public ArrayList<Station> getStations() {
		return this.stations;
	}


	/**
	 * Gets the RSSI value for the specifyed mac address.
	 *
	 * @param mac A MAC address which appropriate RSSI value to retrieve.
	 * @return the appropriate RSSI value or NULL if it does not exist.
	 */
	public Integer getRssiValue(String mac) {
		for (Station d : stations) {
			if (mac.equals(d.mac)) {
				return d.rssi;
			}
		}
		return null;
	}


	/**
	 * Gets the RSSI value for the specifyed mac address.
	 *
	 * @param mac A MAC address which appropriate RSSI value to retrieve.
	 * @return the appropriate RSSI value or NULL if it does not exist.
	 */
	public Integer getRssiFor(String mac) {
		return ap_map.get(mac);
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

		for (Station station : this.stations) {
			sb.append('\t');
			sb.append(sb.append(station.toString()));
			sb.append('\n');
		}

		return sb.toString();
	}
}
