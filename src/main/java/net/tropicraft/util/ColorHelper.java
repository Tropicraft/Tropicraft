package net.tropicraft.util;

import java.util.ArrayList;

import net.minecraft.item.ItemDye;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

/**
 * Class to assist chairs, umbrellas, beach floats, and whatever else uses our dynamic coloring system.
 */
public class ColorHelper {
    /** List of metadata of wool (or just a unique identifier for colors) to an integer color */
    private static BiMap<Integer, Integer> woolValues = HashBiMap.create();

    /** List of integer color values, each index is the color associated with that metadata value */
    private static ArrayList<Integer> colorValues = Lists.newArrayList();

    /** Value used in entityInit methods as a 'default' value */
    public static int DEFAULT_VALUE;

    /** The character used in mc to color code chat */
    public static final char COLOR_CHARACTER = '\u00a7';

    public static void init() {
        // Get all the existing wool colors into the color array
        for (int color : ItemDye.field_150922_c) {
            colorValues.add(Integer.valueOf(color));
        }

        DEFAULT_VALUE = colorValues.get(0);

        // Map the color values to metadata values in the bidirectional map
        for (int i = 0; i < colorValues.size(); i++) {
            woolValues.put(Integer.valueOf(ItemDye.field_150922_c[i]), i);
        }
    }

    /**
     * @param val Color value 0-15
     * @return Returns a formatted String for a minecraft color
     */
    public static String color(int val) {
        return new StringBuilder().append(COLOR_CHARACTER).append(Integer.toHexString(val)).toString();
    }

    /**
     * @return Return the number of colors registered
     */
    public static int getNumColors() {
        return woolValues.keySet().size();
    }

    /**
     * @param damage Damage/metadata value
     * @return Return an integer rgba color that is associated with the given damage value
     */
    public static int getColorFromDamage(int damage) {
        return colorValues.get(Integer.valueOf(damage));
    }

    /**
     * @param color rgba int color value
     * @return Return the damage value associated with the given rgba color
     */
    public static int getDamageFromColor(int color) {
        return woolValues.get(Integer.valueOf(color));
    }

    public static float getRed(int color) {
        return (float)(color >> 16 & 255) / 255.0F;
    }

    public static float getGreen(int color) {
        return (float)(color >> 8 & 255) / 255.0F;
    }

    public static float getBlue(int color) {
        return (float)(color & 255) / 255.0F;
    }

    /**
     * 
     * @param red float value from 0-1 representing the red of this color
     * @param green float value from 0-1 representing the green of this color
     * @param blue float value from 0-1 representing the blue of this color
     * @return Returns a value of the combined rgb floats between 0 and 1 to a single int
     */
    public static int getColor(float red, float green, float blue) {
        return ((int)(red * 255) << 16) | ((int)(green * 255) << 8) | (int)(blue * 255);
    }

    public static int getColor(float[] rgb) {
        return getColor(rgb[0], rgb[1], rgb[2]);
    }
}