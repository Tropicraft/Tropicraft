package net.tropicraft.core.common.item;

public enum AshenMasks {
    SQUARE_ZORD("Square Zord"),
    HORN_MONKEY("Horn Monkey"),
    OBLONGATRON("Oblongatron"),
    HEADINATOR("Headinator"),
    SQUARE_HORN("Square Horn"),
    SCREW_ATTACK("Screw Attack"),
    THE_BRAIN("The Brain"),
    BAT_BOY("Bat Boy"),
    ASHEN_MASK1("Ashen Mask"),
    ASHEN_MASK2("Ashen Mask"),
    ASHEN_MASK3("Ashen Mask"),
    ASHEN_MASK4("Ashen Mask"),
    ASHEN_MASK5("Ashen Mask")
    ;

    private final String name;
    private final double xOffset, yOffset;

    public static AshenMasks[] VALUES = values();

    AshenMasks(String name) {
    	this(name, 0.5, 1);
    }
    
    AshenMasks(String name, double xOffset, double yOffset) {
    	this.name = name;
    	this.xOffset = xOffset / 16;
    	this.yOffset = yOffset / 16;
    }

    public String getName() {
        return name;
    }

    public double getXOffset() {
    	return xOffset;
    }
    
    public double getYOffset() {
    	return yOffset;
    }
}
