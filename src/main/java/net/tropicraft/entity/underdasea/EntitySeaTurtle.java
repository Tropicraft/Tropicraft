package net.tropicraft.entity.underdasea;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntitySeaTurtle extends EntityAmphibian {

    public static final int DATAWATCHER_USERRIDING = 17;    
    
    public EntitySeaTurtle(World par1World) {
        super(par1World);
        setSize(0.3f, 0.3f);
    }

    public EntitySeaTurtle(World world, float age) {
        super(world, age);
        setSize(0.3f, 0.3f);
    }
    
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(DATAWATCHER_USERRIDING, Byte.valueOf((byte)0));
    }
    
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("UserRiding", isUserRiding());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        setUserRiding(nbttagcompound.getBoolean("UserRiding"));
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        
        if(onGround && getAmphibianAge() >= 1 && returnToLand) {
            if(worldObj.getBlock((int)(this.posX), (int)(this.posY) - 1, (int)(this.posZ)).getMaterial() == Material.sand) {
                if(rand.nextInt(600) == 0) {
                    EntityTurtleEgg entityEgg = new EntityTurtleEgg(worldObj);
                    double d3 = this.posX;
                    double d4 = this.posY;
                    double d5 = this.posZ;               
                    entityEgg.setLocationAndAngles(d3, d4, d5, 0.0F, 0.0F);
                    worldObj.spawnEntityInWorld(entityEgg);
                    returnToLand = false;
                }
            }
        }
    }
    
    @Override
    public boolean interact(EntityPlayer entityplayer) {
        if (!super.interact(entityplayer)) {
            if (getAmphibianAge() >= 1 && !isUserRiding() && !worldObj.isRemote && (riddenByEntity == null || riddenByEntity == entityplayer)) {
                entityplayer.mountEntity(this);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public double getMountedYOffset() {
        return (double)height * 0.75D - 1F + 0.7F;
    }
    
    public boolean isUserRiding() {
        return (dataWatcher.getWatchableObjectByte(DATAWATCHER_USERRIDING) & 1) != 0;
    }

    public void setUserRiding(boolean flag) {
        dataWatcher.updateObject(DATAWATCHER_USERRIDING, Byte.valueOf(flag ? (byte)1 : (byte)0));
    }
}
