package net.tropicraft.core.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public class SpearItem extends TridentItem {

	private final IItemTier tier;

	private final Multimap<Attribute, AttributeModifier> defaultModifiers;

	public SpearItem(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
		super(properties.defaultMaxDamage(tier.getMaxUses()));
		this.tier = tier;

		this.defaultModifiers = ImmutableMultimap.<Attribute, AttributeModifier>builder()
				.putAll(super.getAttributeModifiers(EquipmentSlotType.MAINHAND, new ItemStack(this)))
				.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", attackDamage, AttributeModifier.Operation.ADDITION))
				.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", attackSpeed, AttributeModifier.Operation.ADDITION))
				.build();
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		// TODO
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		return slot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return this.tier.getEnchantability();
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return this.tier.getRepairMaterial().test(repair) || super.getIsRepairable(toRepair, repair);
	}
}
