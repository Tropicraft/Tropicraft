package net.tropicraft.core.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;

public class ShellItem extends Item {

    public ShellItem(final Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(final UseOnContext context) {
        final Direction facing = context.getClickedFace();
        final ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        final BlockPos pos = context.getClickedPos().relative(facing);

        // Must set the world coordinates here, or onValidSurface will be false.
        final Level world = context.getLevel();
        final WallItemEntity hangingEntity = new WallItemEntity(world, pos, facing);
        hangingEntity.setItem(stack);

        if (!context.getPlayer().mayUseItemAt(pos, facing, stack)) {
            return InteractionResult.FAIL;
        } else {
            if (hangingEntity.survives()) {
                if (!world.isClientSide) {
                    world.addFreshEntity(hangingEntity);
                }

                stack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }
    }
}
