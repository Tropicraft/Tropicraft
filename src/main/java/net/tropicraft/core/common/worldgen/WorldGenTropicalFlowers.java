package net.tropicraft.core.common.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockTropicsFlowers;
import net.tropicraft.core.common.enums.TropicraftFlowers;
import net.tropicraft.core.registry.BlockRegistry;

public class WorldGenTropicalFlowers extends TCGenBase {

	private static final int FLOWER_TRIES = 35;
	private Block plantBlock;
	private TropicraftFlowers[] flowers;

	public WorldGenTropicalFlowers(World world, Random rand, Block plantBlock) {
		super(world, rand);
		this.plantBlock = plantBlock;
		this.flowers = TropicraftFlowers.VALUES;
	}

	public WorldGenTropicalFlowers(World world, Random rand, Block plantBlock, TropicraftFlowers[] flowers) {
		super(world, rand);
		this.plantBlock = plantBlock;
		this.flowers = flowers;
	}

    @Override
	public boolean generate(BlockPos pos) {
    	int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		for(int l = 0; l < FLOWER_TRIES; l++) {
			int x = (i + rand.nextInt(8)) - rand.nextInt(8);
			int y = (j + rand.nextInt(4)) - rand.nextInt(4);
			int z = (k + rand.nextInt(8)) - rand.nextInt(8);
			BlockPos pos2 = new BlockPos(x, y, z);
			if(TCGenUtils.isAirBlock(worldObj, x, y, z) && ((BlockBush) BlockRegistry.flowers).canBlockStay(this.worldObj, pos2, BlockRegistry.flowers.getDefaultState())) {
				if (rand.nextInt(3) == 0) {
					int meta = rand.nextInt(this.flowers.length);
					TropicraftFlowers flowerEnum = this.flowers[meta];
					// Get random flower until we get one that isn't magic mushroom
					// since magic mushrooms should only generate in/around rainforests,
					// not along with the rest of the flowers.
					while (flowerEnum == TropicraftFlowers.MAGIC_MUSHROOM) {
						meta = rand.nextInt(this.flowers.length);
						flowerEnum = this.flowers[meta];
					}
					TCGenUtils.setBlockState(worldObj, x, y, z, plantBlock.getDefaultState().withProperty(BlockTropicsFlowers.VARIANT, flowerEnum), blockGenNotifyFlag);
				}
			}
		}

		return true;
	}
}
