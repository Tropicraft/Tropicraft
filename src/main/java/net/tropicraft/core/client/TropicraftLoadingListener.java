package net.tropicraft.core.client;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.client.gui.GuiTropicsLoading;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;

@SideOnly(Side.CLIENT)
public class TropicraftLoadingListener {

	private Minecraft mc = FMLClientHandler.instance().getClient();
	private int lastDimension = 0;
	private ArrayList<Pair<String, Integer>> farewellSkipDimensions = new ArrayList<Pair<String, Integer>>();

	@SubscribeEvent
	public void onClientTick(ClientTickEvent evt) {
		if (evt.phase.equals(Phase.END) && mc.player != null)
			lastDimension = mc.player.dimension;
	}

	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent evt) {
		if (evt.getGui() instanceof GuiDownloadTerrain) {
			if (FMLClientHandler.instance().getClientPlayHandler() instanceof NetHandlerPlayClient) {
				if (mc.player != null) {
					GuiTropicsLoading guiLoading = new GuiTropicsLoading();
					boolean isLeaving = false;
					if (lastDimension != TropicraftWorldUtils.TROPICS_DIMENSION_ID
							&& mc.player.dimension == TropicraftWorldUtils.TROPICS_DIMENSION_ID) {
						isLeaving = true;
					}	
					guiLoading.setLeaving(isLeaving);
					if (wasTropicsInvolved() && isDimensionFarewellAllowed(isLeaving)) {
						evt.setGui(guiLoading);
					}
				}
			}
		}
	}

	public boolean isDimensionFarewellAllowed(boolean isFarewell) {
		if(!isFarewell) {
			return true;
		}
		for (int i = 0; i < this.farewellSkipDimensions.size(); i++) {
			Pair<String, Integer> pair = this.farewellSkipDimensions.get(i);
			if(pair.getRight().intValue() == mc.player.dimension) {
				return false;
			}
		}
		
		return true;
	}

	public boolean wasTropicsInvolved() {
		return mc.player.dimension == TropicraftWorldUtils.TROPICS_DIMENSION_ID
				|| lastDimension == TropicraftWorldUtils.TROPICS_DIMENSION_ID;
	}

}
