package net.tropicraft.block.scuba;

import java.util.Random;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.tropicraft.block.BlockTropicraft;
import net.tropicraft.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class BlockAirCompressor extends BlockTropicraft implements ITileEntityProvider {

	public BlockAirCompressor() {
		super(Material.rock);
		this.isBlockContainer = true;
		this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
	}
	
    /**
     * How many world ticks before ticking
     */
	@Override
    public int tickRate(World world) {
        return 4;
    }
	
    /**
     * Ticks the block if it's been scheduled
     */
	@Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isRemote) {
            
        }
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityAirCompressor();
	}

}
