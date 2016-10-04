package net.tropicraft.core.common.entity;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.tropicraft.Info;

public abstract class EntityLandHostile extends EntityLand {

    public EntityLandHostile(World world) {
        super(world);
        setSize(.7F, 1.95F);
    }
}
