package net.tropicraft.core.common.entity.egg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.hostile.TropiSpiderEntity;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class TropiSpiderEggEntity extends EggEntity {

	protected static final DataParameter<Optional<UUID>> MOTHER_UNIQUE_ID = EntityDataManager.createKey(TropiSpiderEggEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	public TropiSpiderEggEntity(final EntityType<? extends EggEntity> type, World world) {
		super(type, world);
	}

	@Nullable
	public UUID getMotherId() {
		return dataManager.get(MOTHER_UNIQUE_ID).orElse(null);
	}

	public void setMotherId(@Nullable UUID uuid) {
		dataManager.set(MOTHER_UNIQUE_ID, Optional.ofNullable(uuid));
	}

	@Override
	public void writeAdditional(CompoundNBT nbt) {
		super.writeAdditional(nbt);
		if (getMotherId() == null) {
			nbt.putString("MotherUUID", "");
		} else {
			nbt.putString("MotherUUID", getMotherId().toString());
		}
	}

	@Override
	public void readAdditional(CompoundNBT nbt) {
		super.readAdditional(nbt);
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
		if (world instanceof ServerWorld && getMotherId() != null) {
			final ServerWorld serverWorld = (ServerWorld) world;
			final Entity e = serverWorld.getEntityByUuid(getMotherId());

			if (e instanceof TropiSpiderEntity) {
				return TropiSpiderEntity.haveBaby((TropiSpiderEntity) e);
			}
		}
		return TropicraftEntities.TROPI_SPIDER.get().create(world);
	}

	@Override
	public int getHatchTime() {
		return 2000;
	}

	@Override
	public int getPreHatchMovement() {
		return 20;
	}
}
