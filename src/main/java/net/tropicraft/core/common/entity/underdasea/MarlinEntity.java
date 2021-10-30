package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class MarlinEntity extends AbstractFishEntity {

    private static final DataParameter<String> TEXTURE_NAME = EntityDataManager.defineId(MarlinEntity.class, DataSerializers.STRING);

    public MarlinEntity(EntityType<? extends AbstractFishEntity> type, World world) {
        super(type, world);
        xpReward = 5;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(TEXTURE_NAME, "marlin");
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return AbstractFishEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 5.0);
    }
    
    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        return ActionResultType.PASS;
    }

    @Override
    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT nbt) {
        setTexture(random.nextInt(50) == 0 ? "purple_marlin" : "marlin");
        return super.finalizeSpawn(world, difficultyInstance, spawnReason, entityData, nbt);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Texture", getTexture());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        setTexture(nbt.getString("Texture"));
    }

    @Override
    protected ItemStack getBucketItemStack() {
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
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.MARLIN_SPAWN_EGG.get());
    }
}
