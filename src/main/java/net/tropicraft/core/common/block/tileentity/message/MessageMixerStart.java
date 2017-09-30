package net.tropicraft.core.common.block.tileentity.message;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.tropicraft.core.common.block.tileentity.TileEntityDrinkMixer;

public class MessageMixerStart extends MessageTileEntity<TileEntityDrinkMixer> {

	public MessageMixerStart() {
		super();
	}

	public MessageMixerStart(TileEntityDrinkMixer mixer) {
		super(mixer);
	}

	public static final class Handler implements IMessageHandler<MessageMixerStart, IMessage> {

		@Override
		public IMessage onMessage(MessageMixerStart message, MessageContext ctx) {
			TileEntityDrinkMixer te = message.getClientTileEntity();
			if (te != null) {
				te.startMixing();
			}
			return null;
		}
	}
}
