package net.tropicraft.core.common.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.PlayerHeadpieceModel;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;

public class AshenMaskItem extends ArmorItem {
    private static final ResourceLocation TEXTURE_LOCATION = Tropicraft.location("textures/entity/ashen/mask.png");

    public AshenMaskItem(Holder<ArmorMaterial> armorMaterial, Properties properties) {
        super(armorMaterial, Type.HELMET, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockPos offsetPos = pos.relative(direction);
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        if (player != null && !canPlace(player, direction, itemStack, offsetPos)) {
            return InteractionResult.FAIL;
        } else {
            Level world = context.getLevel();
            WallItemEntity wallItem = new WallItemEntity(world, offsetPos, direction);
            wallItem.setItem(itemStack);

            if (wallItem.survives()) {
                if (!world.isClientSide) {
                    wallItem.playPlacementSound();
                    world.addFreshEntity(wallItem);
                }

                itemStack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }
    }

    private boolean canPlace(Player player, Direction direction, ItemStack heldStack, BlockPos pos) {
        return player.mayUseItemAt(pos, direction, heldStack);
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return TEXTURE_LOCATION;
    }

    public static class ClientExtensions implements IClientItemExtensions {
        private final AshenMasks type;

        public ClientExtensions(AshenMasks type) {
            this.type = type;
        }

        @Override
        public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
            return PlayerHeadpieceModel.createModel(TropicraftRenderLayers.HEADPIECE_LAYER, null, type.ordinal(), type.getXOffset(), type.getYOffset());
        }
    }
}
