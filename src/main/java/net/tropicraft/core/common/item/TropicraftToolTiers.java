package net.tropicraft.core.common.item;

import com.google.common.base.Suppliers;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public enum TropicraftToolTiers implements Tier {
    BAMBOO(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 110, 1.2F, 1F, 6, () -> Ingredient.of(Items.BAMBOO)),
    ZIRCON(BlockTags.INCORRECT_FOR_STONE_TOOL, 200, 4.5f, 1f, 14, () -> Ingredient.of(TropicraftItems.ZIRCON.get())),
    EUDIALYTE(BlockTags.INCORRECT_FOR_STONE_TOOL, 750, 6.5f, 2f, 14, () -> Ingredient.of(TropicraftItems.EUDIALYTE.get())),
    ZIRCONIUM(BlockTags.INCORRECT_FOR_IRON_TOOL, 1800, 8.5f, 3f, 10, () -> Ingredient.of(TropicraftItems.ZIRCONIUM.get()));

    private final TagKey<Block> incorrectBlockDrops;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairMaterial;

    TropicraftToolTiers(TagKey<Block> incorrectBlockDrops, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.incorrectBlockDrops = incorrectBlockDrops;
        maxUses = maxUsesIn;
        efficiency = efficiencyIn;
        attackDamage = attackDamageIn;
        enchantability = enchantabilityIn;
        repairMaterial = Suppliers.memoize(repairMaterialIn::get);
    }

    @Override
    public int getUses() {
        return maxUses;
    }

    @Override
    public float getSpeed() {
        return efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamage;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return incorrectBlockDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairMaterial.get();
    }
}
