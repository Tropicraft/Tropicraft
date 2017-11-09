package net.tropicraft.core.common.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityPoisonBlot extends EntityThrowable {

    public EntityPoisonBlot(World par1World) {
        super(par1World);
    }
    
    public EntityPoisonBlot(World par1World, EntityLivingBase thrower) {
        super(par1World, thrower);
    }

    @Override
    protected void onImpact(RayTraceResult mop) {
        if (mop.entityHit != null) {
            if (mop.entityHit instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)mop.entityHit;
                player.addPotionEffect(new PotionEffect(MobEffects.POISON, 12 * 20, 0));
                this.setDead();
            }
        }
    }

}
