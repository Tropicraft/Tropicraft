package net.tropicraft.core.common.enums;

public enum AshenMasks implements ITropicraftVariant {
    SQUARE_ZORD,
    HORN_MONKEY,
    OBLONGATRON,
    HEADINATOR,
    SQUARE_HORN,
    SCREW_ATTACK,
    THE_BRAIN,
    BAT_BOY,
    ASHEN_MASK1,
    ASHEN_MASK2,
    ASHEN_MASK3,
    ASHEN_MASK4,
    ASHEN_MASK5
    ;
    
    public static AshenMasks[] VALUES = values();

    @Override
    public String getTypeName() {
        return "mask";
    }

}
