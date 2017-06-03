package net.tropicraft.core.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.underdasea.EntityStarfish;

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
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
    	
    	if (!worldIn.isRemote) {
        	Entity ball = new EntityStarfish(worldIn);
        	ball.setLocationAndAngles(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.cameraYaw, playerIn.cameraPitch);
        	worldIn.spawnEntityInWorld(ball);	
    	}
    	
    	System.out.println("hello hello hello");
    	
        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

}
