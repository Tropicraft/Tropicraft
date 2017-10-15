package net.tropicraft.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;

public class GuiScubaHarness extends GuiContainer {

    public GuiScubaHarness(Container inventorySlotsIn) {
        super(inventorySlotsIn);
        
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // Draw outer book cover //
        float f1 = 1.35F;
        ResourceLocation DISPENSER_GUI_TEXTURES = new ResourceLocation("textures/gui/container/dispenser.png");
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        //TropicraftRenderUtils.bindTextureGui("encyclopediaTropica");
        this.mc.getTextureManager().bindTexture(DISPENSER_GUI_TEXTURES);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        
    }

}
