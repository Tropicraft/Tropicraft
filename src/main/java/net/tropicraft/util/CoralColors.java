package net.tropicraft.util;


/**
 * Class that contains color constants of coral. NOTE: Most colors are in argb format because Minecraft
 */
public enum CoralColors {
    CORAL(0, 0xffff98b1),
    GREEN(1, 0xff33ffc5),
    BLACK(2, 0xff545454),
    MINERAL(3, 16777215);
    
    private static final CoralColors[] coralColors = new CoralColors[values().length];
    
    public int metadata;
    
    public int color;
    
    private CoralColors(int meta, int c) {
        this.metadata = meta;
        this.color = c;
    }
    
    public static int getColor(int meta) {
        return coralColors[meta % coralColors.length].color;
    }
    
    static {
        CoralColors[] colors = values();
        int l = colors.length;

        for (int i = 0; i < l; ++i)
        {
            CoralColors color = colors[i];
            coralColors[color.metadata] = color;
        }
    }
}
