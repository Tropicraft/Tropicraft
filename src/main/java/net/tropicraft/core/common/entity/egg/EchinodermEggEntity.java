package net.tropicraft.core.common.entity.egg;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class EchinodermEggEntity extends EggEntity {

    public EchinodermEggEntity(final EntityType<? extends EchinodermEggEntity> type, World world) {
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
    
    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }
}
