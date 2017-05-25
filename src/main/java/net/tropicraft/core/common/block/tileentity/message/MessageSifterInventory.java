package net.tropicraft.core.common.block.tileentity.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.tropicraft.core.common.block.tileentity.TileEntitySifter;

public class MessageSifterInventory extends MessageTileEntity<TileEntitySifter> {

	private ItemStack siftItem;

	public MessageSifterInventory() {
		super();
	}

	public MessageSifterInventory(TileEntitySifter sifter) {
		super(sifter);
		this.siftItem = sifter.siftItem;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		ByteBufUtils.writeItemStack(buf, siftItem);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		this.siftItem = ByteBufUtils.readItemStack(buf);
	}

	public static final class Handler implements IMessageHandler<MessageSifterInventory, IMessage> {

		@Override
		public IMessage onMessage(MessageSifterInventory message, MessageContext ctx) {
			TileEntitySifter sifter = message.getTileEntity(Minecraft.getMinecraft().theWorld);
			if (sifter != null) {
				sifter.siftItem = message.siftItem;
			}
			return null;
		}
	}
}
