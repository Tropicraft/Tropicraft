package net.tropicraft.core.common.item;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.PlayerHeadpieceRenderer;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;

import javax.annotation.Nullable;

public class AshenMaskItem extends ArmorItem {
    private final AshenMasks maskType;

    public AshenMaskItem(IArmorMaterial armorMaterial, AshenMasks maskType, Properties properties) {
        super(armorMaterial, EquipmentSlotType.HEAD, properties);
        this.maskType = maskType;
    }

    public AshenMasks getMaskType() {
        return maskType;
    }

    /**
     * Called when this item is used when targetting a Block
     */
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockPos pos = context.getPos();
        Direction direction = context.getFace();
        BlockPos offsetPos = pos.offset(direction);
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = context.getItem();
        if (player != null && !this.canPlace(player, direction, itemStack, offsetPos)) {
            return ActionResultType.FAIL;
        } else {
            World world = context.getWorld();
            WallItemEntity wallItem = new WallItemEntity(TropicraftEntities.WALL_ITEM.get(), world);
            wallItem.setHangingPosition(offsetPos);
            wallItem.updateFacingWithBoundingBox(direction);
            wallItem.setDisplayedItem(itemStack);

            if (wallItem.onValidSurface()) {
                if (!world.isRemote) {
                    wallItem.playPlaceSound();
                    world.addEntity(wallItem);
                }

                itemStack.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }
    }

    private boolean canPlace(PlayerEntity player, Direction direction, ItemStack heldStack, BlockPos pos) {
        return !direction.getAxis().isVertical() && player.canPlayerEdit(pos, direction, heldStack);
	}

	@OnlyIn(Dist.CLIENT)
	@Nullable
	@Override
	public BipedModel getArmorModel(final LivingEntity entityLiving, final ItemStack itemStack, final EquipmentSlotType armorSlot, final BipedModel model) {
		return armorSlot == EquipmentSlotType.HEAD ? new PlayerHeadpieceRenderer(maskType.ordinal(), maskType.getXOffset(), maskType.getYOffset()) : null;
	}

	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
    	return TropicraftRenderUtils.getTextureEntity("ashen/mask").toString();
    }
}
