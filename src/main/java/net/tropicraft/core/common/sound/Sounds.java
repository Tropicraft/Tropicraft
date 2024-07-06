package net.tropicraft.core.common.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Tropicraft;

public class Sounds {
    public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(Registries.SOUND_EVENT, Tropicraft.ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> BURIED_TREASURE = register("buried_treasure");
    public static final DeferredHolder<SoundEvent, SoundEvent> EASTERN_ISLES = register("eastern_isles");
    public static final DeferredHolder<SoundEvent, SoundEvent> LOW_TIDE = register("low_tide");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUMMERING = register("summering");
    public static final DeferredHolder<SoundEvent, SoundEvent> THE_TRIBE = register("the_tribe");
    public static final DeferredHolder<SoundEvent, SoundEvent> TRADE_WINDS = register("trade_winds");
    public static final DeferredHolder<SoundEvent, SoundEvent> PAGE_FLIP = register("page_flip");
    public static final DeferredHolder<SoundEvent, SoundEvent> BONGO_LOW = register("bongo.low");
    public static final DeferredHolder<SoundEvent, SoundEvent> BONGO_MED = register("bongo.medium");
    public static final DeferredHolder<SoundEvent, SoundEvent> BONGO_HIGH = register("bongo.high");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEAD_LAUGHING = register("headlaughing");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEAD_ATTACK = register("headattack");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEAD_SHORT = register("headshort");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEAD_MED = register("headmed");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEAD_PAIN = register("headpain");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEAD_DEATH = register("headdeath");
    public static final DeferredHolder<SoundEvent, SoundEvent> IGGY_ATTACK = register("iggyattack");
    public static final DeferredHolder<SoundEvent, SoundEvent> IGGY_DEATH = register("iggydeath");
    public static final DeferredHolder<SoundEvent, SoundEvent> IGGY_LIVING = register("iggyliving");
    public static final DeferredHolder<SoundEvent, SoundEvent> ASHEN_LAUGH = register("ashen_laugh");
    public static final DeferredHolder<SoundEvent, SoundEvent> DOLPHIN = register("dolphin");
    public static final DeferredHolder<SoundEvent, SoundEvent> FROG_SPIT = register("frogspit");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return REGISTER.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Tropicraft.ID, name)));
    }
}
