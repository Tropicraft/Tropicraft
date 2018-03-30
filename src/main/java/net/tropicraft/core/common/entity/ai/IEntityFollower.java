package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.EntityLivingBase;

public interface IEntityFollower {
    public EntityLivingBase getFollowingEntity();
    public void setFollowingEntity(EntityLivingBase entity);
}
