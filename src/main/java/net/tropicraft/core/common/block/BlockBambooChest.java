package net.tropicraft.core.common.block;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.tileentity.TileEntityBambooChest;
import net.tropicraft.core.common.block.tileentity.TileEntityFactory;

public class BlockBambooChest extends BlockChest {

	public BlockBambooChest() {
		super(Type.BASIC);
		this.setHardness(2.5F);
		this.disableStats();
	}
	
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return TileEntityFactory.getBambooChestTE();
    }

    /**
     * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
     * EntityPlayer.
     */
    @SuppressWarnings("deprecation")
    @Override
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
        TileEntityBambooChest tile = (TileEntityBambooChest) world.getTileEntity(pos);
        if (tile != null && tile.isUnbreakable()) {
            return 0.0F;
        }
        return super.getPlayerRelativeBlockHardness(state, player, world, pos);
    }
}
