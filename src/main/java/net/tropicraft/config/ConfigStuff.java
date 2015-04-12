package net.tropicraft.config;

import modconfig.IConfigCategory;

public class ConfigStuff implements IConfigCategory {

	@Override
	public String getConfigFileName() {
		return "Tropicraft_Stuff";
	}

	@Override
	public String getCategory() {
		return "Tropicraft Stuff Config";
	}

	@Override
	public void hookUpdatedValues() {
		//apply changes to code here if needed
	}

}
