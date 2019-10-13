package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.world.World;

public class TropicraftWaterBaseEntity extends WaterMobEntity {

    public TropicraftWaterBaseEntity(EntityType<? extends WaterMobEntity> type, World world) {
        super(type, world);
    }
}
