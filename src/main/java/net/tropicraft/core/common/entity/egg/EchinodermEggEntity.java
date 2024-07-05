package net.tropicraft.core.common.entity.egg;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class EchinodermEggEntity extends EggEntity {

    public EchinodermEggEntity(EntityType<? extends EchinodermEggEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public boolean shouldEggRenderFlat() {
        return true;
    }

    @Override
    public int getHatchTime() {
        return 2 * 60 * 20;
    }

    @Override
    public int getPreHatchMovement() {
        return 0;
    }
}
