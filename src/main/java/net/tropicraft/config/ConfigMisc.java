package net.tropicraft.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import modconfig.ConfigComment;
import modconfig.IConfigCategory;

public class ConfigMisc implements IConfigCategory {

	/** List of users who can use coconut bombs. Empty by default */
	@ConfigComment("List of users who can use coconut bombs.")
	public static String coconutBombWhitelist = "";
	
	public static String[] COCONUT_BOMB_WHITELIST = null;
	public static List<String> coconutBombWhitelistedUsers = new ArrayList<String>();
	
	@Override
	public String getConfigFileName() {
		return "Tropicraft_Misc";
	}

	@Override
	public String getCategory() {
		return "Tropicraft Misc Config";
	}

	@Override
	public void hookUpdatedValues() {
		COCONUT_BOMB_WHITELIST = coconutBombWhitelist.contains(",") ? coconutBombWhitelist.replace(" ", "").split(",") : coconutBombWhitelist.split(" ");
		coconutBombWhitelistedUsers = Arrays.asList(COCONUT_BOMB_WHITELIST);
	}

}
