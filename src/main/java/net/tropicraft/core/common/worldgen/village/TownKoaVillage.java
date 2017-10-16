package net.tropicraft.core.common.worldgen.village;

import build.BuildServerTicks;
import build.ICustomGen;
import build.UtilBuild;
import build.world.Build;
import build.world.BuildJob;
import build.world.BuildManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.entity.passive.EntityKoaHunter;
import net.tropicraft.core.common.town.SpawnLocationData;
import net.tropicraft.core.common.town.TownObject;

import java.io.File;

public class TownKoaVillage extends TownObject implements ICustomGen {

    //from schematic
    public int areaLength = TownKoaVillageGenHelper.areaLength; //Z for rot 0
    public int areaWidth = TownKoaVillageGenHelper.areaWidth; //X for rot 0
    public int areaHeight = TownKoaVillageGenHelper.areaHeight;

    public int direction = 1;

    //spawn coords pertaining to each member, in a static order
    //0: shaman
    //1: trader
    //2-11: fishers
    //12-21: hunters
    //public List<BlockPos> listCoordsSpawn = new ArrayList<BlockPos>();

    public TownKoaVillage() {
        super();
    }

    @Override
    public void tickUpdate() {
        super.tickUpdate();

        if (getWorld().getTotalWorldTime() % 20 == 0) {
            //System.out.println("koa village tick - " + spawn.getX() + ", " + spawn.getZ() + " - E/PE: " + listLoadedEntities.size() + "/" + listPersistantEntities.size());
        }
    }

    @Override
    public void initFirstTime() {
        super.initFirstTime();

        generateSpawnCoords();

        genStructure();
    }

    public void genStructure() {
        genSchematic();
    }

    public void genSchematic() {

        int yOffset = 0;//-1;

        Build mainStructureData = new Build(spawn.getX(), spawn.getY() + yOffset, spawn.getZ(), UtilBuild.getSaveFolderPath() + "schematics" + File.separator + "koavillage");
        /*BlockPos coords = getBuildingCornerCoord();
		mainStructureData.map_coord_minX = coords.getX();
		mainStructureData.map_coord_minY = coords.getY();
		mainStructureData.map_coord_minZ = coords.getZ();*/


        BuildJob bj = new BuildJob(-99, spawn.getX(), spawn.getY() + yOffset, spawn.getZ(), mainStructureData);
    	/*coords = getBuildingCornerCoord();
		bj.build_startX = coords.getX();
		bj.build_startY = coords.getY();
		bj.build_startZ = coords.getZ();*/
        bj.build.dim = getWorld().provider.getDimension();
        bj.useFirstPass = false; //skip air setting pass
        bj.useRotationBuild = true;
        bj.build_rate = 100;
        bj.notifyFlag = 0;
        bj.setDirection(direction);
        bj.customGenCallback = this;
        //bj.blockIDsNoBuildOver.add(HostileWorlds.blockSourceStructure);

        //set stronghold height size to height of schematic
        areaHeight = mainStructureData.map_sizeY;

        BuildServerTicks.buildMan.addBuild(bj);
    }

    public void spawnEntitiesForce() {

        System.out.println("Spawning koa village population for village: " + spawn);
        tickMonitorPersistantMembers();
		
		
		
		/*System.out.println("TEMP SETTING VILLAGE SIZE");
		areaLength = TownKoaVillageGenHelper.areaLength = 76; //Z for rot 0
		areaWidth = TownKoaVillageGenHelper.areaWidth = 86; //X for rot 0
		areaHeight = TownKoaVillageGenHelper.areaHeight = 16;*/


        //int spawnRadius = 1;

        //stuff spawned in the relative positive directions needs a -1 for z, but not ALL of them, wtf?

        //int fix = 0;
		
		
		
		/*spawnEntityRel("shaman", 0);
		spawnEntityRel("trader", 1);
		
		for (int i = 2; i < listCoordsSpawn.size(); i++) {
			spawnEntityRel("hunter", i);
			spawnEntityRel("fisher", i);
		}*/

        //spawnEntityRel("hunter", 3);
        //spawnEntityRel("hunter", 4);
    }

    public void generateSpawnCoords() {

        int y = 2;


        //listCoordsSpawn.clear();
        //shaman
        //listCoordsSpawn.add(getRotatedCoordsWithRelFromCenter(34, 2+y, 0));
        //listCoordsSpawn.add(getRotatedCoordsWithRelFromCorner(77, 2+y, 37));
        registerSpawnLocation(new SpawnLocationData(getRotatedCoordsWithRelFromCorner(77, 2 + y, 37), "shaman"));
        //trader
        //listCoordsSpawn.add(getRotatedCoordsWithRelFromCenter(-17, y, 0));
        //25 37 is true position for trader best spot in rotation 0 for double odd sized village
        registerSpawnLocation(new SpawnLocationData(getRotatedCoordsWithRelFromCorner(25, y, 37), "trader"));
        //listCoordsSpawn.add(getRotatedCoordsWithRelFromCorner(26, y, 38));

        //listCoordsSpawn.add(getRotatedCoordsWithRelFromCorner(24, y, 37));

        //huts, left side front to back
        registerSpawnLocation(getRotatedCoordsWithRelFromCorner(23, 1 + y, 20), "fisher", "hunter");
        registerSpawnLocation(getRotatedCoordsWithRelFromCorner(38, 1 + y, 14), "fisher", "hunter");
        registerSpawnLocation(getRotatedCoordsWithRelFromCorner(57, 1 + y, 3), "fisher", "hunter");
        registerSpawnLocation(getRotatedCoordsWithRelFromCorner(63, 1 + y, 17), "fisher", "hunter");
        registerSpawnLocation(getRotatedCoordsWithRelFromCorner(69, 1 + y, 3), "fisher", "hunter");
        //-787 88 -233
        //-768 88 -244
        //-762 -230
        //-756 -244

        //huts, right side front to back
        //-802 -193
        //-787 -187
        //-768 -176
        //-762 -190
        //-756 -176
        registerSpawnLocation(getRotatedCoordsWithRelFromCorner(23, 1 + y, 54), "fisher", "hunter");
        registerSpawnLocation(getRotatedCoordsWithRelFromCorner(38, 1 + y, 60), "fisher", "hunter");
        registerSpawnLocation(getRotatedCoordsWithRelFromCorner(57, 1 + y, 71), "fisher", "hunter");
        registerSpawnLocation(getRotatedCoordsWithRelFromCorner(63, 1 + y, 57), "fisher", "hunter");
        registerSpawnLocation(getRotatedCoordsWithRelFromCorner(69, 1 + y, 71), "fisher", "hunter");

        //test koa
        //listCoordsSpawn.add(getRotatedCoordsWithRelFromCorner(0, y, 0));

        //listCoordsSpawn.add(getRotatedCoords(-5, 2, 0));
        //listCoordsSpawn.add(getRotatedCoords(-10, 2, 0));

        //listCoordsSpawn.add(getRotatedCoordsWithRelFromCenter(0, 2, 0));
    }

    public BlockPos getRotatedCoordsWithRelFromCorner(int x, int y, int z) {

        //interesting bug, rotation 0 returns relative coords, rotation 1 returns absolute coords

        BlockPos coords = new BlockPos(/*areaWidth/2 + */x, y, /*areaLength/2 + */z);
        return BuildManager.rotateNew(coords, direction,
                new Vec3d(0, 0, 0),/*Vec3d.createVectorHelper(spawn.getX(), spawn.getY(), spawn.getZ()),*/
                new Vec3d(areaWidth, areaHeight, areaLength));
    }
	
	/*public void spawnEntityRel(String parType, int memberID) {
		
		BlockPos coords = listCoordsSpawn.get(memberID);
		
		BlockPos coords = BuildManager.rotateNew(new BlockPos(MathHelper.floor_double(parCoords.xCoord), MathHelper.floor_double(parCoords.yCoord), MathHelper.floor_double(parCoords.zCoord)), direction, 
				Vec3d.createVectorHelper(spawn.getX(), spawn.getY(), spawn.getZ()), 
				Vec3d.createVectorHelper(areaWidth, areaHeight, areaLength));
		
		EntityKoaBase ent = null;
		
		if (parType.equals("fisher")) {
			ent = new EntityKoaFisher(getWorld());
		} else if (parType.equals("hunter")) {
			ent = new EntityKoaHunter(getWorld());
		} else if (parType.equals("trader")) {
			ent = new EntityKoaTrader(getWorld());
		} else if (parType.equals("shaman")) {
			ent = new EntityKoaShaman(getWorld());
		}
		
		if (ent != null) {
			ent.getAIAgent().setManagedLocation(this);
			ent.setPosition(spawn.getX() + coords.getX() + 0.5F, spawn.getY() + coords.getY(), spawn.getZ() + coords.getZ() + 0.5F);
			//ent.setPosition(parCoords.xCoord + 0.5F, parCoords.yCoord, parCoords.zCoord + 0.5F);
			getWorld().spawnEntityInWorld(ent);
			addEntity(parType, ent);
			ent.onSpawnWithEgg(null);
		}
		
		//TODO: register entities with managedlocation, how are ids managed?
	}*/

    public void addEntity(String unitType, EntityLivingBase ent, int parMemberID) {
        super.addEntity(unitType, ent);
    }

    @Override
    public void spawnMemberAtSpawnLocation(SpawnLocationData parData) {
        super.spawnMemberAtSpawnLocation(parData);

        EntityKoaBase ent = null;
		
		/*if (parData.type.equals("fisher")) {
			ent = new EntityKoaFisher(getWorld());
		} else if (parData.type.equals("hunter")) {
			ent = new EntityKoaHunter(getWorld());
		} else if (parData.type.equals("trader")) {
			ent = new EntityKoaTrader(getWorld());
		} else if (parData.type.equals("shaman")) {
			ent = new EntityKoaShaman(getWorld());
		}*/

        ent = new EntityKoaHunter(getWorld());

        if (false && ent != null) {
            //ent.getAIAgent().setManagedLocation(this);
            ent.setPosition(spawn.getX() + parData.coords.getX() + 0.5F, spawn.getY() + parData.coords.getY(), spawn.getZ() + parData.coords.getZ() + 0.5F);
            //ent.setPosition(parCoords.xCoord + 0.5F, parCoords.yCoord, parCoords.zCoord + 0.5F);
            getWorld().spawnEntity(ent);
            addEntity(parData.type, ent);
            parData.entityUUID = ent.getPersistentID();
            ent.onInitialSpawn(getWorld().getDifficultyForLocation(ent.getPosition()), null);
        }
    }

    @Override
    public void genPassPre(World world, BuildJob parBuildJob, int parPass) {
        if (parPass == -1) {
            spawnEntitiesForce();
        }
    }

    @Override
    public NBTTagCompound getInitNBTTileEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound var1) {
        super.readFromNBT(var1);
        direction = var1.getInteger("direction");

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound var1) {
        super.writeToNBT(var1);
        var1.setInteger("direction", direction);
        return var1;
    }

}
