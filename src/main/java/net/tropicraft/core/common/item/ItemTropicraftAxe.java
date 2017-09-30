package net.tropicraft.core.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.EntityLavaBall;
import net.tropicraft.core.common.entity.placeable.EntityChair;

/**
 * This class literally only exists because ItemAxe's constructor is protected. Go figure!
 * @author Cojo
 *
 */
public class ItemTropicraftAxe extends ItemAxe {

	/**
	 * And we have to override this constructor in particular because the other one
	 * won't work because our enum ordinal for our custom tool materials is
	 * beyond what ATTACK_DAMAGES can take.
	 * @param material
	 * @param damage
	 * @param speed
	 */
	public ItemTropicraftAxe(ToolMaterial material, float damage, float speed) {
		super(material, damage, speed);
	}
	
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
    	
//    	if (!worldIn.isRemote) {
//        	Entity ball = new EntityLavaBall(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, 0.5, 0, 0.5);
//        	worldIn.spawnEntity(ball);	
//    	}
    	
    	System.out.println("hello hello hello");
    	
    	if (!worldIn.isRemote) {
        	Entity ball = new EntityChair(worldIn, playerIn.posX, playerIn.posY + 1.01, playerIn.posZ, 0, playerIn);
        	worldIn.spawnEntity(ball);	
    	}
    	
        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

}