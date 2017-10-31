package net.tropicraft.core.common.build.world;

import net.tropicraft.core.common.build.ITileEntityCustomGenData;
import net.tropicraft.core.common.build.SchematicData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.tropicraft.core.common.block.BlockBundle;
import net.tropicraft.core.common.block.BlockTropicraftLog;
import net.tropicraft.core.common.enums.TropicraftLogs;
import net.tropicraft.core.registry.BlockRegistry;

import java.util.LinkedList;
import java.util.List;

//This class works nice and fast, unless the generated thing is hovering in the air. Lighting calculations going overboard perhaps
//maybe clear area top down to make air clearing pass faster?

//observations for rotateNew when building schematic with rotateNew then placing entities with rotateNew (tropicraft new village):
//- if length AND width are both odd, rotation 1 is 1 block off for an axis
//- if length and width are both even, rotations appear perfect (tested rot 1 against 0)
//- given that, its assumed that either length or width with odd value will cause issues
public class BuildManager {

    public List<BuildJob> activeBuilds;
    public List<String> buildNames;
    public int nextBuildID = 0;

    //Building related below

    //global settings
    public int build_rate = 100000;
    public int build_rand = 20;

    public Block placeholderID = Blocks.STONE; //stone

    public BuildManager() {
        activeBuilds = new LinkedList();
        buildNames = new LinkedList();
    }

    public void updateTick() {
        for (int i = 0; i < activeBuilds.size(); i++) {
            BuildJob bj = activeBuilds.get(i);
            if (bj != null) {
                if (bj.build_active) {
                    bj.updateTick();
                    updateBuildProgress(bj);
                } else {
                    activeBuilds.remove(bj);
                }
            }
        }
    }

    public boolean isBuildActive(int id) {
        for (int i = 0; i < activeBuilds.size(); i++) {
            BuildJob bj = activeBuilds.get(i);
            if (bj.id == id) {
                return true;
            }
        }

        return false;
    }

    public boolean isBuildActive(Build build) {
        for (int i = 0; i < activeBuilds.size(); i++) {
            BuildJob bj = activeBuilds.get(i);
            if (bj.build == build) {
                return true;
            }
        }

        return false;
    }

    public int newBuild(int x, int y, int z, String name) {
        int buildID = nextBuildID++;

        BuildJob newBuild = new BuildJob(buildID, x, y, z, name);
        addBuild(newBuild);

        return buildID;
    }

    public int newBuild(Build build) {
        int buildID = nextBuildID++;

        BuildJob newBuild = new BuildJob(buildID, build);
        addBuild(newBuild);

        return buildID;
    }

    public void addBuild(BuildJob buildJob) {
        activeBuilds.add(buildJob);
        buildJob.buildStart();
        //return -1;
    }

    public void addBuild(BuildJob build, int x, int y, int z) {
        activeBuilds.add(build);
        build.buildStart();
        //return -1;
    }

    //Actual building functions

    public void updateBuildProgress(BuildJob buildJob) {

        Build build = buildJob.build;

        buildJob.build_currentTick++;

        World worldRef = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(buildJob.build.dim);

        if (buildJob.timeout > 0) {
            buildJob.timeout--;
            Build b = buildJob.build;

            worldRef.markBlockRangeForRenderUpdate(buildJob.build_startX, buildJob.build_startY, buildJob.build_startZ, buildJob.build_startX + b.map_sizeX, buildJob.build_startY + b.map_sizeY, buildJob.build_startZ + b.map_sizeZ);
            //worldRef.markBlocksDirty(b.map_coord_minX, b.map_coord_minY, b.map_coord_minZ, b.map_coord_minX+b.map_sizeX, b.map_coord_minY+b.map_sizeY, b.map_coord_minZ+b.map_sizeZ);
            return;
        }

        build_rate = 100000;

        build_rate = 25000;

        build_rate = 1000;

        build_rate = buildJob.build_rate;


        //World worldRef = mod_ZombieCraft.worldRef;

        //if first pass mass AIR set is skipped, make sure the air from the schematics are printed, makes all mods happy
        boolean replaceAir = !buildJob.useFirstPass;

        if (buildJob.neverPlaceAir) replaceAir = false;

        int loopCount;
        Block id = null;
        //worldRef.editingBlocks = true;
        buildJob.curLayerCountMax = build.map_sizeX * build.map_sizeZ;
        //System.out.println("rand?: " + doRandomBuild + " build layer " + curLayerCount + " / " + curLayerCountMax + " | " + ((float)curLayerCount / (float)curLayerCountMax));

        buildJob.doRandomBuild = false;
        //build_rate = 50;
        //build_rand = 20;

        try {
            //seriously outdated mode, needs to be brought up to spec with various fixes and format updates
            if (buildJob.doRandomBuild) {
                if (worldRef.rand.nextInt(build_rand) != 0) return;
                boolean first = true;
                int tryCount = 0;
                while ((first || isAir(id)) && tryCount < 300) {
                    tryCount++;
                    first = false;
                    buildJob.build_loopTickX = worldRef.rand.nextInt(build.map_sizeX);
                    buildJob.build_loopTickZ = worldRef.rand.nextInt(build.map_sizeZ);

                    //stop random when 60% built
                    if (((float) buildJob.curLayerCount / (float) buildJob.curLayerCountMax) > 0.9F) {
                        buildJob.doRandomBuild = false;
                        buildJob.build_loopTickX = 0;
                        buildJob.build_loopTickZ = 0;
                        return;
                    }


                    if (!buildJob.build_blockPlaced[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ]) {

                        //try {
                        id = build.build_blockIDArr[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ];

                        //damn you mcedit
                        //if (id < 0) id += 4096;

                        int xx = buildJob.build_startX + buildJob.build_loopTickX;
                        int yy = buildJob.build_startY + buildJob.build_loopTickY;
                        int zz = buildJob.build_startZ + buildJob.build_loopTickZ;


                        //if (id != 0) {
                        //worldRef.setBlockAndMetadata(xx, yy, zz, 0, 0);
                        //worldRef.removeBlockTileEntity(xx, yy, zz);
                        //worldRef.setBlockAndMetadata(build_startX+build_loopTickX, build_startY+build_loopTickY, build_startZ+build_loopTickZ, 0, 0);

                        int meta = build.build_blockMetaArr[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ];
                        IBlockState state = id.getStateFromMeta(meta);
                        worldRef.setBlockState(new BlockPos(xx, yy, zz), state, buildJob.notifyFlag);
                        //worldRef.markBlockNeedsUpdate(xx, yy, zz);
                        buildJob.build_blockPlaced[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ] = true;
                        buildJob.curLayerCount++;
                        //if (id != 0) {
                        //buildParticles(xx,yy,zz);
                        //}
                        //}
                        /*} catch (Exception ex) {
				    		ex.printStackTrace();
				    		worldRef.editingBlocks = false;
				    		build_active = false;
				    		return;
				    	}*/
                    }
                }
            } else {


                for (loopCount = 0; loopCount < build_rate; loopCount++) {


                    if (buildJob.build_loopTickX >= build.map_sizeX) {
                        buildJob.build_loopTickX = 0;
                        buildJob.build_loopTickZ++;
                    }
                    if (buildJob.build_loopTickZ >= build.map_sizeZ) {
                        buildJob.build_loopTickZ = 0;
                        buildJob.build_loopTickY++;
                        buildJob.curLayerCount = 0;
                        //buildJob.doRandomBuild = true;

                    }

                    if (buildJob.build_loopTickY >= build.map_sizeY) {
                        //done
                        if (buildJob.pass == 2) {
                            buildComplete(buildJob);
                            buildJob.buildComplete();
                            if (buildJob.customGenCallback != null)
                                buildJob.customGenCallback.genPassPre(worldRef, buildJob, -1);
                        } else {
                            buildJob.pass++;
                            buildJob.build_loopTickX = 0;
                            buildJob.build_loopTickZ = 0;
                            buildJob.build_loopTickY = 0;

                            buildJob.timeout = 5;

                            if (buildJob.customGenCallback != null)
                                buildJob.customGenCallback.genPassPre(worldRef, buildJob, buildJob.pass);

                            if (buildJob.pass == 1) {
                                //System.out.println("Map size: " + build.map_sizeX + " - " + build.map_sizeY + " - " + build.map_sizeZ);
                                //System.out.println("Starting Build Pass, sys time: " + System.currentTimeMillis());
                            }
                        }
                        //worldRef.editingBlocks = false;
                        return;
                    }

                    buildJob.curTick++;// = buildJob.build_loopTickX + ((buildJob.build_loopTickY + 1) * (buildJob.build_loopTickZ + 1));
			    	
			    	/*try {
				    	float percent = ((float)buildJob.curTick + 1) / ((float)buildJob.maxTicks) * 100F;
						System.out.println(buildJob.id + " - build percent: " + percent + " " + buildJob.curTick + " / " + buildJob.maxTicks + " build ref: " + build);
			    	} catch (Exception ex) {
			    		
			    	}*/

                    if (buildJob.pass == 0) {
                        int xx = buildJob.build_startX + buildJob.build_loopTickX;
                        int yy = buildJob.build_startY + buildJob.build_loopTickY;
                        int zz = buildJob.build_startZ + buildJob.build_loopTickZ;

                        id = build.build_blockIDArr[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ];

                        boolean skipGen = false;


                        //if (id != 0) {
                        //if (worldRef.getBlockId(xx, yy, zz) != 0) { // its quicker to set then to check, mostly
                        //worldRef.removeBlockTileEntity(xx, yy, zz);
                        BlockPos coords = new BlockPos(xx, yy, zz);

                        if (buildJob.useRotationBuild) {
                            if (buildJob.build.backwardsCompatibleOldRotate) {
                                coords = rotate(coords, buildJob.direction,
                                        new Vec3d(buildJob.build_startX, buildJob.build_startY, buildJob.build_startZ),
                                        new Vec3d(build.map_sizeX, build.map_sizeY, build.map_sizeZ));
                            } else {
                                coords = rotateNew(coords, buildJob.direction,
                                        new Vec3d(buildJob.build_startX, buildJob.build_startY, buildJob.build_startZ),
                                        new Vec3d(build.map_sizeX, build.map_sizeY, build.map_sizeZ));
                            }

                        } else {
                            //boolean centerBuild = true;
                            if (buildJob.centerBuildIfNoRotate) {
                                //this was determined to work for odd sized schematics on X and Z
                                coords.add(-MathHelper.floor(build.map_sizeX / 2D), 0, -MathHelper.floor(build.map_sizeZ / 2D));
		    					/*coords.getX() -= MathHelper.floor(build.map_sizeX/2D);
		    					coords.getZ() -= MathHelper.floor(build.map_sizeZ/2D);*/
                                //coords.getZ() += 1; //solve innacuracy for just Z
                            }
                        }
                        if (buildJob.blockIDsNoBuildOver.size() > 0) {
                            Block checkCoord = worldRef.getBlockState(coords).getBlock();
                            if (buildJob.blockIDsNoBuildOver.contains(checkCoord)) {
                                skipGen = true;
                            }
                        }
                        if (!skipGen) {
                            worldRef.setBlockState(coords, Blocks.AIR.getDefaultState(), buildJob.notifyFlag);
                        }
                        //}


                    } else {

                        //TEEEEEMMMMMPPPPPPPP
                        //build_rate = 100000;

                        boolean tropiFixTemp = false;

                        if (!buildJob.build_blockPlaced[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ]) {

                            //try {
                            id = build.build_blockIDArr[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ];
                            int meta = build.build_blockMetaArr[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ];
                            //damn you mcedit ... ?
                            //if (id < 0) id += 4096;
                            //done elsewhere now
					    		/*if (!build.newFormat) {
					    			if (id < 0) id += 256;
					    		} else {
					    			if (id < 0) id += 4096;
					    		}*/

                            int xx = buildJob.build_startX + buildJob.build_loopTickX;
                            int yy = buildJob.build_startY + buildJob.build_loopTickY;
                            int zz = buildJob.build_startZ + buildJob.build_loopTickZ;

                            //if (id != worldRef.getBlockId(par1, par2, par3))
                            boolean skip = false;
                            //pass 1 should be structure pass, pass 2 is redstone pass
                            if (buildJob.pass == 1 && buildJob.blockIDsSkipFirstPass.contains(id)) {
                                skip = true;
                            }
                            //System.out.println(buildJob.build_loopTickX + ", " + buildJob.build_loopTickY + ", " + buildJob.build_loopTickZ + " - " + id);
                            if (!skip && ((replaceAir || !isAir(id))/* && (id != worldRef.getBlockId(xx, yy, zz) || meta != worldRef.getBlockMetadata(xx, yy, zz))*/)) {
                                //if (worldRef.getBlockTileEntity(xx, yy, zz) != null/* || (id != 0 && Block.blocksList[id].blockID == Block.chest.blockID)*/) {
                                //worldRef.removeBlockTileEntity(xx, yy, zz);
                                //worldRef.setBlockAndMetadata(xx, yy, zz, 0, 0);
                                //break;
                                //}
                                //}
                                //worldRef.setBlockAndMetadata(build_startX+build_loopTickX, build_startY+build_loopTickY, build_startZ+build_loopTickZ, 0, 0);
                                //System.out.println("newFormat: " + build.newFormat);
                                BlockPos coords = new BlockPos(xx, yy, zz);
                                //System.out.println("printing: " + id + ", preMeta: " + meta);
                                if (buildJob.useRotationBuild/* && buildJob.direction != 0*/) {
                                    //System.out.println("using new rotate");
                                    if (buildJob.build.backwardsCompatibleOldRotate) {
                                        coords = rotate(coords, buildJob.direction,
                                                new Vec3d(buildJob.build_startX, buildJob.build_startY, buildJob.build_startZ),
                                                new Vec3d(build.map_sizeX, build.map_sizeY, build.map_sizeZ));
                                    } else {
                                        coords = rotateNew(coords, buildJob.direction,
                                                new Vec3d(buildJob.build_startX, buildJob.build_startY, buildJob.build_startZ),
                                                new Vec3d(build.map_sizeX, build.map_sizeY, build.map_sizeZ));
                                    }

                                    if (tropiFixTemp) {
                                        if (id == BlockRegistry.bundles) {
                                            //meta = 0;
                                            IBlockState state = id.getDefaultState().withProperty(BlockBundle.BUNDLE_AXIS, BlockLog.EnumAxis.Y);
                                            meta = id.getMetaFromState(state);
                                        } else if (id == BlockRegistry.logs) {
                                            IBlockState state = id.getDefaultState().withProperty(BlockTropicraftLog.VARIANT, TropicraftLogs.PALM);
                                            meta = id.getMetaFromState(state);
                                        }

                                    } else {
                                        int tryMeta = rotateMeta(worldRef, coords, buildJob.rotation, id, meta);
                                        if (tryMeta != -1) meta = tryMeta;
                                    }
                                } else {
                                    //still center around coord if not rotated
                                    //boolean centerBuild = true;
                                    if (buildJob.centerBuildIfNoRotate) {
                                        //this was determined to work for odd sized schematics on X and Z
                                        coords.add(-MathHelper.floor(build.map_sizeX / 2D), 0, -MathHelper.floor(build.map_sizeZ / 2D));
                                        //coords.getZ() += 1; //solve innacuracy for just Z
                                    }
                                }

                                //custom id fixing
                                //if (id == 98) id = 4;

                                //new protection against schematics printing missing ids that will eventually crash the game
                                if (id != null || isAir(id)) {
                                    boolean skipGen = false;
                                    if (buildJob.blockIDsNoBuildOver.size() > 0) {
                                        Block checkCoord = worldRef.getBlockState(coords).getBlock();
                                        if (buildJob.blockIDsNoBuildOver.contains(checkCoord)) {
                                            skipGen = true;
                                        }
                                    }
                                    if (!skipGen) {
                                        if (false && tropiFixTemp) {
                                            /*IBlockState state = id.getStateFromMeta(meta);
                                            worldRef.setBlockState(coords, state, 2);*/
                                            if (!isAir(id)) {
                                                IBlockState state = id.getStateFromMeta(meta);
                                                worldRef.setBlockState(coords, placeholderID.getDefaultState(), 2);
                                            }
                                        } else {
                                            IBlockState state = id.getStateFromMeta(meta);
                                            worldRef.setBlockState(coords, state, 2);
                                        }
                                        //worldRef.setBlock(coords.getX(), coords.getY(), coords.getZ(), id, meta, 2);
                                    }
                                    //System.out.println("post print - " + coords.getX() + " - " + coords.getZ());
					    				/*if (buildJob.customGenCallback != null) {
					    					NBTTagCompound nbt = buildJob.customGenCallback.getInitNBTTileEntity();
					    					if (nbt != null) {
					    						TileEntity te = worldRef.getBlockTileEntity(coords.getX(), coords.getY(), coords.getZ());
					    						if (te instanceof ITileEntityCustomGenData) {
					    							((ITileEntityCustomGenData) te).initWithNBT(nbt);
					    						}
					    					}
					    				}*/
                                } else {
                                    System.out.println("BUILDMOD SEVERE: schematic contains non existant blockID: " + id + ", replacing with blockID: " + placeholderID);
                                    boolean skipGen = false;
                                    if (buildJob.blockIDsNoBuildOver.size() > 0) {
                                        Block checkCoord = worldRef.getBlockState(coords).getBlock();
                                        if (buildJob.blockIDsNoBuildOver.contains(checkCoord)) {
                                            skipGen = true;
                                        }
                                    }
                                    if (!skipGen) {
                                        worldRef.setBlockState(coords, placeholderID.getDefaultState(), buildJob.notifyFlag);
                                    }
                                }
					    			/*if (id != 0) {
					    				boolean returnVal = Block.blocksList[id].rotateBlock(worldRef, coords.getX(), coords.getY(), coords.getZ(), ForgeDirection.EAST);
					    				if (id == Block.torchWood.blockID) System.out.println("returnVal " + returnVal);
					    			}*/

                                //worldRef.setBlock(xx, yy, zz, id);
                                //worldRef.setBlockMetadata(xx, yy, zz, meta);


                                buildJob.build_blockPlaced[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ] = true;
                                buildJob.curLayerCount++;
                            } else {
                                loopCount--;
                            }
					    	/*} catch (Exception ex) {
					    		ex.printStackTrace();
					    		worldRef.editingBlocks = false;
					    		build_active = false;
					    		return;
					    	}*/
                        }
                    }
                    buildJob.build_loopTickX++;
                    //if (id == 0) loopCount--;
                }
            }
        } catch (Exception ex) {
            buildJob.build_active = false;
            ex.printStackTrace();
        }
        //worldRef.editingBlocks = false;
    }

    public static void rotateSet(BuildJob parBuildJob, BlockPos coords, Block id, int meta) {

        coords = rotate(coords, parBuildJob.direction,
                new Vec3d(parBuildJob.build_startX, parBuildJob.build_startY, parBuildJob.build_startZ),
                new Vec3d(parBuildJob.build.map_sizeX, parBuildJob.build.map_sizeY, parBuildJob.build.map_sizeZ));
        World world = DimensionManager.getWorld(parBuildJob.build.dim);
        if (world != null) {
            world.setBlockState(coords, id.getDefaultState(), 2);
        }
    }

    /* coords: unrotated world coord, rotation: quantify to 90, start: uncentered world coords for structure start point, size: structure size */
    public static BlockPos rotate(BlockPos coords, int direction, Vec3d start, Vec3d size) {
        double rotation = (direction * 90) + 180;
        double centerX = start.xCoord + (size.xCoord / 2D);
        double centerZ = start.zCoord + (size.zCoord / 2D);
        double vecX = coords.getX() - centerX + 0.05; //+0.05 fixes the 0 angle distance calculation issue
        double vecZ = coords.getZ() - centerZ + 0.05;
        double distToCenter = Math.sqrt(vecX * vecX + vecZ * vecZ);
        double rotYaw = (float) (Math.atan2(vecZ, vecX) * 180.0D / Math.PI) - rotation;
        double newX = start.xCoord + Math.cos(rotYaw * 0.017453D) * distToCenter;
        double newZ = start.zCoord + Math.sin(rotYaw * 0.017453D) * distToCenter;

        //fix some bad centering rotations
        if (direction == 1) {
            newZ++;
        } else if (direction == 2) {
            newX++;
            newZ++;
        } else if (direction == 3) {
            newX++;
        }

        return new BlockPos((int) Math.floor(newX), coords.getY(), (int) Math.floor(newZ));
    }

    /* coords: unrotated world coord, rotation: quantify to 90, start: uncentered world coords for structure start point, size: structure size */
    public static BlockPos rotateNew(BlockPos coords, int direction, Vec3d start, Vec3d size) {

        //System.out.println("direction: " + direction);

        //center is not offsetted
        //coords should be offset 0.5 before rotate math, guarantees no strange offset issues, flooring cleans it up afterwards perfectly

        double rotation = (direction * Math.PI / 2D);
        double centerX = start.xCoord + (size.xCoord / 2D);
        double centerZ = start.zCoord + (size.zCoord / 2D);
        double vecX = (coords.getX() + 0.5D) - centerX;
        double vecZ = (coords.getZ() + 0.5D) - centerZ;

        Vec3d vec = new Vec3d(vecX, 0, vecZ);
        vec = vec.rotateYaw((float) rotation);
        return new BlockPos((int) Math.floor(start.xCoord + vec.xCoord), coords.getY(), (int) Math.floor(start.zCoord + vec.zCoord));
    }

    public int rotateMeta(World par1World, BlockPos coords, double rotation, Block id, int meta) {

        //CHECK OUT  RotationHelper.metadataToDirection and RotationHelper.rotateMetadata !!!!!!!

        //was this method updated to match rotateNew's ways? i dont think it was which might be why things mismatch now for EVERY rotation

        int dir = MathHelper.floor((double) (rotation * 4.0F / 360.0F) + 0.5D) & 3;

        dir = (((int) rotation) / 90) & 3;

        if (dir == 0) return meta;

        Block block = id;//Block.blocksList[id];

        if (block instanceof BlockStairs) {

            int rotateMeta = meta & 4;

            //System.out.println("rotation: " + rotation + ", dir: " + dir + ", meta: " + meta + ", rotateMeta: " + rotateMeta);

            int fMeta = -1;

	        /*if (dir == 0) fMeta = 0;// | i1;
	        if (dir == 1) fMeta = 3;// | i1;
	        if (dir == 2) fMeta = 1;// | i1;
	        if (dir == 3) fMeta = 2;// | i1;
*/
            if (dir == 0) {
                //do nothing, assumed proper
            } else if (dir == 1) {
                if (meta == 0) {
                    fMeta = 3;
                } else if (meta == 1) {
                    fMeta = 2;
                } else if (meta == 2) {
                    fMeta = 0;
                } else if (meta == 3) {
                    fMeta = 1;
                }
            } else if (dir == 2) {
                if (meta == 0) {
                    fMeta = 1;
                } else if (meta == 1) {
                    fMeta = 0;
                } else if (meta == 2) {
                    fMeta = 3;
                } else if (meta == 3) {
                    fMeta = 2;
                }
            } else if (dir == 3) {
                if (meta == 0) {
                    fMeta = 2;
                } else if (meta == 1) {
                    fMeta = 3;
                } else if (meta == 2) {
                    fMeta = 1;
                } else if (meta == 3) {
                    fMeta = 0;
                }
            }

            //System.out.println("fMeta: " + fMeta);

            return fMeta | rotateMeta;
        } else if (block instanceof BlockLadder) {
            IBlockState state = block.getStateFromMeta(meta);
            EnumFacing facing = state.getValue(BlockLadder.FACING);
            for (int i = 0; i < dir; i++) {
                facing = facing.rotateYCCW();
            }
            state = state.withProperty(BlockLadder.FACING, facing);
            meta = block.getMetaFromState(state);
            return meta;
            //EnumFacing facing = state.getProperties().get(BlockLadder.FACING);
        }

        //failed
        return -1;
    }

    public void buildComplete(BuildJob buildJob) {

        //System.out.println("Build complete, sys time: " + System.currentTimeMillis());

        spawnLevelEntities(buildJob);


        //ZCGame.instance.wMan.levelRegeneratedCallback();
    }

    public void spawnLevelEntities(BuildJob buildJob) {
        //NBTTagList var14 = entities.getTagList("Entities");

        Build build = buildJob.build;

        World worldRef = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(build.dim);

        /*List ents = worldRef.loadedEntityList;

        for (int i = 0; i < ents.size(); i++) {
            Entity ent = (Entity) ents.get(i);

            //Kill all items, a lazy solution for level cleanup for now
            if (ent instanceof EntityItem) {
                ent.setDead();
            }
        }*/

        if (build.entities != null) {
            for (int var17 = 0; var17 < build.entities.tagCount(); ++var17) {
                NBTTagCompound var16 = (NBTTagCompound) build.entities.getCompoundTagAt(var17);
                Entity var18 = EntityList.createEntityFromNBT(var16, worldRef);
                /*var5.hasEntities = true;

                if (var18 != null)
                {
                    var5.addEntity(var18);
                }*/
            }
        }

        //NBTTagList var15 = par2NBTTagCompound.getTagList("TileEntities");

        if (build.tileEntities != null) {
            for (int var21 = 0; var21 < build.tileEntities.tagCount(); ++var21) {
                //this has been recently changed, make sure it still works with new "get existing tile entity, give it the nbt and fix coords"
                //it fixed (still does) the loaded tile entities coords that went from abs -> rel on writeout and reversed on read
                //verified that this loads ZC tile nbt ok
                NBTTagCompound var20 = (NBTTagCompound) build.tileEntities.getCompoundTagAt(var21);
                //TileEntity var13 = TileEntity.createAndLoadEntity(var20);
                TileEntity var13 = worldRef.getTileEntity(new BlockPos(build.map_coord_minX + var20.getInteger("x"), buildJob.build_startY + var20.getInteger("y"), build.map_coord_minZ + var20.getInteger("z")));

                if (var13 instanceof SchematicData) {
                    ((SchematicData) var13).readFromNBT(var20, build);
                } else if (var13 != null) {
                    var13.readFromNBT(var20);
                }

                if (buildJob.customGenCallback != null) {
                    NBTTagCompound nbt = buildJob.customGenCallback.getInitNBTTileEntity();
                    if (nbt != null) {
                        if (var13 instanceof ITileEntityCustomGenData) {
                            ((ITileEntityCustomGenData) var13).initWithNBT(nbt);
                        }
                    }
                }

                if (var13 != null) {

                    //warning, some tile entities might not have written out with relative coords properly, implement something to tell what tile entities have had relative coords set on them
                    //once stuff using new marking is rescanned/saved, uncomment this if statement
                    //is there actually any harm in always setting this though?
                    //if (!build.newFormat || (var20.hasKey("coordsSetRelative"))) {
                    var13.setPos(new BlockPos(build.map_coord_minX + var13.getPos().getX(),
                            buildJob.build_startY + var13.getPos().getY(),
                            build.map_coord_minZ + var13.getPos().getZ()));
						/*var13.xCoord = build.map_coord_minX+var13.xCoord;
		                var13.yCoord = buildJob.build_startY+var13.yCoord;
		                var13.zCoord = build.map_coord_minZ+var13.zCoord;*/
                    //}

                    try {
                        Packet packet = var13.getUpdatePacket();
                        if (packet != null) {
                            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendPacketToAllPlayers(packet);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    //what is this crap!, GET the tile entity and
                    //worldRef.removeBlockTileEntity(var13.xCoord, var13.yCoord, var13.zCoord);
                    //worldRef.setBlockTileEntity(var13.xCoord, var13.yCoord, var13.zCoord, var13);

                    //worldRef.loadedTileEntityList.add(var13);
                }
                
                /*if (var13 != null)
                {
                    var5.addTileEntity(var13);
                }*/
            }
        }
    }

    public static boolean isAir(Block parBlock) {
        //cant get meta/state without small redesign atm
        return parBlock == Blocks.AIR;
		/*Material mat = parBlock.getMaterial();
		if (mat == Material.AIR) {
			return true;
		} else {
			return false;
		}*/
    }

}
