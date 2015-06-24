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


package org.es25.wififingerprint.util;

import android.content.Context;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.es25.wififingerprint.struct.Location;
import org.es25.wififingerprint.struct.LocationMap;
import org.es25.wififingerprint.struct.Station;


/**
 * Utility class for all the I/O crap.
 *
 * @author Armin Leghissa
 */
public class IO {
	private static final String DRUGS_MSG = "Whitch fucking drugs did they take ???";

	/** File name for the csv log file. */
	public static final String LOG_FILE = "LocEstimation.log";

	/** File name for the csv {@link LocationMap} database. */
	public static final String MAP_FILE = "RssiLearningMap.csv";

	/** Default location name if no location could be triangulated. */
	public static final String NO_LOCATION = "-NONE-";


	/**
	 * Loads a {@link LocationMap} from a csv "database", specifyed by an {@link InputStream}.
	 * All readers and streams will be closed by this function.
	 * NOTE Given instream should be retrieved by {@link Context#openFileInput(String)}!
	 *
	 * @param file The csv file to load from.
	 * @return the {@link LocationMap} represented by the database.
	 */
	public static LocationMap loadMap(InputStream in) {
		LocationMap map = new LocationMap();
		CSVReader csvrd = new CSVReader(new InputStreamReader(in));
		String[] line;

		try {
			while ((line = csvrd.readNext()) != null)
				map.add(line[0], line[1], Integer.parseInt(line[2]));
		} catch (IOException ex) {
			System.out.println("ERROR !! - " + ex.getMessage());
		} finally {
			try {
				csvrd.close();
				in.close();
			} catch (IOException ex) {
				System.out.println(DRUGS_MSG);
			}
		}

		return map;
	}


	/**
	 * Stores a {@link LocationMap} to a csv database specifyed by a {@link FileOutputStream}.
	 * All writers and streams will be closed by this function.
	 * NOTE Given outstream should be retrieved by {@link Context#openFileOutput(String, int)}, using mode
	 * {@link Context#MODE_PRIVATE}!
	 *
	 * @param map Map to store to database.
	 * @param os Outpu stream to write to.
	 */
	public static void storeMap(LocationMap map, FileOutputStream os) {
		CSVWriter csvwr = new CSVWriter(new OutputStreamWriter(os));

		for (Location loc : map) {
			for (Station stat : loc) {
				for (int rssi : stat) {
					String[] line = new String[3];
					line[0] = loc.getName();
					line[1] = stat.mac();
					line[2] = String.valueOf(rssi);
					csvwr.writeNext(line);
				}
			}
		}

		try {
			csvwr.flush();
		} catch (IOException ex) {
			System.out.println("ERROR !! - " + ex.getMessage());
		} finally {
			try {
				csvwr.close();
				os.close();
			} catch (IOException ex) {
				System.out.println(DRUGS_MSG);
			}
		}
	}


	/**
	 * Appends a locationing result log to a logfile represented by a {@link FileOutputStream}.
	 * NOTE Given outstream should be retrieved by {@link Context#openFileOutput(String, int)}, using mode
	 * {@link Context#MODE_APPEND}!
	 *
	 * @param loc a result location to write to log.
	 * @param os a {@link FileOutputStream} for the log file.
	 */
	public static void appendToLogfile(Location loc, FileOutputStream os) {
		CSVWriter csvwr = new CSVWriter(new OutputStreamWriter(os));
		String[] line;

		if (loc != null) {
			line = new String[loc.getStations().size() * 2 + 1];
			line[0] = loc.getName();
			int i = 1;

			for (Station stat : loc) {
				line[i] = stat.mac();
				line[i + 1] = String.valueOf(stat.rssi());
				i += 2;
			}
		} else {
			line = new String[1];
			line[0] = NO_LOCATION;
		}

		csvwr.writeNext(line);

		try {
			csvwr.flush();
		} catch (IOException ex) {
			System.out.println("ERROE !! - " + ex.getMessage());
		} finally {
			try {
				csvwr.close();
				os.close();
			} catch (IOException ex) {
				System.out.println(DRUGS_MSG);
			}
		}
	}
}
