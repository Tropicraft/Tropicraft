package net.tropicraft.core.common.entity.underdasea;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityMarlin extends EntityTropicraftWaterMob {

	public String texture = "";

	public EntityMarlin(World world) {
		super(world);
//		hyperness = 30;
//		fickleness = 150;
//		horFactor = .3F;
//		climbFactor = .225F;
		//this.type = WaterMobType.OCEAN_DWELLER;
		/*texture = ModInfo.TEXTURE_ENTITY_LOC + "marlin.png";
		if(world.isRemote){
			if(rand.nextInt(70) == 0)
			texture = ModInfo.TEXTURE_ENTITY_LOC + "marlin2.png";
		}*/
		setSize(1.4f, 0.95f);
//		this.isCatchable = true;
//		this.fishingMaxLookDist = 40D;
//		this.fishingInterestOdds = 6;
		this.experienceValue = 5;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound n) {
		n.setString("texture", texture);
		super.writeEntityToNBT(n);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound n) {
		texture = n.getString("texture");
		super.readEntityFromNBT(n);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SQUID_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	protected void dropFewItems(boolean flag) {
		int i = rand.nextInt(3) + 1;
		for (int j = 0; j < i; j++) {
			if (!world.isRemote) {
				entityDropItem(new ItemStack(ItemRegistry.freshMarlin), 0.0F);
			}
		}
	}

	/**
	 * drops the loot of this entity upon death
	 */
	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		int numDrops = 2 + this.rand.nextInt(1 + lootingModifier);

		for (int i = 0; i < numDrops; i++) {
			if (!world.isRemote) {
				this.dropItem(ItemRegistry.freshMarlin, 1);
			}
		}
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
		dropFewItems(true);
	}

	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        return false;
    }

//	@Override
//	public void applyEntityCollision(Entity entity) {
//		super.applyEntityCollision(entity);
//		if (isSurfacing) {
//			//entity.attackEntityFrom(DamageSource.causeMobDamage(this), attackStrength());
//		}
//	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

//	@Override
//	protected void updateEntityActionState() {
//		super.updateEntityActionState();
//		if (isInWater() && world.rand.nextInt(500) == 0 && !isSurfacing
//				&& Math.floor(posY) >= 60D) {
//			//System.out.println("Aye");
//			isSurfacing = true;
//			motionX *= 1.5F;
//			motionZ *= 1.5F;
//			addVelocity(0, .75D, 0);
//			surfaceTick = 20;
//			reachedTarget = false;
//			return;
//		}
//	}

//	@Override
//	protected int attackStrength() {
//		switch (world.difficultySetting) {
//		case EASY:
//			return 3;
//		case NORMAL:
//			return 5;
//		case HARD:
//			return 7;
//		default:
//			return 0;
//		}
//	}

	@Override
	protected boolean canDespawn() {
		return true;
	}

}