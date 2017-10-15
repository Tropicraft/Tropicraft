package net.tropicraft.core.common.block;

import java.util.List;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftSands;
import net.tropicraft.core.registry.BlockRegistry;

public class BlockTropicraftSands extends BlockFalling implements ITropicraftBlock {

	public static final PropertyEnum<TropicraftSands> VARIANT = PropertyEnum.create("variant", TropicraftSands.class);
	
	public static final PropertyBool UNDERWATER = PropertyBool.create("underwater");

	public BlockTropicraftSands() {
		super(Material.SAND);
		this.setHardness(0.5f);
		this.setSoundType(SoundType.SAND);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, TropicraftSands.PURIFIED).withProperty(UNDERWATER, false));
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		IBlockState state = world.getBlockState(pos);

		// If not black sands
		if (state.getValue(VARIANT) != TropicraftSands.VOLCANIC) {
			return;
		}

		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entity;

			ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

			// If player isn't wearing anything on their feetsies
			if (stack == null) {
				player.attackEntityFrom(DamageSource.lava, 0.5F);
			}
		} else {
			entity.attackEntityFrom(DamageSource.lava, 0.5F);
		}
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {        
		for (int i = 0; i < TropicraftSands.VALUES.length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, getProperties());
	}

	@Override
	public String getStateName(IBlockState state) {
		return ((TropicraftSands) state.getValue(VARIANT)).getName();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, TropicraftSands.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((TropicraftSands) state.getValue(VARIANT)).ordinal();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
	    IBlockState ret = super.getActualState(state, worldIn, pos);
	    if (pos.getY() < 64 && worldIn.getBlockState(pos.up()).getBlock() == BlockRegistry.tropicsWater) {
	        ret = ret.withProperty(UNDERWATER, true);
	    }
	    return ret;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(state);
	}

	@Override
	public IProperty[] getProperties() {
		return new IProperty[] {VARIANT, UNDERWATER};
	}
}
