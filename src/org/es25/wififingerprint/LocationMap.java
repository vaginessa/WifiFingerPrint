package org.es25.wififingerprint;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class LocationMap {

	private HashMap<String, Location> myLocations;
	
	
	public LocationMap(){
		myLocations = new HashMap<String, Location>();
	}
	
	
	public void add(String ssid, String bssid, int dbvalue){
		Location curLocation = myLocations.get(ssid);
	     if(curLocation == null){
	    	 curLocation = new Location(ssid);
	    	 myLocations.put(ssid,curLocation);	    	
	     }
	    	 curLocation.addData(bssid, dbvalue);
	}
	
	public Set<String> getIntersect(LocationMap other){
	
		Set<String> os =
				new TreeSet<String>(other.myLocations.keySet());
		
		os.retainAll(myLocations.keySet());
		
		return os;
	}
	
	
	public Location getLocation(String ssid){
		return this.myLocations.get(ssid);
	}
	
	public Set<String> getKeys(){
		return myLocations.keySet();
	}
	
}
