package build.world;

import build.SchematicData;
import build.UtilBuild;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Build {

    //1.6.4, uses internal ID to runtime ID, created by storing internal ID to 'unlocalizedname', and finding a block with the unlocalized name to find the final ID to set

    //for 1.7.2, for reading:
    //if version 1.0: we must use old system for sake of converting old map format to new runtime requirements
    //if version 1.1: we use the 'registered name' for block instead of 'unlocalized name'

    //for writing: we just use new 1.1 system

    public static int blockIDHighestVanilla = 255; //figure out an elegant non loop crazy way to determine this automatically

    //public int id = 0;
    public String file = "";

    //Basic placement data
    /*public int startX = 0;
	public int startY = 0;
	public int startZ = 0;
	
	public int sizeX = 0;
	public int sizeY = 0;
	public int sizeZ = 0;*/

    //world data
    public NBTTagCompound levelData = null;
    public NBTTagList tileEntities;
    public NBTTagList entities;
    public Block build_blockIDArr[][][];
    public int build_blockMetaArr[][][];

    //afaik, this is saved internal ID to runtime ID (changed runtime ID to Block for 1.7.x)
    //public HashMap<Integer, Block> blockMappingInternalIDToRuntimeID = new HashMap<Integer, Block>();

    //for 1.7.x, we need to still save to ID for filesize, so we need final data to be internal ID to Block
    public HashMap<Integer, Block> blockMappingInternalIDToBlock = new HashMap<Integer, Block>();

    //REFACTOR THESE NAMES TO THE ABOVE COMMENTED OUT ONES ONCE COMPILING
    public int map_sizeX = 0;
    public int map_sizeY = 0;
    public int map_sizeZ = 0;
    public int map_coord_minX = 0;
    public int map_coord_minY = 0;
    public int map_coord_minZ = 0;
    public int map_surfaceOffset = 0;

    public int dim = 0;

    //the new format
    //- was to use integers instead of bytes, but that even wont be needed if unlocalized names are used (though use integer for saving out
    //defaults to true now, lets old formats load in a missing key setting it to false
    public boolean newFormat = true;

    //1.0 for 1.6.4: tells system to use the blockTranslationMap
    //1.1 for 1.7.2/10: tells system to do whatever i need to do different. i might still use translation map to keep schematic size to a minimum
    public String version = "1.0"; //default to 1.0 for importing old schematics that have missing nbt for this

    public boolean backwardsCompatibleOldRotate = false;

    public Build(int x, int y, int z, String parFile, boolean noLoad) {
        //id = parID;
        file = parFile;

        map_coord_minX = x;
        map_coord_minY = y;
        map_coord_minZ = z;

        if (!noLoad) readNBT(file);
    }

    public String getVersion() {
        return version;
    }

    //latest version to write file as
    public String getSaveVersion() {
        return "1.1";
    }

    public Build(int x, int y, int z, String parFile) {
        this(x, y, z, parFile, false);
    }

    public void load() {

    }

    public void start() {

    }

    public void updateTick() {

    }

    public void setCornerPosition(int x, int y, int z) {
        map_coord_minX = x;
        map_coord_minY = y;
        map_coord_minZ = z;
    }

    public void recalculateLevelSize(int x1, int y1, int z1, int x2, int y2, int z2) {
        recalculateLevelSize(x1, y1, z1, x2, y2, z2, false);
    }

    public void recalculateLevelSize(int x1, int y1, int z1, int x2, int y2, int z2, boolean sizeUp) {

        map_sizeX = 0;
        map_sizeY = 0;
        map_sizeZ = 0;
        map_coord_minX = x1;
        map_coord_minY = y1;
        map_coord_minZ = z1;

        if (x1 > x2) map_coord_minX = x2;
        if (y1 > y2) map_coord_minY = y2;
        if (z1 > z2) map_coord_minZ = z2;

        if (x1 - x2 >= 0) {
            map_sizeX = x1 - x2;
        } else {
            map_sizeX = x2 - x1;
        }

        if (y1 - y2 >= 0) {
            map_sizeY = y1 - y2;
        } else {
            map_sizeY = y2 - y1;
        }

        if (z1 - z2 >= 0) {
            map_sizeZ = z1 - z2;
        } else {
            map_sizeZ = z2 - z1;
        }

        //map_coord_minY--;
        //map_sizeY+=2;
        if (sizeUp) {
            map_sizeX++;
            map_sizeY++;
            map_sizeZ++;
        }

        //System.out.println("Size: " + map_sizeX + "," + map_sizeY + "," + map_sizeZ);
        //System.out.println("map_coord_min: " + map_coord_minX + "," + map_coord_minY + "," + map_coord_minZ);

    }

    public void readNBT(String level) {


        levelData = null;

        try {

            NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(level + ".schematic"));


            levelData = nbttagcompound;

            //only override newFormat if schematic actually tells it to, this also assumes the outside setting is right context...
            //actually lets let it set it to false for missing value, so we can safely assume newFormat all the time unless the key is missing
            //if (nbttagcompound.hasKey("newFormat")) {
            newFormat = nbttagcompound.getBoolean("newFormat");
            //}

            if (nbttagcompound.hasKey("version")) {
                version = nbttagcompound.getString("version");
            } else {
                //already defaulted to 1.0
            }

            if (newFormat) {
                NBTTagCompound nbtMapping = nbttagcompound.getCompoundTag("blockTranslationMap");
                if (getVersion().equals("1.0")) {
                    this.blockMappingInternalIDToBlock = genBlockIDSchemToBlockWithUnlocalizedName(nbtMapping);
                } else if (getVersion().equals("1.1")) {
                    this.blockMappingInternalIDToBlock = genBlockIDSchemToBlockWithRegistry(nbtMapping);
                } else {
                    System.out.println("CRITICAL! BuildMod does not support version: " + getVersion());
                }
            } else {
                System.out.println("TODO: a fallback lookup table for old block IDs to unlocalized names");
                NBTTagCompound nbtFallbackLookup = CompressedStreamTools.readCompressed(new FileInputStream(UtilBuild.getSaveFolderPath() + "BlockTranslationMapFallback.schematic"));
                this.blockMappingInternalIDToBlock = genBlockIDSchemToBlockWithUnlocalizedName(nbtFallbackLookup.getCompoundTag("blockTranslationMap"));
            }

            //System.out.println("TODO for BuildMod: verify 9 is accurate type to use, NBTBase defines type 9 as NBTTagList");
            tileEntities = nbttagcompound.getTagList("TileEntities", 10);
            entities = nbttagcompound.getTagList("Entities", 10);


            int sizeX = map_sizeX = nbttagcompound.getShort("Width");
            int sizeZ = map_sizeZ = nbttagcompound.getShort("Length");
            int sizeY = map_sizeY = nbttagcompound.getShort("Height");

            map_surfaceOffset = nbttagcompound.getShort("surfaceOffset");

            int bPosX = 0;
            int bPosY = 0;
            int bPosZ = 0;

            build_blockIDArr = new Block
                    [sizeX]
                    [sizeY]
                    [sizeZ];

            build_blockMetaArr = new int
                    [sizeX]
                    [sizeY]
                    [sizeZ];
			
			/*build_blockPlaced = new boolean
            [sizeX]
            [sizeY]
            [sizeZ];*/

            if (newFormat) {
                int metadata[] = nbttagcompound.getIntArray("Data");
                int blockids[] = nbttagcompound.getIntArray("Blocks");

                for (int xx = 0; xx < sizeX; xx++) {
                    for (int yy = 0; yy < sizeY; yy++) {
                        for (int zz = 0; zz < sizeZ; zz++) {
                            int index = yy * sizeX * sizeZ + zz * sizeX + xx;
                            //build_blockIDArr[xx][yy][zz] = blockids[index];
                            build_blockMetaArr[xx][yy][zz] = metadata[index];
                            //build_blockPlaced[xx][yy][zz] = false;

                            int internalID = blockids[index];
                            Block block = getConvertInternalIDToBlock(internalID);

                            if (block != null) {

                            } else {
                                System.out.println("CRITICAL! BuildMod: null block when converting a version " + getVersion() + " schematic to block instance, this should be a bug, contact Corosus");
                                block = Blocks.AIR;
                            }

                            build_blockIDArr[xx][yy][zz] = block;

                        }
                    }
                }
            } else {
                System.out.println("Notice: BuildMod detected really old schematic version, noted incase of future import errors");
                byte metadata[] = nbttagcompound.getByteArray("Data");
                byte blockids[] = nbttagcompound.getByteArray("Blocks");

                for (int xx = 0; xx < sizeX; xx++) {
                    for (int yy = 0; yy < sizeY; yy++) {
                        for (int zz = 0; zz < sizeZ; zz++) {
                            int index = yy * sizeX * sizeZ + zz * sizeX + xx;
                            //build_blockIDArr[xx][yy][zz] = blockids[index];
                            build_blockMetaArr[xx][yy][zz] = metadata[index];
                            //build_blockPlaced[xx][yy][zz] = false;

                            int internalID = blockids[index];
                            //fixing old bug using bytes for blockIDs greater than 256
                            if (internalID < 0) {
                                internalID += 256;
                            }
                            Block block = getConvertInternalIDToBlock(internalID);

                            if (block != null) {

                            } else {
                                System.out.println("CRITICAL! BuildMod: null block when converting a version " + getVersion() + " schematic to block instance, this should be a bug, contact Corosus");
                                block = Blocks.AIR;
                            }

                            build_blockIDArr[xx][yy][zz] = block;
                        }
                    }
                }
            }


            file = level;
            //ZCGame.instance.mapMan.curLevel = level;

        } catch (Exception ex) {
            //notification off until generic build copy paste interface is supported for server
            ex.printStackTrace();
        }

    }

    public Block getConvertInternalIDToBlock(int internalID) {
        //WARNING! NEEDS SUPPORT FOR VANILLA BLOCKS! THESE WERE NEVER ADDED TO THE OLD MAPPINGS!

        //time to translate using mapping!
        //int a = build_blockIDArr[xx][yy][zz];
        Block block = null;
        //account for old version not storing vanilla id conversion, as well as new version storing all ids in its own index order
        if (!getVersion().equals("1.0") || internalID > blockIDHighestVanilla) {
            if (blockMappingInternalIDToBlock.containsKey(internalID)) {
                block = blockMappingInternalIDToBlock.get(internalID);
                if (block == null) {
                    System.out.println("CRITICAL! BuildMod: null block when using blockMappingInternalIDToBlock.get(int) for ID " + internalID);
                }
            } else {
                System.out.println("CRITICAL! BuildMod: error finding block internalID to real block for ID " + internalID);
            }
            //protect against javas autocasting primitves, incase of missing entry
			/*if (this.blockMappingInternalIDToRuntimeID.containsKey(a)) {
				Integer bb = this.blockMappingInternalIDToRuntimeID.get(a);
				int b = Integer.valueOf(this.blockMappingInternalIDToRuntimeID.get(a));
				//System.out.println("tra: " + a + " -> " + b);
				build_blockIDArr[xx][yy][zz] = b;
			}*/
        } else {
            block = Block.REGISTRY.getObjectById(internalID);
            if (block == null) {
                System.out.println("CRITICAL! BuildMod: null block when using Block.blockRegistry.getObjectById(int) for ID " + internalID);
            }
        }
        return block;
    }

    public void resetData() {
        build_blockIDArr = new Block
                [map_sizeX]
                [map_sizeY]
                [map_sizeZ];

        build_blockMetaArr = new int
                [map_sizeX]
                [map_sizeY]
                [map_sizeZ];/*
		
		build_blockPlaced = new boolean
        [map_sizeX]
        [map_sizeY]
        [map_sizeZ];*/
    }

    public void scanLevelToData() {
        scanLevelToData(DimensionManager.getWorld(dim));
    }

    public void scanLevelToData(World worldObj) {

        resetData();

        //if (false) map_surfaceOffset = map_coord_minY - ZCGame.ZCWorldHeight;

        for (int xx = 0; xx < map_sizeX; xx++) {
            for (int yy = 0; yy < map_sizeY; yy++) {
                for (int zz = 0; zz < map_sizeZ; zz++) {
                    int index = yy * map_sizeX * map_sizeZ + zz * map_sizeX + xx;

                    World worldRef = worldObj;

                    IBlockState state = worldRef.getBlockState(new BlockPos(map_coord_minX + xx, map_coord_minY + yy, map_coord_minZ + zz));
                    build_blockIDArr[xx][yy][zz] = state.getBlock();
                    build_blockMetaArr[xx][yy][zz] = state.getBlock().getMetaFromState(state);
                    //build_blockPlaced[xx][yy][zz] = false;

                    //System.out.println("build_blockIDArr[xx][yy][zz]: " + build_blockIDArr[xx][yy][zz]);

                    //check for tile entity to write out!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                }
            }
        }
    }

    public void scanWriteNBT() {
        scanLevelToData();
        writeNBT();
    }

    public void writeNBT() {
        //scans over level area and updates all data needed

        HashMap<Block, Integer> mapBlockToInternalID = getBlockToInternalIDMap();

        try {
            if (levelData == null) {
                System.out.println("New NBT Data Object");
                levelData = new NBTTagCompound();
            }

            //Init both formats cause java is bitchy
            int metadataInt[] = new int[map_sizeX * map_sizeY * map_sizeZ];
            int blockidsInt[] = new int[map_sizeX * map_sizeY * map_sizeZ];
            byte metadataByte[] = new byte[map_sizeX * map_sizeY * map_sizeZ];
            byte blockidsByte[] = new byte[map_sizeX * map_sizeY * map_sizeZ];

            NBTTagList var16 = new NBTTagList();

            for (int xx = 0; xx < map_sizeX; xx++) {
                for (int yy = 0; yy < map_sizeY; yy++) {
                    for (int zz = 0; zz < map_sizeZ; zz++) {
                        int index = yy * map_sizeX * map_sizeZ + zz * map_sizeX + xx;

                        if (newFormat) {
                            Integer tryID = mapBlockToInternalID.get(build_blockIDArr[xx][yy][zz]);
                            if (tryID == null) {
                                System.out.println("CRITICAL! BuildMod: converting block to internal ID failed, for block: " + build_blockIDArr[xx][yy][zz]);
                            } else {
                                int internalID = Integer.valueOf(tryID);
                                blockidsInt[index] = internalID;
                            }
                            metadataInt[index] = build_blockMetaArr[xx][yy][zz];
                        } else {
                            System.out.println("CRITICAL BUILDMOD: UNSUPPORTED OLD VERSION OF MAP FOR WRITE OUT, THIS SHOULDNT HAPPEN EVER!");
                            //blockidsByte[index] = (byte)build_blockIDArr[xx][yy][zz];
                            //metadataByte[index] = (byte)build_blockMetaArr[xx][yy][zz];
                        }

                        World worldRef = DimensionManager.getWorld(dim);

                        TileEntity tEnt = worldRef.getTileEntity(new BlockPos(map_coord_minX + xx, map_coord_minY + yy, map_coord_minZ + zz));
                        if (tEnt != null) {
                            NBTTagCompound var10 = new NBTTagCompound();

                            if (tEnt instanceof SchematicData) {
                                ((SchematicData) tEnt).writeToNBT(var10, this);
                            } else {
                                tEnt.writeToNBT(var10);
                            }

                            //adjust coords to be relative to the schematic file
                            var10.setBoolean("coordsSetRelative", true);
                            var10.setInteger("x", xx);
                            var10.setInteger("y", yy);
                            var10.setInteger("z", zz);

                            var16.appendTag(var10);
                        }
                        //check for tile entity to write out!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                    }
                }
            }

            levelData.setTag("TileEntities", var16);

            //somehow get all entities within bounds
            //loadedentity list bounds check

            if (newFormat) {
                levelData.setIntArray("Blocks", blockidsInt);
                levelData.setIntArray("Data", metadataInt);
            } else {
                levelData.setByteArray("Blocks", blockidsByte);
                levelData.setByteArray("Data", metadataByte);
            }
            levelData.setBoolean("newFormat", newFormat);
            levelData.setString("version", getSaveVersion());
            levelData.setTag("blockTranslationMap", genBlockIDToNameMap());

            levelData.setShort("Width", (short) map_sizeX);
            levelData.setShort("Height", (short) map_sizeY);
            levelData.setShort("Length", (short) map_sizeZ);

            levelData.setShort("surfaceOffset", (short) map_surfaceOffset);

            saveLevelData(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //used for helping write out runtime blocks to internalID, this method must match genBlockIDToNameMap() logic
    public HashMap<Block, Integer> getBlockToInternalIDMap() {
        HashMap<Block, Integer> map = new HashMap<Block, Integer>();

        Set set = Block.REGISTRY.getKeys();
        Iterator it = set.iterator();

        int i = 0;
        while (it.hasNext()) {
            ResourceLocation tagName = (ResourceLocation) it.next();
            Block block = Block.REGISTRY.getObject(tagName);

            if (block != null) {
                if (/*i > blockIDHighestVanilla && */!block.getUnlocalizedName().equals("tile.ForgeFiller")) {

                    //old way
                    //nbt.setInteger(block.getUnlocalizedName(), i);

                    //new way
                    //note, for loop index worked as actual block id previously because it stored ID at the right index
                    //hopefully, storing our own index for this will work ok, each schematic has its own translation map so in theory it should work...
                    map.put(block, i);
                    //nbt.setInteger(CoroUtilBlock.getNameByBlock(block), i);
                    i++;
                }
            }
        }
        return map;
    }

    //lets try: still write out using existing block ids, but this map does those ids -> unlocalizedname
    //then on printing, do schematic id -> unlocalizedname -> lookup the new id
    //for write out
    //this method must match getBlockToInternalIDMap() logic
    public NBTTagCompound genBlockIDToNameMap() {
        NBTTagCompound nbt = new NBTTagCompound();

        //if (getVersion().equals("1.0")) {

        Set set = Block.REGISTRY.getKeys();
        Iterator it = set.iterator();

        int i = 0;
        while (it.hasNext()) {
            ResourceLocation tagName = (ResourceLocation) it.next();
            Block block = Block.REGISTRY.getObject(tagName);

            if (block != null) {
                if (/*i > blockIDHighestVanilla && */!block.getUnlocalizedName().equals("tile.ForgeFiller")) {

                    //old way
                    //nbt.setInteger(block.getUnlocalizedName(), i);

                    //new way
                    //note, for loop index worked as actual block id previously because it stored ID at the right index
                    //hopefully, storing our own index for this will work ok, each schematic has its own translation map so in theory it should work...
                    nbt.setInteger(tagName.toString(), i);
                    i++;
                }
            }
        }
			
			/*for (int i = 0; i < Block.blocksList.length; i++) {
	        	Block block = Block.blocksList[i];
	        	if (block != null) {
	        		if (i > blockIDHighestVanilla && !block.getUnlocalizedName().equals("tile.ForgeFiller")) {
	        			//set both lookup methods, or not, since you have to iterate it, make it easier
	        			//nbt.setString("" + i, block.getUnlocalizedName());
	        			nbt.setInteger(block.getUnlocalizedName(), i);
	        		}
	        	}
	        }*/
		/*} else if (getVersion().equals("1.1")) {
			Set set = Block.blockRegistry.getKeys();
	        Iterator it = set.iterator();
	        while (it.hasNext()) {
	        	String tagName = (String) it.next();
	        	Block block = (Block) Block.blockRegistry.getObject(tagName);
	        	
	        	if (block != null)
	            {
	        		
	            }
	        }
		}*/

        return nbt;
    }

    //for read in, more complicated
    public HashMap<Integer, Block> genBlockIDSchemToBlockWithUnlocalizedName(NBTTagCompound parMappingNBT) {
        HashMap<String, Block> swapMap = new HashMap<String, Block>();
        HashMap<Integer, Block> finalMap = new HashMap<Integer, Block>();

        try {

            //first, generate the swapMap for the running mc block name -> id
            Set set = Block.REGISTRY.getKeys();
            Iterator it = set.iterator();
            //int i = 0;
            while (it.hasNext()) {
                ResourceLocation tagName = (ResourceLocation) it.next();
                Block block = Block.REGISTRY.getObject(tagName);

                if (block != null) {
                    if (/*i > blockIDHighestVanilla && */!block.getUnlocalizedName().equals("tile.ForgeFiller")) {
                        swapMap.put(block.getUnlocalizedName(), block);
                        //i++;
                    }
                }
            }
	    	
	    	/*Iterator it = parMappingNBT.func_150296_c().iterator();
			while (it.hasNext()) {
				String tagName = (String) it.next();
				NBTTagCompound data = parMappingNBT.getCompoundTag(tagName);*/
				
			
	        
			/*Collection playerDataCl = parMappingNBT.getTags();
			Iterator it = playerDataCl.iterator();
			
			while (it.hasNext()) {*/
            it = parMappingNBT.getKeySet().iterator();
            while (it.hasNext()) {
                String tagName = (String) it.next();
                int tag = parMappingNBT.getInteger(tagName);//(NBTTagInt)it.next();
                //drunken fix, review in future
                if (swapMap.get(tagName) == null && !tagName.contains("zombiecraft:")) {

                    //this currently blindly fixes non zc issues as well, adding in z_ to scan didnt cover all old zc blocks, hmm
                    tagName = tagName.replace("tile.", "tile.zombiecraft:");

                }
                if (swapMap.get(tagName) != null) {
                    finalMap.put(tag, swapMap.get(tagName));
                } else {
                    System.out.println("missing a mapping in this schematic for: " + tag + " - " + tagName);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return finalMap;
    }

    //for read in, more complicated
    //TODO: CONFIRM CONSISTANCY/LOGIC OF THIS CODE
    public HashMap<Integer, Block> genBlockIDSchemToBlockWithRegistry(NBTTagCompound parMappingNBT) {
        HashMap<String, Block> swapMap = new HashMap<String, Block>();
        HashMap<Integer, Block> finalMap = new HashMap<Integer, Block>();

        try {

            //first, generate the swapMap for the running mc block name -> id
            //for 1.1 i dont think we can blindly just read in like this, the runtime id listing could be different? or is it..... uhhh
            Set set = Block.REGISTRY.getKeys();
            Iterator it = set.iterator();
            //int i = 0;
            while (it.hasNext()) {
                ResourceLocation tagName = (ResourceLocation) it.next();
                Block block = Block.REGISTRY.getObject(tagName);
                if (block != null) {
                    if (/*i > blockIDHighestVanilla && */!block.getUnlocalizedName().equals("tile.ForgeFiller")) {
                        swapMap.put(tagName.toString(), block);
                        //i++;
                    }
                }
            }

            it = parMappingNBT.getKeySet().iterator();
            while (it.hasNext()) {
                String tagName = (String) it.next();
                int tag = parMappingNBT.getInteger(tagName);//(NBTTagInt)it.next();
                if (swapMap.get(tagName) != null) {
                    finalMap.put(tag, swapMap.get(tagName));
                } else {
                    System.out.println("missing a mapping in this schematic for: " + tag);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return finalMap;
    }

    public void saveLevelData(String level) {

        try {

            if (levelData != null) {

                FileOutputStream fos = new FileOutputStream(level + ".schematic");

                CompressedStreamTools.writeCompressed(levelData, fos);

                fos.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public boolean fillBuildData(Build parBuild) {

        FileInputStream fis = null;

        try {
            InputStream is = new FileInputStream(parBuild.file);

            fis = new FileInputStream(parBuild.file);

            NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fis);

            byte metadata[] = nbttagcompound.getByteArray("Data");
            byte blockids[] = nbttagcompound.getByteArray("Blocks");


            if (fis != null) {
                fis.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {


        }
        return true;
    }
}
