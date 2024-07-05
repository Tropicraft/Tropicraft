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
    INVADER("Invader"),
    MOJO("Mojo"),
    WARTHOG("Warthog"),
    THE_HEART("The Heart"),
    ENIGMA("Enigma");

    private final String name;
    private final double xOffset, yOffset;

    public static final AshenMasks[] VALUES = values();

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
