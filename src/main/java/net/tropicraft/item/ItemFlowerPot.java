package net.tropicraft.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemReed;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;

public class ItemFlowerPot extends ItemReed {

	public ItemFlowerPot(Block block) {
		super(block);
	}
	
	@Override
    public void registerIcons(IIconRegister registry) {
        this.itemIcon = registry.registerIcon(TCInfo.ICON_LOCATION + TCNames.flowerPot);
    }
}
