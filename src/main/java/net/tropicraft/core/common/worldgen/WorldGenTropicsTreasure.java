package net.tropicraft.core.common.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.LootRegistry;

public class WorldGenTropicsTreasure extends TCGenBase {

    private static final List<Item> treasureList = new ArrayList<Item>();

    private static final List<Block> sandBlocks = new ArrayList<Block>();

    public WorldGenTropicsTreasure(World worldObj, Random rand) {
        super(worldObj, rand);
        sandBlocks.add(Blocks.SAND);
        sandBlocks.add(Blocks.SANDSTONE);
        sandBlocks.add(BlockRegistry.sands);   
    }

    @Override
    public boolean generate(BlockPos pos) {
//        int i = pos.getX(), j = pos.getY(), k = pos.getZ();
//        int depth = rand.nextInt(2) + 2;
//
//        tryagain:
//            for (int tries = 0; tries < 10; tries++) {
//
//                int x = (i + rand.nextInt(8)) - rand.nextInt(8);
//                int z = (k + rand.nextInt(8)) - rand.nextInt(8);
//                j = getTerrainHeightAt(x, z) - 1;
//                int y = j;
//
//                for (; y > j - depth; y--) {
//                    BlockPos pos2 = new BlockPos(x, y, z);
//                    if (!sandBlocks.contains(worldObj.getBlockState(pos2).getBlock())) {
//                        continue tryagain;
//                    }
//                }
//
//                int sandArea = 3;
//                for (int surroundZ = z - sandArea; surroundZ <= z + sandArea; surroundZ++) {
//                    for (int surroundX = x - sandArea; surroundX <= x + sandArea; surroundX++) {
//                        BlockPos pos3 = new BlockPos(surroundX, j, surroundZ);
//                        if (!sandBlocks.contains(worldObj.getBlock(surroundX, j, surroundZ))) {
//                            continue tryagain;
//                        }
//                    }
//                }
//
//                //System.err.println("Generating Treasure chest at: " + i + " " + y + " " + k);
//
//                for (int surroundZ = z - sandArea; surroundZ <= z + sandArea; surroundZ++) {
//                    for (int surroundX = x - sandArea; surroundX <= x + sandArea; surroundX++) {
//                        if (rand.nextFloat() < 0.2f) {
//                            worldObj.setBlock(surroundX, j, surroundZ, TCBlockRegistry.purifiedSand);
//                        }
//                    }
//                }
                if (!worldObj.isRemote) {
                    worldObj.setBlockState(pos, BlockRegistry.bambooChest.getDefaultState(), 2);

                    TileEntityChest tileentitychest = (TileEntityChest) worldObj.getTileEntity(pos);
                    if (tileentitychest == null) {
                        return false;
                    }

                    tileentitychest.setLootTable(LootRegistry.buriedTreasure, rand.nextLong());
                }


                //            boolean hasAddedMap = false;
                //            for (int e = 0; e < 8; e++) {
                //                ItemStack itemstack = /*e == 0 ? new ItemStack(TCItemRegistry.recordBuriedTreasure) : TODO: Add records*/pickCheckLootItem(worldObj, rand, x, y, z);
                //                if (itemstack != null && (itemstack.getItem() != Items.map || !hasAddedMap)) {
                //                    if (itemstack.getItem() == Items.map) {
                //                        hasAddedMap = true;
                //                        initializeMap(worldObj, itemstack, x, y, z);
                //                    }
                //                    tileentitychest.setInventorySlotContents(rand.nextInt(tileentitychest.getSizeInventory()), itemstack);
                //                }
                //            }

          //      return true;
           // }
        return true;
    }
    //
    //    private ItemStack pickCheckLootItem(World worldObj, Random rand, int x, int y, int z) {
    //
    //        Item loot = treasureList.get(rand.nextInt(treasureList.size()));
    //        
    //        if (loot == Items.IRON_INGOT) {
    //            return new ItemStack(loot, rand.nextInt(36) + 1);
    //        } else if (loot == Items.gold_ingot) {
    //            return new ItemStack(loot, rand.nextInt(46) + 1);
    //        } else if (loot == Items.diamond) {
    //            return new ItemStack(loot, rand.nextInt(24) + 6);
    //        } else if (loot == Items.arrow) {
    //            return new ItemStack(loot, rand.nextInt(45) + 1);
    //        } else if (loot == Items.gold_nugget) {
    //            return new ItemStack(loot, rand.nextInt(40) + 1);
    //        } else if (loot == TCItemRegistry.ore) {
    //        	return new ItemStack(loot, rand.nextInt(10) + 5, rand.nextInt(5));
    //        }  else if (loot == TCItemRegistry.shells) {
    //        	return new ItemStack(loot, rand.nextInt(4) + 2, rand.nextInt(6));
    //        }
    //        
    //        return new ItemStack(loot);
    //        
    //    }

    /**
     * Initialize the map for the realm
     * @param worldObj World object
     * @param mapItem Map object
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    //    private void initializeMap(World worldObj, ItemStack mapItem, int x, int y, int z) {
    //        mapItem.setItemDamage(worldObj.getUniqueDataId("map"));
    //        String mapName = "map_" + mapItem.getItemDamage();
    //        MapData data = new MapData(mapName);
    //        worldObj.setItemData(mapName, data);
    //        data.xCenter = x;
    //        data.zCenter = z;
    //        data.scale = 3;
    //    //    data.dimension = (byte)worldObj.worldProvider.worldType;
    //        data.markDirty();
    //    }

    //    static {
    //        // TODO
    //    	//treasureList.add(TropicraftMod.tropicsPortalEnchanter);
    //    	//treasureList.add(mod_tropicraft.PageOfJournal);
    //    	
    //        treasureList.add(Items.IRON_INGOT);
    //        treasureList.add(Items.GOLD_INGOT);
    //        treasureList.add(Items.GOLD_NUGGET);
    //        treasureList.add(Items.DIAMOND);
    //        treasureList.add(ItemRegistry.azurite);
    //        treasureList.add(ItemRegistry.zircon);
    //        treasureList.add(ItemRegistry.eudialyte);
    //        
    //        treasureList.add(TCItemRegistry.shells);
    //        
    //        treasureList.add(TCItemRegistry.scaleHelmet);
    //        treasureList.add(TCItemRegistry.scaleBoots);
    //        treasureList.add(TCItemRegistry.scaleChestplate);
    //        treasureList.add(TCItemRegistry.scaleLeggings);
    //        
    //        treasureList.add(Items.golden_apple);
    //        treasureList.add(Items.arrow);
    //        
    //        treasureList.add(TCItemRegistry.swordEudialyte);
    //        treasureList.add(TCItemRegistry.swordZircon);
    //        
    //        treasureList.add(Items.gold_nugget);
    //        treasureList.add(Items.map);
    //        treasureList.add(Items.spider_eye);
    //        
    //        treasureList.add(TCItemRegistry.recordTradeWinds);
    //        treasureList.add(TCItemRegistry.recordEasternIsles);
    //        treasureList.add(TCItemRegistry.recordBuriedTreasure);
    //        treasureList.add(TCItemRegistry.recordLowTide);
    //        treasureList.add(TCItemRegistry.recordSummering);
    //        treasureList.add(TCItemRegistry.recordTheTribe);
    //        
    //        sandBlocks.add(Blocks.sand);
    //        sandBlocks.add(Blocks.sandstone);
    //        sandBlocks.add(TCBlockRegistry.purifiedSand);        
    //    }
}
