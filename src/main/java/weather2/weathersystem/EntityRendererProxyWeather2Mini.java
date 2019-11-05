package weather2.weathersystem;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import weather2.ClientTickHandler;
import weather2.Weather;
import weather2.config.ConfigMisc;

public class EntityRendererProxyWeather2Mini extends GameRenderer
{

	public static final ResourceLocation RAIN_TEXTURES_GREEN = new ResourceLocation(Weather.MODID, "textures/environment/rain_green.png");

    public EntityRendererProxyWeather2Mini(Minecraft var1, IResourceManager resMan)
    {
        super(var1, resMan);
    }

    @Override
    protected void renderRainSnow(float par1)
    {
    	
    	boolean overrideOn = ConfigMisc.Misc_proxyRenderOverrideEnabled;

		/**
		 * why render here? because renderRainSnow provides better context, solves issues:
		 * - translucent blocks rendered after
		 * -- shaders are color adjusted when rendering on other side of
		 * --- water
		 * --- stained glass, etc
		 */
		//TODO: 1.14 uncomment
		/*if (ConfigCoroUtil.useEntityRenderHookForShaders) {
			EventHandler.hookRenderShaders(par1);
		}*/
    	
    	if (!overrideOn) {
    		super.renderRainSnow(par1);
    		return;
    	} else {
    		
    		//note, the overcast effect change will effect vanilla non particle rain distance too, particle rain for life!
    		/*if (!ConfigParticle.Particle_RainSnow) {
    			super.renderRainSnow(par1);
    		}*/

			if (ClientTickHandler.minigameWeatherInstance != null && ClientTickHandler.minigameWeatherInstance.isLastRainWasAcid()) {
				renderRainSnowTinted(par1);
			} else {
				super.renderRainSnow(par1);
			}
    	}
    }

	protected void renderRainSnowTinted(float partialTicks) {
		net.minecraftforge.client.IRenderHandler renderer = this.mc.world.getDimension().getWeatherRenderer();
		if (renderer != null) {
			renderer.render(this.rendererUpdateCount, partialTicks, this.mc.world, this.mc);
			return;
		}
		float f = this.mc.world.getRainStrength(partialTicks);
		if (!(f <= 0.0F)) {
			this.enableLightmap();
			World world = this.mc.world;
			int i = MathHelper.floor(this.activeRender.getProjectedView().x);
			int j = MathHelper.floor(this.activeRender.getProjectedView().y);
			int k = MathHelper.floor(this.activeRender.getProjectedView().z);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			GlStateManager.disableCull();
			GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
			GlStateManager.enableBlend();
			GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.alphaFunc(516, 0.1F);
			double d0 = this.activeRender.getProjectedView().x;
			double d1 = this.activeRender.getProjectedView().y;
			double d2 = this.activeRender.getProjectedView().z;
			int l = MathHelper.floor(d1);
			int i1 = 5;
			if (this.mc.gameSettings.fancyGraphics) {
				i1 = 10;
			}

			int j1 = -1;
			float f1 = (float)this.rendererUpdateCount + partialTicks;
			bufferbuilder.setTranslation(-d0, -d1, -d2);
			GlStateManager.color4f(1F, 1F, 1F, 1.0F);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for(int k1 = k - i1; k1 <= k + i1; ++k1) {
				for(int l1 = i - i1; l1 <= i + i1; ++l1) {
					int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
					double d3 = (double)this.rainXCoords[i2] * 0.5D;
					double d4 = (double)this.rainYCoords[i2] * 0.5D;
					blockpos$mutableblockpos.setPos(l1, 0, k1);
					Biome biome = world.getBiome(blockpos$mutableblockpos);
					if (biome.getPrecipitation() != Biome.RainType.NONE) {
						int j2 = world.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos$mutableblockpos).getY();
						int k2 = j - i1;
						int l2 = j + i1;
						if (k2 < j2) {
							k2 = j2;
						}

						if (l2 < j2) {
							l2 = j2;
						}

						int i3 = j2;
						if (j2 < l) {
							i3 = l;
						}

						if (k2 != l2) {
							this.random.setSeed((long)(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
							blockpos$mutableblockpos.setPos(l1, k2, k1);
							float f2 = biome.func_225486_c(blockpos$mutableblockpos);
							if (f2 >= 0.15F) {
								if (j1 != 0) {
									if (j1 >= 0) {
										tessellator.draw();
									}

									j1 = 0;
									this.mc.getTextureManager().bindTexture(RAIN_TEXTURES_GREEN);
									bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
								}

								double d5 = -((double)(this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + (double)partialTicks) / 32.0D * (3.0D + this.random.nextDouble());
								double d6 = (double)((float)l1 + 0.5F) - this.activeRender.getProjectedView().x;
								double d7 = (double)((float)k1 + 0.5F) - this.activeRender.getProjectedView().z;
								float f3 = MathHelper.sqrt(d6 * d6 + d7 * d7) / (float)i1;
								float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
								blockpos$mutableblockpos.setPos(l1, i3, k1);
								int j3 = world.getCombinedLight(blockpos$mutableblockpos, 0);
								int k3 = j3 >> 16 & '\uffff';
								int l3 = j3 & '\uffff';
								bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)l2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
								bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)l2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
								bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)k2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
								bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)k2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
							} else {
								if (j1 != 1) {
									if (j1 >= 0) {
										tessellator.draw();
									}

									j1 = 1;
									this.mc.getTextureManager().bindTexture(SNOW_TEXTURES);
									bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
								}

								double d8 = (double)(-((float)(this.rendererUpdateCount & 511) + partialTicks) / 512.0F);
								double d9 = this.random.nextDouble() + (double)f1 * 0.01D * (double)((float)this.random.nextGaussian());
								double d10 = this.random.nextDouble() + (double)(f1 * (float)this.random.nextGaussian()) * 0.001D;
								double d11 = (double)((float)l1 + 0.5F) - this.activeRender.getProjectedView().x;
								double d12 = (double)((float)k1 + 0.5F) - this.activeRender.getProjectedView().z;
								float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / (float)i1;
								float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f;
								blockpos$mutableblockpos.setPos(l1, i3, k1);
								int i4 = (world.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
								int j4 = i4 >> 16 & '\uffff';
								int k4 = i4 & '\uffff';
								bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)l2, (double)k1 - d4 + 0.5D).tex(0.0D + d9, (double)k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
								bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)l2, (double)k1 + d4 + 0.5D).tex(1.0D + d9, (double)k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
								bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)k2, (double)k1 + d4 + 0.5D).tex(1.0D + d9, (double)l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
								bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)k2, (double)k1 - d4 + 0.5D).tex(0.0D + d9, (double)l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
							}
						}
					}
				}
			}

			if (j1 >= 0) {
				tessellator.draw();
			}

			bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
			GlStateManager.enableCull();
			GlStateManager.disableBlend();
			GlStateManager.alphaFunc(516, 0.1F);
			this.disableLightmap();
		}
	}
}
