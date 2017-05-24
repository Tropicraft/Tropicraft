package net.tropicraft.core.common.block;

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
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}

		ItemStack stack = entityPlayer.inventory.getCurrentItem();

		TileEntitySifter tileentitysifta = (TileEntitySifter) world.getTileEntity(pos);

		if (tileentitysifta != null && stack != null && !tileentitysifta.isSifting()) {
			Item helditem = stack.getItem();
			if (helditem == Item.getItemFromBlock(Blocks.SAND) /*unrefined raftous ore || (helditem == TCItemRegistry.ore && stack.getItemDamage() == 5) */
					|| /* mineral sands */(helditem == Item.getItemFromBlock(BlockRegistry.sands) && stack.getItemDamage() == 3)) {
				entityPlayer.getHeldItemMainhand().stackSize--;

				//                if (helditem == ItemRegistry.) {
				//                    float percent = getTagCompound(stack).getFloat("AmtRefined");
				//                    tileentitysifta.setSifting(true, helditem == Item.getItemFromBlock(Blocks.SAND) ? 1 : 
				//                        helditem == Item.getItemFromBlock(TCBlockRegistry.mineralSands) ? 2 : 3, percent);
				//                } else {
				tileentitysifta.setSifting(true, helditem == Item.getItemFromBlock(Blocks.SAND) ? 1 : 
					helditem == Item.getItemFromBlock(BlockRegistry.sands) ? 2 : 3, -1);
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

}
