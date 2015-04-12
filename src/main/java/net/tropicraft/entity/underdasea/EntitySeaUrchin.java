package net.tropicraft.entity.underdasea;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;

public class EntitySeaUrchin extends EntityEchinoderm {
	/**
	 * Bounding box length/width/height of a freshly hatched sea urchin.
	 */
	public static final float BABY_SIZE = 0.25f;

	/**
	 * Bounding box length/width/height of a mature sea urchin.
	 */
	public static final float ADULT_SIZE = 0.5f;

	/**
	 * Rendered Y offset of a freshly hatched sea urchin.
	 */
	public static final float BABY_YOFFSET = 0.125f;

	/**
	 * Rendered Y offset of a mature sea urchin.
	 */
	public static final float ADULT_YOFFSET = 0.25f;

	public EntitySeaUrchin(World world) {
		super(world);
		this.experienceValue = 5;
	}

	public EntitySeaUrchin(World world, boolean baby) {
		super(world, baby);
		this.experienceValue = 5;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
	}

	@Override
	protected void updateWanderPath() {
		// sea urchins don't wander
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amt) {
		if (source.getDamageType().equals("player")) {
			Entity ent = source.getEntity();

			if (ent instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)ent;

				if (player.getHeldItem() == null) {
					player.attackEntityFrom(DamageSource.causeMobDamage(this), 2);
				}
			}
		}

		return super.attackEntityFrom(source, amt);
	}

	@Override
	public void applyEntityCollision(Entity ent) {
		super.applyEntityCollision(ent);

		if (!worldObj.isRemote) {
			if (ent instanceof EntityLiving && !(ent instanceof EntitySeaUrchin) && !(ent instanceof EntitySeaUrchinEgg)) {
				ent.attackEntityFrom(DamageSource.causeMobDamage(this), 2);
			}
		}
	}

	@Override
	protected Item getDropItem() {
		return isChild() ? null : TCItemRegistry.seaUrchinRoe;
	}

	@Override
	public EntityEchinodermEgg createEgg() {
		return new EntitySeaUrchinEgg(worldObj);
	}

	@Override
	public float getBabyWidth() {
		return BABY_SIZE;
	}

	@Override
	public float getAdultWidth() {
		return ADULT_SIZE;
	}

	@Override
	public float getBabyHeight() {
		return BABY_SIZE;
	}

	@Override
	public float getAdultHeight() {
		return ADULT_SIZE;
	}

	@Override
	public float getBabyYOffset() {
		return BABY_YOFFSET;
	}

	@Override
	public float getAdultYOffset() {
		return ADULT_YOFFSET;
	}
}