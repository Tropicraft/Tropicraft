package net.tropicraft.core.encyclopedia;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@ParametersAreNonnullByDefault
public class SectionPage extends SimplePage {

    public SectionPage(String id) {
        super(id);
    }

    @Override
    public boolean isBookmark() {
        return true;
    }
    
    @Override
    public String getTitle() {
        return super.getTitle().replace("encyclopedia", "encyclopedia.section");
    }
    
    @Override
    public boolean discover(World world, EntityPlayer player) {
        return true;
    }
}
