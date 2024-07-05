package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public abstract class AbstractTexturedFishEntity extends AbstractFish {
    private static final EntityDataAccessor<String> TEXTURE_NAME = SynchedEntityData.defineId(AbstractTexturedFishEntity.class, EntityDataSerializers.STRING);

    public AbstractTexturedFishEntity(EntityType<? extends AbstractFish> type, Level world) {
        super(type, world);
    }

    abstract String getRandomTexture();

    abstract String getDefaultTexture();

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TEXTURE_NAME, getDefaultTexture());
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficultyInstance, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        setTexture(getRandomTexture());
        return super.finalizeSpawn(world, difficultyInstance, spawnReason, entityData);
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

    public void setTexture(final String textureName) {
        getEntityData().set(TEXTURE_NAME, textureName);
    }

    public String getTexture() {
        return getEntityData().get(TEXTURE_NAME);
    }
}
