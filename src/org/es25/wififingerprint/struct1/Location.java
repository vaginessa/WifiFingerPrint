

package org.es25.wififingerprint.struct1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Location {

	String name;
	ArrayList<Station> stations;
	Map<String, Integer> ap_map;


	public Location(String name) {
		this.name = name;
		stations = new ArrayList<Station>();
		ap_map = new HashMap<String, Integer>();
	}


	public void addStation(String mac, int rssi) {
		stations.add(new Station(mac, rssi));
		ap_map.put(mac, rssi);
	}


	public void addStation(Station station) {
		stations.add(station);
		ap_map.put(station.mac, station.rssi);
	}


	public String getName() {
		return this.name;
	}


	public ArrayList<Station> getStations() {
		return this.stations;
	}


	public Integer getRssiValue(String mac) {
		for (Station d : stations) {
			if (mac.equals(d.mac)) {
				return d.rssi;
			}
		}
		return null;
	}


	public Integer getRssiFor(String mac) {
		return ap_map.get(mac);
	}
}
