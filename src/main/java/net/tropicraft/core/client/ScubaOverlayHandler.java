package net.tropicraft.core.client;

import java.time.Duration;

import org.apache.commons.lang3.time.DurationFormatUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tropicraft.core.common.item.scuba.ItemScubaHelmet;
import net.tropicraft.core.common.item.scuba.ScubaCapabilities;
import net.tropicraft.core.common.item.scuba.api.IScubaGear;
import net.tropicraft.core.common.item.scuba.api.IScubaTank;

public class ScubaOverlayHandler extends Gui {

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        // Check to see if player inventory contains dive computer
        float airRemaining = -1, airTemp, timeRemaining = 0, yaw;
        int blocksAbove, blocksBelow, currentDepth, maxDepth;
        
        EntityPlayer player = Minecraft.getMinecraft().player;
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        
        if (helmet == null || !(helmet.getItem() instanceof ItemScubaHelmet)) {
            return;
        }

        ItemStack chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        IScubaGear gear = chestplate != null ? chestplate.getCapability(ScubaCapabilities.getGearCapability(), null) : null;

        maxDepth = getTagCompound(helmet).getInteger("MaxDepth");
        blocksAbove = getTagCompound(helmet).getInteger("WaterBlocksAbove");
        blocksBelow = getTagCompound(helmet).getInteger("WaterBlocksBelow");
        if (gear != null) {
            airRemaining = gear.getTotalPressure();
            timeRemaining += getTimeRemaining(gear.getTanks().getLeft());
            timeRemaining += getTimeRemaining(gear.getTanks().getRight());
        }
        airTemp = player.world.getBiomeForCoordsBody(new BlockPos(MathHelper.floor(player.posX), 0, MathHelper.floor(player.posZ))).getTemperature(player.getPosition());

        yaw = player.rotationYaw;
        
        int size = 100;
        int offset = -size / 2;
        GlStateManager.pushMatrix();
        GlStateManager.translate(sr.getScaledWidth() + offset - 20, 120, 0);
        GlStateManager.rotate(yaw + 180, 0.0F, 0.0F, -1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TropicraftRenderUtils.getTextureGui("compass_background"));
        drawModalRectWithCustomSizedTexture(offset, offset, 0, 0, size, size, size, size);
        GlStateManager.popMatrix();

        //TODO make locations of text configurable
        GlStateManager.pushMatrix();
        if (gear != null) {
            drawString(fr, "psi", sr.getScaledWidth() - 50, 48, 0xffffff);
            //TODO display warning if air is running low / out
            drawString(fr, "Air", sr.getScaledWidth() - 75, 34, 0xffffff);   
        }
        GlStateManager.scale(1.5F, 1.5F, 1.0F);
        if (gear != null) {
            String psi = String.format("%.0f", airRemaining);
            drawString(fr, psi, (int) ((sr.getScaledWidth() - 50) / 1.5f) - fr.getStringWidth(psi) - 1, 30, 0x00ccde);   
        }
        drawString(fr, TropicraftRenderUtils.translateGUI("currentDepth") + ": " + blocksAbove, 4, 70, 0xbbbbff);  // Current depth
        GlStateManager.popMatrix();

        drawString(fr, TropicraftRenderUtils.translateGUI("maxDepth") + ": " + maxDepth, 6, 130, 0xffffffff);
        drawString(fr, (airTemp * 50) + " F", 6, 150, 0xffffffff);

        if (gear != null) {
            String time = DurationFormatUtils.formatDuration(((long) (timeRemaining * 1000) / 60) * 60, "H' hr 'm' mins'");
            drawString(fr, TropicraftRenderUtils.translateGUI("timeRemaining"), 6, 34, 0xffffff);
            drawString(fr, time, 6, 45, 0xF6EB12);   
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    private float getTimeRemaining(IScubaTank tank) {
        if (tank != null) {
            return (tank.getPressure() / tank.getAirType().getUsageRate());
        }
        return 0;
    }
    
    private NBTTagCompound getTagCompound(ItemStack stack) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        return stack.getTagCompound();
    }
}
