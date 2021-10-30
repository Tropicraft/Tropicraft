package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.sound.Sounds;

import javax.annotation.Nullable;

public class TropicraftDolphinEntity extends Dolphin {

    private static final EntityDataAccessor<Boolean> MOUTH_OPEN = SynchedEntityData.defineId(TropicraftDolphinEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> TEXTURE_NAME = SynchedEntityData.defineId(TropicraftDolphinEntity.class, EntityDataSerializers.STRING);

    public TropicraftDolphinEntity(EntityType<? extends TropicraftDolphinEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(MOUTH_OPEN, false);
        entityData.define(TEXTURE_NAME, "dolphin");
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficultyInstance, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag nbt) {
        setTexture(random.nextInt(50) == 0 ? "special_dolphin" : "dolphin");
        return super.finalizeSpawn(world, difficultyInstance, spawnReason, entityData, nbt);
    }

    public void setTexture(final String textureName) {
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
    public void tick() {
        super.tick();
        if (!level.isClientSide) {
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
        return Sounds.DOLPHIN;
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.DOLPHIN_SPAWN_EGG.get());
    }
}
