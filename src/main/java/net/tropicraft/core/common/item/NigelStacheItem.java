package net.tropicraft.core.common.item;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.PlayerHeadpieceRenderer;

import javax.annotation.Nullable;

public class NigelStacheItem extends ArmorItem {

    public NigelStacheItem(final Properties properties) {
        super(ArmorMaterials.NIGEL_STACHE, EquipmentSlotType.HEAD, properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public BipedModel getArmorModel(final LivingEntity entityLiving, final ItemStack itemStack, final EquipmentSlotType armorSlot, final BipedModel model) {
        return armorSlot == EquipmentSlotType.HEAD ? new PlayerHeadpieceRenderer(0, TropicraftRenderUtils.getTextureArmor("nigel_layer_1")) : null;
    }
}
