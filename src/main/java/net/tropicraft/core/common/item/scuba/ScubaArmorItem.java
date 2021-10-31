package net.tropicraft.core.common.item.scuba;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.tropicraft.Constants;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.common.item.ArmorMaterials;
import net.tropicraft.core.common.item.TropicraftArmorItem;

import javax.annotation.Nullable;

public class ScubaArmorItem extends TropicraftArmorItem implements IItemRenderProperties {
    
    private final ScubaType type;

    public ScubaArmorItem(ScubaType type, EquipmentSlot slotType, Item.Properties properties) {
        super(ArmorMaterials.SCUBA, slotType, properties);
        this.type = type;
    }
    
    public ScubaType getType() {
        return type;
    }
    
    public boolean providesAir() {
        return false;
    }
    
    public void tickAir(Player player, EquipmentSlot slot, ItemStack stack) {
    }

    public int addAir(int air, ItemStack stack) {
        return 0;
    }
    
    public int getRemainingAir(ItemStack stack) {
        return 0;
    }
    
    public int getMaxAir(ItemStack stack) {
        return 0;
    }
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return getArmorTexture(this.type).toString();
    }
    
    public static ResourceLocation getArmorTexture(ScubaType material) {
        return new ResourceLocation(Constants.ARMOR_LOCATION + "scuba_gear_" + material.getTextureName() + ".png");   
    }

    @Override
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemstack, EquipmentSlot armorSlot, A _default) {
        if (itemstack.isEmpty()) {
            return null;
        }

        HumanoidModel<?> armorModel;
        switch (armorSlot) {
        case HEAD:
            armorModel = ModelScubaGear.HEAD;
            break;
        case CHEST:
            armorModel = ModelScubaGear.CHEST;
            break;
        case FEET:
            armorModel = ModelScubaGear.FEET;
            break;
        default:
            return null;
        }

        ((HumanoidModel) armorModel).prepareMobModel(entityLiving, 0.0F, 0.0F, 1.0F);

        armorModel.crouching = entityLiving.isShiftKeyDown();
        armorModel.young = entityLiving.isBaby();
        armorModel.rightArmPose = entityLiving.getMainHandItem() != null ? HumanoidModel.ArmPose.BLOCK : HumanoidModel.ArmPose.EMPTY;
        return (A) armorModel;
    }
}
