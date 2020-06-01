package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.egg.EggEntity;
import net.tropicraft.core.common.entity.egg.StarfishEggEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class StarfishEntity extends EchinodermEntity implements IEntityAdditionalSpawnData {
	public static final float BABY_WIDTH = 0.25f;
	public static final float ADULT_WIDTH = 1f;
	public static final float BABY_HEIGHT = 0.1f;
	public static final float ADULT_HEIGHT = 0.2f;
	public static final float BABY_YOFFSET = 0.03125f;
	public static final float ADULT_YOFFSET = 0.03125f;

	private static final DataParameter<Byte> DATA_STARFISH_TYPE = EntityDataManager.createKey(StarfishEntity.class, DataSerializers.BYTE);

	public StarfishEntity(final EntityType<? extends StarfishEntity> type, World world) {
		super(type, world);
		experienceValue = 5;
	}

	@Override
	@Nullable
	public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT nbt) {
		setStarfishType(StarfishType.values()[rand.nextInt(StarfishType.values().length)]);
		return super.onInitialSpawn(world, difficultyInstance, spawnReason, entityData, nbt);
	}

	@Override
	public void registerData() {
		super.registerData();
		getDataManager().register(DATA_STARFISH_TYPE, (byte) 0);
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
	}
	
	public StarfishType getStarfishType() {
		return StarfishType.values()[dataManager.get(DATA_STARFISH_TYPE)];
	}
	
	public void setStarfishType(StarfishType starfishType) {
		dataManager.set(DATA_STARFISH_TYPE, (byte) starfishType.ordinal());
	}
	
	@Override
	public void writeAdditional(CompoundNBT nbt) {
		super.writeAdditional(nbt);
		nbt.putByte("StarfishType", (byte) getStarfishType().ordinal());
	}

	@Override
	public void readAdditional(CompoundNBT nbt) {
		super.readAdditional(nbt);
		if (nbt.contains("StarfishType")) {
			setStarfishType(StarfishType.values()[nbt.getByte("StarfishType")]);
		} else {
			setStarfishType(StarfishType.RED);
		}
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeByte(getStarfishType().ordinal());
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		setStarfishType(StarfishType.values()[additionalData.readByte()]);
	}

	@Override
	public EggEntity createEgg() {
		StarfishEggEntity entity = new StarfishEggEntity(TropicraftEntities.STARFISH_EGG.get(), world);
		entity.setStarfishType(getStarfishType());
		return entity;
	}

	@Override
	public float getBabyWidth() {
		return BABY_WIDTH;
	}

	@Override
	public float getAdultWidth() {
		return ADULT_WIDTH;
	}

	@Override
	public float getBabyHeight() {
		return BABY_HEIGHT;
	}

	@Override
	public float getAdultHeight() {
		return ADULT_HEIGHT;
	}

	@Override
	public float getBabyYOffset() {
		return BABY_YOFFSET;
	}

	@Override
	public float getAdultYOffset() {
		return ADULT_YOFFSET;
	}

	@Override
	public boolean isPotentialMate(EchinodermEntity other) {
		return super.isPotentialMate(other) && ((StarfishEntity) other).getStarfishType() == getStarfishType();
	}
	
	@Override
	public void onDeath(DamageSource d) {
		super.onDeath(d);
		// TODO replace with loot table
		if (!world.isRemote) {
			entityDropItem(new ItemStack(TropicraftItems.STARFISH.get()), 0);
		}
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(TropicraftItems.STARFISH.get());
	}
}