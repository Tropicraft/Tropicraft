package net.tropicraft.core.common.sound;

import net.minecraft.util.SoundEvent;
import net.tropicraft.core.registry.SoundRegistry.SoundName;

/**
 * All Tropicraft mod sounds are registered here. To create a new one:
 *
 * - Create a new public static SoundEvent field named whatever you want
 * - Set that field = null
 * - Add an @SoundName annotation with a string inside equal to whatever you put as the key in sounds.json
 * - Reference it anywhere. This class is initialized super early so you don't have to worry about
 *   something using it before its values are set.
 */
public class TropicraftSounds {
    @SoundName("buried_treasure")
    public static SoundEvent BURIED_TREASURE = null;
    
    @SoundName("eastern_isles")
    public static SoundEvent EASTERN_ISLES = null;
    
    @SoundName("low_tide")
    public static SoundEvent LOW_TIDE = null;
    
    @SoundName("summering")
    public static SoundEvent SUMMERING = null;
    
    @SoundName("the_tribe")
    public static SoundEvent THE_TRIBE = null;
    
    @SoundName("trade_winds")
    public static SoundEvent TRADE_WINDS = null;
    
    @SoundName("page_flip")
    public static SoundEvent PAGE_FLIP = null;
    
    @SoundName("bongo.low")
    public static SoundEvent BONGO_LOW = null;
    
    @SoundName("bongo.medium")
    public static SoundEvent BONGO_MED = null;
    
    @SoundName("bongo.high")
    public static SoundEvent BONGO_HIGH = null;
    
    @SoundName("headlaughing")
    public static SoundEvent HEAD_LAUGHING = null;
    
    @SoundName("headattack")
    public static SoundEvent HEAD_ATTACK = null;
    
    @SoundName("headshort")
    public static SoundEvent HEAD_SHORT = null;
    
    @SoundName("headmed")
    public static SoundEvent HEAD_MED = null;
    
    @SoundName("headpain")
    public static SoundEvent HEAD_PAIN = null;
    
    @SoundName("headdeath")
    public static SoundEvent HEAD_DEATH = null;
    
    @SoundName("iggyattack")
    public static SoundEvent IGGY_ATTACK = null;
    
    @SoundName("iggydeath")
    public static SoundEvent IGGY_DEATH = null;
    
    @SoundName("iggyliving")
    public static SoundEvent IGGY_LIVING = null;
    
    @SoundName("ashen_laugh")
    public static SoundEvent ASHEN_LAUGH = null;
    
    @SoundName("dolphin")
    public static SoundEvent DOLPHIN = null;
}
