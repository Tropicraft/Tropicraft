package net.tropicraft.core.common.entity.egg;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.TropicraftEntities;

import javax.annotation.Nullable;

public class SeaTurtleEggEntity extends EggEntity {

    public SeaTurtleEggEntity(EntityType<? extends SeaTurtleEggEntity> type, Level world) {
        super(type, world);
    }

    @Override
    @Nullable
    public Entity onHatch() {
        return TropicraftEntities.SEA_TURTLE.get().create(level());
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
