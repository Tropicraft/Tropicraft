package net.tropicraft.core.common.entity.ai;

import net.minecraft.world.entity.LivingEntity;

public interface IEntityFollower {
    LivingEntity getFollowingEntity();

    void setFollowingEntity(LivingEntity entity);
}



