package net.tropicraft.core.common.entity.hostile;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.ai.EntityAIWanderNotLazy;
import net.tropicraft.core.common.entity.egg.TropiSpiderEggEntity;
import net.tropicraft.core.common.item.TropicraftItems;

public class TropiSpiderEntity extends SpiderEntity {

	public enum Type {
		ADULT, MOTHER, CHILD;

		private static final Type[] VALUES = values();
	}

	private static final DataParameter<Byte> TYPE = EntityDataManager.<Byte>createKey(TropiSpiderEntity.class, DataSerializers.BYTE);
	private static final int SPIDER_MATURE_AGE = 20 * 60 * 10; // From child to adult in 10 real minutes
	private static final int SPIDER_MAX_EGGS = 10;
	private static final long SPIDER_MIN_EGG_DELAY = 12000; // Once per half minecraft day minimum
	private static final int SPIDER_EGG_CHANCE = 1000;

	private BlockPos nestSite;
	private TropiSpiderEntity mother = null;
	private long ticksSinceLastEgg = 0L;
	public byte initialType = 0;

	public TropiSpiderEntity(final EntityType<? extends SpiderEntity> type, World world) {
		super(type, world);
		ticksExisted = SPIDER_MATURE_AGE;
		ticksSinceLastEgg = ticksExisted;
	}

	public static TropiSpiderEntity haveBaby(final TropiSpiderEntity mother) {
		final TropiSpiderEntity baby = new TropiSpiderEntity(TropicraftEntities.TROPI_SPIDER.get(), mother.world);
		baby.setSpiderType(Type.CHILD);
		baby.ticksExisted = 0;
		baby.mother = mother;
		return baby;
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(TYPE, initialType);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new SwimGoal(this));
		goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.8D, false));
		goalSelector.addGoal(7, new EntityAIWanderNotLazy(this, 0.8D, 40));
		goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		goalSelector.addGoal(8, new LookRandomlyGoal(this));
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		if (damageSrc.getTrueSource() != null && damageSrc.getTrueSource() instanceof LivingEntity) {
			setAttackTarget((LivingEntity) damageSrc.getTrueSource());
		}
		super.damageEntity(damageSrc, damageAmount);
	}

	@Override
    public boolean isOnLadder() {
		return isBesideClimbableBlock() && getNavigator().noPath();
	}

	@Override
    public boolean isBesideClimbableBlock() {
		return collidedHorizontally;
	}

	@Override
	public void tick() {
		fallDistance = 0;
		// TODO port to new getSize() method
//		if (this.getType() == Type.CHILD) {
//			this.setSize(0.9F, 0.7F);
//		} else {
//			this.setSize(1.4F, 0.9F);
//		}
		super.tick();
		LivingEntity attackTarget = getAttackTarget();
		if (attackTarget != null) {
			if (getDistanceSq(attackTarget) < 128D) {
				Util.tryMoveToEntityLivingLongDist(this, attackTarget, 0.8f);
			}
		}
		if (!world.isRemote && attackTarget != null && onGround && rand.nextInt(3) == 0 && attackTarget.getDistance(this) < 5) {
			getNavigator().clearPath();
			jump();
			jumpMovementFactor = 0.3f;
		} else {
			jumpMovementFactor = 0.2f;
		}
		if (!world.isRemote) {
			if (getSpiderType() == Type.CHILD) {
				if (ticksExisted >= SPIDER_MATURE_AGE) {
					setSpiderType(Type.ADULT);
				}
				if (mother != null) {
					if (getDistanceSq(mother) > 16D) {
						Util.tryMoveToEntityLivingLongDist(this, mother, 0.8f);
					} else {
						getNavigator().clearPath();
					}
					if (mother.getAttackTarget() != null) {
						setAttackTarget(mother.getAttackTarget());
					}
				}
			}

			if (getSpiderType() == Type.ADULT) {
				if (mother != null) {
					if (!mother.isAlive()) {
						mother = null;
						getNavigator().clearPath();
						setAttackTarget(null);
					}
					// issues much?
					setAttackTarget(this.mother);
				}
				if (rand.nextInt(SPIDER_EGG_CHANCE) == 0 && this.ticksSinceLastEgg > SPIDER_MIN_EGG_DELAY && this.ticksExisted % 80 == 0) {
					buildNest();
				}
			}

			if (getSpiderType() == Type.MOTHER) {
				if (nestSite != null) {
					if (ticksSinceLastEgg < 2000) {
						if (!getPosition().withinDistance(nestSite, 16)) {
							Util.tryMoveToXYZLongDist(this, nestSite, 0.9f);
						}
					} else {
						nestSite = null;
					}
				}
			}
			ticksSinceLastEgg++;
		}
	}
	
	
	@Override
	protected SoundEvent getAmbientSound() {
		return rand.nextInt(20) == 0 ? super.getAmbientSound() : null;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockState) {
		if (getSpiderType() == Type.CHILD) {
			if (rand.nextInt(20) == 0) {
				super.playStepSound(pos, blockState);
			}
		} else {
			super.playStepSound(pos, blockState);
		}
	}

	@Override
	public boolean canBePushed() {
		return getSpiderType() != Type.MOTHER;
	}

	public void buildNest() {
		if (!world.isRemote) {
			setSpiderType(Type.MOTHER);
			int r = rand.nextInt(SPIDER_MAX_EGGS) + 1;
			
			if (r < 2) {
				return;
			}
			
			for (int i = 0; i < r; i++) {
				TropiSpiderEggEntity egg = TropicraftEntities.TROPI_SPIDER_EGG.get().create(world);
				egg.setMotherId(getUniqueID());
				egg.setPosition(getPosition().getX() + rand.nextFloat(), getPosition().getY(), getPosition().getZ() + rand.nextFloat());
				world.addEntity(egg);
				ticksSinceLastEgg = 0;
			}
			
			for (int x = 0; x < 5; x++) {
				for (int z = 0; z < 5; z++) {
					if (rand.nextInt(8) == 0) {
						BlockPos pos = new BlockPos(getPosition().getX() - 2 + x, getPosition().getY(),
								getPosition().getZ() - 2 + z);
						if (world.getBlockState(pos).getBlock().equals(Blocks.AIR) && world.getBlockState(pos.down()).getMaterial().isSolid()) {
							world.setBlockState(pos, Blocks.COBWEB.getDefaultState());
						}
					}
				}
			}
			nestSite = getPosition();
		}
	}

	@Override
	public void writeAdditional(CompoundNBT n) {
		n.putInt("ticks", ticksExisted);
		n.putByte("spiderType", (byte) getSpiderType().ordinal());
		n.putLong("timeSinceLastEgg", ticksSinceLastEgg);
		super.writeAdditional(n);
	}

	@Override
	public void readAdditional(CompoundNBT n) {
		ticksExisted = n.getInt("ticks");
		setSpiderType(n.getByte("spiderType"));
		ticksSinceLastEgg = n.getLong("timeSinceLastEgg");
		super.readAdditional(n);
	}
	
	public Type getSpiderType() {
		return Type.VALUES[getDataManager().get(TYPE)];
	}

	public void setSpiderType(Type type) {
		getDataManager().set(TYPE, (byte) type.ordinal());
		recalculateSize();
	}

	public void setSpiderType(byte b) {
		getDataManager().set(TYPE, b);
		recalculateSize();
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(TropicraftItems.TROPI_SPIDER_SPAWN_EGG.get());
	}
}
