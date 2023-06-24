package net.tropicraft.core.common.item;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum TropicraftToolTiers implements Tier {
    BAMBOO(1, 110, 1.2F, 1F, 6, () -> Ingredient.of(Items.BAMBOO)),
    ZIRCON(2, 200, 4.5f, 1f, 14, () -> Ingredient.of(TropicraftItems.ZIRCON.get())),
    EUDIALYTE(2, 750, 6.5f, 2f, 14, () -> Ingredient.of(TropicraftItems.EUDIALYTE.get())),
    ZIRCONIUM(3, 1800, 8.5f, 3f, 10, () -> Ingredient.of(TropicraftItems.ZIRCONIUM.get()))
    ;

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyLoadedValue<Ingredient> repairMaterial;

    TropicraftToolTiers(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = new LazyLoadedValue<>(repairMaterialIn);
    }

    @Override
    public int getUses() {
        return this.maxUses;
    }

    @Override
    public float getSpeed() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}
