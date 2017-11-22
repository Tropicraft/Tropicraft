package net.tropicraft.core.common.worldgen.mapgen;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.tropicraft.core.common.block.BlockSeaweed;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityShark;
import net.tropicraft.core.common.enums.TropicraftCorals;
import net.tropicraft.core.common.worldgen.perlin.generator.RidgedMulti;
import net.tropicraft.core.registry.BlockRegistry;

public class MapGenUndergroundWaterCove {

    private World worldObj;

    public int centerX = 0;
    public int centerZ = 0;
    public double length;
    public double width;
    public double height;
    public int y;

    public ArrayList<Pair<BlockPos, TileEntity>> pendingTileEntities = new ArrayList<Pair<BlockPos, TileEntity>>();

    public boolean pendingBossSpawn = true;

    public MapGenUndergroundWaterCove(World worldObj) {
        this.worldObj = worldObj;
    }

    public ChunkPrimer generate(int x, int z, ChunkPrimer primer) {
        BlockPos coveCoords = getCoveNear(worldObj, x, z);
        BlockFalling.fallInstantly = true;

        if (coveCoords != null) {
            centerX = coveCoords.getX();
            y = coveCoords.getY();
            centerZ = coveCoords.getZ();
            x *= 16;
            z *= 16;
        } else {
            pendingBossSpawn = true;
            return primer;
        }

        Random rand = new Random(worldObj.getSeed() * centerX + centerZ * 57647382913L);

        RidgedMulti ridged = new RidgedMulti(rand.nextLong(), 1);
        ridged.frequency = 0.0325;

        length = rand.nextInt(20) + 30;
        width = rand.nextInt(20) + 30;
        height = rand.nextInt(3) + 5;

        length *= length;
        width *= width;
        height *= height;

        for (int blockX = 0; blockX < 16; blockX++) {
            for (int blockZ = 0; blockZ < 16; blockZ++) {
                int relativeX = (x + blockX) - centerX;
                int relativeZ = (z + blockZ) - centerZ;
                relativeX *= relativeX;
                relativeZ *= relativeZ;
                for (double j = -height; j < height; j++) {
                    if ((relativeX / length) + ((j * j) / height) + (relativeZ / width) <= 1) {
                        if (j >= -1) {
                            // System.out.println("Placed Packed Purified Sand "+x+" "+y+" "+z);
                            placeBlock(blockX, y + (int) j + 1, blockZ,
                                    BlockRegistry.blockPackedPurifiedSand.getDefaultState(), primer);
                            placeBlock(blockX, y + (int) j + 2, blockZ, BlockRegistry.sands.getDefaultState(), primer);

                            // }
                        }
                        placeBlock(blockX, y + (int) j, blockZ, BlockRegistry.tropicsWater.getDefaultState(), primer);

                    }
                }

                double noise1 = ridged.getNoise(x + blockX, z + blockZ);
                double noise2 = ridged.getNoise(x + blockX + 15432, z + blockZ + 42314);
                int j = (int) Math.sqrt(height - ((height * relativeX) / length) - ((height * relativeZ) / width));

                placeBlock(blockX, y - j - 1, blockZ, BlockRegistry.sands.getDefaultState(), primer);
                placeBlock(blockX, y - j - 2, blockZ, BlockRegistry.blockPackedPurifiedSand.getDefaultState(), primer);

                if (noise1 > 0.875 || noise2 > 0.855) {
                    // placeBlock(i, y - j - 1, k, BlockRegistry.sands.getDefaultState(), primer);

                    double tunnelHeight = (8 - (((relativeX / 2500D) + (relativeZ / 2500D)) * 2)) / 3;

                    if (tunnelHeight < 5) {
                        tunnelHeight = 5;
                    }
                    for (int j2 = 0; j2 < tunnelHeight; j2++) {
                        placeBlock(blockX, y - j + j2, blockZ, BlockRegistry.tropicsWater.getDefaultState(), primer);
                    }

                    // Decorate with coral
                    if (rand.nextInt(15) == 0) {
                        placeBlock(blockX, y - j, blockZ,
                                BlockRegistry.coral.defaultForVariant(
                                        TropicraftCorals.values()[rand.nextInt(TropicraftCorals.values().length)]),
                                primer);
                    }

                    if (rand.nextInt(80) == 0) {
                        placeBlock(blockX, y - j - 1, blockZ, Blocks.SPONGE.getStateFromMeta(1), primer);
                    }

                    // Decorate with seaweed
                    if (rand.nextInt(45) == 0) {
                        placeBlock(blockX, y - j - 1, blockZ, BlockRegistry.seaweed.getDefaultState(), primer);

                        // Remove any potential blocking object
                        placeBlock(blockX, y - j, blockZ, BlockRegistry.tropicsWater.getDefaultState(), primer);

                        // Queue addition of seaweed's tileentity
                        queueTE(x + blockX, y - j - 1, z + blockZ, new BlockSeaweed.TileSeaweed());
                    }

                }
            }
        }

        BlockFalling.fallInstantly = false;
        return primer;
    }

    public void queueTE(int x, int y, int z, TileEntity e) {
        this.pendingTileEntities.add(Pair.of(new BlockPos(x, y, z), e));
    }

    public void decorate(int chunkX, int chunkZ) {
        if (pendingTileEntities.size() > 0) {
            for (int o = 0; o < pendingTileEntities.size(); o++) {
                BlockPos tepos = pendingTileEntities.get(o).getLeft();
                if (tepos != null) {
                    TileEntity t = worldObj.getTileEntity(tepos);
                    if (t == null) {
                        TileEntity te = pendingTileEntities.get(o).getRight();
                        worldObj.setTileEntity(tepos, te);
                        worldObj.markChunkDirty(tepos, te);
                        te.markDirty();
                    }
                }
            }
            pendingTileEntities.clear();
        }

        if (!this.canGenCoveAtCoords(worldObj, chunkX, chunkZ)) {
            return;
        }

        if (pendingBossSpawn) {
            EntityShark shark = new EntityShark(worldObj).setBoss();
            shark.setPosition(centerX, y, centerZ);
            worldObj.spawnEntity(shark);
            System.out.println("shark boss spawned at " + centerX + " " + y + " " + centerZ);
            pendingBossSpawn = false;
        }
    }

    public int getHeightAt(int x, int z) {
        int relativeX = x - centerX;
        int relativeZ = z - centerZ;
        relativeX *= relativeX;
        relativeZ *= relativeZ;
        return y - (int) Math.sqrt(height - ((height * relativeX) / length) - ((height * relativeZ) / width));
    }

    /**
     * Method to choose spawn locations for volcanos (borrowed from village gen)
     * Rarity is determined by the numChunks/offsetChunks vars (smaller numbers mean
     * more spawning)
     */
    protected boolean canGenCoveAtCoords(World worldObj, int i, int j) {
        byte numChunks = 32;
        byte offsetChunks = 0;
        int oldi = i;
        int oldj = j;

        if (i < 0) {
            i -= numChunks - 1;
        }

        if (j < 0) {
            j -= numChunks - 1;
        }

        int randX = i / numChunks;
        int randZ = j / numChunks;
        long seed = (long) randX * 341832132712L + (long) randZ * 422843987541L + worldObj.getWorldInfo().getSeed()
                + (long) 42231726;
        Random rand = new Random(seed);
        randX *= numChunks;
        randZ *= numChunks;
        randX += rand.nextInt(numChunks - offsetChunks);
        randZ += rand.nextInt(numChunks - offsetChunks);

        if (oldi == randX && oldj == randZ) {
            return true;
        }

        return false;
    }

    /**
     * Returns the coordinates of a grove if it should be spawned near this chunk,
     * otherwise returns null. The posY of the returned object should be used as the
     * grove radius
     */
    public BlockPos getCoveNear(World worldObj, int i, int j) {
        int range = 4;
        for (int x = i - range; x <= i + range; x++) {
            for (int z = j - range; z <= j + range; z++) {
                Random rand = new Random(worldObj.getSeed() * x + z * 57647382913L);
                if (canGenCoveAtCoords(worldObj, x, z)) {
                    return new BlockPos(x * 16 + 8, rand.nextInt(5) + 40, z * 16 + 8);
                }
            }
        }
        return null;
    }

    private void placeBlock(int x, int y, int z, IBlockState block, ChunkPrimer primer) {
        primer.setBlockState(x, y, z, block);
    }
}
