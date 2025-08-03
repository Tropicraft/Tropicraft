package net.tropicraft.core.common.entity.egg;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.hostile.TropiSpiderEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class TropiSpiderEggEntity extends EggEntity {
    @Nullable
    private EntityReference<TropiSpiderEntity> mother;

    public TropiSpiderEggEntity(EntityType<? extends EggEntity> type, Level world) {
        super(type, world);
    }

    public void setMother(TropiSpiderEntity entity) {
        mother = new EntityReference<>(entity);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        EntityReference.store(mother, output, "mother");
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        mother = EntityReference.read(input, "mother");
    }

    @Override
    public boolean shouldEggRenderFlat() {
        return false;
    }

    @Override
    public String getEggTexture() {
        return "spideregg";
    }

    @Override
    public Entity onHatch() {
        if (level() instanceof ServerLevel serverLevel && mother != null) {
            TropiSpiderEntity spider = mother.getEntity(serverLevel, TropiSpiderEntity.class);
            if (spider != null) {
                return TropiSpiderEntity.haveBaby(spider);
            }
        }
        return TropicraftEntities.TROPI_SPIDER.get().create(level(), EntitySpawnReason.BREEDING);
    }

    @Override
    public int getHatchTime() {
        return 2000;
    }

    @Override
    public int getPreHatchMovement() {
        return 20;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(TropicraftItems.TROPI_SPIDER_SPAWN_EGG.get());
    }
}
