package net.tropicraft.core.common.enums;

import net.minecraft.util.SoundEvent;
import net.tropicraft.core.common.sound.TropicraftSounds;

public enum TropicraftBongos implements ITropicraftVariant {
    SMALL(TropicraftSounds.BONGO_HIGH), MEDIUM(TropicraftSounds.BONGO_MED), LARGE(TropicraftSounds.BONGO_LOW);

    private final SoundEvent soundEvent;
    public static final TropicraftBongos VALUES[] = values();

    TropicraftBongos(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
    }
    
    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    @Override
    public String getTypeName() {
        return "bongo";
    }
}
