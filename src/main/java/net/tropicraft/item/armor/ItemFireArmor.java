package net.tropicraft.item.armor;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class ItemFireArmor extends ItemTropicraftArmor {

	public ItemFireArmor(ArmorMaterial material, int renderIndex, int armorType) {
		super(material, renderIndex, armorType);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return getTexturePath("fire_" + type);
	}

}
