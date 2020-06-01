package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.fish.AbstractGroupFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class TropicraftTropicalFishEntity extends AbstractGroupFishEntity implements IAtlasFish {

    enum FishType {
        CLOWNFISH,
        QUEEN_ANGELFISH,
        YELLOW_TANG,
        BUTTERFLY_FISH,
        GEOPHAGUS_SURINAMENSIS,
        BETTA_FISH,
        REGAL_TANG,
        ROYAL_GAMMA;

        public static final FishType[] VALUES = values();
    }

    private static final DataParameter<Byte> DATA_FISH_TYPE = EntityDataManager.createKey(TropicraftTropicalFishEntity.class, DataSerializers.BYTE);

    public TropicraftTropicalFishEntity(EntityType<? extends AbstractGroupFishEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(DATA_FISH_TYPE, (byte) 0);
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT nbt) {
        setFishType(FishType.values()[rand.nextInt(FishType.values().length)]);
        return super.onInitialSpawn(world, difficultyInstance, spawnReason, entityData, nbt);
    }

    public FishType getFishType() {
        return FishType.VALUES[dataManager.get(DATA_FISH_TYPE)];
    }

    public void setFishType(final FishType type) {
        dataManager.set(DATA_FISH_TYPE, (byte) type.ordinal());
    }

    @Override
    public int getMaxGroupSize() {
        return 24;
    }

    @Override
    protected ItemStack getFishBucket() {
        return new ItemStack(TropicraftItems.TROPICAL_FISH_BUCKET.get());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SALMON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SALMON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SALMON_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_SALMON_FLOP;
    }

    @Override
    public int getAtlasSlot() {
        return getFishType().ordinal();
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.TROPICAL_FISH_SPAWN_EGG.get());
    }
}
