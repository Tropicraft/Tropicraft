package net.tropicraft.core.encyclopedia;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@ParametersAreNonnullByDefault
public class SimplePage implements Page {
    
    private final String id;
    
    public SimplePage(String id) {
        this.id = id;
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void drawHeader(int x, int y, float partialTick) {}
    
    @Override
    public void drawIcon(int x, int y, float partialTick) {}

    @Override
    public int getHeaderHeight() {
        return 0;
    }
    
    @Override
    public List<RecipeEntry> getRelevantRecipes() {
        return Collections.emptyList();
    }

    @Override
    public boolean discover(World world, EntityPlayer player) {
        return false;
    }
    
    @Override
    public String toString() {
        return id;
    }
}
