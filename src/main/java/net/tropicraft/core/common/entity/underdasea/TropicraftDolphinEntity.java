package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.sound.Sounds;

import javax.annotation.Nullable;

public class TropicraftDolphinEntity extends DolphinEntity {

    private static final DataParameter<Boolean> MOUTH_OPEN = EntityDataManager.createKey(TropicraftDolphinEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> TEXTURE_NAME = EntityDataManager.createKey(TropicraftDolphinEntity.class, DataSerializers.STRING);

    public TropicraftDolphinEntity(EntityType<? extends TropicraftDolphinEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(MOUTH_OPEN, false);
        dataManager.register(TEXTURE_NAME, "dolphin");
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT nbt) {
        setTexture(rand.nextInt(50) == 0 ? "special_dolphin" : "dolphin");
        return super.onInitialSpawn(world, difficultyInstance, spawnReason, entityData, nbt);
    }

    public void setTexture(final String textureName) {
        getDataManager().set(TEXTURE_NAME, textureName);
    }

    public String getTexture() {
        return getDataManager().get(TEXTURE_NAME);
    }

    public void setMouthOpen(boolean b) {
        getDataManager().set(MOUTH_OPEN, b);
    }

    public boolean getMouthOpen() {
        return getDataManager().get(MOUTH_OPEN);
    }

    @Override
    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putString("Texture", getTexture());
    }

    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        setTexture(nbt.getString("Texture"));
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote) {
            if (livingSoundTime < -(getTalkInterval() - 20)) {
                if (ticksExisted % 3 > 1) {
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
    public int getTalkInterval() {
        return 300;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return Sounds.DOLPHIN;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.DOLPHIN_SPAWN_EGG.get());
    }
}
