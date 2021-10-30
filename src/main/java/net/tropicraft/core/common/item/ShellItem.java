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

import net.minecraft.item.Item.Properties;

public class ShellItem extends Item {

    public ShellItem(final Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(final ItemUseContext context) {
        final Direction facing = context.getClickedFace();
        final ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        final BlockPos pos = context.getClickedPos().relative(facing);

        // Must set the world coordinates here, or onValidSurface will be false.
        final World world = context.getLevel();
        final WallItemEntity hangingEntity = new WallItemEntity(world, pos, facing);
        hangingEntity.setItem(stack);

        if (!context.getPlayer().mayUseItemAt(pos, facing, stack)) {
            return ActionResultType.FAIL;
        } else {
            if (hangingEntity.survives()) {
                if (!world.isClientSide) {
                    world.addFreshEntity(hangingEntity);
                }

                stack.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }
    }
}
