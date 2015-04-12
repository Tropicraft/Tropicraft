package net.tropicraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.tropicraft.entity.placeable.EntityWallStarfish;

public class ItemShell extends ItemTropicraftMulti {

    public ItemShell(String[] names) {
        super(names);
    }
    
	/**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
	@Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
		// If not starfish, return (for now!)
		if (par1ItemStack.getItemDamage() != 4) return false;
		
        if (par7 == 0)
        {
            return false;
        }
        else if (par7 == 1)
        {
            return false;
        }
        else
        {    
            int i1 = Direction.facingToDirection[par7];

            EntityWallStarfish entityhanging = new EntityWallStarfish(par3World);

            if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
            {
                return false;
            }
            else
            {
                if (entityhanging != null && entityhanging.onValidSurface())
                {
                    if (!par3World.isRemote)
                    {
                        par3World.spawnEntityInWorld(entityhanging);
                    }

                    --par1ItemStack.stackSize;
                }

                return true;
            }
        }
    }

}
