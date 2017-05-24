package net.tropicraft.core.common.block.tileentity.message;

import com.google.common.reflect.TypeToken;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Taken from <a href="https://github.com/SleepyTrousers/EnderCore">EnderCore</a>, with permission.
 * 
 * Licensed under CC0.
 */
public abstract class MessageTileEntity<T extends TileEntity> implements IMessage {

    private long pos;
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

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = buf.readLong();
        BlockPos bp = getPos();
        x = bp.getX();
        y = bp.getY();
        z = bp.getZ();
    }

    public BlockPos getPos() {
        return BlockPos.fromLong(pos);
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
        if (teType.isAssignableFrom(te.getClass())) {
            return (T) te;
        }
        return null;
    }

    protected World getWorld(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity.worldObj;
    }
}
