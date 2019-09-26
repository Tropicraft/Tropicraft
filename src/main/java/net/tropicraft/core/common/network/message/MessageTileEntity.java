package net.tropicraft.core.common.network.message;

import com.google.common.reflect.TypeToken;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.tropicraft.core.common.network.TropicraftMessage;

/**
 * Based on <a href="https://github.com/SleepyTrousers/EnderCore">EnderCore</a>, with permission.
 *
 * Licensed under CC0.
 */
public abstract class MessageTileEntity<T extends TileEntity> implements TropicraftMessage {
	protected long pos;
	@Deprecated
	protected int x;
	@Deprecated
	protected int y;
	@Deprecated
	protected int z;

	protected MessageTileEntity() {}

	protected MessageTileEntity(T tile) {
		pos = tile.getPos().toLong();
	}

	protected static void encode(final MessageTileEntity<?> message, PacketBuffer buf) {
		buf.writeLong(message.pos);
	}

	protected static void decode(final MessageTileEntity<?> message, PacketBuffer buf) {
		message.pos = buf.readLong();
		BlockPos bp = message.getPos();
		message.x = bp.getX();
		message.y = bp.getY();
		message.z = bp.getZ();
	}

	public BlockPos getPos() {
		return BlockPos.fromLong(pos);
	}

	protected T getClientTileEntity() {
		return getTileEntity(DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().world));
	}

	@SuppressWarnings("unchecked")
	protected T getTileEntity(World worldObj) {
		// Sanity check, and prevent malicious packets from loading chunks
		if (worldObj == null || !worldObj.isBlockLoaded(getPos())) {
			return null;
		}
		TileEntity te = worldObj.getTileEntity(getPos());
		if (te == null) {
			return null;
		}
		TypeToken<?> teType = TypeToken.of(getClass()).resolveType(MessageTileEntity.class.getTypeParameters()[0]);
		if (teType.isSubtypeOf(te.getClass())) {
			return (T) te;
		}
		return null;
	}
}
