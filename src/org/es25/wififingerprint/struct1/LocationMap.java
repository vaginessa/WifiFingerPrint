

package org.es25.wififingerprint.struct1;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;


/**
 * Container for a set of {@link Location}s
 *
 * @author Armin Leghissa
 */
public class LocationMap {

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
}
