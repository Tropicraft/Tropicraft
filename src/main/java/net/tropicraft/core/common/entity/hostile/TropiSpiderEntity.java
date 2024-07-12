package net.tropicraft.core.common.entity.hostile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.ai.EntityAIWanderNotLazy;
import net.tropicraft.core.common.entity.egg.TropiSpiderEggEntity;

public class TropiSpiderEntity extends Spider {

    public enum Type {
        ADULT, MOTHER, CHILD;

        private static final Type[] VALUES = values();
    }

    private static final EntityDataAccessor<Byte> TYPE = SynchedEntityData.<Byte>defineId(TropiSpiderEntity.class, EntityDataSerializers.BYTE);
    private static final int SPIDER_MATURE_AGE = 20 * 60 * 10; // From child to adult in 10 real minutes
    private static final int SPIDER_MAX_EGGS = 10;
    private static final long SPIDER_MIN_EGG_DELAY = 12000; // Once per half minecraft day minimum
    private static final int SPIDER_EGG_CHANCE = 1000;

    private BlockPos nestSite;
    private TropiSpiderEntity mother = null;
    private long ticksSinceLastEgg = 0L;
    public byte initialType = 0;

    private boolean jumping;

    public TropiSpiderEntity(final EntityType<? extends Spider> type, Level world) {
        super(type, world);
        tickCount = SPIDER_MATURE_AGE;
        ticksSinceLastEgg = tickCount;
    }

    public static TropiSpiderEntity haveBaby(final TropiSpiderEntity mother) {
        final TropiSpiderEntity baby = new TropiSpiderEntity(TropicraftEntities.TROPI_SPIDER.get(), mother.level());
        baby.setSpiderType(Type.CHILD);
        baby.tickCount = 0;
        baby.mother = mother;
        return baby;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TYPE, initialType);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.8D, false));
        goalSelector.addGoal(7, new EntityAIWanderNotLazy(this, 0.8D, 40));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        if (damageSrc.getEntity() != null && damageSrc.getEntity() instanceof LivingEntity) {
            setTarget((LivingEntity) damageSrc.getEntity());
        }
        super.actuallyHurt(damageSrc, damageAmount);
    }

    @Override
    public boolean onClimbable() {
        return isClimbing() && getNavigation().isDone();
    }

    @Override
    public boolean isClimbing() {
        return horizontalCollision;
    }

    @Override
    protected float getFlyingSpeed() {
        return jumping ? 0.3f : 0.2f;
    }

    @Override
    public void tick() {
        fallDistance = 0;
        // TODO port to new getSize() method
//        if (this.getType() == Type.CHILD) {
//            this.setSize(0.9F, 0.7F);
//        } else {
//            this.setSize(1.4F, 0.9F);
//        }
        super.tick();
        LivingEntity attackTarget = getTarget();
        if (attackTarget != null) {
            if (distanceToSqr(attackTarget) < 128D) {
                Util.tryMoveToEntityLivingLongDist(this, attackTarget, 0.8f);
            }
        }
        if (!level().isClientSide && attackTarget != null && onGround() && random.nextInt(3) == 0 && attackTarget.distanceTo(this) < 5) {
            getNavigation().stop();
            jumpFromGround();
            jumping = true;
        } else {
            jumping = false;
        }
        if (!level().isClientSide) {
            if (getSpiderType() == Type.CHILD) {
                if (tickCount >= SPIDER_MATURE_AGE) {
                    setSpiderType(Type.ADULT);
                }
                if (mother != null) {
                    if (distanceToSqr(mother) > 16D) {
                        Util.tryMoveToEntityLivingLongDist(this, mother, 0.8f);
                    } else {
                        getNavigation().stop();
                    }
                    if (mother.getTarget() != null) {
                        setTarget(mother.getTarget());
                    }
                }
            }

            if (getSpiderType() == Type.ADULT) {
                if (mother != null) {
                    if (!mother.isAlive()) {
                        mother = null;
                        getNavigation().stop();
                        setTarget(null);
                    }
                    // issues much?
                    setTarget(this.mother);
                }
                if (random.nextInt(SPIDER_EGG_CHANCE) == 0 && this.ticksSinceLastEgg > SPIDER_MIN_EGG_DELAY && this.tickCount % 80 == 0) {
                    buildNest();
                }
            }

            if (getSpiderType() == Type.MOTHER) {
                if (nestSite != null) {
                    if (ticksSinceLastEgg < 2000) {
                        if (!blockPosition().closerThan(nestSite, 16)) {
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
        return random.nextInt(20) == 0 ? super.getAmbientSound() : null;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        if (getSpiderType() == Type.CHILD) {
            if (random.nextInt(20) == 0) {
                super.playStepSound(pos, blockState);
            }
        } else {
            super.playStepSound(pos, blockState);
        }
    }

    @Override
    public boolean isPushable() {
        return getSpiderType() != Type.MOTHER;
    }

    public void buildNest() {
        if (!level().isClientSide) {
            setSpiderType(Type.MOTHER);
            int r = random.nextInt(SPIDER_MAX_EGGS) + 1;

            if (r < 2) {
                return;
            }

            for (int i = 0; i < r; i++) {
                TropiSpiderEggEntity egg = TropicraftEntities.TROPI_SPIDER_EGG.get().create(level());
                egg.setMotherId(getUUID());
                egg.setPos(blockPosition().getX() + random.nextFloat(), blockPosition().getY(), blockPosition().getZ() + random.nextFloat());
                level().addFreshEntity(egg);
                ticksSinceLastEgg = 0;
            }

            for (int x = 0; x < 5; x++) {
                for (int z = 0; z < 5; z++) {
                    if (random.nextInt(8) == 0) {
                        BlockPos pos = new BlockPos(blockPosition().getX() - 2 + x, blockPosition().getY(),
                                blockPosition().getZ() - 2 + z);
                        if (level().isEmptyBlock(pos) && level().getBlockState(pos.below()).isSolid()) {
                            level().setBlockAndUpdate(pos, Blocks.COBWEB.defaultBlockState());
                        }
                    }
                }
            }
            nestSite = blockPosition();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag n) {
        n.putInt("ticks", tickCount);
        n.putByte("spiderType", (byte) getSpiderType().ordinal());
        n.putLong("timeSinceLastEgg", ticksSinceLastEgg);
        super.addAdditionalSaveData(n);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag n) {
        tickCount = n.getInt("ticks");
        setSpiderType(n.getByte("spiderType"));
        ticksSinceLastEgg = n.getLong("timeSinceLastEgg");
        super.readAdditionalSaveData(n);
    }

    public Type getSpiderType() {
        return Type.VALUES[getEntityData().get(TYPE)];
    }

    public void setSpiderType(Type type) {
        getEntityData().set(TYPE, (byte) type.ordinal());
        refreshDimensions();
    }

    public void setSpiderType(byte b) {
        getEntityData().set(TYPE, b);
        refreshDimensions();
    }
}
