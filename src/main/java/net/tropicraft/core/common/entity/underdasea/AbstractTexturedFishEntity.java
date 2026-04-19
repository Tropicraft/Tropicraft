package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficultyInstance, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        setTexture(getRandomTexture());
        return super.finalizeSpawn(world, difficultyInstance, spawnReason, entityData);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putString("Texture", getTexture());
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setTexture(input.getStringOr("Texture", getDefaultTexture()));
    }

    public void setTexture(String textureName) {
        getEntityData().set(TEXTURE_NAME, textureName);
    }

    public String getTexture() {
        return getEntityData().get(TEXTURE_NAME);
    }
}
