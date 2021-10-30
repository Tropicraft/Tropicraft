package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class AbstractTexturedFishEntity extends AbstractFishEntity {
    private static final DataParameter<String> TEXTURE_NAME = EntityDataManager.defineId(AbstractTexturedFishEntity.class, DataSerializers.STRING);

    public AbstractTexturedFishEntity(EntityType<? extends AbstractFishEntity> type, World world) {
        super(type, world);
    }

    abstract String getRandomTexture();
    abstract String getDefaultTexture();

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(TEXTURE_NAME, getDefaultTexture());
    }

    @Override
    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT nbt) {
        setTexture(getRandomTexture());
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

    public void setTexture(final String textureName) {
        getEntityData().set(TEXTURE_NAME, textureName);
    }

    public String getTexture() {
        return getEntityData().get(TEXTURE_NAME);
    }
}
