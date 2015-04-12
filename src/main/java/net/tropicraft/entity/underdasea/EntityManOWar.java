package net.tropicraft.entity.underdasea;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;

public class EntityManOWar extends EntityWaterMob {

	public float important1;
	protected float randomMotionSpeed;
	protected float important2;
	protected float randomMotionVecX;
	protected float randomMotionVecY;
	protected float randomMotionVecZ;

	public EntityManOWar(World world){
		super(world);
		//texture = ModInfo.TEXTURE_ENTITY_LOC + "manowar.png";
		important1 = 0.0F;       
		randomMotionSpeed = 0.0F;
		important2 = 0.0F;
		randomMotionVecX = 0.0F;
		randomMotionVecY = 0.0F;
		randomMotionVecZ = 0.0F;
		important2 = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
		setSize(0.6f,0.6f);
		this.experienceValue = 7;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(5.0D);
	}

	@Override
	public boolean canBreatheUnderwater()
	{
		return true;
	}

	public byte getAttackStrength() {
		switch (worldObj.difficultySetting) {
		case EASY: return 1;
		case NORMAL: return 2;
		case HARD: return 3;
		default: return 0;
		}	
	}

	protected Entity getTarget() {	
		return null;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (worldObj.isRemote) return;

		if(inWater){
			if(attackTime == 0){
				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(2D, 8D, 2D).getOffsetBoundingBox(0.0D, -8.0D, 0.0D));
				for (int i = 0; i < list.size(); i++) {
					Entity ent = (Entity)list.get(i);
					if(!(ent instanceof EntityManOWar )){
						if(ent instanceof EntityLiving && ((EntityLiving)ent).isInWater()){
							byte byte0 = getAttackStrength();
							((EntityLiving)ent).attackEntityFrom(DamageSource.drown, byte0);
							attackTime = 60;
						}
					}
				}
			}
			important1 += important2;        
			if(important1 > 6.283185F)
			{
				important1 -= 6.283185F;
				if(rand.nextInt(10) == 0)
				{
					important2 = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
				}
			}
			if(important1 < 3.141593F)
			{
				float f = important1 / 3.141593F;
				if((double)f > 0.75D)
				{
					randomMotionSpeed = 1.0F;
				} 
			} else
			{
				randomMotionSpeed = randomMotionSpeed * 0.95F;
			}
			if(!worldObj.isRemote)
			{
				motionX = randomMotionVecX * randomMotionSpeed;
				motionY = randomMotionVecY * randomMotionSpeed;
				motionZ = randomMotionVecZ * randomMotionSpeed;
			}
			float f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
			renderYawOffset += ((-(float)Math.atan2(motionX, motionZ) * 180F) / 3.141593F - renderYawOffset) * 0.1F;
			rotationYaw = renderYawOffset;            
		}
		else
		{
			if(!worldObj.isRemote)
			{
				motionX = 0.0D;               
				motionY *= 0.98000001907348633D;
				motionZ = 0.0D;
			}            
			if(onGround && deathTime == 0){      	
				this.attackEntityFrom(DamageSource.drown, 1);
				int d = 1;
				int e = 1;
				if(rand.nextInt(2) == 0){
					d = -1;
				}
				if(rand.nextInt(2) == 0){
					e = -1;
				}
				motionZ = rand.nextFloat()*.20F *d;
				motionX = rand.nextFloat()*.20F*e;
			}
			if(!inWater){
				motionY -= 0.080000000000000002D;
			}
		}
	}

	@Override
	protected void updateEntityActionState()
	{
		if(rand.nextInt(150) == 0 || !inWater || randomMotionVecX == 0.0F && randomMotionVecY == 0.0F && randomMotionVecZ == 0.0F)
		{
			float f = rand.nextFloat() * 3.141593F * 2.0F;
			randomMotionVecX = MathHelper.cos(f) * 0.025F;            
			randomMotionVecZ = MathHelper.sin(f) * 0.025F;
		}

		if(inWater){

			if(posY < 62.5){
				randomMotionVecY = .05F;
			}
			if(posY >= 62.5F){
				randomMotionVecY = 0F;
			}
		}

	}
	@Override
	public void onDeath(DamageSource d) {
		super.onDeath(d);
		if (!this.worldObj.isRemote) {
	        int numDrops = 3 + this.rand.nextInt(1);

	        for (int i = 0; i < numDrops; i++)
	            this.dropItem(Items.slime_ball, 1);
		}
	}
	
	@Override
	public boolean getCanSpawnHere() {
		return !worldObj.checkBlockCollision(boundingBox);
	}

	@Override
	public int getTalkInterval() {
		return 120;
	}

	@Override
	protected boolean canDespawn() {
		return true;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer) {
		return 3 + worldObj.rand.nextInt(3);
	}
}