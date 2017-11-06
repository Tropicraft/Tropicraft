package net.tropicraft.core.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Names;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

@SideOnly(Side.CLIENT)
public class TropicraftLoadingListener {

	private static final String[] CATEGORIES = {"misc", "knowledge", "hint"};
	private static final String[] BACKGROUNDS = {"loading_bg1", "loading_bg2", "loading_bg3"};
	private static final String[] BLACKLISTED = {"eih", "seaurchin", "starfish", "fish", "piranha", "sardine", "eagleray", "mow"};

	private Random rand = new Random();
	private long lastWorldUpdateTick = 0L;
	private Minecraft mc = FMLClientHandler.instance().getClient();
	
	private Pair<Entity, Entity> screenEntities = null;
	private Triple<ItemStack, ItemStack, ItemStack> screenItems = null;
	private String screenTitle = "";
	private String screenBackground = BACKGROUNDS[0];
	private boolean screenReassign = false;

	
	private void assignScreenContent() {
		ArrayList<String> ta = new ArrayList<String>(Arrays.asList(Names.EGG_NAMES));
		for(String name : BLACKLISTED) {
			ta.remove(name);
		}
		String firstEnt = ta.get(rand.nextInt(ta.size()));
		Entity ent1 = EntityList.createEntityByName("tropicraft."+firstEnt, mc.world);
		ta.remove(firstEnt);
		Entity ent2 = EntityList.createEntityByName("tropicraft."+ta.get(rand.nextInt(ta.size())), mc.world);
		
		// make sure these entities have a random texture assigned
		if(ent1 instanceof EntityTropicraftWaterBase)
			((EntityTropicraftWaterBase)ent1).assignRandomTexture();
		if(ent2 instanceof EntityTropicraftWaterBase)
			((EntityTropicraftWaterBase)ent2).assignRandomTexture();
		
		
		screenEntities = Pair.of(ent1, ent2);
		
		screenItems = Triple.of(stack(ItemRegistry.lemon), stack(BlockRegistry.pineapple), stack(ItemRegistry.orange));
		
		// Pick random subtitle
		String cat = CATEGORIES[rand.nextInt(CATEGORIES.length)];
		
		int count = 0;
		String key = "tropicraft.loading."+cat+".1";
		while(!(I18n.translateToLocal(key)).equals(key)) {
			if(I18n.translateToLocal(key).equals(key)){
				break;
			}
			key = "tropicraft.loading."+cat+"."+count++;
		}
		
		screenTitle = "tropicraft.loading."+cat+"."+String.valueOf(rand.nextInt(count+1)+1);
		if(cat.equals("knowledge") || cat.equals("hint")) {
			String prepend = I18n.translateToLocal("tropicraft.loading."+cat+".pre");
			screenTitle = I18n.translateToLocalFormatted(screenTitle, prepend);
		}else {
			screenTitle = I18n.translateToLocal(screenTitle);

		}
		
		// Pick random background
		screenBackground = BACKGROUNDS[rand.nextInt(BACKGROUNDS.length)];
	}
	
	
	@SubscribeEvent
	public void onRenderOverlay(RenderTickEvent evt) {
		if(evt.type != TickEvent.Type.RENDER) return;
		if(evt.phase != Phase.END) return;	
	
		if(mc.currentScreen instanceof GuiDownloadTerrain) {
			if(this.screenReassign) {
				this.assignScreenContent();
				this.screenReassign = false;
			}
			if(mc.world != null) {
				if(lastWorldUpdateTick != mc.world.getTotalWorldTime() % 240000) {
					
					lastWorldUpdateTick = mc.world.getTotalWorldTime() % 240000;
					
					if(lastWorldUpdateTick % 200 == 0) {
						assignScreenContent();
					}
				}
			}
			if(mc.player != null && mc.player.dimension == TropicraftWorldUtils.TROPICS_DIMENSION_ID) {
				if(screenEntities == null && mc.world != null){
					assignScreenContent();
				}
				FontRenderer f = mc.fontRendererObj;
				ScaledResolution sr = new ScaledResolution(mc);
				
				drawBackground(sr.getScaledWidth(), sr.getScaledHeight());
				if(screenEntities != null) {
					drawScreenEntity(screenEntities.getLeft(), sr.getScaledWidth()/2 - 120, (sr.getScaledHeight()/2)+60, 50, -90+(int)(this.lastWorldUpdateTick*2), 20);
					drawScreenEntity(screenEntities.getRight(), sr.getScaledWidth()/2 + 120, (sr.getScaledHeight()/2)+60, 50, 90-(int)(this.lastWorldUpdateTick*2), 20);
					
					drawScreenItemStack(screenItems.getLeft(), (sr.getScaledWidth()/2)-8-24, (sr.getScaledHeight()/2)+8, 1.5f);
					drawScreenItemStack(screenItems.getMiddle(), (sr.getScaledWidth()/2)-12, (sr.getScaledHeight()/2)+8, 1.5f);
					drawScreenItemStack(screenItems.getRight(), (sr.getScaledWidth()/2)-8+16, (sr.getScaledHeight()/2)+8, 1.5f);
				}
				String msg = I18n.translateToLocal("tropicraft.loading.title");
				GlStateManager.pushMatrix();
				GlStateManager.translate((sr.getScaledWidth())/2 - (f.getStringWidth(msg)/2), (sr.getScaledHeight()/4) - 30, 0);
				GlStateManager.scale(2f, 2f, 1f);
				GlStateManager.translate(-(f.getStringWidth(msg)/4), 0, 0);
				f.drawStringWithShadow(msg, 0, 0, 0x64fc97);
				GlStateManager.popMatrix();
		
				String subtitle = screenTitle;
				
				f.drawStringWithShadow(subtitle, (sr.getScaledWidth())/2 - (f.getStringWidth(subtitle)/2), ((sr.getScaledHeight()/1.2f)) + 5, 0xf7bf56);
						
				GlStateManager.color(1f, 1f, 1f, 1f);
				
						
			}
		}else {
			this.screenReassign = true;
		}
	}
	
	public ItemStack stack(Item i) {
		return new ItemStack(i);
	}
	
	public ItemStack stack(Block i) {
		return new ItemStack(i);
	}
	
	public void drawScreenItemStack(ItemStack i, float x, float y, float scale) {
		GlStateManager.pushMatrix();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate(x, y, 0);
		GlStateManager.scale(scale, scale, 1f);
		mc.getRenderItem().renderItemIntoGUI(i, 0, 0);
		GlStateManager.popMatrix();
	}
	
	private void drawScreenEntity(Entity ent, float x, float y, float scale, float yaw, float pitch) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 50.0F);
		GlStateManager.scale(-scale, scale, scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f,f1,f2,f3,f4;
		f=f1=f2=f3=f4=0;
		if(ent instanceof EntityTropicraftWaterBase) {
			EntityTropicraftWaterBase fish = (EntityTropicraftWaterBase) ent;
			fish.swimYaw = -yaw;
			fish.swimPitch = pitch*0.25f;
		}
		if(ent instanceof EntityLivingBase) {
			EntityLivingBase liv = (EntityLivingBase)ent;
			f = liv.renderYawOffset;
			f1 = liv.rotationYaw;
			f2 = liv.rotationPitch;
			f3 = liv.prevRotationYawHead;
			f4 = liv.rotationYawHead;
			liv.renderYawOffset = yaw;
			liv.rotationYaw = yaw;
			liv.rotationPitch = pitch;
			liv.rotationYawHead = ent.rotationYaw;
			liv.prevRotationYawHead = ent.rotationYaw;
		
		}
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float) Math.atan((double) (pitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = mc.getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		if(ent instanceof EntityLivingBase) {
			EntityLivingBase liv = (EntityLivingBase)ent;
			liv.renderYawOffset = f;
			liv.rotationYaw = f1;
			liv.rotationPitch = f2;
			liv.prevRotationYawHead = f3;
			liv.rotationYawHead = f4;
		}
		GlStateManager.popMatrix();
		
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
	
    public void drawBackground(float width, float height) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        TropicraftRenderUtils.bindTextureGui(screenBackground);
        GlStateManager.color(0.9F, 0.9F, 0.9F, 1.0F);
        float f = 128.0F;
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
     
        vertexbuffer.pos(0,     height, 0)	.tex(0,         height / f)	.color(0.5f, 0.5f, 0.5f, 1f).endVertex();
        vertexbuffer.pos(width, height, 0)	.tex(width / f, height / f)	.color(0.5f, 0.5f, 0.5f, 1f).endVertex();
        vertexbuffer.pos(width, 0,      0)	.tex(width / f, 0)			.color(0.5f, 0.5f, 0.5f, 1f).endVertex();
        vertexbuffer.pos(0,     0,      0)	.tex(0,         0)			.color(0.5f, 0.5f, 0.5f, 1f).endVertex();
		tessellator.draw();
	}
}
