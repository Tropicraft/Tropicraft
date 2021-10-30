package net.tropicraft.core.common.network.message;

import java.util.function.Supplier;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;
import net.tropicraft.core.common.block.tileentity.AirCompressorTileEntity;

public class MessageAirCompressorInventory extends MessageTileEntity<AirCompressorTileEntity> {

    private ItemStack tank = ItemStack.EMPTY;

    public MessageAirCompressorInventory() {
        super();
    }

    public MessageAirCompressorInventory(AirCompressorTileEntity airCompressor) {
        super(airCompressor);
        this.tank = airCompressor.getTankStack();
    }
    
    public static void encode(final MessageAirCompressorInventory message, final FriendlyByteBuf buf) {
        MessageTileEntity.encode(message, buf);
        buf.writeItem(message.tank);
    }

    public static MessageAirCompressorInventory decode(final FriendlyByteBuf buf) {
        final MessageAirCompressorInventory message = new MessageAirCompressorInventory();
        MessageTileEntity.decode(message, buf);
        message.tank = buf.readItem();
        return message;
    }

    public static void handle(final MessageAirCompressorInventory message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            AirCompressorTileEntity compressor = message.getClientTileEntity();
            if (compressor != null) {
                if (!message.tank.isEmpty()) {
                    compressor.addTank(message.tank);
                } else {
                    compressor.ejectTank();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}