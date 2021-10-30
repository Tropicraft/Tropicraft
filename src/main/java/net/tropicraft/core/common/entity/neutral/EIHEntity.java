package net.tropicraft.core.common.entity.neutral;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.entity.hostile.TropicraftCreatureEntity;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.sound.Sounds;

import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class EIHEntity extends TropicraftCreatureEntity {

    private static final EntityDataAccessor<Byte> STATE = SynchedEntityData.defineId(EIHEntity.class, EntityDataSerializers.BYTE);
    public int FLAG_SLEEP = 1 << 0;
    public int FLAG_AWARE = 1 << 1;
    public int FLAG_ANGRY = 1 << 2;

    public EIHEntity(EntityType<? extends PathfinderMob> type, Level world) {
        super(type, world);
        xpReward = 10;
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.EIH_SPAWN_EGG.get());
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(STATE, (byte) 0);
    }

    public byte getState() {
        return getEntityData().get(STATE);
    }

    private void setState(final byte state) {
        getEntityData().set(STATE, state);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100.0)
                .add(Attributes.ATTACK_DAMAGE, 7.0);
    }

    @Override
    public void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false) {
            @Override
            public boolean canUse() {
                if (!isAngry()) return false;
                return super.canUse();
            }
        });

        LeapAtTargetGoal leap = new LeapAtTargetGoal(this, 0.4F);
        //leap.setMutexFlags();
        goalSelector.addGoal(3, leap);

        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D) {
            @Override
            public boolean canUse() {
                if (!isAngry()) return false;
                return super.canUse();
            }
        });


        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
//        goalSelector.addGoal(8, new LookRandomlyGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new TargetAggressorGoal(this));
    }

    @Override
    public void addAdditionalSaveData(final CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("State", getState());
    }

    @Override
    public void readAdditionalSaveData(final CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setState(compound.getByte("State"));
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (isAsleep()) {
            setDeltaMovement(Vec3.ZERO);
        }

        if (!isAsleep()) {
            yRotO = yRot;
            xRotO = xRot;
        }

        if (tickCount % 20 == 0) {
            final LivingEntity attackTarget = getTarget();
            if (attackTarget == null) {
                final Player closestPlayer = level.getNearestPlayer(this, 10);
                if (closestPlayer != null && !closestPlayer.abilities.instabuild && !closestPlayer.isSpectator()) {
                    setTarget(closestPlayer);
                }
            } else if (distanceTo(attackTarget) > 16) {
                setTarget(null);
                setAwake(false);
                setImmobile(true);
                setAngry(false);
            }

            if (attackTarget != null && !isPathFinding() && !isAngry()) {
                if (attackTarget instanceof Player) {
                    final Player player = (Player) attackTarget;
                    if (!player.abilities.instabuild && !player.isSpectator()) {
                        final float distance = distanceTo(player);
                        if (distance < 10F) {
                            setAwake(true);
                            final ItemStack itemstack = player.inventory.getSelected();

                            if (!itemstack.isEmpty()) {
                                if (isAware() && itemstack.getItem() == TropicraftBlocks.CHUNK.get().asItem()) {
                                    setAngry(true);
                                    setImmobile(false);
                                }
                            }
                        }

                        if (distanceTo(player) < 3 && level.getDifficulty() != Difficulty.PEACEFUL) {
                            setAwake(false);
                            setImmobile(false);
                            setAngry(true);
                        }
                    } else {
                        setImmobile(true);
                        setAngry(false);
                        setAwake(false);
                        setDeltaMovement(0D, -.1D, 0D);
                        setRot(yRotO, xRotO);
                    }
                }
            }

            if (isAsleep()) {
                setRot(yRotO, xRotO);
            } else {
                setAwake(false);
            }
        }
    }

    public boolean isAngry() {
        return getEIHFlag(FLAG_ANGRY);
    }

    public void setAngry(final boolean angry) {
        setEIHFlag(FLAG_ANGRY, angry);
    }

    public boolean isAware() {
        return getEIHFlag(FLAG_AWARE);
    }

    public void setAwake(final boolean aware) {
        setEIHFlag(FLAG_AWARE, aware);
    }

    public boolean isAsleep() {
        return getEIHFlag(FLAG_SLEEP);
    }

    public void setImmobile(final boolean asleep) {
        setEIHFlag(FLAG_SLEEP, asleep);
    }

    public void setEIHFlag(int id, boolean flag) {
        if (flag) {
            this.entityData.set(STATE, (byte)(this.entityData.get(STATE) | id));
        } else {
            this.entityData.set(STATE, (byte)(this.entityData.get(STATE) & ~id));
        }
    }

    private boolean getEIHFlag(int id) {
        return (this.entityData.get(STATE) & id) != 0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (isAware()) {
            return random.nextInt(10) == 0 ? Sounds.HEAD_MED : null;
        } else if (isAngry()) {
            return random.nextInt(10) == 0 ? Sounds.HEAD_SHORT : null;
        } else {
            return null;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return Sounds.HEAD_PAIN;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return Sounds.HEAD_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public boolean hurt(final DamageSource source, final float amount) {
        if (source.equals(DamageSource.OUT_OF_WORLD)) {
            return super.hurt(source, amount);
        }

        if (source.getDirectEntity() instanceof Player) {
            Player player = (Player) source.getDirectEntity();
            if (player.abilities.instabuild || player.isSpectator()) {
                return super.hurt(source, amount);
            }
            ItemStack heldItem = player.getMainHandItem();
            if (!heldItem.isEmpty() && heldItem.getItem().getHarvestLevel(heldItem, ToolType.PICKAXE, player, null) >= 1) {
                return super.hurt(source, amount);
            } else {
                playSound(Sounds.HEAD_LAUGHING, getSoundVolume(), getVoicePitch());
                setLastHurtByMob(player);
            }

            setAngry(true);
            setImmobile(false);
        }

        return true;
    }

    private static class TargetAggressorGoal extends NearestAttackableTargetGoal<Player> {
        public TargetAggressorGoal(EIHEntity eih) {
            super(eih, Player.class, true);
        }

        public boolean canUse() {
            return ((EIHEntity) mob).isAngry() && super.canUse();
        }
    }
}
