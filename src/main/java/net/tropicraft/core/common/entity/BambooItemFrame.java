package net.tropicraft.core.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.tropicraft.core.common.item.TropicraftItems;

public class BambooItemFrame extends ItemFrameEntity implements IEntityAdditionalSpawnData {

	public BambooItemFrame(final EntityType<? extends ItemFrameEntity> type, final World world) {
		super(type, world);
	}

	public BambooItemFrame(World worldIn, BlockPos pos, Direction direction) {
		this(TropicraftEntities.BAMBOO_ITEM_FRAME.get(), worldIn, pos, direction);
	}

	protected BambooItemFrame(final EntityType<? extends BambooItemFrame> type, final World world, final BlockPos pos,
			final Direction direction) {
		super(type, world);
		this.hangingPosition = pos;
		this.updateFacingWithBoundingBox(direction);
	}
	
	@Override
	protected void dropItemOrSelf(Entity entityIn, boolean dropSelf) {
		super.dropItemOrSelf(entityIn, false);
		if (dropSelf) {
			this.entityDropItem(TropicraftItems.BAMBOO_ITEM_FRAME.get());
		}
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeBlockPos(this.hangingPosition);
        buffer.writeByte(facingDirection.getIndex());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.hangingPosition = additionalData.readBlockPos();
        updateFacingWithBoundingBox(Direction.byIndex(additionalData.readByte()));
    }
}
