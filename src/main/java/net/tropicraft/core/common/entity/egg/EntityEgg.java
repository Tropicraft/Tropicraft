package net.tropicraft.core.common.entity.egg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityEgg extends EntityLiving {

	private static final DataParameter<Integer> HATCH_DELAY = EntityDataManager.<Integer>createKey(EntityEgg.class, DataSerializers.VARINT);

    public double rotationRand;
   
    public EntityEgg(World w) {
        super(w);
        setSize(.4F, .5F);
        rotationRand = 0;
        ignoreFrustumCheck = true;
       
        this.rotationYaw = rand.nextInt(360);
    }
    
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(HATCH_DELAY, Integer.valueOf(0));
		setHatchDelay(-60+rand.nextInt(120));
	}
    
    @SideOnly(Side.CLIENT)
    public abstract boolean shouldEggRenderFlat();
    
    public abstract String getEggTexture();
    
    /**
     * Create and return a Entity here
     */
    public abstract Entity onHatch();
    
    /**
     * The amount of time in ticks it will take for the egg to hatch
     * 	eg. hatch on tick n
     * @return a positive number
     */
    public abstract int getHatchTime();
    
    
    /**
     * The amount of time in ticks the egg will move around before it hatches
     *  eg. start rolling n ticks before hatch
     * @return a positive number lower than getHatchTime()
     */
    public abstract int getPreHatchMovement();
    
    public int getRandomHatchDelay() {
    		return this.getDataManager().get(HATCH_DELAY);
    }
     
    public boolean isHatching() {
    		return this.ticksExisted > (getHatchTime()+getRandomHatchDelay());
    }
    
    public boolean isNearHatching() {
    		return this.ticksExisted > (getHatchTime()+getRandomHatchDelay()) - getPreHatchMovement();
    }
    
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        
        if (isNearHatching()) {
            rotationRand += 0.1707F * world.rand.nextFloat();
            
            // Hatch time!
            if (ticksExisted >= this.getHatchTime()) {
                if (!world.isRemote) {
                		Entity ent = onHatch();
                		ent.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
                	    world.spawnEntity(ent);
                    this.setDead();
                }
            }
        } 
    }
    
	@Override
	public void writeEntityToNBT(NBTTagCompound n) {
		n.setInteger("ticks", this.ticksExisted);
		n.setInteger("hatchDelay", getHatchDelay());
		super.writeEntityToNBT(n);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound n) {
		this.ticksExisted = n.getInteger("ticks");
		setHatchDelay(n.getInteger("hatchDelay"));
		super.readEntityFromNBT(n);
	}

	@Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2.0D);
    }
	
	public void setHatchDelay(int i) {
		this.getDataManager().set(HATCH_DELAY, -60+rand.nextInt(120));
	}
	
	public int getHatchDelay() {
		return this.getDataManager().get(HATCH_DELAY);
	}
}
