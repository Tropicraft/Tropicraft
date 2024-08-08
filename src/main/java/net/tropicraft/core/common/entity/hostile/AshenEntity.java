package net.tropicraft.core.common.entity.hostile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.ai.ashen.AIAshenChaseAndPickupLostMask;
import net.tropicraft.core.common.entity.ai.ashen.AIAshenShootDart;
import net.tropicraft.core.common.entity.ai.ashen.EntityAIMeleeAndRangedAttack;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.entity.placeable.AshenMaskEntity;
import net.tropicraft.core.common.item.AshenMaskItem;
import net.tropicraft.core.common.item.AshenMasks;
import net.tropicraft.core.common.item.BlowGunItem;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class AshenEntity extends PathfinderMob implements RangedAttackMob {

    public enum AshenState {
        PEACEFUL,
        LOST_MASK,
        HOSTILE;

        public static final AshenState[] VALUES = values();
    }

    private static final EntityDataAccessor<Byte> MASK_TYPE = SynchedEntityData.defineId(AshenEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> ACTION_STATE = SynchedEntityData.defineId(AshenEntity.class, EntityDataSerializers.BYTE);

    @Nullable
    public AshenMaskEntity maskToTrack;

    public AshenEntity(EntityType<? extends PathfinderMob> type, Level world) {
        super((EntityType<? extends AshenEntity>) type, world);
        setActionState(AshenState.HOSTILE);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        setItemInHand(InteractionHand.OFF_HAND, new ItemStack(TropicraftItems.BLOW_GUN.get()));
        setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TropicraftItems.DAGGER.get()));
        setMaskType((byte) AshenMasks.VALUES[world.getRandom().nextInt(AshenMasks.VALUES.length)].ordinal());
        setActionState(AshenState.HOSTILE);
        return super.finalizeSpawn(world, difficulty, reason, spawnData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MASK_TYPE, (byte) 0);
        builder.define(ACTION_STATE, (byte) AshenState.HOSTILE.ordinal());
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new AIAshenChaseAndPickupLostMask(this, 1.0));
        goalSelector.addGoal(3, new AIAshenShootDart(this));
        goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0));
        goalSelector.addGoal(5, new EntityAIMeleeAndRangedAttack(this, 1.0, 20 * 2, 20 * 10, 5.0f));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // TODO: Change predicate in last parameter below?
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EntityKoaBase.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    public boolean hasMask() {
        return getActionState() != AshenState.LOST_MASK;
    }

    public void setMaskType(byte type) {
        getEntityData().set(MASK_TYPE, type);
    }

    public byte getMaskType() {
        return getEntityData().get(MASK_TYPE);
    }

    public void setActionState(AshenState state) {
        getEntityData().set(ACTION_STATE, (byte) state.ordinal());
    }

    public AshenState getActionState() {
        return AshenState.VALUES[getActionStateValue()];
    }

    private byte getActionStateValue() {
        return getEntityData().get(ACTION_STATE);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        ItemStack headGear = target.getItemBySlot(EquipmentSlot.HEAD);
        // Don't shoot things wearing ashen masks
        if (headGear.getItem() instanceof AshenMaskItem) {
            return;
        }

        Arrow tippedArrow = BlowGunItem.createArrow(level(), this, BlowGunItem.getProjectile(), new ItemStack(TropicraftItems.BLOW_GUN.get()));
        double d0 = target.getX() - getX();
        double d1 = target.getBoundingBox().minY + (double) (target.getBbHeight() / 3.0f) - tippedArrow.getY();
        double d2 = target.getZ() - getZ();
        double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
        tippedArrow.shoot(d0, d1 + d3 * 0.20000000298023224, d2, 1.6f, velocity);

        tippedArrow.setBaseDamage(1);

        playSound(SoundEvents.CROSSBOW_SHOOT, 1.0f, 1.0f / (getRandom().nextFloat() * 0.4f + 0.8f));
        level().addFreshEntity(tippedArrow);
    }

    @Override
    public boolean hurt(DamageSource source, float amt) {
        boolean wasHit = super.hurt(source, amt);

        if (!level().isClientSide) {
            if (hasMask() && wasHit && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                dropMask();
            }
        }

        return wasHit;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putByte("MaskType", getMaskType());
        nbt.putByte("ActionState", getActionStateValue());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        setMaskType(nbt.getByte("MaskType"));
        setActionState(AshenState.VALUES[nbt.getByte("ActionState")]);
    }

    public void dropMask() {
        setActionState(AshenState.LOST_MASK);
        maskToTrack = new AshenMaskEntity(TropicraftEntities.ASHEN_MASK.get(), level());
        maskToTrack.setMaskType(getMaskType());
        maskToTrack.absMoveTo(getX(), getY(), getZ(), getYRot(), 0);
        level().addFreshEntity(maskToTrack);
    }

    public void pickupMask(AshenMaskEntity mask) {
        setActionState(AshenState.HOSTILE);
        maskToTrack = null;
        setMaskType(mask.getMaskType());
        mask.remove(RemovalReason.DISCARDED);
    }
}
