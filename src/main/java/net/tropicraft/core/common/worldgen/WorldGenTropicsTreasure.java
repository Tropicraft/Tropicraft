package net.tropicraft.core.common.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.tropicraft.core.common.block.BlockTropicraftSands;
import net.tropicraft.core.common.enums.TropicraftSands;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.LootRegistry;

public class WorldGenTropicsTreasure extends TCGenBase {

    private static final List<Block> sandBlocks = new ArrayList<Block>();

    public WorldGenTropicsTreasure(World worldObj, Random rand) {
        super(worldObj, rand);
        sandBlocks.add(Blocks.SAND);
        sandBlocks.add(Blocks.SANDSTONE);
        sandBlocks.add(BlockRegistry.sands);   
    }

    @Override
    public boolean generate(BlockPos pos) {
        int i = pos.getX(), j = pos.getY(), k = pos.getZ();
        int depth = rand.nextInt(1) + 2;

        IBlockState coloredSand = BlockRegistry.sands.getBlockState().getBaseState().withProperty(BlockTropicraftSands.VARIANT, TropicraftSands.getRandomSand(worldObj.rand));

        tryagain:
            for (int tries = 0; tries < 10; tries++) {
                int x = (i + rand.nextInt(4)) - rand.nextInt(4);
                int z = (k + rand.nextInt(4)) - rand.nextInt(4);
                j = getTerrainHeightAt(x, z) - 1;
                int y = j;
                int sandArea = 2;

                // Check the surface level to make sure there's a 5x5 sand area to gen on
                for (int surroundZ = z - sandArea; surroundZ <= z + sandArea; surroundZ++) {
                    for (int surroundX = x - sandArea; surroundX <= x + sandArea; surroundX++) {
                        BlockPos pos3 = new BlockPos(surroundX, j, surroundZ);
                        if (!sandBlocks.contains(worldObj.getBlockState(pos3).getBlock())) {
                            continue tryagain;
                        }
                    }
                }

                // Check the chest pos, make sure it's sand
                BlockPos chestPos = new BlockPos(x, y - depth, z);
                if (!sandBlocks.contains(worldObj.getBlockState(chestPos).getBlock())) {
                    continue tryagain;
                }

                // Draw the X that marks the spot
                int count = 0;
                BlockPos xPos = new BlockPos(x - sandArea, j, z - sandArea);
                worldObj.setBlockState(xPos, coloredSand);
                while (count <= sandArea) {
                    worldObj.setBlockState(xPos.add(-count, 0, -count), coloredSand);
                    worldObj.setBlockState(xPos.add(count, 0, count), coloredSand);
                    worldObj.setBlockState(xPos.add(count, 0, -count), coloredSand);
                    worldObj.setBlockState(xPos.add(-count, 0, count), coloredSand);
                    count++;
                }

                // Place a chest under the X somewhere, fill it
                if (!worldObj.isRemote) {
                    chestPos = new BlockPos(x - sandArea, y - depth, z - sandArea);
                    worldObj.setBlockState(chestPos, BlockRegistry.bambooChest.getDefaultState(), 2);

                    TileEntityChest chest = (TileEntityChest) worldObj.getTileEntity(chestPos);
                    if (chest == null) {
                        return false;
                    }

                    chest.setLootTable(LootRegistry.buriedTreasure, rand.nextLong());

                    // Add filled map to chest
                    ItemStack map = new ItemStack(Items.FILLED_MAP);
                    initializeMap(worldObj, map, chestPos);
                    chest.setInventorySlotContents(rand.nextInt(chest.getSizeInventory()), map);
                }

                return true;
            }
        return true;
    }

    /**
     * Initialize the map for the realm
     * @param worldObj World object
     * @param mapItem Map object
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    private void initializeMap(World worldObj, ItemStack mapItem, BlockPos pos) {
        mapItem.setItemDamage(worldObj.getUniqueDataId("map"));
        String mapName = "map_" + mapItem.getItemDamage();
        MapData data = new MapData(mapName);
        worldObj.setData(mapName, data);
        data.xCenter = pos.getX();
        data.zCenter = pos.getZ();
        data.scale = 3;
        data.dimension = (byte)worldObj.provider.getDimension();
        data.markDirty();
    }

}
