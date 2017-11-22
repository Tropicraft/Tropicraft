package net.tropicraft.core.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.block.tileentity.TileEntityFactory;
import net.tropicraft.core.common.block.tileentity.TileEntitySifter;
import net.tropicraft.core.registry.BlockRegistry;

public class BlockSifter extends BlockTropicraft implements ITileEntityProvider {

	public BlockSifter() {
		super(Material.WOOD);
		this.setHardness(1.0F);
		this.setResistance(4.0F);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

	@Override
	public boolean isTopSolid(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}

		ItemStack stack = entityPlayer.getHeldItem(hand);

		TileEntitySifter tileentitysifta = (TileEntitySifter) world.getTileEntity(pos);

		if (tileentitysifta != null && !stack.isEmpty() && !tileentitysifta.isSifting()) {
			Item helditem = stack.getItem();
			if (helditem == Item.getItemFromBlock(Blocks.SAND) || (helditem == Item.getItemFromBlock(BlockRegistry.sands))) {
				entityPlayer.getHeldItemMainhand().shrink(1);
				tileentitysifta.addItemToSifter(stack);
				tileentitysifta.startSifting();
			}
		}
		return true;
	} // /o/ \o\ /o\ \o\ /o\ \o/ /o/ /o/ \o\ \o\ /o/ /o/ \o/ /o\ \o/ \o/ /o\ /o\ \o/ \o/ /o/ \o\o\o\o\o\o\o\o\o\ :D

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return TileEntityFactory.getSifterTE();
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
	    return Item.getItemFromBlock(BlockRegistry.sifter);
	}
}
