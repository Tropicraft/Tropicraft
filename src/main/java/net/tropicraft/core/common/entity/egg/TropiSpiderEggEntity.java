package net.tropicraft.core.common.entity.egg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.hostile.TropiSpiderEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public class TropiSpiderEggEntity extends EggEntity {

	protected static final DataParameter<Optional<UUID>> MOTHER_UNIQUE_ID = EntityDataManager.defineId(TropiSpiderEggEntity.class, DataSerializers.OPTIONAL_UUID);

	public TropiSpiderEggEntity(final EntityType<? extends EggEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(MOTHER_UNIQUE_ID, Optional.empty());
	}

	@Nullable
	public UUID getMotherId() {
		return entityData.get(MOTHER_UNIQUE_ID).orElse(null);
	}

	public void setMotherId(@Nullable UUID uuid) {
		entityData.set(MOTHER_UNIQUE_ID, Optional.ofNullable(uuid));
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT nbt) {
		super.addAdditionalSaveData(nbt);
		if (getMotherId() == null) {
			nbt.putString("MotherUUID", "");
		} else {
			nbt.putString("MotherUUID", getMotherId().toString());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT nbt) {
		super.readAdditionalSaveData(nbt);
		String motherUUID = "";
		if (nbt.contains("MotherUUID", 8)) {
			motherUUID = nbt.getString("MotherUUID");
		}

		if (!motherUUID.isEmpty()) {
			setMotherId(UUID.fromString(motherUUID));
		}
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
		if (level instanceof ServerWorld && getMotherId() != null) {
			final ServerWorld serverWorld = (ServerWorld) level;
			final Entity e = serverWorld.getEntity(getMotherId());

			if (e instanceof TropiSpiderEntity) {
				return TropiSpiderEntity.haveBaby((TropiSpiderEntity) e);
			}
		}
		return TropicraftEntities.TROPI_SPIDER.get().create(level);
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
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(TropicraftItems.TROPI_SPIDER_SPAWN_EGG.get());
	}
}
