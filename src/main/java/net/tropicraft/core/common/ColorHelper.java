package net.tropicraft.core.common;

public class ColorHelper {
    public enum Color {
        WHITE(16383998),
        ORANGE(16351261),
        MAGENTA(13061821),
        LIGHT_BLUE(3847130),
        YELLOW(16701501),
        LIME(8439583),
        PINK(15961002),
        GRAY(4673362),
        LIGHT_GRAY(10329495),
        CYAN(1481884),
        PURPLE(8991416),
        BLUE(3949738),
        BROWN(8606770),
        GREEN(6192150),
        RED(11546150),
        BLACK(1908001);

        private final int rgba;
        public static final Color[] VALUES = values();

        Color(final int rgba) {
            this.rgba = rgba;
        }

        public int getColor() {
            return rgba;
        }
    }

    public static Color getColorObject(final int c) {
        for (final Color color : Color.VALUES) {
            if (color.getColor() == c) {
                return color;
            }
        }
        return Color.VALUES[0];
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
