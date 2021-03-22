package net.tropicraft.core.common.item.scuba;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import net.minecraftforge.common.ForgeMod;
import net.tropicraft.Constants;

import java.util.UUID;

public class ScubaFlippersItem extends ScubaArmorItem {

    private static final AttributeModifier SWIM_SPEED_BOOST = new AttributeModifier(UUID.fromString("d0b3c58b-ff33-41f2-beaa-3ffa15e8342b"), Constants.MODID + ".scuba", 0.25, Operation.MULTIPLY_TOTAL);

    private final LazyValue<Multimap<Attribute, AttributeModifier>> boostedModifiers;

    public ScubaFlippersItem(ScubaType type, Properties properties) {
        super(type, EquipmentSlotType.FEET, properties);

        this.boostedModifiers = new LazyValue<>(() ->
                ImmutableMultimap.<Attribute, AttributeModifier>builder()
                        .putAll(super.getAttributeModifiers(EquipmentSlotType.FEET, new ItemStack(this)))
                        .put(ForgeMod.SWIM_SPEED.get(), SWIM_SPEED_BOOST)
                        .build()
        );
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        if (slot == EquipmentSlotType.FEET && EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack) == 0) {
            return boostedModifiers.getValue();
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }
}
