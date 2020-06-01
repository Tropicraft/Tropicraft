package net.tropicraft.core.common.entity.egg;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.underdasea.StarfishEntity;
import net.tropicraft.core.common.entity.underdasea.StarfishType;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class StarfishEggEntity extends EchinodermEggEntity implements IEntityAdditionalSpawnData {
	private StarfishType starfishType;

	public StarfishEggEntity(final EntityType<? extends StarfishEggEntity> type, World world) {
		super(type, world);
		starfishType = StarfishType.values()[rand.nextInt(StarfishType.values().length)];
	}

	public StarfishType getStarfishType() {
		return starfishType;
	}

	public void setStarfishType(StarfishType starfishType) {
		this.starfishType = starfishType;
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeByte(starfishType.ordinal());
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		starfishType = StarfishType.values()[additionalData.readByte()];
	}

	@Override
	public void writeAdditional(CompoundNBT nbt) {
		super.writeAdditional(nbt);
		nbt.putByte("StarfishType", (byte) getStarfishType().ordinal());
	}

	@Override
	public void readAdditional(CompoundNBT nbt) {
		super.readAdditional(nbt);
		setStarfishType(StarfishType.values()[nbt.getByte("StarfishType")]);
	}

	@Override
	public String getEggTexture() {
		return "starfishegg";
	}

	@Override
	public Entity onHatch() {
		StarfishEntity baby = new StarfishEntity(TropicraftEntities.STARFISH.get(), world);
		baby.setBaby();
		baby.setStarfishType(starfishType);
		return baby;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(TropicraftItems.STARFISH.get());
	}
}