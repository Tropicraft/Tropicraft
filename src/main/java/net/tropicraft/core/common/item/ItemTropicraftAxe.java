package net.tropicraft.core.common.item;

import net.minecraft.item.ItemAxe;

/**
 * This class literally only exists because ItemAxe's constructor is protected. Go figure!
 * @author Cojo
 *
 */
public class ItemTropicraftAxe extends ItemAxe {

	/**
	 * And we have to override this constructor in particular because the other one
	 * won't work because our enum ordinal for our custom tool materials is
	 * beyond what ATTACK_DAMAGES can take.
	 * @param material
	 * @param damage
	 * @param speed
	 */
	public ItemTropicraftAxe(ToolMaterial material, float damage, float speed) {
		super(material, damage, speed);
	}

}
