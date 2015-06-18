package org.es25.wififingerprint;

import java.text.Normalizer;
import java.util.ArrayList;

public class Location {

	String SSID;
	ArrayList<LocationData> data;
	
	
	
	
	public Location(String ssid){
		this.SSID = ssid;
		data = new ArrayList<LocationData>();
	}
	
	
	public void addData(String bssid, int dbvalue){
		data.add(new LocationData(bssid,dbvalue));
	}
	
	
	public String getSSID(){
		return this.SSID;
	}
	
	public ArrayList<LocationData> getLocationData(){
		return this.data;
	}
	
	public Integer getRssiValue(String bssid){
		for(LocationData d : data){
			if(bssid.equals(d.BSSID)){
				return d.dBValue;
			}
		}
		return null;
	}
	
	
	
	
	
	public class LocationData{
		public String BSSID;
		public int dBValue;
		public LocationData(String bssid, int dbvalue){
			this.BSSID = bssid;
			this.dBValue = RssiUtils.normalizeDbLevel(dbvalue);
		}
		public String toString(){
			return this.BSSID + " | " + this.dBValue;
		}

	}
	
}

