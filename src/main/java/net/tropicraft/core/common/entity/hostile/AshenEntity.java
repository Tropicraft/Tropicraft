package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.datafix.fixes.TippedArrow;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.ai.ashen.AIAshenChaseAndPickupLostMask;
import net.tropicraft.core.common.entity.ai.ashen.EntityAIMeleeAndRangedAttack;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.entity.placeable.AshenMaskEntity;
import net.tropicraft.core.common.item.AshenMaskItem;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class AshenEntity extends CreatureEntity implements IRangedAttackMob {

    public enum AshenState {
        PEACEFUL,
        LOST_MASK,
        HOSTILE;

        public static final AshenState[] VALUES = values();
    }

    private static final DataParameter<Byte> MASK_TYPE = EntityDataManager.createKey(AshenEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> ACTION_STATE = EntityDataManager.createKey(AshenEntity.class, DataSerializers.BYTE);

    // TODO add once dartgun is back
    //private final AIAshenShootDart aiArrowAttack = new AIAshenShootDart(this);

    public AshenMaskEntity maskToTrack;

    public boolean hasGTFO;

    public AshenEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
        setActionState(AshenState.HOSTILE);
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        setHeldItem(Hand.MAIN_HAND, new ItemStack(TropicraftItems.DAGGER.get()));
        // blowgun
        //this.setHeldItem(Hand.OFF_HAND, new ItemStack(TropicraftItems.DAGGER.get()));
        return super.onInitialSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
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
        //goalSelector.addGoal(3, aiArrowAttack);
        goalSelector.addGoal(4, new RandomWalkingGoal(this, 1.0D));
        goalSelector.addGoal(5, new EntityAIMeleeAndRangedAttack(this, 1.0D, 20*2, 20*10, 5F));
        goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        goalSelector.addGoal(7, new LookRandomlyGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // TODO: Change predicate in last parameter below?
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EntityKoaBase.class, true));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();

        getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3);
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
    public void attackEntityWithRangedAttack(LivingEntity target, float v) {
        ItemStack headGear = target.getItemStackFromSlot(EquipmentSlotType.HEAD);
        if (headGear.getItem() instanceof AshenMaskItem) {
            return;
        }

        ArrowEntity tippedArrow = new ArrowEntity(EntityType.ARROW, world);
        double d0 = target.posX - posX;
        double d1 = target.getBoundingBox().minY + (double)(target.getHeight() / 3.0F) - tippedArrow.posY;
        double d2 = target.posZ - posZ;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        tippedArrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - world.getDifficulty().getId() * 4));

        tippedArrow.setDamage(1);
        tippedArrow.setKnockbackStrength(0);

        ItemStack itemstack = PotionUtils.addPotionToItemStack(new ItemStack(Items.TIPPED_ARROW), Potion.getPotionTypeForName("slowness"));
        tippedArrow.setPotionEffect(itemstack);

        tippedArrow.addEffect(new EffectInstance(Effects.SLOWNESS, 100, 10));

        playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
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
        maskToTrack.setPositionAndRotation(posX, posY, posZ, rotationYaw, 0);
        world.addEntity(maskToTrack);
    }

    public void pickupMask(AshenMaskEntity mask) {
        setActionState(AshenState.HOSTILE);
        maskToTrack = null;
        setMaskType(mask.getMaskType());
        mask.remove();
    }

}
