package build.world;

import build.ICustomGen;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BuildJob {

	public int id = 0;

	public Build build;
	
	public int offset = 0;
	public int build_rate = 1500;
	public int build_rand = 20;
	
	public int timeout = 0;

	public int pass = 0;
	public List<Block> blockIDsSkipFirstPass = new ArrayList();
	public List<Block> blockIDsNoBuildOver = new ArrayList();
	
	public boolean build_active = false;
	public int build_currentTick = 0;
	public int build_loopTickX = 0;
	public int build_loopTickY = 0;
	public int build_loopTickZ = 0;
	public int build_startX = 0;
	public int build_startY = 0;
	public int build_startZ = 0;
    
    public int curLayerCount = 0;
    public int curLayerCountMax = 0;
    
    public boolean doRandomBuild = false;
    
    public boolean useRotationBuild = false;
    public boolean centerBuildIfNoRotate = true;
    public int direction = 0;
    public float rotation = 0;
    
    public boolean useFirstPass = true;
    public boolean neverPlaceAir = false;
    
    public ICustomGen customGenCallback = null;
    public int customGenOffset = 0;
    
    public int buildRate = 99999999;
    
    public int notifyFlag = 2;
    

	
	//visual data - CAUSES OVERLAP FOR BUILDJOBS BOTH USING SAME BUILD OBJECT!
	public int curTick = 0; //MOVE TO BUILDJOB FOR SHARED BUILDS!
	public int maxTicks = 0; //MOVE TO BUILDJOB FOR SHARED BUILDS!
	public boolean build_blockPlaced[][][]; //MOVE TO BUILDJOB FOR SHARED BUILDS!
	
	//REFACTOR THESE NAMES TO THE ABOVE COMMENTED OUT ONES ONCE COMPILING
	/*public int map_sizeX = 0;
	public int map_sizeY = 0;
	public int map_sizeZ = 0;
	public int map_coord_minX = 0;
	public int map_coord_minY = 0;
	public int map_coord_minZ = 0;*/
	
	public BuildJob(int parID, int x, int y, int z, String parFile) {
		id = parID;
		
		build_startX = x;
		build_startY = y;
		build_startZ = z;
		
		build = new Build(x, y, z, parFile);
	}
	
	public BuildJob(int parID, int x, int y, int z, Build parBuild) {
		id = parID;
		
		build_startX = x;
		build_startY = y;
		build_startZ = z;
		
		build = parBuild;
	}
	
	public BuildJob(int parID, Build parBuild) {
		id = parID;
		
		//wat
		build_startX = parBuild.map_coord_minX;
		build_startY = parBuild.map_coord_minY;
		build_startZ = parBuild.map_coord_minZ;
		
		/*if (parBuild.map_coord_minY == ZCGame.ZCWorldHeight) {
			//System.out.println("offset adjust");
			if (false) build_startY = parBuild.map_coord_minY + parBuild.map_surfaceOffset;
		}*/
		
		build = parBuild;
	}
	
	public void setDirection(int dir) {
		direction = dir;
		
		//this used to have + 180 on it, i guess to fix something with old rotation, its removed now
		rotation = (dir * 90)/* + 180*/;
	}
	
	public void load() {
		
	}
	
	public void start() {
		
	}
	
	public void updateTick() {
		
	}
	
	public void newBuild(int x, int y, int z) {
		//InputStream is;// = this.getClass().getResourceAsStream("N:\\dev\\Game Dev\\minecraft\\modding\\mcp50 fishymerge\\jars\\" + file);
		
		//is = new FileInputStream(file);
		
		//int x = (int)mc.thePlayer.posX+5;
		//int y = (int)mc.thePlayer.posY-1;
		//int z = (int)mc.thePlayer.posZ+5;
		
		blockIDsSkipFirstPass = new LinkedList();
		blockIDsSkipFirstPass.add(Blocks.TNT);
		blockIDsSkipFirstPass.add(Blocks.UNPOWERED_REPEATER);
		blockIDsSkipFirstPass.add(Blocks.POWERED_REPEATER);
		blockIDsSkipFirstPass.add(Blocks.UNPOWERED_COMPARATOR);
		blockIDsSkipFirstPass.add(Blocks.POWERED_COMPARATOR);
		blockIDsSkipFirstPass.add(Blocks.REDSTONE_WIRE);
		blockIDsSkipFirstPass.add(Blocks.REDSTONE_TORCH);
		blockIDsSkipFirstPass.add(Blocks.UNLIT_REDSTONE_TORCH);
		blockIDsSkipFirstPass.add(Blocks.TORCH);
		
		int offset = 0;
		
		y += offset;
		
		//if (tryBuild(x, y, z, curBuild)) {
			build_active = true;
		//}
		
		build_currentTick = 0;
    	build_loopTickX = 0;
    	build_loopTickY = 0;
    	build_loopTickZ = 0;
    	pass = 0;
		
		build_startX = x;// - (build.map_sizeX / 2);
    	build_startY = y;
    	build_startZ = z;// - (build.map_sizeZ / 2);
	}
	
	public void buildStart() {
    	resetBuildState();
    	newBuild(build_startX, build_startY, build_startZ);
    	pass = useFirstPass ? 0 : 1;
    	if (customGenCallback != null) customGenCallback.genPassPre(DimensionManager.getWorld(build.dim), this, pass);
    	//build_startX = this.build.map_coord_minX;
    	//build_startY = this.build.map_coord_minY;
    	//build_startZ = this.build.map_coord_minZ;
    	
    	//shouldEntitiesReset = true;
    	//System.out.println("Level build starting");
    }
    
    public void buildComplete() {
    	//shouldEntitiesReset = false;
    	build_active = false;
		doRandomBuild = true;
		//mod_ZombieCraft.worldRef.editingBlocks = false;
		//spawnLevelEntities();
		
		//System.out.println("Level build complete");
    }
    
    
    
    public void resetBuildState() {
    	build_blockPlaced = new boolean
        [build.map_sizeX]
        [build.map_sizeY]
        [build.map_sizeZ];
		
		for (int xx = 0; xx < build.map_sizeX; xx++) {
			for (int yy = 0; yy < build.map_sizeY; yy++) {
				for (int zz = 0; zz < build.map_sizeZ; zz++) {
					int index = yy * build.map_sizeX * build.map_sizeZ + zz * build.map_sizeX + xx;
					build_blockPlaced[xx][yy][zz] = false;
					
				}
			}
		}
		
		curTick = 0;
		maxTicks = build.map_sizeX * build.map_sizeY * build.map_sizeZ * 3; //3 build passes
    }
}
