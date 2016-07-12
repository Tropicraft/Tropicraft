package net.tropicraft;

import java.util.HashMap;
import java.util.Map;

public class RecordInformation {
	private static Map<String, String[]> recordInformation = new HashMap<String, String[]>();
	
	static {
		addInfo("buried_treasure", "https://www.youtube.com/watch?v=lPhHWYnUR_4");
		addInfo("eastern_isles", "https://soundcloud.com/froxlab/eastern_isles");
		addInfo("the_tribe", "https://www.youtube.com/watch?v=LvxVDhMvwE4");
		addInfo("low_tide", "https://youtu.be/VgB2TjwGOuQ");
		addInfo("trade_winds", "https://soundcloud.com/froxlab/breakset-37-unnamed");
		addInfo("summering", "https://www.youtube.com/watch?v=szO30RmM7b8");
	}
	
	private static void addInfo(String name, String...info) {
		recordInformation.put(name, info);
	}
	
	/**
	 * Return the additional information for a song or null if none exists
	 * @param song Song name
	 * @return Additional information for tooltip dialogs
	 */
	public static String[] getInformation(String song) {
		if (recordInformation.containsKey(song)) {
			return recordInformation.get(song);
		} else {
			return null;
		}
	}
}
