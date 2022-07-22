package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class MarlinEntity extends AbstractFish {

    private static final EntityDataAccessor<String> TEXTURE_NAME = SynchedEntityData.defineId(MarlinEntity.class, EntityDataSerializers.STRING);

    public MarlinEntity(EntityType<? extends AbstractFish> type, Level world) {
        super(type, world);
        xpReward = 5;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(TEXTURE_NAME, "marlin");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractFish.createAttributes()
                .add(Attributes.MAX_HEALTH, 5.0);
    }
    
    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficultyInstance, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag nbt) {
        setTexture(random.nextInt(50) == 0 ? "purple_marlin" : "marlin");
        return super.finalizeSpawn(world, difficultyInstance, spawnReason, entityData, nbt);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Texture", getTexture());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        setTexture(nbt.getString("Texture"));
    }

    @Override
    public ItemStack getBucketItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }

    public void setTexture(final String textureName) {
        getEntityData().set(TEXTURE_NAME, textureName);
    }

    public String getTexture() {
        return getEntityData().get(TEXTURE_NAME);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.MARLIN_SPAWN_EGG.get());
    }
}
