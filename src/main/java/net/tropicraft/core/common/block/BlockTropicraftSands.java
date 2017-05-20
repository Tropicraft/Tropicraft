package net.tropicraft.core.common.block;

import java.util.List;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.SandColors;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.enums.TropicraftSands;

public class BlockTropicraftSands extends BlockFalling implements ITropicraftBlock {

	public static final PropertyEnum<TropicraftSands> VARIANT = PropertyEnum.create("variant", TropicraftSands.class);
	public String[] names;

	public BlockTropicraftSands(String[] names) {
		super(Material.SAND);
		this.names = names;
		this.setSoundType(SoundType.SAND);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, TropicraftSands.PURIFIED));
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		IBlockState state = world.getBlockState(pos);
		int metadata = this.getMetaFromState(state);

		// If not black sands
		if (metadata != SandColors.BLACK.metadata) {
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
		for (int i = 0; i < names.length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { VARIANT });
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
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(state);
	}

	@Override
	public IProperty[] getProperties() {
		return new IProperty[] {VARIANT};
	}

	@Override
	public IBlockColor getBlockColor() {
		return TropicraftRenderUtils.SAND_COLORING;
	}

	@Override
	public IItemColor getItemColor() {
		return TropicraftRenderUtils.BLOCK_ITEM_COLORING;
	}
}
