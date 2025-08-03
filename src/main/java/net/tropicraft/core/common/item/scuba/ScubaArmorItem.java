package net.tropicraft.core.common.item.scuba;

import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.tropicraft.core.client.scuba.ModelScubaGear;

public class ScubaArmorItem extends Item {
    private final ScubaType type;

    public ScubaArmorItem(ScubaType type, Item.Properties properties) {
        super(properties);
        this.type = type;
    }

    public ScubaType getScubaType() {
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

    public static class ClientExtensions implements IClientItemExtensions {
        private static final ModelScubaGear HEAD_MODEL = new ModelScubaGear(ModelScubaGear.createGoggles().bakeRoot());
        private static final ModelScubaGear CHEST_MODEL = new ModelScubaGear(ModelScubaGear.createHarness().bakeRoot());
        private static final ModelScubaGear FEET_MODEL = new ModelScubaGear(ModelScubaGear.createFlippers().bakeRoot());

        @Override
        public Model getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original) {
            Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
            EquipmentSlot slot = equippable != null ? equippable.slot() : null;
            return switch (slot) {
                case HEAD -> HEAD_MODEL;
                case CHEST -> CHEST_MODEL;
                case FEET -> FEET_MODEL;
                case null, default -> original;
            };
        }

        @Override
        public ResourceLocation getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, ResourceLocation _default) {
            return layer.textureId();
        }
    }
}
