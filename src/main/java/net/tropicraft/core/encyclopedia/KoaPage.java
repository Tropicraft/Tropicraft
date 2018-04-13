package net.tropicraft.core.encyclopedia;

import java.util.List;
import java.util.Locale;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tropicraft.Info;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.entity.passive.EntityKoaBase.Genders;
import net.tropicraft.core.registry.ItemRegistry;

@ParametersAreNonnullByDefault
public class KoaPage extends EntityPage {
    
    private final EntityKoaBase.Roles role;
    private final ItemStack inHand;

    public KoaPage(EntityKoaBase.Roles role, ItemStack inHand) {
        super("koa." + role.toString().toLowerCase(Locale.ROOT), new ResourceLocation(Info.MODID, "koa"), inHand);
        this.role = role;
        this.inHand = inHand;
    }
    
    @Override
    public void drawIcon(int x, int y, float cycle) {
        GlStateManager.color(0, 0, 0);
        // itemRenderer.renderWithColor = book.isPageVisible(selectedIndex);
        // TODO 1.12 ??
        // itemRenderer.isNotRenderingEffectsInGUI(book.isPageVisible(selectedIndex));
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(ItemRegistry.mobEgg, 1, 13), x, y);
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(inHand, x, y);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }
    
    @Override
    public void drawHeader(int x, int y, float mouseX, float mouseY, float cycle) {
        Genders[] genders = Genders.values();
        float mid = (genders.length - 1) / 2f;
        for (int i = 0; i < genders.length; i++) {
            EntityLivingBase entity = getEntity();
            if (entity != null) {
                ((EntityKoaBase)entity).setGender(genders[i]);
                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, inHand);
            }
            super.drawHeader((int) (x + ((i - mid) * 40)), y, mouseX, mouseY, cycle);
        }
    }
    
    @Override
    public boolean discover(World world, EntityPlayer player) {
        Vec3d pos = player.getPositionVector();
        List<? extends EntityKoaBase> inRange = world.getEntitiesWithinAABB(EntityKoaBase.class, new AxisAlignedBB(pos.addVector(-5, -5, -5), pos.addVector(5, 5, 5)));
        return inRange.stream().filter(e -> e.getRole() == role).count() > 0;
    }
}
