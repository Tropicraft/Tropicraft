package net.tropicraft.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemSnareTrap extends ItemTropicraft {
	public ItemSnareTrap() {
		super();
		setCreativeTab(TCCreativeTabRegistry.tabCombat);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float offsetX, float offsetY, float offsetZ) {
		ForgeDirection dir = ForgeDirection.getOrientation(side);

		if (dir == ForgeDirection.DOWN || dir == ForgeDirection.UP) {
			return false;
		}

		if (world.isRemote) {
			return true;
		}
		
		x += dir.offsetX;
		z += dir.offsetZ;
		
		int height = getHeight(world, x, y, z);
		
		if (height < EntitySnareTrap.MIN_HEIGHT || height > EntitySnareTrap.MAX_HEIGHT) {
			return false;
		}

		List objs = world.getEntitiesWithinAABB(EntitySnareTrap.class, AxisAlignedBB.getBoundingBox(x, y-height+1, z, x+1, y-height+1+EntitySnareTrap.MAX_HEIGHT, z+1));
		
		if (!objs.isEmpty()) {
			return false;
		}
		
		EntitySnareTrap trap = new EntitySnareTrap(world, x, y-height+1, z, height, dir);
		world.spawnEntityInWorld(trap);

		if (!player.capabilities.isCreativeMode) {
			stack.stackSize--;
		}

		return true;
	}
	
	/**
	 * Calculates the number of air blocks below this block.
	 * @return the height.
	 */
	private int getHeight(World world, int x, int y, int z) {
		int height = 0;
		while (y >= 0 && world.getBlockId(x, y, z) == 0) {
			y--;
			height++;
		}
		return height;
	}
}