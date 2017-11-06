package net.tropicraft.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.client.gui.GuiTropicsLoading;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;

@SideOnly(Side.CLIENT)
public class TropicraftLoadingListener {

	private Minecraft mc = FMLClientHandler.instance().getClient();
	
	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent evt) {
		if(evt.getGui() instanceof GuiDownloadTerrain) {
			if(FMLClientHandler.instance().getClientPlayHandler() instanceof NetHandlerPlayClient) {
				if(mc.player != null && mc.player.dimension == TropicraftWorldUtils.TROPICS_DIMENSION_ID) {
					evt.setCanceled(true);
					mc.displayGuiScreen(new GuiTropicsLoading((NetHandlerPlayClient) FMLClientHandler.instance().getClientPlayHandler()));
				}
			}
		}
	}

}
