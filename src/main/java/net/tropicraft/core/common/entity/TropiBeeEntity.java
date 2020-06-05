package net.tropicraft.core.common.entity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.TropicraftItems;

public class TropiBeeEntity extends BeeEntity {
    public TropiBeeEntity(EntityType<? extends BeeEntity> p_i225714_1_, World p_i225714_2_) {
        super(p_i225714_1_, p_i225714_2_);
    }

    @Override
    public BeeEntity createChild(AgeableEntity mob) {
        return TropicraftEntities.TROPI_BEE.get().create(this.world);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.TROPIBEE_SPAWN_EGG.get());
    }
}
