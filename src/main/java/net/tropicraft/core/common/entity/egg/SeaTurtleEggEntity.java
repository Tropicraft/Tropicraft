package net.tropicraft.core.common.entity.egg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.TropicraftEntities;

public class SeaTurtleEggEntity extends EggEntity {

    public SeaTurtleEggEntity(final EntityType<? extends SeaTurtleEggEntity> type, World world) {
        super(type, world);
    }

    @Override
    public Entity onHatch() {
        return TropicraftEntities.SEA_TURTLE.get().create(world);
    }

    @Override
    public int getHatchTime() {
        return 760;
    }

    @Override
    public int getPreHatchMovement() {
        return 360;
    }

    @Override
    public String getEggTexture() {
        return "turtle/egg_text";
    }

    @Override
    public boolean shouldEggRenderFlat() {
        return false;
    }

}