package build.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumBuildState 
{
	NORMAL, PLACE;
	
	private static final Map<Integer, EnumBuildState> lookup = new HashMap<Integer, EnumBuildState>();
    static { for(EnumBuildState e : EnumSet.allOf(EnumBuildState.class)) { lookup.put(e.ordinal(), e); } }
    public static EnumBuildState get(int intValue) { return lookup.get(intValue); }
}
