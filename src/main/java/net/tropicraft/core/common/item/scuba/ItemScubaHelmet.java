package net.tropicraft.core.common.item.scuba;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.TropicraftRenderUtils;

public class ItemScubaHelmet extends ItemScubaGear {

	public ItemScubaHelmet(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, EntityEquipmentSlot slot) {
		super(material, scubaMaterial, renderIndex, slot);

	}

	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		//TODO client side only: <Corosus> spawn some bubbles near them once in a while to simulating using the rebreather
	}

	/**
	 * Called when the client starts rendering the HUD, for whatever item the player currently has as a helmet. 
	 * This is where pumpkins would render there overlay.
	 * 
	 * @param stack The ItemStack that is equipped
	 * @param player Reference to the current client entity
	 * @param resolution Resolution information about the current viewport and configured GUI Scale
	 * @param partialTicks Partial ticks for the renderer, useful for interpolation
	 * @param hasScreen If the player has a screen up, which will be rendered after this.
	 * @param mouseX Mouse's X position on screen
	 * @param mouseY Mouse's Y position on screen
	 */
	@SideOnly(Side.CLIENT)
	public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks, boolean hasScreen, int mouseX, int mouseY) {
		// Check to see if player inventory contains dive computer
	    int width = resolution.getScaledWidth();
	    int height = resolution.getScaledHeight();
        GlStateManager.enableDepth();
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TropicraftRenderUtils.getTextureGui("snorkel"));
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(0.0D, (double)height, -90.0D).tex(0.0D, 1.0D).endVertex();
        vertexbuffer.pos((double)width, (double)height, -90.0D).tex(1.0D, 1.0D);
        vertexbuffer.pos((double)width, 0.0D, -90.0D).tex(1.0D, 0.0D);
        vertexbuffer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D);
        tessellator.draw();
        GL11.glDepthMask(true);
        GlStateManager.disableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		return null;
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {

	}

}
