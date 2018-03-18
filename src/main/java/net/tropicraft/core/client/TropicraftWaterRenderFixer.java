package net.tropicraft.core.client;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.tropicraft.Info;
import net.tropicraft.core.common.biome.BiomeTropicraft;
import net.tropicraft.core.common.block.BlockTropicraftFence;
import net.tropicraft.core.common.block.BlockTropicraftFence.WaterState;
import net.tropicraft.core.common.item.scuba.ItemScubaHelmet;
import net.tropicraft.core.registry.BlockRegistry;

public class TropicraftWaterRenderFixer {

    private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation(Info.MODID, "textures/misc/underwater.png");
    
    private static final Set<IBlockState> tropicalOverlayBlocks = new HashSet<>();
    
    private static void addOverlayAll(Block block) {
        tropicalOverlayBlocks.addAll(block.getBlockState().getValidStates());
    }
    
    private static void addOverlayMatching(Block block, Predicate<IBlockState> pred) {
        for (IBlockState state : block.getBlockState().getValidStates()) {
            if (pred.apply(state)) {
                addOverlay(state);
            }
        }
    }
    
    private static void addOverlay(IBlockState state) {
        tropicalOverlayBlocks.add(state);
    }
    
    static {
        addOverlayAll(BlockRegistry.tropicsWater);
        addOverlayAll(BlockRegistry.tropicsPortal);
        addOverlayAll(BlockRegistry.tropicsPortalTeleporter);
        addOverlayAll(BlockRegistry.coral);
        
        Predicate<IBlockState> waterPred = s -> s.getValue(BlockTropicraftFence.WATER) != WaterState.NONE;
        addOverlayMatching(BlockRegistry.bambooFence, waterPred);
        addOverlayMatching(BlockRegistry.chunkFence, waterPred);
        addOverlayMatching(BlockRegistry.mahoganyFence, waterPred);
        addOverlayMatching(BlockRegistry.palmFence, waterPred);
        addOverlayMatching(BlockRegistry.thatchFence, waterPred);
    }

    @SubscribeEvent
    public void onBlockOverlay(RenderBlockOverlayEvent event) {
        if (event.getOverlayType() == OverlayType.WATER) {
            double d0 = event.getPlayer().posY + event.getPlayer().getEyeHeight();
            BlockPos blockpos = new BlockPos(event.getPlayer().posX, d0, event.getPlayer().posZ);
            IBlockState atPos = event.getPlayer().getEntityWorld().getBlockState(blockpos);
            if (tropicalOverlayBlocks.contains(atPos)) {
                event.setCanceled(true);
                Minecraft mc = Minecraft.getMinecraft();
                
                // ItemRenderer#renderOverlays
                
                mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder vertexbuffer = tessellator.getBuffer();
                float f = mc.player.getBrightness();
                GlStateManager.color(f, f, f, 0.6F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.pushMatrix();
                float f7 = -mc.player.rotationYaw / 64.0F;
                float f8 = mc.player.rotationPitch / 64.0F;
                vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
                vertexbuffer.pos(-1.0D, -1.0D, -0.5D).tex((double)(4.0F + f7), (double)(4.0F + f8)).endVertex();
                vertexbuffer.pos(1.0D, -1.0D, -0.5D).tex((double)(0.0F + f7), (double)(4.0F + f8)).endVertex();
                vertexbuffer.pos(1.0D, 1.0D, -0.5D).tex((double)(0.0F + f7), (double)(0.0F + f8)).endVertex();
                vertexbuffer.pos(-1.0D, 1.0D, -0.5D).tex((double)(4.0F + f7), (double)(0.0F + f8)).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableBlend();
            }
        }
    }
    
    private static final float FOG_OCEAN = 0.0115F;
    private static final float FOG_KELP_FOREST = 0.125f;
    
    // how much the fog changes per tick
    private static final float FOG_DELTA = 0.0025f;
    
    private float lastTickFogDensity = FOG_OCEAN;
    private float fogDensity = FOG_OCEAN;
    
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {

		Minecraft mc = Minecraft.getMinecraft();
    	if (event.phase == Phase.START && mc.world != null && mc.getRenderViewEntity() != null) {
    		IBlockState state = ActiveRenderInfo.getBlockStateAtEntityViewpoint(mc.world, mc.getRenderViewEntity(), mc.getRenderPartialTicks());
    		if (tropicalOverlayBlocks.contains(state)) {
    			BlockPos pos = mc.getRenderViewEntity().getPosition();
    	        double y = mc.getRenderViewEntity().prevPosY + (mc.getRenderViewEntity().posY - mc.getRenderViewEntity().prevPosY) * mc.getRenderPartialTicks();
        		float fogTarget;
        		if (mc.world.getBiome(pos) == BiomeTropicraft.kelpForest) {
        			float diff = FOG_KELP_FOREST - FOG_OCEAN;
        			double scale = 1 - MathHelper.clamp((y - 45) / 20f, 0, 1);
        			fogTarget = (float) (FOG_OCEAN + (diff * scale));
        		} else {
        			fogTarget = FOG_OCEAN;
        		}
        		lastTickFogDensity = fogDensity;
        		if (fogDensity < fogTarget) {
        			fogDensity = Math.min(fogDensity + FOG_DELTA, fogTarget);
        		} else if (fogDensity > fogTarget) {
        			fogDensity = Math.max(fogDensity - FOG_DELTA, fogTarget);
        		}
    		}
    	}
    }
    
    @SubscribeEvent
    public void onFogDensity(FogDensity event) {
        if (tropicalOverlayBlocks.contains(event.getState())) {
            event.setCanceled(true);

            Entity ent = event.getEntity();
            if (ent instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)ent;

                ItemStack goggles = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (goggles != null && goggles.getItem() != null && goggles.getItem() instanceof ItemScubaHelmet) {
                    fogDensity = 0.009F;
                }
            }

            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            double partialDelta = FOG_DELTA * event.getRenderPartialTicks();
            event.setDensity((float) (lastTickFogDensity > fogDensity ? lastTickFogDensity - partialDelta : lastTickFogDensity < fogDensity ? lastTickFogDensity + partialDelta : fogDensity));
        }
    }

    @SubscribeEvent
    public void onFogColor(EntityViewRenderEvent.FogColors event) {
        if (tropicalOverlayBlocks.contains(event.getState())) {
            //keep the fog colors from 1.10 that we expect for our waters
            event.setRed(0.02F);
            event.setGreen(0.02F);
            event.setBlue(0.2F);
        }
    }
}
