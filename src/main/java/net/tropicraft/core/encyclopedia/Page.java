package net.tropicraft.core.encyclopedia;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Info;

@ParametersAreNonnullByDefault
public interface Page {
    
    String getId();
    
    @SideOnly(Side.CLIENT)
    void drawHeader(int x, int y, float mouseX, float mouseY, float cycle);
    
    @SideOnly(Side.CLIENT)
    void drawIcon(int x, int y, float cycle);
    
    int getHeaderHeight();
    
    default String getTitle() {
        return Info.MODID + ".encyclopedia." + getId() + ".title";
    }
    
    @SideOnly(Side.CLIENT)
    default String getLocalizedTitle() {
        return "  " + I18n.format(getTitle());
    }
    
    default String getDescription() {
        return Info.MODID + ".encyclopedia." + getId() + ".desc";
    }
    
    @SideOnly(Side.CLIENT)
    default String getLocalizedDescription() {
        return I18n.format(getDescription());
    }
    
    default boolean isBookmark() {
        return false;
    }
    
    default boolean hasContent() {
        return !isBookmark();
    }
    
    default boolean hasIcon() {
        return !isBookmark();
    }
    
    List<RecipeEntry> getRelevantRecipes();
    
    /**
     * Return true if this page should be "discovered" at this time.
     */
    boolean discover(World world, EntityPlayer player);

}
