package net.tropicraft.core.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.registry.BlockRegistry;


public class BlockPackedPurifiedSand extends BlockTropicraft {

	public BlockPackedPurifiedSand() {
		super(Material.SAND);
		this.setHardness(2.0F);
		this.setResistance(30F);
		this.setSoundType(SoundType.SAND);
	}

	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
			return Item.getItemFromBlock(BlockRegistry.sands.getDefaultState().getBlock());
	}
	
	@Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(world, pos, state, player);
        if (!world.isRemote) {
        		if(player.capabilities.isCreativeMode) {
        			return;
        		}
        		int extra = player.getRNG().nextInt(2)+1;
    			for(int i = 0; i < extra; i++) {
    				this.dropBlockAsItem(world, pos, BlockRegistry.sands.getDefaultState(), 0);
    			}
        		
        }
	}
}
