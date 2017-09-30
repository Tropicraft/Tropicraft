package net.tropicraft.core.common.block.tileentity.message;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.tropicraft.core.common.block.tileentity.TileEntitySifter;

public class MessageSifterStart extends MessageTileEntity<TileEntitySifter> {
	
	public MessageSifterStart() {
		super();
	}

	public MessageSifterStart(TileEntitySifter sifter) {
		super(sifter);
	}

	public static final class Handler implements IMessageHandler<MessageSifterStart, IMessage> {

		@Override
		public IMessage onMessage(MessageSifterStart message, MessageContext ctx) {
			TileEntitySifter te = message.getClientTileEntity();
			if (te != null) {
				te.startSifting();
			}
			return null;
		}
	}
}
