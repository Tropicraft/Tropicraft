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
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.PlayerHeadpieceModel;

import java.util.function.Consumer;

public class NigelStacheItem extends ArmorItem {

    public NigelStacheItem(final Properties properties) {
        super(TropicraftArmorMaterials.NIGEL_STACHE, Type.HELMET, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                return NigelStacheItem.this.type == Type.HELMET ? PlayerHeadpieceModel.createModel(TropicraftRenderLayers.HEADPIECE_LAYER, null, 0, 0, 0) : null;
            }
        });
    }

    @Override
    public ResourceLocation getArmorTexture(final ItemStack stack, final Entity entity, final EquipmentSlot slot, final ArmorMaterial.Layer layer, final boolean innerModel) {
        return TropicraftRenderUtils.getTextureArmor("nigel_layer_1");
    }
}
