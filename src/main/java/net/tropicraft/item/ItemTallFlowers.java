package net.tropicraft.item;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;
import net.tropicraft.block.BlockTallFlowers;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTallFlowers extends ItemBlockTropicraft {

	public ItemTallFlowers(Block block, ArrayList<String> names) {
		super(block, names);
	}
	
    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return ((BlockTallFlowers)block).getIcon(0, damage);
    }
}
