package net.tropicraft.core.common.biome.decorators;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.tropicraft.core.common.block.BlockCoral;
import net.tropicraft.core.common.block.BlockTropicraftSands;
import net.tropicraft.core.common.enums.TropicraftCorals;
import net.tropicraft.core.common.enums.TropicraftOres;
import net.tropicraft.core.common.enums.TropicraftSands;
import net.tropicraft.core.common.worldgen.TCNoiseGen;
import net.tropicraft.core.common.worldgen.WorldGenCoral;
import net.tropicraft.core.common.worldgen.WorldGenSurfaceClump;
import net.tropicraft.core.common.worldgen.WorldGenTropicsTreasure;
import net.tropicraft.core.registry.BlockRegistry;

public class BiomeDecoratorTropicsOcean extends BiomeDecoratorTropicraft {

    public static final int TREASURE_CHANCE = 100;

    private static final IBlockState coralSand = BlockRegistry.sands.getDefaultState().withProperty(BlockTropicraftSands.VARIANT, TropicraftSands.CORAL);

    private static final TCNoiseGen coralGen = new WorldGenCoral(new Random(38745L));
    private static final WorldGenSurfaceClump coralReefGen = new WorldGenSurfaceClump(0.03f, 6, 
            state -> ((BlockCoral)BlockRegistry.coral).canThisPlantGrowOnThisBlock(state.getBlock()), 
            state -> true, // dummy 
            rand -> BlockRegistry.coral.defaultForVariant(TropicraftCorals.VALUES[rand.nextInt(TropicraftCorals.VALUES.length)]), 
            false
        )
    {
        
        @Override
        protected boolean canPlaceBlock(World world, BlockPos pos) {
            return super.canPlaceBlock(world, pos) && ((BlockCoral)BlockRegistry.coral).canBlockStay(world, pos);
        }
    
        @Override
        protected void setBlockAndNotifyAdequately(World worldIn, BlockPos pos, IBlockState state) {
            super.setBlockAndNotifyAdequately(worldIn, pos, state);
            for (BlockPos p : BlockPos.getAllInBox(pos.down().east().north(), pos.down().west().south())) {
                if (worldIn.getBlockState(p).getBlock() == BlockRegistry.sands) {
                    super.setBlockAndNotifyAdequately(worldIn, p, coralSand);
                }
            }
        }
    };
    
    private static final IBlockState foamySand = BlockRegistry.sands.getDefaultState().withProperty(BlockTropicraftSands.VARIANT, TropicraftSands.FOAMY);

    private static final WorldGenSurfaceClump seaweedGen = new WorldGenSurfaceClump(0.025f, 8, 
            state -> state.getBlock() == BlockRegistry.sands, 
            state -> state.getMaterial().isLiquid(),
            rand -> BlockRegistry.seaweed.getDefaultState(),
            true
        )
    {
        @Override
        protected void setBlockAndNotifyAdequately(World worldIn, BlockPos pos, IBlockState state) {
            super.setBlockAndNotifyAdequately(worldIn, pos, state);
            for (BlockPos p : BlockPos.getAllInBox(pos.east().north(), pos.west().south())) {
                if (worldIn.getBlockState(p).getBlock() == BlockRegistry.sands) {
                    super.setBlockAndNotifyAdequately(worldIn, p, foamySand);
                }
            }
        }
    };

    private static final WorldGenSurfaceClump manganeseGen = new WorldGenSurfaceClump(0.025f, 8, 
            state -> state.getBlock() == BlockRegistry.sands, 
            state -> state.getMaterial().isLiquid(),
            rand -> BlockRegistry.ore.defaultForVariant(TropicraftOres.MANGANESE), 
            true
        );

    private static final WorldGenSurfaceClump shakaGen = new WorldGenSurfaceClump(0.025f, 8, 
            state -> state.getBlock() == BlockRegistry.sands, 
            state -> state.getMaterial().isLiquid(),
            rand -> BlockRegistry.ore.defaultForVariant(TropicraftOres.SHAKA), 
            true
        );
    
    @Override
    public void genDecorations(Biome biome, World world, Random rand) {
    	super.genDecorations(biome, world, rand);
    	
        coralGen.generate(world, rand, chunkPos);
        coralReefGen.generate(world, rand, chunkPos);
        seaweedGen.generate(world, rand, chunkPos);
        manganeseGen.generate(world, rand, chunkPos);
        shakaGen.generate(world, rand, chunkPos);

        //        if (ConfigGenRates.SHIPWRECK_CHANCE != 0 /*&& rand.nextInt(ConfigGenRates.SHIPWRECK_CHANCE) == 0*/) {
        //            int i = randDecorationCoord(rand, chunkPos.getX(), 16);
        //            int k = randDecorationCoord(rand, chunkPos.getZ(), 16);
        //            new WorldGenSunkenShip(world, rand).generate(new BlockPos(i, getTerrainHeightAt(world, i, k), k));
        //        }

        if (rand.nextInt(TREASURE_CHANCE) == 0) {
            int i = randDecorationCoord(rand, chunkPos.getX(), 12);
            int k = randDecorationCoord(rand, chunkPos.getZ(), 12);
            new WorldGenTropicsTreasure(world, rand).generate(world, rand, new BlockPos(i, getTerrainHeightAt(world, i, k), k));
        }
    }

}
