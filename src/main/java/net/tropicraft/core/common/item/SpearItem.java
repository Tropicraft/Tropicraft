package net.tropicraft.core.common.item;

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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.projectile.SpearEntity;

public class SpearItem extends TridentItem {

	private final Tier tier;

	private final Multimap<Attribute, AttributeModifier> defaultModifiers;

	public SpearItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
		super(properties.defaultDurability(tier.getUses()));
		this.tier = tier;

		this.defaultModifiers = ImmutableMultimap.<Attribute, AttributeModifier>builder()
				.putAll(super.getAttributeModifiers(EquipmentSlot.MAINHAND, new ItemStack(this)))
				.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", attackDamage, AttributeModifier.Operation.ADDITION))
				.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeed, AttributeModifier.Operation.ADDITION))
				.build();
	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player) {
			Player player = (Player) entityLiving;
			int i = this.getUseDuration(stack) - timeLeft;
			if (i >= 10) {
				int j = EnchantmentHelper.getRiptide(stack);
				if (!worldIn.isClientSide) {
					stack.hurtAndBreak(1, player, (p_43388_) -> {
						p_43388_.broadcastBreakEvent(entityLiving.getUsedItemHand());
					});
					if (j == 0) {
						SpearEntity thrownspear = new SpearEntity(worldIn, player, stack);
						thrownspear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F + (float) j * 0.5F, 1.0F);
						if (player.getAbilities().instabuild) {
							thrownspear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
						}

						worldIn.addFreshEntity(thrownspear);
						worldIn.playSound((Player) null, thrownspear, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
						if (!player.getAbilities().instabuild) {
							player.getInventory().removeItem(stack);
						}
					}
				}

				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return this.tier.getEnchantmentValue();
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return this.tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
	}
}
