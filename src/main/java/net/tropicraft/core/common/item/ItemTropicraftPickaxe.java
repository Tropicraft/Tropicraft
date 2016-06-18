package net.tropicraft.core.common.item;

import net.minecraft.item.ItemPickaxe;

/**
 * This class literally only exists because ItemPickaxe's constructor is protected. Go figure!
 * @author Cojo
 *
 */
public class ItemTropicraftPickaxe extends ItemPickaxe {
	
	public ItemTropicraftPickaxe(ToolMaterial material) {
		super(material);
	}
}
