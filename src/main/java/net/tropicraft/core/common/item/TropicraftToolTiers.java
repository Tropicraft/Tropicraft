package net.tropicraft.core.common.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;

import java.util.function.Supplier;

public enum TropicraftToolTiers implements IItemTier {
    BAMBOO(1, 110, 1.2F, 1F, 6, () -> {
        return Ingredient.fromItems(Items.BAMBOO);
    }),
    ZIRCON(2, 200, 4.5f, 1f, 14, () -> {
        return Ingredient.fromItems(TropicraftItems.ZIRCON.get());
    }),
    EUDIALYTE(2, 750, 6.5f, 2f, 14, () -> {
        return Ingredient.fromItems(TropicraftItems.EUDIALYTE.get());
    }),
    ZIRCONIUM(3, 1800, 8.5f, 3f, 10, () -> {
        return Ingredient.fromItems(TropicraftItems.ZIRCONIUM.get());
    })
    ;

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyLoadBase<Ingredient> repairMaterial;

    TropicraftToolTiers(int p_i48458_3_, int p_i48458_4_, float p_i48458_5_, float p_i48458_6_, int p_i48458_7_, Supplier<Ingredient> p_i48458_8_) {
        this.harvestLevel = p_i48458_3_;
        this.maxUses = p_i48458_4_;
        this.efficiency = p_i48458_5_;
        this.attackDamage = p_i48458_6_;
        this.enchantability = p_i48458_7_;
        this.repairMaterial = new LazyLoadBase<>(p_i48458_8_);
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public float getEfficiency() {
        return this.efficiency;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }
}
