package net.tropicraft.core.encyclopedia;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.tropicraft.Info;

@ParametersAreNonnullByDefault
public class EntityPage extends ItemPage {
    
    private static final ResourceLocation PANEL_BG = new ResourceLocation(Info.MODID, "textures/blocks/thatch_side.png");
    
    private final ResourceLocation entityId;
    
    private @Nullable EntityLivingBase entity;
    
    public EntityPage(String id, ItemStack icon) {
        this(id, new ResourceLocation(Info.MODID, id), icon);
    }

    public EntityPage(String id, ResourceLocation entityId, ItemStack icon) {
        super(id, icon);
        this.entityId = entityId;
    }
    
    protected ResourceLocation getEntityId() {
        return entityId;
    }
    
    protected EntityLivingBase makeEntity() {
        EntityEntry entry = ForgeRegistries.ENTITIES.getValue(entityId);
        EntityLivingBase ret = (EntityLivingBase) entry.newInstance(Minecraft.getMinecraft().world);
        // Hack some extra width in for bipeds
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        if (rendermanager.getEntityRenderObject(ret) instanceof RenderBiped) {
            ret.width += 0.35;
        }
        return ret;
    }
    
    @Nullable
    protected EntityLivingBase getEntity() {
        return entity;
    }

    @Override
    public void drawHeader(int x, int y, float mouseX, float mouseY, float cycle) {
        super.drawHeader(x, y, mouseX, mouseY, cycle);
       
        if (entity == null) {
            entity = makeEntity();
        }

        GlStateManager.color(1, 1, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(PANEL_BG);
        
        int bgX = x + 14;
        int bgY = y;
        int bgWidth = 106;
        int bgHeight = getHeaderHeight() + 10;
        Gui.drawScaledCustomSizeModalRect(bgX, bgY, 0, 0, bgWidth, bgHeight, bgWidth, bgHeight, 16, 16);
        Gui.drawRect(bgX, bgY, bgX + bgWidth, bgY + bgHeight, 0x55444444);
        
        GlStateManager.color(1, 1, 1);
        x += 67;
        y -= 2;
        GuiInventory.drawEntityOnScreen(x, y + getHeaderHeight() + 5, 30, x - mouseX, y - mouseY + 10, getEntity());
    }
    
    @Override
    public int getHeaderHeight() {
        return (int) (entity == null ? 0 : entity.height * 33);
    }
}
