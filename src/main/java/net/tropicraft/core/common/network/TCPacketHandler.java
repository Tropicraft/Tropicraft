package net.tropicraft.core.common.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.tropicraft.Info;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.block.tileentity.message.MessageAirCompressorInventory;
import net.tropicraft.core.common.block.tileentity.message.MessageMixerInventory;
import net.tropicraft.core.common.block.tileentity.message.MessageMixerStart;
import net.tropicraft.core.common.block.tileentity.message.MessageSifterInventory;
import net.tropicraft.core.common.block.tileentity.message.MessageSifterStart;

/**
 * Taken from <a href="https://github.com/SleepyTrousers/EnderCore">EnderCore</a>, with permission.
 * 
 * Licensed under CC0.
 */
public class TCPacketHandler {

	public static final ThreadedNetworkWrapper INSTANCE = new ThreadedNetworkWrapper(Info.NAME);

	public static void init() {
		INSTANCE.registerMessage(MessageMixerInventory.Handler.class, MessageMixerInventory.class, 0, Side.CLIENT);
		INSTANCE.registerMessage(MessageMixerStart.Handler.class, MessageMixerStart.class, 1, Side.CLIENT);
		INSTANCE.registerMessage(MessageSifterInventory.Handler.class, MessageSifterInventory.class, 2, Side.CLIENT);
		INSTANCE.registerMessage(MessageSifterStart.Handler.class, MessageSifterStart.class, 3, Side.CLIENT);
		
		// Scuba client listener
		Tropicraft.proxy.registerClientPacketScuba();
		
		// Scuba server listener
		INSTANCE.registerMessage(MessagePlayerSwimData.Handler.class, MessagePlayerSwimData.class, 5, Side.SERVER);

		INSTANCE.registerMessage(MessageAirCompressorInventory.Handler.class, MessageAirCompressorInventory.class, 6, Side.CLIENT);
	}

	public static void sendToAllAround(IMessage message, TileEntity te, int range) {
		BlockPos p = te.getPos();
		INSTANCE.sendToAllAround(message, new TargetPoint(te.getWorld().provider.getDimension(), p.getX(), p.getY(), p.getZ(), range));
	}

	public static void sendToAllAround(IMessage message, TileEntity te) {
		sendToAllAround(message, te, 64);
	}

	public static void sendTo(IMessage message, EntityPlayerMP player) {
		INSTANCE.sendTo(message, player);
	}

	public static void sendToServer(IMessage message) {
		INSTANCE.sendToServer(message);
	}
}
