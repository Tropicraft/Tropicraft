package net.tropicraft.core.common.item.scuba;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.item.scuba.api.IScubaGear;
import net.tropicraft.core.common.item.scuba.api.IScubaTank;
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
        // Check to see if player inventory contains dive computer
        float airRemaining = -1, airTemp, timeRemaining = 0, yaw;
        int blocksAbove, blocksBelow, currentDepth, maxDepth, heading;

        ItemStack chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        IScubaGear gear = chestplate != null ? chestplate.getCapability(ScubaCapabilities.getGearCapability(), null) : null;

        maxDepth = getTagCompound(stack).getInteger("MaxDepth");
        blocksAbove = getTagCompound(stack).getInteger("WaterBlocksAbove");
        blocksBelow = getTagCompound(stack).getInteger("WaterBlocksBelow");
        if (gear != null) {
            airRemaining = gear.getTotalPressure();
            timeRemaining += getTimeRemaining(gear.getTanks().getLeft());
            timeRemaining += getTimeRemaining(gear.getTanks().getRight());
        }
        airTemp = player.world.getBiomeForCoordsBody(new BlockPos(MathHelper.floor(player.posX), 0, MathHelper.floor(player.posZ))).getTemperature();

        yaw = player.rotationYaw;
        heading = MathHelper.floor((double)(yaw * 4.0F / 360.0F) + 0.5D) & 3;

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
        
        GlStateManager.pushMatrix();
        GlStateManager.glNormal3f(0.0F, 0.0F, 1.0F);
        GlStateManager.scale(0.45F, 0.45F, 1.0F);
        GlStateManager.translate(875.0F, 220.0F, 0.0F);
        GlStateManager.rotate(yaw + 180, 0.0F, 0.0F, -1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TropicraftRenderUtils.getTextureGui("compassBackground"));
        vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        int offset = -75;
        int size = 150;
        vertexbuffer.pos((double)(0 + offset), (double)(size + offset), 0.0D).tex(0.0D, 1.0D).endVertex();
        vertexbuffer.pos((double)(size + offset), (double)(size + offset), 0.0D).tex(1.0D, 1.0D).endVertex();
        vertexbuffer.pos((double)(size + offset), (double)(0 + offset), 0.0D).tex(1.0D, 0.0D).endVertex();
        vertexbuffer.pos((double)(0 + offset), (double)(0 + offset), 0.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();

        //TODO make locations of text configurable
        GlStateManager.pushMatrix();
        if (gear != null) {
            drawString("psi", 398, 48, 0xffffff);
            //TODO display warning if air is running low / out
            drawString("Air", 374, 34, 0xffffff);   
        }
        GlStateManager.scale(1.5F, 1.5F, 1.0F);
        if (gear != null) {
            Minecraft.getMinecraft().fontRendererObj.drawString(String.format("%.0f", airRemaining), 240, 30, 0x00ccde);   
        }
        drawString(TropicraftRenderUtils.translateGUI("currentDepth") + ": " + blocksAbove, 4, 70, 0xbbbbff);  // Current depth
        GlStateManager.popMatrix();

        drawString(TropicraftRenderUtils.translateGUI("maxDepth") + ": " + maxDepth, 6, 130, 0xffffffff);
        drawString(airTemp + " F", 6, 150, 0xffffffff);

        if (gear != null) {
            // TODO localize
            String timeUnits = timeRemaining <= 60 ? "secs" : "mins";
            timeRemaining = timeRemaining <= 60 ? timeRemaining : timeRemaining / 60;

            drawString(TropicraftRenderUtils.translateGUI("timeRemaining"), 30, 34, 0xffffff);
            drawString(String.format("%.0f %s", timeRemaining, timeUnits), 33, 45, 0xF6EB12);   
        }

        GlStateManager.depthMask(true);
        GlStateManager.disableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    private float getTimeRemaining(IScubaTank tank) {
        if (tank != null) {
            return (tank.getPressure() / tank.getAirType().getUsageRate());
        }
        return 0;
    }

    private void drawString(Object text, int x, int y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawString(String.valueOf(text), x, y, color);
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
