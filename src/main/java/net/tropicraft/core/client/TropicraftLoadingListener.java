package net.tropicraft.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.client.gui.GuiTropicsLoading;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;

@SideOnly(Side.CLIENT)
public class TropicraftLoadingListener {

	private Minecraft mc = FMLClientHandler.instance().getClient();
	private int lastDimension = 0;
	private GuiTropicsLoading guiLoading;
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent evt) {
		if(evt.phase.equals(Phase.END))
		lastDimension = evt.player.dimension;
	}
	
	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent evt) {
		if(evt.getGui() instanceof GuiDownloadTerrain) {
			if(FMLClientHandler.instance().getClientPlayHandler() instanceof NetHandlerPlayClient) {
				if(mc.player != null) {
					if(guiLoading == null) {
						guiLoading = new GuiTropicsLoading((NetHandlerPlayClient) FMLClientHandler.instance().getClientPlayHandler());
					}
					if(lastDimension == TropicraftWorldUtils.TROPICS_DIMENSION_ID && mc.player.dimension != TropicraftWorldUtils.TROPICS_DIMENSION_ID) {
						guiLoading.setLeaving(false);
					}else {
						guiLoading.setLeaving(true);
					}
					
					if(wasTropicsInvolved()) {
						evt.setGui(guiLoading);
					}
				}
			}
		}
	}
	
	public boolean wasTropicsInvolved() {
		return mc.player.dimension == TropicraftWorldUtils.TROPICS_DIMENSION_ID || 
				lastDimension == TropicraftWorldUtils.TROPICS_DIMENSION_ID;
	}

}
