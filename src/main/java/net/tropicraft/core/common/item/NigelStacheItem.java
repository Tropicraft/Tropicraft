package net.tropicraft.core.common.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.PlayerHeadpieceModel;

public class NigelStacheItem extends ArmorItem {
    private static final ResourceLocation TEXTURE_LOCATION = Tropicraft.location("textures/models/armor/nigel_layer_1.png");

    public NigelStacheItem(Properties properties) {
        super(TropicraftArmorMaterials.NIGEL_STACHE, Type.HELMET, properties);
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return TEXTURE_LOCATION;
    }

    static class ClientExtensions implements IClientItemExtensions {
        @Override
        public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
            return PlayerHeadpieceModel.createModel(TropicraftRenderLayers.HEADPIECE_LAYER, null, 0, 0, 0);
        }
    }
}
