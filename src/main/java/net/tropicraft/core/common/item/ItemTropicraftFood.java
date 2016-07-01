package net.tropicraft.core.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.worldgen.WorldGenTallTree;

public class ItemTropicraftFood extends ItemFood {

	public ItemTropicraftFood(int healAmount, float saturation) {
		super(healAmount, saturation, false);
	}

    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        playerIn.setActiveHand(hand);
        
        BlockPos pos = playerIn.getPosition();
        pos = new BlockPos(pos.getX(), worldIn.getHeight(pos).getY(), pos.getZ());
        
        new WorldGenTallTree(worldIn, worldIn.rand).generate(pos);
        
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }
}
