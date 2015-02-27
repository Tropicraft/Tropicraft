package net.tropicraft.entity.projectile;

import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityCoconutGrenade extends EntityThrowable implements IProjectile {

    public EntityCoconutGrenade(World world) {
        super(world);
    }

    public EntityCoconutGrenade(World world, EntityPlayer player) {
        super(world, player);
    }

    @Override
    protected void onImpact(MovingObjectPosition mop) {
        if (!this.worldObj.isRemote) 
            worldObj.createExplosion(this, posX, posY, posZ, 2.4F, true);
    }
}
