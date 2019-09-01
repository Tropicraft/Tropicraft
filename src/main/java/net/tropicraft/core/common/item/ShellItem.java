package net.tropicraft.core.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;

public class ShellItem extends Item {

    public ShellItem(final Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(final ItemUseContext context) {
        final Direction facing = context.getFace();
        if (facing.getAxis().isVertical()) {
            return ActionResultType.FAIL;
        } else {
//            // It's a wall, place the shell on it.
//            ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
//
//            final BlockPos pos = context.getPos().offset(facing);
//
//            // Must set the world coordinates here, or onValidSurface will be false.
//            final World world = context.getWorld();
//            HangingEntity entityhanging = new TropicraftWallEntity(world, pos, facing, stack);
//
//            if (!context.getPlayer().canPlayerEdit(pos, facing, stack)) {
//                return ActionResultType.FAIL;
//            } else {
//                if (entityhanging.onValidSurface()) {
//                    if (!world.isRemote) {
//                        world.addEntity(entityhanging);
//                    }
//
//                    stack.shrink(1);
//                }
//
//                return ActionResultType.SUCCESS;
//            }
            // TODO
            return ActionResultType.SUCCESS;
        }
    }
}
