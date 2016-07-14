package net.tropicraft.core.common.item.armor;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Info;

public class ItemTropicraftArmor extends ItemArmor implements ISpecialArmor {

	/** Name of the armor, eg "scale" or "fire", used in getArmorTexture */
	private String modArmorName;

	@SideOnly(Side.CLIENT)
	public static List[] fxLayers;

	public ItemTropicraftArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot equipmentSlotIn) {
		super(material, renderIndex, equipmentSlotIn);
		this.modArmorName = material.name();
	}

	protected String getTexturePath(String name) {
		return Info.ARMOR_LOCATION + name;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return getTexturePath(String.format("%s_layer_" + (slot == EntityEquipmentSlot.LEGS ? 2 : 1) + ".png", modArmorName));
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		return new ArmorProperties(10, source == DamageSource.inFire ? 1.0 : 0.3, Integer.MAX_VALUE);
	}

	/**
	 * Get the displayed effective armor.
	 *
	 * @param player The player wearing the armor.
	 * @param armor The ItemStack of the armor item itself.
	 * @param slot The armor slot the item is in.
	 * @return The number of armor points for display, 2 per shield.
	 */
	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 3;
	}

	/**
	 * Applies damage to the ItemStack. The mod is responsible for reducing the
	 * item durability and stack size. If the stack is depleted it will be cleaned
	 * up automatically.
	 *
	 * @param entity The entity wearing the armor
	 * @param armor The ItemStack of the armor item itself.
	 * @param source The source of the damage, which can be used to alter armor
	 *     properties based on the type or source of damage.
	 * @param damage The amount of damage being applied to the armor
	 * @param slot The armor slot the item is in.
	 */
	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		stack.damageItem(damage, entity);        
	}
}
