package net.tropicraft.core.common.entity.egg;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.underdasea.StarfishEntity;
import net.tropicraft.core.common.entity.underdasea.StarfishType;
import net.tropicraft.core.common.item.TropicraftItems;

public class StarfishEggEntity extends EchinodermEggEntity implements IEntityWithComplexSpawn {
    private StarfishType starfishType;

    public StarfishEggEntity(EntityType<? extends StarfishEggEntity> type, Level world) {
        super(type, world);
        starfishType = StarfishType.values()[random.nextInt(StarfishType.values().length)];
    }

    public StarfishType getStarfishType() {
        return starfishType;
    }

    public void setStarfishType(StarfishType starfishType) {
        this.starfishType = starfishType;
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeByte(starfishType.ordinal());
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        starfishType = StarfishType.values()[additionalData.readByte()];
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putByte("StarfishType", (byte) getStarfishType().ordinal());
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setStarfishType(StarfishType.values()[input.getByteOr("StarfishType", (byte) 0)]);
    }

    @Override
    public String getEggTexture() {
        return "starfishegg";
    }

    @Override
    public Entity onHatch() {
        StarfishEntity baby = new StarfishEntity(TropicraftEntities.STARFISH.get(), level());
        baby.setBaby();
        baby.setStarfishType(starfishType);
        return baby;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(TropicraftItems.STARFISH.get());
    }
}
