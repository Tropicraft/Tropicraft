package net.tropicraft.core.common.item;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Optional;

public class DaggerItem {
    public static Item.Properties applyProperties(ToolMaterial material, Item.Properties properties) {
        return properties
                .durability(material.durability())
                .repairable(material.repairItems())
                .enchantable(material.enchantmentValue())
                .component(DataComponents.TOOL, createToolProperties())
                .component(DataComponents.ATTRIBUTE_MODIFIERS, createAttributes(material))
                .component(DataComponents.WEAPON, new Weapon(1))
                .component(DataComponents.BLOCKS_ATTACKS, new BlocksAttacks(
                        0.0f,
                        0.0f,
                        List.of(new BlocksAttacks.DamageReduction(90.0f, Optional.empty(), 0.0f, 0.0f)),
                        new BlocksAttacks.ItemDamageFunction(0, 0.0f, 0.0f),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                ));
    }

    private static ItemAttributeModifiers createAttributes(ToolMaterial material) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, material.attackDamageBonus() + 2.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, 0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    private static Tool createToolProperties() {
        HolderGetter<Block> blocks = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return new Tool(
                List.of(
                        Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 15.0f),
                        Tool.Rule.overrideSpeed(blocks.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE),
                        Tool.Rule.overrideSpeed(blocks.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5f)
                ),
                1.0f,
                0,
                false
        );
    }
}
