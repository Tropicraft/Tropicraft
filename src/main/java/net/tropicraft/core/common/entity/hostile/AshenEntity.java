package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
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

public class AshenEntity extends TropicraftCreatureEntity implements IRangedAttackMob {

    public enum AshenState {
        PEACEFUL,
        LOST_MASK,
        HOSTILE;

        public static final AshenState[] VALUES = values();
    }

    private static final DataParameter<Byte> MASK_TYPE = EntityDataManager.createKey(AshenEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> ACTION_STATE = EntityDataManager.createKey(AshenEntity.class, DataSerializers.BYTE);

    public AshenMaskEntity maskToTrack;

    public AshenEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
        setActionState(AshenState.HOSTILE);
    }

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
        setHeldItem(Hand.OFF_HAND, new ItemStack(TropicraftItems.BLOW_GUN.get()));
        setHeldItem(Hand.MAIN_HAND, new ItemStack(TropicraftItems.DAGGER.get()));
        setMaskType((byte) AshenMasks.VALUES[world.getRandom().nextInt(AshenMasks.VALUES.length)].ordinal());
        setActionState(AshenState.HOSTILE);
        return super.onInitialSpawn(world, difficulty, reason, spawnData, dataTag);
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(MASK_TYPE, (byte) 0);
        getDataManager().register(ACTION_STATE, (byte) AshenState.HOSTILE.ordinal());
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, new AIAshenChaseAndPickupLostMask(this, 1.0D));
        goalSelector.addGoal(3, new AIAshenShootDart(this));
        goalSelector.addGoal(4, new RandomWalkingGoal(this, 1.0D));
        goalSelector.addGoal(5, new EntityAIMeleeAndRangedAttack(this, 1.0D, 20*2, 20*10, 5F));
        goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        goalSelector.addGoal(7, new LookRandomlyGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // TODO: Change predicate in last parameter below?
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EntityKoaBase.class, true));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return CreatureEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0);
    }

    public boolean hasMask() {
        return getActionState() != AshenState.LOST_MASK;
    }

    public void setMaskType(byte type) {
        getDataManager().set(MASK_TYPE, type);
    }

    public byte getMaskType() {
        return getDataManager().get(MASK_TYPE);
    }

    public void setActionState(final AshenState state) {
        getDataManager().set(ACTION_STATE, (byte) state.ordinal());
    }

    public AshenState getActionState() {
        return AshenState.VALUES[getActionStateValue()];
    }

    private byte getActionStateValue() {
        return getDataManager().get(ACTION_STATE);
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float velocity) {
        ItemStack headGear = target.getItemStackFromSlot(EquipmentSlotType.HEAD);
        // Don't shoot things wearing ashen masks
        if (headGear.getItem() instanceof AshenMaskItem) {
            return;
        }

        ArrowEntity tippedArrow = BlowGunItem.createArrow(world, this, BlowGunItem.getProjectile());
        double d0 = target.getPosX() - getPosX();
        double d1 = target.getBoundingBox().minY + (double)(target.getHeight() / 3.0F) - tippedArrow.getPosY();
        double d2 = target.getPosZ() - getPosZ();
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        tippedArrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, velocity);

        tippedArrow.setDamage(1);
        tippedArrow.setKnockbackStrength(0);

        playSound(SoundEvents.ITEM_CROSSBOW_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
        world.addEntity(tippedArrow);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amt) {
        boolean wasHit = super.attackEntityFrom(source, amt);

        if (!world.isRemote) {
            if (hasMask() && wasHit && !source.equals(DamageSource.OUT_OF_WORLD)) {
                dropMask();
            }
        }

        return wasHit;
    }

    @Override
    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putByte("MaskType", getMaskType());
        nbt.putByte("ActionState", getActionStateValue());
    }
    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        setMaskType(nbt.getByte("MaskType"));
        setActionState(AshenState.VALUES[nbt.getByte("ActionState")]);
    }

    public void dropMask() {
        setActionState(AshenState.LOST_MASK);
        maskToTrack = new AshenMaskEntity(TropicraftEntities.ASHEN_MASK.get(), world);
        maskToTrack.setMaskType(getMaskType());
        maskToTrack.setPositionAndRotation(getPosX(), getPosY(), getPosZ(), rotationYaw, 0);
        world.addEntity(maskToTrack);
    }

    public void pickupMask(AshenMaskEntity mask) {
        setActionState(AshenState.HOSTILE);
        maskToTrack = null;
        setMaskType(mask.getMaskType());
        mask.remove();
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.ASHEN_SPAWN_EGG.get());
    }

}
