package net.tropicraft.core.common.item;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TropicraftArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> REGISTER = DeferredRegister.create(Registries.ARMOR_MATERIAL, Constants.MODID);

    // Ignored, replaced with custom models with IClientItemExtensions
    private static final List<ArmorMaterial.Layer> DUMMY_LAYERS = List.of(new ArmorMaterial.Layer(
            ResourceLocation.fromNamespaceAndPath(Constants.MODID, "dummy")
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ASHEN_MASK = REGISTER.register("ashen_mask", () -> new ArmorMaterial(
            Map.of(
                    ArmorItem.Type.HELMET, 1
            ),
            15,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(TropicraftTags.Items.ASHEN_MASKS),
            DUMMY_LAYERS,
            0,
            0
    ));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> NIGEL_STACHE = REGISTER.register("nigel_stache", () -> new ArmorMaterial(
            Map.of(
                    ArmorItem.Type.HELMET, 1
            ),
            15,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(TropicraftItems.NIGEL_STACHE.get()),
            DUMMY_LAYERS,
            0,
            0
    ));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SCALE_ARMOR = REGISTER.register("scale_armor", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 6);
                map.put(ArmorItem.Type.HELMET, 2);
            }),
            9,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            () -> Ingredient.of(TropicraftItems.SCALE.get()),
            List.of(new ArmorMaterial.Layer(
                    ResourceLocation.fromNamespaceAndPath(Constants.MODID, "scale")
            )),
            0.5f,
            0.0f
    ));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FIRE_ARMOR = REGISTER.register("fire_armor", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 4);
                map.put(ArmorItem.Type.CHESTPLATE, 5);
                map.put(ArmorItem.Type.HELMET, 2);
            }),
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(TropicraftTags.Items.REPAIRS_FIRE_ARMOR),
            List.of(new ArmorMaterial.Layer(
                    ResourceLocation.fromNamespaceAndPath(Constants.MODID, "fire")
            )),
            0.1f,
            0.0f
    ));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SCUBA = REGISTER.register("scuba", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 0);
                map.put(ArmorItem.Type.LEGGINGS, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 0);
                map.put(ArmorItem.Type.HELMET, 0);
            }),
            0,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            () -> Ingredient.of(TropicraftTags.Items.REPAIRS_SCUBA_GEAR),
            DUMMY_LAYERS,
            0,
            0.0f
    ));
}
