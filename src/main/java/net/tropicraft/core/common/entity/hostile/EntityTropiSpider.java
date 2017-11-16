package net.tropicraft.core.common.entity.hostile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.entity.ai.EntityAIWanderNotLazy;
import net.tropicraft.core.common.entity.egg.EntityTropiSpiderEgg;

public class EntityTropiSpider extends EntitySpider implements IMob {

	private static final DataParameter<Byte> TYPE = EntityDataManager.<Byte>createKey(EntityTropiSpider.class, DataSerializers.BYTE);
	private static final int SPIDER_MATURE_AGE = 20 * 60 * 10; // From child to adult in 10 real minutes
	private static final int SPIDER_MAX_EGGS = 10;
	private static final long SPIDER_MIN_EGG_DELAY = 12000; // Once per half minecraft day minimum
	private static final int SPIDER_EGG_CHANCE = 1000;

	private BlockPos nestSite;
	private EntityTropiSpider mother = null;
	private long ticksSinceLastEgg = 0L;
	public byte initialType = 0;

	public EntityTropiSpider(World par1World) {
		super(par1World);
		this.setSize(1.4F, 0.9F);
		this.ticksExisted = SPIDER_MATURE_AGE;
		this.ticksSinceLastEgg = this.ticksExisted;
	}

	public EntityTropiSpider(World w, int motherID) {
		this(w);
		setType(Type.CHILD);
		this.ticksExisted = 0;
		Entity ent = world.getEntityByID(motherID);
		if (ent != null) {
			mother = (EntityTropiSpider) ent;
		}
		world.getEntityByID(this.getEntityId());
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(TYPE, Byte.valueOf((byte) initialType));
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 0.8D, false));
		
		this.tasks.addTask(7, new EntityAIWanderNotLazy(this, 0.8D, 40));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		if(damageSrc.getTrueSource() != null && damageSrc.getTrueSource() instanceof EntityLivingBase) {
			this.setAttackTarget((EntityLivingBase)damageSrc.getTrueSource());
		}
		super.damageEntity(damageSrc, damageAmount);
	}

	@Override
	protected void dropFewItems(boolean par1, int par2) {
		int j = this.rand.nextInt(2) + this.rand.nextInt(1 + par2);
		for (int k = 0; k < j; ++k) {
			this.dropItem(Items.STRING, 1);
		}
	}

	public boolean isOnLadder() {
		return this.isBesideClimbableBlock() && this.getNavigator().noPath();
	}

	public boolean isBesideClimbableBlock() {
		return this.collidedHorizontally;
	}

	@Override
	public void onLivingUpdate() {
		fallDistance = 0;
		if (this.getType() == Type.CHILD) {
			this.setSize(0.9F, 0.7F);
		} else {
			this.setSize(1.4F, 0.9F);
		}
		super.onLivingUpdate();
		if(this.getAttackTarget() != null) {
			if(this.getDistanceSq(this.getAttackTarget()) < 128D) {
				Util.tryMoveToEntityLivingLongDist(this, this.getAttackTarget(), 0.8f);
			}
		}
		if (!world.isRemote && this.getAttackTarget() != null && onGround && rand.nextInt(3) == 0
				&& this.getAttackTarget().getDistance(this) < 5) {
			this.getNavigator().clearPath();
			this.jump();
			this.jumpMovementFactor = 0.3f;
		}else {
			this.jumpMovementFactor = 0.2f;
		}
		if (!world.isRemote) {
			if (this.getType() == Type.CHILD) {
				if (this.ticksExisted >= SPIDER_MATURE_AGE) {
					this.setType(Type.ADULT);
				}
				if (this.mother != null) {
					if (this.getDistanceSq(mother) > 16D) {
						Util.tryMoveToEntityLivingLongDist(this, mother, 0.8f);
					} else {
						this.getNavigator().clearPath();
					}
					if(this.mother.getAttackTarget() != null) {
						this.setAttackTarget(this.mother.getAttackTarget());
					}
				}
			}

			if (this.getType() == Type.ADULT) {

				if (this.mother != null) {
					if(this.mother.isDead) {
						this.mother = null;
						this.getNavigator().clearPath();
						this.setAttackTarget(null);
					}
					// issues much?
					this.setAttackTarget(this.mother);
				}
				if (rand.nextInt(SPIDER_EGG_CHANCE) == 0 && this.ticksSinceLastEgg > SPIDER_MIN_EGG_DELAY && this.ticksExisted % 80 == 0) {
					buildNest();
				}
			}

			if (this.getType() == Type.MOTHER) {
				if (this.nestSite != null) {
					if (this.ticksSinceLastEgg < 2000) {
						if (this.getDistanceSq(nestSite) > 16D) {
							Util.tryMoveToXYZLongDist(this, nestSite, 0.9f);
						}
					} else {
						this.nestSite = null;
					}
				}
			}
			this.ticksSinceLastEgg++;
		}
	}
	
	
	@Override
	protected SoundEvent getAmbientSound() {
		return rand.nextInt(20) == 0 ? super.getAmbientSound() : null;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		if(this.getType() == Type.CHILD) {
			if(rand.nextInt(20) == 0) {
				super.playStepSound(pos, blockIn);
			}
		}else {
			super.playStepSound(pos, blockIn);
		}
	}

	@Override
	public boolean canBePushed() {
		return this.getType() != Type.MOTHER;
	}

	public void buildNest() {
		if(!world.isRemote) {
			this.setType(Type.MOTHER);
			int r = rand.nextInt(SPIDER_MAX_EGGS)+1;
			
			if(r < 2) {
				return;
			}
			
			for (int i = 0; i < r; i++) {
				EntityTropiSpiderEgg egg = new EntityTropiSpiderEgg(world);
				egg.motherID = this.getEntityId();
				egg.setPosition(getPosition().getX() + rand.nextFloat(), getPosition().getY(),
						getPosition().getZ() + rand.nextFloat());
				world.spawnEntity(egg);
				this.ticksSinceLastEgg = 0;
			}
			
			for (int x = 0; x < 5; x++) {
				for (int z = 0; z < 5; z++) {
					if (rand.nextInt(8) == 0) {
						BlockPos pos = new BlockPos(getPosition().getX() - 2 + x, getPosition().getY(),
								getPosition().getZ() - 2 + z);
						if (world.getBlockState(pos).getBlock().equals(Blocks.AIR) &&
								world.getBlockState(pos.down(1)).getMaterial().isSolid()) {
							world.setBlockState(pos, Blocks.WEB.getDefaultState());
						}
					}
				}
			}
			nestSite = getPosition();
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound n) {
		n.setInteger("ticks", this.ticksExisted);
		n.setByte("spiderType", getType());
		n.setLong("timeSinceLastEgg", this.ticksSinceLastEgg);
		super.writeEntityToNBT(n);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound n) {
		this.ticksExisted = n.getInteger("ticks");
		setType(n.getByte("spiderType"));
		this.ticksSinceLastEgg = n.getLong("timeSinceLastEgg");
		super.readEntityFromNBT(n);
	}
	
	public byte getType() {
		return this.getDataManager().get(TYPE);
	}

	public void setType(byte b) {
		this.getDataManager().set(TYPE, b);
	}


	public void setInWeb() {
	}
	
	public static class Type {
		public static final byte ADULT = 0;
		public static final byte MOTHER = 1;
		public static final byte CHILD = 2;
	}
}
