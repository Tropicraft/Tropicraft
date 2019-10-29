package net.tropicraft.core.common.block;

import java.util.Locale;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.shapes.VoxelShape;
import net.tropicraft.core.common.Util;

public enum TropicraftFlower implements Supplier<Block> {

    ACAI_VINE(Effects.REGENERATION.delegate, 0, 7, 16),
    ANEMONE(Effects.REGENERATION.delegate, 0, 9),
    BROMELIAD(Effects.REGENERATION.delegate, 0, 9),
    CANNA(Effects.REGENERATION.delegate, 0),
    COMMELINA_DIFFUSA(Effects.REGENERATION.delegate, 0),
    CROCOSMIA(Effects.REGENERATION.delegate, 0),
    CROTON(Effects.REGENERATION.delegate, 0, 13),
    DRACAENA(Effects.REGENERATION.delegate, 0, 13),
    TROPICAL_FERN(Effects.REGENERATION.delegate, 0, 13),
    FOLIAGE(Effects.REGENERATION.delegate, 0, 13),
    MAGIC_MUSHROOM(Effects.REGENERATION.delegate, 0, 11),
    ORANGE_ANTHURIUM(Effects.REGENERATION.delegate, 0, 11),
    ORCHID(Effects.REGENERATION.delegate, 0),
    PATHOS(Effects.REGENERATION.delegate, 0, 15, 12),
    RED_ANTHURIUM(Effects.REGENERATION.delegate, 0, 11);

    private final String name;
    private final Supplier<Effect> effect;
    private final int effectDuration;
    private final VoxelShape shape;

    private TropicraftFlower(Supplier<Effect> effect, int effectDuration) {
        this(effect, effectDuration, 7);
    }

    private TropicraftFlower(Supplier<Effect> effect, int effectDuration, int w) {
        this(effect, effectDuration, w, 15);
    }

    private TropicraftFlower(Supplier<Effect> effect, int effectDuration, int w, int h) {
        this(null, effect, effectDuration, w, h);
    }
    
    private TropicraftFlower(@Nullable String name, Supplier<Effect> effect, int effectDuration, int w, int h) {
        this.name = name == null ? Util.toEnglishName(name()) : name;
        this.effect = effect;
        this.effectDuration = effectDuration;
        float halfW = w / 2f;
        this.shape = Block.makeCuboidShape(8 - halfW, 0, 8 - halfW, 8 + halfW, h, 8 + halfW);
    }

    public Effect getEffect() {
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
