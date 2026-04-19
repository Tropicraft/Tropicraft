package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.tropicraft.core.common.sound.Sounds;

import org.jspecify.annotations.Nullable;

public class TropicraftDolphinEntity extends Dolphin {

    private static final EntityDataAccessor<Boolean> MOUTH_OPEN = SynchedEntityData.defineId(TropicraftDolphinEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> TEXTURE_NAME = SynchedEntityData.defineId(TropicraftDolphinEntity.class, EntityDataSerializers.STRING);

    public TropicraftDolphinEntity(EntityType<? extends TropicraftDolphinEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MOUTH_OPEN, false);
        builder.define(TEXTURE_NAME, "dolphin");
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficultyInstance, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        setTexture(random.nextInt(50) == 0 ? "special_dolphin" : "dolphin");
        return super.finalizeSpawn(world, difficultyInstance, spawnReason, entityData);
    }

    public void setTexture(String textureName) {
        getEntityData().set(TEXTURE_NAME, textureName);
    }

    public String getTexture() {
        return getEntityData().get(TEXTURE_NAME);
    }

    public void setMouthOpen(boolean b) {
        getEntityData().set(MOUTH_OPEN, b);
    }

    public boolean getMouthOpen() {
        return getEntityData().get(MOUTH_OPEN);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putString("Texture", getTexture());
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setTexture(input.getStringOr("Texture", "dolphin"));
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            if (ambientSoundTime < -(getAmbientSoundInterval() - 20)) {
                if (tickCount % 3 > 1) {
                    if (!getMouthOpen()) {
                        setMouthOpen(true);
                    }
                } else if (getMouthOpen()) {
                    setMouthOpen(false);
                }
            } else if (getMouthOpen()) {
                setMouthOpen(false);
            }
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 300;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return Sounds.DOLPHIN.get();
    }
}
