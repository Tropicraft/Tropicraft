package net.tropicraft.core.common.block;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.tropicraft.core.common.Util;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Supplier;

public enum TropicraftFlower implements Supplier<Block> {

    ACAI_VINE(MobEffects.REGENERATION.delegate, 0, 7, 16),
    ANEMONE(MobEffects.REGENERATION.delegate, 0, 9),
    BROMELIAD(MobEffects.REGENERATION.delegate, 0, 9),
    CANNA(MobEffects.REGENERATION.delegate, 0),
    COMMELINA_DIFFUSA(MobEffects.REGENERATION.delegate, 0),
    CROCOSMIA(MobEffects.REGENERATION.delegate, 0),
    CROTON(MobEffects.REGENERATION.delegate, 0, 13),
    DRACAENA(MobEffects.REGENERATION.delegate, 0, 13),
    TROPICAL_FERN(MobEffects.REGENERATION.delegate, 0, 13),
    FOLIAGE(MobEffects.REGENERATION.delegate, 0, 13),
    MAGIC_MUSHROOM(MobEffects.REGENERATION.delegate, 0, 11),
    ORANGE_ANTHURIUM(MobEffects.REGENERATION.delegate, 0, 11),
    ORCHID(MobEffects.REGENERATION.delegate, 0),
    PATHOS(MobEffects.REGENERATION.delegate, 0, 15, 12),
    RED_ANTHURIUM(MobEffects.REGENERATION.delegate, 0, 11);

    private final String name;
    private final Supplier<MobEffect> effect;
    private final int effectDuration;
    private final VoxelShape shape;

    private TropicraftFlower(Supplier<MobEffect> effect, int effectDuration) {
        this(effect, effectDuration, 7);
    }

    private TropicraftFlower(Supplier<MobEffect> effect, int effectDuration, int w) {
        this(effect, effectDuration, w, 15);
    }

    private TropicraftFlower(Supplier<MobEffect> effect, int effectDuration, int w, int h) {
        this(null, effect, effectDuration, w, h);
    }
    
    private TropicraftFlower(@Nullable String name, Supplier<MobEffect> effect, int effectDuration, int w, int h) {
        this.name = name == null ? Util.toEnglishName(name()) : name;
        this.effect = effect;
        this.effectDuration = effectDuration;
        float halfW = w / 2f;
        this.shape = Block.box(8 - halfW, 0, 8 - halfW, 8 + halfW, h, 8 + halfW);
    }

    public MobEffect getEffect() {
        return effect.get();
    }

    public int getEffectDuration() {
        return effectDuration;
    }

    public VoxelShape getShape() {
        return shape;
    }
    
    public String getId() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public Block get() {
        return TropicraftBlocks.FLOWERS.get(this).get();
    }

    public String getEnglishName() {
        return name;
    }
}
