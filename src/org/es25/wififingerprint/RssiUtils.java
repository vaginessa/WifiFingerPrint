package org.es25.wififingerprint;

public class RssiUtils {

	public static int normalizeDbLevel(int dbvalue) {
		if (dbvalue >= -50) {
			return 100;
		} else if (dbvalue <= -100) {
			return 0;
		} else {
			return 2 * (dbvalue + 100);
		}

	}
	
	public int calcEucliDist (LocationMap learned, LocationMap scanned){
		
		
		
		
		return 100;
	}

}
