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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


/**
 * Container for a set of {@link Location}s
 *
 * @author Armin Leghissa
 */
public class LocationMap implements Iterable<Location> {

	private final HashMap<String, Location> locations;


	/**
	 * Construct the fucking map.
	 */
	public LocationMap() {
		locations = new HashMap<String, Location>();
	}


	/**
	 * Add a new {essid, mac, rssi} triple to this map.
	 *
	 * @param name The name of a Location.
	 * @param mac The mac adress of a station.
	 * @param rssi The quality level of a station
	 */
	public void add(String name, String mac, int rssi) {
		Location loc = locations.get(name);

		if (loc == null) {
			loc = new Location(name);
			locations.put(name, loc);
		}

		loc.addStation(mac, rssi);
	}


	/**
	 * Add a complete scan for a specific {@link Location} denoted by specifyed name.
	 * Note that the "scan" should been median-filtered!
	 *
	 * @param name The name of the location the scan was made for.
	 * @param stations A set of {mac, rssi} tuples.
	 */
	public void add(String name, Set<Station> stations) {
		Location loc = locations.get(name);

		if (loc == null) {
			loc = new Location(name);
			locations.put(name, loc);
		}

		for (Station station : stations)
			loc.addStation(station);
	}


	/**
	 * Gets an intersection of the names of this and another specifyed map.
	 *
	 * @param other Another map tho intersect this with.
	 * @return An intersection set of the location names.
	 */
	public Set<String> getIntersect(LocationMap other) {
		Set<String> os = new TreeSet<String>(other.locations.keySet());
		os.retainAll(locations.keySet());
		return os;
	}


	/**
	 * Gets a the {@link Location} for a specifyed essid.
	 * Returns NULL if given essid is not contained.
	 *
	 * @param essid The network name which location data we want.
	 * @return the location object for the given name.
	 */
	public Location getLocation(String essid) {
		return this.locations.get(essid);
	}


	/**
	 * Gets all names (essid) stored in this map.
	 *
	 * @return A set of all known network names.
	 */
	public Set<String> getNames() {
		return locations.keySet();
	}


	/**
	 * Gets an {@link Iterator} over all stations of this location map.
	 *
	 * @return an iterator.
	 */
	@Override
	public Iterator<Location> iterator() {
		return locations.values().iterator();
	}
}
