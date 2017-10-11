package build.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumCopyState 
{
	NORMAL, SETMIN, SETMAX;
	
	private static final Map<Integer, EnumCopyState> lookup = new HashMap<Integer, EnumCopyState>();
    static { for(EnumCopyState e : EnumSet.allOf(EnumCopyState.class)) { lookup.put(e.ordinal(), e); } }
    public static EnumCopyState get(int intValue) { return lookup.get(intValue); }
}
