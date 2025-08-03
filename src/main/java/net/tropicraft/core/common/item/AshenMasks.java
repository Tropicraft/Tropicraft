package net.tropicraft.core.common.item;

import java.util.Locale;

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

    public static final AshenMasks[] VALUES = values();

    AshenMasks(String name) {
        this.name = name;
    }

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String getName() {
        return name;
    }
}
