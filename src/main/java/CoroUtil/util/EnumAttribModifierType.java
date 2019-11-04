package CoroUtil.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * The mathematical behavior is as follows:
 * Operation 0: Increment X by Amount,
 * Operation 1: Increment Y by X * Amount,
 * Operation 2: Y = Y * (1 + Amount) (equivalent to Increment Y by Y * Amount).
 * The game first sets X = Base, then executes all Operation 0 modifiers, then sets Y = X,
 * then executes all Operation 1 modifiers, and finally executes all Operation 2 modifiers.
 */
public enum EnumAttribModifierType {
	
	INCREMENT, INCREMENT_MULTIPLY_BASE, MULTIPLY_ALL;
	
	private static final Map<Integer, EnumAttribModifierType> lookup = new HashMap<Integer, EnumAttribModifierType>();
    static { for(EnumAttribModifierType e : EnumSet.allOf(EnumAttribModifierType.class)) { lookup.put(e.ordinal(), e); } }
    public static EnumAttribModifierType get(int intValue) { return lookup.get(intValue); }
}
