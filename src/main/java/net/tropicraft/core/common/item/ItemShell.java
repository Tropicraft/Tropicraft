package net.tropicraft.core.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.placeable.EntityWallItem;
import net.tropicraft.core.common.enums.TropicraftShells;
import net.tropicraft.core.registry.CreativeTabRegistry;

public class ItemShell extends ItemTropicraft {

	public ItemShell() {
		super();
		setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
	    if (tab != CreativeTabRegistry.tropicraftTab) return;
	    for (TropicraftShells type : TropicraftShells.values()) {
	        subItems.add(new ItemStack(this, 1, type.getMeta()));
	    }
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
	    return "item." + TropicraftShells.values()[stack.getMetadata() % TropicraftShells.values().length].getUnlocName();
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing.getAxis().isVertical()) {
			return EnumActionResult.FAIL;
		} else {
			// It's a wall, place the shell on it.

		    ItemStack stack = playerIn.getHeldItem(hand);
		    
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

					stack.shrink(1);
				}

				return EnumActionResult.SUCCESS;
			}
		}
	}
}