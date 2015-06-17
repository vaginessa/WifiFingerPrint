package org.es25.wififingerprint;

import java.util.HashMap;

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
	
	
	
	
	public Location getLocation(String ssid){
		return this.myLocations.get(ssid);
	}
	
}
