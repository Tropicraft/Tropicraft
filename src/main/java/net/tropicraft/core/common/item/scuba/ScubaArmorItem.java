package net.tropicraft.core.common.item.scuba;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.common.item.ArmorMaterials;
import net.tropicraft.core.common.item.TropicraftArmorItem;

public class ScubaArmorItem extends TropicraftArmorItem {
    
    private final ScubaType type;
     
    public ScubaArmorItem(ScubaType type, EquipmentSlotType slotType, Item.Properties properties) {
        super(ArmorMaterials.SCUBA, slotType, properties);
        this.type = type;
    }
    
    public ScubaType getType() {
        return type;
    }
    
    public boolean providesAir() {
        return false;
    }
    
    public void tickAir(PlayerEntity player, EquipmentSlotType slot, ItemStack stack) {
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
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return getArmorTexture(this.type).toString();
    }
    
    public static ResourceLocation getArmorTexture(ScubaType material) {
        return new ResourceLocation(Constants.ARMOR_LOCATION + "scuba_gear_" + material.getTextureName() + ".png");   
    }

    @Override
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemstack, EquipmentSlotType armorSlot, A _default) {
        if (itemstack.isEmpty()) {
            return null;
        }

        BipedModel<?> armorModel;
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

        armorModel.isSneak = entityLiving.isSneaking();
        armorModel.isChild = entityLiving.isChild();
        armorModel.rightArmPose = entityLiving.getHeldItemMainhand() != null ? BipedModel.ArmPose.BLOCK : BipedModel.ArmPose.EMPTY;
        return (A) armorModel;
    }
}
