package net.tropicraft.core.common.item;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.projectile.SpearEntity;

import java.util.function.Supplier;

public class SpearItem extends TridentItem {

	private final Tier tier;

	private final Supplier<Multimap<Attribute, AttributeModifier>> defaultModifiers;

	public SpearItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
		super(properties.defaultDurability(tier.getUses()));
		this.tier = tier;

		this.defaultModifiers = Suppliers.memoize(() -> ImmutableMultimap.<Attribute, AttributeModifier>builder()
				.putAll(super.getAttributeModifiers(EquipmentSlot.MAINHAND, new ItemStack(this)))
				.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", attackDamage, AttributeModifier.Operation.ADDITION))
				.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeed, AttributeModifier.Operation.ADDITION))
				.build());
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (entity instanceof Player player) {
			int i = this.getUseDuration(stack) - timeLeft;
			if (i >= 10) {
				if (!level.isClientSide) {
					stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(entity.getUsedItemHand()));

					SpearEntity spear = new SpearEntity(level, player, stack);
					spear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
					if (player.getAbilities().instabuild) {
						spear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					}

					level.addFreshEntity(spear);
					level.playSound(null, spear, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
					if (!player.getAbilities().instabuild) {
						player.getInventory().removeItem(stack);
					}

				}

				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers.get() : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public int getEnchantmentValue(ItemStack stack) {
		return this.tier.getEnchantmentValue();
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment != Enchantments.RIPTIDE && super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return this.tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
	}
}
