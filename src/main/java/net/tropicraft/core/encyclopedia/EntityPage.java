package net.tropicraft.core.encyclopedia;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.tropicraft.Info;

@ParametersAreNonnullByDefault
public class EntityPage extends ItemPage {
    
    private final ResourceLocation entityId;
    
    private @Nullable EntityLivingBase entity;
    
    public EntityPage(String id, ItemStack icon) {
        this(id, new ResourceLocation(Info.MODID, id), icon);
    }

    public EntityPage(String id, ResourceLocation entityId, ItemStack icon) {
        super(id, icon);
        this.entityId = entityId;
    }
    
    protected EntityLivingBase makeEntity() {
        EntityEntry entry = ForgeRegistries.ENTITIES.getValue(entityId);
        return (EntityLivingBase) entry.newInstance(Minecraft.getMinecraft().world);
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
        x += 67;
        y -= 2;
        GuiInventory.drawEntityOnScreen(x, y, 30, x - mouseX, y - mouseY - 50, entity);
    }
    
    @Override
    public int getHeaderHeight() {
        return (int) (entity == null ? 0 : entity.height * 33);
    }
}
