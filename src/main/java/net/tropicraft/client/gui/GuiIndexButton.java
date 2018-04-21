package net.tropicraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.text.TextFormatting;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.encyclopedia.Page;

public class GuiIndexButton extends GuiClearButton {
    
    private final GuiTropicalBook parent;
    private final Page page;

    public GuiIndexButton(GuiTropicalBook parent, Page page, int id, int x, int y, int width, int height, String displayString, int type, String img, int textColor) {
        super(id, x, y, width, height, (page.hasIcon() ? "     " : "") + displayString, type, img, textColor);
        if (page.isBookmark()) {
            this.displayString = TextFormatting.UNDERLINE + this.displayString;
        }
        this.parent = parent;
        this.page = page;
    }

    public Page getPage() {
        return page;
    }
    
    @Override
    public void drawButton(Minecraft minecraft, int i, int j, float partialTicks) {
        super.drawButton(minecraft, page.isBookmark() ? -1000 : i, page.isBookmark() ? -1000 : j, partialTicks);
        if (visible && page.hasIcon()) {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            TropicraftRenderUtils.bindTextureGui("encyclopedia_tropica_inside");
            drawTexturedModalRect(x, y - 3, 3, 190, 18, 18);
            GlStateManager.enableBlend();
            drawRect(x + 1, y - 2, x + 17, y + 14, 0x88666666);
            GlStateManager.popMatrix();

            GlStateManager.enableLighting();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.color(0, 0, 0);

            page.drawIcon(x + 1, y - 2, parent.recipeCycle);
        }
    }
}
