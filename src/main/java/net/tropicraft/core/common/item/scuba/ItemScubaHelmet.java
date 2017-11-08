package net.tropicraft.core.common.item.scuba;

import net.minecraft.client.Minecraft;
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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.item.scuba.api.ScubaMaterial;

public class ItemScubaHelmet extends ItemScubaGear {

    private final int TICKS_TO_BUBBLE = 60;
    private int ticksSinceBubbles = 0;

    public ItemScubaHelmet(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, EntityEquipmentSlot slot) {
        super(material, scubaMaterial, renderIndex, slot);
        ticksSinceBubbles = TICKS_TO_BUBBLE;
    }

    @Override
    public void onScubaTick(World world, EntityPlayer player, ItemStack itemStack) {
        ticksSinceBubbles--;
        //TODO client side only: <Corosus> spawn some bubbles near them once in a while to simulating using the rebreather

        if (world.isRemote) {
            if (world.rand.nextBoolean() && ticksSinceBubbles <= 0 && ScubaHelper.isFullyUnderwater(world, player)) {
                for (int i = 0; i < world.rand.nextInt(4) + 12; i++) {
                    world.spawnParticle(EnumParticleTypes.WATER_BUBBLE,
                            player.posX + (world.rand.nextDouble() - 0.5D) * (double)player.width,
                            player.posY + world.rand.nextDouble() + (double)player.height,
                            player.posZ + (world.rand.nextDouble() - 0.5D) * (double)player.width,
                            0.5D, 0.5D, 0.5D, new int[0]);
                }
                ticksSinceBubbles = TICKS_TO_BUBBLE;
            }
        }
        
//        if (!player.isPotionActive(MobEffects.NIGHT_VISION)) {
//            player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 200));
//        }
    }

    /**
     * Called when the client starts rendering the HUD, for whatever item the player currently has as a helmet. 
     * This is where pumpkins would render there overlay.
     * 
     * @param stack The ItemStack that is equipped
     * @param player Reference to the current client entity
     * @param resolution Resolution information about the current viewport and configured GUI Scale
     * @param partialTicks Partial ticks for the renderer, useful for interpolation
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, net.minecraft.client.gui.ScaledResolution resolution, float partialTicks) {
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TropicraftRenderUtils.getTextureGui("snorkel"));
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(0.0D, (double)height, -90.0D).tex(0.0D, 1.0D).endVertex();
        vertexbuffer.pos((double)width, (double)height, -90.0D).tex(1.0D, 1.0D).endVertex();;
        vertexbuffer.pos((double)width, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();;
        vertexbuffer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();;
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {

    }

    @Override
    protected void onRemovedFromArmorInventory(World world, EntityPlayer player, ItemStack itemstack) {
        
    }

}
