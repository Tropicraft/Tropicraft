package net.tropicraft.event;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.util.EffectHelper;
import net.tropicraft.util.TropicraftWorldUtils;
import CoroUtil.forge.CoroAI;
import CoroUtil.world.WorldDirector;
import CoroUtil.world.WorldDirectorManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.ExtendedRenderer;

public class TCMiscEvents {

	@SubscribeEvent
	public void worldLoad(Load event) {
		if (!event.world.isRemote) {
			if (((WorldServer)event.world).provider.dimensionId == TropicraftWorldUtils.TROPICS_DIMENSION_ID) {
				if (WorldDirectorManager.instance().getWorldDirector(CoroAI.modID, event.world) == null) {
					WorldDirectorManager.instance().registerWorldDirector(new WorldDirector(), CoroAI.modID, event.world);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void tickClient(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			EffectHelper.tick();
		}

		//so bad, but, where else?
		if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) {
			for (int ii = 0; ii < ExtendedRenderer.rotEffRenderer.fxLayers.length; ii++) {
				List list = ExtendedRenderer.rotEffRenderer.fxLayers[ii];
				list.clear();
			}
		}
	}

	@SubscribeEvent
	public void tickServer(ServerTickEvent event) {
		if (event.phase == Phase.END) {
			EffectHelper.tick();
		}
		
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0);

		if (world != null && world instanceof WorldServer) {
			for (int ii = 0; ii < world.playerEntities.size(); ii++) {
				Entity entity1 = (Entity)world.playerEntities.get(ii);

				if (entity1 instanceof EntityPlayerMP) {
					// If player is drunk and it is sunset and the player is riding a chair
					// teleport player!
					if (((EntityPlayerMP)entity1).isPotionActive(Potion.confusion) && isSunset(world) && entity1.ridingEntity instanceof EntityChair) {
						entity1.ridingEntity = null;
						TropicraftWorldUtils.teleportPlayer((EntityPlayerMP)entity1);
					}
				}
			}
		}
	}

	/**
	 * Returns whether it is currently sunset
	 * @param world World object
	 * @return Is it currently sunset in the world?
	 */
	private boolean isSunset(World world) {
		long timeDay = world.getWorldTime() % 24000;
		return timeDay > 12200 && timeDay < 14000;
	}
}
