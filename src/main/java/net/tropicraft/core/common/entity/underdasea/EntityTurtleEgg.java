package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityTurtleEgg extends EntityLiving {

    public int hatchingTime;
    public double rotationRand;
    public BlockPos parentWaterLoc = null;
    
    public EntityTurtleEgg(World par1World) {
        super(par1World);
        setSize(.4F, .7F);
        hatchingTime = 0;
        rotationRand = 10;
        ignoreFrustumCheck = true;
    }
    
    public void onUpdate() {
        super.onUpdate();        
        rotationYaw = 0;
        
        // Once it has lived 400 ticks (20 seconds (20 sec * 20 ticks = 400))
        // Start the hatch countdown
        if (ticksExisted % 400 == 0) {
            hatchingTime = 360;
        }
        
        // So that we don't try hatching before the countdown has started
        // But if we are starting the process of hatching (not at the end yet), spin and decrement counter
        if (hatchingTime != 0) {
            // Do crazy spinny stuff
            rotationRand += 0.1707F * world.rand.nextFloat();
            hatchingTime--;
            
            // Hatch time!
            if (hatchingTime == 1) {
                if (!world.isRemote) {
                    EntitySeaTurtle babyturtle = new EntitySeaTurtle(world, 2);
                    double d3 = this.posX;
                    double d4 = this.posY;
                    double d5 = this.posZ;
                    babyturtle.setLocationAndAngles(d3, d4, d5, 0.0F, 0.0F);
                    world.spawnEntity(babyturtle);
                		this.spawnExplosionParticle();
                		if(this.parentWaterLoc != null) {
                			babyturtle.log("received parent's water entry point, ms saved \\o/");
                		}
                		babyturtle.targetWaterSite = this.parentWaterLoc;
                		babyturtle.isSeekingWater = true;
                		babyturtle.isLandPathing = true;
                    this.setDead();
                }
                
//                for (int i = 0; i < 8; i++) {
//                    world.spawnParticle("snowballpoof", posX, posY, posZ,
//                            0.0D, 0.0D, 0.0D);
//                }
            }
            
            // Stop doing crazy spinny stuff
            if (hatchingTime == 0) {
                rotationRand = 0;
            }
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2.0D);
    }
}
