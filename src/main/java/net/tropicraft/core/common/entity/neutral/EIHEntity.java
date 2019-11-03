package net.tropicraft.core.common.entity.neutral;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.sound.Sounds;

public class EIHEntity extends CreatureEntity {

    private static final DataParameter<Byte> STATE = EntityDataManager.createKey(EIHEntity.class, DataSerializers.BYTE);
    public int FLAG_SLEEP = 1 << 0;
    public int FLAG_AWARE = 1 << 1;
    public int FLAG_ANGRY = 1 << 2;

    public EIHEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
        experienceValue = 10;
    }

    @Override
    public void registerData() {
        super.registerData();
        getDataManager().register(STATE, (byte) 0);
    }

    public byte getState() {
        return getDataManager().get(STATE);
    }

    private void setState(final byte state) {
        getDataManager().set(STATE, state);
    }

    @Override
    public void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100.0D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
    }

    @Override
    public void registerGoals() {
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false) {
            @Override
            public boolean shouldExecute() {
                if (!isAngry()) return false;
                return super.shouldExecute();
            }
        });

        LeapAtTargetGoal leap = new LeapAtTargetGoal(this, 0.4F);
        //leap.setMutexFlags();
        goalSelector.addGoal(3, leap);

        goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D) {
            @Override
            public boolean shouldExecute() {
                if (!isAngry()) return false;
                return super.shouldExecute();
            }
        });


        goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
//        goalSelector.addGoal(8, new LookRandomlyGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new TargetAggressorGoal(this));
    }

    @Override
    public void writeAdditional(final CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putByte("State", getState());
    }

    @Override
    public void readAdditional(final CompoundNBT compound) {
        super.readAdditional(compound);
        setState(compound.getByte("State"));
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (isAsleep()) {
            setMotion(Vec3d.ZERO);
        }

        if (!isAsleep()) {
            prevRotationYaw = rotationYaw;
            prevRotationPitch = rotationPitch;
        }

        if (ticksExisted % 20 == 0) {
            final LivingEntity attackTarget = getAttackTarget();
            if (attackTarget == null) {
                final PlayerEntity closestPlayer = world.getClosestPlayer(this, 10);
                if (closestPlayer != null && !closestPlayer.abilities.isCreativeMode && !closestPlayer.isSpectator()) {
                    setAttackTarget(closestPlayer);
                }
            } else if (getDistance(attackTarget) > 16) {
                setAttackTarget(null);
                setAwake(false);
                setImmobile(true);
                setAngry(false);
            }

            if (attackTarget != null && !hasPath() && !isAngry()) {
                if (attackTarget instanceof PlayerEntity) {
                    final PlayerEntity player = (PlayerEntity) attackTarget;
                    if (!player.abilities.isCreativeMode && !player.isSpectator()) {
                        final float distance = getDistance(player);
                        if (distance < 10F) {
                            setAwake(true);
                            final ItemStack itemstack = player.inventory.getCurrentItem();

                            if (!itemstack.isEmpty()) {
                                if (isAware() && itemstack.getItem() == TropicraftBlocks.CHUNK.get().asItem()) {
                                    setAngry(true);
                                    setImmobile(false);
                                }
                            }
                        }

                        if (getDistance(player) < 3 && world.getDifficulty() != Difficulty.PEACEFUL) {
                            setAwake(false);
                            setImmobile(false);
                            setAngry(true);
                        }
                    } else {
                        setImmobile(true);
                        setAngry(false);
                        setAwake(false);
                        setMotion(0D, -.1D, 0D);
                        setRotation(prevRotationYaw, prevRotationPitch);
                    }
                }
            }

            if (isAsleep()) {
                setRotation(prevRotationYaw, prevRotationPitch);
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
            this.dataManager.set(STATE, (byte)(this.dataManager.get(STATE) | id));
        } else {
            this.dataManager.set(STATE, (byte)(this.dataManager.get(STATE) & ~id));
        }
    }

    private boolean getEIHFlag(int id) {
        return (this.dataManager.get(STATE) & id) != 0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (isAware()) {
            return rand.nextInt(10) == 0 ? Sounds.HEAD_MED : null;
        } else if (isAngry()) {
            return rand.nextInt(10) == 0 ? Sounds.HEAD_SHORT : null;
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
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (source.equals(DamageSource.OUT_OF_WORLD)) {
            return super.attackEntityFrom(source, amount);
        }

        if (source.getImmediateSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getImmediateSource();
            if (player.abilities.isCreativeMode || player.isSpectator()) {
                return super.attackEntityFrom(source, amount);
            }
            ItemStack heldItem = player.getHeldItemMainhand();
            if (!heldItem.isEmpty() && heldItem.getItem().getHarvestLevel(heldItem, ToolType.PICKAXE, player, null) >= 1) {
                return super.attackEntityFrom(source, amount);
            } else {
                playSound(Sounds.HEAD_LAUGHING, getSoundVolume(), getSoundPitch());
                setRevengeTarget(player);
            }

            setAngry(true);
            setImmobile(false);
        }

        return true;
    }

    private static class TargetAggressorGoal extends NearestAttackableTargetGoal<PlayerEntity> {
        public TargetAggressorGoal(EIHEntity eih) {
            super(eih, PlayerEntity.class, true);
        }

        public boolean shouldExecute() {
            return ((EIHEntity) goalOwner).isAngry() && super.shouldExecute();
        }
    }
}
