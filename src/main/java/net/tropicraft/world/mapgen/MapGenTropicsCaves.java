package net.tropicraft.world.mapgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;

public class MapGenTropicsCaves extends MapGenBase {

	private static final int CHUNK_SIZE_Y = 256;
	
	public void generate(IChunkProvider chunkProvider, World world, int x, int z, Block[] blocks)
    {
        int range = this.range;
        this.worldObj = world;
        this.rand.setSeed(world.getSeed());
        long l = this.rand.nextLong();
        long i1 = this.rand.nextLong();

        for (int j1 = x - range; j1 <= x + range; ++j1)
        {
            for (int k1 = z - range; k1 <= z + range; ++k1)
            {
                long l1 = (long)j1 * l;
                long i2 = (long)k1 * i1;
                this.rand.setSeed(l1 ^ i2 ^ world.getSeed());
                this.recursiveGenerate(world, j1, k1, x, z, blocks);
            }
        }
    }
	
    /**
     * Generates a larger initial cave node than usual. Called 25% of the time.
     */
    protected void generateLargeCaveNode(long seed, int chunkX, int chunkZ, Block[] blocks, double i, double j, double k)
    {
        this.generateCaveNode(seed, chunkX, chunkZ, blocks, i, j, k, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    /**
     * Generates a node in the current cave system recursion tree.
     */
    protected void generateCaveNode(long seed, int chunkX, int chunkZ, Block[] blocks, double i, double j, double k, float sizeMod, float headingXZ, float headingY, int currentSection, int length, double sizeModY)
    {
        double d4 = (double)(chunkX * 16 + 8);
        double d5 = (double)(chunkZ * 16 + 8);
        float f3 = 0.0F;
        float f4 = 0.0F;
        Random random = new Random(seed);

        if (length <= 0)
        {
            int j1 = this.range * 16 - 16;
            length = j1 - random.nextInt(j1 / 4);
        }

        boolean flag = false;

        if (currentSection == -1)
        {
            currentSection = length / 2;
            flag = true;
        }

        int k1 = random.nextInt(length / 2) + length / 4;

        for (boolean flag1 = random.nextInt(6) == 0; currentSection < length; ++currentSection)
        {
            double d6 = 1.5D + (double)(MathHelper.sin((float)currentSection * (float)Math.PI / (float)length) * sizeMod * 1.0F);
            double d7 = d6 * sizeModY;
            float f5 = MathHelper.cos(headingY);
            float f6 = MathHelper.sin(headingY);
            i += (double)(MathHelper.cos(headingXZ) * f5);
            j += (double)f6;
            k += (double)(MathHelper.sin(headingXZ) * f5);

            if (flag1)
            {
                headingY *= 0.92F;
            }
            else
            {
                headingY *= 0.7F;
            }

            headingY += f4 * 0.1F;
            headingXZ += f3 * 0.1F;
            f4 *= 0.9F;
            f3 *= 0.75F;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

            if (!flag && currentSection == k1 && sizeMod > 1.0F && length > 0)
            {
                this.generateCaveNode(random.nextLong(), chunkX, chunkZ, blocks, i, j, k, random.nextFloat() * 0.5F + 0.5F, headingXZ - ((float)Math.PI / 2F), headingY / 3.0F, currentSection, length, 1.0D);
                this.generateCaveNode(random.nextLong(), chunkX, chunkZ, blocks, i, j, k, random.nextFloat() * 0.5F + 0.5F, headingXZ + ((float)Math.PI / 2F), headingY / 3.0F, currentSection, length, 1.0D);
                return;
            }

            if (flag || random.nextInt(4) != 0)
            {
                double d8 = i - d4;
                double d9 = k - d5;
                double d10 = (double)(length - currentSection);
                double d11 = (double)(sizeMod + 2.0F + 16.0F);

                if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11)
                {
                    return;
                }

                if (i >= d4 - 16.0D - d6 * 2.0D && k >= d5 - 16.0D - d6 * 2.0D && i <= d4 + 16.0D + d6 * 2.0D && k <= d5 + 16.0D + d6 * 2.0D)
                {
                    int l1 = MathHelper.floor_double(i - d6) - chunkX * 16 - 1;
                    int i2 = MathHelper.floor_double(i + d6) - chunkX * 16 + 1;
                    int j2 = MathHelper.floor_double(j - d7) - 1;
                    int k2 = MathHelper.floor_double(j + d7) + 1;
                    int l2 = MathHelper.floor_double(k - d6) - chunkZ * 16 - 1;
                    int i3 = MathHelper.floor_double(k + d6) - chunkZ * 16 + 1;

                    if (l1 < 0)
                    {
                        l1 = 0;
                    }

                    if (i2 > 16)
                    {
                        i2 = 16;
                    }

                    if (j2 < 1)
                    {
                        j2 = 1;
                    }

                    if (k2 > 120)
                    {
                        k2 = 120;
                    }

                    if (l2 < 0)
                    {
                        l2 = 0;
                    }

                    if (i3 > 16)
                    {
                        i3 = 16;
                    }

                    boolean flag2 = false;
                    int j3;
                    int k3;

                    for (j3 = l1; !flag2 && j3 < i2; ++j3)
                    {
                        for (int l3 = l2; !flag2 && l3 < i3; ++l3)
                        {
                            for (int i4 = k2 + 1; !flag2 && i4 >= j2 - 1; --i4)
                            {
                                k3 = j3 * CHUNK_SIZE_Y * 16 | l3 * CHUNK_SIZE_Y | i4;
                                
                                if (i4 >= 0 && i4 < 128)
                                {
                                    if (isOceanBlock(blocks, k3, j3, i4, l3, chunkX, chunkZ))
                                    {
                                        flag2 = true;
                                    }

                                    if (i4 != j2 - 1 && j3 != l1 && j3 != i2 - 1 && l3 != l2 && l3 != i3 - 1)
                                    {
                                        i4 = j2;
                                    }
                                }
                            }
                        }
                    }

                    if (!flag2)
                    {
                        for (j3 = l1; j3 < i2; ++j3)
                        {
                            double d12 = ((double)(j3 + chunkX * 16) + 0.5D - i) / d6;

                            for (k3 = l2; k3 < i3; ++k3)
                            {
                                double d13 = ((double)(k3 + chunkZ * 16) + 0.5D - k) / d6;
                                boolean flag3 = false;

                                if (d12 * d12 + d13 * d13 < 1.0D)
                                {
                                    for (int k4 = k2 - 1; k4 >= j2; --k4)
                                    {
                                        int j4 = j3 * CHUNK_SIZE_Y * 16 | k3 * CHUNK_SIZE_Y | k4;
                                        
                                        double d14 = ((double)k4 + 0.5D - j) / d7;

                                        if (d14 > -0.7D && d12 * d12 + d14 * d14 + d13 * d13 < 1.0D)
                                        {
                                            if (isTopBlock(blocks, j4, j3, k4, k3, chunkX, chunkZ))
                                            {
                                                flag3 = true;
                                            }

                                            digBlock(blocks, j4, j3, k4, k3, chunkX, chunkZ, flag3);
                                        }
                                    }
                                }
                            }
                        }

                        if (flag)
                        {
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Recursively called by generate() (generate) and optionally by itself.
     */
    protected void recursiveGenerate(World world, int i, int k, int chunkX, int chunkZ, Block[] blocks)
    {
        int i1 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);

        if (this.rand.nextInt(15) != 0)
        {
            i1 = 0;
        }

        for (int j1 = 0; j1 < i1; ++j1)
        {
            double x = (double)(i * 16 + this.rand.nextInt(16));
            double y = (double)this.rand.nextInt(this.rand.nextInt(120) + 8);
            double z = (double)(k * 16 + this.rand.nextInt(16));
            int k1 = 1;

            if (this.rand.nextInt(4) == 0)
            {
                this.generateLargeCaveNode(this.rand.nextLong(), chunkX, chunkZ, blocks, x, y, z);
                k1 += this.rand.nextInt(4);
            }

            for (int l1 = 0; l1 < k1; ++l1)
            {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

                if (this.rand.nextInt(10) == 0)
                {
                    f2 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
                }

                this.generateCaveNode(this.rand.nextLong(), chunkX, chunkZ, blocks, x, y, z, f2, f, f1, 0, 0, 1.0D);
            }
        }
    }

    protected boolean isOceanBlock(Block[] data, int index, int x, int y, int z, int chunkX, int chunkZ)
    {
        return data[index] == Blocks.water;
    }

    //Exception biomes to make sure we generate like vanilla
    private boolean isExceptionBiome(BiomeGenBase biome)
    {
        if (biome == BiomeGenBase.mushroomIsland) return true;
        if (biome == BiomeGenBase.beach) return true;
        if (biome == BiomeGenBase.desert) return true;
        return false;
    }

    //Determine if the block at the specified location is the top block for the biome, we take into account
    //Vanilla bugs to make sure that we generate the map the same way vanilla does.
    private boolean isTopBlock(Block[] data, int index, int x, int y, int z, int chunkX, int chunkZ)
    {
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(x + chunkX * 16, z + chunkZ * 16);
        return (isExceptionBiome(biome) ? data[index] == Blocks.grass : data[index] == biome.topBlock);
    }

    /**
     * Digs out the current block, default implementation removes stone, filler, and top block
     * Sets the block to lava if y is less then 10, and air other wise.
     * If setting to air, it also checks to see if we've broken the surface and if so 
     * tries to make the floor the biome's top block
     * 
     * @param data Block data array
     * @param index Pre-calculated index into block data
     * @param x local X position
     * @param y local Y position
     * @param z local Z position
     * @param chunkX Chunk X position
     * @param chunkZ Chunk Y position
     * @param foundTop True if we've encountered the biome's top block. Ideally if we've broken the surface.
     */
    protected void digBlock(Block[] data, int index, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop)
    {
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(x + chunkX * 16, z + chunkZ * 16);
        Block top    = (isExceptionBiome(biome) ? Blocks.grass : biome.topBlock);
        Block filler = (isExceptionBiome(biome) ? Blocks.dirt  : biome.fillerBlock);
        Block block  = data[index];

        if (block == Blocks.stone || block == filler || block == top)
        {
            if (y < 10)
            {
                data[index] = Blocks.lava;
            }
            else
            {
                data[index] = Blocks.air;

                if (foundTop && data[index - 1] == filler)
                {
                    data[index - 1] = top;
                }
            }
        }
    }
    
}
