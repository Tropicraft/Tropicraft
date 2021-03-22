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

    private static final DataParameter<Byte> DATA_FISH_TYPE = EntityDataManager.createKey(TropicraftTropicalFishEntity.class, DataSerializers.BYTE);

    public TropicraftTropicalFishEntity(EntityType<? extends AbstractGroupFishEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return AbstractGroupFishEntity.func_234176_m_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 5.0);
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(DATA_FISH_TYPE, (byte) 0);
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT nbt) {
        entityData = super.onInitialSpawn(world, difficultyInstance, spawnReason, entityData, nbt);
        if (nbt != null && nbt.contains("BucketVariantTag", 3)) {
            setFishType(FishType.getById(nbt.getInt("BucketVariantTag")));
        } else {
            setFishType(FishType.getRandomType(rand));
        }
        return entityData;
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
    protected SoundEvent getHurtSound(DamageSource dmgSrc) {
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

    @Override
    protected ActionResultType getEntityInteractionResult(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!stack.isEmpty() && stack.getItem() == TropicraftItems.FISHING_NET.get()) {
            final int firstHotbarSlot = 0;
            int bucketSlot = -1;
            for (int i = 0; i < PlayerInventory.getHotbarSize(); i++) {
                ItemStack s = player.inventory.getStackInSlot(firstHotbarSlot + i);
                if (isFishHolder(s)) {
                    bucketSlot = firstHotbarSlot + i;
                    break;
                }
            }

            if (bucketSlot == -1 && isFishHolder(player.getHeldItemOffhand())) {
                bucketSlot = 36;
            }

            if (bucketSlot >= 0) {
                ItemStack fishHolder = player.inventory.getStackInSlot(bucketSlot);
                if (fishHolder.getItem() == Items.WATER_BUCKET) {
                    fishHolder = new ItemStack(TropicraftItems.TROPICAL_FISH_BUCKET.get());
                    player.inventory.setInventorySlotContents(bucketSlot, fishHolder);
                }
                setBucketData(fishHolder);
                player.swingArm(hand);
                world.playSound(player, getPosition(), SoundEvents.ENTITY_GENERIC_SWIM, SoundCategory.PLAYERS, 0.25f, 1f + (rand.nextFloat() * 0.4f));
                remove();
                return ActionResultType.SUCCESS;
            }
        }

        return super.getEntityInteractionResult(player, hand);
    }

    @Override
    public void writeAdditional(final CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("FishType", getFishType().id);
    }

    @Override
    public void readAdditional(final CompoundNBT compound) {
        super.readAdditional(compound);
        setFishType(FishType.getById(compound.getInt("FishType")));
    }

    /**
     * Add extra data to the bucket that just picked this fish up
     */
    @Override
    protected void setBucketData(final ItemStack bucket) {
        super.setBucketData(bucket);
        CompoundNBT compoundnbt = bucket.getOrCreateTag();
        compoundnbt.putInt("BucketVariantTag", getFishType().id);
    }

    private boolean isFishHolder(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() == Items.WATER_BUCKET || stack.getItem() == TropicraftItems.TROPICAL_FISH_BUCKET.get());
    }
}
