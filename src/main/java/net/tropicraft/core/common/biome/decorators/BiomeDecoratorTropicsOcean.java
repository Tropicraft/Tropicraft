package net.tropicraft.core.common.biome.decorators;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.tropicraft.core.common.block.BlockCoral;
import net.tropicraft.core.common.block.BlockTropicraftSands;
import net.tropicraft.core.common.enums.TropicraftCorals;
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
            rand -> BlockRegistry.coral.getDefaultState().withProperty(BlockCoral.VARIANT, TropicraftCorals.VALUES[rand.nextInt(TropicraftCorals.VALUES.length)]), 
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

    public void decorate(World worldIn, Random random, Biome biome, BlockPos pos)
    {
        if (this.decorating)
        {
            throw new RuntimeException("Already decorating");
        }
        else
        {
            this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory(worldIn.getWorldInfo().getGeneratorOptions()).build();
            this.chunkPos = pos;
            this.genDecorations(biome, worldIn, random);
            this.decorating = false;
        }
    }

    public void genDecorations(Biome biome, World world, Random rand) {
        coralGen.generate(world, rand, chunkPos);
        coralReefGen.generate(world, rand, chunkPos);
        seaweedGen.generate(world, rand, chunkPos);
        //        if (rand.nextInt(5) == 0) {
        //            int x = randCoord(rand, chunkPos.getX(), 16) + 8;
        //            int z = randCoord(rand, chunkPos.getZ(), 16) + 8;
        //            BlockPos pos = new BlockPos(x, 0, z);
        //            new WorldGenCoral().generate(world, rand, pos);
        //        }
        //        
        //        if (rand.nextInt(8) == 0) {
        //            int x = randCoord(rand, chunkPos.getX(), 16) + 8;
        //            int z = randCoord(rand, chunkPos.getZ(), 16) + 8;
        //            BlockPos pos = new BlockPos(x, 0, z);
        //            new WorldGenSeaweed().generate(world, rand, pos);
        //        }

        //        if (ConfigGenRates.SHIPWRECK_CHANCE != 0 /*&& rand.nextInt(ConfigGenRates.SHIPWRECK_CHANCE) == 0*/) {
        //            int i = randCoord(rand, chunkPos.getX(), 16);
        //            int k = randCoord(rand, chunkPos.getZ(), 16);
        //            new WorldGenSunkenShip(world, rand).generate(new BlockPos(i, getTerrainHeightAt(world, i, k), k));
        //        }

        if(rand.nextInt(TREASURE_CHANCE) == 0) {
            int i = randCoord(rand, chunkPos.getX(), 16);
            int k = randCoord(rand, chunkPos.getZ(), 16);
            new WorldGenTropicsTreasure(world, rand).generate(world, rand, new BlockPos(i, getTerrainHeightAt(world, i, k), k));
        }
    }

}
