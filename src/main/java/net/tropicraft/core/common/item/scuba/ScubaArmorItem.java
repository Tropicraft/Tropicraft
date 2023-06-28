package net.tropicraft.core.common.item.scuba;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tropicraft.Constants;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.common.item.ArmorMaterials;
import net.tropicraft.core.common.item.TropicraftArmorItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ScubaArmorItem extends TropicraftArmorItem {

    private static final ResourceLocation GOGGLES_OVERLAY_TEX_PATH = new ResourceLocation(Constants.MODID, "textures/gui/goggles.png");

    private final ScubaType type;

    public ScubaArmorItem(ScubaType type, ArmorItem.Type slotType, Item.Properties properties) {
        super(ArmorMaterials.SCUBA, slotType, properties);
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

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return getArmorTexture(this.type).toString();
    }

    public static ResourceLocation getArmorTexture(ScubaType material) {
        return new ResourceLocation(Constants.ARMOR_LOCATION + "scuba_gear_" + material.getTextureName() + ".png");
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                if (stack.isEmpty()) {
                    return null;
                }

                HumanoidModel<?> armorModel = getArmorModel(slot);
                if (armorModel != null) {
                    prepareModel(armorModel, entity);
                    return armorModel;
                } else {
                    return null;
                }
            }

            @Nullable
            private HumanoidModel<?> getArmorModel(EquipmentSlot armorSlot) {
                return switch (armorSlot) {
                    case HEAD -> ModelScubaGear.HEAD;
                    case CHEST -> ModelScubaGear.CHEST;
                    case FEET -> ModelScubaGear.FEET;
                    default -> null;
                };
            }

            @SuppressWarnings("unchecked")
            private <E extends LivingEntity> void prepareModel(HumanoidModel<E> armorModel, LivingEntity entity) {
                armorModel.prepareMobModel((E) entity, 0.0F, 0.0F, 1.0F);
                armorModel.crouching = entity.isShiftKeyDown();
                armorModel.young = entity.isBaby();
                armorModel.rightArmPose = entity.getMainHandItem() != null ? HumanoidModel.ArmPose.BLOCK : HumanoidModel.ArmPose.EMPTY;
            }

            @Override
            public void renderHelmetOverlay(ItemStack stack, Player player, int width, int height, float partialTicks) {
                if (stack.getItem() instanceof ScubaGogglesItem) {
                    Minecraft mc = Minecraft.getInstance();
                    GuiGraphics graphics = new GuiGraphics(mc, mc.renderBuffers().bufferSource());
                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                    RenderSystem.defaultBlendFunc();
                    graphics.blit(GOGGLES_OVERLAY_TEX_PATH, 0, 0, 0, 0, 0, width, height, width, height);
                    RenderSystem.depthMask(true);
                    RenderSystem.enableDepthTest();
                    graphics.flush();
                }
            }
        });
    }
}
