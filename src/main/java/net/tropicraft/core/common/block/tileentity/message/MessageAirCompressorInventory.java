package net.tropicraft.core.common.block.tileentity.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.tropicraft.core.common.block.tileentity.TileEntityAirCompressor;

public class MessageAirCompressorInventory extends MessageTileEntity<TileEntityAirCompressor> {

    private ItemStack tank;

    public MessageAirCompressorInventory() {
        super();
    }

    public MessageAirCompressorInventory(TileEntityAirCompressor airCompressor) {
        super(airCompressor);
        this.tank = airCompressor.getTankStack();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ByteBufUtils.writeItemStack(buf, tank);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        this.tank = ByteBufUtils.readItemStack(buf);
    }

    public static final class Handler implements IMessageHandler<MessageAirCompressorInventory, IMessage> {

        @Override
        public IMessage onMessage(MessageAirCompressorInventory message, MessageContext ctx) {
            TileEntityAirCompressor compressor = message.getClientTileEntity();
            if (compressor != null) {
                compressor.setTank(message.tank);
            }
            return null;
        }
    }
}
