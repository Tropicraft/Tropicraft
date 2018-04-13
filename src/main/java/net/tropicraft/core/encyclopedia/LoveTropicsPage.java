package net.tropicraft.core.encyclopedia;

import java.util.Arrays;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Names;
import net.tropicraft.core.client.TropicraftRenderUtils;

public class LoveTropicsPage extends SimplePage {

    public LoveTropicsPage(String id) {
        super(id);
    }


    @SideOnly(Side.CLIENT)
    public String getLocalizedDescription() {
        StringBuilder sb = new StringBuilder();

        String[] names = Arrays.copyOf(Names.LOVE_TROPICS_NAMES, Names.LOVE_TROPICS_NAMES.length);
        Arrays.sort(names);

        for (String name : names) {
            sb.append(name + "\n");
        }

        return sb.toString();
    }

    @Override
    public void drawHeader(int x, int y, float mouseX, float mouseY, float cycle) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        TropicraftRenderUtils.bindTextureGui("lovetropics");
        drawScaledCustomSizeModalRect(x, y - 10, 0, 0, 825, 216, 125, 33, 825, 216);
    }

    @Override
    public int getHeaderHeight() {
        return 30;
    }

    @Override
    public void drawIcon(int x, int y, float cycle) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        TropicraftRenderUtils.bindTextureGui("ltheart");
        drawScaledCustomSizeModalRect(x, y, 0, 0, 240, 240, 16, 16, 240, 240);
    }

    /**
     * Draws a scaled, textured, tiled modal rect at z = 0. This method isn't used anywhere in vanilla code.
     */
    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)vHeight) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)uWidth) * f), (double)((v + (float)vHeight) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)uWidth) * f), (double)(v * f1)).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }

    @Override
    public boolean discover(World world, EntityPlayer player) {
        // Always be known
        return true;
    }
}
