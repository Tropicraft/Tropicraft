package net.tropicraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.tropicraft.core.client.TropicraftRenderUtils;

public class GuiScubaHarness extends GuiContainer {
   
    public GuiScubaHarness(Container inventorySlotsIn) {
        super(inventorySlotsIn);
        
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        TropicraftRenderUtils.bindTextureGui("scubaHarness");
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        
    }

}
