package net.tropicraft;


/**
 * Class that contains color constants of sand. NOTE: Most colors are in argb format because Minecraft
 */
public enum SandColors {
	PURIFIED(0, 16777215),
    CORAL(1, 0xffff98b1),
    GREEN(2, 0xff33ffc5),
    BLACK(3, 0xff545454),
    MINERAL(4, 16777215);
    
    private static final SandColors[] sandColors = new SandColors[values().length];
    
    public int metadata;
    
    public int color;
    
    private SandColors(int meta, int c) {
        this.metadata = meta;
        this.color = c;
    }
    
    public static int getColor(int meta) {
        return sandColors[meta % sandColors.length].color;
    }
    
    static {
        SandColors[] colors = values();
        int l = colors.length;

        for (int i = 0; i < l; ++i)
        {
            SandColors color = colors[i];
            sandColors[color.metadata] = color;
        }
    }
}
