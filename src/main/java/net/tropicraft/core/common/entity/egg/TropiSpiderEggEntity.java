package net.tropicraft.core.common.entity.egg;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.hostile.TropiSpiderEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public class TropiSpiderEggEntity extends EggEntity {

	protected static final EntityDataAccessor<Optional<UUID>> MOTHER_UNIQUE_ID = SynchedEntityData.defineId(TropiSpiderEggEntity.class, EntityDataSerializers.OPTIONAL_UUID);

	public TropiSpiderEggEntity(final EntityType<? extends EggEntity> type, Level world) {
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
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		if (getMotherId() == null) {
			nbt.putString("MotherUUID", "");
		} else {
			nbt.putString("MotherUUID", getMotherId().toString());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
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
		if (level instanceof ServerLevel && getMotherId() != null) {
			final ServerLevel serverWorld = (ServerLevel) level;
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
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(TropicraftItems.TROPI_SPIDER_SPAWN_EGG.get());
	}
}
