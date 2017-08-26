package net.tropicraft.core.common.item;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.placeable.EntityWallStarfish;
import net.tropicraft.core.common.entity.underdasea.StarfishType;


public class ItemShell extends ItemHangingEntity {

	private boolean isPlaceable;

	public ItemShell(boolean isPlaceable) {
		super(EntityWallStarfish.class);
		this.isPlaceable = isPlaceable;
	}
	
	public ItemShell() {
		this(false);
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public EnumActionResult onItemUse(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (isPlaceable) {
	        BlockPos blockpos = pos.offset(facing);

	        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && player.canPlayerEdit(blockpos, facing, itemstack)) {
	            EntityWallStarfish entityhanging = (EntityWallStarfish) this.createEntity(world, pos, facing);
	            entityhanging.setFacing(facing);
	            System.out.println("r");
	            if (entityhanging != null && entityhanging.onValidSurface()) {
	                if (!world.isRemote) {
	                    entityhanging.playPlaceSound();
	                    world.spawnEntityInWorld(entityhanging);
	                }
	                --itemstack.stackSize;
	            }

	            return EnumActionResult.SUCCESS;
	        }
	        else {
	            return EnumActionResult.FAIL;
	        }
		} else {
			return EnumActionResult.PASS;
		}
	}
	
    @Nullable
    private EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide) {
        return new EntityWallStarfish(worldIn, pos, clickedSide, StarfishType.getRandomType());
    }
}
