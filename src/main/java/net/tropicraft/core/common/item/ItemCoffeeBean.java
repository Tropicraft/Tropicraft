package net.tropicraft.core.common.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.tropicraft.Info;
import net.tropicraft.core.common.block.BlockCoffeeBush;

public class ItemCoffeeBean extends ItemTropicraft implements IPlantable {
	
	private final String[] names;
	private final BlockCoffeeBush bush;
	
	public ItemCoffeeBean(String[] names, BlockCoffeeBush bush) {
		setHasSubtypes(true);
		this.names = names;
		this.bush = bush;
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < names.length; i++) {
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item." + Info.MODID + "." + names[stack.getItemDamage() % names.length].replace('_', '.');
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (stack.getItemDamage() > 0) {
			return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
        IBlockState state = worldIn.getBlockState(pos);
        if (facing == EnumFacing.UP && playerIn.canPlayerEdit(pos.offset(facing), facing, stack) && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up())) {
			worldIn.setBlockState(pos.up(), bush.getDefaultState());
			stack.stackSize--;
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
	
	/* == IPlantable == */

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return bush.getDefaultState();
	}
}
