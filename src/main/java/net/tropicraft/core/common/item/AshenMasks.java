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

    private String name;

    public static AshenMasks[] VALUES = values();

    AshenMasks(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
