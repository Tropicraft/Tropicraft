package net.tropicraft.core.common.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;

public class Sounds {
    public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Constants.MODID);

    public static final RegistryObject<SoundEvent> BURIED_TREASURE = register("buried_treasure");
    public static final RegistryObject<SoundEvent> EASTERN_ISLES = register("eastern_isles");
    public static final RegistryObject<SoundEvent> LOW_TIDE = register("low_tide");
    public static final RegistryObject<SoundEvent> SUMMERING = register("summering");
    public static final RegistryObject<SoundEvent> THE_TRIBE = register("the_tribe");
    public static final RegistryObject<SoundEvent> TRADE_WINDS = register("trade_winds");
    public static final RegistryObject<SoundEvent> PAGE_FLIP = register("page_flip");
    public static final RegistryObject<SoundEvent> BONGO_LOW = register("bongo.low");
    public static final RegistryObject<SoundEvent> BONGO_MED = register("bongo.med");
    public static final RegistryObject<SoundEvent> BONGO_HIGH = register("bongo.high");
    public static final RegistryObject<SoundEvent> HEAD_LAUGHING = register("headlaughing");
    public static final RegistryObject<SoundEvent> HEAD_ATTACK = register("headattack");
    public static final RegistryObject<SoundEvent> HEAD_SHORT = register("headshort");
    public static final RegistryObject<SoundEvent> HEAD_MED = register("headmed");
    public static final RegistryObject<SoundEvent> HEAD_PAIN = register("headpain");
    public static final RegistryObject<SoundEvent> HEAD_DEATH = register("headdeath");
    public static final RegistryObject<SoundEvent> IGGY_ATTACK = register("iggyattack");
    public static final RegistryObject<SoundEvent> IGGY_DEATH = register("iggydeath");
    public static final RegistryObject<SoundEvent> IGGY_LIVING = register("iggyliving");
    public static final RegistryObject<SoundEvent> ASHEN_LAUGH = register("ashen_laugh");
    public static final RegistryObject<SoundEvent> DOLPHIN = register("dolphin");
    public static final RegistryObject<SoundEvent> FROG_SPIT = register("frogspit");

    private static RegistryObject<SoundEvent> register(String name) {
        return REGISTER.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Constants.MODID, name)));
    }
}
