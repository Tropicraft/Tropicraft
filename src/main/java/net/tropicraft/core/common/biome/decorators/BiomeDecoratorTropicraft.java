package net.tropicraft.core.common.biome.decorators;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.tropicraft.core.common.enums.TropicraftOres;
import net.tropicraft.core.registry.BlockRegistry;

public class BiomeDecoratorTropicraft extends BiomeDecorator {

    private WorldGenerator eudialyteGen;
    private WorldGenerator zirconGen;
    private WorldGenerator azuriteGen;

    protected int zirconSize, eudialyteSize;

	public BiomeDecoratorTropicraft() {
	    this.zirconSize = 10;
	    this.eudialyteSize = 8;
	}

	/**
	 * Leave this for each tropics biome decorator to figure out
	 */
	@Override
	public void decorate(World worldIn, Random random, Biome biome, BlockPos pos) {
		if (this.decorating) {
			throw new RuntimeException("Already decorating");
		} else {
		    this.chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(worldIn.getWorldInfo().getGeneratorOptions()).build();
			this.chunkPos = pos;
			this.decorating = false;
			this.initOreGen(biome, worldIn, random);
            this.genDecorations(biome, worldIn, random);
		}
	}

	protected void initOreGen(Biome biome, World world, Random rand) {
	    this.dirtGen = new WorldGenMinable(Blocks.DIRT.getDefaultState(), this.chunkProviderSettings.dirtSize);
        this.gravelOreGen = new WorldGenMinable(Blocks.GRAVEL.getDefaultState(), this.chunkProviderSettings.gravelSize);
	    this.graniteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
	    this.dioriteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
	    this.andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
	    this.coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), this.chunkProviderSettings.coalSize);
	    this.ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), this.chunkProviderSettings.ironSize);
	    this.goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), this.chunkProviderSettings.goldSize);
	    this.redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), this.chunkProviderSettings.redstoneSize);
	    this.diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), this.chunkProviderSettings.diamondSize);
	    this.lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), this.chunkProviderSettings.lapisSize);

	    // Tropics ore gen
	    this.eudialyteGen = new WorldGenMinable(BlockRegistry.ore.defaultForVariant(TropicraftOres.EUDIALYTE), this.eudialyteSize);
	    this.zirconGen = new WorldGenMinable(BlockRegistry.ore.defaultForVariant(TropicraftOres.ZIRCON), this.zirconSize);
	    this.azuriteGen = new AzuriteGenerator();
	}
	
	@Override
	protected void genDecorations(Biome biomeIn, World worldIn, Random random) {
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Pre(worldIn, random, chunkPos));
        this.generateOres(worldIn, random);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Post(worldIn, random, chunkPos));
	}

	@Override
	protected void generateOres(@Nonnull World worldIn, @Nonnull Random random) {
		super.generateOres(worldIn, random);
		// Tropics ores
	    this.genStandardOre1(worldIn, random, this.zirconSize, this.zirconGen, 5, 95);
	    this.genStandardOre1(worldIn, random, this.eudialyteSize, this.eudialyteGen, 5, 55);
        azuriteGen.generate(worldIn, random, chunkPos);
	}

	public int getTerrainHeightAt(World world, int x, int z) {
		for(int y = world.getHeight(new BlockPos(x, 0, z)).getY() + 1; y > 0; y--) {
			IBlockState blockstate = world.getBlockState(new BlockPos(x, y, z));
			if(blockstate.getMaterial() == Material.GRASS ||
			   blockstate.getMaterial() == Material.GROUND ||
			   blockstate.getMaterial() == Material.SAND) {
				return y + 1;
			}
		}
		return 0;
	}

	public final int randDecorationCoord(Random rand, int base, int variance) {
		// Offset by 8 to ensure coordinate is in center of chunks for decoration so that CCG is avoided
		return base + rand.nextInt(variance) + 8;
	}

    private static class AzuriteGenerator extends WorldGenerator
    {
        @Override
        public boolean generate(World worldIn, Random rand, BlockPos pos)
        {
            int count = 3 + rand.nextInt(3);
            for (int i = 0; i < count; i++)
            {
                int x = rand.nextInt(16) + 8;
                int z = rand.nextInt(16) + 8;
                BlockPos blockpos = pos.add(x, rand.nextInt(28) + 4, z);

                net.minecraft.block.state.IBlockState state = worldIn.getBlockState(blockpos);
                if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, net.minecraft.block.state.pattern.BlockMatcher.forBlock(Blocks.STONE))) {
                    worldIn.setBlockState(blockpos, BlockRegistry.ore.defaultForVariant(TropicraftOres.AZURITE), 2);
                }
            }
            return true;
        }
    }
}
