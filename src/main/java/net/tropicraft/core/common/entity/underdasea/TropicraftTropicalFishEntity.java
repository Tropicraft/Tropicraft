package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class TropicraftTropicalFishEntity extends AbstractSchoolingFish implements IAtlasFish {

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

        private static FishType getRandomType(final RandomSource rand) {
            return Util.getRandom(VALUES, rand);
        }
    }

    private static final EntityDataAccessor<Byte> DATA_FISH_TYPE = SynchedEntityData.defineId(TropicraftTropicalFishEntity.class, EntityDataSerializers.BYTE);

    public TropicraftTropicalFishEntity(EntityType<? extends AbstractSchoolingFish> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSchoolingFish.createAttributes()
                .add(Attributes.MAX_HEALTH, 5.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_FISH_TYPE, (byte) 0);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficultyInstance, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag nbt) {
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
    public ItemStack getBucketItemStack() {
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
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && stack.getItem() == TropicraftItems.FISHING_NET.get()) {
            final int firstHotbarSlot = 0;
            int bucketSlot = -1;
            for (int i = 0; i < Inventory.getSelectionSize(); i++) {
                ItemStack s = player.getInventory().getItem(firstHotbarSlot + i);
                if (isFishHolder(s)) {
                    bucketSlot = firstHotbarSlot + i;
                    break;
                }
            }

            if (bucketSlot == -1 && isFishHolder(player.getOffhandItem())) {
                bucketSlot = 36;
            }

            if (bucketSlot >= 0) {
                ItemStack fishHolder = player.getInventory().getItem(bucketSlot);
                if (fishHolder.getItem() == Items.WATER_BUCKET) {
                    fishHolder = new ItemStack(TropicraftItems.TROPICAL_FISH_BUCKET.get());
                    player.getInventory().setItem(bucketSlot, fishHolder);
                }
                saveToBucketTag(fishHolder);
                player.swing(hand);
                level().playSound(player, blockPosition(), SoundEvents.GENERIC_SWIM, SoundSource.PLAYERS, 0.25f, 1f + (random.nextFloat() * 0.4f));
                remove(RemovalReason.DISCARDED);
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(final CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("FishType", getFishType().id);
    }

    @Override
    public void readAdditionalSaveData(final CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setFishType(FishType.getById(compound.getInt("FishType")));
    }

    /**
     * Add extra data to the bucket that just picked this fish up
     */
    @Override
    public void saveToBucketTag(final ItemStack bucket) {
        super.saveToBucketTag(bucket);
        CompoundTag compoundnbt = bucket.getOrCreateTag();
        compoundnbt.putInt("BucketVariantTag", getFishType().id);
    }

    private boolean isFishHolder(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() == Items.WATER_BUCKET || stack.getItem() == TropicraftItems.TROPICAL_FISH_BUCKET.get());
    }
}
