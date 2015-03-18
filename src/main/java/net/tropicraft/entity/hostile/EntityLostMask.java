package net.tropicraft.entity.hostile;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import CoroUtil.api.weather.WindHandler;

public class EntityLostMask extends Entity implements WindHandler{

	//Client side, used for mask bob
	public int type;
	public float bobber;
	private double launchedSpeed = 1D;
	
	public EntityLostMask(World par1World) {
		super(par1World);
		this.setSize(1f, 1f);
	}
	/**
	 * Spawns a LostMask into the world at a given position and angle
	 * @param world World object
	 * @param color Mask type
	 * @param x X position
	 * @param y Y position
	 * @param z Z position
	 * @param angle Use the "attackers" rotationYaw
	 */
	public EntityLostMask(World world, int type, double x, double y, double z, double angle){
		this(world);
		this.setPosition(x, y, z);		
		this.setType(type);
		motionX = Math.cos(Math.toRadians(angle + 90))*launchedSpeed;
		motionZ = Math.sin(Math.toRadians(angle + 90))*launchedSpeed;
		double subAngle = MathHelper.wrapAngleTo180_double(angle);
    	double subAngle2 = subAngle + (180 - subAngle)*2;
		this.rotationYaw = (float) (subAngle2);
	}
	@Override
	public boolean canBeCollidedWith(){
		return true;
	}
	@Override
	public boolean canBePushed(){
		return true;
	}
	@Override 
	public void onUpdate(){
		if(onGround){
			this.motionX *= .5F;
			this.motionZ *= .5F;
			this.motionY = 0;
		}
		if(this.checkForWater(0)){
			this.motionY =.02F;
			this.motionX *= .95F;
			this.motionZ *= .95F;
		}
		
		else{
			int xMod = worldObj.rand.nextInt(10);
			int yMod = worldObj.rand.nextInt(30);
			int zMod = worldObj.rand.nextInt(10);
			int[] a = this.getRotator();
			a[0] += xMod;
			a[1] += yMod;
			a[2] += zMod;
			this.setRotator(a);
			motionY -= .05f;
		}
		this.moveEntity(motionX, motionY, motionZ);		
	}
	@Override
	protected void entityInit() {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(0));
        this.dataWatcher.addObject(19, new Integer(0));
        this.dataWatcher.addObject(20, new Integer(0));
		
	}

	
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.setType(nbt.getInteger("MaskType"));
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		nbt.setInteger("MaskType", this.getColor());

	}
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            if (!this.isDead && !this.worldObj.isRemote)
            {
                this.setDead();
                this.setBeenAttacked();
                EntityPlayer entityplayer = null;

                if (par1DamageSource.getEntity() instanceof EntityPlayer)
                {
                    entityplayer = (EntityPlayer)par1DamageSource.getEntity();
                }

                if (entityplayer != null && entityplayer.capabilities.isCreativeMode)
                {
                    return true;
                }

                this.dropItemStack();
            }

            return true;
        }
    }
	public void dropItemStack() {
		//TODO: Ashen masks
		//this.entityDropItem(new ItemStack(TCItemRegistry.ashenMasks, 1, getColor()), 0.0F);
	}
	private void setRotator(int[] a){
		this.dataWatcher.updateObject(18, new Integer(a[0]));
		this.dataWatcher.updateObject(19, new Integer(a[1]));
		this.dataWatcher.updateObject(20, new Integer(a[2]));
	}
	public int[] getRotator(){
		return new int[]{this.dataWatcher.getWatchableObjectInt(18), this.dataWatcher.getWatchableObjectInt(19), this.dataWatcher.getWatchableObjectInt(20)};
	}
	private void setType(int i){
		this.dataWatcher.updateObject(17, new Integer(i));
	}	
	
	
	public boolean checkForWater(int i){
		return worldObj.getBlock((int)Math.floor(posX), (int) Math.floor(posY) + i, (int)Math.floor(posZ)).getMaterial() == Material.water;
	}
	public int getMode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getColor() {
		// TODO Auto-generated method stub
		return this.dataWatcher.getWatchableObjectInt(17);
	}

	public int getDirection() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getWindWeight() {
		
		return 999999;
	}

	@Override
	public int getParticleDecayExtra() {
		// TODO Auto-generated method stub
		return 0;
	}

}