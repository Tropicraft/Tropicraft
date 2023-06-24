package net.tropicraft.core.common.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.PlayerHeadpieceModel;

import java.util.function.Consumer;

public class NigelStacheItem extends ArmorItem {

    public NigelStacheItem(final Properties properties) {
        super(ArmorMaterials.NIGEL_STACHE, EquipmentSlot.HEAD, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                return NigelStacheItem.this.slot == EquipmentSlot.HEAD ? PlayerHeadpieceModel.createModel(TropicraftRenderLayers.STACHE_LAYER, null, 0, 0, 0) : null;
            }
        });
    }
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return TropicraftRenderUtils.getTextureArmor("nigel_layer_1").toString();
    }
}
