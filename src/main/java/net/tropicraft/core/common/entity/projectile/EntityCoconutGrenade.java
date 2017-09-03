package net.tropicraft.core.common.entity.projectile;

import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityCoconutGrenade extends EntityThrowable implements IProjectile {

    public EntityCoconutGrenade(World world) {
        super(world);
    }

    public EntityCoconutGrenade(World world, EntityPlayer player) {
        super(world, player);
    }

	@Override
	protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            world.createExplosion(this, posX, posY, posZ, 2.4F, true);
        }
	}
}
