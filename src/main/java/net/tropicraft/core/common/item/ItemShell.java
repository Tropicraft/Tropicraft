package net.tropicraft.core.common.item;

import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.placeable.EntityWallItem;

public class ItemShell extends ItemTropicraft {

	public ItemShell() {
		super();
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing.getAxis().isVertical()) {
			return EnumActionResult.FAIL;
		} else {
			// It's a wall, place the shell on it.

			pos = pos.offset(facing);
			
			// Must set the world coordinates here, or onValidSurface will be false.
			EntityHanging entityhanging = new EntityWallItem(worldIn, pos, facing, stack);

			if (!playerIn.canPlayerEdit(pos, facing, stack)) {
				return EnumActionResult.FAIL;
			} else {
				if (entityhanging != null && entityhanging.onValidSurface()) {
					if (!worldIn.isRemote) {
						worldIn.spawnEntity(entityhanging);
					}

					--stack.stackSize;
				}

				return EnumActionResult.SUCCESS;
			}
		}
	}
}