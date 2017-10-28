package net.tropicraft.core.common.enums;

public enum TropicraftBongos implements ITropicraftVariant {
    SMALL("high"), MEDIUM("medium"), LARGE("low");

    private final String sound;
    public static final TropicraftBongos VALUES[] = values();

    TropicraftBongos(String sound) {
        this.sound = sound;
    }

    public String getSoundRegistryName() {
        return getTypeName() + "." + sound;
    }
    
    @Override
    public String getTypeName() {
        return "bongo";
    }
}
