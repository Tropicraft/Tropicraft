package net.tropicraft.core.common.block;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.tropicraft.core.common.TropicraftTags;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Supplier;

public enum TropicraftFlower implements Supplier<Block> {

    ACAI_VINE(MobEffects.REGENERATION.delegate, 0, 7, 16, null, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS),
    ANEMONE(MobEffects.REGENERATION.delegate, 0, 9, null, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS, TropicraftTags.Blocks.RAINFOREST_FLOWERS, TropicraftTags.Blocks.OVERWORLD_FLOWERS),
    BROMELIAD(MobEffects.REGENERATION.delegate, 0, 9, null, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS, TropicraftTags.Blocks.RAINFOREST_FLOWERS),
    CANNA(MobEffects.REGENERATION.delegate, 0, Items.YELLOW_DYE, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS, TropicraftTags.Blocks.RAINFOREST_FLOWERS),
    COMMELINA_DIFFUSA(MobEffects.REGENERATION.delegate, 0, Items.LIGHT_BLUE_DYE, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS, TropicraftTags.Blocks.RAINFOREST_FLOWERS, TropicraftTags.Blocks.OVERWORLD_FLOWERS),
    CROCOSMIA(MobEffects.REGENERATION.delegate, 0, null, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS, TropicraftTags.Blocks.RAINFOREST_FLOWERS),
    CROTON(MobEffects.REGENERATION.delegate, 0, 13, null, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS),
    DRACAENA(MobEffects.REGENERATION.delegate, 0, 13, Items.GREEN_DYE, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS, TropicraftTags.Blocks.RAINFOREST_FLOWERS),
    TROPICAL_FERN(MobEffects.REGENERATION.delegate, 0, 13, null, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS),
    FOLIAGE(MobEffects.REGENERATION.delegate, 0, 13, null, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS),
    MAGIC_MUSHROOM(MobEffects.REGENERATION.delegate, 0, 11, null, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.RAINFOREST_FLOWERS),
    ORANGE_ANTHURIUM(MobEffects.REGENERATION.delegate, 0, 11, Items.ORANGE_DYE, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS, TropicraftTags.Blocks.RAINFOREST_FLOWERS, TropicraftTags.Blocks.OVERWORLD_FLOWERS),
    ORCHID(MobEffects.REGENERATION.delegate, 0, null, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS, TropicraftTags.Blocks.RAINFOREST_FLOWERS, TropicraftTags.Blocks.OVERWORLD_FLOWERS),
    PATHOS(MobEffects.REGENERATION.delegate, 0, 15, 12, null, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS, TropicraftTags.Blocks.OVERWORLD_FLOWERS),
    RED_ANTHURIUM(MobEffects.REGENERATION.delegate, 0, 11, Items.RED_DYE, BlockTags.SMALL_FLOWERS, TropicraftTags.Blocks.TROPICS_FLOWERS, TropicraftTags.Blocks.RAINFOREST_FLOWERS, TropicraftTags.Blocks.OVERWORLD_FLOWERS);

    private final Supplier<MobEffect> effect;
    private final int effectDuration;
    private final VoxelShape shape;
    private final TagKey<Block>[] tags;
    @Nullable
    private final Item dye;

    @SafeVarargs
    TropicraftFlower(Supplier<MobEffect> effect, int effectDuration, Item dye, TagKey<Block>... tags) {
        this(effect, effectDuration, 7, dye, tags);
    }

    @SafeVarargs
    TropicraftFlower(Supplier<MobEffect> effect, int effectDuration, int w, Item dye, TagKey<Block>... tags) {
        this(effect, effectDuration, w, 15, dye, tags);
    }

    @SafeVarargs
    TropicraftFlower(Supplier<MobEffect> effect, int effectDuration, int w, int h, Item dye, TagKey<Block>... tags) {
        this(null, effect, effectDuration, w, h, dye, tags);
    }

    @SafeVarargs
    TropicraftFlower(@Nullable String name, Supplier<MobEffect> effect, int effectDuration, int w, int h, @Nullable Item dye, final TagKey<Block>... tags) {
        this.effect = effect;
        this.effectDuration = effectDuration;
        this.dye = dye;
        this.tags = tags;
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

    public TagKey<Block>[] getTags() {
        return tags;
    }

    @Nullable
    public Item getDye() {
        return dye;
    }
}
