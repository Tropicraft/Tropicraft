package net.tropicraft.core.common.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.PlayerHeadpieceRenderer;

import javax.annotation.Nullable;

import net.minecraft.world.item.Item.Properties;

public class NigelStacheItem extends ArmorItem implements IItemRenderProperties {

    public NigelStacheItem(final Properties properties) {
        super(ArmorMaterials.NIGEL_STACHE, EquipmentSlot.HEAD, properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public HumanoidModel getArmorModel(final LivingEntity entityLiving, final ItemStack itemStack, final EquipmentSlot armorSlot, final HumanoidModel model) {
        return armorSlot == EquipmentSlot.HEAD ? new PlayerHeadpieceRenderer(0) : null;
    }
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return TropicraftRenderUtils.getTextureArmor("nigel_layer_1").toString();
    }
}
