package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.fish.AbstractGroupFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.Random;

public class TropicraftTropicalFishEntity extends AbstractGroupFishEntity implements IAtlasFish {

    enum FishType {
        CLOWNFISH(0),
        QUEEN_ANGELFISH(1),
        YELLOW_TANG(2),
        BUTTERFLY_FISH(3),
        GEOPHAGUS_SURINAMENSIS(4),
        BETTA_FISH(5),
        REGAL_TANG(6),
        ROYAL_GAMMA(7);

        public static final FishType[] VALUES = values();
        private final int id;

        FishType(int id) {
            this.id = id;
        }

        private static FishType getById(final int id) {
            for (final FishType type : VALUES) {
                if (type.id == id) {
                    return type;
                }
            }
            return CLOWNFISH;
        }

        private static FishType getRandomType(final Random rand) {
            return VALUES[rand.nextInt(FishType.values().length)];
        }
    }

    private static final DataParameter<Byte> DATA_FISH_TYPE = EntityDataManager.defineId(TropicraftTropicalFishEntity.class, DataSerializers.BYTE);

    public TropicraftTropicalFishEntity(EntityType<? extends AbstractGroupFishEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return AbstractGroupFishEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 5.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_FISH_TYPE, (byte) 0);
    }

    @Override
    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT nbt) {
        entityData = super.finalizeSpawn(world, difficultyInstance, spawnReason, entityData, nbt);
        if (nbt != null && nbt.contains("BucketVariantTag", 3)) {
            setFishType(FishType.getById(nbt.getInt("BucketVariantTag")));
        } else {
            setFishType(FishType.getRandomType(random));
        }
        return entityData;
    }

    public FishType getFishType() {
        return FishType.VALUES[entityData.get(DATA_FISH_TYPE)];
    }

    public void setFishType(final FishType type) {
        entityData.set(DATA_FISH_TYPE, (byte) type.ordinal());
    }

    @Override
    public int getMaxSchoolSize() {
        return 24;
    }

    @Override
    protected ItemStack getBucketItemStack() {
        return new ItemStack(TropicraftItems.TROPICAL_FISH_BUCKET.get());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource dmgSrc) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }

    @Override
    public int getAtlasSlot() {
        return getFishType().ordinal();
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.TROPICAL_FISH_SPAWN_EGG.get());
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && stack.getItem() == TropicraftItems.FISHING_NET.get()) {
            final int firstHotbarSlot = 0;
            int bucketSlot = -1;
            for (int i = 0; i < PlayerInventory.getSelectionSize(); i++) {
                ItemStack s = player.inventory.getItem(firstHotbarSlot + i);
                if (isFishHolder(s)) {
                    bucketSlot = firstHotbarSlot + i;
                    break;
                }
            }

            if (bucketSlot == -1 && isFishHolder(player.getOffhandItem())) {
                bucketSlot = 36;
            }

            if (bucketSlot >= 0) {
                ItemStack fishHolder = player.inventory.getItem(bucketSlot);
                if (fishHolder.getItem() == Items.WATER_BUCKET) {
                    fishHolder = new ItemStack(TropicraftItems.TROPICAL_FISH_BUCKET.get());
                    player.inventory.setItem(bucketSlot, fishHolder);
                }
                saveToBucketTag(fishHolder);
                player.swing(hand);
                level.playSound(player, blockPosition(), SoundEvents.GENERIC_SWIM, SoundCategory.PLAYERS, 0.25f, 1f + (random.nextFloat() * 0.4f));
                remove();
                return ActionResultType.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(final CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("FishType", getFishType().id);
    }

    @Override
    public void readAdditionalSaveData(final CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        setFishType(FishType.getById(compound.getInt("FishType")));
    }

    /**
     * Add extra data to the bucket that just picked this fish up
     */
    @Override
    protected void saveToBucketTag(final ItemStack bucket) {
        super.saveToBucketTag(bucket);
        CompoundNBT compoundnbt = bucket.getOrCreateTag();
        compoundnbt.putInt("BucketVariantTag", getFishType().id);
    }

    private boolean isFishHolder(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() == Items.WATER_BUCKET || stack.getItem() == TropicraftItems.TROPICAL_FISH_BUCKET.get());
    }
}
