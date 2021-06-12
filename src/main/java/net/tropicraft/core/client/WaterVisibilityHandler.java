package net.tropicraft.core.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.fluid.IFluidState;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class WaterVisibilityHandler {
	@SubscribeEvent
	public static void renderWaterFog(EntityViewRenderEvent.FogDensity event) {
		ActiveRenderInfo info = event.getInfo();
		IFluidState fluid = info.getFluidState();
		if (!fluid.isTagged(FluidTags.WATER) || !(info.getRenderViewEntity() instanceof ClientPlayerEntity)) {
			return;
		}

		ClientPlayerEntity player = (ClientPlayerEntity) info.getRenderViewEntity();

		if (player.world.dimension.getType() == TropicraftWorldUtils.TROPICS_DIMENSION) {
			// if the player has the relevant effect, we disable the water brightness adjustment
			if (player.isPotionActive(Effects.NIGHT_VISION) || player.isPotionActive(Effects.WATER_BREATHING) || player.isPotionActive(Effects.CONDUIT_POWER)) {
				// Taken from FogRenderer#setupFog
				RenderSystem.fogMode(GlStateManager.FogMode.EXP2);
				event.setDensity(0.02F);
				event.setCanceled(true);
			}
		}
	}
}
