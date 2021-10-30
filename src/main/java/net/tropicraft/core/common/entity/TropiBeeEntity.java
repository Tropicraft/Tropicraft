package net.tropicraft.core.common.entity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.common.item.TropicraftItems;

public class TropiBeeEntity extends BeeEntity {
    public TropiBeeEntity(EntityType<? extends BeeEntity> type, World world) {
        super(type, world);
    }

    @Override
    public BeeEntity getBreedOffspring(ServerWorld world, AgeableEntity partner) {
        return TropicraftEntities.TROPI_BEE.get().create(this.level);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.TROPIBEE_SPAWN_EGG.get());
    }
}
