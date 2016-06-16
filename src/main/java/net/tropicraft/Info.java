package net.tropicraft;

public class Info {
	/** Unique mod identifier */
	public static final String MODID = "tropicraft";
	
	/** Mod version */
	public static final String VERSION = "v7.0";
	
	/** Name of the mod */
	public static final String NAME = "Tropicraft";
	
	/** Where to locate icons */
	public static final String ICON_LOCATION = MODID + ":";
	
	/** Where to locate tool icons */
	public static final String TOOL_ICON_LOCATION = ICON_LOCATION + "tools/";
	
	/** Where to locate armor textures */
	public static final String ARMOR_LOCATION = MODID + ":textures/models/armor/";
	
	/** Where to locate gui textures */
	public static final String TEXTURE_GUI_LOC = "/assets/" + MODID + "/gui/";
	
	/** Folder to find records in */
	public static final String RECORDS_LOCATION = ICON_LOCATION + "sounds/records/";

	/** Client proxy location */
	public static final String CLIENT_PROXY = "net.tropicraft.proxy.ClientProxy";
	
	/** Common proxy location */
	public static final String SERVER_PROXY = "net.tropicraft.proxy.ServerProxy";
}
