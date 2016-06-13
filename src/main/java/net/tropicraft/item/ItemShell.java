package net.tropicraft.item;

import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.tropicraft.entity.placeable.EntityWallShell;
import net.tropicraft.entity.placeable.EntityWallStarfish;
import net.tropicraft.entity.underdasea.StarfishType;

public class ItemShell extends ItemTropicraftMulti {

    public ItemShell(String[] names) {
        super(names);
    }
    
	/**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
	@Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player,
    		World world, int x, int y, int z,
    		int side, float local_px, float local_py, float local_pz)
    {		
        if (side == 0)
        {
            return false;
        }
        else if (side == 1)
        {
            return false;
        }
        else // It's a wall, place the shell on it.
        {
            int direction = Direction.facingToDirection[side];

            // Must set the world coordinates here, or onValidSurface will be false.
            EntityHanging entityhanging = new EntityWallShell(world, x, y, z, direction, itemStack.getItemDamage());

            if (!player.canPlayerEdit(x, y, z, side, itemStack))
            {
                return false;
            }
            else
            {
                if (entityhanging != null && entityhanging.onValidSurface())
                {                	
                    if (!world.isRemote)
                    {                    	
                        world.spawnEntityInWorld(entityhanging);
                    }

                    --itemStack.stackSize;
                }
                
                return true;
            }
        }
    }

}
