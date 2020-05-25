package net.tropicraft.core.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;

public class ShellItem extends Item {

    public ShellItem(final Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(final ItemUseContext context) {
        final Direction facing = context.getFace();
        final ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        final BlockPos pos = context.getPos().offset(facing);

        // Must set the world coordinates here, or onValidSurface will be false.
        final World world = context.getWorld();
        final WallItemEntity hangingEntity = new WallItemEntity(world, pos, facing);
        hangingEntity.setDisplayedItem(stack);

        if (!context.getPlayer().canPlayerEdit(pos, facing, stack)) {
            return ActionResultType.FAIL;
        } else {
            if (hangingEntity.onValidSurface()) {
                if (!world.isRemote) {
                    world.addEntity(hangingEntity);
                }

                stack.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }
    }
}
