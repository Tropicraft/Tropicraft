package net.tropicraft.core.common.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum TropicraftToolTiers implements IItemTier {
    BAMBOO(1, 110, 1.2F, 1F, 6, () -> {
        return Ingredient.of(Items.BAMBOO);
    }),
    ZIRCON(2, 200, 4.5f, 1f, 14, () -> {
        return Ingredient.of(TropicraftItems.ZIRCON.get());
    }),
    EUDIALYTE(2, 750, 6.5f, 2f, 14, () -> {
        return Ingredient.of(TropicraftItems.EUDIALYTE.get());
    }),
    ZIRCONIUM(3, 1800, 8.5f, 3f, 10, () -> {
        return Ingredient.of(TropicraftItems.ZIRCONIUM.get());
    })
    ;

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyValue<Ingredient> repairMaterial;

    TropicraftToolTiers(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = new LazyValue<>(repairMaterialIn);
    }

    public int getUses() {
        return this.maxUses;
    }

    public float getSpeed() {
        return this.efficiency;
    }

    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    public int getLevel() {
        return this.harvestLevel;
    }

    public int getEnchantmentValue() {
        return this.enchantability;
    }

    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}
